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

    val primaryBlue = Color(0xFF1D4ED8)
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
            // Heading
            Text(
                text = "Complete Your Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
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

                    // Year of Study (Years 1 to 4 only)
                    Text("Year of Study", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                    Spacer(modifier = Modifier.height(8.dp))
                    val years = listOf("Year 1", "Year 2", "Year 3", "Year 4")
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        years.forEach { y ->
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
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) Color.White else textDark
                                        )
                                    }
                                }
                            }
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

            Spacer(modifier = Modifier.height(24.dp))

            // Save & Continue Button (Centered Text)
            Button(
                onClick = onSaveAndContinue,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
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
}

@Preview(showBackground = true, widthDp = 392, heightDp = 950)
@Composable
fun StudentProfilePreview() {
    Yaza_WangaTheme {
        StudentProfileScreen()
    }
}
