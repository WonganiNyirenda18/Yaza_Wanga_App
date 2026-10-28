package com.example.yaza_wanga.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ShowChart
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.example.yaza_wanga.ui.viewmodel.TutorSessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorSessionScreen(
    viewModel: TutorSessionViewModel = viewModel(),
    onNavigateRequests: () -> Unit = {},
    onNavigateAnalytics: () -> Unit = {},
    onNavigateUpload: () -> Unit = {},
    onNavigateVerify: () -> Unit = {},
    onAcceptRequest: (String) -> Unit = {},
    onDeclineRequest: (String) -> Unit = {},
    onAttachLink: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Pending, 1: Upcoming, 2: Completed

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    val pendingList = uiState.pendingSessions
    val upcomingList = uiState.upcomingSessions
    val completedList = uiState.completedSessions
    val studentMap = uiState.studentMap

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Session Requests",
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
                    Box(modifier = Modifier.padding(end = 4.dp)) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Alerts",
                                tint = textDark
                            )
                        }
                        if (pendingList.isNotEmpty()) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(Color.Red)
                                    .align(Alignment.TopEnd)
                                    .offset(x = (-8).dp, y = 8.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = {},
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
                    onClick = onNavigateRequests,
                    icon = { Icon(Icons.Default.EventAvailable, contentDescription = "Requests") },
                    label = { Text("Requests") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryBlue, selectedTextColor = primaryBlue)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateAnalytics,
                    icon = { Icon(Icons.AutoMirrored.Filled.ShowChart, contentDescription = "Analytics") },
                    label = { Text("Analytics") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateUpload,
                    icon = { Icon(Icons.Default.FileUpload, contentDescription = "Upload") },
                    label = { Text("Upload") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateVerify,
                    icon = { Icon(Icons.Default.VerifiedUser, contentDescription = "Verify") },
                    label = { Text("Verify") },
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
            // Priority Alert Banner Card if pending requests exist
            if (pendingList.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE0ECFF),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(primaryBlue),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "${pendingList.size} requests pending response",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = textDark
                            )
                            Text(
                                text = "Review session bookings from students",
                                fontSize = 12.sp,
                                color = textGray
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color(0xFFDCE6FD)
                        ) {
                            Text(
                                text = "Priority",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            // Tab Selector Pill Row
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFE2E8F0),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pending Tab
                    val isPending = selectedTab == 0
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (isPending) Color.White else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { selectedTab = 0 },
                        shadowElevation = if (isPending) 1.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Pending",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isPending) primaryBlue else textGray
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(if (isPending) primaryBlue else Color(0xFFCBD5E1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${pendingList.size}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Upcoming Tab
                    val isUpcoming = selectedTab == 1
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (isUpcoming) Color.White else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { selectedTab = 1 },
                        shadowElevation = if (isUpcoming) 1.dp else 0.dp
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Upcoming",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isUpcoming) primaryBlue else textGray
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(if (isUpcoming) primaryBlue else Color(0xFFCBD5E1)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${upcomingList.size}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }

                    // Completed Tab
                    val isCompleted = selectedTab == 2
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (isCompleted) Color.White else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { selectedTab = 2 },
                        shadowElevation = if (isCompleted) 1.dp else 0.dp
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Completed",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCompleted) primaryBlue else textGray
                            )
                        }
                    }
                }
            }

            // Display list based on selectedTab
            when (selectedTab) {
                0 -> {
                    // Pending tab
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pending Requests",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                        Text(
                            text = "${pendingList.size} Pending",
                            fontSize = 12.sp,
                            color = textGray
                        )
                    }

                    if (pendingList.isEmpty()) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No session requests yet", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
                            }
                        }
                    } else {
                        pendingList.forEach { session ->
                            val student = studentMap[session.studentId]
                            val studentName = student?.name?.ifBlank { student.email.substringBefore("@") } ?: "Student"
                            val studentDetail = "${session.courseCode} • ${session.method}"

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
                                        verticalAlignment = Alignment.Top
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(46.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFE2E8F0)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.Person, null, tint = textGray)
                                            }

                                            Column {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Text(studentName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                                                    Icon(Icons.Default.Verified, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                                                }
                                                Text(studentDetail, fontSize = 12.sp, color = textGray)
                                            }
                                        }

                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("PENDING", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = lightBlueBg,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(12.dp)) {
                                            Text(
                                                text = session.title.ifBlank { session.courseCode },
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = primaryBlue,
                                                letterSpacing = 0.5.sp
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "\"${session.description.ifBlank { "Tutoring session request for ${session.courseCode}" }}\"",
                                                fontSize = 13.sp,
                                                fontStyle = FontStyle.Italic,
                                                color = textDark
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                        Surface(shape = RoundedCornerShape(8.dp), color = Color(0xFFE0ECFF)) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(Icons.Default.Videocam, null, tint = primaryBlue, modifier = Modifier.size(12.dp))
                                                Text(session.method, fontSize = 11.sp, color = primaryBlue, fontWeight = FontWeight.SemiBold)
                                            }
                                        }

                                        Surface(shape = RoundedCornerShape(8.dp), color = containerBg) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Icon(Icons.Default.Schedule, null, tint = textGray, modifier = Modifier.size(12.dp))
                                                Text("${session.date} • ${session.time}", fontSize = 11.sp, color = textDark)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))

                                    Button(
                                        onClick = {
                                            viewModel.acceptRequest(session.id) {
                                                Toast.makeText(context, "Session accepted", Toast.LENGTH_SHORT).show()
                                                onAcceptRequest(session.id)
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(44.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Accept Request", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Button(
                                            onClick = {
                                                viewModel.declineRequest(session.id) {
                                                    Toast.makeText(context, "Session declined", Toast.LENGTH_SHORT).show()
                                                    onDeclineRequest(session.id)
                                                }
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(38.dp)
                                        ) {
                                            Icon(Icons.Default.Close, null, tint = Color(0xFFDC2626), modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text("Decline", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Upcoming tab
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Confirmed Upcoming",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                        Text(
                            text = "${upcomingList.size} Scheduled",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue
                        )
                    }

                    if (upcomingList.isEmpty()) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No upcoming sessions", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
                            }
                        }
                    } else {
                        upcomingList.forEach { session ->
                            val student = studentMap[session.studentId]
                            val studentName = student?.name?.ifBlank { student.email.substringBefore("@") } ?: "Student / Cohort"

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
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(46.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFFE2E8F0)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(Icons.Default.Person, null, tint = textGray)
                                            }

                                            Column {
                                                Text(studentName, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                                                Text("${session.courseCode} • ${session.title}", fontSize = 12.sp, color = textGray)
                                            }
                                        }

                                        Surface(
                                            shape = RoundedCornerShape(50.dp),
                                            color = lightBlueBg
                                        ) {
                                            Text(
                                                text = "${session.date}, ${session.time}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = primaryBlue,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    if (session.meetingLink.isBlank()) {
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFFFEF2F2),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(12.dp),
                                                verticalAlignment = Alignment.Top,
                                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                                            ) {
                                                Icon(Icons.Default.Warning, null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                                                Column {
                                                    Text(
                                                        text = "No meeting link added yet",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = Color(0xFFDC2626)
                                                    )
                                                    Spacer(modifier = Modifier.height(2.dp))
                                                    Text(
                                                        text = "Add meeting link before session starts.",
                                                        fontSize = 11.sp,
                                                        color = Color(0xFF991B1B)
                                                    )
                                                }
                                            }
                                        }
                                        Spacer(modifier = Modifier.height(12.dp))
                                    }

                                    Button(
                                        onClick = {
                                            viewModel.attachLink(session.id, "https://meet.google.com/default-room") {
                                                Toast.makeText(context, "Meeting link attached", Toast.LENGTH_SHORT).show()
                                                onAttachLink(session.id)
                                            }
                                        },
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(44.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Icon(Icons.Default.Link, null, tint = Color.White, modifier = Modifier.size(16.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text(if (session.meetingLink.isBlank()) "Attach Meeting Link" else "Update Meeting Link", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Completed tab
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Completed Sessions",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                        Text(
                            text = "${completedList.size} Completed",
                            fontSize = 12.sp,
                            color = textGray
                        )
                    }

                    if (completedList.isEmpty()) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(40.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("No completed sessions yet", fontSize = 14.sp, color = textGray, fontWeight = FontWeight.Medium)
                            }
                        }
                    } else {
                        completedList.forEach { session ->
                            Card(
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(session.courseCode, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                                    Text(session.title, fontSize = 13.sp, color = textGray)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Completed on ${session.date}", fontSize = 11.sp, color = Color(0xFF16A34A), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1000)
@Composable
fun TutorSessionPreview() {
    Yaza_WangaTheme {
        TutorSessionScreen()
    }
}
