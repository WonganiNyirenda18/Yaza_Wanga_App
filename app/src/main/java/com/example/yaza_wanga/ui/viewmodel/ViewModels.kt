package com.example.yaza_wanga.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.*
import com.example.yaza_wanga.validation.Validator
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

data class AppSessionUiState(
    val user: User? = null,
    val isLoading: Boolean = true,
    val isSignedOut: Boolean = false,
    val error: String? = null
)

class AppSessionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppSessionUiState())
    val uiState: StateFlow<AppSessionUiState> = _uiState.asStateFlow()

    init {
        observeSession()
    }

    fun observeSession() {
        viewModelScope.launch {
            AppContainer.authRepository.currentUserFlow.collect { user ->
                if (user != null) {
                    if (user.status == UserStatus.SUSPENDED || user.status == UserStatus.REJECTED) {
                        AppContainer.authRepository.signOut()
                        _uiState.update {
                            it.copy(
                                user = null,
                                isSignedOut = true,
                                error = "Your account has been suspended. Contact the administrator",
                                isLoading = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                user = user,
                                isSignedOut = false,
                                error = null,
                                isLoading = false
                            )
                        }
                    }
                } else {
                    _uiState.update {
                        it.copy(
                            user = null,
                            isSignedOut = false,
                            error = null,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun signOut() {
        viewModelScope.launch {
            AppContainer.authRepository.signOut()
            _uiState.update {
                AppSessionUiState(user = null, isLoading = false, isSignedOut = true)
            }
        }
    }
}

data class SplashUiState(val isLoading: Boolean = true)
class SplashViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()
}

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val infoMessage: String? = null
)
class SignInViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState: StateFlow<SignInUiState> = _uiState.asStateFlow()

    fun signIn(email: String, pass: String, onNavigate: (UserRole) -> Unit, onError: (String?) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = AppContainer.authRepository.signIn(email, pass)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                val user = result.getOrNull()
                if (user != null) {
                    onNavigate(user.role)
                }
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Invalid email or password"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }

    fun forgotPassword(email: String, onError: (String?) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, infoMessage = null) }
            val result = AppContainer.authRepository.sendPasswordResetEmail(email)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                _uiState.update { it.copy(infoMessage = "Password reset email sent") }
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Failed to send reset email"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }
}

data class SignUpUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class SignUpViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState: StateFlow<SignUpUiState> = _uiState.asStateFlow()

    fun signUpStudent(data: Map<String, String>, onError: (String?) -> Unit, onComplete: () -> Unit) {
        val name = data["fullName"] ?: "Student"
        val email = data["email"] ?: ""
        val password = data["password"] ?: ""
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = AppContainer.authRepository.signUp(email, password, UserRole.STUDENT, name)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onComplete()
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Sign up failed"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }

    fun signUpTutor(data: Map<String, String>, onError: (String?) -> Unit, onComplete: () -> Unit) {
        val name = data["fullName"] ?: "Tutor"
        val email = data["email"] ?: ""
        val password = data["password"] ?: ""
        val faculty = data["faculty"]
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = AppContainer.authRepository.signUp(email, password, UserRole.TUTOR, name, faculty = faculty)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onComplete()
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Sign up failed"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }
}

data class StudentProfileUiState(
    val user: User? = null,
    val isLoading: Boolean = false
)
class StudentProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StudentProfileUiState())
    val uiState: StateFlow<StudentProfileUiState> = _uiState.asStateFlow()

    init {
        loadUser()
    }

    fun loadUser() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = AppContainer.authRepository.fetchCurrentUser()
            _uiState.update { it.copy(user = result.getOrNull(), isLoading = false) }
        }
    }

    fun saveProfile(data: Map<String, String>, onError: (String?) -> Unit, onComplete: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            if (user == null) {
                _uiState.update { it.copy(isLoading = false) }
                onError("Please sign in")
                return@launch
            }
            val department = data["department"] ?: ""
            val programme = data["programme"] ?: ""
            val yearOfStudy = data["yearOfStudy"] ?: ""
            val phone = data["phone"] ?: ""

            val updatedUser = user.copy(
                department = department,
                programme = programme,
                yearOfStudy = yearOfStudy,
                phone = phone
            )

            val result = AppContainer.userRepository.updateUser(updatedUser)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onComplete()
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Failed to save profile"
                onError(msg)
            }
        }
    }
}

