package freelancerhelper.aab.data

import freelancerhelper.aab.ui.screens.WizardUiState
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiBioGenerator {

    suspend fun generateBio(
        prompt: String,
        apiKey: String,
        modelName: String = "gemini-3.8-flash"
    ): Result<String> = withContext(Dispatchers.IO) {
        val key = apiKey.trim()
        if (key.isBlank()) {
            return@withContext Result.failure(IllegalArgumentException("Gemini API key is not configured."))
        }

        val candidateModels = listOf(
            modelName,
            "gemini-3.5-flash-lite"
        ).distinct()

        var lastException: Exception? = null

        for (model in candidateModels) {
            try {
                val generativeModel = GenerativeModel(
                    modelName = model,
                    apiKey = key
                )
                val response = generativeModel.generateContent(prompt)
                val text = response.text
                if (!text.isNullOrBlank()) {
                    return@withContext Result.success(text)
                } else {
                    lastException = Exception("Gemini model '$model' returned an empty response.")
                }
            } catch (e: Exception) {
                val errorMsg = e.localizedMessage ?: e.message ?: "API call failed for model '$model'"
                lastException = Exception("Failed to generate bio with model '$model': $errorMsg", e)
            }
        }

        Result.failure(
            lastException ?: Exception("Failed to generate bio with Gemini API across all attempted models.")
        )
    }

    fun generateMockBio(uiState: WizardUiState): String {
        val name = uiState.fullName.ifBlank { "[Freelancer Name]" }
        val title = uiState.professionalTitle.ifBlank { "[Professional Title]" }
        val skills = if (uiState.selectedSkills.isNotEmpty()) {
            uiState.selectedSkills.joinToString(", ")
        } else {
            "[Skills]"
        }
        val expTitle = uiState.experienceTitle.ifBlank { "[Experience Title]" }
        val company = if (uiState.experienceCompany.isNotBlank()) " at ${uiState.experienceCompany}" else ""

        return """
👋 (MOCK BIO) Hi, I'm $name, a professional $title, specializing in $skills. I have experience working as a $expTitle$company.

(Please configure your Gemini API Key in the Preview tab to generate a real AI bio!)
        """.trimIndent()
    }
}
