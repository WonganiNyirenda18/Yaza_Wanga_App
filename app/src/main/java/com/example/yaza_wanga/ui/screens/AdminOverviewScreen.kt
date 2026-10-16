package com.example.yaza_wanga.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
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
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@Composable
fun AdminBottomBar(
    currentTab: Int, // 0: Overview, 1: Users, 2: Courses, 3: Content, 4: Finance
    onSelectTab: (Int) -> Unit
) {
    val primaryBlue = Color(0xFF1D4ED8)
    val textGray = Color(0xFF64748B)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentTab == 0,
            onClick = { onSelectTab(0) },
            icon = { Icon(Icons.Default.GridView, contentDescription = "Overview") },
            label = { Text("Overview") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryBlue, selectedTextColor = primaryBlue, unselectedIconColor = textGray, unselectedTextColor = textGray)
        )
        NavigationBarItem(
            selected = currentTab == 1,
            onClick = { onSelectTab(1) },
            icon = { Icon(Icons.Default.Group, contentDescription = "Users") },
            label = { Text("Users") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryBlue, selectedTextColor = primaryBlue, unselectedIconColor = textGray, unselectedTextColor = textGray)
        )
        NavigationBarItem(
            selected = currentTab == 2,
            onClick = { onSelectTab(2) },
            icon = { Icon(Icons.AutoMirrored.Filled.MenuBook, contentDescription = "Courses") },
            label = { Text("Courses") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryBlue, selectedTextColor = primaryBlue, unselectedIconColor = textGray, unselectedTextColor = textGray)
        )
        NavigationBarItem(
            selected = currentTab == 3,
            onClick = { onSelectTab(3) },
            icon = { Icon(Icons.Default.VerifiedUser, contentDescription = "Content") },
            label = { Text("Content") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryBlue, selectedTextColor = primaryBlue, unselectedIconColor = textGray, unselectedTextColor = textGray)
        )
        NavigationBarItem(
            selected = currentTab == 4,
            onClick = { onSelectTab(4) },
            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Finance") },
            label = { Text("Finance") },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = primaryBlue, selectedTextColor = primaryBlue, unselectedIconColor = textGray, unselectedTextColor = textGray)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminOverviewScreen(
    onNavigateTab: (Int) -> Unit = {}
) {
    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        topBar = {
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
                    Box(
                        modifier = Modifier
                            .padding(start = 12.dp)
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
                },
                actions = {
                    IconButton(
                        onClick = {},
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
            // Good Morning Header & System Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Good morning, ...",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textDark
                    )
                    Text(
                        text = "Lead Campus Administrator",
                        fontSize = 12.sp,
                        color = textGray
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFE6F4EA)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF137333)))
                        Text(
                            text = "System Operational",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF137333)
                        )
                    }
                }
            }

            // Top Action Pills Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = lightBlueBg,
                    modifier = Modifier.clickable { onNavigateTab(1) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.PersonAdd, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                        Text("3 Tutor Approvals", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(primaryBlue))
                    }
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = Color(0xFFFEF2F2),
                    modifier = Modifier.clickable { onNavigateTab(3) }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Flag, null, tint = Color(0xFFDC2626), modifier = Modifier.size(14.dp))
                        Text("2 Flagged Notes", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFFDC2626)))
                    }
                }
            }

            // Stats 2x2 Grid (Total Users, Active Courses, No. Students, No. of Tutors)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // Total Users
                    AdminStatCard(
                        title = "Total Users",
                        value = "1,420",
                        badge = "+14%",
                        subtext = "840 std • 580 tut",
                        icon = Icons.Default.Group,
                        modifier = Modifier.weight(1f)
                    )

                    // Active Courses
                    AdminStatCard(
                        title = "Active Courses",
                        value = "48",
                        badge = "6 Facs",
                        subtext = "98.4% syllabus cov.",
                        subtextColor = Color(0xFF16A34A),
                        icon = Icons.Default.School,
                        modifier = Modifier.weight(1f)
                    )
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    // No. Students
                    AdminStatCard(
                        title = "No. Students",
                        value = "840",
                        badge = "Active",
                        badgeColor = primaryBlue,
                        subtext = "Enrolled learners",
                        icon = Icons.Default.Person,
                        modifier = Modifier.weight(1f)
                    )

                    // No. of Tutors
                    AdminStatCard(
                        title = "No. of Tutors",
                        value = "580",
                        badge = "Verified",
                        badgeColor = Color(0xFF16A34A),
                        subtext = "Peer mentors",
                        icon = Icons.Default.VerifiedUser,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Live Tutoring Radar Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.CellTower, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                    Text("Live Tutoring Radar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                }

                Surface(shape = RoundedCornerShape(50.dp), color = lightBlueBg) {
                    Text("2 active", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            // Live Radar Card 1
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFE0ECFF)), contentAlignment = Alignment.Center) {
                                Text("CP", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            }
                            Column {
                                Text("INF 212: Web Dev", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text("Chimwemwe P. ➔ Wongani N.", fontSize = 11.sp, color = textGray)
                            }
                        }
                        Surface(shape = RoundedCornerShape(50.dp), color = Color(0xFFE0ECFF)) {
                            Text("• Online", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryBlue, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("🎥 Google Meet Bridge active", fontSize = 11.sp, color = textGray)
                        Text("42 min elapsed", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
                    }
                }
            }

            // Live Radar Card 2
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFE0ECFF)), contentAlignment = Alignment.Center) {
                                Text("CP", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            }
                            Column {
                                Text("COM 311: Database", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text("Chisomo P. ➔ Davie G.", fontSize = 11.sp, color = textGray)
                            }
                        }
                        Surface(shape = RoundedCornerShape(50.dp), color = Color(0xFFF1F5F9)) {
                            Text("• In-Person", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = textGray, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("📍 Library Study Pod 3B", fontSize = 11.sp, color = textGray)
                        Text("18 min elapsed", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
                    }
                }
            }

            // Most In-Demand Modules
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Most In-Demand Modules", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                Text("This Semester", fontSize = 12.sp, color = textGray)
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DemandModuleItem(rank = "01", code = "INF 212", desc = "Web Systems & Cloud", demand = "+42% demand", primaryBlue = primaryBlue, textDark = textDark, textGray = textGray)
                DemandModuleItem(rank = "02", code = "STA 211", desc = "Probability & Inferences", demand = "+28% demand", primaryBlue = primaryBlue, textDark = textDark, textGray = textGray)
                DemandModuleItem(rank = "03", code = "COM 312", desc = "Operating Systems Architecture", demand = "+18% demand", primaryBlue = primaryBlue, textDark = textDark, textGray = textGray)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Export Analytics Button
            Button(
                onClick = {},
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.Download, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Export Analytics Report (PDF/CSV)", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminStatCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    badge: String = "",
    badgeColor: Color = Color(0xFF16A34A),
    subtext: String = "",
    subtextColor: Color = Color(0xFF64748B),
    valueColor: Color = Color(0xFF0F172A),
    icon: ImageVector
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = modifier.border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, fontSize = 11.sp, color = Color(0xFF64748B))
                Icon(icon, null, tint = Color(0xFF64748B), modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = valueColor)
                if (badge.isNotEmpty()) {
                    Text(badge, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = badgeColor)
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(subtext, fontSize = 11.sp, color = subtextColor)
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
    textGray: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = Color.White,
        modifier = modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(14.dp))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(primaryBlue),
                contentAlignment = Alignment.Center
            ) {
                Text(rank, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(code, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                Text(desc, fontSize = 11.sp, color = textGray)
            }

            Text(demand, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
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
