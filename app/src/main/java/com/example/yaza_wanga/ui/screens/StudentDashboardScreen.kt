package com.example.yaza_wanga.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.components.YazaBottomBar
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.*
import com.example.yaza_wanga.ui.viewmodel.StudentDashboardViewModel
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateCourses: () -> Unit = {},
    onNavigateTutors: () -> Unit = {},
    onNavigateSessions: () -> Unit = {},
    onNavigateMaterials: () -> Unit = {},
    onNavigateProgress: () -> Unit = {},
    onNavigateAlerts: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onViewCourseClick: (String) -> Unit = {},
    viewModel: StudentDashboardViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val user = uiState.user

    val rawName = user?.name?.ifBlank { user.email.substringBefore("@") } ?: user?.email?.substringBefore("@") ?: "Student"
    val displayName = rawName.split(" ").filter { it.isNotBlank() }.joinToString(" ") { word ->
        word.lowercase().replaceFirstChar { char -> if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString() }
    }
    val studentIdDisplay = user?.studentId?.ifBlank { user.email.substringBefore("@") } ?: user?.email?.substringBefore("@") ?: ""

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFEFF6FF)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        bottomBar = {
            YazaBottomBar(
                tabs = listOf(
                    Triple(0, "Home", Icons.Default.Home),
                    Triple(1, "Courses", Icons.AutoMirrored.Filled.MenuBook),
                    Triple(2, "Tutors", Icons.Default.School),
                    Triple(3, "Alerts", Icons.Default.Notifications),
                    Triple(4, "Menu", Icons.Default.Menu)
                ),
                currentTab = 0,
                onSelectTab = { index ->
                    when (index) {
                        0 -> onNavigateHome()
                        1 -> onNavigateCourses()
                        2 -> onNavigateTutors()
                        3 -> onNavigateAlerts()
                        4 -> onNavigateProfile()
                    }
                }
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFFF8FAFC))
        ) {
            // Scrollable Header Block: Blue Background & Overlapping White Card scroll together seamlessly
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Blue background banner at the top of the scroll view
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(primaryBlue)
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // TopAppBar on Blue Banner
                    TopAppBar(
                        title = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                YazaWangaLogo(size = 36.dp)
                                Text(
                                    text = "Yaza Wanga",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        },
                        actions = {
                            Box(modifier = Modifier.padding(end = 4.dp)) {
                                IconButton(onClick = onNavigateAlerts) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = "Alerts",
                                        tint = Color.White
                                    )
                                }
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFDC2626),
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .offset(x = (-4).dp, y = 4.dp)
                                ) {
                                    Text(
                                        text = "3",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            IconButton(
                                onClick = onNavigateProfile,
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.2f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )

                    // White Account Summary Card
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Top Row: Name & Manage Account
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Top
                            ) {
                                Column(modifier = Modifier.weight(1f).padding(end = 12.dp)) {
                                    Text(
                                        text = displayName,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textDark,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Student - $studentIdDisplay",
                                        fontSize = 12.sp,
                                        color = textGray,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                Text(
                                    text = "Manage Account",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = primaryBlue,
                                    modifier = Modifier.clickable { onNavigateProfile() }
                                )
                            }

                            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

                            // Stats Row
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = uiState.enrolledCourses.size.toString(),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textDark
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Enrolled",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryBlue
                                    )
                                    Text(
                                        text = "Enrolled Courses",
                                        fontSize = 11.sp,
                                        color = textGray,
                                        maxLines = 1
                                    )
                                }

                                VerticalDivider(
                                    color = Color(0xFFE2E8F0),
                                    modifier = Modifier.height(44.dp).padding(horizontal = 8.dp)
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onNavigateTutors() },
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = uiState.activeTutorsCount.toString(),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textDark
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Active",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryBlue
                                    )
                                    Text(
                                        text = "Active Tutors",
                                        fontSize = 11.sp,
                                        color = textGray,
                                        maxLines = 1
                                    )
                                }

                                VerticalDivider(
                                    color = Color(0xFFE2E8F0),
                                    modifier = Modifier.height(44.dp).padding(horizontal = 8.dp)
                                )

                                Column(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { onNavigateSessions() },
                                    horizontalAlignment = Alignment.Start
                                ) {
                                    Text(
                                        text = uiState.upcomingSessionsCount.toString(),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textDark
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = "Upcoming",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = primaryBlue
                                    )
                                    Text(
                                        text = "Upcoming Sessions",
                                        fontSize = 11.sp,
                                        color = textGray,
                                        maxLines = 1
                                    )
                                }
                            }

                            if (uiState.enrolledCourses.isNotEmpty()) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .horizontalScroll(rememberScrollState()),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    uiState.enrolledCourses.forEach { enrollment ->
                                        Button(
                                            onClick = { onViewCourseClick(enrollment.courseCode) },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg),
                                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                            modifier = Modifier.height(40.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                                contentDescription = null,
                                                tint = primaryBlue,
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(
                                                text = enrollment.courseCode,
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = primaryBlue,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Quick Actions Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quick Actions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )

                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = primaryBlue,
                        modifier = Modifier.clickable { onNavigateCourses() }
                    )
                }

                // Quick Actions Container Card
                val quickActions = listOf(
                    Triple("My Courses", Icons.AutoMirrored.Filled.MenuBook, onNavigateCourses),
                    Triple("My Sessions", Icons.Default.CalendarToday, onNavigateSessions),
                    Triple("Materials", Icons.Default.FolderOpen, onNavigateMaterials),
                    Triple("Progress", Icons.Default.TrendingUp, onNavigateProgress),
                    Triple("Tutors", Icons.Default.School, onNavigateTutors),
                    Triple("Settings", Icons.Default.Settings, onNavigateProfile),
                    Triple("Payments", Icons.Default.AccountBalanceWallet, { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() }),
                    Triple("Yaza Assist", Icons.Default.SmartToy, { Toast.makeText(context, "Coming soon", Toast.LENGTH_SHORT).show() })
                )

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp, horizontal = 8.dp)
                    ) {
                        quickActions.chunked(4).forEachIndexed { rowIndex, rowItems ->
                            if (rowIndex > 0) {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                rowItems.forEach { (label, icon, onClick) ->
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(6.dp),
                                        modifier = Modifier
                                            .width(72.dp)
                                            .clickable(onClick = onClick)
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(14.dp),
                                            color = lightBlueBg,
                                            modifier = Modifier.size(52.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = icon,
                                                    contentDescription = label,
                                                    tint = primaryBlue,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }
                                        Text(
                                            text = label,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = textDark,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Promotional Banners Carousel
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = primaryBlue),
                        modifier = Modifier
                            .width(300.dp)
                            .height(135.dp)
                            .clickable { onNavigateTutors() }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(50.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Text("PEER TUTORING", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                }
                                Text("Connect with Top Tutors", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Pair with verified peer mentors for exam prep & coursework.", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f), maxLines = 2)
                            }

                            Text("Explore Tutors →", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF4F46E5)),
                        modifier = Modifier
                            .width(300.dp)
                            .height(135.dp)
                            .clickable { onNavigateMaterials() }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(50.dp),
                                    color = Color.White.copy(alpha = 0.2f)
                                ) {
                                    Text("STUDY ARTIFACTS", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp))
                                }
                                Text("Course Materials Directory", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                Text("Access lecture notes, past papers, and study guides.", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f), maxLines = 2)
                            }

                            Text("Browse Notes →", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1000)
@Composable
fun StudentDashboardPreview() {
    Yaza_WangaTheme {
        StudentDashboardScreen()
    }
}
