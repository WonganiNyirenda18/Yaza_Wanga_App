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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileScreen(
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSaveAndContinue: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("Wongani Nyirenda") }
    var studentId by remember { mutableStateOf("BCS-2022-0481") }
    var programme by remember { mutableStateOf("BSc Information Technology") }
    var department by remember { mutableStateOf("Computer Science & IT") }
    var yearOfStudy by remember { mutableStateOf("Year 3") }
    var phone by remember { mutableStateOf("+265 991 234 567") }

    data class CourseItem(val code: String, val name: String, var selected: Boolean)
    val courses = remember {
        mutableStateListOf(
            CourseItem("INF 212", "Web Dev", true),
            CourseItem("COM 311", "Database Systems", true),
            CourseItem("COM 312", "Software Eng", false),
            CourseItem("STA 211", "Probability & Stats", true),
            CourseItem("MAT 201", "Linear Algebra", false),
            CourseItem("INF 111", "Computer Architecture", false)
        )
    }

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
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
                        Text(
                            text = "Academic Profile Setup",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                    }
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
                    IconButton(
                        onClick = onProfileClick,
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(primaryBlue)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Step Progress Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "STEP 1 OF 2 • PROFILE SETUP",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue
                )
                Text(
                    text = "50% Completed",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = textGray
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { 0.5f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp)),
                color = primaryBlue,
                trackColor = Color(0xFFE2E8F0)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Heading
            Text(
                text = "Complete Your Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Tell us about your studies so we can recommend the best tutors, peer study pods, and semester resources.",
                fontSize = 13.sp,
                color = textGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Student Photo Avatar
            Box(
                modifier = Modifier.size(90.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2E8F0))
                        .border(2.dp, primaryBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = textGray,
                        modifier = Modifier.size(50.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(primaryBlue)
                        .border(2.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CameraAlt,
                        contentDescription = "Upload Photo",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Upload Student Photo",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue
                )
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = null,
                    tint = primaryBlue,
                    modifier = Modifier.size(14.dp)
                )
            }
            Text(
                text = "JPG, PNG or HEIC (Max 5MB)",
                fontSize = 11.sp,
                color = textGray
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Academic Credentials Card
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
                        .padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Academic Credentials",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Full Name
                    Text("Full Name", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        leadingIcon = { Icon(Icons.Default.Badge, null, tint = textGray, modifier = Modifier.size(20.dp)) },
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Student Registration ID
                    Text("Student Registration ID", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = studentId,
                        onValueChange = { studentId = it },
                        leadingIcon = { Icon(Icons.Default.Fingerprint, null, tint = textGray, modifier = Modifier.size(20.dp)) },
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Programme of Study
                    Text("Programme of Study", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = programme,
                        onValueChange = { programme = it },
                        leadingIcon = { Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = textGray, modifier = Modifier.size(20.dp)) },
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Department / Faculty
                    Text("Department / Faculty", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = department,
                        onValueChange = { department = it },
                        leadingIcon = { Icon(Icons.Default.Business, null, tint = textGray, modifier = Modifier.size(20.dp)) },
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

                    Spacer(modifier = Modifier.height(14.dp))

                    // Year of Study
                    Text("Year of Study", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                    Spacer(modifier = Modifier.height(8.dp))
                    val years = listOf("Year 1", "Year 2", "Year 3", "Year 4", "Postgraduate")
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            years.take(3).forEach { y ->
                                val selected = yearOfStudy == y
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) primaryBlue else containerBg,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .clickable { yearOfStudy = y },
                                    border = BorderStroke(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0))
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            if (selected) {
                                                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                            }
                                            Text(
                                                text = y,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (selected) Color.White else textDark
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            years.drop(3).forEach { y ->
                                val selected = yearOfStudy == y
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) primaryBlue else containerBg,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .clickable { yearOfStudy = y },
                                    border = BorderStroke(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0))
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            if (selected) {
                                                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                            }
                                            Text(
                                                text = y,
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (selected) Color.White else textDark
                                            )
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Phone Number / WhatsApp
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Phone Number / WhatsApp", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        Text("Tutor updates", fontSize = 11.sp, color = textGray)
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        leadingIcon = { Icon(Icons.Default.Phone, null, tint = textGray, modifier = Modifier.size(20.dp)) },
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

            Spacer(modifier = Modifier.height(20.dp))

            // Enrolled Courses Card
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
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = primaryBlue,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "Enrolled Courses",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textDark
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = lightBlueBg
                        ) {
                            Text(
                                text = "${courses.count { it.selected }} Active",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Select courses where you need peer tutoring or exam revision pods this term.",
                        fontSize = 12.sp,
                        color = textGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Flow of Course Chips
                    val rows = courses.chunked(2)
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        rows.forEach { rowItems ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                rowItems.forEach { course ->
                                    val index = courses.indexOf(course)
                                    Surface(
                                        shape = RoundedCornerShape(50.dp),
                                        color = if (course.selected) primaryBlue else containerBg,
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(42.dp)
                                            .clickable {
                                                courses[index] = course.copy(selected = !course.selected)
                                            },
                                        border = BorderStroke(1.dp, if (course.selected) primaryBlue else Color(0xFFE2E8F0))
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 10.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            Icon(
                                                imageVector = if (course.selected) Icons.Default.Check else Icons.Default.Add,
                                                contentDescription = null,
                                                tint = if (course.selected) Color.White else textGray,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = "${course.code} - ${course.name}",
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = if (course.selected) Color.White else textDark,
                                                maxLines = 1
                                            )
                                        }
                                    }
                                }
                                if (rowItems.size == 1) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Add another course button
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = lightBlueBg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .clickable { },
                        border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(Icons.Default.AddCircle, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Add another course code",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Info, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                        Text(
                            text = "You can add or update your enrolled courses anytime from your student settings.",
                            fontSize = 11.sp,
                            color = textGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Save & Continue Button
            Button(
                onClick = onSaveAndContinue,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "SAVE & CONTINUE TO DASHBOARD",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 950)
@Composable
fun StudentProfilePreview() {
    Yaza_WangaTheme {
        StudentProfileScreen()
    }
}
