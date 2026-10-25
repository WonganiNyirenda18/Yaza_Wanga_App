package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Department
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

open class DepartmentRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open suspend fun getDepartments(): Result<List<Department>> {
        return try {
            val snapshot = safeDb.collection("departments").get().await()
            val list = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Department.fromMap(it) }
            }
            if (list.isEmpty()) {
                val defaults = listOf(
                    Department("comp", "Computer Science & IT", "Faculty of Information Sciences", "INF", "Computing and Information Technology"),
                    Department("math", "Mathematics", "Faculty of Physical Sciences", "MAT", "Applied Mathematics & Statistics"),
                    Department("eng", "Engineering", "School of Engineering", "ENG", "Engineering & Technology"),
                    Department("biz", "Business", "Faculty of Commerce", "BUS", "Business Information Systems")
                )
                Result.success(defaults)
            } else {
                Result.success(list)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun saveDepartment(department: Department): Result<Unit> {
        return try {
            safeDb.collection("departments").document(department.id).set(department.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
