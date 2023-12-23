package com.mesum.galleryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.mesum.galleryapp.domain.usecase.GetMediaUseCase
import com.mesum.galleryapp.ui.adapter.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getMediaUseCase: GetMediaUseCase
) : ViewModel() {

    fun getMedia(albumId: String): List<MediaItem> {
        return getMediaUseCase(albumId)
    }
}
