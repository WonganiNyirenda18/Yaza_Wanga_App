package com.example.yaza_wanga

import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.Course
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
import org.junit.Assert.assertNotNull
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

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
        fakeCourseRepository = FakeCourseRepository()
        fakeEnrollmentRepository = FakeEnrollmentRepository()

        AppContainer.authRepository = fakeAuthRepository
        AppContainer.courseRepository = fakeCourseRepository
        AppContainer.enrollmentRepository = fakeEnrollmentRepository
        AppContainer.materialRepository = FakeMaterialRepository()
        AppContainer.tutorRepository = FakeTutorRepository()

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
        val state = viewModel.uiState.value

        assertEquals("INF 212", state.searchQuery)
        val filtered = state.courses.filter {
            it.code.contains(state.searchQuery, ignoreCase = true) ||
                    it.title.contains(state.searchQuery, ignoreCase = true)
        }

        assertEquals(1, filtered.size)
        assertEquals("INF 212", filtered.first().code)
    }

    @Test
    fun courseSearch_byPartialName_returnsMatchingCourses() = runTest(testDispatcher) {
        val viewModel = CourseDirectoryViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSearchQuery("Web")
        val state = viewModel.uiState.value

        val filtered = state.courses.filter {
            it.code.contains(state.searchQuery, ignoreCase = true) ||
                    it.title.contains(state.searchQuery, ignoreCase = true)
        }

        assertEquals(1, filtered.size)
        assertEquals("Web Development", filtered.first().title)
    }

    @Test
    fun courseSearch_noMatch_returnsEmptyList() = runTest(testDispatcher) {
        val viewModel = CourseDirectoryViewModel()
        testDispatcher.scheduler.advanceUntilIdle()

        viewModel.setSearchQuery("NONEXISTENT_999")
        val state = viewModel.uiState.value

        val filtered = state.courses.filter {
            it.code.contains(state.searchQuery, ignoreCase = true) ||
                    it.title.contains(state.searchQuery, ignoreCase = true)
        }

        assertTrue(filtered.isEmpty())
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
    fun enrollment_duplicateEnrollment_returnsError() = runTest(testDispatcher) {
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

        // Second enrollment attempt
        var secondSuccess = false
        var secondError: String? = null
        viewModel.enrollCourse("INF 212") { success, err ->
            secondSuccess = success
            secondError = err
        }
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(secondSuccess)
        assertEquals("Already enrolled in this course", secondError)
    }

    // --- ADMIN COURSE CREATION TESTS ---

    @Test
    fun adminCourseCreation_validCourse_succeeds() = runTest(testDispatcher) {
        val viewModel = AdminCreateCourseViewModel()
        var successCalled = false
        var errorMsg: String? = null

        viewModel.createCourse(
            code = "INF 310",
            title = "Mobile Application Development",
            department = "Computer Science",
            credits = 3,
            description = "Android development with Kotlin & Jetpack Compose",
            semester = "Semester 2",
            isDraft = false,
            onSuccess = { successCalled = true },
            onError = { errorMsg = it }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(successCalled)
        assertNull(errorMsg)
        assertNotNull(fakeCourseRepository.courses["INF 310"])
        assertEquals("ACTIVE", fakeCourseRepository.courses["INF 310"]?.status)
    }

    @Test
    fun adminCourseCreation_duplicateCourseCode_triggersError() = runTest(testDispatcher) {
        val viewModel = AdminCreateCourseViewModel()
        var successCalled = false
        var errorMsg: String? = null

        // Try to create INF 212 which already exists
        viewModel.createCourse(
            code = "INF 212",
            title = "Duplicate Web Dev",
            department = "Computer Science",
            credits = 3,
            description = "Duplicate description",
            semester = "Semester 1",
            isDraft = false,
            onSuccess = { successCalled = true },
            onError = { errorMsg = it }
        )
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(successCalled)
        assertEquals("Course code already exists. Please use a unique code.", errorMsg)
    }
}
