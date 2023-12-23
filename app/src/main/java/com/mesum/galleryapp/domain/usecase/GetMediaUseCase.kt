package com.mesum.galleryapp.domain.usecase

import com.mesum.galleryapp.data.MediaItem
import com.mesum.galleryapp.domain.repository.MediaRepository

class GetMediaUseCase(private val mediaRepository: MediaRepository) {
    operator fun invoke(albumId: String): List<MediaItem> {
        return mediaRepository.getMedia(albumId)
    }
}
