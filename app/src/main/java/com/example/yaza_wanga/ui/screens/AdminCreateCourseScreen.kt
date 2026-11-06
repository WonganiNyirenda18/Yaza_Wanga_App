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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.data.model.Course
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.*
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
    val uiState by viewModel.uiState.collectAsState()

    var courseName by remember { mutableStateOf("") }
    var courseCode by remember { mutableStateOf("") }
    var courseDescription by remember { mutableStateOf("") }
    var selectedFaculty by remember { mutableStateOf("Faculty of Science") }
    var selectedSemester by remember { mutableStateOf("Semester 1") }
    var isDraft by remember { mutableStateOf(false) }

    var facultyDropdownExpanded by remember { mutableStateOf(false) }
    var semesterDropdownExpanded by remember { mutableStateOf(false) }

    val faculties = listOf("Faculty of Science", "Faculty of Education", "Faculty of Commerce", "School of Humanities")
    val semesters = listOf("Semester 1", "Semester 2", "Summer Session")

    val primaryBlue = Color(0xFF1D4ED8)
    val containerBg = Color(0xFFF8FAFC)
    val lightBlueBg = Color(0xFFF0F4FF)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Add Module",
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
                    placeholder = { Text("Introduction to Web", color = textGray, fontSize = 14.sp) },
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

            // Course Code Section * (3 CR field removed)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Course Code", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("*", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                    }
                }

                OutlinedTextField(
                    value = courseCode,
                    onValueChange = { courseCode = it },
                    placeholder = { Text("e.g., INF 212", color = textGray, fontSize = 14.sp) },
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
                    modifier = Modifier.fillMaxWidth()
                )
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
                            faculties.forEach { title ->
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
                                            Icon(Icons.Default.School, null, tint = primaryBlue, modifier = Modifier.size(20.dp))
                                            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
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

            // Course Description & Scope *
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
                    placeholder = { Text("Provide a concise summary of the module curriculum, prerequisites, and learning outcomes...", color = textGray, fontSize = 13.sp) },
                    minLines = 4,
                    maxLines = 6,
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

            // Target Semester Selection *
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Target Semester", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("*", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                }

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                        .clickable { semesterDropdownExpanded = !semesterDropdownExpanded }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Icon(Icons.Default.DateRange, null, tint = primaryBlue, modifier = Modifier.size(20.dp))
                            Text(selectedSemester, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }

                        Icon(if (semesterDropdownExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = textGray)
                    }
                }

                if (semesterDropdownExpanded) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            semesters.forEach { title ->
                                val selected = selectedSemester == title
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (selected) lightBlueBg else Color.Transparent,
                                    modifier = Modifier.fillMaxWidth().clickable {
                                        selectedSemester = title
                                        semesterDropdownExpanded = false
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
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

            // Save as Unpublished Draft Checkbox
            Row(
                modifier = Modifier.fillMaxWidth().clickable { isDraft = !isDraft },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Checkbox(
                    checked = isDraft,
                    onCheckedChange = { isDraft = it },
                    colors = CheckboxDefaults.colors(checkedColor = primaryBlue)
                )
                Column {
                    Text("Save as Unpublished Draft", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("Hide this module from public enrollment until syllabus is verified.", fontSize = 11.sp, color = textGray)
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Error Message Banner
            uiState.errorMessage?.let { err ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(12.dp))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Error, null, tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
                        Text(err, fontSize = 12.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.Medium)
                    }
                }
            }

            // Submit Button
            Button(
                onClick = {
                    if (courseName.isBlank() || courseCode.isBlank() || courseDescription.isBlank()) {
                        Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    viewModel.createCourse(
                        code = courseCode.trim().uppercase(),
                        title = courseName.trim(),
                        department = selectedFaculty,
                        credits = 3,
                        description = courseDescription.trim(),
                        semester = selectedSemester,
                        isDraft = isDraft,
                        onSuccess = {
                            Toast.makeText(context, "Course created successfully", Toast.LENGTH_SHORT).show()
                            onPublishSuccess()
                        },
                        onError = { err ->
                            Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Icon(Icons.Default.Publish, null, tint = Color.White, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(if (isDraft) "Save Draft Course" else "Publish Course to Directory", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1100)
@Composable
fun AdminCreateCoursePreview() {
    Yaza_WangaTheme {
        AdminCreateCourseScreen()
    }
}
