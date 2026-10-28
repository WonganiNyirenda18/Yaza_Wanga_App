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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.example.yaza_wanga.ui.viewmodel.TutorCreateSessionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorCreateSessionScreen(
    viewModel: TutorCreateSessionViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onNavigateDashboard: () -> Unit = {},
    onNavigateSessions: () -> Unit = {},
    onNavigateStudents: () -> Unit = {},
    onNavigateMenu: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onCreateSessionSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val tutorCourses by viewModel.tutorCourses.collectAsState()

    var selectedCourse by remember(tutorCourses) { mutableStateOf("") }
    var sessionTitle by remember { mutableStateOf("") }
    var courseDropdownExpanded by remember { mutableStateOf(false) }
    var sessionFormat by remember { mutableStateOf("Online") } // "Online" or "In-Person"
    var meetingLink by remember { mutableStateOf("") }
    var venueLocation by remember { mutableStateOf("") }
    var sessionDate by remember { mutableStateOf("") }
    var sessionTime by remember { mutableStateOf("04:00 PM") }

    var selectedDateMillis by remember { mutableStateOf(System.currentTimeMillis() + 86400000) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        selectedDateMillis = millis
                        val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                        sessionDate = formatter.format(Date(millis))
                    }
                    showDatePicker = false
                }) {
                    Text("OK", fontWeight = FontWeight.Bold, color = primaryBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = textGray)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

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
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
                HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
            }
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        if (tutorCourses.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(60.dp)
                                .clip(CircleShape)
                                .background(lightBlueBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.School, null, tint = primaryBlue, modifier = Modifier.size(28.dp))
                        }
                        Text(
                            text = "No Courses Configured",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                        Text(
                            text = "You have not configured any courses in your tutor profile yet. Please add your courses before creating sessions.",
                            fontSize = 13.sp,
                            color = textGray,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            onClick = onNavigateProfile,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            modifier = Modifier.fillMaxWidth().height(46.dp)
                        ) {
                            Text("Go to Tutor Profile", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
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
                    }

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, if (selectedCourse.isBlank()) Color(0xFFE2E8F0) else primaryBlue, RoundedCornerShape(14.dp))
                            .clickable { courseDropdownExpanded = !courseDropdownExpanded }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(lightBlueBg),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                                }
                                Column {
                                    Text("SELECT COURSE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                                    Text(
                                        text = if (selectedCourse.isBlank()) "Choose from your courses..." else selectedCourse,
                                        fontSize = 13.sp,
                                        fontWeight = if (selectedCourse.isBlank()) FontWeight.Normal else FontWeight.Bold,
                                        color = if (selectedCourse.isBlank()) textGray else textDark
                                    )
                                }
                            }

                            Icon(if (courseDropdownExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = primaryBlue)
                        }
                    }

                    if (courseDropdownExpanded) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                            modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                        ) {
                            Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                tutorCourses.forEach { code ->
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (selectedCourse == code) lightBlueBg else Color.Transparent,
                                        modifier = Modifier.fillMaxWidth().clickable {
                                            selectedCourse = code
                                            courseDropdownExpanded = false
                                        }
                                    ) {
                                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                            Text(code, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Session Title
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Session Title", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                    OutlinedTextField(
                        value = sessionTitle,
                        onValueChange = { sessionTitle = it },
                        placeholder = { Text("e.g. Web Development Mentorship") },
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

                // Conditional Format Inputs
                if (sessionFormat == "Online") {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Online Meeting Link *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                            OutlinedTextField(
                                value = meetingLink,
                                onValueChange = { meetingLink = it },
                                placeholder = { Text("https://meet.google.com/...") },
                                leadingIcon = { Icon(Icons.Default.Link, null, tint = primaryBlue, modifier = Modifier.size(18.dp)) },
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
                } else {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("Meeting Location / Venue *", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                            OutlinedTextField(
                                value = venueLocation,
                                onValueChange = { venueLocation = it },
                                placeholder = { Text("e.g., Campus Library Study Pod 3B") },
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
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Date (Tap to pick)", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textGray)
                                Box(modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }) {
                                    OutlinedTextField(
                                        value = sessionDate,
                                        onValueChange = {},
                                        readOnly = true,
                                        enabled = false,
                                        placeholder = { Text("Select date") },
                                        leadingIcon = { Icon(Icons.Default.CalendarToday, null, tint = primaryBlue, modifier = Modifier.size(16.dp)) },
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp),
                                        colors = OutlinedTextFieldDefaults.colors(
                                            disabledContainerColor = containerBg,
                                            disabledTextColor = textDark,
                                            disabledBorderColor = Color(0xFFE2E8F0),
                                            disabledLeadingIconColor = primaryBlue,
                                            disabledPlaceholderColor = textGray
                                        ),
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }
                            }

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
                    onClick = {
                        if (selectedCourse.isBlank()) {
                            Toast.makeText(context, "Please select a course", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        viewModel.createSession(
                            courseCode = selectedCourse,
                            title = sessionTitle,
                            format = sessionFormat,
                            meetingLink = meetingLink,
                            venue = venueLocation,
                            dateMillis = selectedDateMillis,
                            onSuccess = {
                                Toast.makeText(context, "Tutoring session created successfully", Toast.LENGTH_SHORT).show()
                                onCreateSessionSuccess()
                            },
                            onError = { err ->
                                Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                            }
                        )
                    },
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

                Spacer(modifier = Modifier.height(16.dp))
            }
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
