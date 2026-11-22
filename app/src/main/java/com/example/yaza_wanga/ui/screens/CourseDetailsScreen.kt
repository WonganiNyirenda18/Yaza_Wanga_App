package com.example.yaza_wanga.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Chat
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
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.example.yaza_wanga.ui.viewmodel.CourseDetailsViewModel
import kotlinx.coroutines.launch
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    courseCode: String,
    viewModel: CourseDetailsViewModel = viewModel(),
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onContactTutor: (String) -> Unit = {}
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(courseCode) {
        viewModel.loadCourse(courseCode)
    }

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    val course = uiState.course
    val isEnrolled = uiState.isEnrolled
    val hasEligibleTutor = uiState.hasEligibleTutor

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Course Details",
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
                        onClick = onProfileClick,
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
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Course Header Card
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
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFFE0ECFF)
                    ) {
                        Text(
                            text = courseCode,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = course?.title ?: courseCode,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = course?.department ?: "",
                        fontSize = 12.sp,
                        color = textGray
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats Pills Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = containerBg,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.School, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                                Column {
                                    Text("${uiState.tutors.size} Tutors", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = containerBg,
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Default.CalendarToday, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                                Column {
                                    Text(course?.semester ?: "Semester 1", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isEnrolled && !hasEligibleTutor) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFEF2F2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.ErrorOutline, null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                                Text(
                                    text = "No eligible tutor is currently available for this course. Enrollment is closed.",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFDC2626)
                                )
                            }
                        }
                    }

                    // Enroll Course Primary Button
                    Button(
                        onClick = {
                            viewModel.enrollCourse(courseCode) { _, err ->
                                if (err != null) {
                                    Toast.makeText(context, err, Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        enabled = !isEnrolled && hasEligibleTutor,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEnrolled) Color(0xFF10B981) else primaryBlue,
                            disabledContainerColor = if (isEnrolled) Color(0xFF10B981) else Color(0xFF94A3B8)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(
                            imageVector = if (isEnrolled) Icons.Default.Check else if (hasEligibleTutor) Icons.Default.School else Icons.Default.Block,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEnrolled) "Enrolled in Course" else if (hasEligibleTutor) "Enroll Course" else "Enrollment Unavailable",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            // Registered Tutors Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Registered Tutors", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text(
                        text = if (isEnrolled) "Sorted by highest student rating" else "Enroll course to unlock tutor contact",
                        fontSize = 12.sp,
                        color = if (isEnrolled) textGray else primaryBlue
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE2E8F0))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text("Highest Rated", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        Icon(Icons.Default.KeyboardArrowDown, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                    }
                }
            }

            if (uiState.tutors.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No eligible tutor currently available for this course.", fontSize = 13.sp, color = textGray)
                }
            }

            // Tutors List
            uiState.tutors.forEach { tutor ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(modifier = Modifier.size(52.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(lightBlueBg)
                                    .border(2.dp, primaryBlue, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tutor.name.take(2).uppercase().ifEmpty { "TU" },
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(
                                    text = tutor.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textDark
                                )
                                Icon(Icons.Default.Verified, "Verified", tint = primaryBlue, modifier = Modifier.size(16.dp))
                            }

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = "${tutor.teachingMethod} • ${if (tutor.hourlyRate <= 0) "Free" else "MK ${tutor.hourlyRate.toInt()}/session"}",
                                fontSize = 12.sp,
                                color = textGray
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFF59E0B), modifier = Modifier.size(14.dp))
                                Text(
                                    text = "${tutor.rating} (${tutor.reviewCount} reviews)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textDark
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = {
                                if (!isEnrolled) {
                                    Toast.makeText(context, "Please enroll in the course first to contact tutor", Toast.LENGTH_LONG).show()
                                    return@IconButton
                                }
                                coroutineScope.launch {
                                    val currentUser = AppContainer.authRepository.fetchCurrentUser().getOrNull()
                                    val studentName = currentUser?.name?.ifBlank { currentUser.email.substringBefore("@") } ?: currentUser?.email?.substringBefore("@") ?: ""
                                    val phone = tutor.whatsapp.ifBlank { tutor.phone }
                                    if (phone.isBlank()) {
                                        Toast.makeText(context, "Contact details unavailable for this tutor", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val message = "Hello ${tutor.name}, I am $studentName. I would like to inquire about tutoring for $courseCode."
                                        try {
                                            val digits = phone.filter { it.isDigit() }
                                            val encodedMsg = URLEncoder.encode(message, "UTF-8")
                                            val uri = "https://wa.me/$digits?text=$encodedMsg".toUri()
                                            val intent = Intent(Intent.ACTION_VIEW, uri)
                                            context.startActivity(intent)
                                        } catch (_: ActivityNotFoundException) {
                                            Toast.makeText(context, "WhatsApp is not installed on this device", Toast.LENGTH_SHORT).show()
                                        } catch (_: Exception) {
                                            Toast.makeText(context, "Contact details unavailable for this tutor", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(if (isEnrolled) primaryBlue else Color(0xFFCBD5E1))
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Chat,
                                contentDescription = "Chat on WhatsApp",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1000)
@Composable
fun CourseDetailsPreview() {
    Yaza_WangaTheme {
        CourseDetailsScreen(courseCode = "INF 212")
    }
}
