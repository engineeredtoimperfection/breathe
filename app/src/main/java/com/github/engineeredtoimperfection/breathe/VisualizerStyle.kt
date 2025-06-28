package com.github.engineeredtoimperfection.breathe

import kotlin.math.abs

sealed class VisualizerStyle {
    object ExpandingGlowyText : VisualizerStyle()
    object PulsatingCircle : VisualizerStyle()
    object NeonBoxLines : VisualizerStyle()

    fun next(): VisualizerStyle {
        val visualizerStyles =
            VisualizerStyle::class.sealedSubclasses.mapNotNull { it.objectInstance }
        val nextStyleIndex = (visualizerStyles.indexOf(this) + 1) % visualizerStyles.size
        return visualizerStyles[nextStyleIndex]
    }

    fun prev(): VisualizerStyle {
        val visualizerStyles =
            VisualizerStyle::class.sealedSubclasses.mapNotNull { it.objectInstance }
        val nextStyleIndex = abs((visualizerStyles.indexOf(this) - 1)) % visualizerStyles.size
        return visualizerStyles[nextStyleIndex]
    }
}