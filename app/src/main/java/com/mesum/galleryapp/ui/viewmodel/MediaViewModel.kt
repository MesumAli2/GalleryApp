package com.mesum.galleryapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesum.galleryapp.data.MediaItem
import com.mesum.galleryapp.domain.usecase.GetMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getMediaUseCase: GetMediaUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<List<MediaItem>>(emptyList())
    val state get() = _state

    fun getMedia(albumId: String) {
        viewModelScope.launch {
            _state.value = getMediaUseCase.invoke(albumId)
        }
    }
}
