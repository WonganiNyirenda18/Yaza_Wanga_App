package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Course
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CourseRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun getCourses(): Result<List<Course>> {
        return try {
            val snapshot = db.collection("courses").get().await()
            val courses = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Course.fromMap(it) }
            }
            Result.success(courses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourse(code: String): Result<Course?> {
        return try {
            val doc = db.collection("courses").document(code).get().await()
            val data = doc.data
            val course = if (data != null) Course.fromMap(data) else null
            Result.success(course)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveCourse(course: Course): Result<Unit> {
        return try {
            db.collection("courses").document(course.code).set(course.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
