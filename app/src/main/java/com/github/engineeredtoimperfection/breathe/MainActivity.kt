package com.github.engineeredtoimperfection.breathe

import android.app.PendingIntent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import com.github.engineeredtoimperfection.breathe.common.createRemindersNotificationChannel
import com.github.engineeredtoimperfection.breathe.common.requestPermissionIfNotGranted
import com.github.engineeredtoimperfection.breathe.common.scheduleNotificationIfGranted
import com.github.engineeredtoimperfection.breathe.data.BreathingTechnique
import com.github.engineeredtoimperfection.breathe.data.EXERCISES_DONE_COUNTER
import com.github.engineeredtoimperfection.breathe.data.VisualizerStyle
import com.github.engineeredtoimperfection.breathe.data.countExerciseDone
import com.github.engineeredtoimperfection.breathe.data.dataStore
import com.github.engineeredtoimperfection.breathe.ui.composable.BreathingVisualizer
import com.github.engineeredtoimperfection.breathe.ui.composable.ExploreMode
import com.github.engineeredtoimperfection.breathe.ui.theme.BreatheTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createRemindersNotificationChannel(this)

        val exercisesDoneCounterFlow: Flow<Int> =
            dataStore.data.map { preferences: Preferences ->
                preferences[EXERCISES_DONE_COUNTER] ?: 0
            }

        suspend fun runIfExerciseDoneCountExceeds(thresholdCount: Int, block: () -> Unit) {
            exercisesDoneCounterFlow.collect { exercisesDone ->
                if (exercisesDone >= thresholdCount) {
                    block()
                }
            }
        }

        setContent {
            BreatheTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    var isModeExplore by remember { mutableStateOf(false) }

                    var breathingTechnique: BreathingTechnique by remember {
                        mutableStateOf(
                            BreathingTechnique.EqualBreathing
                        )
                    }

                    var visualizerStyle: VisualizerStyle by remember {
                        mutableStateOf(
                            VisualizerStyle.ExpandingGlowyText
                        )
                    }

                    val interactionSource = remember { MutableInteractionSource() }

                    fun Modifier.toggleExploreMode() = this.toggleable(
                        value = isModeExplore,
                        indication = null,
                        interactionSource = interactionSource,
                    ) {
                        isModeExplore = !isModeExplore
                    }

                    Box(
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        ExploreMode(
                            visible = isModeExplore,
                            enter = fadeIn(tween(durationMillis = 1000, delayMillis = 1000)),
                            exit = fadeOut(tween(durationMillis = 1000)),
                            breathingTechnique = breathingTechnique,
                            onNextBreathingTechnique = {
                                breathingTechnique = breathingTechnique.next()
                            },
                            onNextVisualizer = { visualizerStyle = visualizerStyle.next() },
                            onPrevVisualizer = { visualizerStyle = visualizerStyle.prev() }
                        )

                        BreathingVisualizer(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .widthIn(max = 200.dp),
                            breathingTechnique = breathingTechnique,
                            visualizerStyle = visualizerStyle,
                            toggleExploreMode = Modifier::toggleExploreMode,
                            onCompleteBreathingExercise = {
                                lifecycleScope.launch {
                                    countExerciseDone()

                                    runIfExerciseDoneCountExceeds(3) {
                                        // Show UI instead of directly asking for permission
                                        requestPermissionIfNotGranted()

                                        scheduleNotificationIfGranted()
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}