package com.example.yaza_wanga.data.model

data class TutorProfile(
    val tutorId: String = "",
    val name: String = "",
    val bio: String = "",
    val hourlyRate: Double = 0.0,
    val courses: List<String> = emptyList(),
    val rating: Double = 5.0,
    val reviewCount: Int = 0,
    val status: String = "ACTIVE"
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "tutorId" to tutorId,
        "name" to name,
        "bio" to bio,
        "hourlyRate" to hourlyRate,
        "courses" to courses,
        "rating" to rating,
        "reviewCount" to reviewCount,
        "status" to status
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): TutorProfile = TutorProfile(
            tutorId = map["tutorId"] as? String ?: "",
            name = map["name"] as? String ?: "",
            bio = map["bio"] as? String ?: "",
            hourlyRate = (map["hourlyRate"] as? Number)?.toDouble() ?: 0.0,
            courses = (map["courses"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
            rating = (map["rating"] as? Number)?.toDouble() ?: 5.0,
            reviewCount = (map["reviewCount"] as? Number)?.toInt() ?: 0,
            status = map["status"] as? String ?: "ACTIVE"
        )
    }
}
