package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

open class UserRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open fun observeUsers(): Flow<Result<List<User>>> = callbackFlow {
        val registration = safeDb.collection("users")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Result.failure(error))
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val users = snapshot.documents.mapNotNull { doc ->
                        doc.data?.let { User.fromMap(it) }
                    }
                    trySend(Result.success(users))
                }
            }
        awaitClose { registration.remove() }
    }

    open suspend fun getUsers(): Result<List<User>> {
        return try {
            val snapshot = safeDb.collection("users").get().await()
            val users = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { User.fromMap(it) }
            }
            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun getUser(uid: String): Result<User?> {
        return try {
            val doc = safeDb.collection("users").document(uid).get().await()
            val data = doc.data
            val user = if (data != null) User.fromMap(data) else null
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun updateUser(user: User): Result<Unit> {
        return try {
            safeDb.collection("users").document(user.uid).set(user.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun updateUserStatus(uid: String, status: UserStatus): Result<Unit> {
        return try {
            safeDb.collection("users").document(uid).update("status", status.name).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
