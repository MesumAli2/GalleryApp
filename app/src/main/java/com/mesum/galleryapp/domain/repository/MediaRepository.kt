package com.mesum.galleryapp.domain.repository

import com.mesum.galleryapp.ui.adapter.MediaItem

interface MediaRepository {
    fun getMedia(albumId: String): List<MediaItem>
}
