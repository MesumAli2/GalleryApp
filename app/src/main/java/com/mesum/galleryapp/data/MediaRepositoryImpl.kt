package com.mesum.galleryapp.data

import android.content.Context
import com.mesum.galleryapp.domain.repository.MediaRepository
import android.provider.MediaStore

class MediaRepositoryImpl(private val context: Context) : MediaRepository {

    override fun getMedia(albumId: String): List<MediaItem> {
        val mediaList = mutableListOf<MediaItem>()

        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DISPLAY_NAME
            // Add more columns as needed
        )

        // Define the selection criteria (filter)
        val selection = "${MediaStore.Images.Media.BUCKET_ID} = ?"

        // Define the selection arguments
        val selectionArgs = arrayOf(albumId)

        // Query URI for images
        val queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        context.contentResolver.query(
            queryUri,
            projection,
            selection,
            selectionArgs,
            null // Add sort order if needed
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
}


