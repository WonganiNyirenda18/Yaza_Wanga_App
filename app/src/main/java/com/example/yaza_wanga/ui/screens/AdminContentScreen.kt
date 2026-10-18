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
import androidx.compose.material.icons.automirrored.filled.ShowChart
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminContentScreen(
    onNavigateTab: (Int) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Flagged/Pending, 1: Recently Approved

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Content",
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
                HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
            }
        },
        bottomBar = {
            AdminBottomBar(currentTab = 3, onSelectTab = onNavigateTab)
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
            // Header & Review Required Pill
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("CURRICULUM GOVERNANCE", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Academic Content Moderation", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Audit, certify, or quarantine peer-contributed study artifacts and examination archives.", fontSize = 12.sp, color = textGray)
                }
            }

            // Policy Enforced Pill Bar
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = lightBlueBg,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.VerifiedUser, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                        Text("Academic integrity policy v2.4 enforced", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
                    }
                    Text("Guidelines", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue, modifier = Modifier.clickable {})
                }
            }

            // Filter Tabs
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { selectedTab = 0 },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedTab == 0) primaryBlue else Color.White),
                    border = BorderStroke(1.dp, if (selectedTab == 0) primaryBlue else Color(0xFFE2E8F0)),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text("🚩 Flagged / Pending (2)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedTab == 0) Color.White else primaryBlue)
                }

                Button(
                    onClick = { selectedTab = 1 },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedTab == 1) primaryBlue else Color.White),
                    border = BorderStroke(1.dp, if (selectedTab == 1) primaryBlue else Color(0xFFE2E8F0)),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text("Recently Approved (142)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedTab == 1) Color.White else textDark)
                }
            }

            // Syllabus Compliance Alert Banner
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = Color(0xFFFEF2F2),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(14.dp))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(modifier = Modifier.size(36.dp).clip(CircleShape).background(Color(0xFFDC2626)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.ReportProblem, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Syllabus Compliance Alert", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color(0xFF991B1B))
                        Text("2 documents require copyright & syllabus compliance review before public student download access is unlocked.", fontSize = 11.sp, color = Color(0xFF991B1B))
                    }
                }
            }

            // Queue for Triage Header
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Queue for Triage", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                Text("Ordered by Urgency", fontSize = 12.sp, color = textGray)
            }

            // Triage Item 1 (INF 212 Past Exam)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFFEE2E2)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PictureAsPdf, null, tint = Color(0xFFDC2626), modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE0ECFF)) {
                                    Text("INF 212", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryBlue, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                                Surface(shape = RoundedCornerShape(4.dp), color = containerBg) {
                                    Text("Final Exam Prep", fontSize = 10.sp, color = textGray, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("INF 212 - Past Exam 2023 with...", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                            Text("3.2 MB PDF • Uploaded 2h ago", fontSize = 11.sp, color = textGray)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(shape = RoundedCornerShape(8.dp), color = containerBg, modifier = Modifier.fillMaxWidth()) {
                        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Person, null, tint = textGray, modifier = Modifier.size(16.dp))
                                Text("John Banda (Peer Tutor)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = textDark)
                            }
                            Text("Record (14)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Flag Explanation Box
                    Surface(shape = RoundedCornerShape(10.dp), color = Color(0xFFFEF2F2), modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Warning, null, tint = Color(0xFFDC2626), modifier = Modifier.size(14.dp))
                                Text("Potential Copyright / Unreleased Official Exam Paper", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text("Flagged directly by Dr. Chimango (Faculty Coordinator). University department policy prohibits publishing untouched 2023 semester papers.", fontSize = 11.sp, color = Color(0xFF991B1B))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(onClick = {}, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = primaryBlue), modifier = Modifier.fillMaxWidth().height(40.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Approve Resource", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {}, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFDC2626)), modifier = Modifier.weight(1.2f).height(38.dp)) {
                            Text("Take Down / Quarantine", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Button(onClick = {}, shape = RoundedCornerShape(10.dp), colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg), modifier = Modifier.weight(0.8f).height(38.dp)) {
                            Text("Warn Uploader", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }
                    }
                }
            }

            // Recently Verified Section
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF16A34A), modifier = Modifier.size(16.dp))
                    Text("Recently Verified & Live Materials", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                }
                Text("View All (142)", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue, modifier = Modifier.clickable {})
            }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE0ECFF)), contentAlignment = Alignment.Center) {
                                Text("Σ", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            }
                            Column {
                                Text("MAT 201 - Calculus Summary ...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text("✔ Approved by Admin Grace • 📥 420 downloads", fontSize = 11.sp, color = Color(0xFF16A34A))
                            }
                        }
                        Icon(Icons.Default.MoreVert, null, tint = textGray)
                    }

                    HorizontalDivider(color = Color(0xFFE2E8F0))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color(0xFFE0ECFF)), contentAlignment = Alignment.Center) {
                                Icon(Icons.AutoMirrored.Filled.ShowChart, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                            }
                            Column {
                                Text("STA 211 - Probability Quick Form...", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Text("• Live • Peer Certified • 188 downloads", fontSize = 11.sp, color = Color(0xFF16A34A))
                            }
                        }
                        Icon(Icons.Default.MoreVert, null, tint = textGray)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1000)
@Composable
fun AdminContentPreview() {
    Yaza_WangaTheme {
        AdminContentScreen()
    }
}
