package com.example.yaza_wanga.data.model

data class Department(
    val id: String = "",
    val name: String = "",
    val faculty: String = "",
    val code: String = "",
    val description: String = ""
) {
    fun toMap(): Map<String, Any?> = mapOf(
        "id" to id,
        "name" to name,
        "faculty" to faculty,
        "code" to code,
        "description" to description
    )

    companion object {
        fun fromMap(map: Map<String, Any?>): Department = Department(
            id = map["id"] as? String ?: "",
            name = map["name"] as? String ?: "",
            faculty = map["faculty"] as? String ?: "",
            code = map["code"] as? String ?: "",
            description = map["description"] as? String ?: ""
        )
    }
}
