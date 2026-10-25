package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Session
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

open class SessionRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open suspend fun getSessions(): Result<List<Session>> {
        return try {
            val snapshot = safeDb.collection("sessions").get().await()
            val sessions = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Session.fromMap(it) }
            }
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun createSession(session: Session): Result<Unit> {
        return try {
            safeDb.collection("sessions").document(session.id).set(session.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
