package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.TutorProfile
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

open class TutorRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open fun observeVerifiedTutorsForCourse(courseCode: String): Flow<Result<List<TutorProfile>>> = callbackFlow {
        val registration = safeDb.collection("users")
            .whereEqualTo("role", UserRole.TUTOR.name)
            .whereEqualTo("status", UserStatus.ACTIVE.name)
            .addSnapshotListener { userSnapshot, userError ->
                if (userError != null) {
                    trySend(Result.failure(userError))
                    return@addSnapshotListener
                }

                safeDb.collection("tutors").get().addOnSuccessListener { tutorSnapshot ->
                    val tutorMap = tutorSnapshot.documents.mapNotNull { doc ->
                        doc.data?.let { data ->
                            val p = TutorProfile.fromMap(data)
                            val id = if (p.tutorId.isNotBlank()) p.tutorId else doc.id
                            p.copy(tutorId = id)
                        }
                    }.associateBy { it.tutorId }

                    val activeUserTutors = userSnapshot?.documents?.mapNotNull { doc ->
                        val uid = doc.id
                        val name = doc.getString("name") ?: doc.getString("email")?.substringBefore("@") ?: "Tutor"
                        val existing = tutorMap[uid]
                        if (existing != null) {
                            existing.copy(
                                tutorId = uid,
                                name = existing.name.ifBlank { name },
                                status = "VERIFIED"
                            )
                        } else {
                            TutorProfile(
                                tutorId = uid,
                                name = name,
                                bio = "Peer Tutor",
                                status = "VERIFIED"
                            )
                        }
                    } ?: emptyList()

                    // Also include any profiles explicitly marked VERIFIED or ACTIVE in tutors collection
                    val verifiedTutorProfiles = tutorMap.values.filter {
                        it.status == "VERIFIED" || it.status == "ACTIVE" || it.status == "APPROVED"
                    }

                    val combined = (activeUserTutors + verifiedTutorProfiles)
                        .distinctBy { it.tutorId }
                        .filter {
                            courseCode.isBlank() || it.courses.contains(courseCode) || it.courses.isEmpty()
                        }

                    trySend(Result.success(combined))
                }.addOnFailureListener { err ->
                    trySend(Result.failure(err))
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
            val profile = if (data != null) {
                val p = TutorProfile.fromMap(data)
                if (p.tutorId.isBlank()) p.copy(tutorId = doc.id) else p
            } else null
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
            val userSnapshot = safeDb.collection("users")
                .whereEqualTo("role", UserRole.TUTOR.name)
                .whereEqualTo("status", UserStatus.ACTIVE.name)
                .get()
                .await()

            val tutorSnapshot = safeDb.collection("tutors").get().await()
            val tutorMap = tutorSnapshot.documents.mapNotNull { doc ->
                doc.data?.let { data ->
                    val p = TutorProfile.fromMap(data)
                    val id = if (p.tutorId.isNotBlank()) p.tutorId else doc.id
                    p.copy(tutorId = id)
                }
            }.associateBy { it.tutorId }

            val activeUserTutors = userSnapshot.documents.mapNotNull { doc ->
                val uid = doc.id
                val name = doc.getString("name") ?: doc.getString("email")?.substringBefore("@") ?: "Tutor"
                val existing = tutorMap[uid]
                if (existing != null) {
                    existing.copy(
                        tutorId = uid,
                        name = existing.name.ifBlank { name },
                        status = "VERIFIED"
                    )
                } else {
                    TutorProfile(
                        tutorId = uid,
                        name = name,
                        bio = "Peer Tutor",
                        status = "VERIFIED"
                    )
                }
            }

            val verifiedTutorProfiles = tutorMap.values.filter {
                it.status == "VERIFIED" || it.status == "ACTIVE" || it.status == "APPROVED"
            }

            val combined = (activeUserTutors + verifiedTutorProfiles)
                .distinctBy { it.tutorId }
                .filter {
                    courseCode.isBlank() || it.courses.contains(courseCode) || it.courses.isEmpty()
                }

            Result.success(combined)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
