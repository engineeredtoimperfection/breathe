package com.github.engineeredtoimperfection.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.engineeredtoimperfection.breathe.ui.theme.BreatheTheme
import com.github.engineeredtoimperfection.breathe.ui.theme.Purple40

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BreatheTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val infiniteTransition = rememberInfiniteTransition()
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 1F,
                        targetValue = 2F,
                        animationSpec = infiniteRepeatable(
                            animation = tween(durationMillis = 4000, easing = LinearEasing),
                            repeatMode = RepeatMode.Reverse
                        )
                    )

                    var isModeExplore by remember { mutableStateOf(false) }

                    fun Modifier.scaleTransform() = this.graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        transformOrigin = TransformOrigin.Center
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
                        GlowyText(
                            modifier = Modifier
                                .scaleTransform()
                                .align(Alignment.Center),
                            text = "Breathe.",
                            fontSize = 24.sp,
                            glowColor = Purple40,
                            offset = 4f,
                            blurRadius = 4f,
                            toggleOnTap = Modifier::toggleExploreMode,
                        )

                        AnimatedContent(
                            targetState = isModeExplore,
                            modifier = Modifier.align(Alignment.BottomCenter),
                            transitionSpec = {
                                slideInVertically { fullHeight -> fullHeight } + fadeIn() togetherWith
                                        slideOutVertically { fullHeight -> fullHeight } + fadeOut()
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

                        if (isModeExplore) {
                            ExploreMode()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun BoxScope.ExploreMode(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier
            .align(Alignment.Center)
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {}) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Left Arrow")
        }
        IconButton(onClick = {}) {
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Right Arrow")
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
    toggleOnTap: Modifier.() -> Modifier
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        style = LocalTextStyle.current.copy(
            textMotion = TextMotion.Animated, shadow = Shadow(
                color = glowColor,
                offset = Offset(-1 * offset, -1 * offset),
                blurRadius = blurRadius
            )
        )
    )
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        style = LocalTextStyle.current.copy(
            textMotion = TextMotion.Animated, shadow = Shadow(
                color = glowColor,
                offset = Offset(-1 * offset, offset),
                blurRadius = blurRadius
            )
        )
    )
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        style = LocalTextStyle.current.copy(
            textMotion = TextMotion.Animated, shadow = Shadow(
                color = glowColor,
                offset = Offset(offset, -1 * offset),
                blurRadius = blurRadius
            )
        )
    )
    Text(
        text = text,
        modifier = modifier
            .toggleOnTap(),
        fontSize = fontSize,
        style = LocalTextStyle.current.copy(
            textMotion = TextMotion.Animated, shadow = Shadow(
                color = glowColor,
                offset = Offset(offset, offset),
                blurRadius = blurRadius
            )
        )
    )
}