package com.example.yaza_wanga.ui.screens

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaza_wanga.ui.components.YazaErrorBanner
import com.example.yaza_wanga.ui.theme.*
import com.example.yaza_wanga.ui.viewmodel.MaterialDetailsViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterialDetailsScreen(
    courseCode: String,
    materialId: String,
    onBackClick: () -> Unit = {},
    viewModel: MaterialDetailsViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val primaryBlue = Color(0xFF1D4ED8)
    val textDark = Color(0xFF0F172A)
    val textGray = Color(0xFF64748B)

    LaunchedEffect(courseCode, materialId) {
        viewModel.loadMaterial(courseCode, materialId)
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = "Material Details",
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
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = primaryBlue)
                }
            } else if (!uiState.errorMessage.isNullOrBlank()) {
                YazaErrorBanner(errorMessage = uiState.errorMessage!!)
            } else if (uiState.isNotEnrolled) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFFCA5A5), RoundedCornerShape(16.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Enroll in this course to access its materials",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFDC2626)
                        )
                    }
                }
            } else if (uiState.isNotAvailable || uiState.materialItem == null) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "This material is no longer available",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = textGray
                        )
                    }
                }
            } else {
                val item = uiState.materialItem!!
                val m = item.material
                val dateStr = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault()).format(Date(m.createdAt))

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(20.dp))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
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
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = primaryBlue,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }

                            Text(
                                text = m.courseCode,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = textGray
                            )
                        }

                        Text(
                            text = m.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = textDark
                        )

                        if (m.topicTag.isNotBlank()) {
                            Text(
                                text = "#${m.topicTag}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = primaryBlue
                            )
                        }

                        if (m.description.isNotBlank()) {
                            Text(
                                text = m.description,
                                fontSize = 13.sp,
                                color = textDark
                            )
                        }

                        HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Uploaded by ${item.tutorName}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = textGray
                            )

                            Text(
                                text = dateStr,
                                fontSize = 12.sp,
                                color = textGray
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action Buttons
                val url = m.fileUrl.trim()

                Button(
                    onClick = {
                        if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            Toast.makeText(context, "This link cannot be opened", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        try {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        } catch (_: ActivityNotFoundException) {
                            Toast.makeText(context, "This link cannot be opened", Toast.LENGTH_SHORT).show()
                        } catch (_: Exception) {
                            Toast.makeText(context, "This link cannot be opened", Toast.LENGTH_SHORT).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.OpenInNew, null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Open material", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                }

                OutlinedButton(
                    onClick = {
                        if (!isDownloadableFileUrl(url)) {
                            Toast.makeText(context, "This link cannot be downloaded, use Open instead", Toast.LENGTH_LONG).show()
                            return@OutlinedButton
                        }

                        try {
                            val request = DownloadManager.Request(Uri.parse(url))
                            val uriPath = Uri.parse(url).path ?: ""
                            val rawFileName = uriPath.substringAfterLast('/')
                            val fileName = if (rawFileName.contains('.')) rawFileName else "${m.title.replace(" ", "_")}.pdf"

                            request.setTitle(m.title)
                            request.setDescription("Downloading study material...")
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                            request.setDestinationInExternalFilesDir(context, Environment.DIRECTORY_DOWNLOADS, fileName)

                            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            downloadManager.enqueue(request)

                            Toast.makeText(context, "Download started", Toast.LENGTH_SHORT).show()
                        } catch (_: Exception) {
                            Toast.makeText(context, "This link cannot be downloaded, use Open instead", Toast.LENGTH_LONG).show()
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryBlue),
                    border = BorderStroke(1.dp, primaryBlue),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Icon(Icons.Default.Download, null, tint = primaryBlue, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Download", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = primaryBlue)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private fun isDownloadableFileUrl(url: String): Boolean {
    val lower = url.lowercase().trim()
    if (!lower.startsWith("http://") && !lower.startsWith("https://")) return false
    if (lower.contains("youtube.com") || lower.contains("youtu.be") || lower.contains("drive.google.com")) {
        return false
    }
    val path = try { Uri.parse(url).path ?: "" } catch (_: Exception) { "" }
    val extension = path.substringAfterLast('.', "")
    val fileExtensions = listOf("pdf", "doc", "docx", "ppt", "pptx", "xls", "xlsx", "zip", "rar", "txt", "png", "jpg", "jpeg")
    return fileExtensions.contains(extension.lowercase())
}

@Preview(showBackground = true, widthDp = 392, heightDp = 800)
@Composable
fun MaterialDetailsPreview() {
    Yaza_WangaTheme {
        MaterialDetailsScreen(courseCode = "INF 212", materialId = "m1")
    }
}
