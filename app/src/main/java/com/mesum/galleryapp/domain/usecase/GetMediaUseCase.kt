package com.mesum.galleryapp.domain.usecase

import com.mesum.galleryapp.domain.repository.MediaRepository
import com.mesum.galleryapp.ui.adapter.MediaItem

class GetMediaUseCase(private val mediaRepository: MediaRepository) {
    operator fun invoke(albumId: String): List<MediaItem> {
        return mediaRepository.getMedia(albumId)
    }
}
