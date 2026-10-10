package freelancerhelper.aab.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import java.time.Instant
import java.time.YearMonth
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class SkillCategory(
    val name: String,
    val skills: List<String>
)

val POPULAR_SKILL_CATEGORIES = listOf(
    SkillCategory(
        name = "Writing & Translation",
        skills = listOf("Copywriting", "Content Writing", "Translation", "Technical Writing", "Proofreading", "SEO Writing")
    ),
    SkillCategory(
        name = "Sales & Marketing",
        skills = listOf("Lead Generation", "Social Media Marketing", "SEO", "Sales Strategy", "Email Marketing", "Cold Calling")
    ),
    SkillCategory(
        name = "Design & Creative",
        skills = listOf("UI/UX Design", "Graphic Design", "Video Editing", "Figma", "Brand Identity")
    ),
    SkillCategory(
        name = "Admin & Support",
        skills = listOf("Virtual Assistant", "Data Entry", "Customer Support", "Project Management")
    ),
    SkillCategory(
        name = "Development & Tech",
        skills = listOf("Mobile App Dev", "Web Development", "Kotlin", "Python", "React", "Cloud Architecture")
    )
)

val POPULAR_SKILLS = POPULAR_SKILL_CATEGORIES.flatMap { it.skills }

val WORLD_TIME_ZONES = listOf(
    "UTC-10:00 (Hawaii - Honolulu)",
    "UTC-09:00 (Alaska - Anchorage, Juneau)",
    "UTC-08:00 (Pacific US/CA - Los Angeles, San Francisco, Seattle, Vancouver, Tijuana)",
    "UTC-07:00 (Mountain US/CA - Denver, Phoenix, Salt Lake City, Calgary, Edmonton)",
    "UTC-06:00 (Central US/CA/MX - Chicago, Houston, Dallas, Mexico City, Guatemala)",
    "UTC-05:00 (Eastern US/CA/SA - New York, Washington D.C., Miami, Toronto, Bogota, Lima)",
    "UTC-04:00 (Atlantic / Caribbean - Halifax, Caracas, La Paz, Santiago, San Juan)",
    "UTC-03:00 (South America - Buenos Aires, São Paulo, Rio de Janeiro, Montevideo)",
    "UTC+00:00 (GMT/UTC - London, Dublin, Lisbon, Casablanca, Accra, Dakar)",
    "UTC+01:00 (Central Europe/Africa - Berlin, Paris, Madrid, Rome, Amsterdam, Warsaw, Prague, Vienna, Lagos, Algiers)",
    "UTC+02:00 (Eastern Europe/Africa - Kyiv, Bucharest, Athens, Helsinki, Sofia, Cairo, Johannesburg, Harare, Jerusalem)",
    "UTC+03:00 (Middle East/East Africa - Riyadh, Jeddah, Doha, Istanbul, Moscow, Nairobi, Addis Ababa)",
    "UTC+03:30 (Iran - Tehran)",
    "UTC+04:00 (Gulf / Caucasus - Dubai, Abu Dhabi, Muscat, Baku, Tbilisi, Yerevan)",
    "UTC+04:30 (Afghanistan - Kabul)",
    "UTC+05:00 (Pakistan / Central Asia - Karachi, Lahore, Islamabad, Tashkent)",
    "UTC+05:30 (India Standard Time - Mumbai, New Delhi, Bengaluru, Hyderabad, Chennai, Kolkata)",
    "UTC+05:45 (Nepal - Kathmandu)",
    "UTC+06:00 (Bangladesh / Central Asia - Dhaka, Chittagong, Almaty, Bishkek)",
    "UTC+06:30 (Myanmar - Yangon)",
    "UTC+07:00 (Southeast Asia - Bangkok, Jakarta, Hanoi, Ho Chi Minh City, Phnom Penh)",
    "UTC+08:00 (China / SE Asia / AU - Singapore, Manila, Hong Kong, Kuala Lumpur, Taipei, Beijing, Shanghai, Perth)",
    "UTC+09:00 (Japan / Korea - Tokyo, Osaka, Seoul, Busan)",
    "UTC+09:30 (Central Australia - Adelaide, Darwin)",
    "UTC+10:00 (Eastern Australia - Sydney, Melbourne, Brisbane, Canberra)",
    "UTC+12:00 (New Zealand & Pacific - Auckland, Wellington, Fiji)"
)

