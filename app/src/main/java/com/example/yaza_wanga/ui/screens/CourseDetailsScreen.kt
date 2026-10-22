package com.example.yaza_wanga.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

data class TutorModel(
    val name: String,
    val subtitle: String,
    val mode: String,
    val bio: String,
    val rate: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    courseCode: String = "INF 212",
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onContactTutor: (String) -> Unit = {}
) {
    var isEnrolled by remember { mutableStateOf(false) }

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    val tutors = listOf(
        TutorModel("Chimwemwe Phiri", "4th Year • Computer Science", "Both Online & Offline", "Specializes in Frontend, DOM & JavaScript", "FREE • Peer Volunteer"),
        TutorModel("Thokozani Mwale", "Master's Candidate • Applied Science", "Online Only (Google Meet)", "HTML/CSS semantic architecture & APIs", "MK 2,500 / session"),
        TutorModel("John Banda", "3rd Year • Information Systems", "Both Online & Offline", "Hands-on coding, CSS Grid & responsive layout", "MK 2,000 / session"),
        TutorModel("Blessings Kaunda", "3rd Year • Software Engineering", "Offline Only (Campus Library)", "Lab exercises and exam past papers revision", "MK 1,800 / session"),
        TutorModel("Alinafe Banda", "2nd Year • IT Honours", "Online Only", "Beginner foundations & assignments coaching", "FREE • Peer Volunteer")
    )

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
            // Course Header Card with Enroll Course Primary Button
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
                        text = "Introduction to Web Development",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Department of Computer Science & Information Technology",
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
                                    Text("5 Tutors", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
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
                                    Text("Semester 1", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Enroll Course Primary Button
                    Button(
                        onClick = { isEnrolled = true },
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isEnrolled) Color(0xFF10B981) else primaryBlue,
                            disabledContainerColor = Color(0xFF10B981)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(
                            imageVector = if (isEnrolled) Icons.Default.Check else Icons.Default.School,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isEnrolled) "Enrolled in Course" else "Enroll Course",
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

            // Tutors List (Vertical order: Info -> Mode -> Bio -> Fee -> Secondary Contact Button)
            tutors.forEach { tutor ->
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // 1. Tutor Info (Avatar, Name, Subtitle)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.size(48.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFE2E8F0)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Person, null, tint = textGray)
                                }
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF10B981))
                                        .align(Alignment.BottomEnd)
                                        .border(2.dp, Color.White, CircleShape)
                                )
                            }

                            Column {
                                Text(tutor.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text(tutor.subtitle, fontSize = 11.sp, color = textGray)
                            }
                        }

                        // 2. Mode pill
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = containerBg
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Videocam, null, tint = primaryBlue, modifier = Modifier.size(12.dp))
                                Text(tutor.mode, fontSize = 11.sp, color = textDark)
                            }
                        }

                        // 3. Bio text
                        Text(
                            text = tutor.bio,
                            fontSize = 13.sp,
                            color = textGray
                        )

                        // 4. Session Fee
                        Column {
                            Text("Session Fee", fontSize = 10.sp, color = textGray)
                            Text(
                                tutor.rate,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (tutor.rate.contains("FREE")) Color(0xFF10B981) else primaryBlue
                            )
                        }

                        // 5. Secondary Contact Button (Active only after enrollment)
                        Button(
                            onClick = { onContactTutor(tutor.name) },
                            enabled = isEnrolled,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isEnrolled) lightBlueBg else Color(0xFFF1F5F9),
                                disabledContainerColor = Color(0xFFF1F5F9)
                            ),
                            border = if (isEnrolled) BorderStroke(1.dp, primaryBlue) else null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.Chat,
                                null,
                                tint = if (isEnrolled) primaryBlue else textGray,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isEnrolled) "Contact Tutor" else "Enroll to Contact",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isEnrolled) primaryBlue else textGray
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1200)
@Composable
fun CourseDetailsPreview() {
    Yaza_WangaTheme {
        CourseDetailsScreen()
    }
}
