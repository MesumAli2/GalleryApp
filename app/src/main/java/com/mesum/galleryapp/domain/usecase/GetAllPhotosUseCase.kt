package com.mesum.galleryapp.domain.usecase

import android.content.Context
import com.mesum.galleryapp.data.MediaItem
import com.mesum.galleryapp.domain.repository.AlbumRepository

class GetAllPhotosUseCase (private val albumRepository: AlbumRepository){
    operator fun invoke (context: Context): List<MediaItem>{
        return albumRepository.loadAllPictures(context)
    }

}