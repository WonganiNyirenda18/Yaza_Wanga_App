package com.example.yaza_wanga.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.data.model.UserStatus
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme
import com.example.yaza_wanga.ui.viewmodel.AdminUsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    viewModel: AdminUsersViewModel = viewModel(),
    onNavigateTab: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableIntStateOf(0) } // 0: All, 1: Pending

    val primaryBlue = Color(0xFF1D4ED8)
    val lightBlueBg = Color(0xFFF0F4FF)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    val query = searchQuery.trim()
    val filteredUsers = uiState.users.filter { user ->
        val matchesSearch = query.isEmpty() ||
            user.name.contains(query, ignoreCase = true) ||
            user.email.contains(query, ignoreCase = true)
        val matchesFilter = if (selectedFilter == 1) user.status == UserStatus.PENDING else true
        matchesSearch && matchesFilter
    }

    val pendingCount = uiState.users.count { it.status == UserStatus.PENDING }
    val activeTutorsCount = uiState.users.count { it.role.name == "TUTOR" && it.status == UserStatus.ACTIVE }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Users & Vetting",
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
                            onClick = { viewModel.loadUsers() },
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
            // Header Description
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

            // Stats Cards Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = lightBlueBg,
                    modifier = Modifier.weight(1f).border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFDBEAFE)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PersonSearch, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Pending Vetting", fontSize = 11.sp, color = textGray)
                            Text("$pendingCount In Queue", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE6F4EA),
                    modifier = Modifier.weight(1f).border(1.dp, Color(0xFFC6F6D5), RoundedCornerShape(16.dp))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFC6F6D5)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Verified, null, tint = Color(0xFF137333), modifier = Modifier.size(18.dp))
                        }
                        Column {
                            Text("Active Tutors", fontSize = 11.sp, color = textGray)
                            Text("$activeTutorsCount Verified", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                        }
                    }
                }
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search student or tutor by name or email", color = textGray, fontSize = 13.sp) },
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
                    Text("All Users (${uiState.users.size})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedFilter == 0) Color.White else textDark, modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp))
                }

                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = if (selectedFilter == 1) Color(0xFFDC2626) else Color.White,
                    modifier = Modifier.clickable { selectedFilter = 1 },
                    border = BorderStroke(1.dp, if (selectedFilter == 1) Color(0xFFDC2626) else Color(0xFFE2E8F0))
                ) {
                    Text("Pending Verification ($pendingCount)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (selectedFilter == 1) Color.White else Color(0xFFDC2626), modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp))
                }
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            } else if (filteredUsers.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 32.dp), contentAlignment = Alignment.Center) {
                    Text("No users found matching query or filter.", fontSize = 13.sp, color = textGray)
                }
            } else {
                filteredUsers.forEach { user ->
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
                                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                            Text(user.name.ifBlank { "User" }, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                                            if (user.status == UserStatus.ACTIVE) {
                                                Icon(Icons.Default.Verified, null, tint = primaryBlue, modifier = Modifier.size(14.dp))
                                            }
                                        }
                                        Surface(shape = RoundedCornerShape(4.dp), color = if (user.status == UserStatus.PENDING) Color(0xFFFEF3C7) else Color(0xFFE0ECFF)) {
                                            Text("${user.role.name} • ${user.status.name}", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (user.status == UserStatus.PENDING) Color(0xFFB45309) else primaryBlue, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                        }
                                        Text(user.email, fontSize = 11.sp, color = textGray)
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (user.status != UserStatus.ACTIVE) {
                                    Button(
                                        onClick = {
                                            viewModel.updateUserStatus(user.uid, UserStatus.ACTIVE) {
                                                Toast.makeText(context, "Approved ${user.name}", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                                        modifier = Modifier.weight(1f).height(38.dp)
                                    ) {
                                        Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Approve", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                    }
                                }

                                if (user.status != UserStatus.REJECTED) {
                                    Button(
                                        onClick = {
                                            viewModel.updateUserStatus(user.uid, UserStatus.REJECTED) {
                                                Toast.makeText(context, "Rejected ${user.name}", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEE2E2)),
                                        modifier = Modifier.weight(1f).height(38.dp)
                                    ) {
                                        Text("Reject", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                                    }
                                }

                                if (user.status != UserStatus.SUSPENDED) {
                                    Button(
                                        onClick = {
                                            viewModel.updateUserStatus(user.uid, UserStatus.SUSPENDED) {
                                                Toast.makeText(context, "Suspended ${user.name}", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        shape = RoundedCornerShape(10.dp),
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFEF2F2)),
                                        modifier = Modifier.weight(1f).height(38.dp)
                                    ) {
                                        Text("Suspend", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                                    }
                                }
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
fun AdminUsersPreview() {
    Yaza_WangaTheme {
        AdminUsersScreen()
    }
}
