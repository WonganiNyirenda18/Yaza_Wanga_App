package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.TutorProfile
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

open class TutorRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open fun observeVerifiedTutorsForCourse(courseCode: String): Flow<Result<List<TutorProfile>>> = callbackFlow {
        val registration = safeDb.collection("tutors")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val tutors = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let { TutorProfile.fromMap(it) }
                    }.filter {
                        it.status == "VERIFIED" && it.courses.contains(courseCode)
                    }
                    trySend(Result.success(tutors))
                }
            }
        awaitClose { registration.remove() }
    }

    open fun observeTutor(tutorId: String): Flow<Result<TutorProfile?>> = callbackFlow {
        val registration = safeDb.collection("tutors").document(tutorId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                val profile = snapshot?.data?.let { TutorProfile.fromMap(it) }
                trySend(Result.success(profile))
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
            }.filter {
                it.status == "VERIFIED" && it.courses.contains(courseCode)
            }
            Result.success(tutors)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
