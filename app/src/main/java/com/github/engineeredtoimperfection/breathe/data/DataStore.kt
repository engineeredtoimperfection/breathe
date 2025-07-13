package com.github.engineeredtoimperfection.breathe.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val EXERCISES_DONE_COUNTER = intPreferencesKey("exercises_done_counter")

suspend fun Context.countExerciseDone() {
    dataStore.edit { settings ->
        val currentCounterValue = settings[EXERCISES_DONE_COUNTER] ?: 0
        settings[EXERCISES_DONE_COUNTER] = currentCounterValue + 1
    }
}