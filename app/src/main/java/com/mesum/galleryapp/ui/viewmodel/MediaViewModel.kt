package com.mesum.galleryapp.ui.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesum.galleryapp.common.Constant
import com.mesum.galleryapp.data.MediaItem
import com.mesum.galleryapp.domain.usecase.GetAllPhotosUseCase
import com.mesum.galleryapp.domain.usecase.GetAllVideosUseCase
import com.mesum.galleryapp.domain.usecase.GetMediaUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getMediaUseCase: GetMediaUseCase,
    private val getAllPhotosUseCase: GetAllPhotosUseCase, private
    val getAllVideosUseCase: GetAllVideosUseCase,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow<List<MediaItem>>(emptyList())
    val state get() = _state

    fun getMedia(albumId: String) {
        viewModelScope.launch {
            when(albumId){
                Constant.allPhotos ->{
                  _state.value =  getAllPhotosUseCase.invoke(context)
                }
                Constant.allVideos ->{
                    _state.value =  getAllVideosUseCase.invoke(context)

                }
                else -> {
                    _state.value = getMediaUseCase.invoke(albumId)

                }
            }


        }
    }
}
