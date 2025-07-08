package com.github.engineeredtoimperfection.breathe.common

inline fun <reified T> listOfObjectsInSealedClass(): List<T> {
    return T::class.sealedSubclasses.mapNotNull { it.objectInstance }
}