private val EXP_YEARS_OPTIONS = listOf(
    "< 1 year", "1-3 years", "3-5 years", "5-8 years", "8+ years"
)

private val AVAILABILITY_OPTIONS = listOf(
    "Full-time (30+ hrs/week)",
    "Part-time (<30 hrs/week)",
    "As needed / Contract"
)

private val RATE_PRESETS = listOf("30", "45", "65", "85", "120")

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun Modifier.bringIntoViewOnFocus(): Modifier {
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val scope = rememberCoroutineScope()
    return this
        .bringIntoViewRequester(bringIntoViewRequester)
        .onFocusEvent { focusState ->
            if (focusState.isFocused) {
                scope.launch {
                    bringIntoViewRequester.bringIntoView()
                    delay(300)
                    bringIntoViewRequester.bringIntoView()
                }
            }
        }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WizardContent(
    viewModel: WizardViewModel = viewModel(),
    onNavigateToPreview: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val isImeVisible = WindowInsets.isImeVisible

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            .imePadding()
    ) {
        // Step Indicator Banner
        WizardHeader(
            currentStep = uiState.currentStep,
            progress = uiState.progress,
            onStepClick = { step -> viewModel.goToStep(step) }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Scrollable Step Form Area
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                AnimatedVisibility(
                    visible = uiState.isComplete,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        WizardSuccessCard(
                            uiState = uiState,
                            onEditInputs = { viewModel.unlockForEditing(WizardStep.BASIC_INFO) },
                            onReset = { viewModel.resetWizard() },
                            onNavigateToPreview = onNavigateToPreview
                        )
                    }
                }

                if (!uiState.isComplete) {
                    when (uiState.currentStep) {
                        WizardStep.BASIC_INFO -> StepBasicInfo(uiState = uiState, viewModel = viewModel)
                        WizardStep.SKILLS -> StepSkills(uiState = uiState, viewModel = viewModel)
                        WizardStep.EXPERIENCE -> StepExperience(uiState = uiState, viewModel = viewModel)
                        WizardStep.RATE_AVAILABILITY -> StepRateAvailability(uiState = uiState, viewModel = viewModel)
                        WizardStep.REVIEW -> StepReview(
                            uiState = uiState,
                            viewModel = viewModel,
                            onNavigateToPreview = onNavigateToPreview
                        )
                    }
                }
            }
        }

        // Sticky Footer Controls - hidden when soft keyboard is visible
        AnimatedVisibility(
            visible = !uiState.isComplete && !isImeVisible,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                WizardFooter(
                    currentStep = uiState.currentStep,
                    onPrevious = { viewModel.previousStep() },
                    onNext = { viewModel.nextStep() }
                )
            }
        }
    }
}

