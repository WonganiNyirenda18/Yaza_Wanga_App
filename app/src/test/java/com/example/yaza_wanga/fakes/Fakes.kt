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
import com.google.firebase.auth.FirebaseUser

class FakeAuthRepository : AuthRepository(auth = null, db = null) {
    val users = mutableMapOf<String, User>()
    val passwords = mutableMapOf<String, String>()
    var loggedInUser: User? = null
    var shouldFailSignUpWithCollision: Boolean = false

    override val currentUser: FirebaseUser?
        get() = null

    override suspend fun signIn(email: String, pass: String): Result<User> {
        val trimmed = email.trim()
        if (trimmed.isEmpty() || pass.isEmpty()) {
            return Result.failure(Exception("Please fill all fields"))
        }
        val user = users.values.find { it.email.equals(trimmed, ignoreCase = true) }
            ?: return Result.failure(Exception("Invalid email or password"))

        if (passwords[user.uid] != pass) {
            return Result.failure(Exception("Invalid email or password"))
        }

        if (user.status == UserStatus.SUSPENDED) {
            loggedInUser = null
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
        val trimmed = email.trim()
        if (shouldFailSignUpWithCollision || users.values.any { it.email.equals(trimmed, ignoreCase = true) }) {
            return Result.failure(Exception("An account with this email already exists"))
        }

        val uid = "user_${users.size + 1}"
        val user = User(
            uid = uid,
            email = trimmed,
            name = name,
            role = role,
            status = if (role == UserRole.TUTOR) UserStatus.PENDING else UserStatus.ACTIVE,
            faculty = faculty,
            studentId = studentId,
            createdAt = System.currentTimeMillis()
        )
        users[uid] = user
        passwords[uid] = pass
        loggedInUser = user
        return Result.success(user)
    }

    override suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        val trimmed = email.trim()
        if (trimmed.isEmpty()) return Result.failure(Exception("Please fill all fields"))
        return Result.success(Unit)
    }

    override suspend fun fetchCurrentUser(): Result<User?> {
        val user = loggedInUser
        if (user?.status == UserStatus.SUSPENDED) {
            loggedInUser = null
            return Result.failure(Exception("Your account has been suspended. Contact the administrator"))
        }
        return Result.success(user)
    }

    override fun signOut() {
        loggedInUser = null
    }
}

class FakeCourseRepository : CourseRepository(db = null) {
    val courses = mutableMapOf<String, Course>()

    override suspend fun getCourses(): Result<List<Course>> {
        return Result.success(courses.values.toList())
    }

    override suspend fun getCourse(code: String): Result<Course?> {
        return Result.success(courses[code])
    }

    override suspend fun saveCourse(course: Course): Result<Unit> {
        courses[course.code] = course
        return Result.success(Unit)
    }
}

class FakeEnrollmentRepository : EnrollmentRepository(db = null) {
    val enrollments = mutableListOf<Enrollment>()

    override suspend fun getEnrollmentsForStudent(studentId: String): Result<List<Enrollment>> {
        val list = enrollments.filter { it.studentId == studentId && it.status == "ACTIVE" }
        return Result.success(list)
    }

    override suspend fun enroll(enrollment: Enrollment): Result<Unit> {
        val exists = enrollments.any {
            it.studentId == enrollment.studentId && it.courseCode == enrollment.courseCode && it.status == "ACTIVE"
        }
        if (exists) {
            return Result.failure(Exception("Already enrolled in this course"))
        }
        enrollments.add(enrollment)
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

    override suspend fun getMaterials(courseCode: String): Result<List<Material>> {
        return Result.success(materials.filter { it.courseCode == courseCode })
    }

    override suspend fun uploadMaterial(material: Material): Result<Unit> {
        materials.add(material)
        return Result.success(Unit)
    }
}

class FakeTutorRepository : TutorRepository(db = null) {
    val tutors = mutableListOf<TutorProfile>()

    override suspend fun getTutorsForCourse(courseCode: String): Result<List<TutorProfile>> {
        return Result.success(tutors.filter { it.courses.contains(courseCode) })
    }
}
