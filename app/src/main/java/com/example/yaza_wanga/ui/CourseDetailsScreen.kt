package com.example.yaza_wanga.ui

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

data class TutorModel(
    val name: String,
    val rating: String,
    val reviews: String,
    val subtitle: String,
    val mode: String,
    val availability: String,
    val bio: String,
    val rate: String,
    val isTopRated: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDetailsScreen(
    courseCode: String = "INF 212",
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onContactTutor: (String) -> Unit = {}
) {
    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    val tutors = listOf(
        TutorModel("Chimwemwe Phiri", "5.0", "31", "4th Year • Computer Science", "Both Online & Offline", "Available Today", "Specializes in Frontend, DOM & JavaScript", "FREE • Peer Volunteer", true),
        TutorModel("Thokozani Mwale", "4.9", "24", "Master's Candidate • Applied S...", "Online Only (Google Meet)", "Available Tomorrow", "HTML/CSS semantic architecture & APIs", "MK 2,500 / session"),
        TutorModel("John Banda", "4.8", "24", "3rd Year • Information Syste...", "Both Online & Offline", "Available This Week", "Hands-on coding, CSS Grid & responsive layout", "MK 2,000 / session"),
        TutorModel("Blessings Kaunda", "4.7", "15", "3rd Year • Software Engineer...", "Offline Only (Campus Library)", "Available Mon - Fri", "Lab exercises and exam past papers revision", "MK 1,800 / session"),
        TutorModel("Alinafe Banda", "4.6", "11", "2nd Year • IT Honours", "Online Only", "Evenings (18:00 - 20:00)", "Beginner foundations & assignments coaching", "FREE • Peer Volunteer")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFE0ECFF)
                        ) {
                            Text(
                                text = courseCode,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "Course Details",
                            fontSize = 16.sp,
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
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.BookmarkBorder, contentDescription = "Bookmark", tint = textDark)
                    }
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = textDark)
                    }
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
                            modifier = Modifier.size(20.dp)
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE0ECFF)
                        ) {
                            Text(
                                text = "INF 212",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(50.dp),
                            color = Color(0xFFFEF3C7)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(Icons.Default.Star, null, tint = Color(0xFFD97706), modifier = Modifier.size(14.dp))
                                Text(
                                    text = "4.9 (48 reviews)",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFD97706)
                                )
                            }
                        }
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
                                Icon(Icons.AutoMirrored.Filled.MenuBook, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                                Column {
                                    Text("12 Modules", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
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
                }
            }

            // Discussion Pod Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = lightBlueBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(primaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Forum, null, tint = Color.White, modifier = Modifier.size(22.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("INF 212 Discussion Pod", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("42 students sharing past notes & coaching tips.", fontSize = 12.sp, color = textGray)
                    }
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text("Join Pod", fontSize = 12.sp, fontWeight = FontWeight.Bold)
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
                    Text("Sorted by highest student rating", fontSize = 12.sp, color = textGray)
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

            // Tutors List
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
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
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
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                        Text(tutor.name, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                                        if (tutor.isTopRated) {
                                            Surface(
                                                shape = RoundedCornerShape(50.dp),
                                                color = Color(0xFFE0ECFF)
                                            ) {
                                                Text(
                                                    text = "Top Rated",
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = primaryBlue,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                    Text(tutor.subtitle, fontSize = 11.sp, color = textGray)
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = Color(0xFFFEF3C7)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFD97706), modifier = Modifier.size(14.dp))
                                    Text(
                                        text = "${tutor.rating} (${tutor.reviews})",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD97706)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
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

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = containerBg
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(Icons.Default.Schedule, null, tint = Color(0xFF10B981), modifier = Modifier.size(12.dp))
                                    Text(tutor.availability, fontSize = 11.sp, color = textDark)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = tutor.bio,
                            fontSize = 13.sp,
                            color = textGray
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Session Fee", fontSize = 10.sp, color = textGray)
                                Text(tutor.rate, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = if (tutor.rate.contains("FREE")) Color(0xFF10B981) else primaryBlue)
                            }

                            Button(
                                onClick = { onContactTutor(tutor.name) },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                modifier = Modifier.height(38.dp)
                            ) {
                                Icon(Icons.AutoMirrored.Filled.Chat, null, modifier = Modifier.size(14.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Contact Tutor", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Bottom Banner Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = lightBlueBg,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(primaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.School, null, tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Ace your INF 212 tests together", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text("Book multiple peer sessions or host a group code...", fontSize = 12.sp, color = textGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 950)
@Composable
fun CourseDetailsPreview() {
    Yaza_WangaTheme {
        CourseDetailsScreen()
    }
}
