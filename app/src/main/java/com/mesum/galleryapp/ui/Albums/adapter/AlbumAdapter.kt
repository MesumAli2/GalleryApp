package com.mesum.galleryapp.ui.Albums.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mesum.galleryapp.data.Album
import com.mesum.galleryapp.databinding.ItemAlbumBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.mesum.galleryapp.R

class AlbumAdapter(private val albumClickListener: (String, String) -> Unit) : ListAdapter<Album, AlbumAdapter.AlbumViewHolder>(
    AlbumDiffCallback()
) {

    class AlbumViewHolder(val binding: ItemAlbumBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(ItemAlbumBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val album = getItem(position)
        holder.binding.textViewAlbumName.text = album.name
        holder.binding.textViewAlbumCount.text = album.mediaCount.toString()
        if (album.firstImageUri != null) {
            Glide.with(holder.binding.imageViewAlbum.context)
                .load(album.firstImageUri)
                .into(holder.binding.imageViewAlbum)
        } else {
            holder.binding.imageViewAlbum.setImageResource(R.drawable.ic_launcher_background)
        }
        holder.itemView.setOnClickListener {
            albumClickListener(album.id, album.name)
        }

    }

    class AlbumDiffCallback : DiffUtil.ItemCallback<Album>() {
        override fun areItemsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem.name == newItem.name // or another unique identifier
        }

        override fun areContentsTheSame(oldItem: Album, newItem: Album): Boolean {
            return oldItem == newItem
        }
    }
}
