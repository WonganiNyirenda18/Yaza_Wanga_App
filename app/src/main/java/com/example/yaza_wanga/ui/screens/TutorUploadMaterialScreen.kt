package com.example.yaza_wanga.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorUploadMaterialScreen(
    onBackClick: () -> Unit = {},
    onNavigateDashboard: () -> Unit = {},
    onNavigateSessions: () -> Unit = {},
    onNavigateStudents: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onPublishSuccess: () -> Unit = {}
) {
    var selectedCourse by remember { mutableStateOf("INF 212 • Web Dev") }
    var selectedCategory by remember { mutableStateOf("Lecture Notes") }
    var resourceTitle by remember { mutableStateOf("DOM Manipulation & Event Listeners Comprehe") }
    var topicTag by remember { mutableStateOf("Module 4: JavaScript DOM") }
    var description by remember { mutableStateOf("Step-by-step code walkthrough for semester project 1 with solved past paper questions.") }
    var videoLink by remember { mutableStateOf("") }
    var accessMode by remember { mutableStateOf("free") } // "free" or "restricted"
    var certified by remember { mutableStateOf(false) }

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
                            text = "Upload Course Material",
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
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More",
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
            // Tutor Onboarding Progress Banner
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE0ECFF)),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Verified, null, tint = Color(0xFF137333), modifier = Modifier.size(16.dp))
                            Text(
                                text = "TUTOR ONBOARDING • STEP 10 OF 10",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue
                            )
                        }
                        Text(
                            text = "90% Completed",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF137333)
                        )
                    }

                    LinearProgressIndicator(
                        progress = { 0.90f },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = primaryBlue,
                        trackColor = Color(0xFFCBD5E1)
                    )

                    Text(
                        text = "Publish verified study resources and lecture companions for peer learners.",
                        fontSize = 12.sp,
                        color = textGray
                    )
                }
            }

            // Select Course Header & Chips
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Select Course", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("3 Assigned", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                }

                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val courses = listOf("INF 212 • Web Dev", "STA 211 • Stats", "COM 311 • Database")
                    courses.forEach { course ->
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

            // Material Category Section (2x2 Grid)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Material Category", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)

                val categories = listOf(
                    Triple("Lecture Notes", "PDF, Slides", Icons.Default.Description),
                    Triple("Video Tutorial", "MP4 or stream", Icons.Default.VideoLibrary),
                    Triple("Practice Task", "With solutions", Icons.AutoMirrored.Filled.Assignment),
                    Triple("Past Exam", "Walkthrough", Icons.Default.Quiz)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { (title, subtitle, icon) ->
                                val selected = selectedCategory == title
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = if (selected) lightBlueBg else Color.White),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedCategory = title }
                                        .border(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Icon(icon, null, tint = primaryBlue, modifier = Modifier.size(24.dp))
                                        Column {
                                            Text(title, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                                            Text(subtitle, fontSize = 11.sp, color = textGray)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Resource Details Section
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Description, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                        Text("Resource Details", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                    }

                    // Resource Title
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Resource Title *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        OutlinedTextField(
                            value = resourceTitle,
                            onValueChange = { resourceTitle = it },
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

                    // Module / Topic Tag
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Module / Topic Tag", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        OutlinedTextField(
                            value = topicTag,
                            onValueChange = { topicTag = it },
                            leadingIcon = { Icon(Icons.AutoMirrored.Filled.Label, null, tint = primaryBlue, modifier = Modifier.size(16.dp)) },
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

                    // Brief Description
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Brief Description", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                            Text("${description.length}/250", fontSize = 11.sp, color = textGray)
                        }
                        OutlinedTextField(
                            value = description,
                            onValueChange = { if (it.length <= 250) description = it },
                            minLines = 3,
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

            // Files & Attachments Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Files & Attachments", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("Max 50MB", fontSize = 12.sp, color = textGray)
                }

                // Dropzone Box
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = lightBlueBg,
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp).clip(CircleShape).background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CloudUpload, null, tint = primaryBlue, modifier = Modifier.size(24.dp))
                        }
                        Text("Tap to browse PDF, MP4, or slides", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        Text("Supports PDF, MP4, PPTX, Keynote, Code ZIP", fontSize = 11.sp, color = textGray)
                    }
                }

                // Attached File Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(lightBlueBg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.PictureAsPdf, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text("INF212_DOM_Tutorial_Notes.pdf", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text("4.2 MB • ✓ Ready", fontSize = 11.sp, color = Color(0xFF137333))
                            }
                        }

                        IconButton(onClick = {}) {
                            Icon(Icons.Default.Close, null, tint = textGray, modifier = Modifier.size(18.dp))
                        }
                    }
                }

                // Or provide YouTube / Drive video link
                Spacer(modifier = Modifier.height(4.dp))
                Text("Or provide YouTube / Drive video link", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = videoLink,
                    onValueChange = { videoLink = it },
                    placeholder = { Text("https://youtube.com/watch?v=...", color = textGray, fontSize = 13.sp) },
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

            // Student Access & Visibility Section
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Lock, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                        Text("Student Access & Visibility", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                    }

                    // Option 1: Free for all
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (accessMode == "free") lightBlueBg else containerBg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { accessMode = "free" }
                            .border(1.dp, if (accessMode == "free") primaryBlue else Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = accessMode == "free",
                                onClick = { accessMode = "free" },
                                colors = RadioButtonDefaults.colors(selectedColor = primaryBlue)
                            )
                            Column {
                                Text("Free for all enrolled students", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text("Recommended. Boosts your tutor karma score and campus visibility.", fontSize = 11.sp, color = textGray)
                            }
                        }
                    }

                    // Option 2: Restricted
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (accessMode == "restricted") lightBlueBg else containerBg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { accessMode = "restricted" }
                            .border(1.dp, if (accessMode == "restricted") primaryBlue else Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            RadioButton(
                                selected = accessMode == "restricted",
                                onClick = { accessMode = "restricted" },
                                colors = RadioButtonDefaults.colors(selectedColor = primaryBlue)
                            )
                            Column {
                                Text("Restricted to my 1-on-1 tutoring mentees only", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text("Exclusive for learners booked in your private tutoring cohorts.", fontSize = 11.sp, color = textGray)
                            }
                        }
                    }
                }
            }

            // Certification Checkbox
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = certified,
                    onCheckedChange = { certified = it },
                    colors = CheckboxDefaults.colors(checkedColor = primaryBlue)
                )
                Text(
                    text = "I certify that this material complies with university academic guidelines and does not violate exam confidentiality or student copyright policy.",
                    fontSize = 12.sp,
                    color = textGray,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Publish Material Button
            Button(
                onClick = onPublishSuccess,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.Upload, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PUBLISH MATERIAL", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Footer note
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.VerifiedUser, null, tint = textGray, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Yaza Wanga Academic Peer Network • Instant verification",
                    fontSize = 11.sp,
                    color = textGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1200)
@Composable
fun TutorUploadMaterialPreview() {
    Yaza_WangaTheme {
        TutorUploadMaterialScreen()
    }
}
