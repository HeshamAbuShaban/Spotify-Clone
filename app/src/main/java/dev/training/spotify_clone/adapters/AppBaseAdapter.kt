package dev.training.spotify_clone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import dev.training.spotify_clone.data.entities.Music

abstract class AppBaseAdapter(
    private val layoutId: Int,
) : RecyclerView.Adapter<AppBaseAdapter.MusicViewHolder>() {

    class MusicViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val diffCallback =
        object : DiffUtil.ItemCallback<Music>() {
            override fun areItemsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem.mediaId == newItem.mediaId
            }

            override fun areContentsTheSame(oldItem: Music, newItem: Music): Boolean {
                return oldItem.hashCode() == newItem.hashCode()
            }
        }

    protected abstract val differ: AsyncListDiffer<Music>

    var musics: List<Music>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder {
        return MusicViewHolder(
            LayoutInflater.from(parent.context).inflate(
                layoutId,
                parent,
                false
            )
        )
    }



    override fun getItemCount(): Int = musics.size

    // Listeners
    protected var onItemClickListener: ((Music) -> Unit)? = null

    fun setOnItemClickListener(listener: ((Music) -> Unit)) {
        this.onItemClickListener = listener
    }

}