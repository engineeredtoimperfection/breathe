package com.github.engineeredtoimperfection.breathe

import android.os.Bundle
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
import com.github.engineeredtoimperfection.breathe.common.createRemindersNotificationChannel
import com.github.engineeredtoimperfection.breathe.common.sendReminderNotification
import com.github.engineeredtoimperfection.breathe.data.BreathingTechnique
import com.github.engineeredtoimperfection.breathe.data.VisualizerStyle
import com.github.engineeredtoimperfection.breathe.ui.composable.BreathingVisualizer
import com.github.engineeredtoimperfection.breathe.ui.composable.ExploreMode
import com.github.engineeredtoimperfection.breathe.ui.theme.BreatheTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        createRemindersNotificationChannel(this)

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
                            onCompleteBreathingExercise = { sendReminderNotification(this@MainActivity) }
                        )
                    }
                }
            }
        }
    }
}