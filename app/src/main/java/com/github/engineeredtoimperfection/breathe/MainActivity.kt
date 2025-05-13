package com.github.engineeredtoimperfection.breathe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextMotion
import androidx.compose.ui.unit.sp
import com.github.engineeredtoimperfection.breathe.ui.theme.BreatheTheme

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

                    Box(modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()) {
                        Text(
                            text = "Breathe.",
                            modifier = Modifier
                                .graphicsLayer {
                                    scaleX = scale
                                    scaleY = scale
                                    transformOrigin = TransformOrigin.Center
                                }
                                .align(Alignment.Center),
                            fontSize = 24.sp,
                            style = LocalTextStyle.current.copy(textMotion = TextMotion.Animated)
                        )
                    }
                }
            }
        }
    }
}