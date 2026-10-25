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
}
