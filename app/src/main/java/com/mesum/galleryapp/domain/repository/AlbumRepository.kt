package com.mesum.galleryapp.domain.repository

import android.content.Context
import com.mesum.galleryapp.data.Album
import com.mesum.galleryapp.data.MediaItem

interface AlbumRepository {
    fun loadAlbums(context: Context): List<Album>


}
