package com.mesum.galleryapp.domain.usecase

import android.app.Application
import android.content.Context
import com.mesum.galleryapp.data.MediaItem
import com.mesum.galleryapp.domain.repository.AlbumRepository
import dagger.hilt.android.qualifiers.ApplicationContext

class GetAllVideosUseCase(private var albumRepository: AlbumRepository) {
    operator fun invoke (context: Context): List<MediaItem>{
        return albumRepository.loadAllVideos(context)
    }
}