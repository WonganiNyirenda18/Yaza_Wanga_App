package com.example.yaza_wanga

import com.example.yaza_wanga.data.model.Material
import com.example.yaza_wanga.util.ProgressMath
import org.junit.Assert.assertEquals
import org.junit.Test

class ProgressMathTest {

    @Test
    fun percentageZeroWithNoTopicsAndNoMaterials() {
        val calc = ProgressMath.calculate(
            courseCode = "INF 212",
            courseTitle = "Database Systems",
            permittedMaterials = emptyList(),
            completedTopicsFromProgress = emptyList(),
            viewedMaterialIdsFromProgress = emptyList(),
            attendedSessionIdsFromProgress = emptyList()
        )

        assertEquals(0, calc.percent)
        assertEquals(0, calc.totalTopicsCount)
        assertEquals(0, calc.completedTopicsCount)
        assertEquals(0, calc.totalMaterialsCount)
        assertEquals(0, calc.viewedMaterialsCount)
        assertEquals(0, calc.attendedSessionsCount)
    }

    @Test
    fun partialPercentage() {
        val m1 = Material(id = "m1", courseCode = "INF 212", topicTag = "SQL", status = "APPROVED", access = "FREE")
        val m2 = Material(id = "m2", courseCode = "INF 212", topicTag = "NoSQL", status = "APPROVED", access = "FREE")

        val calc = ProgressMath.calculate(
            courseCode = "INF 212",
            courseTitle = "Database Systems",
            permittedMaterials = listOf(m1, m2),
            completedTopicsFromProgress = listOf("SQL"),
            viewedMaterialIdsFromProgress = listOf("m1"),
            attendedSessionIdsFromProgress = listOf("s1")
        )

        // Denominator = 2 topics + 2 materials = 4
        // Numerator = 1 completed topic + 1 viewed material = 2
        // Percentage = 2 / 4 * 100 = 50%
        assertEquals(50, calc.percent)
        assertEquals(2, calc.totalTopicsCount)
        assertEquals(1, calc.completedTopicsCount)
        assertEquals(2, calc.totalMaterialsCount)
        assertEquals(1, calc.viewedMaterialsCount)
        assertEquals(1, calc.attendedSessionsCount)
    }

    @Test
    fun hundredPercent() {
        val m1 = Material(id = "m1", courseCode = "INF 212", topicTag = "SQL", status = "APPROVED", access = "FREE")

        val calc = ProgressMath.calculate(
            courseCode = "INF 212",
            courseTitle = "Database Systems",
            permittedMaterials = listOf(m1),
            completedTopicsFromProgress = listOf("SQL"),
            viewedMaterialIdsFromProgress = listOf("m1"),
            attendedSessionIdsFromProgress = listOf("s1")
        )

        assertEquals(100, calc.percent)
    }

    @Test
    fun neverAboveHundredPercent() {
        val m1 = Material(id = "m1", courseCode = "INF 212", topicTag = "SQL", status = "APPROVED", access = "FREE")

        val calc = ProgressMath.calculate(
            courseCode = "INF 212",
            courseTitle = "Database Systems",
            permittedMaterials = listOf(m1),
            completedTopicsFromProgress = listOf("SQL", "SQL", "ExtraTopic"),
            viewedMaterialIdsFromProgress = listOf("m1", "m1", "m2"),
            attendedSessionIdsFromProgress = listOf("s1", "s2")
        )

        assertEquals(100, calc.percent)
    }

    @Test
    fun removedTopicsAndMaterialsDoNotCount() {
        val m1 = Material(id = "m1", courseCode = "INF 212", topicTag = "SQL", status = "APPROVED", access = "FREE")

        val calc = ProgressMath.calculate(
            courseCode = "INF 212",
            courseTitle = "Database Systems",
            permittedMaterials = listOf(m1),
            completedTopicsFromProgress = listOf("SQL", "RemovedTopic"),
            viewedMaterialIdsFromProgress = listOf("m1", "m_removed"),
            attendedSessionIdsFromProgress = emptyList()
        )

        assertEquals(1, calc.completedTopicsCount)
        assertEquals(1, calc.viewedMaterialsCount)
        assertEquals(100, calc.percent)
    }

    @Test
    fun topicsComeOnlyFromPermittedMaterials() {
        val mPermitted = Material(id = "m1", courseCode = "INF 212", topicTag = "SQL", status = "APPROVED", access = "FREE")
        val mRestricted = Material(id = "m2", courseCode = "INF 212", topicTag = "Hacking", status = "APPROVED", access = "RESTRICTED")

        val calc = ProgressMath.calculate(
            courseCode = "INF 212",
            courseTitle = "Database Systems",
            permittedMaterials = listOf(mPermitted),
            completedTopicsFromProgress = listOf("Hacking"),
            viewedMaterialIdsFromProgress = emptyList(),
            attendedSessionIdsFromProgress = emptyList()
        )

        assertEquals(1, calc.totalTopicsCount)
        assertEquals("SQL", calc.topics.first().topicTag)
        assertEquals(0, calc.completedTopicsCount)
    }
}
