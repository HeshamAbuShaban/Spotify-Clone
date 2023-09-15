package dev.training.spotify_clone.exoplayer

import android.support.v4.media.MediaMetadataCompat
import dev.training.spotify_clone.data.entities.Music

fun MediaMetadataCompat.toMusic(): Music? {
    return description?.let {
        Music(
            it.mediaId ?: "",
            it.title.toString(),
            it.subtitle.toString(),
            it.mediaUri.toString(),
            it.iconUri.toString()
        )
    }
}