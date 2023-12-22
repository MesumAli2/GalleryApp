package com.mesum.galleryapp.common

import android.Manifest
object Constant{


    val readExternal = Manifest.permission.READ_EXTERNAL_STORAGE
    private val readVideo = Manifest.permission.READ_MEDIA_VIDEO
    private val readImages = Manifest.permission.READ_MEDIA_IMAGES
    val permissions = arrayOf(
        readVideo, readImages
    )
}