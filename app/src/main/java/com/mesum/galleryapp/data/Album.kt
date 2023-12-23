package com.mesum.galleryapp.data

import android.net.Uri

data class Album(val id: String,val name: String, val firstImageUri: Uri?, val mediaCount: Int)
