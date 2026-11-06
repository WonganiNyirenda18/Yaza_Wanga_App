package com.example.yaza_wanga.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaza_wanga.data.AppContainer
import com.example.yaza_wanga.data.model.Material
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MaterialItem(
    val material: Material,
    val tutorName: String
)

data class MaterialsUiState(
    val materials: List<MaterialItem> = emptyList(),
    val searchQuery: String = "",
    val categories: List<String> = listOf("All"),
    val selectedCategory: String = "All",
    val courses: List<String> = listOf("All"),
    val selectedCourse: String = "All",
    val hasEnrollments: Boolean = true,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class MaterialsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MaterialsUiState())
    val uiState: StateFlow<MaterialsUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val userRes = AppContainer.authRepository.fetchCurrentUser()
                val user = userRes.getOrNull()
                if (user == null) {
                    val err = userRes.exceptionOrNull()?.localizedMessage ?: "Could not load user profile"
                    _uiState.update { it.copy(isLoading = false, errorMessage = err) }
                    return@launch
                }

                val enrollmentsRes = AppContainer.enrollmentRepository.getEnrollmentsForStudent(user.uid)
                val enrollments = enrollmentsRes.getOrElse { emptyList() }
                val enrolledCourseCodes = enrollments.map { it.courseCode }

                if (enrolledCourseCodes.isEmpty()) {
                    _uiState.update {
                        it.copy(
                            materials = emptyList(),
                            hasEnrollments = false,
                            courses = listOf("All"),
                            categories = listOf("All"),
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                    return@launch
                }

                // Pre-fetch tutor names (one lookup per distinct tutorId)
                val allTutors = AppContainer.tutorRepository.getTutorsForCourse("").getOrElse { emptyList() }
                val tutorMap = allTutors.associateBy({ it.tutorId }, { it.name })

                val flows = enrolledCourseCodes.map { code ->
                    AppContainer.materialRepository.observeMaterialsForCourse(code)
                }

                combine(flows) { resultsArray ->
                    val combinedMaterials = mutableListOf<Material>()
                    for (res in resultsArray) {
                        val list = res.getOrElse { emptyList() }
                        combinedMaterials.addAll(list)
                    }

                    // Filter permitted materials: status == "APPROVED" and access != "RESTRICTED"
                    val permitted = combinedMaterials.filter { m ->
                        m.status.equals("APPROVED", ignoreCase = true) &&
                                !m.access.equals("RESTRICTED", ignoreCase = true)
                    }

                    val items = permitted.map { m ->
                        val name = tutorMap[m.tutorId]?.ifBlank { "Peer Tutor" } ?: "Peer Tutor"
                        MaterialItem(material = m, tutorName = name)
                    }.sortedByDescending { it.material.createdAt }

                    val presentCategories = listOf("All") + items.map { it.material.category }.filter { it.isNotBlank() }.distinct()
                    val availableCourseCodes = listOf("All") + enrolledCourseCodes

                    _uiState.update {
                        it.copy(
                            materials = items,
                            hasEnrollments = true,
                            categories = presentCategories,
                            courses = availableCourseCodes,
                            isLoading = false,
                            errorMessage = null
                        )
                    }
                }.collect {}
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Could not reach the server. Check your connection and try again"
                    )
                }
            }
        }
    }

    fun setSearchQuery(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun setSelectedCategory(category: String) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun setSelectedCourse(courseCode: String) {
        _uiState.update { it.copy(selectedCourse = courseCode) }
    }

    fun retry() {
        loadData()
    }
}

data class MaterialDetailsUiState(
    val materialItem: MaterialItem? = null,
    val isNotEnrolled: Boolean = false,
    val isNotAvailable: Boolean = false,
    val isLoading: Boolean = true,
    val errorMessage: String? = null
)

class MaterialDetailsViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MaterialDetailsUiState())
    val uiState: StateFlow<MaterialDetailsUiState> = _uiState.asStateFlow()

    fun loadMaterial(courseCode: String, materialId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, isNotEnrolled = false, isNotAvailable = false) }
            try {
                val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
                if (user == null) {
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Could not load user profile") }
                    return@launch
                }

                val isEnrolled = AppContainer.enrollmentRepository.isEnrolled(user.uid, courseCode).getOrDefault(false)
                if (!isEnrolled) {
                    _uiState.update { it.copy(isNotEnrolled = true, isLoading = false) }
                    return@launch
                }

                val allRes = AppContainer.materialRepository.getAllMaterials()
                val allMaterials = allRes.getOrElse { emptyList() }
                val target = allMaterials.find { it.id == materialId && it.courseCode.equals(courseCode, ignoreCase = true) }

                if (target == null || !target.status.equals("APPROVED", ignoreCase = true) || target.access.equals("RESTRICTED", ignoreCase = true)) {
                    _uiState.update { it.copy(isNotAvailable = true, isLoading = false) }
                    return@launch
                }

                val allTutors = AppContainer.tutorRepository.getTutorsForCourse(courseCode).getOrElse { emptyList() }
                val tutorName = allTutors.find { it.tutorId == target.tutorId }?.name?.ifBlank { "Peer Tutor" } ?: "Peer Tutor"

                _uiState.update {
                    it.copy(
                        materialItem = MaterialItem(material = target, tutorName = tutorName),
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.localizedMessage ?: "Could not reach the server. Check your connection and try again"
                    )
                }
            }
        }
    }

    fun markMaterialViewed(courseCode: String, materialId: String) {
        viewModelScope.launch {
            try {
                val user = AppContainer.authRepository.fetchCurrentUser().getOrNull()
                if (user != null) {
                    AppContainer.progressRepository.markMaterialViewed(user.uid, courseCode, materialId)
                }
            } catch (_: Throwable) {}
        }
    }
}
