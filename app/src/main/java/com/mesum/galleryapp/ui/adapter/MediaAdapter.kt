package com.mesum.galleryapp.ui.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mesum.galleryapp.databinding.ItemMediaBinding

class MediaAdapter(private val mediaList: List<MediaItem>) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    class MediaViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaItem: MediaItem) {
            // Assuming MediaItem has a Uri or path for the image
            Glide.with(binding.imageViewMedia.context)
                .load(mediaItem.uri)
                .into(binding.imageViewMedia)
            // Set other views in the item as needed
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMediaBinding.inflate(inflater, parent, false)
        return MediaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val mediaItem = mediaList[position]
        holder.bind(mediaItem)
    }

    override fun getItemCount() = mediaList.size
}

data class MediaItem(val uri: Uri) // Define your MediaItem class based on your data model