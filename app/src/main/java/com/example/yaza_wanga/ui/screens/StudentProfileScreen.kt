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
import com.example.yaza_wanga.validation.Validator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentProfileScreen(
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSaveAndContinue: (Map<String, String>, (String?) -> Unit) -> Unit = { _, _ -> }
) {
    var department by remember { mutableStateOf("") }
    var programme by remember { mutableStateOf("") }
    var yearOfStudy by remember { mutableStateOf("Year 3") }
    var phone by remember { mutableStateOf("") }

    var departmentExpanded by remember { mutableStateOf(false) }
    var programmeExpanded by remember { mutableStateOf(false) }

    var departmentError by remember { mutableStateOf<String?>(null) }
    var programmeError by remember { mutableStateOf<String?>(null) }
    var phoneError by remember { mutableStateOf<String?>(null) }
    var generalError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val departments = listOf("Computing Department", "Department of Mathematics & Statistics", "Department of Physics", "Department of Business Studies")
    val programmes = listOf("Computer Science", "Information Systems", "Computer Network Engineering", "Business Information Technology")

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
                            text = "Profile Setup",
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
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Complete Your Profile",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Please verify your academic details and upload a photo to personalize your peer tutoring dashboard.",
                    fontSize = 13.sp,
                    color = textGray
                )
            }

            if (generalError != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.errorContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = generalError ?: "",
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Profile Picture Upload Avatar Section
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
                    text = "Upload Profile Picture",
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

            // Profile Form Card
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
                    // 1. Department Dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Department", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        ExposedDropdownMenuBox(
                            expanded = departmentExpanded,
                            onExpandedChange = { departmentExpanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = department,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { Text("Select your department...", color = textGray, fontSize = 13.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = departmentExpanded) },
                                isError = departmentError != null,
                                supportingText = departmentError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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
                                expanded = departmentExpanded,
                                onDismissRequest = { departmentExpanded = false }
                            ) {
                                departments.forEach { dept ->
                                    DropdownMenuItem(
                                        text = { Text(dept) },
                                        onClick = {
                                            department = dept
                                            departmentError = null
                                            departmentExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 2. Programme of Study Dropdown
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Programme of Study", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        ExposedDropdownMenuBox(
                            expanded = programmeExpanded,
                            onExpandedChange = { programmeExpanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = programme,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { Text("Select your programme...", color = textGray, fontSize = 13.sp) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = programmeExpanded) },
                                isError = programmeError != null,
                                supportingText = programmeError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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
                                expanded = programmeExpanded,
                                onDismissRequest = { programmeExpanded = false }
                            ) {
                                programmes.forEach { prog ->
                                    DropdownMenuItem(
                                        text = { Text(prog) },
                                        onClick = {
                                            programme = prog
                                            programmeError = null
                                            programmeExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }

                    // 3. Year of Study Selection
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Year of Study", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("Year 1", "Year 2", "Year 3", "Year 4").forEach { yr ->
                                val selected = yearOfStudy == yr
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) primaryBlue else Color.White,
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(40.dp)
                                        .clickable { yearOfStudy = yr },
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

                    // 4. Phone Number
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Phone Number", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
                        OutlinedTextField(
                            value = phone,
                            onValueChange = {
                                phone = it
                                phoneError = null
                                generalError = null
                            },
                            placeholder = { Text("+265 995 358 915", color = textGray.copy(alpha = 0.6f), fontSize = 13.sp) },
                            singleLine = true,
                            isError = phoneError != null,
                            supportingText = phoneError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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

            // Continue to Dashboard Button
            Button(
                onClick = {
                    val validationError = Validator.validateStudentProfile(department, programme, phone)
                    if (validationError != null) {
                        if (department.isBlank() || programme.isBlank()) {
                            val err = "Please select your department and programme"
                            departmentError = err
                            programmeError = err
                        } else {
                            departmentError = null
                            programmeError = null
                        }
                        phoneError = Validator.validatePhone(phone)
                        generalError = validationError
                    } else {
                        departmentError = null
                        programmeError = null
                        phoneError = null
                        generalError = null
                        isLoading = true

                        onSaveAndContinue(
                            mapOf(
                                "department" to department,
                                "programme" to programme,
                                "yearOfStudy" to yearOfStudy,
                                "phone" to phone
                            )
                        ) { err ->
                            isLoading = false
                            if (err != null) {
                                generalError = err
                            }
                        }
                    }
                },
                enabled = !isLoading,
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
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                    } else {
                        Text(
                            text = "Continue to Dashboard",
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
fun StudentProfilePreview() {
    Yaza_WangaTheme {
        StudentProfileScreen()
    }
}
