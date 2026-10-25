package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.TutorProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class TutorRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun getTutorProfile(tutorId: String): Result<TutorProfile?> {
        return try {
            val doc = db.collection("tutors").document(tutorId).get().await()
            val data = doc.data
            val profile = if (data != null) TutorProfile.fromMap(data) else null
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveTutorProfile(profile: TutorProfile): Result<Unit> {
        return try {
            db.collection("tutors").document(profile.tutorId).set(profile.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTutorsForCourse(courseCode: String): Result<List<TutorProfile>> {
        return try {
            val snapshot = db.collection("tutors").get().await()
            val tutors = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { TutorProfile.fromMap(it) }
            }.filter { it.courses.contains(courseCode) || it.courses.isEmpty() }

            if (tutors.isEmpty()) {
                val defaults = listOf(
                    TutorProfile("t1", "Chimwemwe Phiri", "Specializes in Frontend, DOM & JavaScript", 0.0, listOf(courseCode), 4.9, 48, "ACTIVE", "+265995358915", "+265995358915", "Both Online & Offline"),
                    TutorProfile("t2", "Thokozani Mwale", "HTML/CSS semantic architecture & APIs", 2500.0, listOf(courseCode), 4.8, 36, "ACTIVE", "+265999123456", "+265999123456", "Online Only (Google Meet)"),
                    TutorProfile("t3", "John Banda", "Hands-on coding, CSS Grid & responsive layout", 2000.0, listOf(courseCode), 4.7, 29, "ACTIVE", "+265888765432", "+265888765432", "Both Online & Offline")
                )
                Result.success(defaults)
            } else {
                Result.success(tutors)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
