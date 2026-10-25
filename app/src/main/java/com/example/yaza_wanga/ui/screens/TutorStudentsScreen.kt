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
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

data class StudentListItem(
    val name: String,
    val course: String,
    val regNumber: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TutorStudentsScreen(
    onNavigateDashboard: () -> Unit = {},
    onNavigateSessions: () -> Unit = {},
    onNavigateStudents: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onViewStudentDetails: (String) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableIntStateOf(0) } // 0: All, 1: Online, 2: Offline
    var currentPage by remember { mutableIntStateOf(1) }

    val primaryBlue = Color(0xFF1D4ED8)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    val studentsList = listOf(
        StudentListItem("Wongani Nyirenda", "INF 212 • Web Development", "bsc-inf-25-23"),
        StudentListItem("Chisomo Phiri", "COM 311 • Database Systems", "bsc-com-17-23"),
        StudentListItem("Alinafe Banda", "STA 211 • Probability & Stats", "bed-com-27-23"),
        StudentListItem("Trinity Kambanje", "INF 212 • Web Development", "bsc-com-ne-25-23"),
        StudentListItem("Madalitso Kasasi", "COM 311 • Database Systems", "bsc-com-ne-17-23"),
        StudentListItem("Martha Bulambo", "STA 211 • Probability & Stats", "bsc-com-27-23"),
        StudentListItem("Pemphero Kalonga", "INF 212 • Web Development", "bsc-com-39-23"),
        StudentListItem("Prosper Tulambe", "COM 311 • Database Systems", "bed-com-22-23")
    )

    val filteredStudents = studentsList.filter {
        it.name.contains(searchQuery, ignoreCase = true) ||
                it.course.contains(searchQuery, ignoreCase = true) ||
                it.regNumber.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "My Students",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                    },
                    navigationIcon = {
                        Box(modifier = Modifier.padding(start = 12.dp)) {
                            YazaWangaLogo(size = 36.dp)
                        }
                    },
                    actions = {
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
                HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
            }
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                val tabs = listOf(
                    Triple(0, "Dashboard", Icons.Default.Home),
                    Triple(1, "Sessions", Icons.Default.CalendarToday),
                    Triple(2, "Students", Icons.Default.School),
                    Triple(3, "Profile", Icons.Default.Person)
                )

                tabs.forEach { (index, label, icon) ->
                    val selected = index == 2 // Students tab selected
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            when (index) {
                                0 -> onNavigateDashboard()
                                1 -> onNavigateSessions()
                                2 -> onNavigateStudents()
                                3 -> onNavigateProfile()
                            }
                        },
                        icon = {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = label,
                                    tint = if (selected) primaryBlue else textGray
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Box(
                                    modifier = Modifier
                                        .width(24.dp)
                                        .height(3.dp)
                                        .clip(RoundedCornerShape(1.5.dp))
                                        .background(if (selected) primaryBlue else Color.Transparent)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                color = if (selected) primaryBlue else textGray
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent,
                            selectedIconColor = primaryBlue,
                            selectedTextColor = primaryBlue,
                            unselectedIconColor = textGray,
                            unselectedTextColor = textGray
                        )
                    )
                }
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
            // Header title & caption
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Assigned Students", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
                Text("Track Students and Monitor Progress", fontSize = 12.sp, color = textGray)
            }

            // Summary Stats Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Active Mentees", fontSize = 11.sp, color = textGray)
                        Text("18 Students", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search your students...", color = textGray, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = textGray, modifier = Modifier.size(20.dp)) },
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

            // Filter Tabs (All, Online, Offline) inspired by reference screenshot
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf("All", "Online (12)", "Offline (6)")
                filters.forEachIndexed { idx, filterName ->
                    val selected = selectedFilter == idx
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (selected) Color(0xFF0F172A) else Color.White,
                        modifier = Modifier.clickable { selectedFilter = idx },
                        border = BorderStroke(1.dp, if (selected) Color(0xFF0F172A) else Color(0xFFE2E8F0))
                    ) {
                        Text(
                            text = filterName,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (selected) Color.White else textDark,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Student Tabular List Card (Inspired by reference screenshot)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    // Table Header Row: Name & Course (Order: Name then Course)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(containerBg)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Name", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textGray, modifier = Modifier.weight(1f))
                        Text("Course", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textGray, modifier = Modifier.weight(1.2f))
                    }

                    HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

                    // Student Rows
                    filteredStudents.forEachIndexed { index, student ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onViewStudentDetails(student.name) }
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // 1. Name
                            Text(
                                text = student.name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textDark,
                                modifier = Modifier.weight(1f)
                            )
                            // 2. Course
                            Text(
                                text = student.course,
                                fontSize = 12.sp,
                                color = primaryBlue,
                                fontWeight = FontWeight.Medium,
                                modifier = Modifier.weight(1.2f)
                            )
                        }

                        if (index < filteredStudents.size - 1) {
                            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
                        }
                    }

                    if (filteredStudents.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No students found matching search.", fontSize = 13.sp, color = textGray)
                        }
                    }

                    HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)

                    // Pagination Bar (Previous, Page X of Y, Next)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { if (currentPage > 1) currentPage-- },
                            enabled = currentPage > 1,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0F172A),
                                disabledContainerColor = Color(0xFFE2E8F0)
                            ),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Previous", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Text("Page $currentPage of 3", fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = textDark)

                        Button(
                            onClick = { if (currentPage < 3) currentPage++ },
                            enabled = currentPage < 3,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0F172A),
                                disabledContainerColor = Color(0xFFE2E8F0)
                            ),
                            modifier = Modifier.height(36.dp)
                        ) {
                            Text("Next", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
fun TutorStudentsPreview() {
    Yaza_WangaTheme {
        TutorStudentsScreen()
    }
}
