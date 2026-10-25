package com.example.yaza_wanga.ui.screens

import android.widget.Toast
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
import com.example.yaza_wanga.ui.viewmodel.AdminCreateCourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminCreateCourseScreen(
    viewModel: AdminCreateCourseViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onNavigateTab: (Int) -> Unit = {},
    onPublishSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    var courseName by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }
    var courseCredits by remember { mutableStateOf("3") }
    var selectedFaculty by remember { mutableStateOf("Computer Science & IT") }
    var facultyDropdownExpanded by remember { mutableStateOf(false) }
    var courseDescription by remember { mutableStateOf("") }
    var scheduledSemester by remember { mutableStateOf("Semester 1") }

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    val faculties = listOf(
        Triple("Computer Science & IT", "Faculty of Information Sciences", Icons.Default.Computer),
        Triple("Mathematics & Statistics", "Faculty of Physical Sciences", Icons.Default.Calculate),
        Triple("Electrical & Electronics Engineering", "School of Engineering", Icons.Default.Bolt),
        Triple("Business Information Systems", "Faculty of Commerce", Icons.Default.Business),
        Triple("Physics & Applied Sciences", "Faculty of Physical Sciences", Icons.Default.Science)
    )

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Create Course",
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
        bottomBar = {
            AdminBottomBar(currentTab = 2, onSelectTab = onNavigateTab)
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
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Create New Course",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textDark
                )
                Text(
                    text = "Add an accredited academic module to the campus directory for tutor pairing and student enrolment.",
                    fontSize = 13.sp,
                    color = textGray
                )
            }

            // Course Name Section *
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Course Name", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("*", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                    }
                    Text("Required", fontSize = 12.sp, color = textGray)
                }

                OutlinedTextField(
                    value = courseName,
                    onValueChange = { courseName = it },
                    placeholder = { Text("e.g., Introduction to Web Development") },
                    leadingIcon = { Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = primaryBlue, modifier = Modifier.size(18.dp)) },
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

            // Course Code Section *
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Course Code & Credits", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("*", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = courseCode,
                        onValueChange = { courseCode = it },
                        placeholder = { Text("e.g., INF 212") },
                        leadingIcon = { Icon(Icons.Default.Numbers, null, tint = primaryBlue, modifier = Modifier.size(18.dp)) },
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
                        modifier = Modifier.weight(2.4f)
                    )

                    OutlinedTextField(
                        value = courseCredits,
                        onValueChange = { courseCredits = it },
                        placeholder = { Text("3") },
                        trailingIcon = { Text("CR", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textGray, modifier = Modifier.padding(end = 12.dp)) },
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
                        modifier = Modifier.weight(1.2f)
                    )
                }
            }

            // Academic Department / Faculty *
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Academic Department / Faculty", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("*", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, primaryBlue, RoundedCornerShape(16.dp))
                        .clickable { facultyDropdownExpanded = !facultyDropdownExpanded }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(lightBlueBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Business, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                            }
                            Text(selectedFaculty, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }

                        Icon(if (facultyDropdownExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = primaryBlue)
                    }
                }

                if (facultyDropdownExpanded) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            faculties.forEach { (title, subtitle, icon) ->
                                val selected = selectedFaculty == title
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (selected) lightBlueBg else Color.Transparent,
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        selectedFaculty = title
                                        facultyDropdownExpanded = false
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                            Icon(icon, null, tint = primaryBlue, modifier = Modifier.size(20.dp))
                                            Column {
                                                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                                                Text(subtitle, fontSize = 11.sp, color = textGray)
                                            }
                                        }
                                        if (selected) {
                                            Icon(Icons.Default.CheckCircle, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Course Description & Scope * (Max 500 validation)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Course Description & Scope", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("*", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                    }
                    Text("${courseDescription.length} / 500", fontSize = 11.sp, color = if (courseDescription.length > 500) Color(0xFFDC2626) else textGray)
                }

                OutlinedTextField(
                    value = courseDescription,
                    onValueChange = { if (it.length <= 500) courseDescription = it },
                    placeholder = { Text("Enter detailed syllabus overview (max 500 characters)...") },
                    minLines = 4,
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

            // Scheduled Semester
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Scheduled Semester", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    listOf("Semester 1", "Semester 2").forEach { sem ->
                        val selected = scheduledSemester == sem
                        Card(
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = if (selected) lightBlueBg else Color.White),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { scheduledSemester = sem }
                                .border(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.DateRange, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                                Text(
                                    text = sem,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) primaryBlue else textDark
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Create Course & Publish Button
            Button(
                onClick = {
                    val creditsInt = courseCredits.toIntOrNull() ?: 3
                    viewModel.createCourse(
                        code = courseCode,
                        title = courseName,
                        department = selectedFaculty,
                        credits = creditsInt,
                        description = courseDescription,
                        semester = scheduledSemester,
                        isDraft = false,
                        onSuccess = {
                            Toast.makeText(context, "Course published successfully", Toast.LENGTH_SHORT).show()
                            onPublishSuccess()
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
                    Text("Create Course & Publish", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            // Save as Draft (keeps hidden from students)
            Button(
                onClick = {
                    val creditsInt = courseCredits.toIntOrNull() ?: 3
                    viewModel.createCourse(
                        code = courseCode,
                        title = courseName,
                        department = selectedFaculty,
                        credits = creditsInt,
                        description = courseDescription,
                        semester = scheduledSemester,
                        isDraft = true,
                        onSuccess = {
                            Toast.makeText(context, "Course saved as draft (hidden from students)", Toast.LENGTH_SHORT).show()
                            onPublishSuccess()
                        },
                        onError = { err ->
                            Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE2E8F0)),
                modifier = Modifier.fillMaxWidth().height(48.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.BookmarkBorder, null, tint = textDark, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Save as Draft", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1200)
@Composable
fun AdminCreateCoursePreview() {
    Yaza_WangaTheme {
        AdminCreateCourseScreen()
    }
}
