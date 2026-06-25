package com.mysanjeevni.mysanjeevni.features.wishlist.data.repository

import android.util.Log
import com.mysanjeevni.mysanjeevni.data.remote.api.AuthApiService
import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.AddWishlistRequest
import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.WishlistItemDto
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.model.WishlistItem
import com.mysanjeevni.mysanjeevni.features.wishlist.data.mapper.toDomain
import com.mysanjeevni.mysanjeevni.features.wishlist.domain.repository.WishlistRepository
import javax.inject.Inject
import kotlin.collections.emptyList

class WishlistRepositoryImpl @Inject constructor(
    private val api: AuthApiService
) : WishlistRepository {

    override suspend fun getWishlist(
        userId: String
    ): Result<List<WishlistItem>> {

        return try {

            val response = api.getWishList(userId)

            if (response.isSuccessful) {

                val items = response.body()
                    ?.items
                    ?.map { it.toDomain() }
                    ?: emptyList()

                Result.success(items)

            } else {

                Result.failure(
                    Exception("Error : ${response.code()}")
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }

    override suspend fun addToWishlist(
        request: AddWishlistRequest
    ): Result<WishlistItemDto> {

        return try {

            Log.d("WISHLIST_API", "Request=$request")

            val response = api.addToWishList(request)

            Log.d("WISHLIST_API", "Code=${response.code()}")
            Log.d("WISHLIST_API", "Successful=${response.isSuccessful}")
            Log.d("WISHLIST_API", "Body=${response.body()}")
            Log.e(
                "WISHLIST_API",
                "ErrorBody=${response.errorBody()?.string()}"
            )

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(
                    Exception("Error ${response.code()}")
                )
            }

        } catch (e: Exception) {

            Log.e(
                "WISHLIST_API",
                "Exception=${e.message}",
                e
            )

            Result.failure(e)
        }
    }

    override suspend fun removeFromWishlist(
        userId: String,
        productId: String
    ): Result<Unit> {

        return try {

            val response =
                api.removeFromWishlist(
                    userId,
                    productId
                )

            if (response.isSuccessful) {

                Result.success(Unit)

            } else {

                Result.failure(
                    Exception("Error : ${response.code()}")
                )
            }

        } catch (e: Exception) {

            Result.failure(e)
        }
    }
}