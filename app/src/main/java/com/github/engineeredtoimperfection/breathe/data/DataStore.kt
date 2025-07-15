package com.github.engineeredtoimperfection.breathe.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val EXERCISES_DONE_COUNTER = intPreferencesKey("exercises_done_counter")
val GENTLE_NUDGE_ENABLED_KEY = booleanPreferencesKey("gentle_nudge_enabled")

suspend fun Context.countExerciseDone() {
    dataStore.edit { settings ->
        val currentCounterValue = settings[EXERCISES_DONE_COUNTER] ?: 0
        settings[EXERCISES_DONE_COUNTER] = currentCounterValue + 1
    }
}

fun Context.isGentleNudgeEnabledFlow(): Flow<Boolean> {
    return dataStore.data.map { preferences ->
        preferences[GENTLE_NUDGE_ENABLED_KEY] ?: false
    }
}

suspend fun Context.runIfGentleNudgeDisabled(block: suspend () -> Unit) {
    isGentleNudgeEnabledFlow().collect { isGentleNudgeEnabled ->
        if (!isGentleNudgeEnabled) {
            block()
        }
    }
}

suspend fun Context.markGentleNudgeAsEnabled() {
    dataStore.edit { settings ->
        settings[GENTLE_NUDGE_ENABLED_KEY] = true
    }
}