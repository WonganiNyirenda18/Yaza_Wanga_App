package com.example.yaza_wanga.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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

data class CourseModel(
    val code: String,
    val title: String,
    val rating: String,
    val reviews: String,
    val department: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseDirectoryScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateCourses: () -> Unit = {},
    onNavigateTutors: () -> Unit = {},
    onNavigateAlerts: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onViewDetailsClick: (String) -> Unit = {}
) {
    var selectedDepartment by remember { mutableStateOf("All Departments") }

    val departments = listOf("All Departments", "Computer Science & IT", "Mathematics", "Engineering", "Business")
    val quickChips = listOf("INF 212", "Database Systems", "STA 211", "Software Eng", "Calculus")

    val courses = listOf(
        CourseModel("INF 212", "Introduction to Web Development", "4.8", "34 reviews", "Computer Science & IT"),
        CourseModel("COM 311", "Database Systems & Architecture", "5.0", "28 reviews", "Computer Science & IT"),
        CourseModel("STA 211", "Probability Theory & Applied Statistics", "4.9", "19 reviews", "Mathematics"),
        CourseModel("COM 312", "Software Architecture & Agile Methods", "4.7", "41 reviews", "Computer Science & IT"),
        CourseModel("MAT 201", "Multivariable Calculus & Linear Algebra", "4.9", "31 reviews", "Mathematics"),
        CourseModel("INF 111", "Algorithms & Data Structures", "4.6", "22 reviews", "Computer Science & IT")
    )

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Courses",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                        Text(
                            text = "YAZA WANGA PEER HUB",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue,
                            letterSpacing = 0.5.sp
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(primaryBlue),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = textDark
                        )
                    }
                    IconButton(
                        onClick = onNavigateProfile,
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
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateHome,
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = true,
                    onClick = onNavigateCourses,
                    icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Courses") },
                    label = { Text("Courses") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryBlue, selectedTextColor = primaryBlue)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateTutors,
                    icon = { Icon(Icons.Default.School, contentDescription = "Tutors") },
                    label = { Text("Tutors") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateAlerts,
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Alerts") },
                    label = { Text("Alerts") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateProfile,
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = textGray, unselectedTextColor = textGray)
                )
            }
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
            // Quick horizontal filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                quickChips.forEach { chip ->
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = Color.White,
                        modifier = Modifier
                            .height(34.dp)
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(50.dp))
                            .clickable {}
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(Icons.Default.Schedule, null, tint = textGray, modifier = Modifier.size(12.dp))
                            Text(chip, fontSize = 12.sp, color = textDark, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }

            // Department filter chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                departments.forEach { dept ->
                    val selected = selectedDepartment == dept
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (selected) primaryBlue else Color.White,
                        modifier = Modifier
                            .height(38.dp)
                            .clickable { selectedDepartment = dept },
                        border = BorderStroke(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0))
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dept,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) Color.White else textDark
                            )
                        }
                    }
                }
            }

            // Browse Course Directory Header & Accredited badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Browse Course Directory",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${courses.size} curriculum modules available",
                        fontSize = 12.sp,
                        color = textGray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = lightBlueBg,
                    border = BorderStroke(1.dp, Color(0xFFDBEAFE))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(Icons.Default.Verified, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                        Text(
                            text = "Accredited",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue
                        )
                    }
                }
            }

            // Course Cards List
            courses.forEach { course ->
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
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE0ECFF)
                            ) {
                                Text(
                                    text = course.code,
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
                                        text = "${course.rating} (${course.reviews})",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD97706)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = course.title,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = { onViewDetailsClick(course.code) },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg),
                            border = BorderStroke(1.dp, Color(0xFFDBEAFE)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                        ) {
                            Text(
                                text = "View Details",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = primaryBlue
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 950)
@Composable
fun CourseDirectoryPreview() {
    Yaza_WangaTheme {
        CourseDirectoryScreen()
    }
}