data class TutorProfileUiState(
    val profile: TutorProfile? = null,
    val availableCourses: List<Course> = emptyList(),
    val selectedCourses: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class TutorProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorProfileUiState())
    val uiState: StateFlow<TutorProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfileAndCourses()
    }

    fun loadProfileAndCourses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            val courses = AppContainer.courseRepository.getCourses().getOrElse { emptyList() }
            var profile: TutorProfile? = null
            if (user != null) {
                profile = AppContainer.tutorRepository.getTutorProfile(user.uid).getOrNull()
            }
            _uiState.update {
                it.copy(
                    profile = profile,
                    availableCourses = courses,
                    selectedCourses = profile?.courses?.toSet() ?: emptySet(),
                    isLoading = false
                )
            }
        }
    }

    fun toggleCourseSelection(courseCode: String) {
        _uiState.update { state ->
            val set = state.selectedCourses.toMutableSet()
            if (set.contains(courseCode)) set.remove(courseCode) else set.add(courseCode)
            state.copy(selectedCourses = set)
        }
    }

    fun saveProfile(
        bio: String,
        rateStr: String,
        teachingMethod: String,
        phone: String,
        whatsapp: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            if (user == null) {
                onError("Please sign in")
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }
            val hourlyRate = rateStr.toDoubleOrNull() ?: 0.0
            val existingProfile = AppContainer.tutorRepository.getTutorProfile(user.uid).getOrNull()
            val status = if (existingProfile?.status == "VERIFIED") "VERIFIED" else "PENDING"

            val profile = TutorProfile(
                tutorId = user.uid,
                name = user.name.ifBlank { user.email.substringBefore("@") },
                bio = bio,
                hourlyRate = hourlyRate,
                courses = _uiState.value.selectedCourses.toList(),
                rating = existingProfile?.rating ?: 5.0,
                reviewCount = existingProfile?.reviewCount ?: 0,
                status = status,
                phone = phone,
                whatsapp = whatsapp.ifBlank { phone },
                teachingMethod = teachingMethod
            )

            val result = AppContainer.tutorRepository.saveTutorProfile(profile)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onSuccess()
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Failed to save tutor profile"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }
}

data class StudentDashboardUiState(
    val user: User? = null,
    val enrolledCoursesCount: Int = 0,
    val activeTutorsCount: Int = 0,
    val upcomingSessionsCount: Int = 0,
    val enrolledCourses: List<Enrollment> = emptyList(),
    val upcomingSession: Session? = null,
    val isLoading: Boolean = false
)
class StudentDashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StudentDashboardUiState())
    val uiState: StateFlow<StudentDashboardUiState> = _uiState.asStateFlow()

    init {
        observeData()
    }

    private fun observeData() {
        viewModelScope.launch {
            AppContainer.authRepository.currentUserFlow.collect { user ->
                _uiState.update { it.copy(user = user) }
                if (user != null) {
                    AppContainer.enrollmentRepository.observeEnrollmentsForStudent(user.uid).collect { res ->
                        val enrollments = res.getOrElse { emptyList() }
                        val courseCodes = enrollments.map { it.courseCode }

                        var tutorCount = 0
                        val tutorsSet = mutableSetOf<String>()
                        for (code in courseCodes) {
                            val tutors = AppContainer.tutorRepository.getTutorsForCourse(code).getOrElse { emptyList() }
                            tutorsSet.addAll(tutors.map { it.tutorId })
                        }
                        tutorCount = tutorsSet.size

                        val allSessions = AppContainer.sessionRepository.getSessions().getOrElse { emptyList() }
                        val todayStart = Validator.getStartOfTodayMillis()
                        val upcoming = allSessions.filter { courseCodes.contains(it.courseCode) && it.dateMillis >= todayStart }

                        _uiState.update {
                            it.copy(
                                user = user,
                                enrolledCourses = enrollments,
                                enrolledCoursesCount = enrollments.size,
                                activeTutorsCount = tutorCount,
                                upcomingSessionsCount = upcoming.size,
                                upcomingSession = upcoming.firstOrNull(),
                                isLoading = false
                            )
                        }
                    }
                }
            }
        }
    }
}

