package com.mesum.galleryapp.ui.Albums.viewmodel
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesum.galleryapp.data.Album
import com.mesum.galleryapp.domain.usecase.albums.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(private val application: Application, private val getAlbumsUseCase: GetAlbumsUseCase, ) : ViewModel() {

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums

    init {
      loadAlbums()
    }

     fun loadAlbums() {
         viewModelScope.launch {
             val albumList = getAlbumsUseCase.invoke(application.applicationContext)
             _albums.value =  albumList

         }
    }
}
