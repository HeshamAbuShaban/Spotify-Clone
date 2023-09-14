package dev.training.spotify_clone.ui.viewmodels.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.training.spotify_clone.exoplayer.MusicServiceConnection
import dev.training.spotify_clone.ui.viewmodels.MainViewModel
import dev.training.spotify_clone.ui.viewmodels.factory.parametars.FactoryParameter


// OldWay , explicitly defining the parameters
/*
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val musicServiceConnection: MusicServiceConnection) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(musicServiceConnection) as T
        }
        throw IllegalArgumentException("ViewModel class Not Found!")
    }
}*/


// NewWay , implicitly defining the parameters
@Suppress("UNCHECKED_CAST")
class MainViewModelFactory(private val factoryParameter: FactoryParameter<MusicServiceConnection>) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(factoryParameter.getFirstParameterValue()) as T
        }
        throw IllegalArgumentException("ViewModel class Not Found!")
    }
}

//.. Usage
/**
 * fun main() {
 * val context = AppController().applicationContext
 * MainViewModelFactory(FactoryParameterImpl(context))
}*
 */

class FactoryParameterImpl(private val context: Context) :
    FactoryParameter<MusicServiceConnection> {
    override fun getFirstParameterValue(): MusicServiceConnection {
        return MusicServiceConnection(context = context)
    }
}