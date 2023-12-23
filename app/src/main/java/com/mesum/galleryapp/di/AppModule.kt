package com.mesum.galleryapp.di

import android.content.Context
import com.mesum.galleryapp.data.AlbumRepositoryImpl
import com.mesum.galleryapp.data.MediaRepositoryImpl
import com.mesum.galleryapp.domain.repository.AlbumRepository
import com.mesum.galleryapp.domain.repository.MediaRepository
import com.mesum.galleryapp.domain.usecase.GetAlbumsUseCase
import com.mesum.galleryapp.domain.usecase.GetMediaUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideAlbumRepository(): AlbumRepository {
        return AlbumRepositoryImpl()
    }


    @Singleton
    @Provides
    fun provideAlbumUseCase(albumRepository: AlbumRepository): GetAlbumsUseCase{
        return GetAlbumsUseCase(albumRepository)
    }

    @Provides
    fun provideMediaRepository(@ApplicationContext context: Context): MediaRepository {
        return MediaRepositoryImpl(context)
    }

    @Provides
    fun provideGetMediaUseCase(mediaRepository: MediaRepository): GetMediaUseCase {
        return GetMediaUseCase(mediaRepository)
    }

}
