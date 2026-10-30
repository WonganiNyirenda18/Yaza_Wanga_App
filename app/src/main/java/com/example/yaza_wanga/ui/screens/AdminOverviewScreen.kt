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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.example.yaza_wanga.ui.viewmodel.AdminOverviewViewModel

@Composable
fun AdminBottomBar(
    currentTab: Int, // 0: Overview, 1: Users, 2: Courses, 3: Content
    onSelectTab: (Int) -> Unit
) {
    val primaryBlue = Color(0xFF1D4ED8)
    val textGray = Color(0xFF64748B)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val tabs = listOf(
            Triple(0, "Overview", Icons.Default.GridView),
            Triple(1, "Users", Icons.Default.Group),
            Triple(2, "Courses", Icons.AutoMirrored.Filled.MenuBook),
            Triple(3, "Content", Icons.Default.VerifiedUser)
        )

        tabs.forEach { (index, label, icon) ->
            val selected = currentTab == index
            NavigationBarItem(
                selected = selected,
                onClick = { onSelectTab(index) },
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOverviewScreen(
    viewModel: AdminOverviewViewModel = viewModel(),
    onNavigateTab: (Int) -> Unit = {},
    onSignOut: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Overview",
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
                            onClick = onSignOut,
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(primaryBlue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Sign Out",
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
            AdminBottomBar(currentTab = 0, onSelectTab = onNavigateTab)
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
                Text("System Administration", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
                Text("Real-Time Analytics & Platform Overview", fontSize = 12.sp, color = textGray)
            }

            // Top Stat Cards Grid (2x2)
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AdminStatCard(
                        title = "Total Users",
                        count = uiState.usersCount.toString(),
                        subtitle = "${uiState.studentsCount} Students • ${uiState.tutorsCount} Tutors",
                        icon = Icons.Default.Group,
                        color = primaryBlue,
                        bgColor = lightBlueBg,
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "Active Courses",
                        count = uiState.coursesCount.toString(),
                        subtitle = "Published Catalog",
                        icon = Icons.AutoMirrored.Filled.MenuBook,
                        color = Color(0xFF0D9488),
                        bgColor = Color(0xFFCCFBF1),
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AdminStatCard(
                        title = "Study Artifacts",
                        count = uiState.materialsCount.toString(),
                        subtitle = "Contributed Content",
                        icon = Icons.Default.Folder,
                        color = Color(0xFFD97706),
                        bgColor = Color(0xFFFEF3C7),
                        modifier = Modifier.weight(1f)
                    )
                    AdminStatCard(
                        title = "Pending Tutors",
                        count = (uiState.usersCount - uiState.studentsCount - uiState.tutorsCount).coerceAtLeast(0).toString(),
                        subtitle = "Awaiting Review",
                        icon = Icons.Default.Verified,
                        color = Color(0xFF7C3AED),
                        bgColor = Color(0xFFF3E8FF),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Top 3 Courses by Actual Enrollment Count
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Top Courses by Enrollment", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                Text("Actual Data", fontSize = 12.sp, color = textGray)
            }

            val topCourses = uiState.topCourses
            if (topCourses.isEmpty() || topCourses.all { it.enrollmentCount == 0 }) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No enrollment data yet", fontSize = 13.sp, color = textGray, fontWeight = FontWeight.Medium)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    topCourses.forEachIndexed { index, topCourse ->
                        DemandModuleItem(
                            rank = String.format("%02d", index + 1),
                            code = topCourse.course.code,
                            desc = topCourse.course.title,
                            demand = "${topCourse.enrollmentCount} enrolled",
                            primaryBlue = primaryBlue,
                            textDark = textDark,
                            textGray = textGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Admin Sign Out Button
            OutlinedButton(
                onClick = onSignOut,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                border = BorderStroke(1.dp, Color(0xFFDC2626)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null, tint = Color(0xFFDC2626), modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Sign Out Administrator", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    count: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    bgColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier.border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, fontSize = 12.sp, color = Color(0xFF64748B), fontWeight = FontWeight.Medium)
                Surface(shape = RoundedCornerShape(8.dp), color = bgColor, modifier = Modifier.size(32.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, null, tint = color, modifier = Modifier.size(18.dp))
                    }
                }
            }
            Text(count, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F172A))
            Text(subtitle, fontSize = 10.sp, color = Color(0xFF64748B))
        }
    }
}

@Composable
fun DemandModuleItem(
    rank: String,
    code: String,
    desc: String,
    demand: String,
    primaryBlue: Color,
    textDark: Color,
    textGray: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = primaryBlue.copy(alpha = 0.1f),
                    modifier = Modifier.size(32.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(rank, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    }
                }

                Column {
                    Text(code, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Text(desc, fontSize = 11.sp, color = textGray, maxLines = 1)
                }
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xFFFEF3C7)
            ) {
                Text(
                    text = demand,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFB45309),
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1000)
@Composable
fun AdminOverviewPreview() {
    Yaza_WangaTheme {
        AdminOverviewScreen()
    }
}
