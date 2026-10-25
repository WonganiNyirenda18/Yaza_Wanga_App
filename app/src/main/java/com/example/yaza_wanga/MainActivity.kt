package com.example.yaza_wanga

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.repository.AuthRepository
import com.example.yaza_wanga.ui.screens.*
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

enum class AppScreen {
    SPLASH,
    SIGN_IN,
    SIGN_UP,
    STUDENT_PROFILE,
    STUDENT_DASHBOARD,
    TUTOR_DASHBOARD,
    TUTOR_PROFILE,
    TUTOR_SESSION,
    TUTOR_STUDENTS,
    TUTOR_UPLOAD_MATERIAL,
    ADMIN_OVERVIEW,
    ADMIN_USERS,
    ADMIN_COURSES,
    ADMIN_CREATE_COURSE,
    ADMIN_CONTENT,
    COURSE_DIRECTORY,
    COURSE_DETAILS
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Yaza_WangaTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var currentScreen by remember { mutableStateOf<AppScreen?>(null) }
                    var signUpRole by remember { mutableStateOf(UserRole.STUDENT) }
                    var selectedCourseCode by remember { mutableStateOf("INF 212") }
                    val coroutineScope = rememberCoroutineScope()

                    // Check session on app start
                    LaunchedEffect(Unit) {
                        val firebaseUser = AuthRepository.currentUser
                        if (firebaseUser != null) {
                            val result = AuthRepository.fetchCurrentUser()
                            if (result.isSuccess) {
                                val user = result.getOrNull()
                                if (user != null) {
                                    currentScreen = when (user.role) {
                                        UserRole.ADMIN -> AppScreen.ADMIN_OVERVIEW
                                        UserRole.TUTOR -> AppScreen.TUTOR_DASHBOARD
                                        UserRole.STUDENT -> AppScreen.STUDENT_DASHBOARD
                                    }
                                } else {
                                    currentScreen = AppScreen.STUDENT_DASHBOARD
                                }
                            } else {
                                currentScreen = AppScreen.SPLASH
                            }
                        } else {
                            currentScreen = AppScreen.SPLASH
                        }
                    }

                    fun navigateAdminTab(tabIndex: Int) {
                        currentScreen = when (tabIndex) {
                            0 -> AppScreen.ADMIN_OVERVIEW
                            1 -> AppScreen.ADMIN_USERS
                            2 -> AppScreen.ADMIN_COURSES
                            3 -> AppScreen.ADMIN_CONTENT
                            else -> AppScreen.ADMIN_OVERVIEW
                        }
                    }

