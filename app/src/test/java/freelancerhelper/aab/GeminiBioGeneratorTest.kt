package freelancerhelper.aab

import freelancerhelper.aab.data.GeminiBioGenerator
import freelancerhelper.aab.ui.screens.WizardUiState
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GeminiBioGeneratorTest {

    private lateinit var generator: GeminiBioGenerator

    @Before
    fun setUp() {
        generator = GeminiBioGenerator()
    }

    @Test
    fun generateBio_blankApiKey_returnsFailure() = runBlocking {
        val result = generator.generateBio(prompt = "Test prompt", apiKey = "   ")
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception is IllegalArgumentException)
        assertEquals("Gemini API key is not configured.", exception?.message)
    }

    @Test
    fun generateBio_invalidApiKey_returnsFailure() = runBlocking {
        val result = generator.generateBio(prompt = "Test prompt", apiKey = "INVALID_KEY_123")
        assertTrue(result.isFailure)
        val exception = result.exceptionOrNull()
        assertTrue(exception != null)
    }

    @Test
    fun generateMockBio_formatsUserInformation() {
        val uiState = WizardUiState(
            fullName = "Jane Doe",
            professionalTitle = "UX Designer",
            selectedSkills = listOf("Figma", "User Research")
        )

        val mockBio = generator.generateMockBio(uiState)

        assertTrue(mockBio.contains("Jane Doe"))
        assertTrue(mockBio.contains("UX Designer"))
        assertTrue(mockBio.contains("Figma, User Research"))
    }
}