data class CourseDirectoryUiState(
    val courses: List<Course> = emptyList(),
    val searchQuery: String = "",
    val selectedDepartment: String = "All Departments",
    val isLoading: Boolean = false
)
class CourseDirectoryViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CourseDirectoryUiState())
    val uiState: StateFlow<CourseDirectoryUiState> = _uiState.asStateFlow()

    init {
        observeCourses()
    }

    private fun observeCourses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            AppContainer.courseRepository.observeCourses().collect { res ->
                val courses = res.getOrElse { emptyList() }
                _uiState.update { it.copy(courses = courses, isLoading = false) }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSelectedDepartment(department: String) {
        _uiState.update { it.copy(selectedDepartment = department) }
    }

    fun setDepartment(department: String) {
        setSelectedDepartment(department)
    }
}

data class CourseDetailsUiState(
    val course: Course? = null,
    val isEnrolled: Boolean = false,
    val tutors: List<TutorProfile> = emptyList(),
    val isLoading: Boolean = false
)
class CourseDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CourseDetailsUiState())
    val uiState: StateFlow<CourseDetailsUiState> = _uiState.asStateFlow()

    fun loadCourse(courseCode: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val course = AppContainer.courseRepository.getCourse(courseCode).getOrNull()
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            val isEnrolled = if (user != null) {
                AppContainer.enrollmentRepository.isEnrolled(user.uid, courseCode).getOrDefault(false)
            } else false
            val tutors = AppContainer.tutorRepository.getTutorsForCourse(courseCode).getOrElse { emptyList() }
            _uiState.update {
                it.copy(
                    course = course,
                    isEnrolled = isEnrolled,
                    tutors = tutors,
                    isLoading = false
                )
            }
        }
    }

    fun enrollCourse(courseCode: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            if (user == null) {
                onResult(false, "Please sign in to enroll")
                return@launch
            }
            val enrollment = Enrollment(
                id = "${user.uid}_$courseCode",
                studentId = user.uid,
                courseCode = courseCode,
                enrolledAt = System.currentTimeMillis()
            )
            val result = AppContainer.enrollmentRepository.enroll(enrollment)
            if (result.isSuccess) {
                _uiState.update { it.copy(isEnrolled = true) }
                onResult(true, null)
            } else {
                onResult(false, result.exceptionOrNull()?.localizedMessage ?: "Enrollment failed")
            }
        }
    }
}

data class TutorDashboardUiState(
    val user: User? = null,
    val activeStudentsCount: Int = 0,
    val coursesTeachingCount: Int = 0,
    val todaysSessionsCount: Int = 0,
    val todaySessionsCount: Int = 0,
    val tutorRating: String = "4.9 ★",
    val nextUpcomingSession: Session? = null,
    val isLoading: Boolean = false
)
class TutorDashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorDashboardUiState())
    val uiState: StateFlow<TutorDashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            var nextSession: Session? = null
            var studentsCount = 0
            var teachingCount = 0
            if (user != null) {
                val profile = AppContainer.tutorRepository.getTutorProfile(user.uid).getOrNull()
                teachingCount = profile?.courses?.size ?: 0
                val sessions = AppContainer.sessionRepository.getSessions().getOrElse { emptyList() }
                val todayStart = Validator.getStartOfTodayMillis()
                val tutorSessions = sessions.filter { it.tutorId == user.uid && it.dateMillis >= todayStart }
                nextSession = tutorSessions.firstOrNull()
                studentsCount = tutorSessions.map { it.studentId }.distinct().size
            }
            _uiState.update {
                it.copy(
                    user = user,
                    activeStudentsCount = studentsCount,
                    coursesTeachingCount = teachingCount,
                    todaysSessionsCount = if (nextSession != null) 1 else 0,
                    nextUpcomingSession = nextSession,
                    isLoading = false
                )
            }
        }
    }
}

