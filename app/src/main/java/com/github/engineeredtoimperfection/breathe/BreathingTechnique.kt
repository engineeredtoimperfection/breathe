package com.github.engineeredtoimperfection.breathe

sealed class BreathingTechnique(val name: String) {
    object EqualBreathing : BreathingTechnique("Equal Breathing")
    object BoxBreathing : BreathingTechnique("Box Breathing")
    object FourSevenEightBreathing : BreathingTechnique("4-7-8 Breathing")
    object BellyBreathing : BreathingTechnique("Belly Breathing")
}