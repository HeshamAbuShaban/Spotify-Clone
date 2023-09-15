package dev.training.spotify_clone.ui.screen

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.RequestManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.training.spotify_clone.R
import dev.training.spotify_clone.adapters.SwipeMusicAdapter
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.databinding.ActivityMainBinding
import dev.training.spotify_clone.exoplayer.isPlaying
import dev.training.spotify_clone.exoplayer.toMusic
import dev.training.spotify_clone.ui.viewmodels.MainViewModel
import dev.training.spotify_clone.utils.GeneralUtils
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

    private var playbackStateCompat: PlaybackStateCompat? = null

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
        setupClickListeners()
    }

    private fun setupClickListeners() {
        with(binding) {

            ivPlayPause.setOnClickListener {
                curPlayingMusic?.let {
                    mainViewModel.playOrToggleMusic(it, true)
                }
            }

        }
    }

    private fun setupVPAdapter() {
        with(binding.vpSong) {
            adapter = swipeMusicAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (playbackStateCompat?.isPlaying == true) {
                        mainViewModel.playOrToggleMusic(swipeMusicAdapter.musics[position])
                    } else {
                        curPlayingMusic = swipeMusicAdapter.musics[position]
                    }
                }
            })
        }
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

        mainViewModel.playbackState.observe(this) {
            playbackStateCompat = it
            binding.ivPlayPause.setImageResource(
                if (playbackStateCompat?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
            )
        }

        mainViewModel.isConnected.observe(this) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.ERROR -> {
                        GeneralUtils
                            .showSnackBar(
                                binding.root,
                                result.message ?: "Some error occurred!",
                                Snackbar.LENGTH_LONG
                            )
                    }

                    else -> Unit
                }
            }
        }


        mainViewModel.networkError.observe(this) {
            it?.getContentIfNotHandled()?.let { result ->
                when (result.status) {
                    Status.ERROR -> {
                        GeneralUtils
                            .showSnackBar(
                                binding.root,
                                result.message ?: "Some error occurred!",
                                Snackbar.LENGTH_LONG
                            )
                    }

                    else -> Unit
                }
            }
        }
    }

}