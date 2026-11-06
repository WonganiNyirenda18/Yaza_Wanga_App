package com.example.yaza_wanga

import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.Course
import com.example.yaza_wanga.data.model.Enrollment
import com.example.yaza_wanga.data.model.Material
import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.example.yaza_wanga.fakes.FakeAuthRepository
import com.example.yaza_wanga.fakes.FakeCourseRepository
import com.example.yaza_wanga.fakes.FakeEnrollmentRepository
import com.example.yaza_wanga.fakes.FakeMaterialRepository
import com.example.yaza_wanga.fakes.FakeProgressRepository
import com.example.yaza_wanga.ui.viewmodel.ProgressViewModel
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
class ProgressViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeEnrollmentRepository: FakeEnrollmentRepository
    private lateinit var fakeCourseRepository: FakeCourseRepository
    private lateinit var fakeMaterialRepository: FakeMaterialRepository
    private lateinit var fakeProgressRepository: FakeProgressRepository

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
        fakeCourseRepository = FakeCourseRepository()
        fakeMaterialRepository = FakeMaterialRepository()
        fakeProgressRepository = FakeProgressRepository()

        fakeAuthRepository.loggedInUser = studentUser
        AppContainer.authRepository = fakeAuthRepository
        AppContainer.enrollmentRepository = fakeEnrollmentRepository
        AppContainer.courseRepository = fakeCourseRepository
        AppContainer.materialRepository = fakeMaterialRepository
        AppContainer.progressRepository = fakeProgressRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun togglingTopicWritesRightDocument() = runTest(testDispatcher) {
        fakeEnrollmentRepository.enrollments.add(Enrollment("e1", "student_1", "INF 212"))
        fakeCourseRepository.courses["INF 212"] = Course("INF 212", "Database Systems")

        val m1 = Material(id = "m1", courseCode = "INF 212", topicTag = "SQL", status = "APPROVED", access = "FREE")
        fakeMaterialRepository.materials.add(m1)

        val viewModel = ProgressViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.toggleTopic("INF 212", "SQL")
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals(1, fakeProgressRepository.progressList.size)
        val p = fakeProgressRepository.progressList.first()
        assertEquals("student_1_INF 212", p.id)
        assertTrue(p.completedTopics.contains("SQL"))
    }

    @Test
    fun noEnrollmentsGivesEmptyState() = runTest(testDispatcher) {
        val viewModel = ProgressViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertFalse(state.hasEnrollments)
        assertTrue(state.courseCalculations.isEmpty())
    }

    @Test
    fun repositoryErrorSetsErrorMessageAndStopsLoading() = runTest(testDispatcher) {
        fakeAuthRepository.loggedInUser = null

        val viewModel = ProgressViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.errorMessage?.isNotBlank() == true)
    }
}
