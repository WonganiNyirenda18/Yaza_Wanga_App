package com.example.yaza_wanga.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

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
                onNavigate(user?.role ?: UserRole.STUDENT)
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Sign in failed"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }

    fun forgotPassword(email: String, onError: (String?) -> Unit) {
        viewModelScope.launch {
            val result = AppContainer.authRepository.sendPasswordResetEmail(email)
            if (result.isFailure) {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Failed"
                onError(msg)
            } else {
                onError(null)
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
}

data class StudentDashboardUiState(
    val user: User? = null,
    val enrolledCoursesCount: Int = 0,
    val enrolledCourses: List<Enrollment> = emptyList(),
    val isLoading: Boolean = false
)
class StudentDashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(StudentDashboardUiState())
    val uiState: StateFlow<StudentDashboardUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val userResult = AppContainer.authRepository.fetchCurrentUser()
            val user = userResult.getOrNull()
            var enrollments = emptyList<Enrollment>()
            if (user != null) {
                enrollments = AppContainer.enrollmentRepository.getEnrollmentsForStudent(user.uid).getOrElse { emptyList() }
            }
            _uiState.update {
                it.copy(
                    user = user,
                    enrolledCourses = enrollments,
                    enrolledCoursesCount = enrollments.size,
                    isLoading = false
                )
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
        loadCourses()
    }

    fun loadCourses() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = AppContainer.courseRepository.getCourses()
            val list = result.getOrElse { emptyList() }.filter { it.status != "DRAFT" }
            _uiState.update { it.copy(courses = list, isLoading = false) }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setDepartment(dept: String) {
        _uiState.update { it.copy(selectedDepartment = dept) }
    }
}

data class CourseDetailsUiState(
    val course: Course? = null,
    val materials: List<Material> = emptyList(),
    val tutors: List<TutorProfile> = emptyList(),
    val isEnrolled: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)
class CourseDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CourseDetailsUiState())
    val uiState: StateFlow<CourseDetailsUiState> = _uiState.asStateFlow()

    fun loadCourse(code: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val courseResult = AppContainer.courseRepository.getCourse(code)
            val materialsResult = AppContainer.materialRepository.getMaterials(code)
            val tutorsResult = AppContainer.tutorRepository.getTutorsForCourse(code)

            val currentUser = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            var enrolled = false
            if (currentUser != null) {
                enrolled = AppContainer.enrollmentRepository.isEnrolled(currentUser.uid, code).getOrDefault(false)
            }

            _uiState.update {
                it.copy(
                    course = courseResult.getOrNull(),
                    materials = materialsResult.getOrElse { emptyList() },
                    tutors = tutorsResult.getOrElse { emptyList() },
                    isEnrolled = enrolled,
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
            val enrollmentId = "${user.uid}_$courseCode"
            val enrollment = Enrollment(
                id = enrollmentId,
                studentId = user.uid,
                courseCode = courseCode,
                enrolledAt = System.currentTimeMillis(),
                status = "ACTIVE"
            )
            val result = AppContainer.enrollmentRepository.enroll(enrollment)
            if (result.isSuccess) {
                _uiState.update { it.copy(isEnrolled = true) }
                onResult(true, null)
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Enrollment failed"
                onResult(false, msg)
            }
        }
    }
}

data class TutorDashboardUiState(val user: User? = null, val isLoading: Boolean = false)
class TutorDashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorDashboardUiState())
    val uiState: StateFlow<TutorDashboardUiState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            _uiState.update { it.copy(user = user) }
        }
    }
}

data class TutorProfileUiState(val profile: TutorProfile? = null, val isLoading: Boolean = false)
class TutorProfileViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorProfileUiState())
    val uiState: StateFlow<TutorProfileUiState> = _uiState.asStateFlow()
}

data class TutorSessionUiState(val sessions: List<Session> = emptyList(), val isLoading: Boolean = false)
class TutorSessionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorSessionUiState())
    val uiState: StateFlow<TutorSessionUiState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            val sessions = AppContainer.sessionRepository.getSessions().getOrElse { emptyList() }
            _uiState.update { it.copy(sessions = sessions) }
        }
    }
}

data class TutorStudentsUiState(val students: List<User> = emptyList(), val isLoading: Boolean = false)
class TutorStudentsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorStudentsUiState())
    val uiState: StateFlow<TutorStudentsUiState> = _uiState.asStateFlow()
}

