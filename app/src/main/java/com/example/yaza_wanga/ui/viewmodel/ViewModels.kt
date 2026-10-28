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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
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
                        val todayStr = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())
                        val upcoming = allSessions.filter { courseCodes.contains(it.courseCode) && it.date >= todayStr }

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
            if (courseCode.isBlank()) {
                _uiState.update { it.copy(course = null, isLoading = false) }
                return@launch
            }
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
            if (courseCode.isBlank()) {
                onResult(false, "Invalid course code")
                return@launch
            }
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
    val tutorRating: String = "4.9 ★",
    val nextUpcomingSession: Session? = null,
    val tutorCourses: List<String> = emptyList(),
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
            if (user != null) {
                val tutorProfile = AppContainer.tutorRepository.getTutorProfile(user.uid).getOrNull()
                val courses = tutorProfile?.courses ?: emptyList()
                val enrollments = AppContainer.enrollmentRepository.observeEnrollmentsForCourses(courses).let { flow ->
                    var list = emptyList<Enrollment>()
                    flow.collect { res ->
                        list = res.getOrElse { emptyList() }
                        return@collect
                    }
                    list
                }
                val uniqueStudents = enrollments.map { it.studentId }.distinct().size

                val sessions = AppContainer.sessionRepository.getSessions().getOrNull() ?: emptyList()
                val tutorSessions = sessions.filter { it.tutorId == user.uid }
                val todayStr = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())
                val todayCount = tutorSessions.count { it.date == todayStr }
                val upcoming = tutorSessions.filter { (it.status == "SCHEDULED" || it.status == "PENDING") && it.date >= todayStr }
                    .sortedBy { it.date }

                _uiState.update {
                    it.copy(
                        user = user,
                        activeStudentsCount = uniqueStudents,
                        coursesTeachingCount = courses.size,
                        todaysSessionsCount = todayCount,
                        nextUpcomingSession = upcoming.firstOrNull(),
                        tutorCourses = courses,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }
}

data class StudentListItem(
    val studentId: String,
    val name: String,
    val course: String,
    val regNumber: String
)

data class TutorStudentsUiState(
    val students: List<StudentListItem> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
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
            if (user == null) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Please sign in") }
                return@launch
            }
            val tutorProfile = AppContainer.tutorRepository.getTutorProfile(user.uid).getOrNull()
            val courses = tutorProfile?.courses ?: emptyList()
            if (courses.isEmpty()) {
                _uiState.update { it.copy(students = emptyList(), isLoading = false) }
                return@launch
            }
            AppContainer.enrollmentRepository.observeEnrollmentsForCourses(courses).collect { res ->
                val enrollments = res.getOrElse { emptyList() }
                val studentItems = mutableListOf<StudentListItem>()
                for (enr in enrollments) {
                    val studentUser = AppContainer.userRepository.getUser(enr.studentId).getOrNull()
                    val name = studentUser?.name?.ifBlank { studentUser.email.substringBefore("@") } ?: "Student"
                    val regNum = studentUser?.studentId?.ifBlank { enr.studentId } ?: enr.studentId
                    studentItems.add(
                        StudentListItem(
                            studentId = enr.studentId,
                            name = name,
                            course = enr.courseCode,
                            regNumber = regNum
                        )
                    )
                }
                _uiState.update { it.copy(students = studentItems.distinctBy { it.studentId }, isLoading = false) }
            }
        }
    }
}

