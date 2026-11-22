package com.example.yaza_wanga

import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.Course
import com.example.yaza_wanga.data.model.TutorProfile
import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.example.yaza_wanga.fakes.FakeAuthRepository
import com.example.yaza_wanga.fakes.FakeCourseRepository
import com.example.yaza_wanga.fakes.FakeEnrollmentRepository
import com.example.yaza_wanga.fakes.FakeMaterialRepository
import com.example.yaza_wanga.fakes.FakeTutorRepository
import com.example.yaza_wanga.ui.viewmodel.AdminCreateCourseViewModel
import com.example.yaza_wanga.ui.viewmodel.CourseDetailsViewModel
import com.example.yaza_wanga.ui.viewmodel.CourseDirectoryViewModel
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
class CourseViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeAuthRepository: FakeAuthRepository
    private lateinit var fakeCourseRepository: FakeCourseRepository
    private lateinit var fakeEnrollmentRepository: FakeEnrollmentRepository
    private lateinit var fakeTutorRepository: FakeTutorRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
        fakeCourseRepository = FakeCourseRepository()
        fakeEnrollmentRepository = FakeEnrollmentRepository()
        fakeTutorRepository = FakeTutorRepository()

        AppContainer.authRepository = fakeAuthRepository
        AppContainer.courseRepository = fakeCourseRepository
        AppContainer.enrollmentRepository = fakeEnrollmentRepository
        AppContainer.materialRepository = FakeMaterialRepository()
        AppContainer.tutorRepository = fakeTutorRepository

        // Seed fake tutors
        val tutor1 = TutorProfile(tutorId = "t1", name = "Eligible Tutor", courses = listOf("INF 212", "COM 311"), status = "VERIFIED")
        fakeTutorRepository.tutors.add(tutor1)

        // Seed fake courses
        val c1 = Course("INF 212", "Web Development", "Computer Science", 3, "Web dev basics", "Semester 1", emptyList(), "ACTIVE")
        val c2 = Course("COM 311", "Database Systems", "Computer Science", 4, "Database design", "Semester 1", emptyList(), "ACTIVE")
        val c3 = Course("MAT 201", "Calculus", "Mathematics", 3, "Multivariable calculus", "Semester 1", emptyList(), "ACTIVE")

        fakeCourseRepository.courses["INF 212"] = c1
        fakeCourseRepository.courses["COM 311"] = c2
        fakeCourseRepository.courses["MAT 201"] = c3
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- COURSE SEARCH TESTS ---

    @Test
    fun courseSearch_byCode_returnsMatchingCourse() = runTest(testDispatcher) {
        val viewModel = CourseDirectoryViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSearchQuery("INF 212")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        assertEquals("INF 212", state.searchQuery)

        val filtered = state.courses.filter {
            it.code.contains(state.searchQuery, ignoreCase = true) || it.title.contains(state.searchQuery, ignoreCase = true)
        }

        assertEquals(1, filtered.size)
        assertEquals("INF 212", filtered.first().code)
    }

    @Test
    fun courseSearch_byTitle_returnsMatchingCourse() = runTest(testDispatcher) {
        val viewModel = CourseDirectoryViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSearchQuery("Calculus")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        val filtered = state.courses.filter {
            it.code.contains(state.searchQuery, ignoreCase = true) || it.title.contains(state.searchQuery, ignoreCase = true)
        }

        assertEquals(1, filtered.size)
        assertEquals("MAT 201", filtered.first().code)
    }

    @Test
    fun courseSearch_byDepartmentFilter_returnsMatchingDepartmentCourses() = runTest(testDispatcher) {
        val viewModel = CourseDirectoryViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSelectedDepartment("Mathematics")
        testDispatcher.scheduler.advanceUntilIdle()

        val state = viewModel.uiState.value
        val filtered = state.courses.filter { it.department == "Mathematics" }

        assertEquals(1, filtered.size)
        assertEquals("MAT 201", filtered.first().code)
    }

    // --- ENROLLMENT TESTS ---

    @Test
    fun enrollment_firstTime_succeeds() = runTest(testDispatcher) {
        val user = User(
            uid = "student_101",
            email = "student@unima.ac.mw",
            name = "Student User",
            role = UserRole.STUDENT,
            status = UserStatus.ACTIVE
        )
        fakeAuthRepository.loggedInUser = user

        val viewModel = CourseDetailsViewModel()
        var successResult = false
        var errorResult: String? = null

        viewModel.enrollCourse("INF 212") { success, err ->
            successResult = success
            errorResult = err
        }
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(successResult)
        assertNull(errorResult)
        assertTrue(viewModel.uiState.value.isEnrolled)
        assertEquals(1, fakeEnrollmentRepository.enrollments.size)
        assertEquals("INF 212", fakeEnrollmentRepository.enrollments.first().courseCode)
    }

    @Test
    fun enrollment_duplicateEnrollment_preservesExistingEnrollmentWithoutDuplicates() = runTest(testDispatcher) {
        val user = User(
            uid = "student_101",
            email = "student@unima.ac.mw",
            name = "Student User",
            role = UserRole.STUDENT,
            status = UserStatus.ACTIVE
        )
        fakeAuthRepository.loggedInUser = user

        val viewModel = CourseDetailsViewModel()

        // First enrollment
        viewModel.enrollCourse("INF 212") { _, _ -> }
        testDispatcher.scheduler.advanceUntilIdle()
        assertEquals(1, fakeEnrollmentRepository.enrollments.size)

        // Second enrollment attempt
        var secondSuccess = false
        var secondError: String? = null
        viewModel.enrollCourse("INF 212") { success, err ->
            secondSuccess = success
            secondError = err
        }
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(secondSuccess)
        assertNull(secondError)
        assertEquals(1, fakeEnrollmentRepository.enrollments.size)
    }

    @Test
    fun enrollment_noEligibleTutor_failsWithClearMessage() = runTest(testDispatcher) {
        val user = User(
            uid = "student_101",
            email = "student@unima.ac.mw",
            name = "Student User",
            role = UserRole.STUDENT,
            status = UserStatus.ACTIVE
        )
        fakeAuthRepository.loggedInUser = user

        val viewModel = CourseDetailsViewModel()
        var successResult = true
        var errorResult: String? = null

        // MAT 201 has no eligible tutors assigned
        viewModel.enrollCourse("MAT 201") { success, err ->
            successResult = success
            errorResult = err
        }
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(successResult)
        assertEquals("No eligible tutor is currently available for this course. Enrollment cannot be completed.", errorResult)
        assertFalse(viewModel.uiState.value.isEnrolled)
        assertEquals(0, fakeEnrollmentRepository.enrollments.size)
    }

    // --- ADMIN COURSE CREATION TESTS ---

    @Test
    fun createCourse_validData_succeeds() = runTest(testDispatcher) {
        val viewModel = AdminCreateCourseViewModel()
        var success = false
        var errorMsg: String? = null

        viewModel.createCourse(
            code = "CSC 411",
            title = "Artificial Intelligence",
            department = "Computer Science",
            credits = 4,
            description = "AI fundamentals",
            semester = "Semester 1",
            isDraft = false,
            onSuccess = { success = true },
            onError = { errorMsg = it }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(success)
        assertNull(errorMsg)
        assertTrue(fakeCourseRepository.courses.containsKey("CSC 411"))
    }

    @Test
    fun createCourse_duplicateCode_fails() = runTest(testDispatcher) {
        val viewModel = AdminCreateCourseViewModel()
        var success = false
        var errorMsg: String? = null

        viewModel.createCourse(
            code = "INF 212",
            title = "Duplicate Code",
            department = "Computer Science",
            credits = 3,
            description = "Desc",
            semester = "Semester 1",
            isDraft = false,
            onSuccess = { success = true },
            onError = { errorMsg = it }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(success)
        assertEquals("Course code already exists. Please use a unique code.", errorMsg)
    }
}
