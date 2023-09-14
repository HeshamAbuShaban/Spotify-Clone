package dev.training.spotify_clone.ui.viewmodels.factory.parametars

interface FactoryParameter<T> {
    fun getFirstParameterValue(): T
}