package com.github.engineeredtoimperfection.breathe

sealed class BreathingTechnique(val name: String) {
    object EqualBreathing : BreathingTechnique("Equal Breathing")
    object BoxBreathing : BreathingTechnique("Box Breathing")
    object FourSevenEightBreathing : BreathingTechnique("4-7-8 Breathing")
    object BellyBreathing : BreathingTechnique("Belly Breathing")

    fun next(): BreathingTechnique {
        val breathingTechniques = BreathingTechnique::class.sealedSubclasses.mapNotNull { it.objectInstance }
        val nextTechniqueIndex = (breathingTechniques.indexOf(this) + 1) % breathingTechniques.size
        return breathingTechniques[nextTechniqueIndex]
    }
}