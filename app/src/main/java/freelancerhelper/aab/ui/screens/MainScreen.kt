package freelancerhelper.aab.ui.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import android.app.Activity
import freelancerhelper.aab.data.AdManager
import freelancerhelper.aab.data.UserPreferencesRepository
import freelancerhelper.aab.ui.navigation.Navigator
import freelancerhelper.aab.ui.navigation.rememberNavigationState
import freelancerhelper.aab.ui.navigation.toEntries

private sealed interface TopLevelRoute {
    val key: NavKey
    val icon: ImageVector
    val title: String
}

private data object IntroRoute : TopLevelRoute {
    override val key = IntroScreen
    override val icon = Icons.Default.RocketLaunch
    override val title = "Intro"
}

private data object WizardRoute : TopLevelRoute {
    override val key = WizardScreen
    override val icon = Icons.Default.Create
    override val title = "Wizard"
}

private data object TipsRoute : TopLevelRoute {
    override val key = TipsScreen
    override val icon = Icons.Default.Lightbulb
    override val title = "Tips"
}

private data object PreviewRoute : TopLevelRoute {
    override val key = PreviewScreen
    override val icon = Icons.Default.PlayArrow
    override val title = "Preview"
}

private data object AboutRoute : TopLevelRoute {
    override val key = AboutScreen
    override val icon = Icons.Outlined.Info
    override val title = "About"
}

private val TOP_LEVEL_ROUTES = listOf(IntroRoute, WizardRoute, TipsRoute, PreviewRoute, AboutRoute)

@Composable
fun MainScreen() {
    val context = LocalContext.current
    val repository = remember(context) { UserPreferencesRepository(context.applicationContext) }
    val wizardViewModel: WizardViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return WizardViewModel(userPreferencesRepository = repository) as T
            }
        }
    )

    val topLevelKeys = TOP_LEVEL_ROUTES.map { it.key }.toSet()
    val navigationState = rememberNavigationState(
        startRoute = IntroScreen,
        topLevelRoutes = topLevelKeys
    )
    val navigator = remember { Navigator(navigationState) }

    val entryProvider = entryProvider {
        entry<IntroScreen> {
            IntroContent(
                onNavigateToWizard = { navigator.navigate(WizardScreen) },
                onNavigateToTips = { navigator.navigate(TipsScreen) },
                onNavigateToPreview = { navigator.navigate(PreviewScreen) }
            )
        }
        entry<WizardScreen> {
            WizardContent(
                viewModel = wizardViewModel,
                onNavigateToPreview = {
                    AdManager.showInterstitialAd(context as? Activity) {
                        navigator.navigate(PreviewScreen)
                    }
                }
            )
        }
        entry<TipsScreen> {
            TipsContent()
        }
        entry<PreviewScreen> {
            PreviewContent(
                viewModel = wizardViewModel,
                onNavigateToWizard = { navigator.navigate(WizardScreen) }
            )
        }
        entry<AboutScreen> {
            AboutContent(
                viewModel = wizardViewModel,
                onNavigateToIntro = { navigator.navigate(IntroScreen) }
            )
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        color = MaterialTheme.colorScheme.background
    ) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                TOP_LEVEL_ROUTES.forEach { route ->
                    item(
                        selected = route.key == navigationState.topLevelRoute,
                        onClick = { navigator.navigate(route.key) },
                        icon = { Icon(imageVector = route.icon, contentDescription = route.title) },
                        label = { Text(route.title) }
                    )
                }
            }
        ) {
            NavDisplay(
                entries = navigationState.toEntries(entryProvider = entryProvider),
                onBack = { navigator.goBack() }
            )
        }
    }
}
