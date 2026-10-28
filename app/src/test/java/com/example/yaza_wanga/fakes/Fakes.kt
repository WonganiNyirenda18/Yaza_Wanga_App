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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class FakeAuthRepository : AuthRepository(auth = null, db = null) {
    val users = mutableMapOf<String, User>()
    val passwords = mutableMapOf<String, String>()
    
    private val _currentUserFlow = MutableStateFlow<User?>(null)
    override val currentUserFlow: StateFlow<User?> = _currentUserFlow

    var loggedInUser: User? = null
        set(value) {
            field = value
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
        loggedInUser = user
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
        loggedInUser = user
        return Result.success(user)
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun fetchCurrentUser(): Result<User?> {
        return Result.success(loggedInUser)
    }

    override fun signOut() {
        loggedInUser = null
    }
}

class FakeCourseRepository : CourseRepository(db = null) {
    val courses = mutableMapOf<String, Course>()
    private val coursesFlow = MutableStateFlow<List<Course>>(emptyList())

    override fun observeCourses(includeDrafts: Boolean): Flow<Result<List<Course>>> {
        coursesFlow.value = courses.values.toList()
        return coursesFlow.map { list -> Result.success(list.filter { if (includeDrafts) true else it.status == "ACTIVE" }) }
    }

    override suspend fun getCourses(includeDrafts: Boolean): Result<List<Course>> {
        coursesFlow.value = courses.values.toList()
        return Result.success(courses.values.toList().filter { if (includeDrafts) true else it.status == "ACTIVE" })
    }

    override suspend fun getCourse(code: String): Result<Course?> {
        return Result.success(courses[code])
    }

    override suspend fun saveCourse(course: Course): Result<Unit> {
        courses[course.code] = course
        coursesFlow.value = courses.values.toList()
        return Result.success(Unit)
    }
}

class FakeEnrollmentRepository : EnrollmentRepository(db = null) {
    val enrollments = mutableListOf<Enrollment>()
    private val enrollmentsFlow = MutableStateFlow<List<Enrollment>>(emptyList())

    override fun observeEnrollmentsForStudent(studentId: String): Flow<Result<List<Enrollment>>> {
        enrollmentsFlow.value = enrollments.toList()
        return enrollmentsFlow.map { list -> Result.success(list.filter { it.studentId == studentId && it.status == "ACTIVE" }) }
    }

    override suspend fun getEnrollmentsForStudent(studentId: String): Result<List<Enrollment>> {
        enrollmentsFlow.value = enrollments.toList()
        val list = enrollments.filter { it.studentId == studentId && it.status == "ACTIVE" }
        return Result.success(list)
    }

    override suspend fun getAllEnrollments(): Result<List<Enrollment>> {
        enrollmentsFlow.value = enrollments.toList()
        return Result.success(enrollments.toList())
    }

    override suspend fun enroll(enrollment: Enrollment): Result<Unit> {
        val exists = enrollments.any {
            it.studentId == enrollment.studentId && it.courseCode == enrollment.courseCode && it.status == "ACTIVE"
        }
        if (exists) {
            return Result.failure(Exception("Already enrolled in this course"))
        }
        enrollments.add(enrollment)
        enrollmentsFlow.value = enrollments.toList()
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

    override fun observeSessions(): Flow<Result<List<Session>>> =
        flowOf(Result.success(sessions.toList()))

    override suspend fun getSessions(): Result<List<Session>> {
        return Result.success(sessions.toList())
    }

    override suspend fun createSession(session: Session): Result<Unit> {
        sessions.add(session)
        return Result.success(Unit)
    }
}

class FakeMaterialRepository : MaterialRepository(db = null) {
    val materials = mutableListOf<Material>()

    fun refreshFlow() {}

    override fun observeMaterials(courseCode: String): Flow<Result<List<Material>>> =
        flowOf(Result.success(materials.filter { it.courseCode == courseCode }))

    override fun observeAllMaterials(): Flow<Result<List<Material>>> =
        flowOf(Result.success(materials.toList()))

    override suspend fun getMaterials(courseCode: String): Result<List<Material>> {
        return Result.success(materials.filter { it.courseCode == courseCode })
    }

    override suspend fun getAllMaterials(): Result<List<Material>> {
        return Result.success(materials.toList())
    }

    override suspend fun uploadMaterial(material: Material): Result<Unit> {
        materials.add(material)
        return Result.success(Unit)
    }
}

class FakeTutorRepository : TutorRepository(db = null) {
    val tutors = mutableListOf<TutorProfile>()

    override fun observeVerifiedTutorsForCourse(courseCode: String): Flow<Result<List<TutorProfile>>> =
        flowOf(Result.success(tutors.filter { (it.status == "VERIFIED" || it.status == "ACTIVE") && (it.courses.contains(courseCode) || it.courses.isEmpty()) }))

    override suspend fun getTutorsForCourse(courseCode: String): Result<List<TutorProfile>> {
        return Result.success(tutors.filter { (it.status == "VERIFIED" || it.status == "ACTIVE") && (it.courses.contains(courseCode) || it.courses.isEmpty()) })
    }
}

class FakeUserRepository : UserRepository(db = null) {
    val users = mutableMapOf<String, User>()

    fun saveUser(user: User) {
        users[user.uid] = user
    }

    override fun observeUsers(): Flow<Result<List<User>>> =
        flowOf(Result.success(users.values.toList()))

    override suspend fun getUsers(): Result<List<User>> {
        return Result.success(users.values.toList())
    }

    override suspend fun getUser(uid: String): Result<User?> {
        return Result.success(users[uid])
    }

    override suspend fun updateUserStatus(uid: String, status: UserStatus): Result<Unit> {
        val u = users[uid]
        if (u != null) {
            users[uid] = u.copy(status = status)
        }
        return Result.success(Unit)
    }
}
