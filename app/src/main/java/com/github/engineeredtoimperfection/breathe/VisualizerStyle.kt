package com.github.engineeredtoimperfection.breathe

sealed class VisualizerStyle {
    object ExpandingGlowyText : VisualizerStyle()
    object PulsatingCircle : VisualizerStyle()
    object NeonBoxLines : VisualizerStyle()
}