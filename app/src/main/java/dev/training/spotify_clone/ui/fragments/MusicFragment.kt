package dev.training.spotify_clone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.databinding.FragmentMusicBinding
import dev.training.spotify_clone.exoplayer.toMusic
import dev.training.spotify_clone.ui.viewmodels.MainViewModel
import dev.training.spotify_clone.ui.viewmodels.MusicViewModel
import dev.training.spotify_clone.utils.Status.SUCCESS
import javax.inject.Inject

@AndroidEntryPoint
class MusicFragment : Fragment() {
    private lateinit var binding: FragmentMusicBinding

    @Inject
    lateinit var glide: RequestManager

    private lateinit var mainViewModel: MainViewModel

    private val musicViewModel: MusicViewModel by viewModels()

    private var curPlayingMusic: Music? = null


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

    }

}