@file:Suppress("DEPRECATION")

package dev.training.spotify_clone.exoplayer.callbacks

import android.widget.Toast
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player.STATE_READY
import com.google.android.exoplayer2.Player.Listener
import dev.training.spotify_clone.exoplayer.MusicService

class MusicPlayerEventListener(
    private val musicService: MusicService,
) : Listener {

    @Deprecated("Deprecated in Java")
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayerStateChanged(playWhenReady, playbackState)
        if (playbackState == STATE_READY && !playWhenReady) {
            musicService.stopForeground(false)
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        super.onPlayerError(error)
        Toast.makeText(musicService, "Error Occurred: $error", Toast.LENGTH_LONG).show()
    }
}