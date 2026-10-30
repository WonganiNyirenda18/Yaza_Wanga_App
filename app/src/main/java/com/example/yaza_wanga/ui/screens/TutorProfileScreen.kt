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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
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
import com.example.yaza_wanga.ui.viewmodel.TutorProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorProfileScreen(
    viewModel: TutorProfileViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onCompleteSetup: () -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val profile = uiState.profile

    var professionalBio by remember(profile) { mutableStateOf(profile?.bio ?: "") }
    var sessionRate by remember(profile) { mutableStateOf(if (profile != null && profile.hourlyRate > 0) profile.hourlyRate.toInt().toString() else "") }
    var teachingMethod by remember(profile) { mutableStateOf(profile?.teachingMethod ?: "Both Online & Offline") }
    var phone by remember(profile) { mutableStateOf(profile?.phone ?: "") }
    var whatsapp by remember(profile) { mutableStateOf(profile?.whatsapp ?: "") }

    var methodExpanded by remember { mutableStateOf(false) }
    val methodOptions = listOf("Both Online & Offline", "Online Only (Google Meet)", "In-Person Only")

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
                        YazaWangaLogo(size = 36.dp)
                        Text(
                            text = "Tutor Profile Setup",
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header instructions
            Column(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFE6F4EA),
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Verified, null, tint = Color(0xFF137333), modifier = Modifier.size(12.dp))
                        Text("TUTOR ONBOARDING • STEP 2", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF137333))
                    }
                }
                Text(
                    text = "Professional Profile & Payout",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Set up your professional bio, tutoring rates, courses you teach, and contact details.",
                    fontSize = 13.sp,
                    color = textGray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Tutor Profile Form Card
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
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // 1. Professional Bio / Expertise
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Professional Bio / Specialization *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        OutlinedTextField(
                            value = professionalBio,
                            onValueChange = { professionalBio = it },
                            placeholder = { Text("Enter professional bio...", color = textGray, fontSize = 13.sp) },
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

                    // 2. Select Courses Taught (from real Firestore courses)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Courses You Teach *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        if (uiState.availableCourses.isEmpty()) {
                            Text("No courses available in system", fontSize = 12.sp, color = textGray)
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                uiState.availableCourses.forEach { course ->
                                    val isSelected = uiState.selectedCourses.contains(course.code)
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { viewModel.toggleCourseSelection(course.code) }
                                            .padding(vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Checkbox(
                                            checked = isSelected,
                                            onCheckedChange = { viewModel.toggleCourseSelection(course.code) },
                                            colors = CheckboxDefaults.colors(checkedColor = primaryBlue)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text("${course.code} - ${course.title}", fontSize = 13.sp, color = textDark)
                                    }
                                }
                            }
                        }
                    }

                    // 3. Teaching Method Dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Teaching Method *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        ExposedDropdownMenuBox(
                            expanded = methodExpanded,
                            onExpandedChange = { methodExpanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = teachingMethod,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = methodExpanded) },
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = containerBg,
                                    unfocusedContainerColor = containerBg,
                                    focusedBorderColor = primaryBlue,
                                    unfocusedBorderColor = Color(0xFFE2E8F0),
                                    focusedTextColor = textDark,
                                    unfocusedTextColor = textDark
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, true)
                            )
                            ExposedDropdownMenu(
                                expanded = methodExpanded,
                                onDismissRequest = { methodExpanded = false }
                            ) {
                                methodOptions.forEach { method ->
                                    DropdownMenuItem(
                                        text = { Text(method) },
                                        onClick = {
                                            teachingMethod = method
                                            methodExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 4. Hourly Rate (0 = Free)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Hourly Rate in MK (0 = Free / Volunteer)", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        OutlinedTextField(
                            value = sessionRate,
                            onValueChange = { sessionRate = it },
                            placeholder = { Text("0 for free or e.g. 2500", color = textGray, fontSize = 13.sp) },
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

                    // 5. Phone Number
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Phone Number *", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { phone = it },
                            placeholder = { Text("+265 ...", color = textGray, fontSize = 13.sp) },
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

                    // 6. WhatsApp Number
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("WhatsApp Number", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        OutlinedTextField(
                            value = whatsapp,
                            onValueChange = { whatsapp = it },
                            placeholder = { Text("+265 ...", color = textGray, fontSize = 13.sp) },
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

            Spacer(modifier = Modifier.height(8.dp))

            // Complete Tutor Setup & Publish Button
            Button(
                onClick = {
                    if (professionalBio.isBlank()) {
                        Toast.makeText(context, "Please enter your professional bio", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (uiState.selectedCourses.isEmpty()) {
                        Toast.makeText(context, "Please select at least one course", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    if (phone.isBlank()) {
                        Toast.makeText(context, "Please enter your phone number", Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    viewModel.saveProfile(
                        bio = professionalBio,
                        rateStr = sessionRate,
                        teachingMethod = teachingMethod,
                        phone = phone,
                        whatsapp = whatsapp,
                        onSuccess = {
                            Toast.makeText(context, "Tutor profile saved successfully (Status: PENDING)", Toast.LENGTH_LONG).show()
                            onCompleteSetup()
                        },
                        onError = { err ->
                            Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                        }
                    )
                },
                enabled = !uiState.isLoading,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = "Complete Tutor Setup & Publish",
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
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1000)
@Composable
fun TutorProfilePreview() {
    Yaza_WangaTheme {
        TutorProfileScreen()
    }
}
