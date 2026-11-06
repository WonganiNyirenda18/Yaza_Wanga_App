package com.example.yaza_wanga.util

import com.example.yaza_wanga.data.model.Material
import kotlin.math.roundToInt

data class TopicState(
    val topicTag: String,
    val isCompleted: Boolean
)

data class CourseProgressCalculation(
    val courseCode: String,
    val courseTitle: String,
    val topics: List<TopicState>,
    val totalTopicsCount: Int,
    val completedTopicsCount: Int,
    val totalMaterialsCount: Int,
    val viewedMaterialsCount: Int,
    val attendedSessionsCount: Int,
    val percent: Int
)

object ProgressMath {
    fun calculate(
        courseCode: String,
        courseTitle: String,
        permittedMaterials: List<Material>,
        completedTopicsFromProgress: List<String>,
        viewedMaterialIdsFromProgress: List<String>,
        attendedSessionIdsFromProgress: List<String>
    ): CourseProgressCalculation {
        // Topics = distinct non-blank topicTag values of permitted materials
        val permittedTopicTags = permittedMaterials
            .map { it.topicTag.trim() }
            .filter { it.isNotBlank() }
            .distinct()

        // Completed topics = completedTopics in progress that still exist in permittedTopicTags
        val validCompletedTopics = completedTopicsFromProgress
            .map { it.trim() }
            .filter { permittedTopicTags.contains(it) }
            .toSet()

        val topicStates = permittedTopicTags.map { tag ->
            TopicState(topicTag = tag, isCompleted = validCompletedTopics.contains(tag))
        }

        val totalTopics = permittedTopicTags.size
        val completedTopicsCount = validCompletedTopics.size

        // Total materials = number of permitted materials of that course
        val permittedMaterialIds = permittedMaterials.map { it.id }.filter { it.isNotBlank() }.toSet()
        val totalMaterials = permittedMaterials.size

        // Viewed materials = viewedMaterialIds that still exist in permittedMaterialIds
        val validViewedMaterials = viewedMaterialIdsFromProgress.filter { permittedMaterialIds.contains(it) }.toSet()
        val viewedMaterialsCount = validViewedMaterials.size

        // Sessions attended = size of attendedSessionIds (count only)
        val attendedSessionsCount = attendedSessionIdsFromProgress.distinct().size

        // Overall percentage = (completed topics + viewed materials) / (total topics + total materials) * 100, rounded; 0 when denominator is 0; never above 100
        val numerator = completedTopicsCount + viewedMaterialsCount
        val denominator = totalTopics + totalMaterials

        val rawPercent = if (denominator > 0) {
            ((numerator.toDouble() / denominator.toDouble()) * 100).roundToInt()
        } else {
            0
        }

        val finalPercent = rawPercent.coerceIn(0, 100)

        return CourseProgressCalculation(
            courseCode = courseCode,
            courseTitle = courseTitle,
            topics = topicStates,
            totalTopicsCount = totalTopics,
            completedTopicsCount = completedTopicsCount,
            totalMaterialsCount = totalMaterials,
            viewedMaterialsCount = viewedMaterialsCount,
            attendedSessionsCount = attendedSessionsCount,
            percent = finalPercent
        )
    }
}
