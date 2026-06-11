package com.mysanjeevni.mysanjeevni.data.remote.api

import com.mysanjeevni.mysanjeevni.data.remote.model.AddToCartRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.AuthResponse
import com.mysanjeevni.mysanjeevni.data.remote.model.CreateAddressRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.GenericResponse
import com.mysanjeevni.mysanjeevni.data.remote.model.LoginRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.MessageResponse
import com.mysanjeevni.mysanjeevni.data.remote.model.RegisterRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.ResetPasswordRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.SendOtpBeforeSignupRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.SendOtpRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.UpdateAddressRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.UpdateCartRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.VerifyOtpRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.WishlistItemResponse
import com.mysanjeevni.mysanjeevni.data.remote.model.WishlistRequest
import com.mysanjeevni.mysanjeevni.data.remote.model.WishlistResponse
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartResponse
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabTestDetailResponse
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabTestsResponse
import com.mysanjeevni.mysanjeevni.features.medicines.data.dto.MedicineDetailResponseDto
import com.mysanjeevni.mysanjeevni.features.medicines.data.dto.MedicineResponseDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.GetOrdersResponse
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.RefundRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.RefundResponse
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.VerifyPaymentRequest
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.VerifyResponse
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressResponse
import com.mysanjeevni.mysanjeevni.features.review.data.dto.CreateReviewRequestDto
import com.mysanjeevni.mysanjeevni.features.review.data.dto.CreateReviewResponseDto
import com.mysanjeevni.mysanjeevni.features.review.data.dto.ReviewListResponseDto
import com.mysanjeevni.mysanjeevni.features.review.data.dto.UpdateReviewRequestDto
import com.mysanjeevni.mysanjeevni.features.review.data.dto.UpdateReviewResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {


    @POST("api/auth/login")
    suspend fun loginUser(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("api/auth/signup")
    suspend fun registerUser(
        @Body request: RegisterRequest
    ): Response<AuthResponse>

    @POST("api/auth/phone/signup")
    suspend fun sendOtpBeforeSignup(
        @Body request: SendOtpBeforeSignupRequest
    ): Response<AuthResponse>

    @POST("api/auth/phone/verify-signup-otp")
    suspend fun verifyBeforeSignup(
        @Body request: VerifyOtpRequest
    ): Response<AuthResponse>

    @GET("api/cart")
    suspend fun getCart(
        @Query("userId") userId:String
    ): Response<CartResponse>

    @POST("api/auth/phone/send-otp")
    suspend fun sendOtp(
        @Body request: SendOtpRequest
    ): Response<GenericResponse>

    @POST("api/auth/reset-password")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequest
    ): Response<GenericResponse>

    @POST("api/auth/phone/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequest
    ): Response<AuthResponse>

    @POST("api/payments/razorpay/verify-order")
    suspend fun verifyPayment(
        @Body request: VerifyPaymentRequest
    ): Response<VerifyResponse>

    @POST("api/payments/razorpay/refund")
    suspend fun refundPayment(
        @Body request: RefundRequest
    ): Response<RefundResponse>

    @GET("api/lab-tests")
    suspend fun getLabTests(
        @Query("category") category: String? = null,
        @Query("search") search: String? = null,
        @Query("gender") gender: String? = null,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): Response<LabTestsResponse>

    @GET("api/lab-tests/{id}")
    suspend fun getLabTestById(
        @Path("id") id: String
    ): Response<LabTestDetailResponse>
    @GET("api/products")
    suspend fun getMedicines(): Response<MedicineResponseDto>

    @POST("api/cart/add")
    suspend fun addToCart(
        @Body request: AddToCartRequest
    ): Response<CartResponse>

    @GET("api/wishlist")
    suspend fun getWishlist(
        @Query("userId") userId: String
    ): Response<WishlistResponse>


    @POST("api/wishlist")
    suspend fun addToWishlist(
        @Body request: WishlistRequest
    ): Response<WishlistItemResponse>


    @DELETE("api/wishlist")
    suspend fun removeFromWishlist(
        @Query("userId") userId: String,
        @Query("productId") productId:String
    ): Response<MessageResponse>

    @POST("api/cart")
    suspend fun updateCart(
        @Body request: UpdateCartRequest
    ): Response<CartResponse>

    @GET("api/reviews")
    suspend fun getReviews(
        @Query("productId") productId: String,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 10
    ): Response<ReviewListResponseDto>


    @POST("api/reviews")
    suspend fun createReview(
        @Header("Authorization") token: String,
        @Body request: CreateReviewRequestDto
    ): Response<CreateReviewResponseDto>


    @PATCH("api/reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: String,
        @Body request: UpdateReviewRequestDto
    ): Response<UpdateReviewResponseDto>


    @DELETE("api/cart")
    suspend fun clearCart(
        @Query("userId") userId: String
    ): Response<MessageResponse>



    @GET("api/orders")
    suspend fun getOrders(
        @Query("userId") userId: String?
    ): Response<GetOrdersResponse>

    @POST("api/orders")
    suspend fun createOrder(
        @Body request: CreateOrderRequest
    ): Response<CreateOrderResponse>


    @GET("api/products/{id}")
    suspend fun getMedicineById(
        @Path("id") id: String
    ): Response<MedicineDetailResponseDto>


    @POST("api/addresses")
    suspend fun addAddress(
        @Header("Authorization") token: String,
        @Body address: CreateAddressRequest
    ): Response<AddressResponse>

    @GET("api/products/popular")
    suspend fun getPopularProducts(): Response<MedicineResponseDto>

    @GET("api/addresses")
    suspend fun getAddresses(
        @Header("Authorization") token: String,
        @Query("userId") userId: String
    ): Response<AddressResponse>

    @DELETE("api/addresses/{id}")
    suspend fun deleteAddress(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Query("userId") userId: String
    ): Response<Unit>

    @PUT("api/addresses/{id}")
    suspend fun updateAddress(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body address: UpdateAddressRequest
    ): Response<AddressResponse>


}