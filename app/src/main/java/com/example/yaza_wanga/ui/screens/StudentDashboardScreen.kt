package com.example.yaza_wanga.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDashboardScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateCourses: () -> Unit = {},
    onNavigateTutors: () -> Unit = {},
    onNavigateAlerts: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onViewCourseClick: (String) -> Unit = {}
) {
    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Student Home",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                        Text(
                            text = "YAZA WANGA PEER HUB",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue,
                            letterSpacing = 0.5.sp
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(primaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = textDark
                        )
                    }
                    IconButton(
                        onClick = onNavigateProfile,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(primaryBlue)
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
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = onNavigateHome,
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryBlue, selectedTextColor = primaryBlue)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateCourses,
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Courses") },
                    label = { Text("Courses") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateTutors,
                    icon = { Icon(Icons.Default.School, contentDescription = "Tutors") },
                    label = { Text("Tutors") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateAlerts,
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
                    label = { Text("Alerts") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateProfile,
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
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
            // Action Buttons Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Icon(Icons.Default.Search, null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Find New Tutor", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFDBEAFE)),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = lightBlueBg),
                    modifier = Modifier
                        .weight(1f)
                        .height(44.dp)
                ) {
                    Icon(Icons.Default.CalendarToday, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Book a Session", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                }
            }

            // Summary Stats Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Card 1
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF10B981)))
                            Text("Active", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("3", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("Enrolled...", fontSize = 11.sp, color = textGray)
                    }
                }

                // Card 2
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(primaryBlue))
                            Text("Paired", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("4", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("Active Tutors", fontSize = 11.sp, color = textGray)
                    }
                }

                // Card 3
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFF59E0B)))
                            Text("Next: 4PM", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFF59E0B))
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("2", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("Upcoming", fontSize = 11.sp, color = textGray)
                    }
                }
            }

            // Scheduled Sessions Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(primaryBlue))
                    Text("Scheduled Sessions", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                }
                Text(
                    text = "View Calendar",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = primaryBlue,
                    modifier = Modifier.clickable {}
                )
            }

            // Scheduled Session Card 1 (Online)
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
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, null, tint = textGray)
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("INF 212", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                                    Text("• Web Development", fontSize = 12.sp, color = textGray)
                                }
                                Text("John Banda", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color(0xFFE6F4EA)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF137333)))
                                Text("Online", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF137333))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = containerBg,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.VideoCall, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                                Text("Google Meet Room", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text("(Link active)", fontSize = 12.sp, color = textGray)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Icon(Icons.Default.Schedule, null, tint = textGray, modifier = Modifier.size(14.dp))
                                    Text("Today, 16:00 - 17:30", fontSize = 12.sp, color = textGray)
                                }
                                Text("In 2h 19m", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Icon(Icons.Default.VideoCall, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Join Meeting", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFFDBEAFE)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = lightBlueBg),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Chat, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Message", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }
                    }
                }
            }

            // Scheduled Session Card 2 (In-Person)
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
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(44.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFE2E8F0)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Person, null, tint = textGray)
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text("STA 211", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                                    Text("• Probability & Stats", fontSize = 12.sp, color = textGray)
                                }
                                Text("Thokozani Mwale", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                            }
                        }
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFD97706)))
                                Text("In-Person", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = containerBg,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.LocationOn, null, tint = Color(0xFFD97706), modifier = Modifier.size(16.dp))
                                Text("Campus Library, Room 4B", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Schedule, null, tint = textGray, modifier = Modifier.size(14.dp))
                                Text("Tomorrow, 14:00 - 15:30", fontSize = 12.sp, color = textGray)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Map simulation banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.9f))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Navigation, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                            Text("2nd Floor North Wing Study Pods", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFFDBEAFE)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = lightBlueBg),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Icon(Icons.Default.Map, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("View Location", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }

                        OutlinedButton(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                            colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                            modifier = Modifier
                                .weight(1f)
                                .height(40.dp)
                        ) {
                            Icon(Icons.Default.CalendarToday, null, tint = textGray, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Reschedule", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }
                    }
                }
            }

            // Course Progress Section Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFF10B981)))
                    Text("Course Progress", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                }
                Text("3 active enrollments", fontSize = 12.sp, color = textGray)
            }

            // Course Progress Card 1 (INF 212)
            CourseProgressCard(
                code = "INF 212",
                dept = "CS Dept",
                title = "Introduction to Web Development",
                progress = 0.68f,
                progressText = "68%",
                topics = "14/20 topics",
                tutor = "John Banda",
                nextTopic = "React State",
                onViewCourse = { onViewCourseClick("INF 212") },
                primaryBlue = primaryBlue,
                containerBg = containerBg,
                textDark = textDark,
                textGray = textGray
            )

            // Course Progress Card 2 (COM 311)
            CourseProgressCard(
                code = "COM 311",
                dept = "CS Dept",
                title = "Database Systems & Design",
                progress = 0.45f,
                progressText = "45%",
                topics = "9/20 topics",
                tutor = "Chimwemwe Phiri",
                nextTopic = "SQL Joins & Indexes",
                onViewCourse = { onViewCourseClick("COM 311") },
                primaryBlue = primaryBlue,
                containerBg = containerBg,
                textDark = textDark,
                textGray = textGray
            )

            // Course Progress Card 3 (STA 211)
            CourseProgressCard(
                code = "STA 211",
                dept = "Math & Stats",
                title = "Probability & Statistics",
                progress = 0.82f,
                progressText = "82%",
                topics = "18/22 topics",
                tutor = "Thokozani Mwale",
                nextTopic = "Hypothesis Testing",
                onViewCourse = { onViewCourseClick("STA 211") },
                primaryBlue = primaryBlue,
                containerBg = containerBg,
                textDark = textDark,
                textGray = textGray
            )

            // Study Pods Active Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = lightBlueBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                            .shadow(1.dp, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Groups, null, tint = primaryBlue, modifier = Modifier.size(24.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Study Pods Active", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("12 classmates collaborating right now", fontSize = 12.sp, color = textGray)
                    }
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text("Join In", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CourseProgressCard(
    code: String,
    dept: String,
    title: String,
    progress: Float,
    progressText: String,
    topics: String,
    tutor: String,
    nextTopic: String,
    onViewCourse: () -> Unit,
    primaryBlue: Color,
    containerBg: Color,
    textDark: Color,
    textGray: Color
) {
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
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = Color(0xFFE0ECFF)
                    ) {
                        Text(
                            text = code,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                    Text(text = dept, fontSize = 12.sp, color = textGray)
                }
                Text(text = progressText, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = topics, fontSize = 11.sp, color = textGray)

            Spacer(modifier = Modifier.height(10.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = primaryBlue,
                trackColor = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = containerBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.School, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                        Column {
                            Text("Assigned Tutor", fontSize = 10.sp, color = textGray)
                            Text(tutor, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Next:", fontSize = 11.sp, color = textGray)
                        Text(nextTopic, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onViewCourse,
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0xFFDBEAFE)),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color(0xFFF0F4FF)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Text("View Course", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 950)
@Composable
fun StudentDashboardPreview() {
    Yaza_WangaTheme {
        StudentDashboardScreen()
    }
}
