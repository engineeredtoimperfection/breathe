package com.github.engineeredtoimperfection.breathe

sealed class BreathingTechnique(
    val name: String,
    val description: String,
    val timingPattern: TimingPattern
) {
    object EqualBreathing : BreathingTechnique(
        name = "Equal Breathing",
        description = "Helps steady your focus and keep you centered. Good for staying mentally clear during work or meditation.",
        timingPattern = TimingPattern(
            inhaleForSeconds = 4,
            afterInhalePause = 0,
            exhaleForSeconds = 4,
            afterExhalePause = 4
        )
    )

    object BoxBreathing : BreathingTechnique(
        name = "Box Breathing",
        description = "Helps calm your nerves and regain control under pressure. Great for anxiety or stressful situations where you need to stay sharp.",
        timingPattern = TimingPattern(
            inhaleForSeconds = 4,
            afterInhalePause = 4,
            exhaleForSeconds = 4,
            afterExhalePause = 4
        )
    )

    object FourSevenEightBreathing : BreathingTechnique(
        name = "4-7-8 Breathing",
        description = "Helps slow your body down and trigger deep relaxation. Suitable for falling asleep or unwinding after a long day.",
        timingPattern = TimingPattern(
            inhaleForSeconds = 4,
            afterInhalePause = 7,
            exhaleForSeconds = 8,
            afterExhalePause = 0
        )
    )

    object BellyBreathing : BreathingTechnique(
        name = "Belly Breathing",
        description = "Helps relax physical tension and settle your body. Good for daily practice or moments when you feel disconnected or overstimulated.",
        timingPattern = TimingPattern(
            inhaleForSeconds = 4,
            afterInhalePause = 0,
            exhaleForSeconds = 6,
            afterExhalePause = 0
        )
    )

    fun next(): BreathingTechnique {
        val breathingTechniques =
            BreathingTechnique::class.sealedSubclasses.mapNotNull { it.objectInstance }
        val nextTechniqueIndex = (breathingTechniques.indexOf(this) + 1) % breathingTechniques.size
        return breathingTechniques[nextTechniqueIndex]
    }

    data class TimingPattern(
        val inhaleForSeconds: Int,
        val afterInhalePause: Int,
        val exhaleForSeconds: Int,
        val afterExhalePause: Int
    )
}