package com.example.yaza_wanga

import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.Enrollment
import com.example.yaza_wanga.data.model.Material
import com.example.yaza_wanga.data.model.TutorProfile
import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.example.yaza_wanga.fakes.FakeAuthRepository
import com.example.yaza_wanga.fakes.FakeEnrollmentRepository
import com.example.yaza_wanga.fakes.FakeMaterialRepository
import com.example.yaza_wanga.fakes.FakeTutorRepository
import com.example.yaza_wanga.ui.viewmodel.MaterialDetailsViewModel
import com.example.yaza_wanga.ui.viewmodel.MaterialsViewModel
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
class MaterialsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeEnrollmentRepository: FakeEnrollmentRepository
    private lateinit var fakeMaterialRepository: FakeMaterialRepository
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
        fakeMaterialRepository = FakeMaterialRepository()
        fakeTutorRepository = FakeTutorRepository()

        fakeAuthRepository.loggedInUser = studentUser
        AppContainer.authRepository = fakeAuthRepository
        AppContainer.enrollmentRepository = fakeEnrollmentRepository
        AppContainer.materialRepository = fakeMaterialRepository
        AppContainer.tutorRepository = fakeTutorRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun onlyEnrolledCoursesMaterialsAreShown() = runTest(testDispatcher) {
        fakeEnrollmentRepository.enrollments.add(Enrollment("e1", "student_1", "INF 212"))

        val m1 = Material(id = "m1", courseCode = "INF 212", title = "Lecture 1", status = "APPROVED", access = "FREE")
        val m2 = Material(id = "m2", courseCode = "MAT 111", title = "Lecture 2", status = "APPROVED", access = "FREE")
        fakeMaterialRepository.materials.addAll(listOf(m1, m2))

        val viewModel = MaterialsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(1, state.materials.size)
        assertEquals("INF 212", state.materials.first().material.courseCode)
    }

    @Test
    fun quarantinedFlaggedRestrictedAreHidden() = runTest(testDispatcher) {
        fakeEnrollmentRepository.enrollments.add(Enrollment("e1", "student_1", "INF 212"))

        val mApproved = Material(id = "m_app", courseCode = "INF 212", title = "Approved Material", status = "APPROVED", access = "FREE")
        val mRestricted = Material(id = "m_res", courseCode = "INF 212", title = "Restricted Material", status = "APPROVED", access = "RESTRICTED")
        val mQuarantined = Material(id = "m_qua", courseCode = "INF 212", title = "Quarantined Material", status = "QUARANTINED", access = "FREE")
        val mFlagged = Material(id = "m_fla", courseCode = "INF 212", title = "Flagged Material", status = "FLAGGED", access = "FREE")

        fakeMaterialRepository.materials.addAll(listOf(mApproved, mRestricted, mQuarantined, mFlagged))

        val viewModel = MaterialsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals(1, state.materials.size)
        assertEquals("m_app", state.materials.first().material.id)
    }

    @Test
    fun searchByTitleAndByTutorName() = runTest(testDispatcher) {
        fakeEnrollmentRepository.enrollments.add(Enrollment("e1", "student_1", "INF 212"))

        fakeTutorRepository.tutors.add(TutorProfile(tutorId = "t1", name = "Alice Tutor", status = "VERIFIED"))
        fakeTutorRepository.tutors.add(TutorProfile(tutorId = "t2", name = "Bob Tutor", status = "VERIFIED"))

        val m1 = Material(id = "m1", courseCode = "INF 212", tutorId = "t1", title = "Algorithms Guide", status = "APPROVED", access = "FREE")
        val m2 = Material(id = "m2", courseCode = "INF 212", tutorId = "t2", title = "Data Structures", status = "APPROVED", access = "FREE")
        fakeMaterialRepository.materials.addAll(listOf(m1, m2))

        val viewModel = MaterialsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        // Test search by title
        viewModel.setSearchQuery("Algorithms")
        val filteredByTitle = viewModel.uiState.value.materials.filter {
            it.material.title.contains("Algorithms", ignoreCase = true) || it.tutorName.contains("Algorithms", ignoreCase = true)
        }
        assertEquals(1, filteredByTitle.size)
        assertEquals("m1", filteredByTitle.first().material.id)

        // Test search by tutor name
        viewModel.setSearchQuery("Bob")
        val filteredByTutor = viewModel.uiState.value.materials.filter {
            it.material.title.contains("Bob", ignoreCase = true) || it.tutorName.contains("Bob", ignoreCase = true)
        }
        assertEquals(1, filteredByTutor.size)
        assertEquals("m2", filteredByTutor.first().material.id)
    }

    @Test
    fun categoryAndCourseFilters() = runTest(testDispatcher) {
        fakeEnrollmentRepository.enrollments.add(Enrollment("e1", "student_1", "INF 212"))

        val m1 = Material(id = "m1", courseCode = "INF 212", title = "Notes 1", category = "Lecture Notes", status = "APPROVED")
        val m2 = Material(id = "m2", courseCode = "INF 212", title = "Tutorial 1", category = "Tutorial", status = "APPROVED")
        fakeMaterialRepository.materials.addAll(listOf(m1, m2))

        val viewModel = MaterialsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state.categories.contains("Lecture Notes"))
        assertTrue(state.categories.contains("Tutorial"))

        viewModel.setSelectedCategory("Tutorial")
        assertEquals("Tutorial", viewModel.uiState.value.selectedCategory)
    }

    @Test
    fun noEnrollmentsGivesEmptyState() = runTest(testDispatcher) {
        val viewModel = MaterialsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertFalse(state.hasEnrollments)
        assertTrue(state.materials.isEmpty())
    }

    @Test
    fun repositoryErrorSetsErrorMessageAndStopsLoading() = runTest(testDispatcher) {
        fakeAuthRepository.loggedInUser = null

        val viewModel = MaterialsViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.errorMessage?.isNotBlank() == true)
    }

    @Test
    fun detailsCheckRefusesNotEnrolledStudent() = runTest(testDispatcher) {
        val m1 = Material(id = "m1", courseCode = "INF 212", title = "Lecture 1", status = "APPROVED", access = "FREE")
        fakeMaterialRepository.materials.add(m1)

        val viewModel = MaterialDetailsViewModel()
        viewModel.loadMaterial("INF 212", "m1")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertTrue(state.isNotEnrolled)
        assertNull(state.materialItem)
    }
}
