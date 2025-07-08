package com.github.engineeredtoimperfection.breathe.ui.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.engineeredtoimperfection.breathe.data.BreathingTechnique

@Composable
fun BoxScope.ExploreMode(
    visible: Boolean,
    enter: EnterTransition,
    exit: ExitTransition,
    breathingTechnique: BreathingTechnique, onNextBreathingTechnique: () -> Unit,
    onNextVisualizer: () -> Unit,
    onPrevVisualizer: () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = Modifier.align(Alignment.TopCenter),
        enter = enter,
        exit = exit
    ) {
        BreathingTechniqueLabel(
            breathingTechnique = breathingTechnique,
            onNext = onNextBreathingTechnique
        )
    }

    AnimatedVisibility(
        visible = visible,
        modifier = Modifier.align(Alignment.Center),
        enter = enter,
        exit = exit
    ) {
        ExploreModeHorizontalArrows(
            onNext = onNextVisualizer,
            onPrev = onPrevVisualizer
        )
    }

    AnimatedVisibility(
        visible = visible,
        modifier = Modifier.align(Alignment.BottomCenter),
        enter = enter,
        exit = exit
    ) {
        ExploreModeLabel()
    }
}

@Composable
fun BreathingTechniqueLabel(
    modifier: Modifier = Modifier,
    breathingTechnique: BreathingTechnique,
    onNext: () -> Unit
) {
    Text(
        text = breathingTechnique.name,
        modifier = modifier.clickable(onClick = onNext)
    )
}

@Composable
fun ExploreModeHorizontalArrows(
    modifier: Modifier = Modifier,
    onNext: () -> Unit,
    onPrev: () -> Unit
) {

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
fun ExploreModeLabel(modifier: Modifier = Modifier) {
    Text(text = "Explore Mode", modifier = modifier)
}