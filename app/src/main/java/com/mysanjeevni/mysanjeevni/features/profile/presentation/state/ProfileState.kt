package com.mysanjeevni.mysanjeevni.features.profile.presentation.state

import com.mysanjeevni.mysanjeevni.data.remote.model.User

data class ProfileState (

    val isLoading: Boolean = false,
    val user: User?=null,
    val error: String?=null
)