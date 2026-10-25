package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Session
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class SessionRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun getSessions(): Result<List<Session>> {
        return try {
            val snapshot = db.collection("sessions").get().await()
            val sessions = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Session.fromMap(it) }
            }
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createSession(session: Session): Result<Unit> {
        return try {
            db.collection("sessions").document(session.id).set(session.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
