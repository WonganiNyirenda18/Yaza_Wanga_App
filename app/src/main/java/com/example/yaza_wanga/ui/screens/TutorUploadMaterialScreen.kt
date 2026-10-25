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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.example.yaza_wanga.ui.viewmodel.TutorUploadMaterialViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorUploadMaterialScreen(
    viewModel: TutorUploadMaterialViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onNavigateDashboard: () -> Unit = {},
    onNavigateSessions: () -> Unit = {},
    onNavigateStudents: () -> Unit = {},
    onNavigateMenu: () -> Unit = {},
    onPublishSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    var selectedCourse by remember { mutableStateOf("INF 212") }
    var selectedCategory by remember { mutableStateOf("PDF") }
    var resourceTitle by remember { mutableStateOf("") }
    var topicTag by remember { mutableStateOf("Module 4: JavaScript DOM") }
    var description by remember { mutableStateOf("") }
    var videoLink by remember { mutableStateOf("") }
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
            // Select Course Header & Chips
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Select Course *", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text("3 Assigned", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                }

                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val courses = listOf("INF 212", "STA 211", "COM 311")
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

            // Material Category Section
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Material Category", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)

                val categories = listOf(
                    Triple("PDF", "Lecture Notes", Icons.Default.Description),
                    Triple("MP4", "Video Tutorial", Icons.Default.VideoLibrary),
                    Triple("ASSIGNMENT", "Practice Task", Icons.AutoMirrored.Filled.Assignment),
                    Triple("EXAM", "Past Exam", Icons.Default.Quiz)
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    categories.chunked(2).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowItems.forEach { (catKey, title, icon) ->
                                val selected = selectedCategory == catKey
                                Card(
                                    shape = RoundedCornerShape(14.dp),
                                    colors = CardDefaults.cardColors(containerColor = if (selected) lightBlueBg else Color.White),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { selectedCategory = catKey }
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
                                            Text(catKey, fontSize = 11.sp, color = textGray)
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

                    // Resource Title *
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Resource Title *", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        OutlinedTextField(
                            value = resourceTitle,
                            onValueChange = { resourceTitle = it },
                            placeholder = { Text("e.g., DOM Manipulation Notes") },
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

                    // Brief Description (Max 250 validation)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Brief Description", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                            Text("${description.length}/250", fontSize = 11.sp, color = if (description.length > 250) Color(0xFFDC2626) else textGray)
                        }
                        OutlinedTextField(
                            value = description,
                            onValueChange = { if (it.length <= 250) description = it },
                            placeholder = { Text("Max 250 characters summary...") },
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
                        Text("File attached successfully (Simulated < 50MB)", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    }
                }

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

            // Certification Checkbox *
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
                    text = "I certify that this material complies with university academic guidelines and does not violate exam confidentiality or student copyright policy. *",
                    fontSize = 12.sp,
                    color = textDark,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Publish Material Button with validation
            Button(
                onClick = {
                    viewModel.uploadMaterial(
                        courseCode = selectedCourse,
                        title = resourceTitle,
                        description = description,
                        category = selectedCategory,
                        fileUrl = videoLink,
                        fileSizeMb = 5.0,
                        certified = certified,
                        onSuccess = {
                            Toast.makeText(context, "Material published successfully", Toast.LENGTH_SHORT).show()
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
                    Icon(Icons.Default.Upload, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PUBLISH MATERIAL", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                }
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
