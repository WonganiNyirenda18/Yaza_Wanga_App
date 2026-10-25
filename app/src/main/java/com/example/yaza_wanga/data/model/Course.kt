package com.example.yaza_wanga.data.model

data class Course(
    val code: String = "",
    val title: String = "",
    val department: String = "",
    val credits: Int = 0,
    val description: String = "",
    val semester: String = "",
    val prerequisites: List<String> = emptyList(),
    val status: String = "ACTIVE" // "ACTIVE" or "DRAFT"
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "code" to code,
        "title" to title,
        "department" to department,
        "credits" to credits,
        "description" to description,
        "semester" to semester,
        "prerequisites" to prerequisites,
        "status" to status
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): Course = Course(
            code = map["code"] as? String ?: "",
            title = map["title"] as? String ?: "",
            department = map["department"] as? String ?: "",
            credits = (map["credits"] as? Number)?.toInt() ?: 0,
            description = map["description"] as? String ?: "",
            semester = map["semester"] as? String ?: "",
            prerequisites = (map["prerequisites"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
            status = map["status"] as? String ?: "ACTIVE"
        )
    }
}
