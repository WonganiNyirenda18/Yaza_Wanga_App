package com.example.yaza_wanga

import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.example.yaza_wanga.fakes.FakeAuthRepository
import com.example.yaza_wanga.fakes.FakeSessionRepository
import com.example.yaza_wanga.ui.viewmodel.TutorCreateSessionViewModel
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
class SessionViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeSessionRepository: FakeSessionRepository
    private lateinit var fakeAuthRepository: FakeAuthRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeSessionRepository = FakeSessionRepository()
        fakeAuthRepository = FakeAuthRepository()

        fakeAuthRepository.loggedInUser = User(
            uid = "t1",
            email = "tutor@unima.ac.mw",
            name = "Tutor John",
            role = UserRole.TUTOR,
            status = UserStatus.ACTIVE
        )

        AppContainer.sessionRepository = fakeSessionRepository
        AppContainer.authRepository = fakeAuthRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun createSession_onlineWithoutMeetingLink_triggersError() = runTest(testDispatcher) {
        val viewModel = TutorCreateSessionViewModel()
        var successCalled = false
        var errorMsg: String? = null
        val futureTime = System.currentTimeMillis() + 86400000

        viewModel.createSession(
            courseCode = "INF 212",
            title = "Web Development Session",
            format = "Online",
            meetingLink = "",
            venue = "",
            dateMillis = futureTime,
            onSuccess = { successCalled = true },
            onError = { errorMsg = it }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(successCalled)
        assertEquals("Valid meeting link required for online sessions", errorMsg)
        assertTrue(fakeSessionRepository.sessions.isEmpty())
    }

    @Test
    fun createSession_inPersonWithoutVenue_triggersError() = runTest(testDispatcher) {
        val viewModel = TutorCreateSessionViewModel()
        var successCalled = false
        var errorMsg: String? = null
        val futureTime = System.currentTimeMillis() + 86400000

        viewModel.createSession(
            courseCode = "INF 212",
            title = "Web Development Session",
            format = "In-Person",
            meetingLink = "",
            venue = "",
            dateMillis = futureTime,
            onSuccess = { successCalled = true },
            onError = { errorMsg = it }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(successCalled)
        assertEquals("Venue location required for in-person sessions", errorMsg)
        assertTrue(fakeSessionRepository.sessions.isEmpty())
    }

    @Test
    fun createSession_dateInPast_triggersError() = runTest(testDispatcher) {
        val viewModel = TutorCreateSessionViewModel()
        var successCalled = false
        var errorMsg: String? = null
        val pastTime = System.currentTimeMillis() - (2 * 86400000)

        viewModel.createSession(
            courseCode = "INF 212",
            title = "Web Development Session",
            format = "Online",
            meetingLink = "https://meet.google.com/abc-defg-hij",
            venue = "",
            dateMillis = pastTime,
            onSuccess = { successCalled = true },
            onError = { errorMsg = it }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(successCalled)
        assertEquals("Session date cannot be in the past", errorMsg)
        assertTrue(fakeSessionRepository.sessions.isEmpty())
    }

    @Test
    fun createSession_validOnlineSession_createsSuccessfully() = runTest(testDispatcher) {
        val viewModel = TutorCreateSessionViewModel()
        var successCalled = false
        var errorMsg: String? = null
        val futureTime = System.currentTimeMillis() + 86400000

        viewModel.createSession(
            courseCode = "INF 212",
            title = "HTML & CSS Deep Dive",
            format = "Online",
            meetingLink = "https://meet.google.com/abc-defg-hij",
            venue = "",
            dateMillis = futureTime,
            onSuccess = { successCalled = true },
            onError = { errorMsg = it }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(successCalled)
        assertNull(errorMsg)
        assertEquals(1, fakeSessionRepository.sessions.size)

        val created = fakeSessionRepository.sessions.first()
        assertEquals("INF 212", created.courseCode)
        assertEquals("HTML & CSS Deep Dive", created.title)
        assertEquals("Online Link: https://meet.google.com/abc-defg-hij", created.description)
        assertEquals("SCHEDULED", created.status)
    }

    @Test
    fun createSession_validInPersonSession_createsSuccessfully() = runTest(testDispatcher) {
        val viewModel = TutorCreateSessionViewModel()
        var successCalled = false
        var errorMsg: String? = null
        val futureTime = System.currentTimeMillis() + 86400000

        viewModel.createSession(
            courseCode = "COM 311",
            title = "SQL Joins Workshop",
            format = "In-Person",
            meetingLink = "",
            venue = "Computer Lab 2",
            dateMillis = futureTime,
            onSuccess = { successCalled = true },
            onError = { errorMsg = it }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(successCalled)
        assertNull(errorMsg)
        assertEquals(1, fakeSessionRepository.sessions.size)

        val created = fakeSessionRepository.sessions.first()
        assertEquals("COM 311", created.courseCode)
        assertEquals("SQL Joins Workshop", created.title)
        assertEquals("Venue: Computer Lab 2", created.description)
        assertEquals("SCHEDULED", created.status)
    }
}
