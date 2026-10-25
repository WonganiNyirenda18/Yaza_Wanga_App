package com.example.yaza_wanga

import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.example.yaza_wanga.fakes.FakeAuthRepository
import com.example.yaza_wanga.ui.viewmodel.SignInViewModel
import com.example.yaza_wanga.ui.viewmodel.SignUpViewModel
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
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var fakeAuthRepository: FakeAuthRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeAuthRepository = FakeAuthRepository()
        AppContainer.authRepository = fakeAuthRepository
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- LOGIN TESTS ---

    @Test
    fun signIn_emptyFields_triggersError() = runTest(testDispatcher) {
        val viewModel = SignInViewModel()
        var errorMsg: String? = null
        var navigatedRole: UserRole? = null

        viewModel.signIn("", "", onNavigate = { navigatedRole = it }, onError = { errorMsg = it })
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Please fill all fields", errorMsg)
        assertNull(navigatedRole)
        assertEquals("Please fill all fields", viewModel.uiState.value.errorMessage)
        assertFalse(viewModel.uiState.value.isLoading)
    }

    @Test
    fun signIn_wrongPassword_triggersError() = runTest(testDispatcher) {
        val existingUser = User(
            uid = "user_1",
            email = "student@unima.ac.mw",
            name = "Student User",
            role = UserRole.STUDENT,
            status = UserStatus.ACTIVE
        )
        fakeAuthRepository.users["user_1"] = existingUser
        fakeAuthRepository.passwords["user_1"] = "correctpass"

        val viewModel = SignInViewModel()
        var errorMsg: String? = null
        var navigatedRole: UserRole? = null

        viewModel.signIn("student@unima.ac.mw", "wrongpass", onNavigate = { navigatedRole = it }, onError = { errorMsg = it })
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Invalid email or password", errorMsg)
        assertNull(navigatedRole)
        assertEquals("Invalid email or password", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun signIn_suspendedAccount_triggersSuspendedError() = runTest(testDispatcher) {
        val suspendedUser = User(
            uid = "user_suspended",
            email = "suspended@unima.ac.mw",
            name = "Suspended User",
            role = UserRole.STUDENT,
            status = UserStatus.SUSPENDED
        )
        fakeAuthRepository.users["user_suspended"] = suspendedUser
        fakeAuthRepository.passwords["user_suspended"] = "pass1234"

        val viewModel = SignInViewModel()
        var errorMsg: String? = null
        var navigatedRole: UserRole? = null

        viewModel.signIn("suspended@unima.ac.mw", "pass1234", onNavigate = { navigatedRole = it }, onError = { errorMsg = it })
        testDispatcher.scheduler.advanceUntilIdle()

        assertEquals("Your account has been suspended. Contact the administrator", errorMsg)
        assertNull(navigatedRole)
        assertEquals("Your account has been suspended. Contact the administrator", viewModel.uiState.value.errorMessage)
    }

    @Test
    fun signIn_validCredentials_navigatesToDashboard() = runTest(testDispatcher) {
        val validUser = User(
            uid = "user_1",
            email = "student@unima.ac.mw",
            name = "Student User",
            role = UserRole.STUDENT,
            status = UserStatus.ACTIVE
        )
        fakeAuthRepository.users["user_1"] = validUser
        fakeAuthRepository.passwords["user_1"] = "pass1234"

        val viewModel = SignInViewModel()
        var errorMsg: String? = null
        var navigatedRole: UserRole? = null

        viewModel.signIn("student@unima.ac.mw", "pass1234", onNavigate = { navigatedRole = it }, onError = { errorMsg = it })
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(errorMsg)
        assertEquals(UserRole.STUDENT, navigatedRole)
        assertNull(viewModel.uiState.value.errorMessage)
    }

    // --- SIGN-UP TESTS ---

    @Test
    fun signUp_student_success() = runTest(testDispatcher) {
        val viewModel = SignUpViewModel()
        var completed = false
        var errorMsg: String? = null

        val data = mapOf(
            "fullName" to "Jane Doe",
            "email" to "janedoe@unima.ac.mw",
            "password" to "Password123"
        )

        viewModel.signUpStudent(data, onError = { errorMsg = it }, onComplete = { completed = true })
        testDispatcher.scheduler.advanceUntilIdle()

        assertTrue(completed)
        assertNull(errorMsg)
        assertEquals(1, fakeAuthRepository.users.size)
        assertEquals("janedoe@unima.ac.mw", fakeAuthRepository.users.values.first().email)
    }

    @Test
    fun signUp_emailAlreadyExists_triggersError() = runTest(testDispatcher) {
        val existingUser = User(
            uid = "existing_1",
            email = "janedoe@unima.ac.mw",
            name = "Jane Existing",
            role = UserRole.STUDENT,
            status = UserStatus.ACTIVE
        )
        fakeAuthRepository.users["existing_1"] = existingUser

        val viewModel = SignUpViewModel()
        var completed = false
        var errorMsg: String? = null

        val data = mapOf(
            "fullName" to "Jane Doe",
            "email" to "janedoe@unima.ac.mw",
            "password" to "Password123"
        )

        viewModel.signUpStudent(data, onError = { errorMsg = it }, onComplete = { completed = true })
        testDispatcher.scheduler.advanceUntilIdle()

        assertFalse(completed)
        assertEquals("An account with this email already exists", errorMsg)
        assertEquals("An account with this email already exists", viewModel.uiState.value.errorMessage)
    }

    // --- SIGN-OUT TESTS ---

    @Test
    fun signOut_clearsCurrentUser() = runTest(testDispatcher) {
        val user = User(
            uid = "user_1",
            email = "test@unima.ac.mw",
            name = "Test User",
            role = UserRole.STUDENT,
            status = UserStatus.ACTIVE
        )
        fakeAuthRepository.loggedInUser = user

        val currentBefore = AppContainer.authRepository.fetchCurrentUser().getOrNull()
        assertEquals(user, currentBefore)

        fakeAuthRepository.signOut()

        val currentAfter = AppContainer.authRepository.fetchCurrentUser().getOrNull()
        assertNull(currentAfter)
    }
}
