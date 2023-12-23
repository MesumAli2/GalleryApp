package com.mesum.galleryapp.domain.repository

import com.mesum.galleryapp.data.MediaItem

interface MediaRepository {
    fun getMedia(albumId: String): List<MediaItem>
}
