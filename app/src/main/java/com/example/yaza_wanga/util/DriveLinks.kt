package com.example.yaza_wanga.util

object DriveLinks {
    fun isDriveLink(url: String): Boolean {
        val lower = url.lowercase().trim()
        return lower.contains("drive.google.com") || lower.contains("docs.google.com")
    }

    fun extractDriveFileId(url: String): String? {
        val trimmed = url.trim()
        if (trimmed.isBlank()) return null

        val fileRegex = Regex("drive\\.google\\.com/file/d/([a-zA-Z0-9_-]+)")
        val fileMatch = fileRegex.find(trimmed)
        if (fileMatch != null && fileMatch.groupValues.size > 1) {
            return fileMatch.groupValues[1]
        }

        val idRegex = Regex("[?&]id=([a-zA-Z0-9_-]+)")
        val idMatch = idRegex.find(trimmed)
        if (idMatch != null && idMatch.groupValues.size > 1) {
            return idMatch.groupValues[1]
        }

        return null
    }

    fun normalizeDriveLink(url: String): String {
        val trimmed = url.trim()
        if (!isDriveLink(trimmed)) return trimmed
        val fileId = extractDriveFileId(trimmed)
        return if (fileId != null) {
            "https://drive.google.com/uc?export=download&id=$fileId"
        } else {
            trimmed
        }
    }

    fun isValidMaterialLink(url: String): Boolean {
        val trimmed = url.trim()
        return trimmed.startsWith("http://", ignoreCase = true) || trimmed.startsWith("https://", ignoreCase = true)
    }
}
