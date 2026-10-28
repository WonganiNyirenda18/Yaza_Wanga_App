package com.example.yaza_wanga.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorProfileScreen(
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onCompleteSetup: () -> Unit = {}
) {
    var professionalBio by remember { mutableStateOf("") }
    var sessionRate by remember { mutableStateOf("") }
    var paymentMethod by remember { mutableStateOf("Airtel Money") }
    var accountNumber by remember { mutableStateOf("") }

    var paymentExpanded by remember { mutableStateOf(false) }
    val paymentOptions = listOf("Airtel Money", "TNM Mpamba", "National Bank of Malawi", "Standard Bank")

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
                    text = "Set up your professional bio, tutoring rates, and payment payout methods to start accepting mentees.",
                    fontSize = 13.sp,
                    color = textGray
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Profile Picture Upload Avatar Section with Plus Badge & Format Note
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(96.dp)
                        .clickable { }
                ) {
                    Box(
                        modifier = Modifier
                            .size(96.dp)
                            .clip(CircleShape)
                            .background(lightBlueBg)
                            .border(2.dp, primaryBlue, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = "Upload Photo",
                            tint = primaryBlue,
                            modifier = Modifier.size(36.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(primaryBlue)
                            .border(2.dp, Color.White, CircleShape)
                            .align(Alignment.BottomEnd),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Photo",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Upload Tutor Avatar",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue
                )

                Text(
                    text = "Supported: PNG, JPG • Max size: 5MB",
                    fontSize = 11.sp,
                    color = textGray,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

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
                        Text("Professional Bio / Specialization", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
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

                    // 2. Session Fee / Rate
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Session Fee / Rate", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        OutlinedTextField(
                            value = sessionRate,
                            onValueChange = { sessionRate = it },
                            placeholder = { Text("e.g. FREE • Peer Volunteer", color = textGray, fontSize = 13.sp) },
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

                    // 3. Payment Method Payout Dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Payment Payout Method", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        ExposedDropdownMenuBox(
                            expanded = paymentExpanded,
                            onExpandedChange = { paymentExpanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = paymentMethod,
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = paymentExpanded) },
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
                                expanded = paymentExpanded,
                                onDismissRequest = { paymentExpanded = false }
                            ) {
                                paymentOptions.forEach { method ->
                                    DropdownMenuItem(
                                        text = { Text(method) },
                                        onClick = {
                                            paymentMethod = method
                                            paymentExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 4. Payout Account Number / Phone
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Payout Account Number / Phone", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        OutlinedTextField(
                            value = accountNumber,
                            onValueChange = { accountNumber = it },
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
                onClick = onCompleteSetup,
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
