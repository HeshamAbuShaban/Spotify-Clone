package dev.training.spotify_clone.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import com.bumptech.glide.RequestManager
import dev.training.spotify_clone.R
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.databinding.ItemMusicBinding
import javax.inject.Inject

class MusicAdapter @Inject constructor(
    private val glide: RequestManager,
) : AppBaseAdapter(R.layout.item_music) {
    private lateinit var binding: ItemMusicBinding

    override val differ = AsyncListDiffer(this, diffCallback)
    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musics[position]
        binding = ItemMusicBinding.bind(holder.itemView)
        bindData(binding, music)
    }

    private fun bindData(binding: ItemMusicBinding, music: Music) {
        with(binding) {
            glide.load(music.imageUrl).into(ivItemImage)
            tvPrimary.text = music.title
            tvSecondary.text = music.subtitle

            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(music)
                }
            }
        }
    }

}