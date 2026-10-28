package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Enrollment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

open class EnrollmentRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open fun observeEnrollmentsForStudent(studentId: String): Flow<Result<List<Enrollment>>> = callbackFlow {
        val registration = safeDb.collection("enrollments")
            .whereEqualTo("studentId", studentId)
            .whereEqualTo("status", "ACTIVE")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val enrollments = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let { Enrollment.fromMap(it) }
                    }
                    trySend(Result.success(enrollments))
                }
            }
        awaitClose { registration.remove() }
    }

    open fun observeEnrollmentsForCourses(courseCodes: List<String>): Flow<Result<List<Enrollment>>> = callbackFlow {
        if (courseCodes.isEmpty()) {
            trySend(Result.success(emptyList()))
            awaitClose {}
            return@callbackFlow
        }
        val registration = safeDb.collection("enrollments")
            .whereEqualTo("status", "ACTIVE")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val enrollments = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let { Enrollment.fromMap(it) }
                    }.filter { courseCodes.contains(it.courseCode) }
                    trySend(Result.success(enrollments))
                }
            }
        awaitClose { registration.remove() }
    }

    open suspend fun getEnrollmentsForStudent(studentId: String): Result<List<Enrollment>> {
        return try {
            val snapshot = safeDb.collection("enrollments")
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("status", "ACTIVE")
                .get()
                .await()
            val enrollments = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Enrollment.fromMap(it) }
            }
            Result.success(enrollments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun getEnrollments(): Result<List<Enrollment>> {
        return try {
            val snapshot = safeDb.collection("enrollments")
                .whereEqualTo("status", "ACTIVE")
                .get()
                .await()
            val enrollments = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Enrollment.fromMap(it) }
            }
            Result.success(enrollments)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun enroll(enrollment: Enrollment): Result<Unit> {
        return try {
            val docId = "${enrollment.studentId}_${enrollment.courseCode}"
            val existing = safeDb.collection("enrollments").document(docId).get().await()

            if (existing.exists() && existing.getString("status") == "ACTIVE") {
                return Result.failure(Exception("Already enrolled in this course"))
            }

            val map = enrollment.toMap().toMutableMap()
            map["id"] = docId
            safeDb.collection("enrollments").document(docId).set(map).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun isEnrolled(studentId: String, courseCode: String): Result<Boolean> {
        return try {
            val docId = "${studentId}_${courseCode}"
            val doc = safeDb.collection("enrollments").document(docId).get().await()
            val enrolled = doc.exists() && doc.getString("status") == "ACTIVE"
            Result.success(enrolled)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
