package com.example.yaza_wanga.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.components.SystemBarsStyle
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.example.yaza_wanga.ui.theme.primaryBlue
import com.example.yaza_wanga.ui.viewmodel.AppSessionUiState
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    sessionState: AppSessionUiState = AppSessionUiState(isLoading = false),
    onSplashFinished: () -> Unit = {}
) {
    SystemBarsStyle(darkIcons = false)

    val words = listOf("Connect", "Learn", "Progress")
    var currentIndex by remember { mutableIntStateOf(0) }
    var delayFinished by remember { mutableStateOf(false) }

    // Cycle through slideshow words for min duration ~2.7s
    LaunchedEffect(Unit) {
        repeat(3) {
            delay(900)
            currentIndex = (currentIndex + 1) % words.size
        }
        delayFinished = true
    }

    // Wait for both slideshow delay AND session state loading completion
    LaunchedEffect(delayFinished, sessionState.isLoading) {
        if (delayFinished && !sessionState.isLoading) {
            onSplashFinished()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(primaryBlue),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            YazaWangaLogo(size = 96.dp)

            Text(
                text = "Yaza Wanga",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = 1.2.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedContent(
                targetState = words[currentIndex],
                transitionSpec = {
                    (fadeIn() + slideInVertically { height -> height / 2 }) togetherWith
                            (fadeOut() + slideOutVertically { height -> -height / 2 })
                },
                label = "SplashSlideshow"
            ) { targetWord ->
                Text(
                    text = targetWord,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White.copy(alpha = 0.9f),
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun SplashPreviewSmall() {
    Yaza_WangaTheme {
        SplashScreen()
    }
}
