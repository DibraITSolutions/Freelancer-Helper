package freelancerhelper.aab

import freelancerhelper.aab.ui.screens.TipCategory
import freelancerhelper.aab.ui.screens.TipsViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class TipsViewModelTest {

    private lateinit var viewModel: TipsViewModel

    @Before
    fun setUp() {
        viewModel = TipsViewModel()
    }

    @Test
    fun defaultState_containsTipsAndFirstExpanded() {
        val state = viewModel.uiState.value
        assertTrue(state.tips.isNotEmpty())
        assertEquals(TipCategory.ALL, state.selectedCategory)
        assertEquals("tip_proposal_hook_loom", state.expandedTipId)
    }

    @Test
    fun initialList_isOrderedByPrioritizedCategories() {
        val tips = viewModel.uiState.value.tips
        val categoriesInOrder = listOf(
            TipCategory.PROPOSALS,
            TipCategory.PROFILE_HEADLINES,
            TipCategory.TECH_DEV,
            TipCategory.WRITING_TRANSLATION,
            TipCategory.SALES_MARKETING,
            TipCategory.DESIGN_CREATIVE,
            TipCategory.ADMIN_SUPPORT,
            TipCategory.PRICING_ALGORITHM,
            TipCategory.GLOBAL_PLAYBOOK
        )

        var lastIndex = -1
        for (category in categoriesInOrder) {
            val firstIndexOfCategory = tips.indexOfFirst { it.category == category }
            assertTrue("Category $category should be present in tips", firstIndexOfCategory != -1)
            assertTrue("Category $category should appear after previous category", firstIndexOfCategory > lastIndex)
            lastIndex = tips.indexOfLast { it.category == category }
        }
    }

    @Test
    fun selectCategory_filtersTipsCorrectly() {
        viewModel.selectCategory(TipCategory.PROFILE_HEADLINES)

        val state = viewModel.uiState.value
        assertEquals(TipCategory.PROFILE_HEADLINES, state.selectedCategory)
        assertTrue(state.filteredTips.all { it.category == TipCategory.PROFILE_HEADLINES })
    }

    @Test
    fun selectCategory_globalPlaybook_returnsFiveRegionalTips() {
        viewModel.selectCategory(TipCategory.GLOBAL_PLAYBOOK)

        val state = viewModel.uiState.value
        assertEquals(TipCategory.GLOBAL_PLAYBOOK, state.selectedCategory)
        assertEquals(5, state.filteredTips.size)
        assertTrue(state.filteredTips.all { it.category == TipCategory.GLOBAL_PLAYBOOK })

        val tipIds = state.filteredTips.map { it.id }.toSet()
        assertTrue(tipIds.contains("tip_global_eastern_europe"))
        assertTrue(tipIds.contains("tip_global_south_asia"))
        assertTrue(tipIds.contains("tip_global_africa"))
        assertTrue(tipIds.contains("tip_global_latam"))
        assertTrue(tipIds.contains("tip_global_mena"))
    }

    @Test
    fun updateSearchQuery_filtersTipsByQuery() {
        viewModel.updateSearchQuery("Hourly")

        val state = viewModel.uiState.value
        assertEquals("Hourly", state.searchQuery)
        assertTrue(state.filteredTips.isNotEmpty())
        assertTrue(state.filteredTips.all { tip ->
            tip.title.contains("Hourly", ignoreCase = true) ||
                    tip.summary.contains("Hourly", ignoreCase = true) ||
                    tip.detail.contains("Hourly", ignoreCase = true) ||
                    tip.category.displayName.contains("Hourly", ignoreCase = true) ||
                    (tip.profession?.contains("Hourly", ignoreCase = true) == true) ||
                    (tip.formula?.contains("Hourly", ignoreCase = true) == true) ||
                    tip.dos.any { it.contains("Hourly", ignoreCase = true) } ||
                    tip.donts.any { it.contains("Hourly", ignoreCase = true) }
        })
    }

    @Test
    fun updateSearchQuery_regionalKeywords_returnsMatchingTips() {
        val regionalQueries = listOf(
            "Eastern Europe" to "tip_global_eastern_europe",
            "Loom" to "tip_global_south_asia",
            "Africa" to "tip_global_africa",
            "Nearshore" to "tip_global_latam",
            "MENA" to "tip_global_mena"
        )

        for ((query, expectedId) in regionalQueries) {
            viewModel.updateSearchQuery(query)
            val filtered = viewModel.uiState.value.filteredTips
            assertTrue(
                "Query '$query' should match tip '$expectedId'",
                filtered.any { it.id == expectedId }
            )
        }
    }

    @Test
    fun toggleExpand_togglesExpandedTipId() {
        val targetId = "tip_value_pricing_fixed_vs_hourly"
        viewModel.toggleExpand(targetId)

        assertEquals(targetId, viewModel.uiState.value.expandedTipId)

        viewModel.toggleExpand(targetId)
        assertNull(viewModel.uiState.value.expandedTipId)
    }

    @Test
    fun toggleBookmark_togglesBookmarkState() {
        val targetId = "tip_headline_formula"
        val initialBookmarked = viewModel.uiState.value.tips.first { it.id == targetId }.isBookmarked

        viewModel.toggleBookmark(targetId)
        val updatedBookmarked = viewModel.uiState.value.tips.first { it.id == targetId }.isBookmarked

        assertEquals(!initialBookmarked, updatedBookmarked)
    }

    @Test
    fun toggleBookmark_regionalTip_updatesBookmarkStateAndCount() {
        val targetId = "tip_global_eastern_europe"
        val initialCount = viewModel.uiState.value.bookmarkedCount
        assertFalse(viewModel.uiState.value.tips.first { it.id == targetId }.isBookmarked)

        viewModel.toggleBookmark(targetId)

        assertTrue(viewModel.uiState.value.tips.first { it.id == targetId }.isBookmarked)
        assertEquals(initialCount + 1, viewModel.uiState.value.bookmarkedCount)
    }

    @Test
    fun toggleRead_updatesReadCount() {
        val initialReadCount = viewModel.uiState.value.readCount
        val targetId = "tip_headline_formula"

        viewModel.toggleRead(targetId)
        val updatedReadCount = viewModel.uiState.value.readCount

        assertEquals(initialReadCount + 1, updatedReadCount)
    }

    @Test
    fun toggleRead_regionalTip_updatesReadCountAndState() {
        val initialReadCount = viewModel.uiState.value.readCount
        val targetId = "tip_global_latam"

        viewModel.toggleRead(targetId)

        assertTrue(viewModel.uiState.value.tips.first { it.id == targetId }.isRead)
        assertEquals(initialReadCount + 1, viewModel.uiState.value.readCount)
    }
}
