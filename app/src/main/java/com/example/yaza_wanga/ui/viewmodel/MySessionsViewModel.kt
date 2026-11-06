package com.example.yaza_wanga.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.Session
import com.example.yaza_wanga.validation.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SessionItem(
    val session: Session,
    val tutorName: String
)

data class MySessionsUiState(
    val upcomingSessions: List<SessionItem> = emptyList(),
    val pastSessions: List<SessionItem> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class MySessionsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MySessionsUiState())
    val uiState: StateFlow<MySessionsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val userRes = AppContainer.authRepository.fetchCurrentUser()
                val user = userRes.getOrNull()
                if (user == null) {
                    val err = userRes.exceptionOrNull()?.localizedMessage ?: "Could not load user profile"
                    _uiState.update { it.copy(isLoading = false, errorMessage = err) }
                    return@launch
                }

                val enrollmentsRes = AppContainer.enrollmentRepository.getEnrollmentsForStudent(user.uid)
                val enrollments = enrollmentsRes.getOrElse { emptyList() }
                val courseCodes = enrollments.map { it.courseCode }

                if (courseCodes.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            upcomingSessions = emptyList(),
                            pastSessions = emptyList(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    return@launch
                }

                // Pre-fetch tutors for distinct tutorIds (no Firestore calls inside snapshot listener)
                val allTutors = AppContainer.tutorRepository.getTutorsForCourse("").getOrElse { emptyList() }
                val tutorMap = allTutors.associateBy({ it.tutorId }, { it.name })

                AppContainer.sessionRepository.observeSessionsForCourses(courseCodes).collect { res ->
                    if (res.isFailure) {
                        val err = res.exceptionOrNull()?.localizedMessage ?: "Could not load sessions"
                        _uiState.update { it.copy(isLoading = false, errorMessage = err) }
                        return@collect
                    }

                    val allSessions = res.getOrDefault(emptyList())
                    val validSessions = allSessions.filter {
                        it.status == "SCHEDULED" || it.status == "ACCEPTED" || it.status == "COMPLETED"
                    }

                    val todayStart = Validator.getStartOfTodayMillis()

                    val upcoming = mutableListOf<SessionItem>()
                    val past = mutableListOf<SessionItem>()

                    for (s in validSessions) {
                        val tutorName = tutorMap[s.tutorId]?.ifBlank { "Peer Tutor" } ?: "Peer Tutor"
                        val item = SessionItem(session = s, tutorName = tutorName)

                        if (s.dateMillis >= todayStart && s.status != "COMPLETED") {
                            upcoming.add(item)
                        } else {
                            past.add(item)
                        }
                    }

                    upcoming.sortWith(compareBy<SessionItem> { it.session.dateMillis }.thenBy { it.session.time })
                    past.sortWith(compareBy<SessionItem> { it.session.dateMillis }.thenBy { it.session.time })

                    _uiState.update {
                        it.copy(
                            upcomingSessions = upcoming,
                            pastSessions = past,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Could not reach the server. Check your connection and try again"
                    )
                }
            }
        }
    }

    fun markSessionAttended(courseCode: String, sessionId: String) {
        viewModelScope.launch {
            try {
                val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
                if (user != null) {
                    AppContainer.progressRepository.markSessionAttended(user.uid, courseCode, sessionId)
                }
            } catch (_: Throwable) {}
        }
    }

    fun retry() {
        loadData()
    }
}
