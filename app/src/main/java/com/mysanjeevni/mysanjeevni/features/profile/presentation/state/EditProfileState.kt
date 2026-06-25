package com.mysanjeevni.mysanjeevni.features.profile.presentation.state

import android.net.Uri

data class EditProfileState(
    val isLoading: Boolean = false,

    val userId: String = "",

    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val address: String = "",

    val profileImage: String? = null,

    val selectedImageUri: Uri? = null,

    val successMessage: String? = null,
    val errorMessage: String? = null,
    val navigateBack: Boolean = false
)