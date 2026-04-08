package com.mysanjeevni.mysanjeevni.data.remote.api

import retrofit2.Response
import com.mysanjeevni.mysanjeevni.data.remote.model.AuthResponse
import com.mysanjeevni.mysanjeevni.data.remote.model.LoginRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.RegisterRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.UpdateProfileRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @POST("api/user/signup")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @GET("api/auth/profile")
    suspend fun getProfile(
        @Header("Authorization") token: String
    ): Response<AuthResponse>

    @PUT("api/auth/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body request: UpdateProfileRequest
    ): Response<AuthResponse>
}