package com.example.yaza_wanga.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@Composable
fun GoogleLogo(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val size = size.minDimension
        val strokeWidth = size * 0.22f

        // Blue arc (Right/Top)
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -45f,
            sweepAngle = 135f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        // Green arc (Bottom)
        drawArc(
            color = Color(0xFF34A853),
            startAngle = 90f,
            sweepAngle = 90f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        // Yellow arc (Bottom-Left)
        drawArc(
            color = Color(0xFFFBBC05),
            startAngle = 180f,
            sweepAngle = 45f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        // Red arc (Top-Left)
        drawArc(
            color = Color(0xFFEA4335),
            startAngle = 225f,
            sweepAngle = 135f,
            useCenter = false,
            style = Stroke(width = strokeWidth)
        )
        // Horizontal bar of the G (Blue)
        drawRect(
            color = Color(0xFF4285F4),
            topLeft = Offset(center.x, center.y - strokeWidth / 2f),
            size = Size(size / 2f, strokeWidth)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onLoginClick: (String, String, UserRole) -> Unit = { _, _, _ -> },
    onGoogleSignInClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedRole by remember { mutableStateOf(UserRole.STUDENT) }

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
                            text = "Sign In",
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
            // Top App Icon
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .shadow(4.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(primaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Yaza Wanga Pill Badge
            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color(0xFFE0ECFF),
                modifier = Modifier.padding(vertical = 4.dp)
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
                        text = "Yaza Wanga",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = primaryBlue,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Headings
            Text(
                text = "Hello,",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = textDark
            )
            Text(
                text = "Welcome back",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = primaryBlue
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Sign in to continue learning.",
                fontSize = 14.sp,
                color = textGray
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Student / Tutor Role Selector
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFE2E8F0),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    val isStudent = selectedRole == UserRole.STUDENT
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isStudent) primaryBlue else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { selectedRole = UserRole.STUDENT }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Student Sign In",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isStudent) Color.White else textGray
                            )
                        }
                    }

                    val isTutor = selectedRole == UserRole.TUTOR
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isTutor) primaryBlue else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { selectedRole = UserRole.TUTOR }
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "Tutor Sign In",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isTutor) Color.White else textGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Main Card Form
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(24.dp))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    // School Email Label
                    Text(
                        text = if (selectedRole == UserRole.STUDENT) "School Email" else "Tutor Email / Staff ID Email",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Email TextField
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text(
                                text = "bsc-inf-15-23@unima.ac.mw",
                                color = textGray,
                                fontSize = 14.sp
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = containerBg,
                            unfocusedContainerColor = containerBg,
                            disabledContainerColor = containerBg,
                            focusedBorderColor = primaryBlue,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedTextColor = textDark,
                            unfocusedTextColor = textDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Password Label
                    Text(
                        text = "Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = textDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Password TextField
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = {
                            Text(
                                text = "Enter your password",
                                color = textGray,
                                fontSize = 14.sp
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = textGray,
                                modifier = Modifier.size(20.dp)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle password visibility",
                                    tint = textGray,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        },
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = containerBg,
                            unfocusedContainerColor = containerBg,
                            disabledContainerColor = containerBg,
                            focusedBorderColor = primaryBlue,
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedTextColor = textDark,
                            unfocusedTextColor = textDark
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Forgot Password
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Text(
                            text = "Forgot password?",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = primaryBlue,
                            modifier = Modifier.clickable(onClick = onForgotPasswordClick)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Login Button
                    Button(
                        onClick = { onLoginClick(email, password, selectedRole) },
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
                                text = if (selectedRole == UserRole.STUDENT) "LOGIN AS STUDENT" else "LOGIN AS TUTOR",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                letterSpacing = 1.sp
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

                    Spacer(modifier = Modifier.height(20.dp))

                    // Divider with "OR CONTINUE WITH"
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 1.dp,
                            color = Color(0xFFE2E8F0)
                        )
                        Text(
                            text = " OR CONTINUE WITH ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textGray
                        )
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            thickness = 1.dp,
                            color = Color(0xFFE2E8F0)
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Sign in with Google Button
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = lightBlueBg,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .clickable(onClick = onGoogleSignInClick),
                        border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .border(1.dp, Color(0xFFDBEAFE), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                GoogleLogo(modifier = Modifier.size(18.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Sign in with Google",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Footer Sign Up
            Row(
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account? ",
                    fontSize = 14.sp,
                    color = textGray
                )
                Text(
                    text = "Sign Up",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = primaryBlue,
                    modifier = Modifier.clickable(onClick = onSignUpClick)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 850)
@Composable
fun SignInScreenPreview() {
    Yaza_WangaTheme {
        SignInScreen()
    }
}
