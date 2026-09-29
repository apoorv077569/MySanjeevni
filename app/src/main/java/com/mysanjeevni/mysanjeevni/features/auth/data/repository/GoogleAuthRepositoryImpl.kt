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

    companion object {
        private const val TAG = "GOOGLE_DEBUG"
    }

    override suspend fun loginWithGoogle(
        googleIdToken: String
    ): Result<AuthResponseDto> {

        Log.d(TAG, "========== GOOGLE LOGIN START ==========")
        Log.d(TAG, "Google ID Token received")
        Log.d(TAG, "Token length = ${googleIdToken.length}")

        return try {

            // STEP 1
            Log.d(TAG, "STEP 1: Creating Firebase credential")

            val credential = GoogleAuthProvider.getCredential(
                googleIdToken,
                null
            )

            Log.d(TAG, "STEP 1 SUCCESS: Firebase credential created")

            // STEP 2
            Log.d(TAG, "STEP 2: Firebase signInWithCredential")

            val firebaseUser =
                suspendCancellableCoroutine { cont ->

                    FirebaseAuth.getInstance()
                        .signInWithCredential(credential)
                        .addOnSuccessListener { result ->

                            val user = result.user

                            Log.d(
                                TAG,
                                "STEP 2 SUCCESS: Firebase sign-in successful"
                            )

                            Log.d(
                                TAG,
                                "Firebase UID = ${user?.uid}"
                            )

                            Log.d(
                                TAG,
                                "Firebase Email = ${user?.email}"
                            )

                            if (user != null) {
                                cont.resume(user)
                            } else {
                                cont.resumeWith(
                                    Result.failure(
                                        Exception("Firebase user is null")
                                    )
                                )
                            }
                        }
                        .addOnFailureListener { exception ->

                            Log.e(
                                TAG,
                                "STEP 2 FAILED: Firebase sign-in failed",
                                exception
                            )

                            cont.resumeWith(
                                Result.failure(exception)
                            )
                        }
                }

            // STEP 3
            Log.d(TAG, "STEP 3: Getting Firebase ID token")

            val firebaseToken =
                suspendCancellableCoroutine { cont ->

                    firebaseUser
                        .getIdToken(true)
                        .addOnSuccessListener { result ->

                            val token = result.token

                            Log.d(
                                TAG,
                                "STEP 3 SUCCESS: Firebase ID token received"
                            )

                            Log.d(
                                TAG,
                                "Firebase token length = ${token?.length}"
                            )

                            if (token != null) {

                                cont.resume(token)

                            } else {

                                cont.resumeWith(
                                    Result.failure(
                                        Exception(
                                            "Firebase ID token is null"
                                        )
                                    )
                                )
                            }
                        }
                        .addOnFailureListener { exception ->

                            Log.e(
                                TAG,
                                "STEP 3 FAILED: Unable to get Firebase ID token",
                                exception
                            )

                            cont.resumeWith(
                                Result.failure(exception)
                            )
                        }
                }

            // STEP 4
            Log.d(TAG, "STEP 4: Sending Firebase token to backend")

            val response = try {

                authRepository.googleLogin(firebaseToken)

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "STEP 4 FAILED: Backend request exception",
                    e
                )

                throw e
            }

            Log.d(
                TAG,
                "STEP 4 RESPONSE: HTTP ${response.code()}"
            )

            if (response.isSuccessful && response.body() != null) {

                Log.d(
                    TAG,
                    "STEP 4 SUCCESS: Backend Google login successful"
                )

                Result.success(response.body()!!)

            } else {

                val errorBody =
                    response.errorBody()?.string()

                Log.e(
                    TAG,
                    "STEP 4 FAILED: Backend rejected login"
                )

                Log.e(
                    TAG,
                    "HTTP Code = ${response.code()}"
                )

                Log.e(
                    TAG,
                    "Error Body = $errorBody"
                )

                Result.failure(
                    Exception(
                        errorBody ?: "Google Login Failed"
                    )
                )
            }

        } catch (e: Exception) {

            Log.e(
                TAG,
                "GOOGLE LOGIN EXCEPTION",
                e
            )

            Result.failure(e)

        } finally {

            Log.d(TAG, "========== GOOGLE LOGIN END ==========")
        }
    }
}