data class TutorStudentsUiState(
    val students: List<Pair<User, String>> = emptyList(), // User + CourseCode
    val isLoading: Boolean = false
)
class TutorStudentsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorStudentsUiState())
    val uiState: StateFlow<TutorStudentsUiState> = _uiState.asStateFlow()

    init {
        loadStudents()
    }

    fun loadStudents() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            val list = mutableListOf<Pair<User, String>>()
            if (user != null) {
                val profile = AppContainer.tutorRepository.getTutorProfile(user.uid).getOrNull()
                val courses = profile?.courses ?: emptyList()
                val allUsers = AppContainer.userRepository.getUsers().getOrElse { emptyList() }

                for (u in allUsers) {
                    if (u.role == UserRole.STUDENT) {
                        val studentEnrollments = AppContainer.enrollmentRepository.getEnrollmentsForStudent(u.uid).getOrElse { emptyList() }
                        for (enr in studentEnrollments) {
                            if (courses.contains(enr.courseCode)) {
                                list.add(u to enr.courseCode)
                            }
                        }
                    }
                }
            }
            _uiState.update { it.copy(students = list.distinctBy { it.first.uid }, isLoading = false) }
        }
    }
}

data class TutorSessionUiState(
    val sessions: List<Session> = emptyList(),
    val pendingSessions: List<Session> = emptyList(),
    val upcomingSessions: List<Session> = emptyList(),
    val completedSessions: List<Session> = emptyList(),
    val studentMap: Map<String, User> = emptyMap(),
    val isLoading: Boolean = false
)
class TutorSessionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorSessionUiState())
    val uiState: StateFlow<TutorSessionUiState> = _uiState.asStateFlow()

    init {
        loadSessions()
    }

    fun loadSessions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            val sessions = if (user != null) {
                AppContainer.sessionRepository.getSessions().getOrElse { emptyList() }.filter { it.tutorId == user.uid }
            } else emptyList()

            val todayStart = Validator.getStartOfTodayMillis()
            val pending = sessions.filter { it.status == "PENDING" }
            val upcoming = sessions.filter { (it.status == "SCHEDULED" || it.status == "ACCEPTED") && it.dateMillis >= todayStart }
            val completed = sessions.filter { it.status == "COMPLETED" || it.dateMillis < todayStart }

            val allUsers = AppContainer.userRepository.getUsers().getOrElse { emptyList() }
            val studentMap = allUsers.associateBy { it.uid }

            _uiState.update {
                it.copy(
                    sessions = sessions,
                    pendingSessions = pending,
                    upcomingSessions = upcoming,
                    completedSessions = completed,
                    studentMap = studentMap,
                    isLoading = false
                )
            }
        }
    }

    fun acceptRequest(id: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            db.collection("sessions").document(id).update("status", "ACCEPTED").await()
            loadSessions()
            onComplete()
        }
    }

    fun declineRequest(id: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            db.collection("sessions").document(id).update("status", "REJECTED").await()
            loadSessions()
            onComplete()
        }
    }

    fun attachLink(id: String, link: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val db = FirebaseFirestore.getInstance()
            db.collection("sessions").document(id).update("meetingLink", link).await()
            loadSessions()
            onComplete()
        }
    }
}

