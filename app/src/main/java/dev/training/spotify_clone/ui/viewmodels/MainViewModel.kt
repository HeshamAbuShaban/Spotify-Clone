package dev.training.spotify_clone.ui.viewmodels

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.exoplayer.MusicServiceConnection
import dev.training.spotify_clone.exoplayer.constants.Constants.MEDIA_ROOT_ID
import dev.training.spotify_clone.exoplayer.isPlayEnabled
import dev.training.spotify_clone.exoplayer.isPlaying
import dev.training.spotify_clone.exoplayer.isPrepared
import dev.training.spotify_clone.utils.Resource
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val musicServiceConnection: MusicServiceConnection,
) : ViewModel() {

    private val _mediaItems = MutableLiveData<Resource<List<Music>>>()
    val mediaItems: LiveData<Resource<List<Music>>> = _mediaItems

    val isConnected = musicServiceConnection.isConnected
    val networkError = musicServiceConnection.networkError
    val curPlayingMusic = musicServiceConnection.curPlayingMusic
    val playbackState = musicServiceConnection.playbackState

    init {
        _mediaItems.postValue(Resource.loading(null))
        musicServiceConnection.subscribe(
            MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {
                override fun onChildrenLoaded(
                    parentId: String,
                    children: MutableList<MediaBrowserCompat.MediaItem>,
                ) {
                    super.onChildrenLoaded(parentId, children)

                    val items = children.map {
                        Music(
                            it.mediaId!!,
                            it.description.title.toString(),
                            it.description.subtitle.toString(),
                            it.description.mediaUri.toString(),
                            it.description.iconUri.toString(),
                        )
                    }

                    _mediaItems.postValue(Resource.success(items))

                }
            }
        )
    }

    fun skipToNextMusic() {
        musicServiceConnection.transportControls.skipToNext()
    }

    fun skipToPreviousMusic() {
        musicServiceConnection.transportControls.skipToPrevious()
    }

    fun seekTo(pos: Long) {
        musicServiceConnection.transportControls.seekTo(pos)
    }

    fun playOrToggleMusic(mediaItem: Music, toggle: Boolean = false) {
        val isPrepared = playbackState.value?.isPrepared ?: false
        if (
            isPrepared
            && mediaItem.mediaId ==
            curPlayingMusic.value?.getString(METADATA_KEY_MEDIA_ID)
        ) {

            playbackState.value?.let { playbackState ->

                when {
                    playbackState.isPlaying ->
                        if (toggle) musicServiceConnection.transportControls.pause()

                    playbackState.isPlayEnabled ->
                        musicServiceConnection.transportControls.play()

                    else -> Unit
                }

            }

        } else {
            musicServiceConnection.transportControls.playFromMediaId(mediaItem.mediaId, null)
        }
    }

    override fun onCleared() {
        super.onCleared()
        musicServiceConnection.unsubscribe(MEDIA_ROOT_ID,
            object : MediaBrowserCompat.SubscriptionCallback() {}
        )
    }
}