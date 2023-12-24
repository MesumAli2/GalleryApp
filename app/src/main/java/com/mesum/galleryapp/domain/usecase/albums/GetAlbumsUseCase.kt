package com.mesum.galleryapp.domain.usecase.albums

import android.content.Context
import com.mesum.galleryapp.data.Album
import com.mesum.galleryapp.domain.repository.AlbumRepository

class GetAlbumsUseCase(private val albumRepository: AlbumRepository) {
    operator fun invoke(context: Context): List<Album> {
        return albumRepository.loadAlbums(context)
    }
}
