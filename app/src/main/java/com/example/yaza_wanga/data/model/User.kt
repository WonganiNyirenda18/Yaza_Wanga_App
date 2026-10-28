package com.example.yaza_wanga.data.model

enum class UserRole {
    STUDENT, TUTOR, ADMIN;

    companion object {
        fun fromString(value: String?): UserRole {
            return try {
                valueOf(value?.uppercase() ?: "STUDENT")
            } catch (_: Exception) {
                STUDENT
            }
        }
    }
}

enum class UserStatus {
    ACTIVE, PENDING, SUSPENDED, REJECTED;

    companion object {
        fun fromString(value: String?): UserStatus {
            return try {
                valueOf(value?.uppercase() ?: "ACTIVE")
            } catch (_: Exception) {
                ACTIVE
            }
        }
    }
}

data class User(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val role: UserRole = UserRole.STUDENT,
    val status: UserStatus = UserStatus.ACTIVE,
    val faculty: String? = null,
    val studentId: String? = null,
    val department: String? = null,
    val programme: String? = null,
    val yearOfStudy: String? = null,
    val phone: String? = null,
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "email" to email,
            "name" to name,
            "role" to role.name,
            "status" to status.name,
            "faculty" to faculty,
            "studentId" to studentId,
            "department" to department,
            "programme" to programme,
            "yearOfStudy" to yearOfStudy,
            "phone" to phone,
            "createdAt" to createdAt
        )
    }

    companion object {
        fun fromMap(map: Map<String, Any?>): User {
            return User(
                uid = map["uid"] as? String ?: "",
                email = map["email"] as? String ?: "",
                name = map["name"] as? String ?: "",
                role = UserRole.fromString(map["role"] as? String),
                status = UserStatus.fromString(map["status"] as? String),
                faculty = map["faculty"] as? String,
                studentId = map["studentId"] as? String,
                department = map["department"] as? String,
                programme = map["programme"] as? String,
                yearOfStudy = map["yearOfStudy"] as? String,
                phone = map["phone"] as? String,
                createdAt = (map["createdAt"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
        }
    }
}
