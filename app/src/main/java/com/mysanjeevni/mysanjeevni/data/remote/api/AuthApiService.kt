package com.mysanjeevni.mysanjeevni.data.remote.api

import com.mysanjeevni.mysanjeevni.data.remote.model.auth.AuthResponse
import com.mysanjeevni.mysanjeevni.data.remote.model.notification.GenericResponse
import com.mysanjeevni.mysanjeevni.data.remote.model.auth.GoogleLoginRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.notification.SaveTokenRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.notification.SendNotificationRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.upload.UploadResponse
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.CreateLabBookingRequestDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabBookingResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.cancel.CancelBookingResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.history.BookingHistoryResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.history.SyncBookingResponseDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.GetOrdersResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.RazorpayOrderResponse
import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.AddWishlistRequest
import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.GetWishlistResponse
import com.mysanjeevni.mysanjeevni.features.wishlist.data.dto.WishlistItemDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AuthApiService {

    @POST("api/notification/save-token")
    suspend fun saveFcmToken(
        @Body request: SaveTokenRequest
    ): Response<GenericResponse>

    @POST("api/lab-test-bookings")
    suspend fun createLabTestBooking(
        @Header("x-user-id") userId: String?,
        @Body request: CreateLabBookingRequestDto
    ): Response<LabBookingResponseDto>

    @GET("api/lab-test-bookings/history")
    suspend fun getBookingHistory(
        @Header("x-user-id") userId: String?
    ): Response<BookingHistoryResponseDto>

    @Multipart
    @POST("api/upload")
    suspend fun uploadPrescription(
        @Part image: MultipartBody.Part
    ):Response<UploadResponse>


    @POST("api/lab-test-bookings/{id}/cancel")
    suspend fun cancelBooking(
        @Path("id") bookingId: String,
        @Header("x-user-id") userId: String?
    ): Response<CancelBookingResponseDto>

    @POST("api/lab-test-bookings/{id}/sync")
    suspend fun syncBooking(
        @Path("id") bookingId: String,
        @Header("x-user-id") userId: String?
    ): Response<SyncBookingResponseDto>

    @POST("api/orders")
    suspend fun createOrder(
        @Body request: CreateOrderRequest
    ): Response<CreateOrderResponse>

    @GET("api/orders")
    suspend fun getOrders(
        @Query("userId") userId: String?
    ): Response<GetOrdersResponse>

    @GET("api/orders/payments/razorpay/order")
    suspend fun getRazorpayOrder(
        @Query("orderId") orderId: String
    ): Response<RazorpayOrderResponse>

    @POST("api/wishlist")
    suspend fun addToWishList(
        @Body request: AddWishlistRequest
    ): Response<WishlistItemDto>

    @GET("api/wishlist")
    suspend fun getWishList(
        @Query("userId") userId:String
    ): Response<GetWishlistResponse>

    @DELETE("api/wishlist")
    suspend fun removeFromWishlist(
        @Query("userId") userId: String,
        @Query("productId") productId:String
    ): Response<Unit>
    @POST("api/notification/send")
    suspend fun sendNotification(
        @Body request: SendNotificationRequest
    ): Response<GenericResponse>

    @POST("api/auth/signin-google")
    suspend fun googleSignin(
        @Body request: GoogleLoginRequest
    ): Response<AuthResponse>
}