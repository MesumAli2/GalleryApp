package com.mesum.galleryapp.data

import android.content.Context
import com.mesum.galleryapp.domain.repository.MediaRepository
import com.mesum.galleryapp.ui.adapter.MediaItem

class MediaRepositoryImpl(private val context: Context) : MediaRepository {

    override fun getMedia(albumId: String): List<MediaItem> {
        val mediaList = mutableListOf<MediaItem>()
        // Logic to query MediaStore and fetch media items for the given albumId
        return mediaList
    }
}
