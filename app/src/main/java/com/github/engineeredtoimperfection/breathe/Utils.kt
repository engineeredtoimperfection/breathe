package com.github.engineeredtoimperfection.breathe

inline fun <reified T> listOfObjectsInSealedClass(): List<T> {
    return T::class.sealedSubclasses.mapNotNull { it.objectInstance }
}