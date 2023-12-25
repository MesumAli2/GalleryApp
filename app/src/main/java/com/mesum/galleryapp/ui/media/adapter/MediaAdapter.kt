package com.mesum.galleryapp.ui.media.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mesum.galleryapp.data.MediaItem
import com.mesum.galleryapp.databinding.ItemMediaBinding

class MediaAdapter() : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    private var mediaList: List<MediaItem> = emptyList()
    class MediaViewHolder(val binding: ItemMediaBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mediaItem: MediaItem) {
            val displayMetrics = binding.root.context.resources.displayMetrics
            val halfScreenHeight = displayMetrics.heightPixels / 3
            binding.imageViewMedia.layoutParams.height = halfScreenHeight
            Glide.with(binding.imageViewMedia.context)
                .load(mediaItem.uri)
                .into(binding.imageViewMedia)
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
    fun setItems(photos: List<MediaItem>) {
        mediaList = photos
        notifyDataSetChanged()
    }
}

