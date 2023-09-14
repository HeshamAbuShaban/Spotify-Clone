@file:Suppress("DEPRECATION")

package dev.training.spotify_clone.exoplayer

import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
import android.support.v4.media.MediaDescriptionCompat
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_ARTIST
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_SUBTITLE
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_DISPLAY_TITLE
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_ID
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_MEDIA_URI
import android.support.v4.media.MediaMetadataCompat.METADATA_KEY_TITLE
import androidx.core.net.toUri
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.data.remote.firebase.MusicDatabase
import dev.training.spotify_clone.exoplayer.constants.State.STATE_CREATED
import dev.training.spotify_clone.exoplayer.constants.State.STATE_ERROR
import dev.training.spotify_clone.exoplayer.constants.State.STATE_INITIALIZED
import dev.training.spotify_clone.exoplayer.constants.State.STATE_INITIALIZING
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseMusicSource @Inject constructor(
    private val musicDatabase: MusicDatabase,
) {

    var musics = emptyList<MediaMetadataCompat>()

    suspend fun fetchMediaData() = withContext(Dispatchers.IO) {
        state = STATE_INITIALIZING
        val allMusics = musicDatabase.getAllMusics()
        musics = allMusics.map { music: Music ->
            MediaMetadataCompat.Builder()
                .putString(METADATA_KEY_TITLE, music.title)
                .putString(METADATA_KEY_DISPLAY_TITLE, music.title)
                .putString(METADATA_KEY_ARTIST, music.subtitle)
                .putString(METADATA_KEY_DISPLAY_SUBTITLE, music.subtitle)
                .putString(METADATA_KEY_DISPLAY_DESCRIPTION, music.subtitle)
                .putString(METADATA_KEY_MEDIA_ID, music.mediaId)
                .putString(METADATA_KEY_DISPLAY_ICON_URI, music.imageUrl)
                .putString(METADATA_KEY_MEDIA_URI, music.musicUrl)
                .putString(METADATA_KEY_ALBUM_ART_URI, music.imageUrl)
                .build()
        }
        state = STATE_INITIALIZED
    }

    fun asMediaSource(dataSourceFactory: DefaultDataSourceFactory): ConcatenatingMediaSource {
        val concatenatingMediaSource = ConcatenatingMediaSource()
        musics.forEach { music ->
            val mediaSource = ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(
                    MediaItem.fromUri(
                        getMusicUri(music)
                    )
                )
            concatenatingMediaSource.addMediaSource(mediaSource)
        }
        return concatenatingMediaSource
    }

    fun asMediaItems() = musics.map { music ->
        val desc = MediaDescriptionCompat.Builder()
            .setMediaUri(getMusicUri(music))
            .setTitle(music.description.title)
            .setSubtitle(music.description.subtitle)
            .setMediaId(music.description.mediaId)
            .setIconUri(music.description.iconUri)
            .build()
        MediaBrowserCompat.MediaItem(desc, FLAG_PLAYABLE)
    }.toMutableList()

    private fun getMusicUri(music: MediaMetadataCompat) =
        music.getString(METADATA_KEY_MEDIA_URI).toUri()

    private val onReadyListener = mutableListOf<(Boolean) -> Unit>()

    private var state = STATE_CREATED
        set(value) {
            if (value == STATE_INITIALIZED || value == STATE_ERROR) {
                synchronized(onReadyListener) {
                    field = value
                    onReadyListener.forEach { listener ->
                        listener(state == STATE_INITIALIZED)
                    }
                }
            } else {
                field = value
            }
        }

    fun whenReady(action: (Boolean) -> Unit): Boolean {
        return if (state == STATE_CREATED || state == STATE_INITIALIZING) {
            onReadyListener += action
            false
        } else {
            action(state == STATE_INITIALIZED)
            true
        }
    }


}

