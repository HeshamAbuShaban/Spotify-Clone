package dev.training.spotify_clone.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import dev.training.spotify_clone.data.entities.Music
import dev.training.spotify_clone.databinding.ItemMusicBinding
import javax.inject.Inject

class MusicAdapter @Inject constructor(
    private val glide: RequestManager,
) : RecyclerView.Adapter<MusicAdapter.MusicViewHolder>() {

    class MusicViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(music: Music) {
            with(binding) {
                tvPrimary.text = music.title
                tvSecondary.text = music.subtitle
            }
        }

        fun giveImageInstance() = binding.ivItemImage

    }

    private val diffCallback =
        object : DiffUtil.ItemCallback<Music>() {
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem.mediaId == newItem.mediaId
            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }

    private val differ = AsyncListDiffer(this, diffCallback)

    var musics: List<Music>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            ItemMusicBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
        val music = musics[position]
        holder.bindData(music)
        glide.load(music.imageUrl).into(holder.giveImageInstance())

        holder.itemView.setOnClickListener {
            onItemClickListener?.let { click ->
                click(music)
            }
        }
    }

    override fun getItemCount(): Int = musics.size

    // Listeners
    private var onItemClickListener: ((Music) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Music) -> Unit)) {
        this.onItemClickListener = listener
    }

}