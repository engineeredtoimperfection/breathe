package com.github.engineeredtoimperfection.breathe.ui.composable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.github.engineeredtoimperfection.breathe.data.BreathingTechnique
import com.github.engineeredtoimperfection.breathe.data.VisualizerStyle
import com.github.engineeredtoimperfection.breathe.ui.theme.Purple40
import kotlinx.coroutines.delay

@Composable
fun BreathingVisualizer(
    modifier: Modifier = Modifier,
    breathingTechnique: BreathingTechnique,
    visualizerStyle: VisualizerStyle,
    toggleExploreMode: Modifier.() -> Modifier,
    onCompleteBreathingExercise: () -> Unit,
) {
    when (visualizerStyle) {

        VisualizerStyle.ExpandingGlowyText -> ExpandingGlowyText(
            modifier = modifier,
            breathingTechnique = breathingTechnique,
            toggleExploreMode = toggleExploreMode,
            onCompleteBreathingExercise = onCompleteBreathingExercise
        )

        VisualizerStyle.PulsatingCircle -> PulsatingCircle(
            modifier = modifier,
            breathingTechnique = breathingTechnique,
            toggleExploreMode = toggleExploreMode,
            onCompleteBreathingExercise = onCompleteBreathingExercise
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
fun ExpandingGlowyText(
    modifier: Modifier = Modifier,
    breathingTechnique: BreathingTechnique,
    toggleExploreMode: Modifier.() -> Modifier,
    onCompleteBreathingExercise: () -> Unit
) {

    val scaleAnimation = remember(breathingTechnique) { Animatable(1F) }

    LaunchExpandingAnimation(breathingTechnique, scaleAnimation, onCompleteBreathingExercise)

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
    toggleExploreMode: Modifier.() -> Modifier,
    radiusDenominatorAtStart: Float = 2.5F,
    radiusDenominatorAtEnd: Float = 2F,
    onCompleteBreathingExercise: () -> Unit
) {

    val radiusDenominatorAnimation =
        remember(breathingTechnique) { Animatable(radiusDenominatorAtStart) }

    LaunchPulsatingAnimation(
        breathingTechnique = breathingTechnique,
        radiusDenominatorAnimation = radiusDenominatorAnimation,
        startValue = radiusDenominatorAtStart,
        endValue = radiusDenominatorAtEnd,
        onFinish = onCompleteBreathingExercise
    )

    Circle(
        modifier = modifier,
        radiusDenominator = radiusDenominatorAnimation,
        toggleOnTap = toggleExploreMode
    )
}

@Composable
private fun LaunchPulsatingAnimation(
    breathingTechnique: BreathingTechnique,
    radiusDenominatorAnimation: Animatable<Float, AnimationVector1D>,
    startValue: Float, endValue: Float,
    onFinish: () -> Unit
) {
    LaunchedEffect(breathingTechnique) {
        with(breathingTechnique.timingPattern) {
            repeat(times = 5) {
                radiusDenominatorAnimation.animateTo(
                    targetValue = endValue,
                    animationSpec = tween(
                        durationMillis = inhaleForSeconds * 1000,
                        easing = LinearEasing
                    )
                )
                delay(timeMillis = afterInhalePause * 1000L)
                radiusDenominatorAnimation.animateTo(
                    targetValue = startValue,
                    animationSpec = tween(
                        durationMillis = exhaleForSeconds * 1000,
                        easing = LinearEasing
                    )
                )
                delay(timeMillis = afterExhalePause * 1000L)
            }
            onFinish()
        }
    }
}

@Composable
private fun Circle(
    modifier: Modifier = Modifier,
    radiusDenominator: Animatable<Float, AnimationVector1D>,
    toggleOnTap: Modifier.() -> Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .drawBehind {

                val radius = size.width / radiusDenominator.value

                drawCircle(
                    color = Purple40,
                    radius = radius,
                    style = Stroke(width = 10f)
                )
            }
            .toggleOnTap()
    )
}

@Composable
private fun LaunchExpandingAnimation(
    breathingTechnique: BreathingTechnique,
    scaleAnimation: Animatable<Float, AnimationVector1D>,
    onFinish: () -> Unit
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
            onFinish()
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