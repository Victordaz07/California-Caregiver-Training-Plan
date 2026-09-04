package com.caregiverproca.app.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.caregiverproca.app.content.PathwayId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "caregiver_user_preferences")

/**
 * Durable app state backed by Jetpack DataStore (androidx.datastore:datastore-preferences).
 *
 * This is an interim persistence layer: 03_ANDROID_TECHNICAL/ANDROID_ARCHITECTURE.md
 * specifies DataStore for exactly this shape of data (locale, pathwayId, small
 * flags) and Room for durable learning facts (attempts, review queue, curriculum
 * progress per objective). Room is not wired up in this session — see
 * docs/caregiver_upgrade/DECISIONS.md for why — so `currentDay` and
 * `completedBlocksToday` live here too for now, as a real (not simulated)
 * survives-relaunch value for the "Hoy" tab, rather than the full Room schema
 * `DATA_MODEL_AND_SCHEMA.md` describes for the learning engine.
 */
data class UserPreferencesState(
    val pathwayId: PathwayId?,
    val onboardingCompleted: Boolean,
    val currentPlanDay: Int,
    val completedBlocksToday: Int,
)

class UserPreferencesRepository(private val context: Context) {

    private object Keys {
        val PathwayId = stringPreferencesKey("pathway_id")
        val OnboardingCompleted = booleanPreferencesKey("onboarding_completed")
        val CurrentPlanDay = intPreferencesKey("current_plan_day")
        val CompletedBlocksToday = intPreferencesKey("completed_blocks_today")
    }

    val state: Flow<UserPreferencesState> = context.dataStore.data.map { prefs ->
        UserPreferencesState(
            pathwayId = prefs[Keys.PathwayId]?.let { stored ->
                runCatching { PathwayId.valueOf(stored) }.getOrNull()
            },
            onboardingCompleted = prefs[Keys.OnboardingCompleted] ?: false,
            currentPlanDay = prefs[Keys.CurrentPlanDay] ?: 1,
            completedBlocksToday = prefs[Keys.CompletedBlocksToday] ?: 0,
        )
    }

    suspend fun setPathway(pathwayId: PathwayId) {
        context.dataStore.edit { prefs ->
            prefs[Keys.PathwayId] = pathwayId.name
            prefs[Keys.OnboardingCompleted] = true
        }
    }

    suspend fun setCompletedBlocksToday(count: Int) {
        context.dataStore.edit { prefs -> prefs[Keys.CompletedBlocksToday] = count }
    }

    suspend fun setCurrentPlanDay(day: Int) {
        context.dataStore.edit { prefs -> prefs[Keys.CurrentPlanDay] = day }
    }
}
