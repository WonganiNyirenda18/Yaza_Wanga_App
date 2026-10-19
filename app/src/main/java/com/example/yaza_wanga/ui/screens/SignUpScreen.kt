package com.example.yaza_wanga.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

enum class UserRole {
    STUDENT, TUTOR, ADMIN
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    initialRole: UserRole = UserRole.STUDENT,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onCreateStudentAccount: (Map<String, String>) -> Unit = {},
    onCreateTutorAccount: (Map<String, String>) -> Unit = {}
) {
    var role by remember { mutableStateOf(initialRole) }

    // Student form state
    var studentFullName by remember { mutableStateOf("") }
    var studentEmail by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var studentPassword by remember { mutableStateOf("") }
    var studentConfirmPassword by remember { mutableStateOf("") }
    var studentTermsAccepted by remember { mutableStateOf(false) }
    var studentPasswordVisible by remember { mutableStateOf(false) }
    var studentConfirmPasswordVisible by remember { mutableStateOf(false) }

    // Tutor form state
    var tutorFullName by remember { mutableStateOf("") }
    var tutorEmail by remember { mutableStateOf("") }
    var tutorId by remember { mutableStateOf("") }
    var tutorDepartment by remember { mutableStateOf("") }
    var tutorPassword by remember { mutableStateOf("") }
    var tutorConfirmPassword by remember { mutableStateOf("") }
    var tutorTermsAccepted by remember { mutableStateOf(false) }
    var tutorPasswordVisible by remember { mutableStateOf(false) }
    var tutorConfirmPasswordVisible by remember { mutableStateOf(false) }
    var departmentExpanded by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    val faculties = listOf(
        "Faculty of Science & Technology",
        "Faculty of Medicine & Health Sciences",
        "Faculty of Humanities & Social Sciences",
        "Faculty of Business & Economics",
        "Faculty of Engineering",
        "Faculty of Law"
    )

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
                            text = "Create Account",
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
            // Badges row
            if (role == UserRole.STUDENT) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFE0ECFF),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = primaryBlue,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "STUDENT REGISTRATION",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFFE0ECFF)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = primaryBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "ACADEMIC PEER NETWORK",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color(0xFFE6F4EA)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = Color(0xFF137333),
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = "Accredited Portal",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF137333)
                            )
                        }
                    }
                }
            }

            // Headings
            Text(
                text = if (role == UserRole.STUDENT) "Welcome to Yaza Wanga" else "Join as a Peer Tutor",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = textDark,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (role == UserRole.STUDENT)
                    "Create your student account to connect with peer tutors and study groups."
                else
                    "Share your knowledge, empower fellow students, and earn tutoring income with Yaza Wanga.",
                fontSize = 13.sp,
                color = textGray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Segmented Role Toggle (Student vs Tutor)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFE2E8F0),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Student Button
                    val isStudent = role == UserRole.STUDENT
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isStudent) primaryBlue else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { role = UserRole.STUDENT }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = if (isStudent) Color.White else textGray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Student",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isStudent) Color.White else textGray
                            )
                        }
                    }

                    // Tutor Button
                    val isTutor = role == UserRole.TUTOR
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isTutor) primaryBlue else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { role = UserRole.TUTOR }
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = if (isTutor) Color.White else textGray,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tutor",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isTutor) Color.White else textGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Form Fields based on role
            if (role == UserRole.STUDENT) {
                StudentForm(
                    fullName = studentFullName,
                    onFullNameChange = { studentFullName = it },
                    email = studentEmail,
                    onEmailChange = { studentEmail = it },
                    studentId = studentId,
                    onStudentIdChange = { studentId = it },
                    password = studentPassword,
                    onPasswordChange = { studentPassword = it },
                    confirmPassword = studentConfirmPassword,
                    onConfirmPasswordChange = { studentConfirmPassword = it },
                    termsAccepted = studentTermsAccepted,
                    onTermsAcceptedChange = { studentTermsAccepted = it },
                    passwordVisible = studentPasswordVisible,
                    onPasswordVisibleChange = { studentPasswordVisible = it },
                    confirmPasswordVisible = studentConfirmPasswordVisible,
                    onConfirmPasswordVisibleChange = { studentConfirmPasswordVisible = it },
                    onSubmit = {
                        onCreateStudentAccount(
                            mapOf(
                                "fullName" to studentFullName,
                                "email" to studentEmail,
                                "studentId" to studentId,
                                "password" to studentPassword
                            )
                        )
                    },
                    onSignInClick = onSignInClick,
                    primaryBlue = primaryBlue,
                    containerBg = containerBg,
                    textDark = textDark,
                    textGray = textGray
                )
            } else {
                TutorForm(
                    fullName = tutorFullName,
                    onFullNameChange = { tutorFullName = it },
                    email = tutorEmail,
                    onEmailChange = { tutorEmail = it },
                    tutorId = tutorId,
                    onTutorIdChange = { tutorId = it },
                    department = tutorDepartment,
                    onDepartmentChange = { tutorDepartment = it },
                    faculties = faculties,
                    departmentExpanded = departmentExpanded,
                    onDepartmentExpandedChange = { departmentExpanded = it },
                    password = tutorPassword,
                    onPasswordChange = { tutorPassword = it },
                    confirmPassword = tutorConfirmPassword,
                    onConfirmPasswordChange = { tutorConfirmPassword = it },
                    termsAccepted = tutorTermsAccepted,
                    onTermsAcceptedChange = { tutorTermsAccepted = it },
                    passwordVisible = tutorPasswordVisible,
                    onPasswordVisibleChange = { tutorPasswordVisible = it },
                    confirmPasswordVisible = tutorConfirmPasswordVisible,
                    onConfirmPasswordVisibleChange = { tutorConfirmPasswordVisible = it },
                    onSubmit = {
                        onCreateTutorAccount(
                            mapOf(
                                "fullName" to tutorFullName,
                                "email" to tutorEmail,
                                "tutorId" to tutorId,
                                "department" to tutorDepartment,
                                "password" to tutorPassword
                            )
                        )
                    },
                    onSignInClick = onSignInClick,
                    primaryBlue = primaryBlue,
                    containerBg = containerBg,
                    textDark = textDark,
                    textGray = textGray
                )
            }
        }
    }
}

