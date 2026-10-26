package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Session
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

open class SessionRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open fun observeSessions(): Flow<Result<List<Session>>> = callbackFlow {
        val registration = safeDb.collection("sessions")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val sessions = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let { Session.fromMap(it) }
                    }
                    trySend(Result.success(sessions))
                }
            }
        awaitClose { registration.remove() }
    }

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
