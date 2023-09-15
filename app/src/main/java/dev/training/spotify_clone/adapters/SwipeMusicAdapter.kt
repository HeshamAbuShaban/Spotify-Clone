package dev.training.spotify_clone.adapters

import androidx.recyclerview.widget.AsyncListDiffer
import dev.training.spotify_clone.R
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.databinding.ItemSwipeMusicBinding

class SwipeMusicAdapter : AppBaseAdapter(R.layout.item_swipe_music) {
    private lateinit var binding: ItemSwipeMusicBinding

    override val differ = AsyncListDiffer(this, diffCallback)
    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musics[position]
        binding = ItemSwipeMusicBinding.bind(holder.itemView)
        bindData(binding, music)
    }

    private fun bindData(binding: ItemSwipeMusicBinding, music: Music) {
        with(binding) {

            val text = "${music.title} - ${music.subtitle}"
            tvPrimary.text = text

            root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(music)
                }
            }
        }
    }

}