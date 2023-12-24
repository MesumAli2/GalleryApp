package com.mesum.galleryapp.domain.usecase.media

import android.content.Context
import com.mesum.galleryapp.data.MediaItem
import com.mesum.galleryapp.domain.repository.AlbumRepository
import com.mesum.galleryapp.domain.repository.MediaRepository

class GetAllPhotosUseCase (private val mediaRepository: MediaRepository){
    operator fun invoke (context: Context): List<MediaItem>{
        return mediaRepository.loadAllPictures(context)
    }

}