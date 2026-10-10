package freelancerhelper.aab

import freelancerhelper.aab.ui.screens.BioGenerationState
import freelancerhelper.aab.ui.screens.POPULAR_SKILLS
import freelancerhelper.aab.ui.screens.POPULAR_SKILL_CATEGORIES
import freelancerhelper.aab.ui.screens.WORLD_TIME_ZONES
import freelancerhelper.aab.ui.screens.WizardStep
import freelancerhelper.aab.ui.screens.WizardViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class WizardViewModelTest {

    private lateinit var viewModel: WizardViewModel

    @Before
    fun setUp() {
        viewModel = WizardViewModel()
    }

    @Test
    fun updateFullName_updatesStateAndPrompt() {
        viewModel.updateFullName("John Doe")

        val state = viewModel.uiState.value
        assertEquals("John Doe", state.fullName)
        assertTrue(state.promptText.contains("John Doe"))
    }

    @Test
    fun updateProfessionalTitle_updatesStateAndPrompt() {
        viewModel.updateProfessionalTitle("Senior Android Engineer")

        val state = viewModel.uiState.value
        assertEquals("Senior Android Engineer", state.professionalTitle)
        assertTrue(state.promptText.contains("Senior Android Engineer"))
    }

    @Test
    fun updateLocation_withWorldTimeZone_updatesStateAndPrompt() {
        val selectedZone = WORLD_TIME_ZONES.first()
        viewModel.updateLocation(selectedZone)

        val state = viewModel.uiState.value
        assertEquals(selectedZone, state.location)
        assertTrue(state.promptText.contains(selectedZone))
    }

    @Test
    fun updateEmploymentHistoryFields_updatesStateAndPrompt() {
        viewModel.updateYearsOfExperience("5-8 years")
        viewModel.updateExperienceTitle("Lead Content Writer")
        viewModel.updateExperienceCompany("Acme Media")
        viewModel.updateExperiencePeriod("Jan 2021 - Present")
        viewModel.updateExperienceDescription("Authored over 100 high-converting tech articles.")

        val state = viewModel.uiState.value
        assertEquals("5-8 years", state.yearsOfExperience)
        assertEquals("Lead Content Writer", state.experienceTitle)
        assertEquals("Acme Media", state.experienceCompany)
        assertEquals("Jan 2021 - Present", state.experiencePeriod)
        assertEquals("Authored over 100 high-converting tech articles.", state.experienceDescription)

        assertTrue(state.promptText.contains("5-8 years"))
        assertTrue(state.promptText.contains("Lead Content Writer"))
        assertTrue(state.promptText.contains("Acme Media"))
        assertTrue(state.promptText.contains("Jan 2021 - Present"))
    }

    @Test
    fun addCustomSkill_addsSkillToSelectedSkillsAndClearsInput() {
        viewModel.updateCustomSkillInput("GraphQL")
        viewModel.addCustomSkill()

        val state = viewModel.uiState.value
        assertTrue(state.selectedSkills.contains("GraphQL"))
        assertEquals("", state.customSkillInput)
        assertTrue(state.promptText.contains("GraphQL"))
    }

    @Test
    fun toggleSkill_removesIfPresentAndAddsIfNot() {
        val targetSkill = "Kotlin"
        assertFalse(viewModel.uiState.value.selectedSkills.contains(targetSkill))

        viewModel.toggleSkill(targetSkill)
        assertTrue(viewModel.uiState.value.selectedSkills.contains(targetSkill))

        viewModel.toggleSkill(targetSkill)
        assertFalse(viewModel.uiState.value.selectedSkills.contains(targetSkill))
    }

    @Test
    fun updateHourlyRate_cleansNonNumericExceptDecimal() {
        viewModel.updateHourlyRate("75")

        val state = viewModel.uiState.value
        assertEquals("75", state.hourlyRate)
        assertTrue(state.promptText.contains("$75/hr"))
    }

    @Test
    fun navigationBetweenSteps_worksCorrectly() {
        assertEquals(WizardStep.BASIC_INFO, viewModel.uiState.value.currentStep)

        viewModel.nextStep()
        assertEquals(WizardStep.SKILLS, viewModel.uiState.value.currentStep)

        viewModel.nextStep()
        assertEquals(WizardStep.EXPERIENCE, viewModel.uiState.value.currentStep)

        viewModel.previousStep()
        assertEquals(WizardStep.SKILLS, viewModel.uiState.value.currentStep)

        viewModel.goToStep(WizardStep.REVIEW)
        assertEquals(WizardStep.REVIEW, viewModel.uiState.value.currentStep)

        viewModel.nextStep()
        assertTrue(viewModel.uiState.value.isComplete)
    }

    @Test
    fun resetWizard_resetsToDefaultState() {
        viewModel.updateFullName("Jane Doe")
        viewModel.toggleSkill("Kotlin")
        viewModel.nextStep()
        viewModel.resetWizard()

        val state = viewModel.uiState.value
        assertEquals("", state.fullName)
        assertTrue(state.selectedSkills.isEmpty())
        assertEquals(WizardStep.BASIC_INFO, state.currentStep)
        assertFalse(state.isComplete)
    }

    @Test
    fun updateApiKey_updatesState() {
        viewModel.updateApiKey("test_api_key_12345")
        assertEquals("test_api_key_12345", viewModel.apiKey.value)
    }

    @Test
    fun toggleApiKeyVisibility_togglesBooleanState() {
        assertFalse(viewModel.isApiKeyVisible.value)
        viewModel.toggleApiKeyVisibility()
        assertTrue(viewModel.isApiKeyVisible.value)
        viewModel.toggleApiKeyVisibility()
        assertFalse(viewModel.isApiKeyVisible.value)
    }

    @Test
    fun useMockBio_setsSuccessBioStateWithMockTrue() {
        viewModel.updateFullName("Alex Rivera")
        viewModel.updateProfessionalTitle("Lead Kotlin Architect")
        viewModel.useMockBio()

        val bioState = viewModel.bioGenerationState.value
        assertTrue(bioState is BioGenerationState.Success)
        val successState = bioState as BioGenerationState.Success
        assertTrue(successState.isMock)
        assertTrue(successState.bioText.contains("Alex Rivera"))
        assertTrue(successState.bioText.contains("Lead Kotlin Architect"))
    }

    @Test
    fun generateBio_withBlankKey_fallsBackToMockBio() {
        viewModel.updateApiKey("")
        viewModel.generateBioWithGemini(prompt = "Test prompt", apiKeyOverride = "")

        val bioState = viewModel.bioGenerationState.value
        assertTrue(bioState is BioGenerationState.Success)
        val successState = bioState as BioGenerationState.Success
        assertTrue(successState.isMock)
    }

    @Test
    fun getFormattedFullProfileText_returnsCompleteFormattedString() {
        viewModel.updateFullName("Sarah Connor")
        viewModel.updateProfessionalTitle("Android Security Specialist")
        viewModel.updateHourlyRate("95")
        viewModel.updateLocation("Austin, TX")

        val fullText = viewModel.getFormattedFullProfileText()

        assertNotNull(fullText)
        assertTrue(fullText.contains("Sarah Connor"))
        assertTrue(fullText.contains("Android Security Specialist"))
        assertTrue(fullText.contains("$95/hr"))
        assertTrue(fullText.contains("Austin, TX"))
        assertTrue(fullText.contains("FREELANCER PROFILE SUMMARY"))
    }

    @Test
    fun popularSkillCategories_containsAllFreelancerCategoriesAndSkills() {
        val categoryNames = POPULAR_SKILL_CATEGORIES.map { it.name }
        assertTrue(categoryNames.contains("Writing & Translation"))
        assertTrue(categoryNames.contains("Sales & Marketing"))
        assertTrue(categoryNames.contains("Design & Creative"))
        assertTrue(categoryNames.contains("Admin & Support"))
        assertTrue(categoryNames.contains("Development & Tech"))

        assertTrue(POPULAR_SKILLS.contains("Copywriting"))
        assertTrue(POPULAR_SKILLS.contains("Lead Generation"))
        assertTrue(POPULAR_SKILLS.contains("UI/UX Design"))
        assertTrue(POPULAR_SKILLS.contains("Virtual Assistant"))
        assertTrue(POPULAR_SKILLS.contains("Mobile App Dev"))
    }

    @Test
    fun toggleCategorySkills_updatesSelectedSkillsInState() {
        val testSkills = listOf("Copywriting", "SEO", "Graphic Design", "Customer Support", "Cloud Architecture")

        testSkills.forEach { skill ->
            viewModel.toggleSkill(skill)
        }

        val state = viewModel.uiState.value
        testSkills.forEach { skill ->
            assertTrue(state.selectedSkills.contains(skill))
        }
    }

    @Test
    fun unlockForEditing_resetsIsCompleteAndSetsCurrentStep() {
        viewModel.goToStep(WizardStep.REVIEW)
        viewModel.nextStep() // Complete profile
        assertTrue(viewModel.uiState.value.isComplete)

        viewModel.unlockForEditing(WizardStep.BASIC_INFO)
        assertFalse(viewModel.uiState.value.isComplete)
        assertEquals(WizardStep.BASIC_INFO, viewModel.uiState.value.currentStep)
    }

    @Test
    fun clearAppData_resetsWizardStateToDefaults() {
        viewModel.updateFullName("John Doe")
        viewModel.toggleSkill("Kotlin")
        viewModel.clearAppData()

        val state = viewModel.uiState.value
        assertEquals("", state.fullName)
        assertTrue(state.selectedSkills.isEmpty())
        assertFalse(state.isComplete)
    }
}