                    if (currentScreen == null) {
                        SplashScreen(onSplashFinished = {})
                    } else {
                        when (currentScreen) {
                            AppScreen.SPLASH -> {
                                SplashScreen(
                                    onSplashFinished = {
                                        currentScreen = AppScreen.SIGN_IN
                                    }
                                )
                            }
                            AppScreen.SIGN_IN -> {
                                SignInScreen(
                                    onBackClick = {
                                        currentScreen = AppScreen.SIGN_IN
                                    },
                                    onProfileClick = {},
                                    onLoginClick = { email, password, onError ->
                                        coroutineScope.launch {
                                            val result = AuthRepository.signIn(email, password)
                                            if (result.isSuccess) {
                                                val user = result.getOrNull()
                                                currentScreen = when (user?.role) {
                                                    UserRole.ADMIN -> AppScreen.ADMIN_OVERVIEW
                                                    UserRole.TUTOR -> AppScreen.TUTOR_DASHBOARD
                                                    else -> AppScreen.STUDENT_DASHBOARD
                                                }
                                            } else {
                                                onError(result.exceptionOrNull()?.localizedMessage ?: "Invalid email or password")
                                            }
                                        }
                                    },
                                    onForgotPasswordClick = { email, onError ->
                                        coroutineScope.launch {
                                            val result = AuthRepository.sendPasswordResetEmail(email)
                                            if (result.isFailure) {
                                                onError(result.exceptionOrNull()?.localizedMessage ?: "Failed to send password reset email")
                                            } else {
                                                onError(null)
                                            }
                                        }
                                    },
                                    onSignUpClick = {
                                        signUpRole = UserRole.STUDENT
                                        currentScreen = AppScreen.SIGN_UP
                                    }
                                )
                            }
                            AppScreen.SIGN_UP -> {
                                SignUpScreen(
                                    initialRole = signUpRole,
                                    onBackClick = {
                                        currentScreen = AppScreen.SIGN_IN
                                    },
                                    onSignInClick = {
                                        currentScreen = AppScreen.SIGN_IN
                                    },
                                    onCreateStudentAccount = { data, onError ->
                                        val name = data["fullName"] ?: "Student"
                                        val email = data["email"] ?: ""
                                        val password = data["password"] ?: ""
                                        coroutineScope.launch {
                                            val result = AuthRepository.signUp(email, password, UserRole.STUDENT, name)
                                            if (result.isSuccess) {
                                                currentScreen = AppScreen.STUDENT_PROFILE
                                            } else {
                                                onError(result.exceptionOrNull()?.localizedMessage ?: "Sign up failed")
                                            }
                                        }
                                    },
                                    onCreateTutorAccount = { data, onError ->
                                        val name = data["fullName"] ?: "Tutor"
                                        val email = data["email"] ?: ""
                                        val password = data["password"] ?: ""
                                        val faculty = data["faculty"] ?: ""
                                        coroutineScope.launch {
                                            val result = AuthRepository.signUp(email, password, UserRole.TUTOR, name, faculty = faculty)
                                            if (result.isSuccess) {
                                                currentScreen = AppScreen.TUTOR_PROFILE
                                            } else {
                                                onError(result.exceptionOrNull()?.localizedMessage ?: "Sign up failed")
                                            }
                                        }
                                    }
                                )
                            }
                            AppScreen.STUDENT_PROFILE -> {
                                StudentProfileScreen(
                                    onBackClick = {
                                        AuthRepository.signOut()
                                        currentScreen = AppScreen.SIGN_IN
                                    },
                                    onProfileClick = {},
                                    onSaveAndContinue = { data, onError ->
                                        coroutineScope.launch {
                                            val uid = AuthRepository.currentUser?.uid
                                            if (uid != null) {
                                                try {
                                                    val db = FirebaseFirestore.getInstance()
                                                    db.collection("users").document(uid).update(
                                                        mapOf(
                                                            "department" to (data["department"] ?: ""),
                                                            "programme" to (data["programme"] ?: ""),
                                                            "yearOfStudy" to (data["yearOfStudy"] ?: ""),
                                                            "phone" to (data["phone"] ?: ""),
                                                            "status" to "ACTIVE"
                                                        )
                                                    ).await()
                                                    currentScreen = AppScreen.STUDENT_DASHBOARD
                                                } catch (e: Exception) {
                                                    onError(e.localizedMessage ?: "Failed to save profile")
                                                }
                                            } else {
                                                currentScreen = AppScreen.STUDENT_DASHBOARD
                                            }
                                        }
                                    }
                                )
                            }
                            AppScreen.STUDENT_DASHBOARD -> {
                                StudentDashboardScreen(
                                    onNavigateHome = {
                                        currentScreen = AppScreen.STUDENT_DASHBOARD
                                    },
                                    onNavigateCourses = {
                                        currentScreen = AppScreen.COURSE_DIRECTORY
                                    },
                                    onNavigateTutors = {
                                        currentScreen = AppScreen.COURSE_DIRECTORY
                                    },
                                    onNavigateAlerts = {},
                                    onNavigateProfile = {
                                        currentScreen = AppScreen.STUDENT_PROFILE
                                    },
                                    onViewCourseClick = { code ->
                                        selectedCourseCode = code
                                        currentScreen = AppScreen.COURSE_DETAILS
                                    }
                                )
                            }
                            AppScreen.TUTOR_DASHBOARD -> {
                                TutorDashboardScreen(
                                    onNavigateDashboard = {
                                        currentScreen = AppScreen.TUTOR_DASHBOARD
                                    },
                                    onNavigateSessions = {
                                        currentScreen = AppScreen.TUTOR_SESSION
                                    },
                                    onNavigateStudents = {
                                        currentScreen = AppScreen.TUTOR_STUDENTS
                                    },
                                    onNavigateMenu = {
                                        currentScreen = AppScreen.TUTOR_PROFILE
                                    },
                                    onUploadNotesClick = {
                                        currentScreen = AppScreen.TUTOR_UPLOAD_MATERIAL
                                    },
                                    onStartCall = { _ -> },
                                    onViewStudentDetails = { _ -> }
                                )
                            }
                            AppScreen.TUTOR_PROFILE -> {
                                TutorProfileScreen(
                                    onBackClick = {
                                        AuthRepository.signOut()
                                        currentScreen = AppScreen.SIGN_IN
                                    },
                                    onProfileClick = {},
                                    onCompleteSetup = {
                                        currentScreen = AppScreen.TUTOR_DASHBOARD
                                    }
                                )
                            }
                            AppScreen.TUTOR_SESSION -> {
                                TutorCreateSessionScreen(
                                    onBackClick = {
                                        currentScreen = AppScreen.TUTOR_DASHBOARD
                                    },
                                    onNavigateDashboard = {
                                        currentScreen = AppScreen.TUTOR_DASHBOARD
                                    },
                                    onNavigateSessions = {
                                        currentScreen = AppScreen.TUTOR_SESSION
                                    },
                                    onNavigateStudents = {
                                        currentScreen = AppScreen.TUTOR_STUDENTS
                                    },
                                    onNavigateMenu = {
                                        currentScreen = AppScreen.TUTOR_PROFILE
                                    },
                                    onCreateSessionSuccess = {
                                        currentScreen = AppScreen.TUTOR_DASHBOARD
                                    }
                                )
                            }
                            AppScreen.TUTOR_STUDENTS -> {
                                TutorStudentsScreen(
                                    onNavigateDashboard = {
                                        currentScreen = AppScreen.TUTOR_DASHBOARD
                                    },
                                    onNavigateSessions = {
                                        currentScreen = AppScreen.TUTOR_SESSION
                                    },
                                    onNavigateStudents = {
                                        currentScreen = AppScreen.TUTOR_STUDENTS
                                    },
                                    onNavigateMenu = {
                                        currentScreen = AppScreen.TUTOR_PROFILE
                                    },
                                    onViewStudentDetails = { _ -> }
                                )
                            }
                            AppScreen.TUTOR_UPLOAD_MATERIAL -> {
                                TutorUploadMaterialScreen(
                                    onBackClick = {
                                        currentScreen = AppScreen.TUTOR_DASHBOARD
                                    },
                                    onNavigateDashboard = {
                                        currentScreen = AppScreen.TUTOR_DASHBOARD
                                    },
                                    onNavigateSessions = {
                                        currentScreen = AppScreen.TUTOR_SESSION
                                    },
                                    onNavigateStudents = {
                                        currentScreen = AppScreen.TUTOR_STUDENTS
                                    },
                                    onNavigateMenu = {
                                        currentScreen = AppScreen.TUTOR_PROFILE
                                    },
                                    onPublishSuccess = {
                                        currentScreen = AppScreen.TUTOR_DASHBOARD
                                    }
                                )
                            }
                            AppScreen.ADMIN_OVERVIEW -> {
                                AdminOverviewScreen(
                                    onNavigateTab = ::navigateAdminTab
                                )
                            }
                            AppScreen.ADMIN_USERS -> {
                                AdminUsersScreen(
                                    onNavigateTab = ::navigateAdminTab
                                )
                            }
                            AppScreen.ADMIN_COURSES -> {
                                AdminCoursesScreen(
                                    onNavigateTab = ::navigateAdminTab,
                                    onNewCourseClick = {
                                        currentScreen = AppScreen.ADMIN_CREATE_COURSE
                                    }
                                )
                            }
                            AppScreen.ADMIN_CREATE_COURSE -> {
                                AdminCreateCourseScreen(
                                    onBackClick = {
                                        currentScreen = AppScreen.ADMIN_COURSES
                                    },
                                    onNavigateTab = ::navigateAdminTab,
                                    onPublishSuccess = {
                                        currentScreen = AppScreen.ADMIN_COURSES
                                    }
                                )
                            }
                            AppScreen.ADMIN_CONTENT -> {
                                AdminContentScreen(
                                    onNavigateTab = ::navigateAdminTab
                                )
                            }
                            AppScreen.COURSE_DIRECTORY -> {
                                CourseDirectoryScreen(
                                    onNavigateHome = {
                                        currentScreen = AppScreen.STUDENT_DASHBOARD
                                    },
                                    onNavigateCourses = {
                                        currentScreen = AppScreen.COURSE_DIRECTORY
                                    },
                                    onNavigateTutors = {
                                        currentScreen = AppScreen.COURSE_DIRECTORY
                                    },
                                    onNavigateAlerts = {},
                                    onNavigateProfile = {
                                        currentScreen = AppScreen.STUDENT_PROFILE
                                    },
                                    onViewDetailsClick = { code ->
                                        selectedCourseCode = code
                                        currentScreen = AppScreen.COURSE_DETAILS
                                    }
                                )
                            }
                            AppScreen.COURSE_DETAILS -> {
                                CourseDetailsScreen(
                                    courseCode = selectedCourseCode,
                                    onBackClick = {
                                        currentScreen = AppScreen.COURSE_DIRECTORY
                                    },
                                    onProfileClick = {
                                        currentScreen = AppScreen.STUDENT_PROFILE
                                    },
                                    onContactTutor = { _ -> }
                                )
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainAppPreview() {
    Yaza_WangaTheme {
        StudentDashboardScreen()
    }
}
