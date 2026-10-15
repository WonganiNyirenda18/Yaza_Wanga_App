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
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    onNavigateTab: (Int) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableIntStateOf(0) }

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
                            text = "YAZA WANGA",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = primaryBlue,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = "Users",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
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
                            imageVector = Icons.Default.School,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                actions = {
                    Box(modifier = Modifier.padding(end = 4.dp)) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = "Alerts",
                                tint = textDark
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(Color.Red)
                                .align(Alignment.TopEnd)
                                .offset(x = (-8).dp, y = 8.dp)
                        )
                    }
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
            AdminBottomBar(currentTab = 1, onSelectTab = onNavigateTab)
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
            // Header & Total Users Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(primaryBlue))
                        Text("ACADEMIC GOVERNANCE", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                    }
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("User Directory & Verification", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
                    Spacer(modifier = Modifier.height(2.dp))
                    Text("Review credential portfolios, triage verification pipelines, and manage institutional standing.", fontSize = 12.sp, color = textGray)
                }
            }

            // Stats Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Pending Vetting Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = lightBlueBg,
                    modifier = Modifier.weight(1f).border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFDBEAFE)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PersonSearch, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Pending Vetting", fontSize = 10.sp, color = textGray)
                            Text("3 In Queue", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }
                    }
                }

                // Active Peer Tutors Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE6F4EA),
                    modifier = Modifier.weight(1f).border(1.dp, Color(0xFFC6F6D5), RoundedCornerShape(16.dp))
                ) {
                    Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFC6F6D5)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Verified, null, tint = Color(0xFF137333), modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Active Peer Tutors", fontSize = 10.sp, color = textGray)
                            Text("580 Verified", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search student or tutor by name, email, or...", color = textGray, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = textGray, modifier = Modifier.size(20.dp)) },
                trailingIcon = { if (searchQuery.isNotEmpty()) IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Close, null, tint = textGray) } },
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

            // Filter Chips
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = if (selectedFilter == 0) primaryBlue else Color.White,
                    modifier = Modifier.clickable { selectedFilter = 0 },
                    border = BorderStroke(1.dp, if (selectedFilter == 0) primaryBlue else Color(0xFFE2E8F0))
                ) {
                    Text("All Users (1,420)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedFilter == 0) Color.White else textDark, modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp))
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = if (selectedFilter == 1) Color(0xFFDC2626) else Color.White,
                    modifier = Modifier.clickable { selectedFilter = 1 },
                    border = BorderStroke(1.dp, if (selectedFilter == 1) Color(0xFFDC2626) else Color(0xFFE2E8F0))
                ) {
                    Text("Pending Verification (3)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedFilter == 1) Color.White else Color(0xFFDC2626), modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp))
                }
            }

            // Priority Verification Queue Header
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(Icons.Default.HourglassTop, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                    Text("Priority Verification Queue", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                }
                Surface(shape = RoundedCornerShape(50.dp), color = Color(0xFFFEF2F2)) {
                    Text("Requires Attention", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626), modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                }
            }

            // Verification Queue Item 1
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.size(44.dp).clip(CircleShape).background(Color(0xFFE2E8F0)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, null, tint = textGray)
                            }
                            Column {
                                Text("Chimwemwe Chirwa", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                                Surface(shape = RoundedCornerShape(4.dp), color = Color(0xFFE0ECFF)) {
                                    Text("Tutor Applicant", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryBlue, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                Text("c.chirwa@poly.ac.mw", fontSize = 11.sp, color = textGray)
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("3.92", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                            Text("Grade A Verified", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color(0xFF16A34A))
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(shape = RoundedCornerShape(10.dp), color = containerBg, modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("MAT 201: Calculus & Linear Algebra • Semester 3", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = textDark)
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.PictureAsPdf, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                                Text("Official_Transcript_Sem3.pdf", fontSize = 11.sp, color = primaryBlue, fontWeight = FontWeight.Bold, modifier = Modifier.clickable {})
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                            modifier = Modifier.weight(1.2f).height(38.dp)
                        ) {
                            Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Approve Tutor", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg),
                            modifier = Modifier.weight(0.8f).height(38.dp)
                        ) {
                            Text("Review", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }

                        Button(
                            onClick = {},
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                            modifier = Modifier.weight(0.8f).height(38.dp)
                        ) {
                            Text("Reject", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                        }
                    }
                }
            }

            // Active Directory Header
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Active Directory", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textDark)
                Text("Sort by Activity ▾", fontSize = 12.sp, color = primaryBlue, modifier = Modifier.clickable {})
            }

            // Active Directory Item 1 (John Banda)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFE2E8F0)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, null, tint = textGray)
                            }
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text("John Banda", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                                    Icon(Icons.Default.Verified, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                                }
                                Text("INF 212 • STA 211 Master Tutor", fontSize = 11.sp, color = textGray)
                            }
                        }
                        Surface(shape = RoundedCornerShape(50.dp), color = Color(0xFFFEF3C7)) {
                            Text("⭐ 4.8", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD97706), modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = {}, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg), modifier = Modifier.weight(1f).height(36.dp)) {
                            Text("Profile", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }
                        Button(onClick = {}, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg), modifier = Modifier.weight(1f).height(36.dp)) {
                            Text("Roles", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }
                        Button(onClick = {}, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)), modifier = Modifier.weight(1f).height(36.dp)) {
                            Text("Suspend", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                        }
                    }
                }
            }

            // Bottom Actions Row
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button(onClick = {}, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = primaryBlue), modifier = Modifier.weight(1f).height(46.dp)) {
                    Icon(Icons.Default.PersonAdd, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Invite Faculty", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                Button(onClick = {}, shape = RoundedCornerShape(12.dp), colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg), modifier = Modifier.weight(1f).height(46.dp)) {
                    Icon(Icons.Default.Download, null, tint = primaryBlue, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Export Registry", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true, widthDp = 392, heightDp = 1000)
@Composable
fun AdminUsersPreview() {
    Yaza_WangaTheme {
        AdminUsersScreen()
    }
}
