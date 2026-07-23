package com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.ImageUploadData
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.ProfileUpdate
import com.mysanjeevni.mysanjeevni.features.profile.domain.usecase.GetProfileUseCase
import com.mysanjeevni.mysanjeevni.features.profile.domain.usecase.UpdateProfileImageUseCase
import com.mysanjeevni.mysanjeevni.features.profile.domain.usecase.UpdateProfileUseCase
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val uploadProfileImageUseCase: UpdateProfileImageUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _state = MutableStateFlow(EditProfileState())
    val state = _state.asStateFlow()
    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }
            if (userId.isBlank()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "User ID is required"
                    )
                }
                return@launch
            }
            getProfileUseCase(userId)
                .onSuccess { profile ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userId = profile.id,
                            fullName = profile.fullName,
                            email = profile.email,
                            phone = profile.phone,
                            address = profile.address,
                            profileImage = profile.profileImage,
                            errorMessage = null
                        )
                    }
                }
                .onFailure { exception ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message
                                ?: "Failed to load profile"
                        )
                    }
                }
        }
    }
    fun updateProfile() {

        Log.d("EDIT_PROFILE_UPDATE", "updateProfile() called")

        viewModelScope.launch {
            Log.d("EDIT_PROFILE_UPDATE", "Update coroutine started")

            _state.update {
                it.copy(
                    isLoading = true,
                    errorMessage = null,
                    successMessage = null
                )
            }
            try {
                val currentState = _state.value

                Log.d("EDIT_PROFILE_UPDATE", "User ID: ${currentState.userId}")
                Log.d("EDIT_PROFILE_UPDATE", "Existing Image URL: ${currentState.profileImage}")
                Log.d("EDIT_PROFILE_UPDATE", "Selected Image URI: ${currentState.selectedImageUri}")

                var finalProfileImageUrl = currentState.profileImage

                if (currentState.selectedImageUri != null) {
                    Log.d("EDIT_PROFILE_IMAGE", "New image selected. Preparing image...")

                    val imageData = prepareImageForUpload(
                        uri = currentState.selectedImageUri
                    )

                    Log.d("EDIT_PROFILE_IMAGE", "Image prepared successfully")
                    Log.d("EDIT_PROFILE_IMAGE", "File Name: ${imageData.fileName}")
                    Log.d("EDIT_PROFILE_IMAGE", "Mime Type: ${imageData.mimeType}")
                    Log.d("EDIT_PROFILE_IMAGE", "Size: ${imageData.bytes.size} bytes")
                    Log.d("EDIT_PROFILE_IMAGE", "Calling uploadProfileImageUseCase...")

                    val uploadResult = uploadProfileImageUseCase(
                        imageBytes = imageData.bytes,
                        fileName = imageData.fileName,
                        mimeType = imageData.mimeType
                    )

                    uploadResult
                        .onSuccess { imageUrl ->
                            Log.d("EDIT_PROFILE_IMAGE", "Image upload successful")
                            Log.d("EDIT_PROFILE_IMAGE", "Cloudinary URL: $imageUrl")

                            finalProfileImageUrl = imageUrl
                        }
                        .onFailure { exception ->
                            Log.e("EDIT_PROFILE_IMAGE", "Image upload failed: ${exception.message}", exception)
                        }

                    if (uploadResult.isFailure) {
                        val exception = uploadResult.exceptionOrNull()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = exception?.message
                                    ?: "Image upload failed"
                            )
                        }
                        return@launch
                    }
                } else {
                    Log.d("EDIT_PROFILE_IMAGE", "No new image selected")
                    Log.d("EDIT_PROFILE_IMAGE", "Keeping existing image URL: $finalProfileImageUrl")
                }
                Log.d("EDIT_PROFILE_UPDATE", "Creating ProfileUpdate model")

                val profileUpdate = ProfileUpdate(
                    userId = currentState.userId,
                    fullName = currentState.fullName,
                    phone = currentState.phone,
                    fullAddress = currentState.address,
                    profileImage = finalProfileImageUrl
                )
                Log.d("EDIT_PROFILE_UPDATE", "Final Profile Image URL: $finalProfileImageUrl")
                Log.d("EDIT_PROFILE_UPDATE", "Calling updateProfileUseCase...")

                updateProfileUseCase(profileUpdate)
                    .onSuccess { updatedProfile ->
                        Log.d("EDIT_PROFILE_UPDATE", "Profile updated successfully")
                        Log.d("EDIT_PROFILE_UPDATE", "Updated Image URL: ${updatedProfile.profileImage}")
                        _state.update {
                            it.copy(
                                isLoading = false,
                                userId = updatedProfile.id,
                                fullName = updatedProfile.fullName,
                                email = updatedProfile.email,
                                phone = updatedProfile.phone,
                                address = updatedProfile.address,
                                profileImage = updatedProfile.profileImage,
                                selectedImageUri = null,
                                successMessage = "Profile Updated Successfully",
                                errorMessage = null,
                                navigateBack = true
                            )
                        }
                    }
                    .onFailure { exception ->
                        Log.e("EDIT_PROFILE_UPDATE", "Profile update failed: ${exception.message}", exception)
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = exception.message
                                    ?: "Update failed"
                            )
                        }
                    }
            } catch (e: Exception) {
                Log.e("EDIT_PROFILE_UPDATE", "Unexpected error: ${e.message}", e)
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                            ?: "Something went wrong"
                    )
                }
            }
        }
    }
    fun onNavigatedBack() {
        _state.update { it.copy(navigateBack = false) }
    }
    fun onNameChanged(value: String) {
        _state.update {
            it.copy(fullName = value)
        }
    }
    fun onEmailChanged(value: String) {
        _state.update {
            it.copy(email = value)
        }
    }

    fun onPhoneChanged(value: String) {
        _state.update {
            it.copy(phone = value)
        }
    }
    fun onAddressChanged(value: String) {
        _state.update {
            it.copy(address = value)
        }
    }

    fun onImageSelected(uri: Uri) {

        Log.d(
            "EDIT_PROFILE_IMAGE",
            "Image selected"
        )

        Log.d(
            "EDIT_PROFILE_IMAGE",
            "Selected Uri: $uri"
        )

        _state.update {
            it.copy(
                selectedImageUri = uri,
                errorMessage = null
            )
        }
    }

    private fun prepareImageForUpload(uri: Uri): ImageUploadData {

        Log.d("EDIT_PROFILE_IMAGE", "Preparing Uri: $uri")

        val contentResolver = context.contentResolver

        val mimeType = contentResolver.getType(uri)
            ?: throw IllegalArgumentException(
                "Unable to detect image type"
            )

        Log.d("EDIT_PROFILE_IMAGE", "Detected MIME Type: $mimeType")

        val allowedMimeTypes = setOf(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/webp"
        )

        if (mimeType !in allowedMimeTypes) {

            Log.e(
                "EDIT_PROFILE_IMAGE",
                "Unsupported MIME Type: $mimeType"
            )

            throw IllegalArgumentException(
                "Only JPG, JPEG, PNG and WEBP images are allowed"
            )
        }

        val fileName = getFileName(uri)

        val bytes = contentResolver
            .openInputStream(uri)
            ?.use { inputStream ->
                inputStream.readBytes()
            }
            ?: throw IllegalArgumentException(
                "Unable to read selected image"
            )

        Log.d(
            "EDIT_PROFILE_IMAGE",
            "Image bytes read successfully"
        )

        Log.d(
            "EDIT_PROFILE_IMAGE",
            "Image size: ${bytes.size} bytes"
        )

        val maxSize = 5 * 1024 * 1024

        if (bytes.size > maxSize) {
            throw IllegalArgumentException(
                "Image size must be less than 5MB"
            )
        }

        return ImageUploadData(
            bytes = bytes,
            fileName = fileName,
            mimeType = mimeType
        )
    }
    private fun getFileName(
        uri: Uri
    ): String {

        var fileName = "profile_image.jpg"

        context.contentResolver.query(
            uri,
            null,
            null,
            null,
            null
        )?.use { cursor ->

            val nameIndex = cursor.getColumnIndex(
                OpenableColumns.DISPLAY_NAME
            )

            if (
                nameIndex >= 0 &&
                cursor.moveToFirst()
            ) {
                fileName = cursor.getString(nameIndex)
            }
        }

        Log.d(
            "EDIT_PROFILE_IMAGE",
            "Resolved File Name: $fileName"
        )

        return fileName
    }
}