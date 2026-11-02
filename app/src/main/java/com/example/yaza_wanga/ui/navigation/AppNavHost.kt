package com.example.yaza_wanga.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.yaza_wanga.data.model.*
import com.example.yaza_wanga.ui.screens.*
import com.example.yaza_wanga.ui.viewmodel.*

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    
    object AuthGraph : Screen("auth_graph") {
        object SignIn : Screen("auth/signin")
        object SignUp : Screen("auth/signup")
    }

    object StudentGraph : Screen("student_graph") {
        object Dashboard : Screen("student/dashboard")
        object Profile : Screen("student/profile")
        object CourseDirectory : Screen("student/courses")
        object CourseDetails : Screen("student/courses/{courseCode}") {
            fun createRoute(courseCode: String) = "student/courses/$courseCode"
        }
    }

    object TutorGraph : Screen("tutor_graph") {
        object Dashboard : Screen("tutor/dashboard")
        object Profile : Screen("tutor/profile")
        object Session : Screen("tutor/session")
        object Students : Screen("tutor/students")
        object CreateSession : Screen("tutor/create_session")
        object UploadMaterial : Screen("tutor/upload_material")
    }

    object AdminGraph : Screen("admin_graph") {
        object Overview : Screen("admin/overview")
        object Users : Screen("admin/users")
        object Courses : Screen("admin/courses")
        object CreateCourse : Screen("admin/create_course")
        object Content : Screen("admin/content")
    }
}

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    var userRole by remember { mutableStateOf<UserRole?>(null) }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        // Splash
        composable(Screen.Splash.route) {
            SplashScreen(
                onSplashFinished = {
                    navController.navigate(Screen.AuthGraph.SignIn.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Auth Graph
        navigation(
            startDestination = Screen.AuthGraph.SignIn.route,
            route = Screen.AuthGraph.route
        ) {
            composable(Screen.AuthGraph.SignIn.route) {
                val viewModel: SignInViewModel = viewModel()
                SignInScreen(
                    onBackClick = { navController.popBackStack() },
                    onProfileClick = {},
                    onLoginClick = { email, password, onError ->
                        viewModel.signIn(email, password, onNavigate = { role ->
                            userRole = role
                            val dest = when (role) {
                                UserRole.ADMIN -> Screen.AdminGraph.Overview.route
                                UserRole.TUTOR -> Screen.TutorGraph.Dashboard.route
                                UserRole.STUDENT -> Screen.StudentGraph.Dashboard.route
                            }
                            navController.navigate(dest) {
                                popUpTo(Screen.AuthGraph.route) { inclusive = true }
                            }
                        }, onError = onError)
                    },
                    onGoogleSignInClick = {},
                    onForgotPasswordClick = { email, onError ->
                        viewModel.forgotPassword(email, onError)
                    },
                    onSignUpClick = {
                        navController.navigate(Screen.AuthGraph.SignUp.route)
                    }
                )
            }
            composable(Screen.AuthGraph.SignUp.route) {
                val viewModel: SignUpViewModel = viewModel()
                SignUpScreen(
                    initialRole = UserRole.STUDENT,
                    onBackClick = { navController.popBackStack() },
                    onSignInClick = { navController.navigate(Screen.AuthGraph.SignIn.route) { popUpTo(Screen.AuthGraph.SignUp.route) { inclusive = true } } },
                    onCreateStudentAccount = { data, onError ->
                        viewModel.signUpStudent(data, onError, onComplete = {
                            navController.navigate(Screen.StudentGraph.Profile.route) {
                                popUpTo(Screen.AuthGraph.route) { inclusive = true }
                            }
                        })
                    },
                    onCreateTutorAccount = { data, onError ->
                        viewModel.signUpTutor(data, onError, onComplete = {
                            navController.navigate(Screen.TutorGraph.Dashboard.route) {
                                popUpTo(Screen.AuthGraph.route) { inclusive = true }
                            }
                        })
                    }
                )
            }
        }

        // Student Graph (Guarded: only STUDENT or ADMIN)
        navigation(
            startDestination = Screen.StudentGraph.Dashboard.route,
            route = Screen.StudentGraph.route
        ) {
            composable(Screen.StudentGraph.Dashboard.route) {
                if (userRole == UserRole.TUTOR) {
                    LaunchedEffect(Unit) { navController.navigate(Screen.TutorGraph.Dashboard.route) { popUpTo(Screen.StudentGraph.route) { inclusive = true } } }
                    return@composable
                }
                StudentDashboardScreen(
                    onNavigateHome = {},
                    onNavigateCourses = { navController.navigate(Screen.StudentGraph.CourseDirectory.route) },
                    onNavigateTutors = {},
                    onNavigateAlerts = {},
                    onNavigateProfile = { navController.navigate(Screen.StudentGraph.Profile.route) },
                    onViewCourseClick = { code -> navController.navigate(Screen.StudentGraph.CourseDetails.createRoute(code)) }
                )
            }
            composable(Screen.StudentGraph.Profile.route) {
                val viewModel: StudentProfileViewModel = viewModel()
                StudentProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onProfileClick = {},
                    onSaveAndContinue = { data, onError ->
                        viewModel.saveProfile(data, onError, onComplete = {
                            navController.navigate(Screen.StudentGraph.Dashboard.route) {
                                popUpTo(Screen.StudentGraph.Profile.route) { inclusive = true }
                            }
                        })
                    }
                )
            }
            composable(Screen.StudentGraph.CourseDirectory.route) {
                CourseDirectoryScreen(
                    onNavigateHome = { navController.navigate(Screen.StudentGraph.Dashboard.route) },
                    onNavigateCourses = {},
                    onNavigateTutors = {},
                    onNavigateAlerts = {},
                    onNavigateProfile = { navController.navigate(Screen.StudentGraph.Profile.route) },
                    onViewDetailsClick = { code -> navController.navigate(Screen.StudentGraph.CourseDetails.createRoute(code)) }
                )
            }
            composable(
                route = Screen.StudentGraph.CourseDetails.route,
                arguments = listOf(navArgument("courseCode") { type = NavType.StringType })
            ) { backStackEntry ->
                val courseCode = backStackEntry.arguments?.getString("courseCode") ?: "INF 212"
                val viewModel: CourseDetailsViewModel = viewModel()
                LaunchedEffect(courseCode) { viewModel.loadCourse(courseCode) }
                CourseDetailsScreen(
                    courseCode = courseCode,
                    onBackClick = { navController.popBackStack() },
                    onProfileClick = { navController.navigate(Screen.StudentGraph.Profile.route) },
                    onContactTutor = { tutorName -> }
                )
            }
        }

        // Tutor Graph (Guarded: only TUTOR or ADMIN; Students blocked)
        navigation(
            startDestination = Screen.TutorGraph.Dashboard.route,
            route = Screen.TutorGraph.route
        ) {
            composable(Screen.TutorGraph.Dashboard.route) {
                if (userRole == UserRole.STUDENT) {
                    LaunchedEffect(Unit) { navController.navigate(Screen.StudentGraph.Dashboard.route) { popUpTo(Screen.TutorGraph.route) { inclusive = true } } }
                    return@composable
                }
                TutorDashboardScreen(
                    onNavigateDashboard = {},
                    onNavigateSessions = { navController.navigate(Screen.TutorGraph.Session.route) },
                    onNavigateStudents = { navController.navigate(Screen.TutorGraph.Students.route) },
                    onNavigateMenu = { navController.navigate(Screen.TutorGraph.Profile.route) },
                    onUploadNotesClick = { navController.navigate(Screen.TutorGraph.UploadMaterial.route) },
                    onStartCall = {},
                    onViewStudentDetails = {}
                )
            }
            composable(Screen.TutorGraph.Profile.route) {
                TutorProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onProfileClick = {},
                    onCompleteSetup = { navController.navigate(Screen.TutorGraph.Dashboard.route) }
                )
            }
            composable(Screen.TutorGraph.Session.route) {
                TutorCreateSessionScreen(
                    onBackClick = { navController.popBackStack() },
                    onNavigateDashboard = { navController.navigate(Screen.TutorGraph.Dashboard.route) },
                    onNavigateSessions = {},
                    onNavigateStudents = { navController.navigate(Screen.TutorGraph.Students.route) },
                    onNavigateMenu = { navController.navigate(Screen.TutorGraph.Profile.route) },
                    onCreateSessionSuccess = { navController.navigate(Screen.TutorGraph.Dashboard.route) }
                )
            }
            composable(Screen.TutorGraph.Students.route) {
                TutorStudentsScreen(
                    onNavigateDashboard = { navController.navigate(Screen.TutorGraph.Dashboard.route) },
                    onNavigateSessions = { navController.navigate(Screen.TutorGraph.Session.route) },
                    onNavigateStudents = {},
                    onNavigateMenu = { navController.navigate(Screen.TutorGraph.Profile.route) },
                    onViewStudentDetails = {}
                )
            }
            composable(Screen.TutorGraph.CreateSession.route) {
                TutorCreateSessionScreen(
                    onBackClick = { navController.popBackStack() },
                    onNavigateDashboard = { navController.navigate(Screen.TutorGraph.Dashboard.route) },
                    onNavigateSessions = {},
                    onNavigateStudents = { navController.navigate(Screen.TutorGraph.Students.route) },
                    onNavigateMenu = { navController.navigate(Screen.TutorGraph.Profile.route) },
                    onCreateSessionSuccess = { navController.popBackStack() }
                )
            }
            composable(Screen.TutorGraph.UploadMaterial.route) {
                TutorUploadMaterialScreen(
                    onBackClick = { navController.popBackStack() },
                    onNavigateDashboard = { navController.navigate(Screen.TutorGraph.Dashboard.route) },
                    onNavigateSessions = { navController.navigate(Screen.TutorGraph.Session.route) },
                    onNavigateStudents = { navController.navigate(Screen.TutorGraph.Students.route) },
                    onNavigateMenu = { navController.navigate(Screen.TutorGraph.Profile.route) },
                    onPublishSuccess = { navController.popBackStack() }
                )
            }
        }

        // Admin Graph (Guarded: only ADMIN)
        navigation(
            startDestination = Screen.AdminGraph.Overview.route,
            route = Screen.AdminGraph.route
        ) {
            composable(Screen.AdminGraph.Overview.route) {
                if (userRole != UserRole.ADMIN) {
                    LaunchedEffect(Unit) {
                        val dest = if (userRole == UserRole.TUTOR) Screen.TutorGraph.Dashboard.route else Screen.StudentGraph.Dashboard.route
                        navController.navigate(dest) { popUpTo(Screen.AdminGraph.route) { inclusive = true } }
                    }
                    return@composable
                }
                AdminOverviewScreen(
                    onNavigateTab = { index ->
                        when (index) {
                            0 -> navController.navigate(Screen.AdminGraph.Overview.route)
                            1 -> navController.navigate(Screen.AdminGraph.Users.route)
                            2 -> navController.navigate(Screen.AdminGraph.Courses.route)
                            3 -> navController.navigate(Screen.AdminGraph.Content.route)
                        }
                    }
                )
            }
            composable(Screen.AdminGraph.Users.route) {
                if (userRole != UserRole.ADMIN) {
                    navController.popBackStack()
                    return@composable
                }
                AdminUsersScreen(
                    onNavigateTab = { index ->
                        when (index) {
                            0 -> navController.navigate(Screen.AdminGraph.Overview.route)
                            1 -> navController.navigate(Screen.AdminGraph.Users.route)
                            2 -> navController.navigate(Screen.AdminGraph.Courses.route)
                            3 -> navController.navigate(Screen.AdminGraph.Content.route)
                        }
                    }
                )
            }
            composable(Screen.AdminGraph.Courses.route) {
                if (userRole != UserRole.ADMIN) {
                    navController.popBackStack()
                    return@composable
                }
                AdminCoursesScreen(
                    onNavigateTab = { index ->
                        when (index) {
                            0 -> navController.navigate(Screen.AdminGraph.Overview.route)
                            1 -> navController.navigate(Screen.AdminGraph.Users.route)
                            2 -> navController.navigate(Screen.AdminGraph.Courses.route)
                            3 -> navController.navigate(Screen.AdminGraph.Content.route)
                        }
                    },
                    onNewCourseClick = { navController.navigate(Screen.AdminGraph.CreateCourse.route) }
                )
            }
            composable(Screen.AdminGraph.CreateCourse.route) {
                if (userRole != UserRole.ADMIN) {
                    navController.popBackStack()
                    return@composable
                }
                AdminCreateCourseScreen(
                    onBackClick = { navController.popBackStack() },
                    onNavigateTab = {},
                    onPublishSuccess = { navController.popBackStack() }
                )
            }
            composable(Screen.AdminGraph.Content.route) {
                if (userRole != UserRole.ADMIN) {
                    navController.popBackStack()
                    return@composable
                }
                AdminContentScreen(
                    onNavigateTab = { index ->
                        when (index) {
                            0 -> navController.navigate(Screen.AdminGraph.Overview.route)
                            1 -> navController.navigate(Screen.AdminGraph.Users.route)
                            2 -> navController.navigate(Screen.AdminGraph.Courses.route)
                            3 -> navController.navigate(Screen.AdminGraph.Content.route)
                        }
                    }
                )
            }
        }
    }
}
