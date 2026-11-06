package com.example.yaza_wanga.ui.screens

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.components.YazaEmptyState
import com.example.yaza_wanga.ui.components.YazaErrorBanner
import com.example.yaza_wanga.ui.theme.*
import com.example.yaza_wanga.ui.viewmodel.MaterialsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialsScreen(
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onNavigateCourses: () -> Unit = {},
    onMaterialClick: (String, String) -> Unit = { _, _ -> },
    viewModel: MaterialsViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val primaryBlue = Color(0xFF1D4ED8)
    val containerBg = Color(0xFFF8FAFC)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    // Filter materials by searchQuery, selectedCourse, and selectedCategory
    val filteredMaterials = uiState.materials.filter { item ->
        val query = uiState.searchQuery.trim()
        val m = item.material

        val matchesSearch = query.isEmpty() ||
                m.title.contains(query, ignoreCase = true) ||
                m.topicTag.contains(query, ignoreCase = true) ||
                m.description.contains(query, ignoreCase = true) ||
                item.tutorName.contains(query, ignoreCase = true)

        val matchesCourse = uiState.selectedCourse == "All" ||
                m.courseCode.equals(uiState.selectedCourse, ignoreCase = true)

        val matchesCategory = uiState.selectedCategory == "All" ||
                m.category.equals(uiState.selectedCategory, ignoreCase = true)

        matchesSearch && matchesCourse && matchesCategory
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Materials",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = textDark
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = onProfileClick,
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
            // Error Banner with Retry
            if (!uiState.errorMessage.isNullOrBlank()) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    YazaErrorBanner(errorMessage = uiState.errorMessage!!)
                    Button(
                        onClick = { viewModel.retry() },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                    ) {
                        Text("Retry", fontWeight = FontWeight.Bold, color = Color.White)
                    }
                }
            }

            if (!uiState.hasEnrollments) {
                YazaEmptyState(
                    message = "Enroll in a course to see its materials",
                    icon = Icons.Default.FolderOff,
                    actionButton = {
                        Button(
                            onClick = onNavigateCourses,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue)
                        ) {
                            Text("Browse Course Directory", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                )
            } else {
                // Search Field
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    placeholder = {
                        Text(
                            text = "Search materials or tutors...",
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
                        if (uiState.searchQuery.isNotEmpty()) {
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

                // Course Chips
                if (uiState.courses.size > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.courses.forEach { code ->
                            val selected = uiState.selectedCourse == code
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = if (selected) primaryBlue else Color.White,
                                modifier = Modifier.clickable { viewModel.setSelectedCourse(code) },
                                border = BorderStroke(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0))
                            ) {
                                Text(
                                    text = code,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (selected) Color.White else textDark,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Category Chips
                if (uiState.categories.size > 1) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.categories.forEach { cat ->
                            val selected = uiState.selectedCategory == cat
                            Surface(
                                shape = RoundedCornerShape(50.dp),
                                color = if (selected) primaryBlue else Color.White,
                                modifier = Modifier.clickable { viewModel.setSelectedCategory(cat) },
                                border = BorderStroke(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0))
                            ) {
                                Text(
                                    text = cat,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = if (selected) Color.White else textDark,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = primaryBlue)
                    }
                } else if (uiState.materials.isEmpty()) {
                    YazaEmptyState(
                        message = "No materials yet",
                        icon = Icons.Default.FolderOff,
                        actionButton = {
                            Text(
                                text = "Your tutors will upload materials for your courses",
                                fontSize = 12.sp,
                                color = textGray
                            )
                        }
                    )
                } else if (filteredMaterials.isEmpty()) {
                    YazaEmptyState(
                        message = "No materials match your search",
                        icon = Icons.Default.SearchOff
                    )
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        filteredMaterials.forEach { item ->
                            val m = item.material
                            val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(m.createdAt))

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onMaterialClick(m.courseCode, m.id) }
                                    .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFFE0ECFF)
                                        ) {
                                            Text(
                                                text = m.category,
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = primaryBlue,
                                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                            )
                                        }

                                        Text(
                                            text = m.courseCode,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = textGray
                                        )
                                    }

                                    Text(
                                        text = m.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textDark
                                    )

                                    if (m.topicTag.isNotBlank()) {
                                        Text(
                                            text = "#${m.topicTag}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = primaryBlue
                                        )
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Tutor: ${item.tutorName}",
                                            fontSize = 11.sp,
                                            color = textGray
                                        )

                                        Text(
                                            text = dateStr,
                                            fontSize = 11.sp,
                                            color = textGray
                                        )
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
fun MaterialsPreview() {
    Yaza_WangaTheme {
        MaterialsScreen()
    }
}
