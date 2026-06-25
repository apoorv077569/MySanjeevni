package com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.profile.data.model.UpdateProfileRequest
import com.mysanjeevni.mysanjeevni.features.profile.domain.usecase.GetProfileUseCase
import com.mysanjeevni.mysanjeevni.features.profile.domain.usecase.UpdateProfileUseCase
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {

    private val _state =
        MutableStateFlow(EditProfileState())

    val state = _state.asStateFlow()


    fun loadProfile(userId: String) {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            try {
                val response =
                    getProfileUseCase(userId)
                if (response.isSuccessful) {
                    val user = response.body()?.user
                    _state.update {
                        it.copy(
                            isLoading = false,
                            userId = user?._id.orEmpty(), // ya user?.id
                            fullName = user?.fullName.orEmpty(),
                            email = user?.email.orEmpty(),
                            phone = user?.phone.orEmpty(),
                            address = user?.address.orEmpty(),
                            profileImage = user?.profileImage
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun updateProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val response = updateProfileUseCase(
                    UpdateProfileRequest(
                        userId = _state.value.userId,
                        fullName = _state.value.fullName,
                        phone = _state.value.phone,
                        fullAddress = _state.value.address
                    )
                )

                if (response.isSuccessful) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            successMessage = "Profile Updated Successfully",
                            navigateBack = true  // ✅ Navigation flag
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Update failed"
                        )
                    }
                }

            } catch (e: Exception) {
                _state.update {
                    it.copy(isLoading = false, errorMessage = e.message)
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
        _state.update {
            it.copy(selectedImageUri = uri)
        }
    }
}