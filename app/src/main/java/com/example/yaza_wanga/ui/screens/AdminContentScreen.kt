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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.example.yaza_wanga.ui.viewmodel.AdminContentViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminContentScreen(
    viewModel: AdminContentViewModel = viewModel(),
    onNavigateTab: (Int) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Flagged/Pending, 1: Recently Approved

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    LaunchedEffect(Unit) {
        viewModel.loadMaterials()
    }

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
                            onClick = { viewModel.loadMaterials() },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(primaryBlue)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
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
                    Text("🚩 Flagged / Pending", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedTab == 0) Color.White else primaryBlue)
                }

                Button(
                    onClick = { selectedTab = 1 },
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = if (selectedTab == 1) primaryBlue else Color.White),
                    border = BorderStroke(1.dp, if (selectedTab == 1) primaryBlue else Color(0xFFE2E8F0)),
                    modifier = Modifier.height(38.dp)
                ) {
                    Text("Live Materials (${uiState.materials.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedTab == 1) Color.White else textDark)
                }
            }

            // Live Materials List
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            } else if (uiState.materials.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
                    Text("No materials uploaded yet.", fontSize = 13.sp, color = textGray)
                }
            } else {
                uiState.materials.forEach { material ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                    ) {
                        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFE0ECFF)) {
                                    Text(material.courseCode, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryBlue, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                                Surface(shape = RoundedCornerShape(6.dp), color = containerBg) {
                                    Text(material.fileType, fontSize = 10.sp, color = textGray, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }

                            Text(material.title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark)
                            if (material.description.isNotBlank()) {
                                Text(material.description, fontSize = 12.sp, color = textGray, maxLines = 2)
                            }
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
fun AdminContentPreview() {
    Yaza_WangaTheme {
        AdminContentScreen()
    }
}
