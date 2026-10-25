package com.example.yaza_wanga.data.model

data class Session(
    val id: String = "",
    val tutorId: String = "",
    val courseCode: String = "",
    val title: String = "",
    val description: String = "",
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis() + 3600000,
    val capacity: Int = 10,
    val enrolledCount: Int = 0,
    val status: String = "SCHEDULED"
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "tutorId" to tutorId,
        "courseCode" to courseCode,
        "title" to title,
        "description" to description,
        "startTime" to startTime,
        "endTime" to endTime,
        "capacity" to capacity,
        "enrolledCount" to enrolledCount,
        "status" to status
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): Session = Session(
            id = map["id"] as? String ?: "",
            tutorId = map["tutorId"] as? String ?: "",
            courseCode = map["courseCode"] as? String ?: "",
            title = map["title"] as? String ?: "",
            description = map["description"] as? String ?: "",
            startTime = (map["startTime"] as? Number)?.toLong() ?: System.currentTimeMillis(),
            endTime = (map["endTime"] as? Number)?.toLong() ?: System.currentTimeMillis() + 3600000,
            capacity = (map["capacity"] as? Number)?.toInt() ?: 10,
            enrolledCount = (map["enrolledCount"] as? Number)?.toInt() ?: 0,
            status = map["status"] as? String ?: "SCHEDULED"
        )
    }
}
