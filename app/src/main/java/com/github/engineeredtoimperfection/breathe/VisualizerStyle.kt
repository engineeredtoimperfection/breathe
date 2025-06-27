package com.github.engineeredtoimperfection.breathe

sealed class VisualizerStyle {
    object GlowyText : VisualizerStyle()
    object PulsatingCircle : VisualizerStyle()
    object NeonBoxLines : VisualizerStyle()
}