package com.example.yaza_wanga.data.model

data class Enrollment(
    val id: String = "",
    val studentId: String = "",
    val courseCode: String = "",
    val enrolledAt: Long = System.currentTimeMillis(),
    val status: String = "ACTIVE"
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "studentId" to studentId,
        "courseCode" to courseCode,
        "enrolledAt" to enrolledAt,
        "status" to status
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): Enrollment = Enrollment(
            id = map["id"] as? String ?: "",
            studentId = map["studentId"] as? String ?: "",
            courseCode = map["courseCode"] as? String ?: "",
            enrolledAt = (map["enrolledAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
            status = map["status"] as? String ?: "ACTIVE"
        )
    }
}
