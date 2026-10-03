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
import com.example.yaza_wanga.ui.SignInScreen
import com.example.yaza_wanga.ui.SignUpScreen
import com.example.yaza_wanga.ui.UserRole
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

enum class AppScreen {
    SIGN_IN,
    SIGN_UP
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

                    when (currentScreen) {
                        AppScreen.SIGN_IN -> {
                            SignInScreen(
                                onSignUpClick = {
                                    signUpRole = UserRole.STUDENT
                                    currentScreen = AppScreen.SIGN_UP
                                },
                                onStudentPortalClick = {
                                    // Can also open student portal or switch to sign up
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
fun MainPreview() {
    Yaza_WangaTheme {
        SignInScreen()
    }
}
