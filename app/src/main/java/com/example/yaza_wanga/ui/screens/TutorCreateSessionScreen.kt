package com.example.yaza_wanga.ui.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Label
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.*
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
    val tutorCourses by viewModel.tutorCourses.collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedCourse by remember(tutorCourses) { mutableStateOf(tutorCourses.firstOrNull() ?: "") }
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
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
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
                            text = "Schedule Tutoring Session",
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
                        IconButton(
                            onClick = onNavigateMenu,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(primaryBlue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
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
            if (tutorCourses.isEmpty()) {
                // Empty state when tutor has no courses configured
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().padding(top = 32.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(32.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.School, null, tint = primaryBlue, modifier = Modifier.size(48.dp))
                        Text(
                            text = "No assigned courses found in your tutor profile.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Please update your profile to add courses before scheduling tutoring sessions.",
                            fontSize = 12.sp,
                            color = textGray,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = onNavigateProfile,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                        ) {
                            Text("Go to Tutor Profile Setup", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            } else {
                // Select Course Section
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Select Course *", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("${tutorCourses.size} Assigned", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        tutorCourses.forEach { course ->
                            val selected = selectedCourse == course
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = if (selected) primaryBlue else Color.White,
                                modifier = Modifier.clickable { selectedCourse = course },
                                border = BorderStroke(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                        contentDescription = null,
                                        tint = if (selected) Color.White else primaryBlue,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = course,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (selected) Color.White else textDark
                                    )
                                    if (selected) {
                                        Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                    }
                                }
                            }
                        }
                    }
                }

                // Session Details Card
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Event, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                            Text("Session Details", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }

                        // Session Title
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Session Title *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                            OutlinedTextField(
                                value = sessionTitle,
                                onValueChange = { sessionTitle = it },
                                placeholder = { Text("e.g. Midterm Revision & Past Papers", color = textGray, fontSize = 13.sp) },
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

                        // Session Format (Online vs In-Person)
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Session Format *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                val formats = listOf("Online", "In-Person")
                                formats.forEach { fmt ->
                                    val selected = sessionFormat == fmt
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (selected) lightBlueBg else containerBg,
                                        modifier = Modifier
                                            .weight(1f)
                                            .clickable { sessionFormat = fmt }
                                            .border(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(12.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            RadioButton(
                                                selected = selected,
                                                onClick = { sessionFormat = fmt },
                                                colors = RadioButtonDefaults.colors(selectedColor = primaryBlue)
                                            )
                                            Text(fmt, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                                        }
                                    }
                                }
                            }
                        }

                        // Link or Venue
                        if (sessionFormat == "Online") {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Google Meet / Zoom Link *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                                OutlinedTextField(
                                    value = meetingLink,
                                    onValueChange = { meetingLink = it },
                                    placeholder = { Text("https://meet.google.com/...", color = textGray, fontSize = 13.sp) },
                                    leadingIcon = { Icon(Icons.Default.Link, null, tint = primaryBlue, modifier = Modifier.size(16.dp)) },
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
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Venue Location *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                                OutlinedTextField(
                                    value = venueLocation,
                                    onValueChange = { venueLocation = it },
                                    placeholder = { Text("e.g. Computer Lab 2, Chancellor College", color = textGray, fontSize = 13.sp) },
                                    leadingIcon = { Icon(Icons.Default.LocationOn, null, tint = primaryBlue, modifier = Modifier.size(16.dp)) },
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

                        // Date & Time Row
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Date *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                                OutlinedTextField(
                                    value = sessionDate,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { Text("MM/DD/YYYY", color = textGray, fontSize = 13.sp) },
                                    trailingIcon = {
                                        IconButton(onClick = { showDatePicker = true }) {
                                            Icon(Icons.Default.CalendarToday, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
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
                                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true }
                                )
                            }

                            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text("Time *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                                OutlinedTextField(
                                    value = sessionTime,
                                    onValueChange = { sessionTime = it },
                                    placeholder = { Text("04:00 PM", color = textGray, fontSize = 13.sp) },
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
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Create Session Button
                Button(
                    onClick = {
                        if (selectedCourse.isBlank()) {
                            Toast.makeText(context, "Please select a course", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (sessionTitle.isBlank()) {
                            Toast.makeText(context, "Please enter a session title", Toast.LENGTH_SHORT).show()
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
                                Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
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
            }

            Spacer(modifier = Modifier.height(16.dp))
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
