package com.example.yaza_wanga.data.model

data class Session(
    val id: String = "",
    val tutorId: String = "",
    val studentId: String = "",
    val courseCode: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val method: String = "Online",
    val meetingLink: String = "",
    val venue: String = "",
    val status: String = "PENDING",
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "tutorId" to tutorId,
        "studentId" to studentId,
        "courseCode" to courseCode,
        "title" to title,
        "description" to description,
        "date" to date,
        "time" to time,
        "method" to method,
        "meetingLink" to meetingLink,
        "venue" to venue,
        "status" to status,
        "createdAt" to createdAt
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): Session = Session(
            id = map["id"] as? String ?: "",
            tutorId = map["tutorId"] as? String ?: "",
            studentId = map["studentId"] as? String ?: "",
            courseCode = map["courseCode"] as? String ?: "",
            title = map["title"] as? String ?: "",
            description = map["description"] as? String ?: "",
            date = map["date"] as? String ?: "",
            time = map["time"] as? String ?: "",
            method = map["method"] as? String ?: "Online",
            meetingLink = map["meetingLink"] as? String ?: "",
            venue = map["venue"] as? String ?: "",
            status = map["status"] as? String ?: "PENDING",
            createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
        )
    }
}
