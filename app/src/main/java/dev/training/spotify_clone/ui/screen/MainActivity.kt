package dev.training.spotify_clone.ui.screen

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.Navigation
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

    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        setupVPAdapter()
        subscribeToObserver()
        setupClickListeners()
        navControllerListener()
    }

    private fun setupClickListeners() {
        with(binding) {

            ivPlayPause.setOnClickListener {
                curPlayingMusic?.let {
                    mainViewModel.playOrToggleMusic(it, true)
                }
            }

            swipeMusicAdapter.onItemClickListener {
                navController.navigate(R.id.globalActionNavigateToMusicFragment)
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

    private fun navControllerListener() {

        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.musicFragment -> hideBottomBar()
                R.id.homeFragment -> showBottomBar()
                else -> showBottomBar()
            }

        }

    }

    private fun hideBottomBar() {
        with(binding) {
            ivCurSongImage.isVisible = false
            vpSong.isVisible = false
            ivPlayPause.isVisible = false
        }
    }

    private fun showBottomBar() {
        with(binding) {
            ivCurSongImage.isVisible = true
            vpSong.isVisible = true
            ivPlayPause.isVisible = true
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