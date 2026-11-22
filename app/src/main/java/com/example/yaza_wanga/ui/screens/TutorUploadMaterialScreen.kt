package com.example.yaza_wanga.ui.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.automirrored.filled.InsertDriveFile
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.repository.SelectedFileInfo
import com.example.yaza_wanga.data.repository.UploadState
import com.example.yaza_wanga.ui.theme.*
import com.example.yaza_wanga.ui.viewmodel.TutorUploadMaterialViewModel
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorUploadMaterialScreen(
    viewModel: TutorUploadMaterialViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onNavigateDashboard: () -> Unit = {},
    onNavigateSessions: () -> Unit = {},
    onNavigateStudents: () -> Unit = {},
    onNavigateMenu: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onPublishSuccess: () -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tutorCourses by viewModel.tutorCourses.collectAsStateWithLifecycle()

    var selectedCourse by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("PDF") }
    var resourceTitle by remember { mutableStateOf("") }
    var topicTag by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var videoLink by remember { mutableStateOf("") }
    var certified by remember { mutableStateOf(false) }

    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    var selectedFileInfo by remember { mutableStateOf<SelectedFileInfo?>(null) }
    var isUploading by remember { mutableStateOf(false) }
    var uploadProgress by remember { mutableIntStateOf(0) }
    var uploadError by remember { mutableStateOf<String?>(null) }

    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            val info = AppContainer.storageRepository.getFileInfo(context, uri)
            if (info != null) {
                if (info.sizeMb > 50.0) {
                    uploadError = "File size (${String.format(Locale.getDefault(), "%.1f", info.sizeMb)} MB) exceeds the 50 MB limit."
                    selectedUri = null
                    selectedFileInfo = null
                } else {
                    uploadError = null
                    selectedUri = uri
                    selectedFileInfo = info
                }
            } else {
                uploadError = "Unreadable file or permission was denied."
            }
        }
    }

    LaunchedEffect(tutorCourses) {
        if (tutorCourses.isNotEmpty() && selectedCourse.isBlank()) {
            selectedCourse = tutorCourses.first()
        }
    }

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
                            text = "Upload Notes & Artifacts",
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
                    Triple(3, "Menu", Icons.Default.Menu)
                )

                tabs.forEach { (index, label, icon) ->
                    val selected = index == 0
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            when (index) {
                                0 -> onNavigateDashboard()
                                1 -> onNavigateSessions()
                                2 -> onNavigateStudents()
                                3 -> onNavigateMenu()
                            }
                        },
                        icon = {
                            Icon(imageVector = icon, contentDescription = label, tint = if (selected) primaryBlue else textGray)
                        },
                        label = {
                            Text(text = label, fontSize = 11.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal, color = if (selected) primaryBlue else textGray)
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(Icons.Default.School, null, tint = primaryBlue, modifier = Modifier.size(48.dp))
                        Text("No Assigned Courses", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text("You must set up your tutor profile and select at least one course before uploading study materials.", fontSize = 13.sp, color = textGray)
                        Button(
                            onClick = onNavigateProfile,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
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
                // Select Course Header & Chips
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                            Text("Select Course", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                            Text("*", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                        }
                        Text("${tutorCourses.size} Assigned", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
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
                    if (selectedCourse.isBlank()) {
                        Text("Please select a course to upload material for", fontSize = 11.sp, color = Color(0xFFDC2626))
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
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
                                placeholder = { Text("e.g. Module 4: JavaScript DOM") },
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

                    if (isUploading) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = lightBlueBg),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                LinearProgressIndicator(
                                    progress = { uploadProgress / 100f },
                                    color = primaryBlue,
                                    trackColor = Color(0xFFDBEAFE),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp))
                                )
                                Text("Uploading file to Firebase Storage ($uploadProgress%)...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            }
                        }
                    } else if (selectedFileInfo != null) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, primaryBlue, RoundedCornerShape(16.dp))
                        ) {
                            Row(
                                modifier = Modifier
                                    .padding(14.dp)
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(lightBlueBg),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.InsertDriveFile, null, tint = primaryBlue, modifier = Modifier.size(22.dp))
                                    }

                                    Column {
                                        Text(selectedFileInfo!!.name, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                                        Text("${String.format(Locale.getDefault(), "%.1f", selectedFileInfo!!.sizeMb)} MB", fontSize = 11.sp, color = textGray)
                                    }
                                }

                                IconButton(onClick = {
                                    selectedUri = null
                                    selectedFileInfo = null
                                }) {
                                    Icon(Icons.Default.Close, "Remove attachment", tint = Color(0xFFDC2626))
                                }
                            }
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = lightBlueBg,
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
                                .clickable { filePickerLauncher.launch("*/*") }
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(CircleShape)
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.CloudUpload, null, tint = primaryBlue, modifier = Modifier.size(24.dp))
                                }
                                Text("Choose File from Device or Google Drive", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                                Text("Tap to browse documents, PDFs, or slides", fontSize = 11.sp, color = textGray)
                            }
                        }
                    }

                    if (!uploadError.isNullOrBlank()) {
                        Text(uploadError!!, fontSize = 12.sp, color = Color(0xFFDC2626), fontWeight = FontWeight.SemiBold)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Or provide YouTube / Drive video link", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                    Text("Note: For Google Drive or YouTube links, ensure students are granted view permission.", fontSize = 11.sp, color = textGray)
                    Spacer(modifier = Modifier.height(2.dp))
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

                // Publish Material Button with validation and Storage Upload
                Button(
                    onClick = {
                        if (selectedCourse.isBlank()) {
                            Toast.makeText(context, "Please select a course", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (resourceTitle.isBlank()) {
                            Toast.makeText(context, "Please enter a resource title", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (selectedUri == null && videoLink.isBlank()) {
                            Toast.makeText(context, "Please attach a file or provide a video link", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        if (!certified) {
                            Toast.makeText(context, "You must certify compliance with academic guidelines", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        if (selectedUri != null && selectedFileInfo != null) {
                            val fileInfo = selectedFileInfo!!
                            isUploading = true
                            uploadProgress = 0
                            uploadError = null
                            val customMaterialId = UUID.randomUUID().toString()

                            coroutineScope.launch {
                                AppContainer.storageRepository.uploadMaterialFile(context, selectedCourse, customMaterialId, fileInfo).collect { state ->
                                    when (state) {
                                        is UploadState.Progress -> {
                                            uploadProgress = state.percentage
                                        }
                                        is UploadState.Success -> {
                                            viewModel.uploadMaterial(
                                                courseCode = selectedCourse,
                                                title = resourceTitle,
                                                description = description,
                                                category = selectedCategory,
                                                fileUrl = state.downloadUrl,
                                                fileName = state.fileName,
                                                mimeType = state.mimeType,
                                                fileSizeMb = state.fileSizeMb,
                                                isExternalLink = false,
                                                certified = certified,
                                                tag = topicTag,
                                                customMaterialId = customMaterialId,
                                                onSuccess = {
                                                    isUploading = false
                                                    Toast.makeText(context, "Material uploaded & published successfully!", Toast.LENGTH_SHORT).show()
                                                    onPublishSuccess()
                                                },
                                                onError = { err ->
                                                    isUploading = false
                                                    uploadError = err
                                                }
                                            )
                                        }
                                        is UploadState.Error -> {
                                            isUploading = false
                                            uploadError = state.message
                                        }
                                        else -> {}
                                    }
                                }
                            }
                        } else {
                            viewModel.uploadMaterial(
                                courseCode = selectedCourse,
                                title = resourceTitle,
                                description = description,
                                category = selectedCategory,
                                fileUrl = videoLink,
                                isExternalLink = true,
                                certified = certified,
                                tag = topicTag,
                                onSuccess = {
                                    Toast.makeText(context, "External link published successfully!", Toast.LENGTH_SHORT).show()
                                    onPublishSuccess()
                                },
                                onError = { err ->
                                    uploadError = err
                                }
                            )
                        }
                    },
                    enabled = !isUploading && !uiState.isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                        if (isUploading || uiState.isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("UPLOADING ($uploadProgress%)...", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        } else {
                            Icon(Icons.Default.Upload, null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("PUBLISH MATERIAL", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White, letterSpacing = 1.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1100)
@Composable
fun TutorUploadMaterialPreview() {
    Yaza_WangaTheme {
        TutorUploadMaterialScreen()
    }
}
