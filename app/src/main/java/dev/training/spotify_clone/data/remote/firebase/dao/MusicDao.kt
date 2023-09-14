package dev.training.spotify_clone.data.remote.firebase.dao

import dev.training.spotify_clone.data.entities.Music

interface MusicDao {
    suspend fun getAllMusics(): List<Music>
}