package com.mesum.galleryapp

import android.content.Context
import com.mesum.galleryapp.domain.usecase.media.GetAllPhotosUseCase
import com.mesum.galleryapp.domain.usecase.media.GetAllVideosUseCase
import com.mesum.galleryapp.domain.usecase.media.GetMediaUseCase
import com.mesum.galleryapp.ui.media.viewmodel.MediaViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MediaViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()
    @Mock
    private lateinit var getMediaUseCase: GetMediaUseCase

    @Mock
    private lateinit var getAllPhotosUseCase: GetAllPhotosUseCase

    @Mock
    private lateinit var getAllVideosUseCase: GetAllVideosUseCase

    @Mock
    private lateinit var context: Context

    private lateinit var mediaViewModel: MediaViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        MockitoAnnotations.initMocks(this)
        mediaViewModel = MediaViewModel(
            getMediaUseCase = getMediaUseCase,
            getAllPhotosUseCase = getAllPhotosUseCase,
            getAllVideosUseCase = getAllVideosUseCase,
            context = context
        )
    }
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }
    @Test
    fun loadMedia_callsUseCase() {
        val albumId = "albumId"
        mediaViewModel.getMedia(albumId)
        verify(getMediaUseCase).invoke(albumId)
    }

}
