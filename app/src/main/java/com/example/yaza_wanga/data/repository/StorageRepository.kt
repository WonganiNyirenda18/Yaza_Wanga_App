package com.example.yaza_wanga.data.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.example.yaza_wanga.util.YazaLogger
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageMetadata
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Locale

sealed class UploadState {
    object Idle : UploadState()
    data class Progress(val percentage: Int) : UploadState()
    data class Success(val downloadUrl: String, val fileName: String, val mimeType: String, val fileSizeMb: Double) : UploadState()
    data class Error(val message: String) : UploadState()
}

data class SelectedFileInfo(
    val uri: Uri,
    val name: String,
    val sizeBytes: Long,
    val mimeType: String,
    val sizeMb: Double
)

open class StorageRepository(private val storage: FirebaseStorage? = null) {
    private val safeStorage get() = storage ?: try {
        FirebaseStorage.getInstance("gs://yaza-wanga.firebasestorage.app")
    } catch (_: Throwable) {
        try {
            FirebaseStorage.getInstance()
        } catch (_: Throwable) {
            null
        }
    }

    open fun getFileInfo(context: Context, uri: Uri): SelectedFileInfo? {
        return try {
            var name = "attachment"
            var size = 0L
            val mimeType = context.contentResolver.getType(uri) ?: "application/octet-stream"

            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                    if (nameIndex != -1) name = cursor.getString(nameIndex) ?: "attachment"
                    if (sizeIndex != -1) size = cursor.getLong(sizeIndex)
                }
            }

            if (size == 0L) {
                try {
                    context.contentResolver.openInputStream(uri)?.use { stream ->
                        size = stream.available().toLong()
                    }
                } catch (_: Exception) {}
            }

            val sizeMb = size / (1024.0 * 1024.0)
            SelectedFileInfo(uri = uri, name = name, sizeBytes = size, mimeType = mimeType, sizeMb = sizeMb)
        } catch (e: Exception) {
            YazaLogger.e("YazaData", "getFileInfo failed", e)
            null
        }
    }

    open fun uploadMaterialFile(
        context: Context,
        courseCode: String,
        materialId: String,
        fileInfo: SelectedFileInfo
    ): Flow<UploadState> = callbackFlow {
        YazaLogger.d("YazaData", "StorageRepository.uploadMaterialFile start: ${fileInfo.name}")
        val st = safeStorage
        if (st == null) {
            trySend(UploadState.Error("Could not connect to storage server. Check your connection."))
            close()
            return@callbackFlow
        }

        if (fileInfo.sizeMb > 50.0) {
            trySend(UploadState.Error("File size (${String.format(Locale.getDefault(), "%.1f", fileInfo.sizeMb)} MB) exceeds the 50 MB limit."))
            close()
            return@callbackFlow
        }

        try {
            val inputStream = context.contentResolver.openInputStream(fileInfo.uri)
            if (inputStream == null) {
                trySend(UploadState.Error("Selected file is unreadable or permission was denied."))
                close()
                return@callbackFlow
            }

            val bytes = inputStream.use { it.readBytes() }
            val cleanCourseCode = courseCode.replace("[^a-zA-Z0-9._-]".toRegex(), "_")
            val cleanFileName = fileInfo.name.replace("[^a-zA-Z0-9._-]".toRegex(), "_")
            val storagePath = "materials/$cleanCourseCode/${materialId}_$cleanFileName"
            val storageRef = st.reference.child(storagePath)

            val metadata = StorageMetadata.Builder()
                .setContentType(fileInfo.mimeType)
                .setCustomMetadata("originalName", fileInfo.name)
                .build()

            val uploadTask = storageRef.putBytes(bytes, metadata)

            uploadTask.addOnProgressListener { snapshot ->
                val progress = if (snapshot.totalByteCount > 0) {
                    ((100.0 * snapshot.bytesTransferred) / snapshot.totalByteCount).toInt()
                } else 0
                trySend(UploadState.Progress(progress.coerceIn(0, 99)))
            }.addOnSuccessListener { taskSnapshot ->
                val ref = taskSnapshot.storage
                ref.downloadUrl.addOnSuccessListener { uri ->
                    val url = uri.toString()
                    YazaLogger.d("YazaData", "StorageRepository upload success with downloadUrl: $url")
                    trySend(UploadState.Success(
                        downloadUrl = url,
                        fileName = fileInfo.name,
                        mimeType = fileInfo.mimeType,
                        fileSizeMb = fileInfo.sizeMb
                    ))
                    close()
                }.addOnFailureListener { err ->
                    val storageEx = err as? StorageException
                    val errorCode = storageEx?.errorCode ?: -1
                    val errorMessage = storageEx?.message ?: err.localizedMessage ?: "Unknown error"
                    YazaLogger.e("YazaData", "storage.downloadUrl failed. ErrorCode: $errorCode, Message: $errorMessage", err)

                    val userMsg = if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND || errorMessage.contains("does not exist", true)) {
                        "File storage is not set up for this project yet. Paste a Google Drive link instead."
                    } else {
                        "Failed to retrieve download link (Code $errorCode): $errorMessage"
                    }
                    trySend(UploadState.Error(userMsg))
                    close()
                }
            }.addOnFailureListener { err ->
                val storageEx = err as? StorageException
                val errorCode = storageEx?.errorCode ?: -1
                val errorMessage = storageEx?.message ?: err.localizedMessage ?: "Unknown error"
                YazaLogger.e("YazaData", "Upload failed. ErrorCode: $errorCode, Message: $errorMessage", err)

                val userMsg = if (errorCode == StorageException.ERROR_OBJECT_NOT_FOUND || errorMessage.contains("does not exist", true)) {
                    "File storage is not set up for this project yet. Paste a Google Drive link instead."
                } else if (errorCode == StorageException.ERROR_NOT_AUTHENTICATED || errorCode == StorageException.ERROR_NOT_AUTHORIZED) {
                    "Upload permission denied. Contact administrator."
                } else if (errorMessage.contains("network", true) || errorMessage.contains("host", true)) {
                    "Network connection unavailable. Check your internet and try again."
                } else {
                    "Failed to upload file: $errorMessage"
                }
                trySend(UploadState.Error(userMsg))
                close()
            }

            awaitClose {
                if (uploadTask.isInProgress) {
                    uploadTask.cancel()
                }
            }
        } catch (e: Exception) {
            YazaLogger.e("YazaData", "StorageRepository upload exception", e)
            trySend(UploadState.Error(e.localizedMessage ?: "Failed to read selected file."))
            close()
        }
    }
}
