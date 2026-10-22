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
fun AdminCreateCourseScreen(
    onBackClick: () -> Unit = {},
    onNavigateTab: (Int) -> Unit = {},
    onPublishSuccess: () -> Unit = {}
) {
    var courseName by remember { mutableStateOf("Introduction to Web Development") }
    var courseCode by remember { mutableStateOf("INF 212") }
    var courseCredits by remember { mutableStateOf("3") }
    var selectedFaculty by remember { mutableStateOf("Computer Science & IT") }
    var facultyDropdownExpanded by remember { mutableStateOf(false) }
    var courseDescription by remember { mutableStateOf("Foundational principles of frontend architecture, semantic web development, responsive CSS layouts, and modern JavaScript programming paradigms for interactive web apps.") }
    var targetYear by remember { mutableStateOf("Year 2") }
    var scheduledSemester by remember { mutableStateOf("Semester 1") }
    var allowPeerTutoring by remember { mutableStateOf(true) }

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
            // Header Title & Subtitle (Governance & institution cards removed)
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
                Text("Full academic course title as reflected in the university syllabus.", fontSize = 11.sp, color = textGray)
            }

            // Course Code Section * (Prefix + Level badge removed)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Course Code", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("*", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Code Field
                    OutlinedTextField(
                        value = courseCode,
                        onValueChange = { courseCode = it },
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

                    // Credits Field
                    OutlinedTextField(
                        value = courseCredits,
                        onValueChange = { courseCredits = it },
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
                Text("Standard format includes departmental letters followed by numerical tier.", fontSize = 11.sp, color = textGray)
            }

            // Academic Department / Faculty *
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text("Academic Department / Faculty", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("*", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                }

                // Selector Card
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

                // Expanded Faculty List
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

            // Course Description & Scope * (Include tags removed)
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("Course Description & Scope", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("*", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                    }
                    Text("${courseDescription.length} / 500", fontSize = 11.sp, color = textGray)
                }

                OutlinedTextField(
                    value = courseDescription,
                    onValueChange = { if (it.length <= 500) courseDescription = it },
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

            // Target Academic Year (Postgrad removed, Year 4 added)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Target Academic Year", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf("Year 1", "Year 2", "Year 3", "Year 4").forEach { yr ->
                        val selected = targetYear == yr
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (selected) primaryBlue else Color.White,
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .clickable { targetYear = yr },
                            border = BorderStroke(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = yr,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else textDark
                                )
                            }
                        }
                    }
                }
            }

            // Scheduled Semester (Autumn & Spring removed, Semester 1 & Semester 2)
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

            // Allow Peer Tutoring Switch Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(lightBlueBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Groups, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Allow Peer Tutoring", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                            Text("Eligible senior students can list paid & volunteer tutoring pods", fontSize = 11.sp, color = textGray)
                        }
                    }

                    Switch(
                        checked = allowPeerTutoring,
                        onCheckedChange = { allowPeerTutoring = it },
                        colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = primaryBlue)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Create Course & Publish Button
            Button(
                onClick = onPublishSuccess,
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

            // Save as Draft
            Button(
                onClick = onPublishSuccess,
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

            // Footer note
            Text(
                text = "Changes will immediately propagate across the student course roster.",
                fontSize = 11.sp,
                color = textGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

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
