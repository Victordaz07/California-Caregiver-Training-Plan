package com.caregiverproca.app.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.caregiverproca.app.content.PathwayId
import com.caregiverproca.app.content.pathwayById
import com.caregiverproca.app.data.UserPreferencesRepository
import com.caregiverproca.app.ui.screens.AnalizadorVocacionalScreen
import com.caregiverproca.app.ui.screens.AudioLeccionesScreen
import com.caregiverproca.app.ui.screens.BibliotecaAudiosScreen
import com.caregiverproca.app.ui.screens.EscenariosFlashcardsScreen
import com.caregiverproca.app.ui.screens.Plan90DiasScreen
import com.caregiverproca.app.ui.screens.RecursosSemanaScreen
import com.caregiverproca.app.ui.screens.RequisitosCertificacionScreen
import com.caregiverproca.app.ui.screens.RolePlayBilingueScreen
import com.caregiverproca.app.ui.screens.RutinaDiariaScreen
import com.caregiverproca.app.ui.screens.SimulacionTurnoScreen
import com.caregiverproca.app.ui.screens.hub.AudioHubScreen
import com.caregiverproca.app.ui.screens.hub.CareerHubScreen
import com.caregiverproca.app.ui.screens.hub.PracticeHubScreen
import com.caregiverproca.app.ui.screens.onboarding.PathwaySelectionScreen
import com.caregiverproca.app.ui.screens.onboarding.WelcomeScreen
import kotlinx.coroutines.launch

private const val RouteAppEntry = "app_entry"
private const val RouteOnboardingWelcome = "onboarding_welcome"
private const val RouteOnboardingPathway = "onboarding_pathway"

/**
 * Root navigation graph: app-entry loading state -> onboarding (welcome +
 * pathway, only if no pathway is stored yet) -> the five-tab shell defined by
 * [MainTab], with the eight [Screen] detail screens pushed on top of the
 * relevant tab. Bottom navigation is shown only on the five hub routes — it
 * intentionally disappears on pushed detail screens, which already have a
 * back arrow via DetailScaffold.
 *
 * Persistence: [UserPreferencesRepository] (DataStore) is the real, but
 * interim, source of truth for which pathway is selected — see
 * docs/caregiver_upgrade/DECISIONS.md for why this isn't Room yet.
 */
@Composable
fun CaregiverNavHost() {
    val context = LocalContext.current
    val repository = remember { UserPreferencesRepository(context) }
    val scope = rememberCoroutineScope()
    val prefs by repository.state.collectAsState(initial = null)

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentTab = MainTab.entries.find { it.route == backStackEntry?.destination?.route }

    Scaffold(
        bottomBar = {
            if (currentTab != null) {
                NavigationBar {
                    MainTab.entries.forEach { tab ->
                        NavigationBarItem(
                            selected = tab == currentTab,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = null) },
                            label = { Text(tab.label) },
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RouteAppEntry,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(RouteAppEntry) {
                val loadedPrefs = prefs
                LaunchedEffect(loadedPrefs) {
                    if (loadedPrefs != null) {
                        val destination = if (loadedPrefs.onboardingCompleted) MainTab.Today.route else RouteOnboardingWelcome
                        navController.navigate(destination) {
                            popUpTo(RouteAppEntry) { inclusive = true }
                        }
                    }
                }
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            composable(RouteOnboardingWelcome) {
                WelcomeScreen(onContinue = { navController.navigate(RouteOnboardingPathway) })
            }

            composable(RouteOnboardingPathway) {
                PathwaySelectionScreen(
                    onSelect = { pathwayId ->
                        scope.launch {
                            repository.setPathway(pathwayId)
                            navController.navigate(MainTab.Today.route) {
                                popUpTo(RouteAppEntry) { inclusive = true }
                            }
                        }
                    },
                )
            }

            composable(MainTab.Today.route) { RutinaDiariaScreen() }
            composable(MainTab.Plan.route) { Plan90DiasScreen() }

            composable(MainTab.Practice.route) {
                PracticeHubScreen(onOpenScreen = { screen -> navController.navigate(screen.route) })
            }
            composable(MainTab.Audio.route) {
                AudioHubScreen(onOpenScreen = { screen -> navController.navigate(screen.route) })
            }
            composable(MainTab.Career.route) {
                val pathwayId = prefs?.pathwayId ?: PathwayId.FamilyCaregiver
                CareerHubScreen(
                    pathway = pathwayById(pathwayId),
                    onChangePathway = { navController.navigate(RouteOnboardingPathway) },
                    onOpenScreen = { screen -> navController.navigate(screen.route) },
                )
            }

            val onBack: () -> Unit = { navController.popBackStack() }
            composable(Screen.EscenariosFlashcards.route) { EscenariosFlashcardsScreen(onBack) }
            composable(Screen.SimulacionTurno.route) { SimulacionTurnoScreen(onBack) }
            composable(Screen.RolePlayBilingue.route) { RolePlayBilingueScreen(onBack) }
            composable(Screen.AudioLecciones.route) { AudioLeccionesScreen(onBack) }
            composable(Screen.BibliotecaAudios.route) { BibliotecaAudiosScreen(onBack) }
            composable(Screen.RequisitosCertificacion.route) {
                RequisitosCertificacionScreen(pathwayId = prefs?.pathwayId ?: PathwayId.FamilyCaregiver, onBack = onBack)
            }
            composable(Screen.RecursosSemana.route) {
                RecursosSemanaScreen(pathwayId = prefs?.pathwayId ?: PathwayId.FamilyCaregiver, onBack = onBack)
            }
            composable(Screen.AnalizadorVocacional.route) { AnalizadorVocacionalScreen(onBack) }
        }
    }
}
