package com.mesum.galleryapp.di

import com.mesum.galleryapp.data.AlbumRepositoryImpl
import com.mesum.galleryapp.domain.repository.AlbumRepository
import com.mesum.galleryapp.domain.usecase.GetAlbumsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

    // Provide other dependencies

    @Singleton
    @Provides
    fun provideAlbumUseCase(albumRepository: AlbumRepository): GetAlbumsUseCase{
        return GetAlbumsUseCase(albumRepository)
    }
}
