package com.example.yaza_wanga.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.components.YazaBottomBar
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.*
import com.example.yaza_wanga.ui.viewmodel.StudentDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateCourses: () -> Unit = {},
    onNavigateTutors: () -> Unit = {},
    onNavigateSessions: () -> Unit = {},
    onNavigateMaterials: () -> Unit = {},
    onNavigateAlerts: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onViewCourseClick: (String) -> Unit = {},
    viewModel: StudentDashboardViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val user = uiState.user

    val displayName = (user?.name?.ifBlank { user.email.substringBefore("@") } ?: user?.email?.substringBefore("@") ?: "").uppercase()
    val studentIdDisplay = user?.studentId?.ifBlank { user.email.substringBefore("@") } ?: user?.email?.substringBefore("@") ?: ""

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
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
        ) {
            // Header Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(primaryBlue)
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            YazaWangaLogo(size = 40.dp)
                            Column {
                                Text(
                                    text = "Champo Tutors",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "Peer Mentorship Portal",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = onNavigateAlerts,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Notifications",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }

                            IconButton(
                                onClick = onNavigateProfile,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color.White.copy(alpha = 0.15f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = "Profile",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "STUDENT DASHBOARD",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.8f),
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Welcome, $displayName",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "VERIFIED STUDENT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
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
                // Student Profile Info Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(modifier = Modifier.size(52.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(52.dp)
                                        .clip(CircleShape)
                                        .background(lightBlueBg)
                                        .border(2.dp, primaryBlue, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Avatar",
                                        tint = primaryBlue,
                                        modifier = Modifier.size(28.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.width(12.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = displayName,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textDark
                                )
                                Text(
                                    text = "Reg No: $studentIdDisplay",
                                    fontSize = 12.sp,
                                    color = textGray
                                )
                                Text(
                                    text = "${user?.department?.ifBlank { "Computer Science" }} • ${user?.yearOfStudy?.ifBlank { "Year 3" }}",
                                    fontSize = 11.sp,
                                    color = primaryBlue,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = "Manage Account",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Normal,
                                color = primaryBlue,
                                modifier = Modifier.clickable { onNavigateProfile() }
                            )
                        }

                        Spacer(modifier = Modifier.height(14.dp))
                        HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
                        Spacer(modifier = Modifier.height(14.dp))

                        // Middle Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = uiState.enrolledCourses.size.toString(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textDark
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Enrolled Courses",
                                    fontSize = 11.sp,
                                    color = textGray,
                                    maxLines = 1
                                )
                            }

                            VerticalDivider(
                                color = Color(0xFFE2E8F0),
                                modifier = Modifier.height(36.dp).padding(horizontal = 8.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onNavigateTutors() }
                            ) {
                                Text(
                                    text = uiState.activeTutorsCount.toString(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textDark
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Active Tutors",
                                    fontSize = 11.sp,
                                    color = textGray,
                                    maxLines = 1
                                )
                            }

                            VerticalDivider(
                                color = Color(0xFFE2E8F0),
                                modifier = Modifier.height(36.dp).padding(horizontal = 8.dp)
                            )

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { onNavigateSessions() }
                            ) {
                                Text(
                                    text = uiState.upcomingSessionsCount.toString(),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textDark
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Upcoming",
                                    fontSize = 11.sp,
                                    color = textGray,
                                    maxLines = 1
                                )
                            }
                        }

                        if (uiState.enrolledCourses.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(14.dp))
                            HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                uiState.enrolledCourses.take(2).forEach { enrollment ->
                                    Button(
                                        onClick = { onViewCourseClick(enrollment.courseCode) },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2)),
                                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(40.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                            contentDescription = null,
                                            tint = Color(0xFFDC2626),
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = enrollment.courseCode,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFFDC2626),
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Quick Actions Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Quick Actions",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )
                }

                val quickActions = listOf(
                    Triple("My Courses", Icons.AutoMirrored.Filled.MenuBook, onNavigateCourses),
                    Triple("My Sessions", Icons.Default.CalendarToday, onNavigateSessions),
                    Triple("Materials", Icons.Default.FolderOpen, onNavigateMaterials),
                    Triple("Progress", Icons.Default.TrendingUp, {}),
                    Triple("Tutors", Icons.Default.School, onNavigateTutors),
                    Triple("Settings", Icons.Default.Settings, {}),
                    Triple("Payments", Icons.Default.AccountBalanceWallet, {}),
                    Triple("Yaza Assist", Icons.Default.SmartToy, {})
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                        .padding(vertical = 12.dp, horizontal = 8.dp)
                ) {
                    quickActions.chunked(4).forEachIndexed { rowIndex, rowItems ->
                        if (rowIndex > 0) {
                            Spacer(modifier = Modifier.height(12.dp))
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
                                        modifier = Modifier.size(50.dp)
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
