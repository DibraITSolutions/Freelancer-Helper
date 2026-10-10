package freelancerhelper.aab.ui.screens

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import freelancerhelper.aab.data.GeminiBioGenerator
import freelancerhelper.aab.data.UserPreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class WizardStep(val stepNumber: Int, val title: String, val subtitle: String) {
    BASIC_INFO(1, "Basic Info", "Tell us who you are and your role"),
    SKILLS(2, "Skills & Tech Stack", "Select or add your key expertise"),
    EXPERIENCE(3, "Employment History 💼", "Share your background and achievements"),
    RATE_AVAILABILITY(4, "Rate & Availability", "Set your hourly rate and schedule"),
    REVIEW(5, "Review & Prompt", "Verify your profile details")
}

sealed interface BioGenerationState {
    data object Idle : BioGenerationState
    data object Loading : BioGenerationState
    data class Success(val bioText: String, val isMock: Boolean) : BioGenerationState
    data class Error(val message: String) : BioGenerationState
}

data class WizardUiState(
    val currentStep: WizardStep = WizardStep.BASIC_INFO,
    val fullName: String = "",
    val professionalTitle: String = "",
    val bioOverview: String = "",
    val selectedSkills: List<String> = emptyList(),
    val customSkillInput: String = "",
    val yearsOfExperience: String = "3-5 years",
    val experienceTitle: String = "",
    val experienceCompany: String = "",
    val experiencePeriod: String = "",
    val experienceDescription: String = "",
    val hourlyRate: String = "55",
    val availability: String = "Full-time (30+ hrs/week)",
    val location: String = "",
    val promptText: String = "",
    val isComplete: Boolean = false
) {
    val progress: Float
        get() = currentStep.stepNumber / WizardStep.entries.size.toFloat()
}

