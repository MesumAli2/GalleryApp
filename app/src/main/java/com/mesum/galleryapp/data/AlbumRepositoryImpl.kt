package com.mesum.galleryapp.data


import android.content.ContentUris
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
                    // Always update the imageUri to the latest one
                    albumMap[albumId] = Triple(albumName, imageUri, newCount)
                }
            }
        }

        albumMap.forEach { (albumId ,albumInfo) ->
            albums.add(Album(id = albumId, name = albumInfo.first, firstImageUri = albumInfo.second, mediaCount = albumInfo.third))
        }

        return albums
    }

    fun loadAllPicturesAlbums(context: Context): Album?{
        val projection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media._ID
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        var album : Album? = null
        context.contentResolver.query(contentUri, projection, null, null, sortOrder)?.use {
            val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)
            while (it.moveToNext()){
                val id = it.getLong(idColumn)
                val name = it.getString(nameColumn)
                val mediaUri = ContentUris.withAppendedId(contentUri, id)

            }

        }
        return album

    }
    override fun loadAllPictures(context: Context): List<MediaItem> {
        return loadMedia(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    }

    override fun loadAllVideos(context: Context): List<MediaItem> {
        return loadMedia(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
    }

    private fun loadMedia(context: Context, contentUri: Uri): List<MediaItem> {
        val mediaList = mutableListOf<MediaItem>()

        val projection = arrayOf(
            MediaStore.MediaColumns._ID,
            MediaStore.MediaColumns.DISPLAY_NAME
        )
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            contentUri,
            projection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val mediaUri = ContentUris.withAppendedId(contentUri, id)
                mediaList.add(MediaItem(mediaUri, name))
            }
        }

        return mediaList
    }
}
