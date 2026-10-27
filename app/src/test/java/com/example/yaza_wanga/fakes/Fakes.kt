package com.example.yaza_wanga.fakes

import com.example.yaza_wanga.data.model.Course
import com.example.yaza_wanga.data.model.Enrollment
import com.example.yaza_wanga.data.model.Material
import com.example.yaza_wanga.data.model.Session
import com.example.yaza_wanga.data.model.TutorProfile
import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.example.yaza_wanga.data.repository.AuthRepository
import com.example.yaza_wanga.data.repository.CourseRepository
import com.example.yaza_wanga.data.repository.EnrollmentRepository
import com.example.yaza_wanga.data.repository.MaterialRepository
import com.example.yaza_wanga.data.repository.SessionRepository
import com.example.yaza_wanga.data.repository.TutorRepository
import com.example.yaza_wanga.data.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class FakeAuthRepository : AuthRepository(auth = null, db = null) {
    val users = mutableMapOf<String, User>()
    val passwords = mutableMapOf<String, String>()

    private val _currentUserFlow = MutableStateFlow<User?>(null)
    override val currentUserFlow: StateFlow<User?> = _currentUserFlow.asStateFlow()

    var loggedInUser: User?
        get() = _currentUserFlow.value
        set(value) {
            _currentUserFlow.value = value
        }

    override val currentUser: FirebaseUser?
        get() = null

    override suspend fun signIn(email: String, pass: String): Result<User> {
        if (email.isBlank() || pass.isBlank()) {
            return Result.failure(Exception("Please fill all fields"))
        }
        val user = users.values.find { it.email.equals(email, ignoreCase = true) }
        val expectedPass = user?.let { passwords[email.lowercase()] ?: passwords[it.uid] } ?: "password123"
        if (user == null || pass != expectedPass) {
            return Result.failure(Exception("Invalid email or password"))
        }
        if (user.status == UserStatus.SUSPENDED) {
            return Result.failure(Exception("Your account has been suspended. Contact the administrator"))
        }
        _currentUserFlow.value = user
        return Result.success(user)
    }

    override suspend fun signUp(
        email: String,
        pass: String,
        role: UserRole,
        name: String,
        faculty: String?,
        studentId: String?
    ): Result<User> {
        val exists = users.values.any { it.email.equals(email, ignoreCase = true) }
        if (exists) {
            return Result.failure(Exception("An account with this email already exists"))
        }

        val status = if (role == UserRole.TUTOR) UserStatus.PENDING else UserStatus.ACTIVE
        val user = User(
            uid = "uid_${email.substringBefore("@")}",
            email = email,
            name = name,
            role = role,
            status = status,
            faculty = faculty,
            studentId = studentId,
            createdAt = System.currentTimeMillis()
        )
        users[user.uid] = user
        passwords[email.lowercase()] = pass
        passwords[user.uid] = pass
        _currentUserFlow.value = user
        return Result.success(user)
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun fetchCurrentUser(): Result<User?> {
        return Result.success(_currentUserFlow.value)
    }

    override fun signOut() {
        _currentUserFlow.value = null
    }
}

class FakeCourseRepository : CourseRepository(db = null) {
    val courses = mutableMapOf<String, Course>()
    private val _coursesFlow = MutableStateFlow<List<Course>>(emptyList())

    fun refreshFlow() {
        _coursesFlow.value = courses.values.toList()
    }

    override fun observeCourses(includeDrafts: Boolean): Flow<Result<List<Course>>> {
        refreshFlow()
        return _coursesFlow.map { list ->
            Result.success(list.filter { if (includeDrafts) true else it.status == "ACTIVE" })
        }
    }

    override suspend fun getCourses(includeDrafts: Boolean): Result<List<Course>> {
        refreshFlow()
        return Result.success(_coursesFlow.value.filter { if (includeDrafts) true else it.status == "ACTIVE" })
    }

    override suspend fun getCourse(code: String): Result<Course?> {
        return Result.success(courses[code])
    }

    override suspend fun saveCourse(course: Course): Result<Unit> {
        courses[course.code] = course
        refreshFlow()
        return Result.success(Unit)
    }
}

class FakeEnrollmentRepository : EnrollmentRepository(db = null) {
    val enrollments = mutableListOf<Enrollment>()
    private val _enrollmentsFlow = MutableStateFlow<List<Enrollment>>(emptyList())

    fun refreshFlow() {
        _enrollmentsFlow.value = enrollments.toList()
    }

    override fun observeEnrollmentsForStudent(studentId: String): Flow<Result<List<Enrollment>>> {
        refreshFlow()
        return _enrollmentsFlow.map { list ->
            Result.success(list.filter { it.studentId == studentId && it.status == "ACTIVE" })
        }
    }

    override suspend fun getEnrollmentsForStudent(studentId: String): Result<List<Enrollment>> {
        refreshFlow()
        return Result.success(enrollments.filter { it.studentId == studentId && it.status == "ACTIVE" })
    }

    override suspend fun enroll(enrollment: Enrollment): Result<Unit> {
        val exists = enrollments.any {
            it.studentId == enrollment.studentId && it.courseCode == enrollment.courseCode && it.status == "ACTIVE"
        }
        if (exists) {
            return Result.failure(Exception("Already enrolled in this course"))
        }
        enrollments.add(enrollment)
        refreshFlow()
        return Result.success(Unit)
    }

    override suspend fun isEnrolled(studentId: String, courseCode: String): Result<Boolean> {
        val enrolled = enrollments.any {
            it.studentId == studentId && it.courseCode == courseCode && it.status == "ACTIVE"
        }
        return Result.success(enrolled)
    }
}

class FakeSessionRepository : SessionRepository(db = null) {
    val sessions = mutableListOf<Session>()
    private val _sessionsFlow = MutableStateFlow<List<Session>>(emptyList())

    fun refreshFlow() {
        _sessionsFlow.value = sessions.toList()
    }

    override fun observeSessions(): Flow<Result<List<Session>>> {
        refreshFlow()
        return _sessionsFlow.map { list -> Result.success(list) }
    }

    override suspend fun getSessions(): Result<List<Session>> {
        refreshFlow()
        return Result.success(_sessionsFlow.value)
    }

    override suspend fun createSession(session: Session): Result<Unit> {
        sessions.add(session)
        refreshFlow()
        return Result.success(Unit)
    }
}

class FakeMaterialRepository : MaterialRepository(db = null) {
    val materials = mutableListOf<Material>()
    private val _materialsFlow = MutableStateFlow<List<Material>>(emptyList())

    fun refreshFlow() {
        _materialsFlow.value = materials.toList()
    }

    override fun observeMaterials(courseCode: String): Flow<Result<List<Material>>> {
        refreshFlow()
        return _materialsFlow.map { list -> Result.success(list.filter { it.courseCode == courseCode }) }
    }

    override fun observeAllMaterials(): Flow<Result<List<Material>>> {
        refreshFlow()
        return _materialsFlow.map { list -> Result.success(list) }
    }

    override suspend fun getMaterials(courseCode: String): Result<List<Material>> {
        refreshFlow()
        return Result.success(materials.filter { it.courseCode == courseCode })
    }

    override suspend fun getAllMaterials(): Result<List<Material>> {
        refreshFlow()
        return Result.success(materials.toList())
    }

    override suspend fun uploadMaterial(material: Material): Result<Unit> {
        materials.add(material)
        refreshFlow()
        return Result.success(Unit)
    }
}

class FakeTutorRepository : TutorRepository(db = null) {
    val tutors = mutableListOf<TutorProfile>()
    private val _tutorsFlow = MutableStateFlow<List<TutorProfile>>(emptyList())

    fun refreshFlow() {
        _tutorsFlow.value = tutors.toList()
    }

    override fun observeVerifiedTutorsForCourse(courseCode: String): Flow<Result<List<TutorProfile>>> {
        refreshFlow()
        return _tutorsFlow.map { list ->
            Result.success(list.filter { (it.status == "VERIFIED" || it.status == "ACTIVE") && (it.courses.contains(courseCode) || it.courses.isEmpty()) })
        }
    }

    override suspend fun getTutorsForCourse(courseCode: String): Result<List<TutorProfile>> {
        refreshFlow()
        return Result.success(tutors.filter { (it.status == "VERIFIED" || it.status == "ACTIVE") && (it.courses.contains(courseCode) || it.courses.isEmpty()) })
    }
}

class FakeUserRepository : UserRepository(db = null) {
    val users = mutableMapOf<String, User>()
    private val _usersFlow = MutableStateFlow<List<User>>(emptyList())

    fun refreshFlow() {
        _usersFlow.value = users.values.toList()
    }

    override fun observeUsers(): Flow<Result<List<User>>> {
        refreshFlow()
        return _usersFlow.map { list -> Result.success(list) }
    }

    override suspend fun getUsers(): Result<List<User>> {
        refreshFlow()
        return Result.success(_usersFlow.value)
    }

    override suspend fun getUser(uid: String): Result<User?> {
        return Result.success(users[uid])
    }

    override suspend fun updateUserStatus(uid: String, status: UserStatus): Result<Unit> {
        val u = users[uid]
        if (u != null) {
            users[uid] = u.copy(status = status)
            refreshFlow()
        }
        return Result.success(Unit)
    }

    fun saveUser(user: User) {
        users[user.uid] = user
        refreshFlow()
    }
}
