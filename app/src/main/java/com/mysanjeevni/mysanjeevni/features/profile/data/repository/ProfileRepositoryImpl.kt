package com.mysanjeevni.mysanjeevni.features.profile.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.profile.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.profile.data.mapper.toRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.ProfileUpdate
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.UserProfile
import com.mysanjeevni.mysanjeevni.features.profile.domain.repository.ProfileRepository
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProfileRepository {

    override suspend fun getProfile(
        userId: String
    ): Result<UserProfile> {
        return try {
            val response = apiService.getProfile(userId)

            if (response.isSuccessful) {
                val userDto = response.body()?.user

                if (userDto != null) {
                    Result.success(userDto.toDomain())
                } else {
                    Result.failure(
                        Exception("Profile data not found")
                    )
                }
            } else {
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Failed to fetch profile"
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateProfile(profileUpdate: ProfileUpdate): Result<UserProfile> {
        return try{
            val requestDto = profileUpdate.toRequestDto()
            val response = apiService.updateProfile(requestDto)
            if (response.isSuccessful){
                val updatedProfile = response.body()?.toDomain()
                if (updatedProfile != null){
                    Result.success(updatedProfile)
                }else{
                    Result.failure(Exception("Update Profile Data not found"))
                }
            }else{
                Result.failure(
                    Exception(response.errorBody()?.string()?:"Failed to update profile")
                )
            }
        }catch(e:Exception){
            Result.failure(e)
        }

    }

    override suspend fun uploadProfileImage(
        imageBytes: ByteArray,
        fileName: String,
        mimeType: String
    ): Result<String> {
        return try {
            Log.d("PROFILE_IMAGE_UPLOAD", "Starting profile image upload")
            Log.d("PROFILE_IMAGE_UPLOAD", "File Name: $fileName")
            Log.d("PROFILE_IMAGE_UPLOAD", "Mime Type: $mimeType")
            Log.d("PROFILE_IMAGE_UPLOAD", "File Size: ${imageBytes.size} bytes")

            val requestBody = imageBytes.toRequestBody(mimeType.toMediaType())

            Log.d("PROFILE_IMAGE_UPLOAD", "RequestBody created successfully")

            val imagePart = MultipartBody.Part.createFormData(
                name = "image",
                filename = fileName,
                body = requestBody
            )

            Log.d("PROFILE_IMAGE_UPLOAD", "MultipartBody.Part created successfully")
            Log.d("PROFILE_IMAGE_UPLOAD", "Calling uploadProfileImage API...")

            val response = apiService.uploadProfileImage(image = imagePart)

            Log.d("PROFILE_IMAGE_UPLOAD", "Response Code: ${response.code()}")

            Log.d("PROFILE_IMAGE_UPLOAD", "Response Successful: ${response.isSuccessful}")

            if (response.isSuccessful) {

                val body = response.body()

                Log.d("PROFILE_IMAGE_UPLOAD", "Response Body: $body")
                Log.d("PROFILE_IMAGE_UPLOAD", "Upload Success Flag: ${body?.success}")
                Log.d("PROFILE_IMAGE_UPLOAD", "Image URL: ${body?.imageUrl}")
                Log.d("PROFILE_IMAGE_UPLOAD", "Public ID: ${body?.publicId}")

                val imageUrl = body?.imageUrl

                if (body?.success == true && !imageUrl.isNullOrBlank()) {

                    Log.d("PROFILE_IMAGE_UPLOAD", "Image uploaded successfully")
                    Log.d("PROFILE_IMAGE_UPLOAD", "Final Cloudinary URL: $imageUrl")

                    Result.success(imageUrl)
                } else {
                    Log.e("PROFILE_IMAGE_UPLOAD", "Invalid upload response")
                    Log.e("PROFILE_IMAGE_UPLOAD", "Body: $body")
                    Result.failure(Exception("Image upload response is invalid"))
                }
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("PROFILE_IMAGE_UPLOAD", "Image upload API failed")
                Log.e("PROFILE_IMAGE_UPLOAD", "Response Code: ${response.code()}")
                Log.e("PROFILE_IMAGE_UPLOAD", "Response Message: ${response.message()}")
                Log.e("PROFILE_IMAGE_UPLOAD", "Error Body: $errorBody")
                Result.failure(Exception(errorBody ?: "Failed to upload profile image")
                )
            }
        } catch (e: Exception) {
            Log.e("PROFILE_IMAGE_UPLOAD", "Upload Exception: ${e.message}", e)
            Log.e("PROFILE_IMAGE_UPLOAD", "Exception Type: ${e.javaClass.simpleName}")
            Result.failure(e)
        }
    }
}