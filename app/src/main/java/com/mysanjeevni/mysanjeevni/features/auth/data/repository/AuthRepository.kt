package com.mysanjeevni.mysanjeevni.features.auth.data.repository

import com.mysanjeevni.mysanjeevni.data.remote.api.ApiService
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.GoogleLoginRequest
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val api: ApiService
) {


    suspend fun googleLogin(
        idToken: String
    ) = api.googleSignin(
        GoogleLoginRequest(idToken)
    )



}



