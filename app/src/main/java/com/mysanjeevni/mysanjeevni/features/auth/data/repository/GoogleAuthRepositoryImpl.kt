package com.mysanjeevni.mysanjeevni.features.auth.data.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.AuthResponseDto
import com.mysanjeevni.mysanjeevni.features.auth.domain.repository.GoogleAuthRepository
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

class GoogleAuthRepositoryImpl @Inject constructor(
    private val authRepository: AuthRepository
) : GoogleAuthRepository {

    override suspend fun loginWithGoogle(
        googleIdToken: String
    ): Result<AuthResponseDto> {

        return try {
            Log.d("GOOGLE_DEBUG", "================================")
            Log.d("GOOGLE_DEBUG", "GOOGLE TOKEN")
            Log.d("GOOGLE_DEBUG", googleIdToken)
            Log.d("GOOGLE_DEBUG", "================================")
            val credential =
                GoogleAuthProvider.getCredential(
                    googleIdToken,
                    null
                )

            val firebaseUser =
                suspendCancellableCoroutine { cont ->

                    FirebaseAuth.getInstance()
                        .signInWithCredential(credential)
                        .addOnSuccessListener { result ->

                            val user = result.user

                            if (user != null) {
                                cont.resume(user)
                            } else {
                                cont.resumeWith(
                                    Result.failure(
                                        Exception("Firebase user not found")
                                    )
                                )
                            }
                        }
                        .addOnFailureListener {

                            cont.resumeWith(
                                Result.failure(it)
                            )
                        }
                }

            val firebaseToken =
                suspendCancellableCoroutine { cont ->

                    firebaseUser
                        .getIdToken(true)
                        .addOnSuccessListener {

                            val token = it.token
                            Log.d("GOOGLE_DEBUG", "================================")
                            Log.d("GOOGLE_DEBUG", "FIREBASE TOKEN")
                            Log.d("GOOGLE_DEBUG", token ?: "")
                            Log.d("GOOGLE_DEBUG", "================================")

                            if (token != null) {
                                cont.resume(token)
                            } else {
                                cont.resumeWith(
                                    Result.failure(
                                        Exception("Firebase token is null")
                                    )
                                )
                            }
                        }
                        .addOnFailureListener {

                            cont.resumeWith(
                                Result.failure(it)
                            )
                        }
                }
            Log.d("GOOGLE_DEBUG", "================================")
            Log.d("GOOGLE_DEBUG", "SENDING FIREBASE TOKEN TO BACKEND")
            Log.d("GOOGLE_DEBUG", "Token Length = ${firebaseToken.length}")
            Log.d("GOOGLE_DEBUG", "Token Prefix = ${firebaseToken.take(40)}")
            Log.d("GOOGLE_DEBUG", "================================")
            val response =
                authRepository.googleLogin(firebaseToken)
            Log.d("GOOGLE_DEBUG", "HTTP Code = ${response.code()}")


            if (response.isSuccessful && response.body() != null) {

                Result.success(response.body()!!)

            } else {
                Log.d(
                    "GOOGLE_DEBUG",
                    "Error Body = ${response.errorBody()?.string()}"
                )
                Result.failure(
                    Exception(
                        response.errorBody()?.string()
                            ?: "Google Login Failed"
                    )
                )
            }

        } catch (e: Exception) {

            Result.failure(e)

        }
    }
}