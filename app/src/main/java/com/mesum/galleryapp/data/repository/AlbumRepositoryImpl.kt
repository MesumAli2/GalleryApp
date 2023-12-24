package com.mesum.galleryapp.data.repository


import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.mesum.galleryapp.common.Constant
import com.mesum.galleryapp.data.Album
import com.mesum.galleryapp.domain.repository.AlbumRepository

class AlbumRepositoryImpl : AlbumRepository {

    override fun loadAlbums(context: Context): List<Album> {
        val allPicturesAlbum = loadAllPicturesAlbums(context)
        val allVideosAlbum = loadAllVideosAlbums(context)
        val albums = mutableListOf<Album>()

        if (allPicturesAlbum != null) {
            albums.add(allPicturesAlbum)
        }
        if (allVideosAlbum != null) {
            albums.add(allVideosAlbum)
        }
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media._ID
        )

        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        val albumMap = mutableMapOf<String, Triple<String, Uri?, Int>>()

        cursor?.use {
            val albumNameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val albumIdColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_ID)
            val imageIdColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (it.moveToNext()) {
                val albumName = it.getString(albumNameColumn)
                val albumId = it.getString(albumIdColumn)
                val imageId = it.getLong(imageIdColumn)
                val imageUri = Uri.withAppendedPath(uri, imageId.toString())

                val albumInfo = albumMap[albumId]
                if (albumInfo == null) {
                    albumMap[albumId] = Triple(albumName, imageUri, 1)
                } else {
                    val newCount = albumInfo.third + 1
                    albumMap[albumId] = Triple(albumName, imageUri, newCount)
                }
            }
        }

        albumMap.forEach { (albumId ,albumInfo) ->
            albums.add(Album(id = albumId, name = albumInfo.first, firstImageUri = albumInfo.second, mediaCount = albumInfo.third))
        }

        return albums
    }

    private fun loadAllPicturesAlbums(context: Context): Album? {


        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID
        )
        val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        var latestImageUri: Uri? = null
        var totalCount = 0

        context.contentResolver.query(contentUri, projection, null, null, sortOrder)?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            while (cursor.moveToNext()) {
                if (latestImageUri == null) {
                    val imageId = cursor.getLong(idColumn)
                    latestImageUri = Uri.withAppendedPath(uri, imageId.toString())
                }
                totalCount++
            }
        }

        return if (totalCount > 0) {
            Album(Constant.allPhotos, Constant.allPhotos, latestImageUri, totalCount)
        } else {
            null
        }
    }

    private fun loadAllVideosAlbums(context: Context): Album? {
        val projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATE_ADDED
        )
        val uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"
        var latestVideoUri: Uri? = null
        var totalCount = 0

        context.contentResolver.query(uri, projection, null, null, sortOrder)?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)

            while (cursor.moveToNext()) {
                if (latestVideoUri == null) {
                    val videoId = cursor.getLong(idColumn)
                    latestVideoUri = Uri.withAppendedPath(uri, videoId.toString())
                }
                totalCount++
            }
        }

        return if (totalCount > 0) {
            Album(Constant.allVideos, Constant.allVideos, latestVideoUri, totalCount)
        } else {
            null
        }
    }


}
