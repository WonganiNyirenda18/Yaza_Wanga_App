package com.example.yaza_wanga

import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.Course
import com.example.yaza_wanga.data.model.Material
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
import com.example.yaza_wanga.ui.viewmodel.AdminOverviewViewModel
import com.example.yaza_wanga.ui.viewmodel.SignInViewModel
import com.example.yaza_wanga.ui.viewmodel.SignUpViewModel
import com.example.yaza_wanga.validation.Validator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RefactoringTests {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeUserRepository: FakeUserRepository
    private lateinit var fakeCourseRepository: FakeCourseRepository
    private lateinit var fakeEnrollmentRepository: FakeEnrollmentRepository
    private lateinit var fakeMaterialRepository: FakeMaterialRepository
    private lateinit var fakeSessionRepository: FakeSessionRepository
    private lateinit var fakeTutorRepository: FakeTutorRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
        fakeUserRepository = FakeUserRepository()
        fakeCourseRepository = FakeCourseRepository()
        fakeEnrollmentRepository = FakeEnrollmentRepository()
        fakeMaterialRepository = FakeMaterialRepository()
        fakeSessionRepository = FakeSessionRepository()
        fakeTutorRepository = FakeTutorRepository()

        AppContainer.authRepository = fakeAuthRepository
        AppContainer.userRepository = fakeUserRepository
        AppContainer.courseRepository = fakeCourseRepository
        AppContainer.enrollmentRepository = fakeEnrollmentRepository
        AppContainer.materialRepository = fakeMaterialRepository
        AppContainer.sessionRepository = fakeSessionRepository
        AppContainer.tutorRepository = fakeTutorRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- REFACTORING SAFETY TEST SUITE ---

    @Test
    fun validator_emailValidation_returnsCorrectErrors() {
        assertEquals("Please fill all fields", Validator.validateEmail(""))
        assertEquals("Please enter a valid email address", Validator.validateEmail("invalid-email"))
        assertNull(Validator.validateEmail("student@unima.ac.mw"))
    }

    @Test
    fun validator_passwordValidation_returnsCorrectErrors() {
        assertEquals("Please fill all fields", Validator.validatePassword(""))
        assertEquals("Password must be at least 8 characters", Validator.validatePassword("12345"))
        assertNull(Validator.validatePassword("password123"))
    }

    @Test
    fun authentication_studentRegistration_createsActiveUser() = runTest(testDispatcher) {
        val viewModel = SignUpViewModel()
        val data = mapOf(
            "fullName" to "Student One",
            "email" to "student1@unima.ac.mw",
            "password" to "password123"
        )

        var completed = false
        viewModel.signUpStudent(data, onError = {}, onComplete = { completed = true })
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(completed)
        val user = fakeAuthRepository.loggedInUser
        assertTrue(user != null)
        assertEquals(UserRole.STUDENT, user?.role)
        assertEquals(UserStatus.ACTIVE, user?.status)
    }

    @Test
    fun authentication_tutorRegistration_createsPendingUser() = runTest(testDispatcher) {
        val viewModel = SignUpViewModel()
        val data = mapOf(
            "fullName" to "Tutor One",
            "email" to "tutor1@unima.ac.mw",
            "password" to "password123",
            "faculty" to "Science"
        )

        var completed = false
        viewModel.signUpTutor(data, onError = {}, onComplete = { completed = true })
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(completed)
        val user = fakeAuthRepository.loggedInUser
        assertTrue(user != null)
        assertEquals(UserRole.TUTOR, user?.role)
        assertEquals(UserStatus.PENDING, user?.status)
    }

    @Test
    fun authentication_suspendedUser_cannotSignIn() = runTest(testDispatcher) {
        val suspendedUser = User(
            uid = "suspended_1",
            email = "suspended@unima.ac.mw",
            role = UserRole.STUDENT,
            status = UserStatus.SUSPENDED
        )
        fakeAuthRepository.users[suspendedUser.uid] = suspendedUser
        fakeAuthRepository.passwords[suspendedUser.email] = "password123"

        val viewModel = SignInViewModel()
        var errorMsg: String? = null

        viewModel.signIn("suspended@unima.ac.mw", "password123", onNavigate = {}, onError = { errorMsg = it })
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(errorMsg.isNullOrEmpty())
        assertTrue(errorMsg?.contains("suspended", ignoreCase = true) == true)
        assertNull(fakeAuthRepository.loggedInUser)
    }

    @Test
    fun adminOverview_calculatesMetricsCorrectly() = runTest(testDispatcher) {
        val student = User("s1", "student@unima.ac.mw", "Student One", UserRole.STUDENT, UserStatus.ACTIVE)
        val tutor = User("t1", "tutor@unima.ac.mw", "Tutor One", UserRole.TUTOR, UserStatus.ACTIVE)

        fakeUserRepository.saveUser(student)
        fakeUserRepository.saveUser(tutor)

        fakeCourseRepository.saveCourse(Course("INF 212", "Web Dev", "CS", 3, "Desc", "Sem 1", emptyList(), "ACTIVE"))
        fakeCourseRepository.saveCourse(Course("COM 311", "DB", "CS", 4, "Desc", "Sem 1", emptyList(), "ACTIVE"))

        fakeMaterialRepository.materials.add(Material(id = "m1", courseCode = "INF 212", tutorId = "tutor_1", title = "Slides", category = "Lecture", topicTag = "Tag", description = "Desc", fileUrl = "url", access = "FREE", status = "APPROVED"))
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
    fun dateValidation_pastDate_failsValidation() {
        val pastMillis = System.currentTimeMillis() - 86400000L
        val todayStart = Validator.getStartOfTodayMillis()
        assertTrue(pastMillis < todayStart)
    }
}
