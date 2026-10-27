package com.example.yaza_wanga

import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.Course
import com.example.yaza_wanga.data.model.Enrollment
import com.example.yaza_wanga.data.model.Material
import com.example.yaza_wanga.data.model.Session
import com.example.yaza_wanga.data.model.TutorProfile
import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.example.yaza_wanga.fakes.FakeAuthRepository
import com.example.yaza_wanga.fakes.FakeCourseRepository
import com.example.yaza_wanga.fakes.FakeEnrollmentRepository
import com.example.yaza_wanga.fakes.FakeMaterialRepository
import com.example.yaza_wanga.fakes.FakeSessionRepository
import com.example.yaza_wanga.fakes.FakeTutorRepository
import com.example.yaza_wanga.fakes.FakeUserRepository
import com.example.yaza_wanga.ui.viewmodel.AdminCoursesViewModel
import com.example.yaza_wanga.ui.viewmodel.AdminOverviewViewModel
import com.example.yaza_wanga.ui.viewmodel.AdminUsersViewModel
import com.example.yaza_wanga.ui.viewmodel.CourseDirectoryViewModel
import com.example.yaza_wanga.ui.viewmodel.StudentDashboardViewModel
import com.example.yaza_wanga.ui.viewmodel.StudentProfileViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class RefactoringTests {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeCourseRepository: FakeCourseRepository
    private lateinit var fakeEnrollmentRepository: FakeEnrollmentRepository
    private lateinit var fakeSessionRepository: FakeSessionRepository
    private lateinit var fakeMaterialRepository: FakeMaterialRepository
    private lateinit var fakeTutorRepository: FakeTutorRepository
    private lateinit var fakeUserRepository: FakeUserRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
        fakeCourseRepository = FakeCourseRepository()
        fakeEnrollmentRepository = FakeEnrollmentRepository()
        fakeSessionRepository = FakeSessionRepository()
        fakeMaterialRepository = FakeMaterialRepository()
        fakeTutorRepository = FakeTutorRepository()
        fakeUserRepository = FakeUserRepository()

        AppContainer.authRepository = fakeAuthRepository
        AppContainer.courseRepository = fakeCourseRepository
        AppContainer.enrollmentRepository = fakeEnrollmentRepository
        AppContainer.sessionRepository = fakeSessionRepository
        AppContainer.materialRepository = fakeMaterialRepository
        AppContainer.tutorRepository = fakeTutorRepository
        AppContainer.userRepository = fakeUserRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun namesCorrectlyComeFromSignedInUser() = runTest(testDispatcher) {
        val testUser = User(
            uid = "user_999",
            email = "chifundo@unima.ac.mw",
            name = "Chifundo Banda",
            role = UserRole.STUDENT,
            status = UserStatus.ACTIVE
        )
        fakeAuthRepository.loggedInUser = testUser

        val viewModel = StudentProfileViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertNotNull(viewModel.uiState.value.user)
        assertEquals("Chifundo Banda", viewModel.uiState.value.user?.name)
        assertEquals("chifundo@unima.ac.mw", viewModel.uiState.value.user?.email)
    }

    @Test
    fun countsAreCorrectlyComputedFromEnrollments() = runTest(testDispatcher) {
        val student = User(
            uid = "student_1",
            email = "student@unima.ac.mw",
            name = "Student One",
            role = UserRole.STUDENT,
            status = UserStatus.ACTIVE
        )
        fakeAuthRepository.loggedInUser = student

        // Seed enrollments
        fakeEnrollmentRepository.enroll(Enrollment("e1", "student_1", "INF 212", 0L, "ACTIVE"))
        fakeEnrollmentRepository.enroll(Enrollment("e2", "student_1", "COM 311", 0L, "ACTIVE"))

        // Seed tutors
        fakeTutorRepository.tutors.add(TutorProfile("t1", "Tutor A", "Bio", 10.0, listOf("INF 212"), 4.9, 10, "VERIFIED"))

        // Seed sessions (today / future)
        val todayStr = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault()).format(Date())
        fakeSessionRepository.createSession(Session("s1", "t1", "", "INF 212", "Workshop", "Desc", todayStr, "04:00 PM", "Online", "", "", "SCHEDULED", 0L))

        val dashboardViewModel = StudentDashboardViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, dashboardViewModel.uiState.value.enrolledCoursesCount)
        assertEquals(1, dashboardViewModel.uiState.value.activeTutorsCount)
        assertEquals(1, dashboardViewModel.uiState.value.upcomingSessionsCount)
    }

    @Test
    fun countsAreCorrectlyComputedFromUsersAndCourses() = runTest(testDispatcher) {
        val student = User(
            uid = "student_1",
            email = "student@unima.ac.mw",
            name = "Student One",
            role = UserRole.STUDENT,
            status = UserStatus.ACTIVE
        )
        val tutor = User(
            uid = "tutor_1",
            email = "tutor@unima.ac.mw",
            name = "Tutor One",
            role = UserRole.TUTOR,
            status = UserStatus.ACTIVE
        )

        fakeUserRepository.saveUser(student)
        fakeUserRepository.saveUser(tutor)

        fakeCourseRepository.saveCourse(Course("INF 212", "Web Dev", "CS", 3, "Desc", "Sem 1", emptyList(), "ACTIVE"))
        fakeCourseRepository.saveCourse(Course("COM 311", "DB", "CS", 4, "Desc", "Sem 1", emptyList(), "ACTIVE"))

        fakeMaterialRepository.materials.add(Material("m1", "INF 212", "tutor_1", "Slides", "Lecture", "Tag", "Desc", "url", "FREE", "APPROVED", 0L))
        fakeMaterialRepository.refreshFlow()

        val adminViewModel = AdminOverviewViewModel()
        adminViewModel.loadData()
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(2, adminViewModel.uiState.value.usersCount)
        assertEquals(1, adminViewModel.uiState.value.studentsCount)
        assertEquals(1, adminViewModel.uiState.value.tutorsCount)
        assertEquals(2, adminViewModel.uiState.value.coursesCount)
        assertEquals(1, adminViewModel.uiState.value.materialsCount)
    }

    @Test
    fun viewModelsEmitEmptyListWhenRepositoriesAreEmpty() = runTest(testDispatcher) {
        val courseDirViewModel = CourseDirectoryViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(courseDirViewModel.uiState.value.courses.isEmpty())

        val adminUsersViewModel = AdminUsersViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(adminUsersViewModel.uiState.value.users.isEmpty())

        val adminCoursesViewModel = AdminCoursesViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(adminCoursesViewModel.uiState.value.courses.isEmpty())
    }

    @Test
    fun viewModelsEmitLiveUpdatesWhenDocumentIsAdded() = runTest(testDispatcher) {
        val courseDirViewModel = CourseDirectoryViewModel()
        testDispatcher.scheduler.advanceUntilIdle()
        assertTrue(courseDirViewModel.uiState.value.courses.isEmpty())

        // Add a course dynamically using fake repository backed by MutableStateFlow
        val newCourse = Course("PHY 101", "Physics", "Science", 4, "Physics mechanics", "Semester 1", emptyList(), "ACTIVE")
        fakeCourseRepository.saveCourse(newCourse)
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(courseDirViewModel.uiState.value.courses.isEmpty())
        assertEquals(1, courseDirViewModel.uiState.value.courses.size)
        assertEquals("PHY 101", courseDirViewModel.uiState.value.courses.first().code)
        assertEquals("Physics", courseDirViewModel.uiState.value.courses.first().title)
    }
}