@Composable
fun StudentForm(
    fullName: String, onFullNameChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    studentId: String, onStudentIdChange: (String) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    confirmPassword: String, onConfirmPasswordChange: (String) -> Unit,
    termsAccepted: Boolean, onTermsAcceptedChange: (Boolean) -> Unit,
    passwordVisible: Boolean, onPasswordVisibleChange: (Boolean) -> Unit,
    confirmPasswordVisible: Boolean, onConfirmPasswordVisibleChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onSignInClick: () -> Unit,
    primaryBlue: Color,
    containerBg: Color,
    textDark: Color,
    textGray: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Full Name Field
        Text("Full Name", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            placeholder = { Text("Wongani Nyirenda", color = textGray, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Person, null, tint = textGray, modifier = Modifier.size(20.dp)) },
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

        // School Email Label
        Text("School Email", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("bsc-inf-15-23@unima.ac.mw", color = textGray, fontSize = 13.sp) },
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

        // Student ID / Reg No.
        Text("Student ID / Reg No.", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = studentId,
            onValueChange = onStudentIdChange,
            placeholder = { Text("BSC/INF/21/042", color = textGray, fontSize = 13.sp) },
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

        // Password
        Text("Password", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = { Text("Min. 8 characters", color = textGray, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = textGray, modifier = Modifier.size(20.dp)) },
            trailingIcon = {
                IconButton(onClick = { onPasswordVisibleChange(!passwordVisible) }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        null,
                        tint = textGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

        // Confirm Password
        Text("Confirm Password", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = { Text("Repeat your password", color = textGray, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.LockReset, null, tint = textGray, modifier = Modifier.size(20.dp)) },
            trailingIcon = {
                IconButton(onClick = { onConfirmPasswordVisibleChange(!confirmPasswordVisible) }) {
                    Icon(
                        if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        null,
                        tint = textGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

        Spacer(modifier = Modifier.height(16.dp))

        // Terms Checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = onTermsAcceptedChange,
                colors = CheckboxDefaults.colors(checkedColor = primaryBlue)
            )
            Text(
                text = "I agree to the Terms of Service & Privacy Policy",
                fontSize = 13.sp,
                color = textDark
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Create Account Button
        Button(
            onClick = onSubmit,
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
                    text = "CREATE STUDENT ACCOUNT",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Footer Sign In
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account? ", fontSize = 14.sp, color = textGray)
            Text(
                "Sign In",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue,
                modifier = Modifier.clickable(onClick = onSignInClick)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorForm(
    fullName: String, onFullNameChange: (String) -> Unit,
    email: String, onEmailChange: (String) -> Unit,
    tutorId: String, onTutorIdChange: (String) -> Unit,
    department: String, onDepartmentChange: (String) -> Unit,
    faculties: List<String>,
    departmentExpanded: Boolean, onDepartmentExpandedChange: (Boolean) -> Unit,
    password: String, onPasswordChange: (String) -> Unit,
    confirmPassword: String, onConfirmPasswordChange: (String) -> Unit,
    termsAccepted: Boolean, onTermsAcceptedChange: (Boolean) -> Unit,
    passwordVisible: Boolean, onPasswordVisibleChange: (Boolean) -> Unit,
    confirmPasswordVisible: Boolean, onConfirmPasswordVisibleChange: (Boolean) -> Unit,
    onSubmit: () -> Unit,
    onSignInClick: () -> Unit,
    primaryBlue: Color,
    containerBg: Color,
    textDark: Color,
    textGray: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Full Name Field
        Text("Full Name", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            placeholder = { Text("Amina Banda", color = textGray, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Person, null, tint = textGray, modifier = Modifier.size(20.dp)) },
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

        // School Email
        Text("Institutional Staff / Student Email", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("bsc-inf-15-23@unima.ac.mw", color = textGray, fontSize = 13.sp) },
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

        // Student ID / Staff ID
        Text("Student ID / Staff ID", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = tutorId,
            onValueChange = onTutorIdChange,
            placeholder = { Text("e.g. STU-2023-8891", color = textGray, fontSize = 13.sp) },
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

        // Academic Department / Faculty Dropdown
        Text("Academic Department / Faculty", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        ExposedDropdownMenuBox(
            expanded = departmentExpanded,
            onExpandedChange = { onDepartmentExpandedChange(it) },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = department,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select your faculty or school...", color = textGray, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Business, null, tint = textGray, modifier = Modifier.size(20.dp)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = departmentExpanded) },
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
                onDismissRequest = { onDepartmentExpandedChange(false) }
            ) {
                faculties.forEach { faculty ->
                    DropdownMenuItem(
                        text = { Text(faculty) },
                        onClick = {
                            onDepartmentChange(faculty)
                            onDepartmentExpandedChange(false)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Password
        Text("Password", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            placeholder = { Text("Minimum 8 characters", color = textGray, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Lock, null, tint = textGray, modifier = Modifier.size(20.dp)) },
            trailingIcon = {
                IconButton(onClick = { onPasswordVisibleChange(!passwordVisible) }) {
                    Icon(
                        if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        null,
                        tint = textGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

        // Confirm Password
        Text("Confirm Password", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            placeholder = { Text("Re-enter password", color = textGray, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.LockReset, null, tint = textGray, modifier = Modifier.size(20.dp)) },
            trailingIcon = {
                IconButton(onClick = { onConfirmPasswordVisibleChange(!confirmPasswordVisible) }) {
                    Icon(
                        if (confirmPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        null,
                        tint = textGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
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

        // Terms Checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = onTermsAcceptedChange,
                colors = CheckboxDefaults.colors(checkedColor = primaryBlue)
            )
            Text(
                text = "I agree to the Academic Integrity Code and peer tutor guidelines.",
                fontSize = 13.sp,
                color = textDark
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Create Tutor Account Button
        Button(
            onClick = onSubmit,
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
                    text = "CREATE TUTOR ACCOUNT",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.8.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, null, tint = Color.White, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Footer Sign In
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account? ", fontSize = 14.sp, color = textGray)
            Text(
                "Sign In",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue,
                modifier = Modifier.clickable(onClick = onSignInClick)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 950)
@Composable
fun SignUpStudentPreview() {
    Yaza_WangaTheme {
        SignUpScreen(initialRole = UserRole.STUDENT)
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 950)
@Composable
fun SignUpTutorPreview() {
    Yaza_WangaTheme {
        SignUpScreen(initialRole = UserRole.TUTOR)
    }
}
