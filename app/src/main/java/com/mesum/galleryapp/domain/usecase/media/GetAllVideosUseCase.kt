package com.mesum.galleryapp.domain.usecase.media

import android.app.Application
import android.content.Context
import com.mesum.galleryapp.data.MediaItem
import com.mesum.galleryapp.domain.repository.AlbumRepository
import com.mesum.galleryapp.domain.repository.MediaRepository
import dagger.hilt.android.qualifiers.ApplicationContext

class GetAllVideosUseCase(private var mediaRepository: MediaRepository) {
    operator fun invoke (context: Context): List<MediaItem>{
        return mediaRepository.loadAllVideos(context)
    }
}