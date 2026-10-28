package com.example.yaza_wanga.data.model

data class Material(
    val id: String = "",
    val courseCode: String = "",
    val tutorId: String = "",
    val title: String = "",
    val category: String = "Lecture Notes",
    val topicTag: String = "",
    val description: String = "",
    val fileUrl: String = "",
    val access: String = "FREE",
    val status: String = "APPROVED",
    val warnedAt: Long? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "courseCode" to courseCode,
        "tutorId" to tutorId,
        "title" to title,
        "category" to category,
        "topicTag" to topicTag,
        "description" to description,
        "fileUrl" to fileUrl,
        "access" to access,
        "status" to status,
        "warnedAt" to warnedAt,
        "createdAt" to createdAt
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): Material = Material(
            id = map["id"] as? String ?: "",
            courseCode = map["courseCode"] as? String ?: "",
            tutorId = map["tutorId"] as? String ?: "",
            title = map["title"] as? String ?: "",
            category = map["category"] as? String ?: "Lecture Notes",
            topicTag = map["topicTag"] as? String ?: "",
            description = map["description"] as? String ?: "",
            fileUrl = map["fileUrl"] as? String ?: "",
            access = map["access"] as? String ?: "FREE",
            status = map["status"] as? String ?: "APPROVED",
            warnedAt = (map["warnedAt"] as? Number)?.toLong(),
            createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
        )
    }
}
