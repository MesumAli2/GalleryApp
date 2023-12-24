package com.mesum.galleryapp.data.repository

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import com.mesum.galleryapp.domain.repository.MediaRepository
import android.provider.MediaStore
import com.mesum.galleryapp.data.MediaItem

class MediaRepositoryImpl(private val context: Context) : MediaRepository {

    override fun getMedia(albumId: String): List<MediaItem> {
        val mediaList = mutableListOf<MediaItem>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
            // You can add DATE_ADDED or DATE_TAKEN if you want to use them in your logic
        )

        // Define the selection criteria (filter)
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"

        // Define the selection arguments
        val selectionArgs = arrayOf(albumId)

        // Query URI for images
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        // Adding sort order - Newest first based on DATE_ADDED
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

        context.contentResolver.query(
            queryUri,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)

            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val name = cursor.getString(nameColumn)
                val contentUri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL).buildUpon()
                    .appendPath(id.toString())
                    .build()

                mediaList.add(MediaItem(contentUri, name))
            }
        }

        return mediaList
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



