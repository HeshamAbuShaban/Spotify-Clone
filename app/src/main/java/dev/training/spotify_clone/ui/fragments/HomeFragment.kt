package dev.training.spotify_clone.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.training.spotify_clone.R
import dev.training.spotify_clone.adapters.MusicAdapter
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.databinding.FragmentHomeBinding
import dev.training.spotify_clone.exoplayer.MusicServiceConnection
import dev.training.spotify_clone.ui.viewmodels.MainViewModel
import dev.training.spotify_clone.ui.viewmodels.factory.MainViewModelFactory
import dev.training.spotify_clone.utils.Status
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding

    private lateinit var mainViewModel: MainViewModel

    @Inject
    lateinit var musicServiceConnection: MusicServiceConnection

    @Inject
    lateinit var musicAdapter: MusicAdapter

    @Inject
    lateinit var factory: MainViewModelFactory

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        // we use this way of init the mainViewModel
        //  .. due the need of binding this fragment to our activity explicitly

        // This is the old way of making the ViewModelFactory
        /*
                musicServiceConnection = MusicServiceConnection(this@HomeFragment.requireContext())
                val factory = MainViewModelFactory(musicServiceConnection)
                mainViewModel = ViewModelProvider(requireActivity(), factory)[MainViewModel::class.java]
        */
//        musicServiceConnection = MusicServiceConnection(this@HomeFragment.requireContext())
//        val factory = MainViewModelFactory(FactoryParameterImpl(requireContext()))

        mainViewModel = ViewModelProvider(requireActivity(), factory)[MainViewModel::class.java]

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFragment()
    }

    private fun initFragment() {
        setupRecyclerView()
        subscribeToObservers()

        musicAdapter.setOnItemClickListener { music: Music ->
            mainViewModel.playOrToggleMusic(music)
        }
    }

    private fun setupRecyclerView() = binding.rvAllSongs.apply {
        adapter = musicAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun subscribeToObservers() {
        mainViewModel.mediaItems.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                Status.SUCCESS -> {
                    binding.allSongsProgressBar.isVisible = false

                    result.data?.let { musics ->
                        musicAdapter.musics = musics
                    }

                }

                Status.LOADING -> {
                    binding.allSongsProgressBar.isVisible = true
                }

                Status.ERROR -> {
                    Unit
                }
            }
        }
    }


}