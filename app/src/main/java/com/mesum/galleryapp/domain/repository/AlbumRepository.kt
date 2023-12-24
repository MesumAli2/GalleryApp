package com.mesum.galleryapp.domain.repository

import android.content.Context
import com.mesum.galleryapp.data.Album
import com.mesum.galleryapp.data.MediaItem

interface AlbumRepository {
    fun loadAlbums(context: Context): List<Album>
    fun loadAllPictures(context: Context): List<MediaItem>
    fun loadAllVideos(context: Context): List<MediaItem>
}