data class TutorCreateSessionUiState(
    val tutorCourses: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class TutorCreateSessionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorCreateSessionUiState())
    val uiState: StateFlow<TutorCreateSessionUiState> = _uiState.asStateFlow()

    private val _tutorCourses = MutableStateFlow<List<String>>(emptyList())
    val tutorCourses: StateFlow<List<String>> = _tutorCourses.asStateFlow()

    init {
        loadTutorCourses()
    }

    fun loadTutorCourses() {
        viewModelScope.launch {
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            if (user != null) {
                val profile = AppContainer.tutorRepository.getTutorProfile(user.uid).getOrNull()
                val courses = profile?.courses ?: emptyList()
                _uiState.update { it.copy(tutorCourses = courses) }
                _tutorCourses.value = courses
            }
        }
    }

    fun createSession(
        courseCode: String,
        title: String,
        format: String,
        meetingLink: String,
        venue: String,
        dateMillis: Long,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (format == "Online" && meetingLink.isBlank()) {
            onError("Valid meeting link required for online sessions")
            return
        }
        if (format == "In-Person" && venue.isBlank()) {
            onError("Venue location required for in-person sessions")
            return
        }
        val todayStart = Validator.getStartOfTodayMillis()
        if (dateMillis < todayStart) {
            onError("Session date cannot be in the past")
            return
        }

        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val dateStr = formatter.format(Date(dateMillis))

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            if (user == null) {
                onError("Please sign in")
                return@launch
            }
            val session = Session(
                id = UUID.randomUUID().toString(),
                courseCode = courseCode,
                tutorId = user.uid,
                studentId = "",
                title = title,
                description = if (format == "Online") "Online Link: $meetingLink" else "Venue: $venue",
                date = dateStr,
                dateMillis = dateMillis,
                time = "04:00 PM",
                method = format,
                meetingLink = meetingLink,
                venue = venue,
                status = "SCHEDULED",
                createdAt = System.currentTimeMillis()
            )
            val result = AppContainer.sessionRepository.createSession(session)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onSuccess()
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Failed to create session"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }
}

data class TutorUploadMaterialUiState(
    val tutorCourses: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class TutorUploadMaterialViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorUploadMaterialUiState())
    val uiState: StateFlow<TutorUploadMaterialUiState> = _uiState.asStateFlow()

    private val _tutorCourses = MutableStateFlow<List<String>>(emptyList())
    val tutorCourses: StateFlow<List<String>> = _tutorCourses.asStateFlow()

    init {
        loadTutorCourses()
    }

    fun loadTutorCourses() {
        viewModelScope.launch {
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            if (user != null) {
                val profile = AppContainer.tutorRepository.getTutorProfile(user.uid).getOrNull()
                val courses = profile?.courses ?: emptyList()
                _uiState.update { it.copy(tutorCourses = courses) }
                _tutorCourses.value = courses
            }
        }
    }

    fun uploadMaterial(
        courseCode: String,
        title: String,
        category: String,
        description: String,
        fileUrl: String,
        fileSizeMb: Double,
        certified: Boolean,
        tag: String = "",
        accessMode: String = "FREE",
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val urlErr = Validator.validateMaterialUrl(fileUrl)
        if (urlErr != null) {
            onError(urlErr)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            if (user == null) {
                onError("Please sign in")
                return@launch
            }
            val material = Material(
                id = UUID.randomUUID().toString(),
                courseCode = courseCode,
                tutorId = user.uid,
                title = title,
                category = category,
                topicTag = tag,
                description = description,
                fileUrl = fileUrl,
                access = accessMode.uppercase(),
                status = "APPROVED",
                createdAt = System.currentTimeMillis()
            )
            val result = AppContainer.materialRepository.uploadMaterial(material)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onSuccess()
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Failed to upload material"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }
}

data class CourseEnrollmentStat(
    val course: Course,
    val enrollmentCount: Int
)

data class AdminOverviewUiState(
    val usersCount: Int = 0,
    val coursesCount: Int = 0,
    val studentsCount: Int = 0,
    val tutorsCount: Int = 0,
    val materialsCount: Int = 0,
    val topCourses: List<CourseEnrollmentStat> = emptyList(),
    val isLoading: Boolean = false
)
class AdminOverviewViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminOverviewUiState())
    val uiState: StateFlow<AdminOverviewUiState> = _uiState.asStateFlow()

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val users = AppContainer.userRepository.getUsers().getOrElse { emptyList() }
            val courses = AppContainer.courseRepository.getCourses(includeDrafts = true).getOrElse { emptyList() }
            val materials = AppContainer.materialRepository.getAllMaterials().getOrElse { emptyList() }

            val students = users.count { it.role == UserRole.STUDENT }
            val tutors = users.count { it.role == UserRole.TUTOR }

            val enrollments = AppContainer.enrollmentRepository.getAllEnrollments().getOrElse { emptyList() }

            val countsMap = enrollments.groupBy { it.courseCode }.mapValues { it.value.size }
            val sortedCourses = courses.map { course ->
                CourseEnrollmentStat(course, countsMap[course.code] ?: 0)
            }.sortedByDescending { it.enrollmentCount }.take(3)

            _uiState.update {
                it.copy(
                    usersCount = users.size,
                    coursesCount = courses.size,
                    studentsCount = students,
                    tutorsCount = tutors,
                    materialsCount = materials.size,
                    topCourses = sortedCourses,
                    isLoading = false
                )
            }
        }
    }
}

