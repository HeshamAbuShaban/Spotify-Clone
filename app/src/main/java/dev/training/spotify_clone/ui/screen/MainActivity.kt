package dev.training.spotify_clone.ui.screen

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import dev.training.spotify_clone.adapters.SwipeMusicAdapter
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.databinding.ActivityMainBinding
import dev.training.spotify_clone.exoplayer.toMusic
import dev.training.spotify_clone.ui.viewmodels.MainViewModel
import dev.training.spotify_clone.utils.Status
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mainViewModel: MainViewModel by viewModels()

    @Inject
    lateinit var swipeMusicAdapter: SwipeMusicAdapter

    @Inject
    lateinit var glide: RequestManager

    private var curPlayingMusic: Music? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        setupVPAdapter()
        subscribeToObserver()
    }

    private fun setupVPAdapter() {
        binding.vpSong.adapter = swipeMusicAdapter
    }

    private fun switchViewPagerToCurrentMusic(music: Music) {
        val newItemIndex = swipeMusicAdapter.musics.indexOf(music)
        if (newItemIndex != -1) {
            binding.vpSong.currentItem = newItemIndex
            curPlayingMusic = music
        }
    }

    private fun subscribeToObserver() {
        mainViewModel.mediaItems.observe(this) {
            it?.let { result ->
                when (result.status) {
                    Status.SUCCESS -> {
                        result.data?.let { musics ->
                            swipeMusicAdapter.musics = musics
                            if (musics.isNotEmpty()) {
                                glide.load((curPlayingMusic ?: musics[0]).imageUrl)
                                    .into(binding.ivCurSongImage)
                            }
                            switchViewPagerToCurrentMusic(curPlayingMusic ?: return@observe)
                        }
                    }

                    Status.LOADING -> Unit
                    Status.ERROR -> Unit
                }
            }
        }

        mainViewModel.curPlayingMusic.observe(this) {
            if (it == null) return@observe

            curPlayingMusic = it.toMusic()
            glide.load(curPlayingMusic?.imageUrl).into(binding.ivCurSongImage)
            switchViewPagerToCurrentMusic(curPlayingMusic ?: return@observe)

        }

    }

}