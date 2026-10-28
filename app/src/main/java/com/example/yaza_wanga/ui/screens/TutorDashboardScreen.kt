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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.NoteAdd
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.example.yaza_wanga.ui.viewmodel.TutorDashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorDashboardScreen(
    viewModel: TutorDashboardViewModel = viewModel(),
    onNavigateDashboard: () -> Unit = {},
    onNavigateSessions: () -> Unit = {},
    onNavigateStudents: () -> Unit = {},
    onNavigateMenu: () -> Unit = {},
    onUploadNotesClick: () -> Unit = {},
    onStartCall: (String) -> Unit = {},
    onViewStudentDetails: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user

    val tutorName = user?.name?.ifBlank { user.email.substringBefore("@") } ?: user?.email?.substringBefore("@") ?: "Tutor"
    val greeting = "Good morning, Tutor $tutorName"

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Tutor Overview",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                    },
                    navigationIcon = {
                        Box(modifier = Modifier.padding(start = 12.dp)) {
                            YazaWangaLogo(size = 36.dp)
                        }
                    },
                    actions = {
                        Box(modifier = Modifier.padding(end = 12.dp)) {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Default.Notifications,
                                    contentDescription = "Alerts",
                                    tint = textDark
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-8).dp, y = 8.dp)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
                HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val tabs = listOf(
                    Triple(0, "Dashboard", Icons.Default.Home),
                    Triple(1, "Sessions", Icons.Default.CalendarToday),
                    Triple(2, "Students", Icons.Default.School),
                    Triple(3, "Menu", Icons.Default.Menu)
                )

                tabs.forEach { (index, label, icon) ->
                    val selected = index == 0 // Dashboard selected
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            when (index) {
                                0 -> onNavigateDashboard()
                                1 -> onNavigateSessions()
                                2 -> onNavigateStudents()
                                3 -> onNavigateMenu()
                            }
                        },
                        icon = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (selected) primaryBlue else textGray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(1.5.dp))
                                        .background(if (selected) primaryBlue else Color.Transparent)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) primaryBlue else textGray
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor = primaryBlue,
                            selectedTextColor = primaryBlue,
                            unselectedIconColor = textGray,
                            unselectedTextColor = textGray
                        )
                    )
                }
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = greeting,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )

            // Summary Stats 2x2 Grid (Real Stats from ViewModel)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TutorStatCard(
                        title = "Active Students",
                        value = "${uiState.activeStudentsCount}",
                        modifier = Modifier.weight(1f)
                    )

                    TutorStatCard(
                        title = "Courses Teaching",
                        value = "${uiState.coursesTeachingCount} Courses",
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TutorStatCard(
                        title = "Today's Sessions",
                        value = "${uiState.todaysSessionsCount} Active",
                        modifier = Modifier.weight(1f)
                    )

                    TutorStatCard(
                        title = "Tutor Rating",
                        value = uiState.tutorRating,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Quick Actions Row
            Column {
                Text(
                    text = "QUICK ACTIONS",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = textDark,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickActionButton(icon = Icons.Default.CalendarMonth, label = "Set Avail.") {}
                    QuickActionButton(icon = Icons.Default.Link, label = "Share Link") {}
                    QuickActionButton(icon = Icons.AutoMirrored.Filled.NoteAdd, label = "Upload Notes", onClick = onUploadNotesClick)
                    QuickActionButton(icon = Icons.Default.Tune, label = "Courses") {}
                }
            }

            // Upcoming Session Banner / Real Next Session Card
            val nextSession = uiState.nextUpcomingSession
            if (nextSession != null) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = primaryBlue),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color.White))
                                    Text(
                                        text = "NEXT UPCOMING SESSION",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }

                            Text(
                                text = nextSession.time,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color.White.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Laptop, null, tint = Color.White, modifier = Modifier.size(20.dp))
                            }

                            Column {
                                Text(
                                    text = nextSession.courseCode,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White.copy(alpha = 0.9f)
                                )
                                Text(
                                    text = nextSession.title.ifBlank { "Tutoring Session" },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Text(
                                    text = "🎥 ${nextSession.method} • Date: ${nextSession.date}",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onStartCall(nextSession.courseCode) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                                modifier = Modifier
                                    .weight(1.2f)
                                    .height(40.dp)
                            ) {
                                Icon(Icons.Default.VideoCall, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Join Call / Room", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            }
                        }
                    }
                }
            } else {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No upcoming sessions", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Scheduled Sessions Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Scheduled Sessions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("Upcoming 1-on-1 tutoring appointments", fontSize = 12.sp, color = textGray)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.clickable { onNavigateSessions() }
                ) {
                    Text("View All", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    Icon(Icons.Default.ChevronRight, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun TutorStatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    val textDark = Color(0xFF0F172A)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(6.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
        }
    }
}

@Composable
fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit = {}
) {
    val primaryBlue = Color(0xFF1D4ED8)
    val textDark = Color(0xFF0F172A)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .size(64.dp)
                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                .clickable(onClick = onClick),
            shadowElevation = 1.dp
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = primaryBlue, modifier = Modifier.size(24.dp))
            }
        }
        Text(label, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = textDark)
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1000)
@Composable
fun TutorDashboardScreenPreview() {
    Yaza_WangaTheme {
        TutorDashboardScreen()
    }
}
