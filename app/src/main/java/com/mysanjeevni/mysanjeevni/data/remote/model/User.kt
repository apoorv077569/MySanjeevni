package com.mysanjeevni.mysanjeevni.data.remote.model

data class User(
    val _id: String,
    val fullName: String,

    val phone: String,
    val role: String,
    val email: String,
    val profileImage: String
)