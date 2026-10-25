package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.User
import com.example.yaza_wanga.data.model.UserRole
import com.example.yaza_wanga.data.model.UserStatus
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.net.UnknownHostException

open class AuthRepository(
    private val auth: FirebaseAuth? = null,
    private val db: FirebaseFirestore? = null
) {
    private val safeAuth get() = auth ?: FirebaseAuth.getInstance()
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open val currentUser: FirebaseUser? get() = auth?.currentUser

    open suspend fun signIn(email: String, pass: String): Result<User> {
        return try {
            val trimmedEmail = email.trim()
            val authResult = safeAuth.signInWithEmailAndPassword(trimmedEmail, pass).await()
            val firebaseUser = authResult.user ?: return Result.failure(Exception("Invalid email or password"))
            val uid = firebaseUser.uid

            val doc = safeDb.collection("users").document(uid).get().await()
            val userMap = doc.data
            val user = if (userMap != null) {
                User.fromMap(userMap)
            } else {
                User(uid = uid, email = firebaseUser.email ?: trimmedEmail, name = firebaseUser.displayName ?: "")
            }

            if (user.status == UserStatus.SUSPENDED) {
                safeAuth.signOut()
                return Result.failure(Exception("Your account has been suspended. Contact the administrator"))
            }

            Result.success(user)
        } catch (e: Exception) {
            val message = when (e) {
                is FirebaseAuthInvalidCredentialsException,
                is FirebaseAuthInvalidUserException -> "Invalid email or password"
                is FirebaseNetworkException,
                is UnknownHostException -> "No internet connection. Please check your network and try again"
                else -> {
                    val msg = e.localizedMessage ?: ""
                    if (msg.contains("network", true) || msg.contains("host", true) || msg.contains("unable to resolve host", true)) {
                        "No internet connection. Please check your network and try again"
                    } else if (msg.contains("suspended", true)) {
                        "Your account has been suspended. Contact the administrator"
                    } else {
                        "Invalid email or password"
                    }
                }
            }
            Result.failure(Exception(message))
        }
    }

    open suspend fun signUp(
        email: String,
        pass: String,
        role: UserRole,
        name: String,
        faculty: String? = null,
        studentId: String? = null
    ): Result<User> {
        return try {
            val trimmedEmail = email.trim()
            val authResult = safeAuth.createUserWithEmailAndPassword(trimmedEmail, pass).await()
            val firebaseUser = authResult.user ?: return Result.failure(Exception("User UID is null"))
            val uid = firebaseUser.uid

            val status = if (role == UserRole.TUTOR) UserStatus.PENDING else UserStatus.ACTIVE

            val user = User(
                uid = uid,
                email = trimmedEmail,
                name = name,
                role = role,
                status = status,
                faculty = faculty,
                studentId = studentId,
                createdAt = System.currentTimeMillis()
            )

            safeDb.collection("users").document(uid).set(user.toMap()).await()

            try {
                firebaseUser.sendEmailVerification().await()
            } catch (_: Exception) {
                // Ignore if offline
            }

            Result.success(user)
        } catch (e: Exception) {
            val message = when (e) {
                is FirebaseAuthUserCollisionException -> "An account with this email already exists"
                is FirebaseNetworkException,
                is UnknownHostException -> "No internet connection. Please check your network and try again"
                else -> {
                    val msg = e.localizedMessage ?: ""
                    if (msg.contains("already in use", true) || msg.contains("collision", true) || msg.contains("email address is already in use", true)) {
                        "An account with this email already exists"
                    } else if (msg.contains("network", true) || msg.contains("host", true) || msg.contains("unable to resolve host", true)) {
                        "No internet connection. Please check your network and try again"
                    } else {
                        e.localizedMessage ?: "Sign up failed"
                    }
                }
            }
            Result.failure(Exception(message))
        }
    }

    open suspend fun sendPasswordResetEmail(email: String): Result<Unit> {
        return try {
            val trimmedEmail = email.trim()
            safeAuth.sendPasswordResetEmail(trimmedEmail).await()
            Result.success(Unit)
        } catch (e: Exception) {
            val message = when (e) {
                is FirebaseNetworkException,
                is UnknownHostException -> "No internet connection. Please check your network and try again"
                else -> {
                    val msg = e.localizedMessage ?: ""
                    if (msg.contains("network", true) || msg.contains("host", true) || msg.contains("unable to resolve host", true)) {
                        "No internet connection. Please check your network and try again"
                    } else {
                        e.localizedMessage ?: "Failed to send password reset email"
                    }
                }
            }
            Result.failure(Exception(message))
        }
    }

    open suspend fun fetchCurrentUser(): Result<User?> {
        val firebaseUser = auth?.currentUser ?: safeAuth.currentUser ?: return Result.success(null)
        return try {
            val doc = safeDb.collection("users").document(firebaseUser.uid).get().await()
            val userMap = doc.data
            if (userMap != null) {
                val user = User.fromMap(userMap)
                if (user.status == UserStatus.SUSPENDED) {
                    safeAuth.signOut()
                    return Result.failure(Exception("Your account has been suspended. Contact the administrator"))
                }
                Result.success(user)
            } else {
                Result.success(User(uid = firebaseUser.uid, email = firebaseUser.email ?: "", name = firebaseUser.displayName ?: ""))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open fun signOut() {
        auth?.signOut() ?: safeAuth.signOut()
    }
}
