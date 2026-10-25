package com.example.yaza_wanga.data.repository

import com.example.yaza_wanga.data.model.Course
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CourseRepository(private val db: FirebaseFirestore = FirebaseFirestore.getInstance()) {
    suspend fun getCourses(): Result<List<Course>> {
        return try {
            val snapshot = db.collection("courses").get().await()
            var courses = snapshot.documents.mapNotNull { doc ->
                doc.data?.let { Course.fromMap(it) }
            }
            if (courses.isEmpty()) {
                // Seed default courses
                val defaults = listOf(
                    Course("INF 212", "Introduction to Web Development", "Computer Science & IT", 3, "Foundational principles of frontend architecture, semantic web development, responsive CSS layouts, and modern JavaScript programming paradigms for interactive web apps.", "Semester 1", listOf("INF 111"), "ACTIVE"),
                    Course("COM 311", "Database Systems & Architecture", "Computer Science & IT", 4, "Relational database design, SQL querying, normalization, indexing, and ACID transactions.", "Semester 1", listOf("INF 212"), "ACTIVE"),
                    Course("STA 211", "Probability Theory & Applied Statistics", "Mathematics", 3, "Probability distributions, hypothesis testing, regression analysis, and statistical inference.", "Semester 1", listOf("MAT 101"), "ACTIVE"),
                    Course("COM 312", "Software Architecture & Agile Methods", "Computer Science & IT", 3, "Software design patterns, microservices architecture, CI/CD pipelines, and Scrum agile development methodologies.", "Semester 2", listOf("COM 311"), "ACTIVE"),
                    Course("MAT 201", "Multivariable Calculus & Linear Algebra", "Mathematics", 4, "Vector calculus, partial derivatives, matrices, eigenvalues, and linear transformations.", "Semester 1", listOf("MAT 101"), "ACTIVE"),
                    Course("INF 111", "Algorithms & Data Structures", "Computer Science & IT", 3, "Fundamental data structures, sorting algorithms, time complexity, and problem-solving techniques.", "Semester 1", emptyList(), "ACTIVE")
                )
                for (course in defaults) {
                    db.collection("courses").document(course.code).set(course.toMap()).await()
                }
                courses = defaults
            }
            Result.success(courses)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCourse(code: String): Result<Course?> {
        return try {
            val doc = db.collection("courses").document(code).get().await()
            val data = doc.data
            val course = if (data != null) Course.fromMap(data) else null
            Result.success(course)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun saveCourse(course: Course): Result<Unit> {
        return try {
            db.collection("courses").document(course.code).set(course.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
