package dev.training.spotify_clone.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.training.spotify_clone.exoplayer.MusicService
import dev.training.spotify_clone.exoplayer.MusicServiceConnection
import dev.training.spotify_clone.exoplayer.constants.Constants
import dev.training.spotify_clone.exoplayer.currentPlaybackPosition
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MusicViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    private val playbackState = musicServiceConnection.playbackState

    private val _curMusicDuration = MutableLiveData<Long>()
    val curMusicDuration: LiveData<Long> = _curMusicDuration

    private val _curPlayerPosition = MutableLiveData<Long>()
    val curPlayerPosition: LiveData<Long> = _curPlayerPosition

    init {
        updateCurrentPlayerPosition()
    }
    private fun updateCurrentPlayerPosition() {
        viewModelScope.launch {
            while (true) {
                val position = playbackState.value?.currentPlaybackPosition
                if (curPlayerPosition.value != position){
                    _curPlayerPosition.postValue(position!!)
                    _curMusicDuration.postValue(MusicService.curMusicDuration)
                }
                delay(Constants.UPDATE_PLAYER_POSITION_INTERVAL)
            }
        }
    }
}