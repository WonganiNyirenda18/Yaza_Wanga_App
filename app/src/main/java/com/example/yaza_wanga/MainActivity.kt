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
import com.example.yaza_wanga.ui.screens.*
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

enum class AppScreen {
    SPLASH,
    SIGN_IN,
    SIGN_UP,
    STUDENT_PROFILE,
    STUDENT_DASHBOARD,
    TUTOR_DASHBOARD,
    TUTOR_SESSION,
    TUTOR_STUDENTS,
    TUTOR_UPLOAD_MATERIAL,
    ADMIN_OVERVIEW,
    ADMIN_USERS,
    ADMIN_COURSES,
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
                    var currentScreen by remember { mutableStateOf(AppScreen.SPLASH) }
                    var signUpRole by remember { mutableStateOf(UserRole.STUDENT) }
                    var selectedCourseCode by remember { mutableStateOf("INF 212") }

                    fun navigateAdminTab(tabIndex: Int) {
                        currentScreen = when (tabIndex) {
                            0 -> AppScreen.ADMIN_OVERVIEW
                            1 -> AppScreen.ADMIN_USERS
                            2 -> AppScreen.ADMIN_COURSES
                            3 -> AppScreen.ADMIN_CONTENT
                            else -> AppScreen.ADMIN_OVERVIEW
                        }
                    }

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
                                onSignUpClick = {
                                    signUpRole = UserRole.STUDENT
                                    currentScreen = AppScreen.SIGN_UP
                                },
                                onLoginClick = { email, password ->
                                    val trimmedEmail = email.trim().lowercase()
                                    if ((trimmedEmail == "admin" || trimmedEmail == "admin@unima.ac.mw") && password == "admin") {
                                        currentScreen = AppScreen.ADMIN_OVERVIEW
                                    } else if ((trimmedEmail == "tutor" || trimmedEmail == "tutor@unima.ac.mw") && password == "tutor") {
                                        currentScreen = AppScreen.TUTOR_DASHBOARD
                                    } else {
                                        currentScreen = AppScreen.STUDENT_PROFILE
                                    }
                                },
                                onGoogleSignInClick = {
                                    currentScreen = AppScreen.STUDENT_PROFILE
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
                                onCreateStudentAccount = { _ ->
                                    currentScreen = AppScreen.STUDENT_PROFILE
                                },
                                onCreateTutorAccount = { _ ->
                                    currentScreen = AppScreen.TUTOR_DASHBOARD
                                }
                            )
                        }
                        AppScreen.STUDENT_PROFILE -> {
                            StudentProfileScreen(
                                onBackClick = {
                                    currentScreen = AppScreen.SIGN_IN
                                },
                                onProfileClick = {
                                    // Profile
                                },
                                onSaveAndContinue = {
                                    currentScreen = AppScreen.STUDENT_DASHBOARD
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
                                onNavigateAlerts = {
                                    // Alerts
                                },
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
                                onNavigateProfile = {
                                    currentScreen = AppScreen.STUDENT_PROFILE
                                },
                                onStartCall = { _ -> },
                                onViewStudentDetails = { _ -> }
                            )
                        }
                        AppScreen.TUTOR_SESSION -> {
                            TutorSessionScreen(
                                onNavigateRequests = {
                                    currentScreen = AppScreen.TUTOR_DASHBOARD
                                },
                                onNavigateAnalytics = {
                                    currentScreen = AppScreen.ADMIN_OVERVIEW
                                },
                                onNavigateUpload = {
                                    currentScreen = AppScreen.TUTOR_UPLOAD_MATERIAL
                                },
                                onNavigateVerify = {
                                    currentScreen = AppScreen.TUTOR_STUDENTS
                                },
                                onAcceptRequest = { _ -> },
                                onDeclineRequest = { _ -> },
                                onAttachLink = { _ -> }
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
                                onNavigateProfile = {
                                    currentScreen = AppScreen.STUDENT_PROFILE
                                },
                                onMessageStudent = { _ -> },
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
                                onNavigateProfile = {
                                    currentScreen = AppScreen.STUDENT_PROFILE
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
                                onNavigateTab = ::navigateAdminTab
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
                                onNavigateAlerts = {
                                    // Alerts
                                },
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
