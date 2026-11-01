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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.components.YazaBottomBar
import com.example.yaza_wanga.ui.components.YazaWangaLogo
import com.example.yaza_wanga.ui.theme.*
import com.example.yaza_wanga.ui.viewmodel.FindTutorsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FindTutorsScreen(
    onNavigateHome: () -> Unit = {},
    onNavigateCourses: () -> Unit = {},
    onNavigateTutors: () -> Unit = {},
    onNavigateAlerts: () -> Unit = {},
    onNavigateProfile: () -> Unit = {},
    onViewCourseClick: (String) -> Unit = {},
    viewModel: FindTutorsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tutors = uiState.tutors
    val searchQuery = uiState.searchQuery
    val selectedMethod = uiState.selectedMethod

    val primaryBlue = Color(0xFF1D4ED8)
    val containerBg = Color(0xFFF8FAFC)
    val lightBlueBg = Color(0xFFF0F4FF)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    // Filter tutors by search query & method
    val filteredTutors = tutors.filter { tutor ->
        val matchesSearch = tutor.name.contains(searchQuery, ignoreCase = true) ||
                tutor.courses.any { it.contains(searchQuery, ignoreCase = true) }
        val matchesMethod = when (selectedMethod) {
            "Online" -> tutor.teachingMethod.contains("Online", ignoreCase = true) || tutor.teachingMethod.contains("Both", ignoreCase = true)
            "In-Person" -> tutor.teachingMethod.contains("In-Person", ignoreCase = true) || tutor.teachingMethod.contains("Offline", ignoreCase = true) || tutor.teachingMethod.contains("Both", ignoreCase = true)
            else -> true
        }
        matchesSearch && matchesMethod
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Find Tutors",
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
            YazaBottomBar(
                tabs = listOf(
                    Triple(0, "Home", Icons.Default.Home),
                    Triple(1, "Courses", Icons.AutoMirrored.Filled.MenuBook),
                    Triple(2, "Tutors", Icons.Default.School),
                    Triple(3, "Alerts", Icons.Default.Notifications),
                    Triple(4, "Menu", Icons.Default.Menu)
                ),
                currentTab = 2,
                onSelectTab = { index ->
                    when (index) {
                        0 -> onNavigateHome()
                        1 -> onNavigateCourses()
                        2 -> onNavigateTutors()
                        3 -> onNavigateAlerts()
                        4 -> onNavigateProfile()
                    }
                }
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
            // Header title
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text("Verified Peer Tutors", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textDark)
                Text("Search tutors by course or teaching method", fontSize = 12.sp, color = textGray)
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                placeholder = {
                    Text(
                        text = "Search tutors or course codes...",
                        color = textGray,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = textGray,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { viewModel.setSearchQuery("") }) {
                            Icon(Icons.Default.Close, null, tint = textGray)
                        }
                    }
                },
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

            // Method Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val methods = listOf("All", "Online", "In-Person")
                methods.forEach { method ->
                    val isSelected = selectedMethod == method
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (isSelected) primaryBlue else Color.White,
                        modifier = Modifier.clickable { viewModel.setSelectedMethod(method) },
                        border = BorderStroke(1.dp, if (isSelected) primaryBlue else Color(0xFFE2E8F0))
                    ) {
                        Text(
                            text = method,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.White else textDark,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        )
                    }
                }
            }

            // Tutors List Section
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            } else if (filteredTutors.isEmpty()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No tutors match your search.",
                            fontSize = 14.sp,
                            color = textGray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    filteredTutors.forEach { tutor ->
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = lightBlueBg,
                                            modifier = Modifier.size(44.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Text(
                                                    text = tutor.name.take(2).uppercase().ifEmpty { "T" },
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = primaryBlue
                                                )
                                            }
                                        }

                                        Column {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = tutor.name,
                                                    fontSize = 15.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = textDark
                                                )
                                                Icon(
                                                    imageVector = Icons.Default.Verified,
                                                    contentDescription = "Verified Tutor",
                                                    tint = primaryBlue,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                            Text(
                                                text = tutor.teachingMethod,
                                                fontSize = 12.sp,
                                                color = textGray
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (tutor.hourlyRate <= 0) Color(0xFFE6F4EA) else Color(0xFFFEF3C7)
                                    ) {
                                        Text(
                                            text = if (tutor.hourlyRate <= 0) "FREE" else "MK ${tutor.hourlyRate.toInt()}/session",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (tutor.hourlyRate <= 0) Color(0xFF137333) else Color(0xFFB45309),
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }

                                if (tutor.bio.isNotBlank()) {
                                    Text(
                                        text = tutor.bio,
                                        fontSize = 12.sp,
                                        color = textGray,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }

                                if (tutor.courses.isNotEmpty()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        tutor.courses.forEach { code ->
                                            Surface(
                                                shape = RoundedCornerShape(6.dp),
                                                color = Color(0xFFE0ECFF),
                                                modifier = Modifier.clickable { onViewCourseClick(code) }
                                            ) {
                                                Text(
                                                    text = code,
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = primaryBlue,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
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
fun FindTutorsPreview() {
    Yaza_WangaTheme {
        FindTutorsScreen()
    }
}
