package com.mesum.galleryapp.ui.Albums.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.mesum.galleryapp.data.Album
import com.mesum.galleryapp.databinding.ItemAlbumBinding
import com.mesum.galleryapp.databinding.ItemAlbumLinearBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.mesum.galleryapp.R

class AlbumAdapter(private val albumClickListener: (String, String) -> Unit) : ListAdapter<Album, RecyclerView.ViewHolder>(AlbumDiffCallback()) {
    var isGridLayoutManager: Boolean = true // Default is grid layout

    companion object {
        const val VIEW_TYPE_GRID = 1
        const val VIEW_TYPE_LINEAR = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (isGridLayoutManager) VIEW_TYPE_GRID else VIEW_TYPE_LINEAR
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_GRID) {
            val binding = ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AlbumViewHolder(binding)
        } else {
            val binding = ItemAlbumLinearBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            AlbumLinearViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val album = getItem(position)
        if (holder is AlbumViewHolder) {
            bindGridHolder(holder, album)
        } else if (holder is AlbumLinearViewHolder) {
            bindLinearHolder(holder, album)
        }
    }

    private fun bindGridHolder(holder: AlbumViewHolder, album: Album) {
        // Bind grid view holder
        holder.binding.textViewAlbumName.text = album.name
        holder.binding.textViewAlbumCount.text = album.mediaCount.toString()
        setAlbumImage(holder.binding.imageViewAlbum, album)
        holder.itemView.setOnClickListener { albumClickListener(album.id, album.name) }
    }

    private fun bindLinearHolder(holder: AlbumLinearViewHolder, album: Album) {
        // Bind linear view holder
        holder.binding.textViewAlbumName.text = album.name
        holder.binding.textViewAlbumCount.text = album.mediaCount.toString()
        setAlbumImage(holder.binding.imageViewAlbum, album)
        holder.itemView.setOnClickListener { albumClickListener(album.id, album.name) }
    }

    private fun setAlbumImage(imageView: ImageView, album: Album) {
        if (album.firstImageUri != null) {
            Glide.with(imageView.context)
                .load(album.firstImageUri)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.ic_launcher_background)
        }
    }

    class AlbumViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root)
    class AlbumLinearViewHolder(val binding: ItemAlbumLinearBinding) : RecyclerView.ViewHolder(binding.root)

    class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.name == newItem.name // or another unique identifier
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
}