data class AdminUsersUiState(
    val users: List<User> = emptyList(),
    val isLoading: Boolean = false
)
class AdminUsersViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUsersUiState())
    val uiState: StateFlow<AdminUsersUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
        observeUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            val users = AppContainer.userRepository.getUsers().getOrElse { emptyList() }
            _uiState.update { it.copy(users = users) }
        }
    }

    private fun observeUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            AppContainer.userRepository.observeUsers().collect { res ->
                val users = res.getOrElse { emptyList() }
                _uiState.update { it.copy(users = users, isLoading = false) }
            }
        }
    }

    fun updateUserStatus(uid: String, status: UserStatus, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            AppContainer.userRepository.updateUserStatus(uid, status)
            onComplete()
        }
    }
}

data class AdminCoursesUiState(
    val courses: List<Course> = emptyList(),
    val isLoading: Boolean = false
)
class AdminCoursesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminCoursesUiState())
    val uiState: StateFlow<AdminCoursesUiState> = _uiState.asStateFlow()

    init {
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            AppContainer.courseRepository.observeCourses(includeDrafts = true).collect { res ->
                val courses = res.getOrElse { emptyList() }
                _uiState.update { it.copy(courses = courses, isLoading = false) }
            }
        }
    }
}

data class AdminContentUiState(
    val materials: List<Material> = emptyList(),
    val isLoading: Boolean = false
)
class AdminContentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminContentUiState())
    val uiState: StateFlow<AdminContentUiState> = _uiState.asStateFlow()

    init {
        loadMaterials()
    }

    fun loadMaterials() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            AppContainer.materialRepository.observeAllMaterials().collect { res ->
                val materials = res.getOrElse { emptyList() }
                _uiState.update { it.copy(materials = materials, isLoading = false) }
            }
        }
    }

    fun updateMaterialStatus(id: String, status: String) {
        viewModelScope.launch {
            AppContainer.materialRepository.updateMaterialStatus(id, status)
        }
    }

    fun warnUploader(id: String) {
        viewModelScope.launch {
            AppContainer.materialRepository.warnUploader(id)
        }
    }
}

data class AdminCreateCourseUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class AdminCreateCourseViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminCreateCourseUiState())
    val uiState: StateFlow<AdminCreateCourseUiState> = _uiState.asStateFlow()

    fun createCourse(course: Course, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val existing = AppContainer.courseRepository.getCourse(course.code).getOrNull()
            if (existing != null) {
                val msg = "Course code already exists. Please use a unique code."
                _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                onError(msg)
                return@launch
            }
            val result = AppContainer.courseRepository.saveCourse(course)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onSuccess()
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Failed to save course"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }

    fun createCourse(
        code: String,
        title: String,
        department: String,
        credits: Int,
        description: String,
        semester: String,
        isDraft: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        val course = Course(
            code = code,
            title = title,
            department = department,
            credits = credits,
            description = description,
            semester = semester,
            status = if (isDraft) "DRAFT" else "ACTIVE"
        )
        createCourse(course, onSuccess, onError)
    }
}
