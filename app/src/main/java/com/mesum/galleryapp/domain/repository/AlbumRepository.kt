package com.mesum.galleryapp.domain.repository

import android.content.Context
import com.mesum.galleryapp.data.Album

interface AlbumRepository {
    fun loadAlbums(context: Context): List<Album>
}
