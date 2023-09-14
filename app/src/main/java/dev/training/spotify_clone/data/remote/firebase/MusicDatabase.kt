package dev.training.spotify_clone.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import dev.training.spotify_clone.data.constants.Collections
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.data.remote.firebase.dao.MusicDao
import kotlinx.coroutines.tasks.await

class MusicDatabase : MusicDao {

    private val firestore = FirebaseFirestore.getInstance()
    private val musicCollection = firestore.collection(Collections.MUSICS.value)

    override suspend fun getAllMusics(): List<Music> = try {
        musicCollection.get().await().toObjects(Music::class.java)
    } catch (exception: Exception) {
        exception.printStackTrace()
        emptyList()
    }

}