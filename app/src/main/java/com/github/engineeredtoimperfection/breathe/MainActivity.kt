package com.github.engineeredtoimperfection.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.engineeredtoimperfection.breathe.ui.theme.BreatheTheme
import com.github.engineeredtoimperfection.breathe.ui.theme.Purple40
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
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
                        AnimatedVisibility(
                            visible = isModeExplore,
                            modifier = Modifier.align(Alignment.TopCenter),
                            enter = fadeIn(tween(durationMillis = 1000, delayMillis = 1000)),
                            exit = fadeOut(tween(durationMillis = 1000))
                        ) {
                            Text(
                                text = breathingTechnique.name,
                                modifier = Modifier.clickable {
                                    breathingTechnique = breathingTechnique.next()
                                }
                            )
                        }

                        BreathingVisualizer(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .widthIn(max = 200.dp),
                            breathingTechnique = breathingTechnique,
                            visualizerStyle = visualizerStyle,
                            toggleExploreMode = Modifier::toggleExploreMode,
                        )

                        AnimatedContent(
                            targetState = isModeExplore,
                            modifier = Modifier.align(Alignment.BottomCenter),
                            transitionSpec = {
                                fadeIn(
                                    tween(
                                        durationMillis = 1000,
                                        delayMillis = 1000
                                    )
                                ) togetherWith
                                        fadeOut(tween(durationMillis = 1000))
                            }
                        ) { modeState ->
                            Text(
                                text =
                                    if (modeState)
                                        "Explore Mode"
                                    else
                                        "Breathe Mode"
                            )
                        }

                        AnimatedVisibility(
                            visible = isModeExplore,
                            modifier = Modifier.align(Alignment.Center),
                            enter = fadeIn(tween(durationMillis = 1000, delayMillis = 1000)),
                            exit = fadeOut(tween(durationMillis = 1000))
                        ) {
                            ExploreMode(
                                onNext = { visualizerStyle = visualizerStyle.next() },
                                onPrev = { visualizerStyle = visualizerStyle.prev() }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BreathingVisualizer(
    modifier: Modifier = Modifier,
    breathingTechnique: BreathingTechnique,
    visualizerStyle: VisualizerStyle,
    toggleExploreMode: Modifier.() -> Modifier
) {
    when (visualizerStyle) {

        VisualizerStyle.ExpandingGlowyText -> ExpandingGlowyText(
            modifier = modifier,
            breathingTechnique = breathingTechnique,
            toggleExploreMode = toggleExploreMode
        )

        VisualizerStyle.PulsatingCircle -> PulsatingCircle(
            modifier = modifier,
            breathingTechnique = breathingTechnique,
            toggleExploreMode = toggleExploreMode
        )

        VisualizerStyle.NeonBoxLines -> Text(
            text = "NeonBoxLines visualizer not yet implemented.",
            textAlign = TextAlign.Center,
            color = Color.Black,
            modifier = modifier.background(Color.White)
        )
    }
}

@Composable
fun ExploreMode(modifier: Modifier = Modifier, onNext: () -> Unit, onPrev: () -> Unit) {

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = onPrev) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Left Arrow")
        }
        IconButton(onClick = onNext) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Right Arrow")
        }
    }
}

@Composable
fun ExpandingGlowyText(
    modifier: Modifier = Modifier,
    breathingTechnique: BreathingTechnique,
    toggleExploreMode: Modifier.() -> Modifier
) {

    val scaleAnimation = remember(breathingTechnique) { Animatable(1F) }

    LaunchExpandingAnimation(breathingTechnique, scaleAnimation)

    fun Modifier.scaleTransform() = this.graphicsLayer {
        scaleX = scaleAnimation.value
        scaleY = scaleAnimation.value
        transformOrigin = TransformOrigin.Center
    }

    GlowyText(
        modifier = modifier
            .scaleTransform(),
        text = "Breathe.",
        fontSize = 24.sp,
        glowColor = Purple40,
        offset = 4f,
        blurRadius = 4f,
        textMotion = TextMotion.Animated,
        toggleOnTap = toggleExploreMode,
    )
}

@Composable
fun PulsatingCircle(
    modifier: Modifier = Modifier,
    breathingTechnique: BreathingTechnique,
    toggleExploreMode: Modifier.() -> Modifier
) {
    Text(
        text = "PulsatingCircle visualizer not yet implemented.",
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
private fun LaunchExpandingAnimation(
    breathingTechnique: BreathingTechnique,
    scaleAnimation: Animatable<Float, AnimationVector1D>
) {
    LaunchedEffect(breathingTechnique) {
        with(breathingTechnique.timingPattern) {
            repeat(times = 5) {
                scaleAnimation.animateTo(
                    targetValue = 2F,
                    animationSpec = tween(
                        durationMillis = inhaleForSeconds * 1000,
                        easing = LinearEasing
                    )
                )
                delay(timeMillis = afterInhalePause * 1000L)
                scaleAnimation.animateTo(
                    targetValue = 1F,
                    animationSpec = tween(
                        durationMillis = exhaleForSeconds * 1000,
                        easing = LinearEasing
                    )
                )
                delay(timeMillis = afterExhalePause * 1000L)
            }
        }
    }
}

@Composable
fun GlowyText(
    modifier: Modifier = Modifier,
    text: String,
    fontSize: TextUnit,
    glowColor: Color,
    offset: Float,
    blurRadius: Float,
    textMotion: TextMotion?,
    toggleOnTap: Modifier.() -> Modifier
) {

    val topLeftOffset = Offset(x = -1 * offset, y = -1 * offset)
    val topRightOffset = Offset(x = 1 * offset, y = -1 * offset)
    val bottomLeftOffset = Offset(x = -1 * offset, y = 1 * offset)
    val bottomRightOffset = Offset(x = 1 * offset, y = 1 * offset)

    val glowyTextShadow = Shadow(
        color = glowColor,
        blurRadius = blurRadius
    )

    val topLeftShadow = glowyTextShadow.copy(offset = topLeftOffset)
    val topRightShadow = glowyTextShadow.copy(offset = topRightOffset)
    val bottomRightShadow = glowyTextShadow.copy(offset = bottomRightOffset)
    val bottomLeftShadow = glowyTextShadow.copy(offset = bottomLeftOffset)

    val glowyTextStyle = LocalTextStyle.current.merge(
        textMotion = textMotion, shadow = glowyTextShadow
    )

    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        style = glowyTextStyle.copy(shadow = topLeftShadow)
    )

    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        style = glowyTextStyle.copy(shadow = topRightShadow)
    )

    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        style = glowyTextStyle.copy(shadow = bottomRightShadow)
    )

    Text(
        text = text,
        modifier = modifier
            .toggleOnTap(),
        fontSize = fontSize,
        style = glowyTextStyle.copy(shadow = bottomLeftShadow)
    )
}