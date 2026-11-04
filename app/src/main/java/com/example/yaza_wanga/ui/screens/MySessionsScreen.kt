package com.example.yaza_wanga.ui.screens

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.components.YazaEmptyState
import com.example.yaza_wanga.ui.components.YazaErrorBanner
import com.example.yaza_wanga.ui.components.YazaStatusChip
import com.example.yaza_wanga.ui.theme.*
import com.example.yaza_wanga.ui.viewmodel.MySessionsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MySessionsScreen(
    onBackClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    viewModel: MySessionsViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Upcoming, 1: Past

    val primaryBlue = Color(0xFF1D4ED8)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    val currentList = if (selectedTab == 0) uiState.upcomingSessions else uiState.pastSessions

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "My Sessions",
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
            // Error Banner
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

            // Tab Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val tabs = listOf("Upcoming (${uiState.upcomingSessions.size})", "Past (${uiState.pastSessions.size})")
                tabs.forEachIndexed { idx, title ->
                    val selected = selectedTab == idx
                    Surface(
                        shape = RoundedCornerShape(50.dp),
                        color = if (selected) primaryBlue else Color.White,
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = idx },
                        border = BorderStroke(1.dp, if (selected) primaryBlue else Color(0xFFE2E8F0))
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 10.dp)) {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (selected) Color.White else textDark
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
            } else if (currentList.isEmpty()) {
                if (selectedTab == 0) {
                    YazaEmptyState(
                        message = "No upcoming sessions yet",
                        icon = Icons.Default.Event,
                        actionButton = {
                            Text(
                                text = "Your tutors will schedule sessions for your courses",
                                fontSize = 12.sp,
                                color = textGray
                            )
                        }
                    )
                } else {
                    YazaEmptyState(
                        message = "No past sessions",
                        icon = Icons.Default.History
                    )
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    currentList.forEach { item ->
                        val session = item.session
                        val tutorName = item.tutorName
                        val chipText = when (session.status.uppercase()) {
                            "SCHEDULED" -> "Scheduled"
                            "ACCEPTED" -> "Accepted"
                            "COMPLETED" -> "Completed"
                            else -> session.status
                        }

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
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFE0ECFF)
                                    ) {
                                        Text(
                                            text = session.courseCode,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = primaryBlue,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }

                                    YazaStatusChip(status = chipText)
                                }

                                Text(
                                    text = session.title.ifBlank { "Tutoring Session" },
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textDark
                                )

                                Text(
                                    text = "Tutor: $tutorName",
                                    fontSize = 12.sp,
                                    color = textGray
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(Icons.Default.CalendarToday, null, tint = textGray, modifier = Modifier.size(14.dp))
                                        Text("${session.date} • ${session.time}", fontSize = 12.sp, color = textDark)
                                    }

                                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Icon(Icons.Default.Videocam, null, tint = textGray, modifier = Modifier.size(14.dp))
                                        Text(session.method, fontSize = 12.sp, color = textDark)
                                    }
                                }

                                val link = session.meetingLink.trim()
                                val isOnline = session.method.contains("Online", ignoreCase = true)

                                if (isOnline) {
                                    if (link.startsWith("http://") || link.startsWith("https://")) {
                                        Button(
                                            onClick = {
                                                try {
                                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
                                                    context.startActivity(intent)
                                                } catch (_: ActivityNotFoundException) {
                                                    Toast.makeText(context, "This link cannot be opened", Toast.LENGTH_SHORT).show()
                                                } catch (_: Exception) {
                                                    Toast.makeText(context, "This link cannot be opened", Toast.LENGTH_SHORT).show()
                                                }
                                            },
                                            shape = RoundedCornerShape(10.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                                            modifier = Modifier.fillMaxWidth().height(38.dp)
                                        ) {
                                            Icon(Icons.Default.Link, null, tint = Color.White, modifier = Modifier.size(14.dp))
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Text("Join", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                                        }
                                    } else {
                                        Text(
                                            text = "Link will be added by the tutor",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = textGray
                                        )
                                    }
                                } else if (session.venue.isNotBlank()) {
                                    Text(
                                        text = "Venue: ${session.venue}",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = primaryBlue
                                    )
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
fun MySessionsPreview() {
    Yaza_WangaTheme {
        MySessionsScreen()
    }
}
