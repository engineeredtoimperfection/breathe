package com.github.engineeredtoimperfection.breathe

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
        val nextIndexUnbounded = visualizerStyles.indexOf(this) - 1
        val nextStyleIndex =
            if (nextIndexUnbounded < 0) visualizerStyles.size - 1 else nextIndexUnbounded % visualizerStyles.size
        return visualizerStyles[nextStyleIndex]
    }
}