@Composable
private fun WizardHeader(
    currentStep: WizardStep,
    progress: Float,
    onStepClick: (WizardStep) -> Unit
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Step ${currentStep.stepNumber} of ${WizardStep.entries.size}",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = currentStep.title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = currentStep.subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.outlineVariant
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Step Chips Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WizardStep.entries.forEach { step ->
                    val isSelected = step == currentStep
                    val isPassed = step.stepNumber < currentStep.stepNumber

                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(
                                when {
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    isPassed -> MaterialTheme.colorScheme.primaryContainer
                                    else -> MaterialTheme.colorScheme.surface
                                }
                            )
                            .clickable { onStepClick(step) }
                            .padding(horizontal = 10.dp, vertical = 6.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${step.stepNumber}",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = when {
                                isSelected -> MaterialTheme.colorScheme.onPrimary
                                isPassed -> MaterialTheme.colorScheme.onPrimaryContainer
                                else -> MaterialTheme.colorScheme.onSurface
                            }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StepBasicInfo(
    uiState: WizardUiState,
    viewModel: WizardViewModel
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Personal & Professional Overview",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedTextField(
                    value = uiState.fullName,
                    onValueChange = { viewModel.updateFullName(it) },
                    label = { Text("Full Name") },
                    placeholder = { Text("e.g. Alex Johnson") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewOnFocus(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                OutlinedTextField(
                    value = uiState.professionalTitle,
                    onValueChange = { viewModel.updateProfessionalTitle(it) },
                    label = { Text("Professional Role / Title") },
                    placeholder = { Text("e.g. Senior Android & Kotlin Engineer") },
                    supportingText = { Text("(This becomes your Freelancer Profile Title)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewOnFocus(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = uiState.location,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Location / Timezone") },
                        placeholder = { Text("Select your timezone") },
                        supportingText = { Text("(Used to optimize your profile for local matches)") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                            .bringIntoViewOnFocus(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        WORLD_TIME_ZONES.forEach { timeZone ->
                            DropdownMenuItem(
                                text = { Text(timeZone) },
                                onClick = {
                                    viewModel.updateLocation(timeZone)
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = uiState.bioOverview,
                    onValueChange = { viewModel.updateBioOverview(it) },
                    label = { Text("Overview & Hook Summary") },
                    placeholder = { Text("Describe what problems you solve, your focus area, and client results...") },
                    supportingText = { Text("(This helps the AI write your Freelancer Overview section)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .bringIntoViewOnFocus(),
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StepSkills(
    uiState: WizardUiState,
    viewModel: WizardViewModel
) {
    var selectedCategoryFilter by remember { mutableStateOf("All") }
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Skills & Expertise (${uiState.selectedSkills.size} selected)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "Select high-demand skills across key Freelancer categories or type custom skills below. These will help match your profile in search algorithms.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Custom Skill Entry Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = uiState.customSkillInput,
                        onValueChange = { viewModel.updateCustomSkillInput(it) },
                        label = { Text("Add Custom Skill") },
                        placeholder = { Text("e.g. Copywriting, SEO, GraphQL") },
                        modifier = Modifier
                            .weight(1f)
                            .bringIntoViewOnFocus(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = {
                            viewModel.addCustomSkill()
                            focusManager.clearFocus()
                        })
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { viewModel.addCustomSkill() },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Skill")
                    }
                }

                // Selected Skills Chips
                if (uiState.selectedSkills.isNotEmpty()) {
                    Text(
                        text = "Your Selected Skills:",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.selectedSkills.forEach { skill ->
                            InputChip(
                                selected = true,
                                onClick = { viewModel.removeSkill(skill) },
                                label = { Text(skill) },
                                trailingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove skill",
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Popular Suggestions Section Header
                Text(
                    text = "Popular Skill Suggestions by Freelancer Category:",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // Category Filter Chips
                val filterCategories = listOf("All") + POPULAR_SKILL_CATEGORIES.map { it.name }
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    filterCategories.forEach { categoryName ->
                        val isSelected = selectedCategoryFilter == categoryName
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategoryFilter = categoryName },
                            label = { Text(categoryName, fontSize = 12.sp) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Display skill categories and chip grids
                val displayedCategories = if (selectedCategoryFilter == "All") {
                    POPULAR_SKILL_CATEGORIES
                } else {
                    POPULAR_SKILL_CATEGORIES.filter { it.name == selectedCategoryFilter }
                }

                displayedCategories.forEach { category ->
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${category.skills.count { uiState.selectedSkills.contains(it) }}/${category.skills.size}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            category.skills.forEach { skill ->
                                val isSelected = uiState.selectedSkills.contains(skill)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { viewModel.toggleSkill(skill) },
                                    label = { Text(skill) },
                                    leadingIcon = if (isSelected) {
                                        {
                                            Icon(
                                                imageVector = Icons.Default.Check,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp)
                                            )
                                        }
                                    } else null
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class ParsedExperiencePeriod(
    val fromDate: String,
    val isCurrentlyWorking: Boolean,
    val toDate: String
)

private fun parseExperiencePeriod(period: String): ParsedExperiencePeriod {
    if (period.isBlank()) {
        return ParsedExperiencePeriod(fromDate = "", isCurrentlyWorking = false, toDate = "")
    }
    return if (period.contains(" - ")) {
        val parts = period.split(" - ")
        val from = parts.getOrNull(0)?.trim().orEmpty()
        val to = parts.getOrNull(1)?.trim().orEmpty()
        val isPresent = to.equals("Present", ignoreCase = true)
        ParsedExperiencePeriod(
            fromDate = from,
            isCurrentlyWorking = isPresent,
            toDate = if (isPresent) "" else to
        )
    } else if (period.equals("Present", ignoreCase = true)) {
        ParsedExperiencePeriod(fromDate = "", isCurrentlyWorking = true, toDate = "")
    } else {
        ParsedExperiencePeriod(fromDate = period.trim(), isCurrentlyWorking = false, toDate = "")
    }
}

private fun buildPeriodString(fromDate: String, isCurrentlyWorking: Boolean, toDate: String): String {
    val cleanFrom = fromDate.trim()
    val cleanTo = if (isCurrentlyWorking) "Present" else toDate.trim()
    return when {
        cleanFrom.isNotBlank() && cleanTo.isNotBlank() -> "$cleanFrom - $cleanTo"
        cleanFrom.isNotBlank() -> cleanFrom
        cleanTo.isNotBlank() -> cleanTo
        else -> ""
    }
}

private fun formatMillisToMonthYear(millis: Long): String {
    val instant = Instant.ofEpochMilli(millis)
    val localDate = instant.atZone(ZoneId.of("UTC")).toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.US)
    return localDate.format(formatter)
}

private fun parseMonthYearToMillis(dateStr: String): Long? {
    if (dateStr.isBlank()) return null
    return try {
        val formatter = DateTimeFormatter.ofPattern("MMM yyyy", Locale.US)
        val yearMonth = YearMonth.parse(dateStr, formatter)
        val localDate = yearMonth.atDay(1)
        localDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
    } catch (_: Exception) {
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExperienceDatePickerDialog(
    initialDateStr: String,
    titleText: String,
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val initialMillis = parseMonthYearToMillis(initialDateStr)
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialMillis ?: System.currentTimeMillis()
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val formatted = formatMillisToMonthYear(millis)
                        onDateSelected(formatted)
                    }
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            title = {
                Text(
                    text = titleText,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(start = 24.dp, top = 16.dp)
                )
            }
        )
    }
}

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun StepExperience(
    uiState: WizardUiState,
    viewModel: WizardViewModel
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    val initialParsed = remember(uiState.experiencePeriod) {
        parseExperiencePeriod(uiState.experiencePeriod)
    }
    var localFromDate by remember { mutableStateOf(initialParsed.fromDate) }
    var localIsCurrentlyWorking by remember { mutableStateOf(initialParsed.isCurrentlyWorking) }
    var localToDate by remember { mutableStateOf(initialParsed.toDate) }

    var showFromDatePicker by remember { mutableStateOf(false) }
    var showToDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.experiencePeriod) {
        val currentFormatted = buildPeriodString(localFromDate, localIsCurrentlyWorking, localToDate)
        if (uiState.experiencePeriod != currentFormatted) {
            val p = parseExperiencePeriod(uiState.experiencePeriod)
            localFromDate = p.fromDate
            localIsCurrentlyWorking = p.isCurrentlyWorking
            if (!p.isCurrentlyWorking && p.toDate.isNotBlank()) {
                localToDate = p.toDate
            }
        }
    }

    if (showFromDatePicker) {
        ExperienceDatePickerDialog(
            initialDateStr = localFromDate,
            titleText = "Select Start Date",
            onDateSelected = { selectedDate ->
                localFromDate = selectedDate
                val newPeriod = buildPeriodString(selectedDate, localIsCurrentlyWorking, localToDate)
                viewModel.updateExperiencePeriod(newPeriod)
            },
            onDismiss = { showFromDatePicker = false }
        )
    }

    if (showToDatePicker) {
        ExperienceDatePickerDialog(
            initialDateStr = localToDate,
            titleText = "Select End Date",
            onDateSelected = { selectedDate ->
                localToDate = selectedDate
                val newPeriod = buildPeriodString(localFromDate, localIsCurrentlyWorking, selectedDate)
                viewModel.updateExperiencePeriod(newPeriod)
            },
            onDismiss = { showToDatePicker = false }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Step 3: Employment History 💼",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Text(
                    text = "Years of Experience:",
                    style = MaterialTheme.typography.labelLarge
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    EXP_YEARS_OPTIONS.forEach { option ->
                        val isSelected = uiState.yearsOfExperience == option
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.updateYearsOfExperience(option) },
                            label = { Text(option, fontSize = 12.sp) },
                            leadingIcon = if (isSelected) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            } else null
                        )
                    }
                }

                OutlinedTextField(
                    value = uiState.experienceTitle,
                    onValueChange = { viewModel.updateExperienceTitle(it) },
                    label = { Text("Recent Job Title") },
                    placeholder = { Text("e.g., Senior Content Writer or Full Stack Developer") },
                    supportingText = { Text("Freelancer Field: Title (e.g., Senior Content Writer or Full Stack Developer)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewOnFocus(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                OutlinedTextField(
                    value = uiState.experienceCompany,
                    onValueChange = { viewModel.updateExperienceCompany(it) },
                    label = { Text("Company / Client") },
                    placeholder = { Text("e.g., Acme Inc. or Freelance") },
                    supportingText = { Text("Freelancer Field: Company (e.g., Acme Inc. or Freelance)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewOnFocus(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                    keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) })
                )

                // Structured Date Selection Section
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Time Period",
                        style = MaterialTheme.typography.labelLarge
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Start Date (From)
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { showFromDatePicker = true }
                        ) {
                            OutlinedTextField(
                                value = localFromDate,
                                onValueChange = {},
                                readOnly = true,
                                enabled = false,
                                label = { Text("Start Date (From)") },
                                placeholder = { Text("e.g., Jan 2022") },
                                trailingIcon = {
                                    IconButton(onClick = { showFromDatePicker = true }) {
                                        Icon(
                                            imageVector = Icons.Default.CalendarToday,
                                            contentDescription = "Select Start Date",
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                    disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    disabledBorderColor = MaterialTheme.colorScheme.outline,
                                    disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
                                    disabledContainerColor = MaterialTheme.colorScheme.surface
                                ),
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }

                        // End Date (To) - shown if !localIsCurrentlyWorking
                        if (!localIsCurrentlyWorking) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { showToDatePicker = true }
                            ) {
                                OutlinedTextField(
                                    value = localToDate,
                                    onValueChange = {},
                                    readOnly = true,
                                    enabled = false,
                                    label = { Text("End Date (To)") },
                                    placeholder = { Text("e.g., Dec 2023") },
                                    trailingIcon = {
                                        IconButton(onClick = { showToDatePicker = true }) {
                                            Icon(
                                                imageVector = Icons.Default.CalendarToday,
                                                contentDescription = "Select End Date",
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                                        disabledPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                        disabledTrailingIconColor = MaterialTheme.colorScheme.primary,
                                        disabledContainerColor = MaterialTheme.colorScheme.surface
                                    ),
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true
                                )
                            }
                        }
                    }

                    // Checkbox for "I currently work here"
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val newWorking = !localIsCurrentlyWorking
                                localIsCurrentlyWorking = newWorking
                                val newPeriod = buildPeriodString(localFromDate, newWorking, localToDate)
                                viewModel.updateExperiencePeriod(newPeriod)
                            }
                            .padding(vertical = 4.dp)
                    ) {
                        Checkbox(
                            checked = localIsCurrentlyWorking,
                            onCheckedChange = { checked ->
                                localIsCurrentlyWorking = checked
                                val newPeriod = buildPeriodString(localFromDate, checked, localToDate)
                                viewModel.updateExperiencePeriod(newPeriod)
                            }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "I currently work here",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Text(
                        text = if (uiState.experiencePeriod.isNotBlank()) {
                            "Freelancer Field: Period (${uiState.experiencePeriod})"
                        } else {
                            "Freelancer Field: Period (e.g., Jan 2022 - Present)"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                OutlinedTextField(
                    value = uiState.experienceDescription,
                    onValueChange = { viewModel.updateExperienceDescription(it) },
                    label = { Text("Responsibilities / Key Accomplishments") },
                    placeholder = { Text("Bullet points or paragraph of what you achieved...") },
                    supportingText = { Text("Freelancer Field: Description (Bullet points or paragraph of what you achieved)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .bringIntoViewOnFocus(),
                    maxLines = 5,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StepRateAvailability(
    uiState: WizardUiState,
    viewModel: WizardViewModel
) {
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AttachMoney,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Target Hourly Rate & Weekly Schedule",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                OutlinedTextField(
                    value = uiState.hourlyRate,
                    onValueChange = { viewModel.updateHourlyRate(it) },
                    label = { Text("Target Hourly Rate ($/hr)") },
                    supportingText = { Text("(This will be your visible Hourly Rate on your Freelancer profile)") },
                    leadingIcon = {
                        Text(
                            text = "$",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .bringIntoViewOnFocus(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )

                // Preset Rates
                Text(
                    text = "Quick Presets:",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RATE_PRESETS.forEach { rate ->
                        val isSelected = uiState.hourlyRate == rate
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.updateHourlyRate(rate) },
                            label = { Text("$$rate/hr") }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Weekly Availability:",
                    style = MaterialTheme.typography.labelLarge
                )

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AVAILABILITY_OPTIONS.forEach { option ->
                        val isSelected = uiState.availability == option
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .border(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { viewModel.updateAvailability(option) },
                            color = if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surface
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = if (isSelected) Icons.Default.CheckCircle else Icons.Default.Info,
                                    contentDescription = null,
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Suppress("UnusedPrivateMember")
@Composable
private fun StepRate(
    uiState: WizardUiState,
    viewModel: WizardViewModel
) = StepRateAvailability(uiState = uiState, viewModel = viewModel)

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun StepReview(
    uiState: WizardUiState,
    viewModel: WizardViewModel,
    onNavigateToPreview: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // High-visibility Navigation Action Banner to Preview
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = "Ready to View Full Live Profile?",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Text(
                    text = "Your profile details and AI prompt are saved and ready. Tap below to navigate directly to the complete Profile Preview.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
                )

                Button(
                    onClick = onNavigateToPreview,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Go to Profile Preview 🚀",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Summary Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Profile Summary",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(onClick = { viewModel.goToStep(WizardStep.BASIC_INFO) }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Profile")
                    }
                }

                ReviewItem(
                    label = "Name & Role",
                    value = "${uiState.fullName.ifBlank { "Not set" }} — ${uiState.professionalTitle.ifBlank { "Not set" }}"
                )

                ReviewItem(
                    label = "Location",
                    value = uiState.location.ifBlank { "Not set" }
                )

                ReviewItem(
                    label = "Employment History",
                    value = "${uiState.yearsOfExperience} | ${uiState.experienceTitle.ifBlank { "Job Title Not Set" }} at ${uiState.experienceCompany.ifBlank { "Company Not Set" }}${if (uiState.experiencePeriod.isNotBlank()) " (${uiState.experiencePeriod})" else ""}"
                )

                ReviewItem(
                    label = "Hourly Rate & Schedule",
                    value = "$${uiState.hourlyRate}/hr (${uiState.availability})"
                )

                Text(
                    text = "Selected Stack (${uiState.selectedSkills.size}):",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    uiState.selectedSkills.forEach { skill ->
                        AssistChip(
                            onClick = { },
                            label = { Text(skill, fontSize = 11.sp) }
                        )
                    }
                }
            }
        }

        // AI Prompt Card
        ElevatedCard(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Generated AI Profile Prompt",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = {
                            clipboardManager.setText(AnnotatedString(uiState.promptText))
                            Toast.makeText(context, "Prompt copied to clipboard!", Toast.LENGTH_SHORT).show()
                        },
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Copy", fontSize = 12.sp)
                    }
                }

                OutlinedTextField(
                    value = uiState.promptText,
                    onValueChange = { viewModel.updatePromptText(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .bringIntoViewOnFocus(),
                    textStyle = MaterialTheme.typography.bodySmall,
                    maxLines = 8,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() })
                )
            }
        }
    }
}

@Composable
private fun ReviewItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun WizardSuccessCard(
    uiState: WizardUiState,
    onEditInputs: () -> Unit,
    onReset: () -> Unit,
    onNavigateToPreview: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Profile Details Saved!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )

            Text(
                text = "Congratulations ${uiState.fullName.ifBlank { "Freelancer" }}! Your details have been formatted and saved into your ViewModel state and persistent DataStore repository. You can now preview your full profile, generate custom bios, or copy your formatted text.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.9f)
            )

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateToPreview,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Go to Profile Preview 🚀",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onEditInputs,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Reset & Edit Inputs",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onReset,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.height(48.dp)
                ) {
                    Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Clear All")
                }
            }
        }
    }
}

@Composable
private fun WizardFooter(
    currentStep: WizardStep,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (currentStep != WizardStep.BASIC_INFO) {
            OutlinedButton(
                onClick = onPrevious,
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier.height(38.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Previous",
                    style = MaterialTheme.typography.labelMedium
                )
            }
        } else {
            Spacer(modifier = Modifier.width(1.dp))
        }

        Button(
            onClick = onNext,
            shape = RoundedCornerShape(10.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp),
            modifier = Modifier.height(38.dp)
        ) {
            Text(
                text = if (currentStep == WizardStep.REVIEW) "Complete Profile" else "Next Step",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = if (currentStep == WizardStep.REVIEW) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun WizardContentPreview() {
    MaterialTheme {
        WizardContent()
    }
}
