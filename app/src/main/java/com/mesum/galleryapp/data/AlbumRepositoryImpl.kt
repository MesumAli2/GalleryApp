package com.mesum.galleryapp.data


import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.mesum.galleryapp.domain.repository.AlbumRepository

class AlbumRepositoryImpl : AlbumRepository {

    override fun loadAlbums(context: Context): List<Album> {
        val albums = mutableListOf<Album>()

        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media._ID
        )

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        val albumMap = mutableMapOf<String, Pair<Uri?, Int>>()

        cursor?.use {
            val albumNameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val imageIdColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (it.moveToNext()) {
                val albumName = it.getString(albumNameColumn)
                val albumId = it.getString(albumIdColumn)
                val imageId = it.getLong(imageIdColumn)
                val imageUri = Uri.withAppendedPath(uri, imageId.toString())

                val count = albumMap[albumId]?.second?.plus(1) ?: 1
                albumMap[albumId] = Pair(imageUri, count)
            }
        }

        albumMap.entries.forEach { entry ->
            val albumName = entry.key
            val firstImageUri = entry.value.first
            val count = entry.value.second
            albums.add(Album(albumName, firstImageUri, count))
        }

        return albums
    }
}

