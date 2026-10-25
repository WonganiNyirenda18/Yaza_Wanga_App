package com.example.yaza_wanga.data

import com.example.yaza_wanga.data.repository.*

object AppContainer {
    var authRepository: AuthRepository = AuthRepository()
    var courseRepository: CourseRepository = CourseRepository()
    var tutorRepository: TutorRepository = TutorRepository()
    var sessionRepository: SessionRepository = SessionRepository()
    var materialRepository: MaterialRepository = MaterialRepository()
    var userRepository: UserRepository = UserRepository()
    var enrollmentRepository: EnrollmentRepository = EnrollmentRepository()
    var departmentRepository: DepartmentRepository = DepartmentRepository()
    var storageRepository: StorageRepository = StorageRepository()
    var notificationRepository: NotificationRepository = NotificationRepository()
    var progressRepository: ProgressRepository = ProgressRepository()
}