data class TutorCreateSessionUiState(val isLoading: Boolean = false, val errorMessage: String? = null)
class TutorCreateSessionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorCreateSessionUiState())
    val uiState: StateFlow<TutorCreateSessionUiState> = _uiState.asStateFlow()

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
        viewModelScope.launch {
            if (format == "Online" && meetingLink.isBlank()) {
                onError("Valid meeting link required for online sessions")
                return@launch
            }
            if (format == "In-Person" && venue.isBlank()) {
                onError("Venue location required for in-person sessions")
                return@launch
            }
            if (dateMillis < System.currentTimeMillis() - 86400000) {
                onError("Session date cannot be in the past")
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val tutorId = AppContainer.authRepository.currentUser?.uid ?: "tutor_default"
            val session = Session(
                id = UUID.randomUUID().toString(),
                tutorId = tutorId,
                courseCode = courseCode,
                title = title.ifBlank { "Tutoring Session" },
                description = if (format == "Online") "Online Link: $meetingLink" else "Venue: $venue",
                startTime = dateMillis,
                endTime = dateMillis + 3600000,
                status = "SCHEDULED"
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

data class TutorUploadMaterialUiState(val isLoading: Boolean = false, val errorMessage: String? = null)
class TutorUploadMaterialViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorUploadMaterialUiState())
    val uiState: StateFlow<TutorUploadMaterialUiState> = _uiState.asStateFlow()

    fun uploadMaterial(
        courseCode: String,
        title: String,
        description: String,
        category: String,
        fileUrl: String,
        fileSizeMb: Double,
        certified: Boolean,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            if (title.isBlank()) {
                onError("Resource title is required")
                return@launch
            }
            if (!certified) {
                onError("You must certify compliance with academic guidelines")
                return@launch
            }
            if (fileSizeMb > 50.0) {
                onError("File size cannot exceed 50MB")
                return@launch
            }
            if (description.length > 250) {
                onError("Description cannot exceed 250 characters")
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val uploaderId = AppContainer.authRepository.currentUser?.uid ?: "tutor_default"
            val material = Material(
                id = UUID.randomUUID().toString(),
                courseCode = courseCode,
                title = title.trim(),
                description = description.trim(),
                fileUrl = fileUrl.ifBlank { "https://firebasestorage.googleapis.com/v0/b/yaza-wanga.appspot.com/o/sample.pdf" },
                fileType = category,
                uploadedBy = uploaderId,
                uploadedAt = System.currentTimeMillis()
            )
            val result = AppContainer.materialRepository.uploadMaterial(material)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onSuccess()
            } else {
                val msg = result.exceptionOrNull()?.localizedMessage ?: "Upload failed"
                _uiState.update { it.copy(errorMessage = msg) }
                onError(msg)
            }
        }
    }
}

data class AdminOverviewUiState(val usersCount: Int = 0, val coursesCount: Int = 0, val isLoading: Boolean = false)
class AdminOverviewViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminOverviewUiState())
    val uiState: StateFlow<AdminOverviewUiState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            val users = AppContainer.userRepository.getUsers().getOrElse { emptyList() }
            val courses = AppContainer.courseRepository.getCourses().getOrElse { emptyList() }
            _uiState.update { it.copy(usersCount = users.size, coursesCount = courses.size) }
        }
    }
}

data class AdminUsersUiState(val users: List<User> = emptyList(), val isLoading: Boolean = false)
class AdminUsersViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminUsersUiState())
    val uiState: StateFlow<AdminUsersUiState> = _uiState.asStateFlow()

    init {
        loadUsers()
    }

    fun loadUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val users = AppContainer.userRepository.getUsers().getOrElse { emptyList() }
            _uiState.update { it.copy(users = users, isLoading = false) }
        }
    }

    fun updateUserStatus(uid: String, status: UserStatus, onComplete: () -> Unit) {
        viewModelScope.launch {
            AppContainer.userRepository.updateUserStatus(uid, status)
            loadUsers()
            onComplete()
        }
    }
}

data class AdminCoursesUiState(val courses: List<Course> = emptyList(), val isLoading: Boolean = false)
class AdminCoursesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminCoursesUiState())
    val uiState: StateFlow<AdminCoursesUiState> = _uiState.asStateFlow()
    init {
        viewModelScope.launch {
            val courses = AppContainer.courseRepository.getCourses().getOrElse { emptyList() }
            _uiState.update { it.copy(courses = courses) }
        }
    }
}

data class AdminCreateCourseUiState(val isLoading: Boolean = false, val errorMessage: String? = null)
class AdminCreateCourseViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminCreateCourseUiState())
    val uiState: StateFlow<AdminCreateCourseUiState> = _uiState.asStateFlow()

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
        viewModelScope.launch {
            if (title.isBlank()) {
                onError("Course name is required")
                return@launch
            }
            if (code.isBlank()) {
                onError("Course code is required")
                return@launch
            }
            if (description.length > 500) {
                onError("Description cannot exceed 500 characters")
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val existing = AppContainer.courseRepository.getCourse(code.trim()).getOrNull()
            if (existing != null && !isDraft) {
                _uiState.update { it.copy(isLoading = false) }
                onError("Course code already exists. Please use a unique code.")
                return@launch
            }

            val course = Course(
                code = code.trim(),
                title = title.trim(),
                department = department,
                credits = credits,
                description = description.trim(),
                semester = semester,
                status = if (isDraft) "DRAFT" else "ACTIVE"
            )

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
}

data class AdminContentUiState(val materials: List<Material> = emptyList(), val isLoading: Boolean = false)
class AdminContentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminContentUiState())
    val uiState: StateFlow<AdminContentUiState> = _uiState.asStateFlow()
}
