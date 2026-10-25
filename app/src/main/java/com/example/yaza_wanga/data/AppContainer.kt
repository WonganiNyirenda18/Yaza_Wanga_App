package com.example.yaza_wanga.data

import com.example.yaza_wanga.data.repository.*

object AppContainer {
    val authRepository = AuthRepository()
    val courseRepository = CourseRepository()
    val tutorRepository = TutorRepository()
    val sessionRepository = SessionRepository()
    val materialRepository = MaterialRepository()
    val userRepository = UserRepository()
    val enrollmentRepository = EnrollmentRepository()
    val departmentRepository = DepartmentRepository()
    val storageRepository = StorageRepository()
    val notificationRepository = NotificationRepository()
    val progressRepository = ProgressRepository()
}
