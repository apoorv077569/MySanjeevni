package com.mysanjeevni.mysanjeevni.features.profile.domain.model

data class ImageUploadData(
    val bytes: ByteArray,
    val fileName: String,
    val mimeType: String
)