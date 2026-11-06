package com.example.yaza_wanga.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.Course
import com.example.yaza_wanga.data.model.Material
import com.example.yaza_wanga.data.model.Progress
import com.example.yaza_wanga.util.CourseProgressCalculation
import com.example.yaza_wanga.util.ProgressMath
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

data class ProgressUiState(
    val courseCalculations: List<CourseProgressCalculation> = emptyList(),
    val hasEnrollments: Boolean = true,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class ProgressViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ProgressUiState())
    val uiState: StateFlow<ProgressUiState> = _uiState.asStateFlow()

    private var currentStudentId: String? = null

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                withTimeout(15_000L) {
                    val userRes = AppContainer.authRepository.fetchCurrentUser()
                    val user = userRes.getOrNull()
                    if (user == null) {
                        val err = userRes.exceptionOrNull()?.localizedMessage ?: "Could not load user profile"
                        _uiState.update { it.copy(isLoading = false, errorMessage = err) }
                        return@withTimeout
                    }

                    currentStudentId = user.uid

                    val enrollmentsRes = AppContainer.enrollmentRepository.getEnrollmentsForStudent(user.uid)
                    val enrollments = enrollmentsRes.getOrElse { emptyList() }
                    val courseCodes = enrollments.map { it.courseCode }

                    if (courseCodes.isEmpty()) {
                        _uiState.update {
                            it.copy(
                                courseCalculations = emptyList(),
                                hasEnrollments = false,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                        return@withTimeout
                    }

                    // Pre-fetch course details for course titles
                    val allCourses = AppContainer.courseRepository.getCourses(includeDrafts = false).getOrElse { emptyList() }
                    val courseTitleMap = allCourses.associateBy({ it.code }, { it.title })

                    val materialsFlows: List<Flow<Result<List<Material>>>> = courseCodes.map { code ->
                        AppContainer.materialRepository.observeMaterialsForCourse(code)
                    }

                    val progressFlow = AppContainer.progressRepository.observeProgressForStudent(user.uid)

                    combine(
                        combine(materialsFlows) { array -> array.toList() },
                        progressFlow
                    ) { materialsResults, progressResult ->
                        val progressList = progressResult.getOrElse { emptyList() }
                        val progressMap = progressList.associateBy { it.courseCode }

                        val calculations = mutableListOf<CourseProgressCalculation>()

                        courseCodes.forEachIndexed { idx, code ->
                            val materialsRes = materialsResults.getOrNull(idx)
                            val materials = materialsRes?.getOrElse { emptyList() } ?: emptyList()
                            val permitted = materials.filter { m ->
                                m.status.equals("APPROVED", ignoreCase = true) &&
                                        !m.access.equals("RESTRICTED", ignoreCase = true)
                            }

                            val prog = progressMap[code] ?: Progress(studentId = user.uid, courseCode = code)
                            val title = courseTitleMap[code] ?: code

                            val calc = ProgressMath.calculate(
                                courseCode = code,
                                courseTitle = title,
                                permittedMaterials = permitted,
                                completedTopicsFromProgress = prog.completedTopics,
                                viewedMaterialIdsFromProgress = prog.viewedMaterialIds,
                                attendedSessionIdsFromProgress = prog.attendedSessionIds
                            )
                            calculations.add(calc)
                        }

                        _uiState.update {
                            it.copy(
                                courseCalculations = calculations,
                                hasEnrollments = true,
                                isLoading = false,
                                errorMessage = null
                            )
                        }
                    }.collect {}
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

    fun toggleTopic(courseCode: String, topic: String) {
        val studentId = currentStudentId ?: return
        viewModelScope.launch {
            val calc = _uiState.value.courseCalculations.find { it.courseCode == courseCode }
            val currentTopicState = calc?.topics?.find { it.topicTag == topic }
            val isCurrentlyCompleted = currentTopicState?.isCompleted ?: false
            val newCompletedState = !isCurrentlyCompleted

            AppContainer.progressRepository.setTopicCompleted(
                studentId = studentId,
                courseCode = courseCode,
                topic = topic,
                completed = newCompletedState
            )
        }
    }

    fun retry() {
        loadData()
    }
}
