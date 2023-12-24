package com.mesum.galleryapp.domain.repository

import android.content.Context
import com.mesum.galleryapp.data.MediaItem

interface MediaRepository {
    fun getMedia(albumId: String): List<MediaItem>
    fun loadAllPictures(context: Context): List<MediaItem>
    fun loadAllVideos(context: Context): List<MediaItem>
}
