package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Course
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

open class CourseRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open fun observeCourses(includeDrafts: Boolean = false): Flow<Result<List<Course>>> = callbackFlow {
        val listener = safeDb.collection("courses")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val courses = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let { Course.fromMap(it) }
                    }.filter { if (includeDrafts) true else it.status == "ACTIVE" }
                    trySend(Result.success(courses))
                }
            }
        awaitClose { listener.remove() }
    }

    open suspend fun getCourses(includeDrafts: Boolean = false): Result<List<Course>> {
        return try {
            val snapshot = safeDb.collection("courses").get().await()
            val courses = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Course.fromMap(it) }
            }.filter { if (includeDrafts) true else it.status == "ACTIVE" }
            Result.success(courses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun getCourse(code: String): Result<Course?> {
        return try {
            val doc = safeDb.collection("courses").document(code).get().await()
            val data = doc.data
            val course = if (data != null) Course.fromMap(data) else null
            Result.success(course)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun saveCourse(course: Course): Result<Unit> {
        return try {
            safeDb.collection("courses").document(course.code).set(course.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
