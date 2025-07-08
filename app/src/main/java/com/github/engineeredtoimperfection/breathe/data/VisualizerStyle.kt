package com.github.engineeredtoimperfection.breathe.data

import com.github.engineeredtoimperfection.breathe.common.listOfObjectsInSealedClass

sealed class VisualizerStyle {
    object ExpandingGlowyText : VisualizerStyle()
    object PulsatingCircle : VisualizerStyle()
    object NeonBoxLines : VisualizerStyle()

    fun next(): VisualizerStyle {
        val visualizerStyles = listOfObjectsInSealedClass<VisualizerStyle>()
        val nextStyleIndex = (visualizerStyles.indexOf(this) + 1) % visualizerStyles.size
        return visualizerStyles[nextStyleIndex]
    }

    fun prev(): VisualizerStyle {
        val visualizerStyles = listOfObjectsInSealedClass<VisualizerStyle>()
        val nextIndexUnbounded = visualizerStyles.indexOf(this) - 1
        val nextStyleIndex =
            if (nextIndexUnbounded < 0) visualizerStyles.size - 1 else nextIndexUnbounded % visualizerStyles.size
        return visualizerStyles[nextStyleIndex]
    }
}