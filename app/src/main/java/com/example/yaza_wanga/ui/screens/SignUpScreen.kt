package com.example.yaza_wanga.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.example.yaza_wanga.validation.Validator

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    initialRole: UserRole = UserRole.STUDENT,
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onSignInClick: () -> Unit = {},
    onCreateStudentAccount: (Map<String, String>, (String?) -> Unit) -> Unit = { _, _ -> },
    onCreateTutorAccount: (Map<String, String>, (String?) -> Unit) -> Unit = { _, _ -> }
) {
    var role by remember { mutableStateOf(initialRole) }

    // Student form state
    var studentFullName by remember { mutableStateOf("") }
    var studentEmail by remember { mutableStateOf("") }
    var studentPassword by remember { mutableStateOf("") }
    var studentConfirmPassword by remember { mutableStateOf("") }
    var studentTermsAccepted by remember { mutableStateOf(false) }
    var studentPasswordVisible by remember { mutableStateOf(false) }
    var studentConfirmPasswordVisible by remember { mutableStateOf(false) }

    var studentFullNameError by remember { mutableStateOf<String?>(null) }
    var studentEmailError by remember { mutableStateOf<String?>(null) }
    var studentPasswordError by remember { mutableStateOf<String?>(null) }
    var studentConfirmPasswordError by remember { mutableStateOf<String?>(null) }
    var studentTermsError by remember { mutableStateOf<String?>(null) }
    var studentGeneralError by remember { mutableStateOf<String?>(null) }
    var studentIsLoading by remember { mutableStateOf(false) }

    // Tutor form state
    var tutorFullName by remember { mutableStateOf("") }
    var tutorEmail by remember { mutableStateOf("") }
    var tutorPassword by remember { mutableStateOf("") }
    var tutorConfirmPassword by remember { mutableStateOf("") }
    var tutorFaculty by remember { mutableStateOf("") }
    var tutorTermsAccepted by remember { mutableStateOf(false) }
    var tutorPasswordVisible by remember { mutableStateOf(false) }
    var tutorConfirmPasswordVisible by remember { mutableStateOf(false) }
    var tutorFacultyExpanded by remember { mutableStateOf(false) }

    var tutorFullNameError by remember { mutableStateOf<String?>(null) }
    var tutorEmailError by remember { mutableStateOf<String?>(null) }
    var tutorPasswordError by remember { mutableStateOf<String?>(null) }
    var tutorConfirmPasswordError by remember { mutableStateOf<String?>(null) }
    var tutorFacultyError by remember { mutableStateOf<String?>(null) }
    var tutorTermsError by remember { mutableStateOf<String?>(null) }
    var tutorGeneralError by remember { mutableStateOf<String?>(null) }
    var tutorIsLoading by remember { mutableStateOf(false) }

    val faculties = listOf("Faculty of Science", "Faculty of Social Sciences", "Faculty of Humanities", "Faculty of Education", "Faculty of Law", "Faculty of Commerce")

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
                        text = if (role == UserRole.STUDENT) "STUDENT REGISTRATION" else "TUTOR REGISTRATION",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBlue,
                        letterSpacing = 0.5.sp
                    )
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

            if (role == UserRole.STUDENT) {
                if (studentGeneralError != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = studentGeneralError ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                StudentForm(
                    fullName = studentFullName,
                    onFullNameChange = { studentFullName = it; studentFullNameError = null; studentGeneralError = null },
                    email = studentEmail,
                    onEmailChange = { studentEmail = it; studentEmailError = null; studentGeneralError = null },
                    password = studentPassword,
                    onPasswordChange = { studentPassword = it; studentPasswordError = null; studentGeneralError = null },
                    confirmPassword = studentConfirmPassword,
                    onConfirmPasswordChange = { studentConfirmPassword = it; studentConfirmPasswordError = null; studentGeneralError = null },
                    termsAccepted = studentTermsAccepted,
                    onTermsAcceptedChange = { studentTermsAccepted = it; studentTermsError = null; studentGeneralError = null },
                    passwordVisible = studentPasswordVisible,
                    onPasswordVisibleChange = { studentPasswordVisible = it },
                    confirmPasswordVisible = studentConfirmPasswordVisible,
                    onConfirmPasswordVisibleChange = { studentConfirmPasswordVisible = it },
                    fullNameError = studentFullNameError,
                    emailError = studentEmailError,
                    passwordError = studentPasswordError,
                    confirmPasswordError = studentConfirmPasswordError,
                    termsError = studentTermsError,
                    isLoading = studentIsLoading,
                    onSubmit = {
                        val validationError = Validator.validateStudentSignUp(
                            studentFullName, studentEmail, studentPassword, studentConfirmPassword, studentTermsAccepted
                        )
                        if (validationError != null) {
                            when {
                                studentFullName.isBlank() -> studentFullNameError = "Please fill all fields"
                                else -> studentFullNameError = null
                            }
                            val emailErr = Validator.validateEmail(studentEmail)
                            studentEmailError = emailErr

                            val passErr = Validator.validatePassword(studentPassword)
                            studentPasswordError = passErr

                            if (studentConfirmPassword.isBlank()) {
                                studentConfirmPasswordError = "Please fill all fields"
                            } else if (studentPassword != studentConfirmPassword) {
                                studentConfirmPasswordError = "Passwords do not match"
                            } else {
                                studentConfirmPasswordError = null
                            }

                            if (!studentTermsAccepted) {
                                studentTermsError = "You must agree to the Terms of Service & Privacy Policy"
                            } else {
                                studentTermsError = null
                            }

                            studentGeneralError = validationError
                        } else {
                            studentFullNameError = null
                            studentEmailError = null
                            studentPasswordError = null
                            studentConfirmPasswordError = null
                            studentTermsError = null
                            studentGeneralError = null
                            studentIsLoading = true

                            onCreateStudentAccount(
                                mapOf(
                                    "fullName" to studentFullName.trim(),
                                    "email" to studentEmail.trim(),
                                    "password" to studentPassword
                                )
                            ) { err ->
                                studentIsLoading = false
                                if (err != null) {
                                    studentGeneralError = err
                                    studentPassword = ""
                                    studentConfirmPassword = ""
                                }
                            }
                        }
                    },
                    onSignInClick = onSignInClick,
                    primaryBlue = primaryBlue,
                    containerBg = containerBg,
                    textDark = textDark,
                    textGray = textGray
                )
            } else {
                if (tutorGeneralError != null) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.errorContainer,
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    ) {
                        Text(
                            text = tutorGeneralError ?: "",
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                TutorForm(
                    fullName = tutorFullName,
                    onFullNameChange = { tutorFullName = it; tutorFullNameError = null; tutorGeneralError = null },
                    email = tutorEmail,
                    onEmailChange = { tutorEmail = it; tutorEmailError = null; tutorGeneralError = null },
                    password = tutorPassword,
                    onPasswordChange = { tutorPassword = it; tutorPasswordError = null; tutorGeneralError = null },
                    confirmPassword = tutorConfirmPassword,
                    onConfirmPasswordChange = { tutorConfirmPassword = it; tutorConfirmPasswordError = null; tutorGeneralError = null },
                    faculty = tutorFaculty,
                    onFacultyChange = { tutorFaculty = it; tutorFacultyError = null; tutorGeneralError = null },
                    facultyExpanded = tutorFacultyExpanded,
                    onFacultyExpandedChange = { tutorFacultyExpanded = it },
                    faculties = faculties,
                    termsAccepted = tutorTermsAccepted,
                    onTermsAcceptedChange = { tutorTermsAccepted = it; tutorTermsError = null; tutorGeneralError = null },
                    passwordVisible = tutorPasswordVisible,
                    onPasswordVisibleChange = { tutorPasswordVisible = it },
                    confirmPasswordVisible = tutorConfirmPasswordVisible,
                    onConfirmPasswordVisibleChange = { tutorConfirmPasswordVisible = it },
                    fullNameError = tutorFullNameError,
                    emailError = tutorEmailError,
                    passwordError = tutorPasswordError,
                    confirmPasswordError = tutorConfirmPasswordError,
                    facultyError = tutorFacultyError,
                    termsError = tutorTermsError,
                    isLoading = tutorIsLoading,
                    onSubmit = {
                        val validationError = Validator.validateTutorSignUp(
                            tutorFullName, tutorEmail, tutorPassword, tutorConfirmPassword, tutorFaculty, tutorTermsAccepted
                        )
                        if (validationError != null) {
                            if (tutorFullName.isBlank()) tutorFullNameError = "Please fill all fields" else tutorFullNameError = null
                            tutorEmailError = Validator.validateEmail(tutorEmail)
                            tutorPasswordError = Validator.validatePassword(tutorPassword)
                            if (tutorConfirmPassword.isBlank()) tutorConfirmPasswordError = "Please fill all fields"
                            else if (tutorPassword != tutorConfirmPassword) tutorConfirmPasswordError = "Passwords do not match"
                            else tutorConfirmPasswordError = null

                            if (tutorFaculty.isBlank()) tutorFacultyError = "Please select your faculty or school" else tutorFacultyError = null
                            if (!tutorTermsAccepted) tutorTermsError = "You must agree to the Academic Integrity Code and peer tutor guidelines" else tutorTermsError = null

                            tutorGeneralError = validationError
                        } else {
                            tutorFullNameError = null
                            tutorEmailError = null
                            tutorPasswordError = null
                            tutorConfirmPasswordError = null
                            tutorFacultyError = null
                            tutorTermsError = null
                            tutorGeneralError = null
                            tutorIsLoading = true

                            onCreateTutorAccount(
                                mapOf(
                                    "fullName" to tutorFullName.trim(),
                                    "email" to tutorEmail.trim(),
                                    "password" to tutorPassword,
                                    "faculty" to tutorFaculty
                                )
                            ) { err ->
                                tutorIsLoading = false
                                if (err != null) {
                                    tutorGeneralError = err
                                    tutorPassword = ""
                                    tutorConfirmPassword = ""
                                }
                            }
                        }
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
    password: String, onPasswordChange: (String) -> Unit,
    confirmPassword: String, onConfirmPasswordChange: (String) -> Unit,
    termsAccepted: Boolean, onTermsAcceptedChange: (Boolean) -> Unit,
    passwordVisible: Boolean, onPasswordVisibleChange: (Boolean) -> Unit,
    confirmPasswordVisible: Boolean, onConfirmPasswordVisibleChange: (Boolean) -> Unit,
    fullNameError: String?, emailError: String?, passwordError: String?, confirmPasswordError: String?, termsError: String?,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    onSignInClick: () -> Unit,
    primaryBlue: Color,
    containerBg: Color,
    textDark: Color,
    textGray: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Full Name", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            placeholder = { Text("Wongani Nyirenda", color = textGray, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Person, null, tint = textGray, modifier = Modifier.size(20.dp)) },
            singleLine = true,
            isError = fullNameError != null,
            supportingText = fullNameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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

        Text("Student email", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("bsc-inf-15-23@unima.ac.mw", color = textGray, fontSize = 13.sp) },
            singleLine = true,
            isError = emailError != null,
            supportingText = emailError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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
            isError = passwordError != null,
            supportingText = passwordError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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
            isError = confirmPasswordError != null,
            supportingText = confirmPasswordError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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
                color = if (termsError != null) MaterialTheme.colorScheme.error else textDark
            )
        }
        if (termsError != null) {
            Text(
                text = termsError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp, top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onSubmit,
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
        }

        Spacer(modifier = Modifier.height(20.dp))

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
    password: String, onPasswordChange: (String) -> Unit,
    confirmPassword: String, onConfirmPasswordChange: (String) -> Unit,
    faculty: String, onFacultyChange: (String) -> Unit,
    facultyExpanded: Boolean, onFacultyExpandedChange: (Boolean) -> Unit,
    faculties: List<String>,
    termsAccepted: Boolean, onTermsAcceptedChange: (Boolean) -> Unit,
    passwordVisible: Boolean, onPasswordVisibleChange: (Boolean) -> Unit,
    confirmPasswordVisible: Boolean, onConfirmPasswordVisibleChange: (Boolean) -> Unit,
    fullNameError: String?, emailError: String?, passwordError: String?, confirmPasswordError: String?, facultyError: String?, termsError: String?,
    isLoading: Boolean,
    onSubmit: () -> Unit,
    onSignInClick: () -> Unit,
    primaryBlue: Color,
    containerBg: Color,
    textDark: Color,
    textGray: Color
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Full Name", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = fullName,
            onValueChange = onFullNameChange,
            placeholder = { Text("Amina Banda", color = textGray, fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Person, null, tint = textGray, modifier = Modifier.size(20.dp)) },
            singleLine = true,
            isError = fullNameError != null,
            supportingText = fullNameError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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

        Text("Student email", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            placeholder = { Text("bsc-inf-15-23@unima.ac.mw", color = textGray, fontSize = 13.sp) },
            singleLine = true,
            isError = emailError != null,
            supportingText = emailError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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

        // Faculty Dropdown
        Text("Faculty or School", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = textDark)
        Spacer(modifier = Modifier.height(6.dp))
        ExposedDropdownMenuBox(
            expanded = facultyExpanded,
            onExpandedChange = onFacultyExpandedChange,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = faculty,
                onValueChange = {},
                readOnly = true,
                placeholder = { Text("Select your faculty or school", color = textGray, fontSize = 13.sp) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = facultyExpanded) },
                isError = facultyError != null,
                supportingText = facultyError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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
                expanded = facultyExpanded,
                onDismissRequest = { onFacultyExpandedChange(false) }
            ) {
                faculties.forEach { fac ->
                    DropdownMenuItem(
                        text = { Text(fac) },
                        onClick = {
                            onFacultyChange(fac)
                            onFacultyExpandedChange(false)
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

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
            isError = passwordError != null,
            supportingText = passwordError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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
            isError = confirmPasswordError != null,
            supportingText = confirmPasswordError?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
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
                color = if (termsError != null) MaterialTheme.colorScheme.error else textDark
            )
        }
        if (termsError != null) {
            Text(
                text = termsError,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 12.dp, top = 2.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onSubmit,
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
        }

        Spacer(modifier = Modifier.height(20.dp))

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
