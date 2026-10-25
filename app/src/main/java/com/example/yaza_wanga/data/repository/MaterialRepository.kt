package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Material
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

open class MaterialRepository(private val db: FirebaseFirestore? = null) {
    private val safeDb get() = db ?: FirebaseFirestore.getInstance()

    open suspend fun getMaterials(courseCode: String): Result<List<Material>> {
        return try {
            val snapshot = safeDb.collection("materials").whereEqualTo("courseCode", courseCode).get().await()
            val materials = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Material.fromMap(it) }
            }
            Result.success(materials)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    open suspend fun uploadMaterial(material: Material): Result<Unit> {
        return try {
            safeDb.collection("materials").document(material.id).set(material.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
