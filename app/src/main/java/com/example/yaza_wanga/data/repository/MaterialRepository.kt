package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Material
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MaterialRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun getMaterials(courseCode: String): Result<List<Material>> {
        return try {
            val snapshot = db.collection("materials").whereEqualTo("courseCode", courseCode).get().await()
            val materials = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Material.fromMap(it) }
            }
            Result.success(materials)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun uploadMaterial(material: Material): Result<Unit> {
        return try {
            db.collection("materials").document(material.id).set(material.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
