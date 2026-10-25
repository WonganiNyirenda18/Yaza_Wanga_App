package com.example.yaza_wanga.data.model

data class Material(
    val id: String = "",
    val courseCode: String = "",
    val title: String = "",
    val description: String = "",
    val fileUrl: String = "",
    val fileType: String = "PDF",
    val uploadedBy: String = "",
    val uploadedAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "courseCode" to courseCode,
        "title" to title,
        "description" to description,
        "fileUrl" to fileUrl,
        "fileType" to fileType,
        "uploadedBy" to uploadedBy,
        "uploadedAt" to uploadedAt
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): Material = Material(
            id = map["id"] as? String ?: "",
            courseCode = map["courseCode"] as? String ?: "",
            title = map["title"] as? String ?: "",
            description = map["description"] as? String ?: "",
            fileUrl = map["fileUrl"] as? String ?: "",
            fileType = map["fileType"] as? String ?: "PDF",
            uploadedBy = map["uploadedBy"] as? String ?: "",
            uploadedAt = (map["uploadedAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
        )
    }
}
