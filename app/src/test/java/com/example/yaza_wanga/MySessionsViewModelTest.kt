package com.example.yaza_wanga

import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.Enrollment
import com.example.yaza_wanga.data.model.Session
import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.example.yaza_wanga.fakes.FakeAuthRepository
import com.example.yaza_wanga.fakes.FakeEnrollmentRepository
import com.example.yaza_wanga.fakes.FakeSessionRepository
import com.example.yaza_wanga.fakes.FakeTutorRepository
import com.example.yaza_wanga.ui.viewmodel.MySessionsViewModel
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
class MySessionsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeEnrollmentRepository: FakeEnrollmentRepository
    private lateinit var fakeSessionRepository: FakeSessionRepository
    private lateinit var fakeTutorRepository: FakeTutorRepository

    private val studentUser = User(
        uid = "student_1",
        email = "student@unima.ac.mw",
        name = "Student One",
        role = UserRole.STUDENT,
        status = UserStatus.ACTIVE
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
        fakeEnrollmentRepository = FakeEnrollmentRepository()
        fakeSessionRepository = FakeSessionRepository()
        fakeTutorRepository = FakeTutorRepository()

        fakeAuthRepository.loggedInUser = studentUser
        AppContainer.authRepository = fakeAuthRepository
        AppContainer.enrollmentRepository = fakeEnrollmentRepository
        AppContainer.sessionRepository = fakeSessionRepository
        AppContainer.tutorRepository = fakeTutorRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun sessionsOfEnrolledCoursesOnlyAreShown() = runTest(testDispatcher) {
        fakeEnrollmentRepository.enrollments.add(Enrollment("e1", "student_1", "INF 212"))

        val s1 = Session(id = "s1", courseCode = "INF 212", tutorId = "t1", status = "SCHEDULED", dateMillis = Validator.getStartOfTodayMillis() + 86400000L)
        val s2 = Session(id = "s2", courseCode = "MAT 111", tutorId = "t1", status = "SCHEDULED", dateMillis = Validator.getStartOfTodayMillis() + 86400000L)
        fakeSessionRepository.sessions.addAll(listOf(s1, s2))

        val viewModel = MySessionsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(1, state.upcomingSessions.size)
        assertEquals("INF 212", state.upcomingSessions.first().session.courseCode)
    }

    @Test
    fun upcomingVsPastSplitIsCorrect() = runTest(testDispatcher) {
        fakeEnrollmentRepository.enrollments.add(Enrollment("e1", "student_1", "INF 212"))

        val today = Validator.getStartOfTodayMillis()
        val upcomingSession = Session(id = "s_upcoming", courseCode = "INF 212", tutorId = "t1", status = "SCHEDULED", dateMillis = today + 86400000L)
        val pastSession = Session(id = "s_past", courseCode = "INF 212", tutorId = "t1", status = "COMPLETED", dateMillis = today - 86400000L)

        fakeSessionRepository.sessions.addAll(listOf(upcomingSession, pastSession))

        val viewModel = MySessionsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.upcomingSessions.size)
        assertEquals("s_upcoming", state.upcomingSessions.first().session.id)
        assertEquals(1, state.pastSessions.size)
        assertEquals("s_past", state.pastSessions.first().session.id)
    }

    @Test
    fun cancelledAndRejectedSessionsAreHidden() = runTest(testDispatcher) {
        fakeEnrollmentRepository.enrollments.add(Enrollment("e1", "student_1", "INF 212"))

        val today = Validator.getStartOfTodayMillis()
        val scheduledSession = Session(id = "s_sched", courseCode = "INF 212", tutorId = "t1", status = "SCHEDULED", dateMillis = today + 86400000L)
        val cancelledSession = Session(id = "s_canc", courseCode = "INF 212", tutorId = "t1", status = "CANCELLED", dateMillis = today + 86400000L)
        val rejectedSession = Session(id = "s_rej", courseCode = "INF 212", tutorId = "t1", status = "REJECTED", dateMillis = today + 86400000L)

        fakeSessionRepository.sessions.addAll(listOf(scheduledSession, cancelledSession, rejectedSession))

        val viewModel = MySessionsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.upcomingSessions.size)
        assertEquals("s_sched", state.upcomingSessions.first().session.id)
        assertTrue(state.pastSessions.isEmpty())
    }

    @Test
    fun studentWithNoEnrollmentsGetsEmptyState() = runTest(testDispatcher) {
        val s1 = Session(id = "s1", courseCode = "INF 212", tutorId = "t1", status = "SCHEDULED", dateMillis = Validator.getStartOfTodayMillis() + 86400000L)
        fakeSessionRepository.sessions.add(s1)

        val viewModel = MySessionsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertTrue(state.upcomingSessions.isEmpty())
        assertTrue(state.pastSessions.isEmpty())
    }

    @Test
    fun repositoryErrorSetsErrorMessageAndStopsLoading() = runTest(testDispatcher) {
        fakeAuthRepository.loggedInUser = null

        val viewModel = MySessionsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.errorMessage?.isNotBlank() == true)
    }
}
