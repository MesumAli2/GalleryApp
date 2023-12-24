package com.mesum.galleryapp.ui.viewmodel
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesum.galleryapp.common.Constant
import com.mesum.galleryapp.data.Album
import com.mesum.galleryapp.domain.usecase.GetAlbumsUseCase
import com.mesum.galleryapp.domain.usecase.GetAllPhotosUseCase
import com.mesum.galleryapp.domain.usecase.GetAllVideosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(private val application: Application,private val getAlbumsUseCase: GetAlbumsUseCase,
                                          private val getAllPhotosUseCase: GetAllPhotosUseCase, private val getAllVideosUseCase: GetAllVideosUseCase) : ViewModel() {

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums

    init {
      loadAlbums()
    }

     fun loadAlbums() {
         viewModelScope.launch {
             val albumList = getAlbumsUseCase.invoke(application.applicationContext)
             val photos = getAllPhotosUseCase.invoke(application.applicationContext)
             val videos = getAllVideosUseCase.invoke(application.applicationContext)

             val specialCategories = listOf(
                 Album(Constant.allPhotos, Constant.allPhotos,
                     photos.first().uri, photos.size),
                 Album(Constant.allVideos ,Constant.allVideos,videos.first().uri, videos.size)

             )
             _albums.value = specialCategories + albumList

         }
    }
}
