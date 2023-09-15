package dev.training.spotify_clone.ui.fragments

import android.os.Bundle
import android.support.v4.media.session.PlaybackStateCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import dev.training.spotify_clone.R
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.databinding.FragmentMusicBinding
import dev.training.spotify_clone.exoplayer.isPlaying
import dev.training.spotify_clone.exoplayer.toMusic
import dev.training.spotify_clone.ui.viewmodels.MainViewModel
import dev.training.spotify_clone.ui.viewmodels.MusicViewModel
import dev.training.spotify_clone.utils.Status.SUCCESS
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class MusicFragment : Fragment() {
    private lateinit var binding: FragmentMusicBinding

    @Inject
    lateinit var glide: RequestManager

    private lateinit var mainViewModel: MainViewModel

    private val musicViewModel: MusicViewModel by viewModels()

    private var curPlayingMusic: Music? = null

    private var playbackState: PlaybackStateCompat? = null

    private var shouldUpdateSeekBar: Boolean = true

    private val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMusicBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()
    }

    private fun init() {
        mainViewModel = ViewModelProvider(requireActivity())[MainViewModel::class.java]
        subscribeToObserves()
        setupClickListeners()
    }

    private fun setupClickListeners() {
        with(binding) {

            ivPlayPauseDetail.setOnClickListener {
                curPlayingMusic?.let {
                    mainViewModel.playOrToggleMusic(it, true)
                }
            }

            ivSkip.setOnClickListener {
                mainViewModel.skipToNextMusic()
            }

            ivSkipPrevious.setOnClickListener {
                mainViewModel.skipToPreviousMusic()
            }

            seekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser){
                        setCurPlayerTimeToCurTextView(progress.toLong())
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                    shouldUpdateSeekBar = false
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    seekBar?.let {
                        mainViewModel.seekTo(it.progress.toLong())
                        shouldUpdateSeekBar = true
                    }
                }

            })

        }
    }

    private fun updateTitleAndMusicImage(music: Music) {
        with(binding) {
            val title = "${music.title} - ${music.subtitle}"
            tvSongName.text = title
            glide.load(music.imageUrl).into(ivSongImage)
        }
    }

    private fun subscribeToObserves() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) {
            it?.let { result ->
                when (result.status) {
                    SUCCESS -> {
                        result.data?.let { musics ->
                            if (curPlayingMusic == null && musics.isNotEmpty()) {
                                curPlayingMusic = musics[0]
                                updateTitleAndMusicImage(musics[0])
                            }
                        }
                    }

                    else -> Unit
                }
            }
        }

        mainViewModel.curPlayingMusic.observe(viewLifecycleOwner) {
            if (it == null) return@observe
            curPlayingMusic = it.toMusic()
            updateTitleAndMusicImage(curPlayingMusic!!)
        }

        mainViewModel.playbackState.observe(viewLifecycleOwner) {
            playbackState = it
            with(binding) {
                ivPlayPauseDetail.setImageResource(
                    if (playbackState?.isPlaying == true) R.drawable.ic_pause else R.drawable.ic_play
                )
                seekBar.progress = it?.position?.toInt() ?: 0
            }
        }

        musicViewModel.curPlayerPosition.observe(viewLifecycleOwner) {
            if (shouldUpdateSeekBar) {
                binding.seekBar.progress = it.toInt()
                setCurPlayerTimeToCurTextView(it)
            }
        }

        musicViewModel.curMusicDuration.observe(viewLifecycleOwner) {
            binding.seekBar.max = it.toInt()
            setCurPlayerTimeToMaxTextView(it)
        }

    }

    private fun setCurPlayerTimeToCurTextView(ms: Long) {
        binding.tvCurTime.text = dateFormat.format(ms)

    }

    private fun setCurPlayerTimeToMaxTextView(ms: Long) {
        binding.tvSongDuration.text = dateFormat.format(ms)
    }

}