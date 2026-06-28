package com.mysanjeevni.mysanjeevni.features.auth.domain.model

sealed class AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>()
    data class Error(val message: String) : AuthResult<Nothing>()
    object Loading : AuthResult<Nothing>()
    object Idle : AuthResult<Nothing>()
}