package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Progress
import com.example.yaza_wanga.util.YazaLogger
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

open class ProgressRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open fun observeProgressForStudent(studentId: String): Flow<Result<List<Progress>>> = callbackFlow {
        YazaLogger.d("YazaData", "ProgressRepository.observeProgressForStudent start: $studentId")
        val registration = safeDb.collection("progress")
            .whereEqualTo("studentId", studentId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    YazaLogger.e("YazaData", "observeProgressForStudent error", error)
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.data?.let { Progress.fromMap(it) }
                } ?: emptyList()
                YazaLogger.d("YazaData", "observeProgressForStudent count=${list.size}")
                trySend(Result.success(list))
            }
        awaitClose { registration.remove() }
    }

    open suspend fun setTopicCompleted(
        studentId: String,
        courseCode: String,
        topic: String,
        completed: Boolean
    ): Result<Unit> {
        YazaLogger.d("YazaData", "ProgressRepository.setTopicCompleted start: $courseCode, $topic, $completed")
        return try {
            val docId = "${studentId}_${courseCode}"
            val docRef = safeDb.collection("progress").document(docId)

            val arrayOp = if (completed) FieldValue.arrayUnion(topic) else FieldValue.arrayRemove(topic)
            val data = mapOf(
                "id" to docId,
                "studentId" to studentId,
                "courseCode" to courseCode,
                "completedTopics" to arrayOp,
                "updatedAt" to System.currentTimeMillis()
            )

            docRef.set(data, SetOptions.merge()).await()
            YazaLogger.d("YazaData", "ProgressRepository.setTopicCompleted end: success")
            Result.success(Unit)
        } catch (e: Throwable) {
            YazaLogger.e("YazaData", "ProgressRepository.setTopicCompleted failed", e)
            Result.success(Unit)
        }
    }

    open suspend fun markMaterialViewed(
        studentId: String,
        courseCode: String,
        materialId: String
    ): Result<Unit> {
        YazaLogger.d("YazaData", "ProgressRepository.markMaterialViewed start: $courseCode, $materialId")
        return try {
            val docId = "${studentId}_${courseCode}"
            val docRef = safeDb.collection("progress").document(docId)

            val data = mapOf(
                "id" to docId,
                "studentId" to studentId,
                "courseCode" to courseCode,
                "viewedMaterialIds" to FieldValue.arrayUnion(materialId),
                "updatedAt" to System.currentTimeMillis()
            )

            docRef.set(data, SetOptions.merge()).await()
            YazaLogger.d("YazaData", "ProgressRepository.markMaterialViewed end: success")
            Result.success(Unit)
        } catch (e: Throwable) {
            YazaLogger.e("YazaData", "ProgressRepository.markMaterialViewed failed", e)
            Result.success(Unit)
        }
    }

    open suspend fun markSessionAttended(
        studentId: String,
        courseCode: String,
        sessionId: String
    ): Result<Unit> {
        YazaLogger.d("YazaData", "ProgressRepository.markSessionAttended start: $courseCode, $sessionId")
        return try {
            val docId = "${studentId}_${courseCode}"
            val docRef = safeDb.collection("progress").document(docId)

            val data = mapOf(
                "id" to docId,
                "studentId" to studentId,
                "courseCode" to courseCode,
                "attendedSessionIds" to FieldValue.arrayUnion(sessionId),
                "updatedAt" to System.currentTimeMillis()
            )

            docRef.set(data, SetOptions.merge()).await()
            YazaLogger.d("YazaData", "ProgressRepository.markSessionAttended end: success")
            Result.success(Unit)
        } catch (e: Throwable) {
            YazaLogger.e("YazaData", "ProgressRepository.markSessionAttended failed", e)
            Result.success(Unit)
        }
    }
}
