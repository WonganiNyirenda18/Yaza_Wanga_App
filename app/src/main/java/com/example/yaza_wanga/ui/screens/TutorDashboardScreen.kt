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
import androidx.compose.material.icons.automirrored.filled.MenuBook
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
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorDashboardScreen(
    onNavigateDashboard: () -> Unit = {},
    onNavigateSessions: () -> Unit = {},
    onNavigateStudents: () -> Unit = {},
    onNavigateMenu: () -> Unit = {},
    onUploadNotesClick: () -> Unit = {},
    onStartCall: (String) -> Unit = {},
    onViewStudentDetails: (String) -> Unit = {}
) {
    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Column {
                            Text(
                                text = "PEER MENTORSHIP",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Yaza Wanga Tutor",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textDark
                            )
                        }
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
            // Welcome Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFFE6F4EA)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF137333)))
                            Text(
                                text = "Active Tutor • Verified",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF137333)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Good morning, Tutor John 👋",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Faculty of Science & Technology • Department of Math & Stats",
                        fontSize = 12.sp,
                        color = textGray
                    )
                }
            }

            // Summary Stats 2x2 Grid
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Active Students
                    TutorStatCard(
                        title = "Active Students",
                        value = "18",
                        subtext = "↗ +3 this month",
                        subtextColor = Color(0xFF10B981),
                        icon = Icons.Default.Groups,
                        modifier = Modifier.weight(1f)
                    )

                    // Courses Teaching
                    TutorStatCard(
                        title = "Courses Teaching",
                        value = "3 Courses",
                        subtext = "INF 212, STA 211,...",
                        subtextColor = textGray,
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Today's Sessions
                    TutorStatCard(
                        title = "Today's Sessions",
                        value = "2 Active",
                        subtext = "1 Online, 1 In-person",
                        subtextColor = textGray,
                        icon = Icons.Default.CalendarToday,
                        modifier = Modifier.weight(1f)
                    )

                    // Tutor Rating
                    TutorStatCard(
                        title = "Tutor Rating",
                        value = "4.9 ★",
                        subtext = "42 verified reviews",
                        subtextColor = textGray,
                        icon = Icons.Default.Star,
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

            // Upcoming Banner Card ("HAPPENING IN 15 MINS")
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
                                    text = "HAPPENING IN 15 MINS",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Text(
                            text = "16:00 – 17:30",
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
                                text = "INF 212: WEB DEVELOPMENT",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                            Text(
                                text = "Wongani Nyirenda",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "🎥 Google Meet • 90 min deep dive",
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
                            onClick = { onStartCall("Wongani Nyirenda") },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(40.dp)
                        ) {
                            Icon(Icons.Default.VideoCall, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Join Call Now", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White.copy(alpha = 0.2f)),
                            modifier = Modifier
                                .weight(0.8f)
                                .height(40.dp)
                        ) {
                            Icon(Icons.Default.Description, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Notes", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
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
                    modifier = Modifier.clickable {}
                ) {
                    Text("View All", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    Icon(Icons.Default.ChevronRight, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                }
            }

            // Session Item 1
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = lightBlueBg
                        ) {
                            Text(
                                text = "Today • 18:00 - 19:00",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(Icons.Default.Link, null, tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                            Text("Link Attached", fontSize = 11.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Davie Gondwe", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("INF 212 • CSS Grid & Responsive Layout", fontSize = 13.sp, color = textGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("🎥 Virtual Session", fontSize = 11.sp, color = textGray)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = { onStartCall("Davie Gondwe") },
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            modifier = Modifier
                                .weight(1.2f)
                                .height(38.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Start Call", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg),
                            modifier = Modifier
                                .weight(0.8f)
                                .height(38.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Chat, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Chat", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }
                    }
                }
            }

            // Student Progress & Tracking Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Student Progress & Tracking", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("Mentorship syllabus completion & milestones", fontSize = 12.sp, color = textGray)
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    modifier = Modifier.clickable {}
                ) {
                    Text("View All", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    Icon(Icons.Default.ChevronRight, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                }
            }

            // Tracking Card 1
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Wongani Nyirenda",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = textDark,
                                modifier = Modifier.clickable { onViewStudentDetails("Wongani Nyirenda") }
                            )
                            Text("INF 212 • Web Development", fontSize = 12.sp, color = textGray)
                        }

                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color(0xFFE6F4EA)
                        ) {
                            Text(
                                text = "Quiz: 88%",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF137333),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Syllabus Covered", fontSize = 11.sp, color = textGray)
                        Text("75% (9/12 topics)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { 0.75f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = primaryBlue,
                        trackColor = Color(0xFFE2E8F0)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Next: DOM Manipulation & APIs", fontSize = 11.sp, color = textGray)
                        Text("Log Notes", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue, modifier = Modifier.clickable {})
                    }
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
    subtext: String,
    subtextColor: Color,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    val textDark = Color(0xFF0F172A)
    val primaryBlue = Color(0xFF1D4ED8)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFF0F4FF)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(icon, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textDark)

            Spacer(modifier = Modifier.height(2.dp))

            Text(subtext, fontSize = 11.sp, color = subtextColor, fontWeight = FontWeight.Medium)
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
