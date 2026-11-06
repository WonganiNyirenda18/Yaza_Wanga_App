package com.example.yaza_wanga.ui.screens

import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.*
import com.example.yaza_wanga.ui.viewmodel.AdminUsersViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersScreen(
    viewModel: AdminUsersViewModel = viewModel(),
    onNavigateTab: (Int) -> Unit = {}
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val users = uiState.users

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("ALL USERS") }

    val primaryBlue = Color(0xFF1D4ED8)
    val containerBg = Color(0xFFF8FAFC)
    val lightBlueBg = Color(0xFFF0F4FF)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    // Calculate Summary Stats
    val pendingCount = users.count { it.status == UserStatus.PENDING || (it.role == UserRole.TUTOR && it.status != UserStatus.ACTIVE) }
    val activeTutorsCount = users.count { it.role == UserRole.TUTOR && it.status == UserStatus.ACTIVE }

    // Filter Users
    val filteredUsers = users.filter { u ->
        val matchesSearch = searchQuery.isBlank() ||
                u.name.contains(searchQuery, ignoreCase = true) ||
                u.email.contains(searchQuery, ignoreCase = true)

        val matchesFilter = when (selectedFilter) {
            "STUDENTS" -> u.role == UserRole.STUDENT
            "TUTORS" -> u.role == UserRole.TUTOR
            "PENDING VETTING" -> u.status == UserStatus.PENDING || (u.role == UserRole.TUTOR && u.status != UserStatus.ACTIVE)
            "ACTIVE" -> u.status == UserStatus.ACTIVE
            "SUSPENDED" -> u.status == UserStatus.SUSPENDED
            else -> true
        }

        matchesSearch && matchesFilter
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "User Directory & Vetting",
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
            AdminBottomBar(currentTab = 1, onSelectTab = onNavigateTab)
        },
        containerColor = Color(0xFFF8FAFC)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column {
                Surface(shape = RoundedCornerShape(50.dp), color = lightBlueBg, modifier = Modifier.padding(bottom = 6.dp)) {
                    Text("PORTFOLIO VETTING • SECURITY AUDIT", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = primaryBlue, modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp))
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text("User Directory & Verification", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
                Spacer(modifier = Modifier.height(2.dp))
                Text("Review credential portfolios, triage verification pipelines, and manage institutional standing.", fontSize = 12.sp, color = textGray)
            }

            // Stats Cards Row (Equal Size & Single Line)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = lightBlueBg,
                    modifier = Modifier.weight(1f).border(1.dp, Color(0xFFDBEAFE), RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFDBEAFE)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PersonSearch, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Pending Vetting", fontSize = 11.sp, color = textGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("$pendingCount In Queue", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFE6F4EA),
                    modifier = Modifier.weight(1f).border(1.dp, Color(0xFFC6F6D5), RoundedCornerShape(16.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp).fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFC6F6D5)), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Verified, null, tint = Color(0xFF137333), modifier = Modifier.size(18.dp))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Active Tutors", fontSize = 11.sp, color = textGray, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Text("$activeTutorsCount Verified", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textDark, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
                modifier = Modifier.fillMaxWidth().height(52.dp)
            )

            // Filter Chips Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val filters = listOf("ALL USERS", "PENDING VETTING", "TUTORS", "STUDENTS", "ACTIVE", "SUSPENDED")
                filters.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (isSelected) primaryBlue else Color.White,
                        modifier = Modifier.clickable { selectedFilter = filter },
                        border = BorderStroke(1.dp, if (isSelected) primaryBlue else Color(0xFFE2E8F0))
                    ) {
                        Text(
                            text = filter,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else textDark,
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            // Users List
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            } else if (filteredUsers.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Box(modifier = Modifier.fillMaxWidth().padding(32.dp), contentAlignment = Alignment.Center) {
                        Text("No users match the selected criteria.", fontSize = 13.sp, color = textGray)
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    filteredUsers.forEach { user ->
                        AdminUserCard(
                            user = user,
                            onStatusChange = { newStatus ->
                                viewModel.updateUserStatus(user.uid, newStatus) {
                                    Toast.makeText(context, "Status updated to ${newStatus.name}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AdminUserCard(
    user: User,
    onStatusChange: (UserStatus) -> Unit
) {
    val primaryBlue = Color(0xFF1D4ED8)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(shape = CircleShape, color = Color(0xFFE0ECFF), modifier = Modifier.size(40.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(user.name.take(2).uppercase().ifEmpty { "U" }, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                        }
                    }

                    Column {
                        Text(user.name.ifBlank { user.email.substringBefore("@") }, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = textDark)
                        Text(user.email, fontSize = 12.sp, color = textGray)
                    }
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (user.role) {
                        UserRole.ADMIN -> Color(0xFFF3E8FF)
                        UserRole.TUTOR -> Color(0xFFFEF3C7)
                        UserRole.STUDENT -> Color(0xFFE0ECFF)
                    }
                ) {
                    Text(
                        text = user.role.name,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (user.role) {
                            UserRole.ADMIN -> Color(0xFF7C3AED)
                            UserRole.TUTOR -> Color(0xFFB45309)
                            UserRole.STUDENT -> primaryBlue
                        },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Status:", fontSize = 12.sp, color = textGray)
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = when (user.status) {
                            UserStatus.ACTIVE -> Color(0xFFE6F4EA)
                            UserStatus.PENDING -> Color(0xFFFEF3C7)
                            UserStatus.SUSPENDED -> Color(0xFFFEF2F2)
                            UserStatus.REJECTED -> Color(0xFFFEF2F2)
                        }
                    ) {
                        Text(
                            text = user.status.name,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (user.status) {
                                UserStatus.ACTIVE -> Color(0xFF137333)
                                UserStatus.PENDING -> Color(0xFFB45309)
                                UserStatus.SUSPENDED -> Color(0xFFDC2626)
                                UserStatus.REJECTED -> Color(0xFFDC2626)
                            },
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    if (user.status != UserStatus.ACTIVE) {
                        Button(
                            onClick = { onStatusChange(UserStatus.ACTIVE) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF137333)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Approve / Activate", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }

                    if (user.status != UserStatus.SUSPENDED && user.role != UserRole.ADMIN) {
                        OutlinedButton(
                            onClick = { onStatusChange(UserStatus.SUSPENDED) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDC2626)),
                            border = BorderStroke(1.dp, Color(0xFFDC2626)),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text("Suspend", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFDC2626))
                        }
                    }
                }
            }
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
