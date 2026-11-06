package com.example.yaza_wanga.data.model

data class Progress(
    val id: String = "",
    val studentId: String = "",
    val courseCode: String = "",
    val completedTopics: List<String> = emptyList(),
    val viewedMaterialIds: List<String> = emptyList(),
    val attendedSessionIds: List<String> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "studentId" to studentId,
        "courseCode" to courseCode,
        "completedTopics" to completedTopics,
        "viewedMaterialIds" to viewedMaterialIds,
        "attendedSessionIds" to attendedSessionIds,
        "updatedAt" to updatedAt
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): Progress = Progress(
            id = map["id"] as? String ?: "",
            studentId = map["studentId"] as? String ?: "",
            courseCode = map["courseCode"] as? String ?: "",
            completedTopics = (map["completedTopics"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
            viewedMaterialIds = (map["viewedMaterialIds"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
            attendedSessionIds = (map["attendedSessionIds"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
            updatedAt = (map["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
        )
    }
}
