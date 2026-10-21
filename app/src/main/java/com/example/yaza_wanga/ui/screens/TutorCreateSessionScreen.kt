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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorCreateSessionScreen(
    onBackClick: () -> Unit = {},
    onNavigateDashboard: () -> Unit = {},
    onNavigateSessions: () -> Unit = {},
    onNavigateStudents: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onCreateSessionSuccess: () -> Unit = {}
) {
    var selectedCourse by remember { mutableStateOf("INF 212: Intro to Web Dev") }
    var courseDropdownExpanded by remember { mutableStateOf(false) }
    var sessionFormat by remember { mutableStateOf("Online") } // "Online" or "In-Person"
    var meetingLink by remember { mutableStateOf("https://meet.google.com/xyz-qwer-at") }
    var venueLocation by remember { mutableStateOf("") }
    var sessionDate by remember { mutableStateOf("10/24/2024") }
    var sessionTime by remember { mutableStateOf("04:00 PM") }

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Create Tutoring Session",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = textDark
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
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
                    Triple(3, "Profile", Icons.Default.Person)
                )

                tabs.forEach { (index, label, icon) ->
                    val selected = index == 1 // Sessions selected
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            when (index) {
                                0 -> onNavigateDashboard()
                                1 -> onNavigateSessions()
                                2 -> onNavigateStudents()
                                3 -> onNavigateProfile()
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
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Workspace Badge & Header
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = lightBlueBg
                ) {
                    Text(
                        text = "• TUTOR WORKSPACE • NEW SESSION",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBlue,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        letterSpacing = 0.5.sp
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Schedule Tutoring Session",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textDark
                )
                Text(
                    text = "Set up a focused learning session for your enrolled students or cohort.",
                    fontSize = 13.sp,
                    color = textGray
                )
            }

            // Section 1: Select Course *
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("1. Select Course", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("*", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                    }
                    Text("3 Courses Active", fontSize = 12.sp, color = textGray)
                }

                // Active Course Selector Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, primaryBlue, RoundedCornerShape(16.dp))
                        .clickable { courseDropdownExpanded = !courseDropdownExpanded }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(lightBlueBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = primaryBlue, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text("ACTIVE COURSE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                                Text(selectedCourse, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Surface(shape = RoundedCornerShape(50.dp), color = lightBlueBg) {
                                Text("18 enrolled", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                            }
                            Icon(if (courseDropdownExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = primaryBlue)
                        }
                    }
                }

                // Expanded Course Options list when clicked
                if (courseDropdownExpanded) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            CourseOptionItem(
                                code = "INF 212: Intro to Web Dev",
                                details = "18 Students Enrolled • BSc Computing",
                                active = true,
                                selected = selectedCourse.contains("INF 212"),
                                onClick = { selectedCourse = "INF 212: Intro to Web Dev"; courseDropdownExpanded = false }
                            )
                            CourseOptionItem(
                                code = "STA 211: Probability & Distribution",
                                details = "12 Students Enrolled • Year 2 Stats",
                                active = false,
                                selected = selectedCourse.contains("STA 211"),
                                onClick = { selectedCourse = "STA 211: Probability & Distribution"; courseDropdownExpanded = false }
                            )
                            CourseOptionItem(
                                code = "COM 311: Database Mgmt System...",
                                details = "14 Students Enrolled • Year 3 CS",
                                active = false,
                                selected = selectedCourse.contains("COM 311"),
                                onClick = { selectedCourse = "COM 311: Database Mgmt System"; courseDropdownExpanded = false }
                            )
                        }
                    }
                }
            }

            // Section 2: Session Format *
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("2. Session Format", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("*", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Online Format Card
                    val isOnline = sessionFormat == "Online"
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isOnline) lightBlueBg else Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { sessionFormat = "Online" }
                            .border(1.dp, if (isOnline) primaryBlue else Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Videocam, null, tint = primaryBlue, modifier = Modifier.size(24.dp))
                                Text("Online", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text("Meet or Zoom room", fontSize = 11.sp, color = textGray)
                            }
                            RadioButton(selected = isOnline, onClick = { sessionFormat = "Online" }, colors = RadioButtonDefaults.colors(selectedColor = primaryBlue))
                        }
                    }

                    // In-Person Format Card
                    val isInPerson = sessionFormat == "In-Person"
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = if (isInPerson) lightBlueBg else Color.White),
                        modifier = Modifier
                            .weight(1f)
                            .clickable { sessionFormat = "In-Person" }
                            .border(1.dp, if (isInPerson) primaryBlue else Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.LocationOn, null, tint = primaryBlue, modifier = Modifier.size(24.dp))
                                Text("In-Person", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text("Library or Pod", fontSize = 11.sp, color = textGray)
                            }
                            RadioButton(selected = isInPerson, onClick = { sessionFormat = "In-Person" }, colors = RadioButtonDefaults.colors(selectedColor = primaryBlue))
                        }
                    }
                }
            }

            // Conditional Format Inputs (Online vs In-Person)
            if (sessionFormat == "Online") {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Online Meeting Link", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                            Surface(shape = RoundedCornerShape(50.dp), color = Color(0xFFE6F4EA)) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Verified, null, tint = Color(0xFF137333), modifier = Modifier.size(12.dp))
                                    Text("Verified Link", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF137333))
                                }
                            }
                        }

                        // Link TextField with Copy Icon
                        OutlinedTextField(
                            value = meetingLink,
                            onValueChange = { meetingLink = it },
                            leadingIcon = { Icon(Icons.Default.Link, null, tint = primaryBlue, modifier = Modifier.size(18.dp)) },
                            trailingIcon = {
                                IconButton(onClick = {}) {
                                    Icon(Icons.Default.ContentCopy, null, tint = textGray, modifier = Modifier.size(18.dp))
                                }
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = containerBg,
                                unfocusedContainerColor = containerBg,
                                focusedBorderColor = primaryBlue,
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedTextColor = textDark,
                                unfocusedTextColor = textDark
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        // Action Buttons Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg),
                                modifier = Modifier.weight(1.2f).height(38.dp)
                            ) {
                                Icon(Icons.Default.VideoCall, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Generate Meet", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            }

                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg),
                                modifier = Modifier.weight(0.9f).height(38.dp)
                            ) {
                                Icon(Icons.Default.Videocam, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Zoom", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            }

                            Button(
                                onClick = {},
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg),
                                modifier = Modifier.weight(0.9f).height(38.dp)
                            ) {
                                Icon(Icons.Default.Bookmark, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Saved", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            }
                        }

                        // Info Box
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = lightBlueBg,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Info, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Meeting link will be automatically attached to student Google / Outlook calendar invites and push notifications.",
                                    fontSize = 11.sp,
                                    color = textDark
                                )
                            }
                        }
                    }
                }
            } else {
                // In-Person Venue Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Meeting Location / Venue", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                        OutlinedTextField(
                            value = venueLocation,
                            onValueChange = { venueLocation = it },
                            placeholder = { Text("e.g., Campus Library Study Pod 3B, Room...", color = textGray, fontSize = 13.sp) },
                            leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = primaryBlue, modifier = Modifier.size(18.dp)) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = containerBg,
                                unfocusedContainerColor = containerBg,
                                focusedBorderColor = primaryBlue,
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedTextColor = textDark,
                                unfocusedTextColor = textDark
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Section 3: Date, Time & Duration *
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("3. Date, Time & Duration", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("*", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // Date Input
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Date", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textGray)
                            OutlinedTextField(
                                value = sessionDate,
                                onValueChange = { sessionDate = it },
                                leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = primaryBlue, modifier = Modifier.size(16.dp)) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = containerBg,
                                    unfocusedContainerColor = containerBg,
                                    focusedBorderColor = primaryBlue,
                                    unfocusedBorderColor = Color(0xFFE2E8F0),
                                    focusedTextColor = textDark,
                                    unfocusedTextColor = textDark
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Time Input
                        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Time", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textGray)
                            OutlinedTextField(
                                value = sessionTime,
                                onValueChange = { sessionTime = it },
                                leadingIcon = { Icon(Icons.Default.Schedule, null, tint = primaryBlue, modifier = Modifier.size(16.dp)) },
                                singleLine = true,
                                shape = RoundedCornerShape(10.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = containerBg,
                                    unfocusedContainerColor = containerBg,
                                    focusedBorderColor = primaryBlue,
                                    unfocusedBorderColor = Color(0xFFE2E8F0),
                                    focusedTextColor = textDark,
                                    unfocusedTextColor = textDark
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Create Session Button
            Button(
                onClick = onCreateSessionSuccess,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.AddCircle, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Create Session", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            // Save as Draft
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(onClick = {}) {
                    Icon(Icons.Default.MailOutline, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save as Draft", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CourseOptionItem(
    code: String,
    details: String,
    active: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val primaryBlue = Color(0xFF1D4ED8)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (selected) Color(0xFFF0F4FF) else Color.Transparent,
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = primaryBlue, modifier = Modifier.size(20.dp))
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(code, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        if (active) {
                            Surface(shape = RoundedCornerShape(4.dp), color = primaryBlue) {
                                Text("Active", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color.White, modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp))
                            }
                        }
                    }
                    Text(details, fontSize = 11.sp, color = textGray)
                }
            }

            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = primaryBlue)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1200)
@Composable
fun TutorCreateSessionPreview() {
    Yaza_WangaTheme {
        TutorCreateSessionScreen()
    }
}
