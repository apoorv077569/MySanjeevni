package com.mysanjeevni.mysanjeevni.features.profile.presentation.state

import com.mysanjeevni.mysanjeevni.features.profile.domain.model.UserProfile

data class ProfileState (
    val isLoading: Boolean = false,
    val user: UserProfile?=null,
    val error: String?=null
)