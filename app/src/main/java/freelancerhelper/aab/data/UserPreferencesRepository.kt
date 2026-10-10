package freelancerhelper.aab.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import freelancerhelper.aab.BuildConfig
import freelancerhelper.aab.ui.screens.WizardUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository(private val context: Context) {

    private val geminiKey = stringPreferencesKey("gemini_api_key")
    private val fullNameKey = stringPreferencesKey("full_name")
    private val professionalTitleKey = stringPreferencesKey("professional_title")
    private val bioOverviewKey = stringPreferencesKey("bio_overview")
    private val selectedSkillsKey = stringSetPreferencesKey("selected_skills")
    private val yearsOfExperienceKey = stringPreferencesKey("years_of_experience")
    private val experienceTitleKey = stringPreferencesKey("experience_title")
    private val experienceCompanyKey = stringPreferencesKey("experience_company")
    private val experiencePeriodKey = stringPreferencesKey("experience_period")
    private val experienceDescriptionKey = stringPreferencesKey("experience_description")
    private val hourlyRateKey = stringPreferencesKey("hourly_rate")
    private val availabilityKey = stringPreferencesKey("availability")
    private val locationKey = stringPreferencesKey("location")
    private val promptTextKey = stringPreferencesKey("prompt_text")
    private val isCompleteKey = booleanPreferencesKey("is_complete")

    val apiKeyFlow: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[geminiKey]?.takeIf { it.isNotBlank() }
                ?: BuildConfig.GEMINI_API_KEY
        }

    val profileFlow: Flow<WizardUiState> = context.dataStore.data
        .map { preferences ->
            WizardUiState(
                fullName = preferences[fullNameKey] ?: "",
                professionalTitle = preferences[professionalTitleKey] ?: "",
                bioOverview = preferences[bioOverviewKey] ?: "",
                selectedSkills = preferences[selectedSkillsKey]?.toList() ?: emptyList(),
                yearsOfExperience = preferences[yearsOfExperienceKey] ?: "3-5 years",
                experienceTitle = preferences[experienceTitleKey] ?: "",
                experienceCompany = preferences[experienceCompanyKey] ?: "",
                experiencePeriod = preferences[experiencePeriodKey] ?: "",
                experienceDescription = preferences[experienceDescriptionKey] ?: "",
                hourlyRate = preferences[hourlyRateKey] ?: "55",
                availability = preferences[availabilityKey] ?: "Full-time (30+ hrs/week)",
                location = preferences[locationKey] ?: "",
                promptText = preferences[promptTextKey] ?: "",
                isComplete = preferences[isCompleteKey] ?: false
            )
        }

    suspend fun saveProfile(uiState: WizardUiState) {
        context.dataStore.edit { preferences ->
            preferences[fullNameKey] = uiState.fullName
            preferences[professionalTitleKey] = uiState.professionalTitle
            preferences[bioOverviewKey] = uiState.bioOverview
            preferences[selectedSkillsKey] = uiState.selectedSkills.toSet()
            preferences[yearsOfExperienceKey] = uiState.yearsOfExperience
            preferences[experienceTitleKey] = uiState.experienceTitle
            preferences[experienceCompanyKey] = uiState.experienceCompany
            preferences[experiencePeriodKey] = uiState.experiencePeriod
            preferences[experienceDescriptionKey] = uiState.experienceDescription
            preferences[hourlyRateKey] = uiState.hourlyRate
            preferences[availabilityKey] = uiState.availability
            preferences[locationKey] = uiState.location
            preferences[promptTextKey] = uiState.promptText
            preferences[isCompleteKey] = uiState.isComplete
        }
    }

    suspend fun saveApiKey(key: String) {
        context.dataStore.edit { preferences ->
            preferences[geminiKey] = key.trim()
        }
    }

    suspend fun clearApiKey() {
        context.dataStore.edit { preferences ->
            preferences.remove(geminiKey)
        }
    }

    suspend fun clearAllData() {
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}
