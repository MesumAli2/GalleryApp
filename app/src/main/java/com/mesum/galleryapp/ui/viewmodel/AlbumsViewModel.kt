package com.mesum.galleryapp.ui.viewmodel
import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mesum.galleryapp.data.Album
import com.mesum.galleryapp.domain.usecase.GetAlbumsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(private val application: Application,private val getAlbumsUseCase: GetAlbumsUseCase) : ViewModel() {

    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums

    init {
        loadAlbums()
    }

    private fun loadAlbums() {
        viewModelScope.launch {
            val albumList = getAlbumsUseCase.invoke(application.applicationContext)
            _albums.value = albumList
        }
    }
}