data class TutorSessionUiState(
    val pendingSessions: List<Session> = emptyList(),
    val upcomingSessions: List<Session> = emptyList(),
    val completedSessions: List<Session> = emptyList(),
    val studentMap: Map<String, User> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
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
            if (user == null) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Please sign in") }
                return@launch
            }
            AppContainer.sessionRepository.observeSessionsForTutor(user.uid).collect { res ->
                val sessions = res.getOrElse { emptyList() }
                val pending = sessions.filter { it.status.uppercase() == "PENDING" }
                val upcoming = sessions.filter { it.status.uppercase() == "SCHEDULED" || it.status.uppercase() == "ACCEPTED" }
                val completed = sessions.filter { it.status.uppercase() == "COMPLETED" || it.status.uppercase() == "FINISHED" }

                val sMap = mutableMapOf<String, User>()
                for (s in sessions) {
                    if (s.studentId.isNotBlank() && !sMap.containsKey(s.studentId)) {
                        AppContainer.userRepository.getUser(s.studentId).getOrNull()?.let {
                            sMap[s.studentId] = it
                        }
                    }
                }

                _uiState.update {
                    it.copy(
                        pendingSessions = pending,
                        upcomingSessions = upcoming,
                        completedSessions = completed,
                        studentMap = sMap,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun acceptRequest(sessionId: String, meetingLink: String = "", onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val sessions = AppContainer.sessionRepository.getSessions().getOrNull() ?: emptyList()
            val session = sessions.find { it.id == sessionId }
            if (session != null) {
                val updated = session.copy(
                    status = "SCHEDULED",
                    meetingLink = if (meetingLink.isNotBlank()) meetingLink else session.meetingLink
                )
                AppContainer.sessionRepository.createSession(updated)
                onComplete()
            }
        }
    }

    fun declineRequest(sessionId: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val sessions = AppContainer.sessionRepository.getSessions().getOrNull() ?: emptyList()
            val session = sessions.find { it.id == sessionId }
            if (session != null) {
                val updated = session.copy(status = "DECLINED")
                AppContainer.sessionRepository.createSession(updated)
                onComplete()
            }
        }
    }

    fun attachLink(sessionId: String, link: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            val sessions = AppContainer.sessionRepository.getSessions().getOrNull() ?: emptyList()
            val session = sessions.find { it.id == sessionId }
            if (session != null) {
                val updated = session.copy(meetingLink = link)
                AppContainer.sessionRepository.createSession(updated)
                onComplete()
            }
        }
    }
}

data class TutorCreateSessionUiState(
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
                _tutorCourses.value = profile?.courses ?: emptyList()
            }
        }
    }

    fun createSession(
        courseCode: String,
        format: String,
        link: String,
        venue: String,
        date: String,
        time: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
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
                date = date,
                time = time,
                method = format,
                meetingLink = link,
                venue = venue,
                status = "PENDING",
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
        if (courseCode.isBlank()) {
            onError("Please select a course")
            return
        }
        if (format == "Online" && meetingLink.isBlank()) {
            onError("Valid meeting link required for online sessions")
            return
        }
        if (format == "In-Person" && venue.isBlank()) {
            onError("Venue location required for in-person sessions")
            return
        }
        if (dateMillis < System.currentTimeMillis() - 60000) {
            onError("Session date cannot be in the past")
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
            if (user == null) {
                onError("Please sign in")
                return@launch
            }
            val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            val dateStr = formatter.format(Date(dateMillis))

            val session = Session(
                id = UUID.randomUUID().toString(),
                courseCode = courseCode,
                tutorId = user.uid,
                studentId = "",
                title = title,
                description = if (format == "Online") "Online Link: $meetingLink" else "Venue: $venue",
                date = dateStr,
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
                _tutorCourses.value = profile?.courses ?: emptyList()
            }
        }
    }

    fun uploadMaterial(
        courseCode: String,
        title: String,
        category: String,
        tag: String,
        description: String,
        fileUrl: String,
        accessMode: String,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        if (courseCode.isBlank()) {
            onError("Please select a course")
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
        uploadMaterial(courseCode, title, category, tag, description, fileUrl, accessMode, onSuccess, onError)
    }
}

data class TopCourseInfo(
    val course: Course,
    val enrollmentCount: Int
)

data class AdminOverviewUiState(
    val usersCount: Int = 0,
    val coursesCount: Int = 0,
    val studentsCount: Int = 0,
    val tutorsCount: Int = 0,
    val materialsCount: Int = 0,
    val topCourses: List<TopCourseInfo> = emptyList(),
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
            val enrollments = AppContainer.enrollmentRepository.getEnrollments().getOrElse { emptyList() }

            val students = users.count { it.role == UserRole.STUDENT }
            val tutors = users.count { it.role == UserRole.TUTOR }

            val enrollmentCounts = enrollments.groupBy { it.courseCode }.mapValues { it.value.size }
            val topCourses = courses.map { course ->
                TopCourseInfo(course = course, enrollmentCount = enrollmentCounts[course.code] ?: 0)
            }.sortedByDescending { it.enrollmentCount }.take(3)

            _uiState.update {
                it.copy(
                    usersCount = users.size,
                    coursesCount = courses.size,
                    studentsCount = students,
                    tutorsCount = tutors,
                    materialsCount = materials.size,
                    topCourses = topCourses,
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
