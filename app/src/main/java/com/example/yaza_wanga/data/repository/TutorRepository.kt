package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.TutorProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

open class TutorRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open fun observeTutorsForCourse(courseCode: String): Flow<Result<List<TutorProfile>>> = callbackFlow {
        val registration = safeDb.collection("tutors")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val tutors = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let { TutorProfile.fromMap(it) }
                    }.filter { (it.status == "ACTIVE") && (it.courses.contains(courseCode) || it.courses.isEmpty()) }
                    trySend(Result.success(tutors))
                }
            }
        awaitClose { registration.remove() }
    }

    open suspend fun getTutorProfile(tutorId: String): Result<TutorProfile?> {
        return try {
            val doc = safeDb.collection("tutors").document(tutorId).get().await()
            val data = doc.data
            val profile = if (data != null) TutorProfile.fromMap(data) else null
            Result.success(profile)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun saveTutorProfile(profile: TutorProfile): Result<Unit> {
        return try {
            safeDb.collection("tutors").document(profile.tutorId).set(profile.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun getTutorsForCourse(courseCode: String): Result<List<TutorProfile>> {
        return try {
            val snapshot = safeDb.collection("tutors").get().await()
            val tutors = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { TutorProfile.fromMap(it) }
            }.filter { (it.status == "ACTIVE") && (it.courses.contains(courseCode) || it.courses.isEmpty()) }

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