class WizardViewModel(
    private val geminiBioGenerator: GeminiBioGenerator = GeminiBioGenerator(),
    private val userPreferencesRepository: UserPreferencesRepository? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(WizardUiState())
    val uiState: StateFlow<WizardUiState> = _uiState.asStateFlow()

    private val _apiKey = MutableStateFlow<String>(getInitialApiKey())
    val apiKey: StateFlow<String> = _apiKey.asStateFlow()

    private val _isApiKeyVisible = MutableStateFlow(false)
    val isApiKeyVisible: StateFlow<Boolean> = _isApiKeyVisible.asStateFlow()

    private val _bioGenerationState = MutableStateFlow<BioGenerationState>(BioGenerationState.Idle)
    val bioGenerationState: StateFlow<BioGenerationState> = _bioGenerationState.asStateFlow()

    init {
        if (userPreferencesRepository != null) {
            viewModelScope.launch {
                userPreferencesRepository.profileFlow.collect { savedState ->
                    _uiState.update { current ->
                        if (savedState.fullName.isNotBlank() ||
                            savedState.professionalTitle.isNotBlank() ||
                            savedState.selectedSkills.isNotEmpty() ||
                            savedState.location.isNotBlank() ||
                            savedState.experienceTitle.isNotBlank() ||
                            savedState.experienceCompany.isNotBlank() ||
                            savedState.experiencePeriod.isNotBlank()
                        ) {
                            current.copy(
                                fullName = savedState.fullName,
                                professionalTitle = savedState.professionalTitle,
                                bioOverview = savedState.bioOverview,
                                selectedSkills = savedState.selectedSkills,
                                yearsOfExperience = savedState.yearsOfExperience,
                                experienceTitle = savedState.experienceTitle,
                                experienceCompany = savedState.experienceCompany,
                                experiencePeriod = savedState.experiencePeriod,
                                experienceDescription = savedState.experienceDescription,
                                hourlyRate = savedState.hourlyRate,
                                availability = savedState.availability,
                                location = savedState.location,
                                promptText = savedState.promptText,
                                isComplete = savedState.isComplete
                            )
                        } else {
                            current
                        }
                    }
                    recalculatePrompt()
                    refreshMockBioIfActive()
                }
            }
            viewModelScope.launch {
                userPreferencesRepository.apiKeyFlow.collect { key ->
                    if (key.isNotBlank()) {
                        _apiKey.value = key
                    } else {
                        _apiKey.value = getInitialApiKey()
                    }
                }
            }
        } else {
            recalculatePrompt()
            useMockBio()
        }
    }

    private fun persistState() {
        val repo = userPreferencesRepository ?: return
        viewModelScope.launch {
            repo.saveProfile(_uiState.value)
        }
    }

    fun updateApiKey(key: String) {
        _apiKey.value = key
        val repo = userPreferencesRepository ?: return
        viewModelScope.launch {
            repo.saveApiKey(key)
        }
    }

    fun toggleApiKeyVisibility() {
        _isApiKeyVisible.value = !_isApiKeyVisible.value
    }

    fun generateBioWithGemini(prompt: String, apiKeyOverride: String) {
        viewModelScope.launch {
            _bioGenerationState.value = BioGenerationState.Loading

            val keyToUse = apiKeyOverride.ifBlank { _apiKey.value }.trim()

            if (keyToUse.isBlank()) {
                delay(600)
                val mockText = geminiBioGenerator.generateMockBio(_uiState.value)
                _bioGenerationState.value = BioGenerationState.Success(
                    bioText = mockText,
                    isMock = true
                )
                return@launch
            }

            try {
                val result = geminiBioGenerator.generateBio(prompt = prompt, apiKey = keyToUse)

                result.fold(
                    onSuccess = { generatedText ->
                        _bioGenerationState.value = BioGenerationState.Success(
                            bioText = generatedText,
                            isMock = false
                        )
                    },
                    onFailure = { throwable ->
                        Log.e("WizardViewModel", "Failed to generate bio with Gemini", throwable)
                        _bioGenerationState.value = BioGenerationState.Error(
                            message = throwable.localizedMessage ?: "Failed to generate bio with Gemini"
                        )
                    }
                )
            } catch (e: Exception) {
                Log.e("WizardViewModel", "Failed to generate bio with Gemini", e)
                _bioGenerationState.value = BioGenerationState.Error(
                    message = e.localizedMessage ?: "Failed to generate bio with Gemini"
                )
            }
        }
    }

    fun useMockBio() {
        val mockText = geminiBioGenerator.generateMockBio(_uiState.value)
        _bioGenerationState.value = BioGenerationState.Success(
            bioText = mockText,
            isMock = true
        )
    }

    fun retryBioGeneration() {
        generateBioWithGemini(_uiState.value.promptText, _apiKey.value)
    }

    fun getFormattedFullProfileText(): String {
        val state = _uiState.value
        val currentBio = when (val bState = _bioGenerationState.value) {
            is BioGenerationState.Success -> bState.bioText
            else -> geminiBioGenerator.generateMockBio(state)
        }

        val name = state.fullName.ifBlank { "Freelancer Name" }
        val title = state.professionalTitle.ifBlank { "Professional Title" }
        val location = state.location.ifBlank { "Remote" }
        val skills = if (state.selectedSkills.isNotEmpty()) state.selectedSkills.joinToString(", ") else "Android Development"
        val expTitle = state.experienceTitle.ifBlank { "Software Engineer" }
        val company = state.experienceCompany.ifBlank { "N/A" }
        val period = if (state.experiencePeriod.isNotBlank()) " (${state.experiencePeriod})" else ""
        val expDesc = state.experienceDescription.ifBlank { "Delivered high impact Android solutions." }

        return """
========================================
FREELANCER PROFILE SUMMARY: $name
========================================

📌 ROLE: $title
📍 LOCATION: $location
💰 RATE: $${state.hourlyRate}/hr (${state.availability})
⏳ EXPERIENCE: ${state.yearsOfExperience}

----------------------------------------
EXECUTIVE SUMMARY & BIO:
----------------------------------------
$currentBio

----------------------------------------
CORE TECH STACK:
----------------------------------------
$skills

----------------------------------------
WORK EXPERIENCE:
----------------------------------------
• Position: $expTitle at $company$period
• Key Achievements & Impact: $expDesc
========================================
        """.trimIndent()
    }

    // Step 1: Basic Info
    fun updateFullName(name: String) {
        _uiState.update { it.copy(fullName = name) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    fun updateProfessionalTitle(title: String) {
        _uiState.update { it.copy(professionalTitle = title) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    fun updateBioOverview(bio: String) {
        _uiState.update { it.copy(bioOverview = bio) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    fun updateLocation(location: String) {
        _uiState.update { it.copy(location = location) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    // Step 2: Skills
    fun updateCustomSkillInput(input: String) {
        _uiState.update { it.copy(customSkillInput = input) }
    }

    fun addCustomSkill() {
        val skill = _uiState.value.customSkillInput.trim()
        if (skill.isNotEmpty() && !_uiState.value.selectedSkills.contains(skill)) {
            _uiState.update {
                it.copy(
                    selectedSkills = it.selectedSkills + skill,
                    customSkillInput = ""
                )
            }
            recalculatePrompt()
            refreshMockBioIfActive()
            persistState()
        }
    }

    fun toggleSkill(skill: String) {
        _uiState.update { state ->
            val updatedSkills = if (state.selectedSkills.contains(skill)) {
                state.selectedSkills - skill
            } else {
                state.selectedSkills + skill
            }
            state.copy(selectedSkills = updatedSkills)
        }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    fun removeSkill(skill: String) {
        _uiState.update { state ->
            state.copy(selectedSkills = state.selectedSkills - skill)
        }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    // Step 3: Work Experience
    fun updateYearsOfExperience(years: String) {
        _uiState.update { it.copy(yearsOfExperience = years) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    fun updateExperienceTitle(title: String) {
        _uiState.update { it.copy(experienceTitle = title) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    fun updateExperienceCompany(company: String) {
        _uiState.update { it.copy(experienceCompany = company) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    fun updateExperiencePeriod(period: String) {
        _uiState.update { it.copy(experiencePeriod = period) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    fun updateExperienceDescription(description: String) {
        _uiState.update { it.copy(experienceDescription = description) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    // Step 4: Rate & Availability
    fun updateHourlyRate(rate: String) {
        val cleanRate = rate.filter { it.isDigit() || it == '.' }
        _uiState.update { it.copy(hourlyRate = cleanRate) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    fun updateAvailability(availability: String) {
        _uiState.update { it.copy(availability = availability) }
        recalculatePrompt()
        refreshMockBioIfActive()
        persistState()
    }

    // Navigation between steps
    fun nextStep() {
        val current = _uiState.value.currentStep
        val entries = WizardStep.entries
        val nextOrdinal = current.ordinal + 1
        if (nextOrdinal < entries.size) {
            _uiState.update { it.copy(currentStep = entries[nextOrdinal]) }
        } else if (current == WizardStep.REVIEW) {
            _uiState.update { it.copy(isComplete = true) }
        }
        persistState()
    }

    fun previousStep() {
        val current = _uiState.value.currentStep
        val entries = WizardStep.entries
        val prevOrdinal = current.ordinal - 1
        if (prevOrdinal >= 0) {
            _uiState.update { it.copy(currentStep = entries[prevOrdinal]) }
        }
        persistState()
    }

    fun goToStep(step: WizardStep) {
        _uiState.update { it.copy(currentStep = step, isComplete = false) }
        persistState()
    }

    fun unlockForEditing(step: WizardStep = WizardStep.BASIC_INFO) {
        _uiState.update { it.copy(isComplete = false, currentStep = step) }
        persistState()
    }

    fun updatePromptText(text: String) {
        _uiState.update { it.copy(promptText = text) }
        persistState()
    }

    fun resetWizard() {
        _uiState.update {
            WizardUiState(
                currentStep = WizardStep.BASIC_INFO,
                selectedSkills = emptyList()
            )
        }
        recalculatePrompt()
        useMockBio()
        persistState()
    }

    fun clearAppData() {
        resetWizard()
        val repo = userPreferencesRepository ?: return
        viewModelScope.launch {
            repo.clearAllData()
        }
    }

    private fun refreshMockBioIfActive() {
        val currentState = _bioGenerationState.value
        if (currentState is BioGenerationState.Success && currentState.isMock) {
            useMockBio()
        }
    }

    private fun recalculatePrompt() {
        val state = _uiState.value
        val name = state.fullName.ifBlank { "[Freelancer Name]" }
        val title = state.professionalTitle.ifBlank { "[Professional Title]" }
        val loc = if (state.location.isNotBlank()) " based in ${state.location}" else ""
        val skillsStr = if (state.selectedSkills.isNotEmpty()) state.selectedSkills.joinToString(", ") else "[Skills not provided]"
        val expTitle = state.experienceTitle.ifBlank { "[Experience Title]" }
        val company = state.experienceCompany.ifBlank { "[Company not provided]" }
        val period = if (state.experiencePeriod.isNotBlank()) " (${state.experiencePeriod})" else ""
        val expDesc = state.experienceDescription.ifBlank { "[Description not provided]" }
        val rate = state.hourlyRate.ifBlank { "50" }
        val audience = "Potential clients looking for a $title"
        val bioOverview = state.bioOverview.ifBlank { "[Overview not provided]" }

        val generated = """
            Write a professional bio based ONLY on the provided role and skills below. Do not default to a software developer unless explicitly stated.
            
            Name: $name$loc
            Role: $title
            Skills: $skillsStr
            Years of Experience: ${state.yearsOfExperience}
            Target Rate: $$rate/hr (${state.availability})
            
            Past Experience:
            - Title: $expTitle
            - Company: $company$period
            - Description: $expDesc
            
            Overview: $bioOverview
            
            Target Audience: $audience
            
            Format requirements: Include a strong hook line, bulleted key achievements based ONLY on the provided past experience, a skills summary, and a clear call-to-action for clients.
        """.trimIndent()

        _uiState.update { it.copy(promptText = generated) }
    }

    companion object {
        private fun getInitialApiKey(): String {
            return try {
                val clazz = Class.forName("freelancerhelper.aab.BuildConfig")
                val field = clazz.getField("GEMINI_API_KEY")
                (field.get(null) as? String) ?: ""
            } catch (e: Throwable) {
                ""
            }
        }
    }
}
