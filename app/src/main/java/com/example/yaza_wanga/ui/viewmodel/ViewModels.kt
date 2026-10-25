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
            val result = AppContainer.authRepository.fetchCurrentUser()
            _uiState.update { it.copy(user = result.getOrNull(), isLoading = false) }
        }
    }
}

data class CourseDirectoryUiState(
    val courses: List<Course> = emptyList(),
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
            _uiState.update { it.copy(courses = result.getOrElse { emptyList() }, isLoading = false) }
        }
    }
}

data class CourseDetailsUiState(
    val course: Course? = null,
    val materials: List<Material> = emptyList(),
    val isEnrolled: Boolean = false,
    val isLoading: Boolean = false
)
class CourseDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(CourseDetailsUiState())
    val uiState: StateFlow<CourseDetailsUiState> = _uiState.asStateFlow()

    fun loadCourse(code: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val courseResult = AppContainer.courseRepository.getCourse(code)
            val materialsResult = AppContainer.materialRepository.getMaterials(code)
            _uiState.update {
                it.copy(
                    course = courseResult.getOrNull(),
                    materials = materialsResult.getOrElse { emptyList() },
                    isLoading = false
                )
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

    fun createSession(session: Session, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = AppContainer.sessionRepository.createSession(session)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onSuccess()
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Failed to create session") }
            }
        }
    }
}

data class TutorUploadMaterialUiState(val isLoading: Boolean = false, val errorMessage: String? = null)
class TutorUploadMaterialViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TutorUploadMaterialUiState())
    val uiState: StateFlow<TutorUploadMaterialUiState> = _uiState.asStateFlow()

    fun uploadMaterial(material: Material, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = AppContainer.materialRepository.uploadMaterial(material)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onSuccess()
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Upload failed") }
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
        viewModelScope.launch {
            val users = AppContainer.userRepository.getUsers().getOrElse { emptyList() }
            _uiState.update { it.copy(users = users) }
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

    fun createCourse(course: Course, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = AppContainer.courseRepository.saveCourse(course)
            _uiState.update { it.copy(isLoading = false) }
            if (result.isSuccess) {
                onSuccess()
            } else {
                _uiState.update { it.copy(errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Failed") }
            }
        }
    }
}

data class AdminContentUiState(val materials: List<Material> = emptyList(), val isLoading: Boolean = false)
class AdminContentViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AdminContentUiState())
    val uiState: StateFlow<AdminContentUiState> = _uiState.asStateFlow()
}
