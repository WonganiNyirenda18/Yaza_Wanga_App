package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Enrollment
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class EnrollmentRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun getEnrollmentsForStudent(studentId: String): Result<List<Enrollment>> {
        return try {
            val snapshot = db.collection("enrollments")
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

    suspend fun enroll(enrollment: Enrollment): Result<Unit> {
        return try {
            // Check for existing enrollment
            val existing = db.collection("enrollments")
                .whereEqualTo("studentId", enrollment.studentId)
                .whereEqualTo("courseCode", enrollment.courseCode)
                .get()
                .await()

            if (!existing.isEmpty) {
                return Result.failure(Exception("Already enrolled in this course"))
            }

            db.collection("enrollments").document(enrollment.id).set(enrollment.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun isEnrolled(studentId: String, courseCode: String): Result<Boolean> {
        return try {
            val snapshot = db.collection("enrollments")
                .whereEqualTo("studentId", studentId)
                .whereEqualTo("courseCode", courseCode)
                .whereEqualTo("status", "ACTIVE")
                .get()
                .await()
            Result.success(!snapshot.isEmpty)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
