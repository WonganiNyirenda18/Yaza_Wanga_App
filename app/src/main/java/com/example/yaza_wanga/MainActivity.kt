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
import com.example.yaza_wanga.ui.*
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

enum class AppScreen {
    SIGN_IN,
    SIGN_UP,
    STUDENT_PROFILE,
    STUDENT_DASHBOARD,
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
                    var currentScreen by remember { mutableStateOf(AppScreen.SIGN_IN) }
                    var signUpRole by remember { mutableStateOf(UserRole.STUDENT) }
                    var selectedCourseCode by remember { mutableStateOf("INF 212") }

                    when (currentScreen) {
                        AppScreen.SIGN_IN -> {
                            SignInScreen(
                                onSignUpClick = {
                                    signUpRole = UserRole.STUDENT
                                    currentScreen = AppScreen.SIGN_UP
                                },
                                onLoginClick = { _, _ ->
                                    // After login, go to Student Profile Setup
                                    currentScreen = AppScreen.STUDENT_PROFILE
                                },
                                onStudentPortalClick = {
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
                                    // After account creation, redirect to Login / Profile
                                    currentScreen = AppScreen.STUDENT_PROFILE
                                },
                                onCreateTutorAccount = { _ ->
                                    currentScreen = AppScreen.STUDENT_PROFILE
                                }
                            )
                        }
                        AppScreen.STUDENT_PROFILE -> {
                            StudentProfileScreen(
                                onBackClick = {
                                    currentScreen = AppScreen.SIGN_IN
                                },
                                onProfileClick = {
                                    // Stay or profile
                                },
                                onSaveAndContinue = {
                                    // Proceed to Dashboard
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
                                    // Alerts / Notifications
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
                                onContactTutor = { _ ->
                                    // Contact tutor action
                                }
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
