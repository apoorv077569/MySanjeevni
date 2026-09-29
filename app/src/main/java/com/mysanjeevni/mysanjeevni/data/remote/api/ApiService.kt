package com.mysanjeevni.mysanjeevni.data.remote.api

import com.mysanjeevni.mysanjeevni.data.remote.model.notification.GenericResponse
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.GoogleLoginRequest
import com.mysanjeevni.mysanjeevni.features.cart.data.dto.UpdateCartRequest
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.AuthResponseDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.LoginRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.RegisterRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.ResetPasswordRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.SendOtpBeforeSignupRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.SendOtpRequestDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.VerifyOtpBeforeSignupDto
import com.mysanjeevni.mysanjeevni.features.auth.data.dto.VerifyOtpRequestDto
import com.mysanjeevni.mysanjeevni.features.cart.data.dto.CartResponse
import com.mysanjeevni.mysanjeevni.features.category.data.dto.CategoryResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.AgoraTokenRequestDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.AgoraTokenResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.BookConsultationRequestDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.BookConsultationResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.CancelConsultationRequestDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.CancelConsultationResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.ConsultationResponseDto
import com.mysanjeevni.mysanjeevni.features.consult.data.dto.DoctorResponseDto
import com.mysanjeevni.mysanjeevni.features.currency.data.dto.CountryResponseDto
import com.mysanjeevni.mysanjeevni.features.currency.data.dto.ExchangeRateResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.CreateLabBookingRequestDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabBookingResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabTestDetailResponse
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabTestsResponse
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.cancel.CancelBookingResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.history.BookingHistoryResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.history.SyncBookingResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.slot.ServiceabilityResponseDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.slot.SlotsRequestDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.slot.SlotsResponseDto
import com.mysanjeevni.mysanjeevni.features.medicines.data.dto.MedicineDetailResponseDto
import com.mysanjeevni.mysanjeevni.features.medicines.data.dto.MedicineResponseDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CancelOrderRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CancelOrderResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.CreateOrderResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.GetOrdersResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.RazorpayOrderResponse
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.ServiceabilityRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.AddressResponseDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.CreateAddressRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.ProfileImageUploadResponseDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.ProfileResponseDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.UpdateAddressRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.UpdateProfileRequestDto
import com.mysanjeevni.mysanjeevni.features.profile.data.dto.UpdateProfileResponseDto
import com.mysanjeevni.mysanjeevni.features.review.data.dto.CreateReviewRequestDto
import com.mysanjeevni.mysanjeevni.features.review.data.dto.CreateReviewResponseDto
import com.mysanjeevni.mysanjeevni.features.review.data.dto.ReviewListResponseDto
import com.mysanjeevni.mysanjeevni.features.review.data.dto.UpdateReviewRequestDto
import com.mysanjeevni.mysanjeevni.features.review.data.dto.UpdateReviewResponseDto
import com.mysanjeevni.mysanjeevni.features.support.chat.data.dto.ChatResponseDto
import com.mysanjeevni.mysanjeevni.features.support.chat.data.dto.SendChatRequestDto
import com.mysanjeevni.mysanjeevni.features.support.returns.data.dto.ReturnListResponseDto
import com.mysanjeevni.mysanjeevni.features.support.returns.data.dto.ReturnRequestDto
import com.mysanjeevni.mysanjeevni.features.support.returns.data.dto.ReturnResponseDto
import com.mysanjeevni.mysanjeevni.features.support.ticket.data.dto.CreateTicketRequestDto
import com.mysanjeevni.mysanjeevni.features.support.ticket.data.dto.CreateTicketResponseDto
import com.mysanjeevni.mysanjeevni.features.support.ticket.data.dto.TicketsResponseDto
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface ApiService {

    @POST("api/auth/login")
    suspend fun loginUser(
        @Body request: LoginRequestDto
    ): Response<AuthResponseDto>

    @POST("api/user/support/chat")
    suspend fun sendChatMessage(
        @Body request: SendChatRequestDto
    ): Response<ChatResponseDto>


    @GET("api/user/support/chat")
    suspend fun getChatMessages(
        @Query("userId") userId: String
    ): Response<ChatResponseDto>

    @POST("api/user/support/returns")
    suspend fun submitReturnRequest(
        @Body request: ReturnRequestDto
    ): Response<ReturnResponseDto>

    @GET("api/user/support/returns")
    suspend fun getReturnRequests(
        @Query("userId") userId: String
    ): Response<ReturnListResponseDto>

    @GET("api/user/profile")
    suspend fun getProfile(
        @Query("id") userId: String?
    ): Response<ProfileResponseDto>

    @POST("api/inquiries")
    suspend fun raiseTicket(
        @Header("x-user-id") userId: String?,
        @Header("x-user-name") userName: String?,
        @Header("x-user-email") email: String?,
        @Header("x-user-role") role: String?,
        @Body request: CreateTicketRequestDto
    ): Response<CreateTicketResponseDto>

    @GET("api/user/support/tickets")
    suspend fun getTickets(
        @Query("userId") userId: String?
    ): Response<TicketsResponseDto>

    @GET("api/payments/razorpay/order")
    suspend fun getRazorpayOrder(
        @Query("orderId") orderId: String
    ): Response<RazorpayOrderResponse>

    @PUT("api/user/update-profile")
    suspend fun updateProfile(
        @Body request: UpdateProfileRequestDto
    ): Response<UpdateProfileResponseDto>

    @Multipart
    @POST("api/user/upload-profile-image")
    suspend fun uploadProfileImage(
        @Part image: MultipartBody.Part
    ): Response<ProfileImageUploadResponseDto>

    @GET("api/lab-partners/serviceability")
    suspend fun checkServiceability(
        @Query("testId") testId: String,
        @Query("pincode") pincode: String
    ): Response<ServiceabilityResponseDto>

    @POST("api/lab-partners/slots")
    suspend fun searchSlots(
        @Body request: SlotsRequestDto
    ): Response<SlotsResponseDto>

    @GET("api/lab-test-bookings/history")
    suspend fun getBookingHistory(
        @Header("x-user-id") userId: String?
    ): Response<BookingHistoryResponseDto>

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

    @POST("api/auth/signup")
    suspend fun registerUser(
        @Body request: RegisterRequestDto
    ): Response<AuthResponseDto>

    @POST("api/auth/phone/signup")
    suspend fun sendOtpBeforeSignup(
        @Body request: SendOtpBeforeSignupRequestDto
    ): Response<AuthResponseDto>

    @POST("api/auth/phone/verify-signup-otp")
    suspend fun verifyBeforeSignup(
        @Body request: VerifyOtpBeforeSignupDto
    ): Response<AuthResponseDto>

    @GET("api/cart")
    suspend fun getCart(
        @Query("userId") userId: String
    ): Response<CartResponse>

    @POST("api/auth/phone/send-otp")
    suspend fun sendOtp(
        @Body request: SendOtpRequestDto
    ): Response<GenericResponse>

    @POST("api/shiprocket/serviceability")
    suspend fun checkServiceability(
        @Body request: ServiceabilityRequestDto
    ): Response<com.mysanjeevni.mysanjeevni.features.orders.data.dto.ServiceabilityResponseDto>

    @POST("api/auth/forgot-password/reset")
    suspend fun resetPassword(
        @Body request: ResetPasswordRequestDto
    ): Response<GenericResponse>

    @POST("api/auth/phone/verify-otp")
    suspend fun verifyOtp(
        @Body request: VerifyOtpRequestDto
    ): Response<AuthResponseDto>

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
    suspend fun getMedicines(
        @Query("page") page: Int,
        @Query("limit") limit: Int = 20,
        @Query("category") category:String ?= null,
    ): Response<MedicineResponseDto>


    @POST("api/cart")
    suspend fun updateCart(
        @Body request: UpdateCartRequest
    ): Response<CartResponse>

    @POST("api/lab-test-bookings")
    suspend fun createLabTestBooking(
        @Header("x-user-id") userId: String?,
        @Body request: CreateLabBookingRequestDto
    ): Response<LabBookingResponseDto>

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
        @Body address: CreateAddressRequestDto
    ): Response<AddressResponseDto>

    @GET("api/products/popular")
    suspend fun getPopularProducts(): Response<MedicineResponseDto>

    @GET("api/addresses")
    suspend fun getAddresses(
        @Header("Authorization") token: String,
        @Query("userId") userId: String
    ): Response<AddressResponseDto>

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
        @Body address: UpdateAddressRequestDto
    ): Response<AddressResponseDto>

    @GET("api/categories")
    suspend fun getCategories(): Response<CategoryResponseDto>

    @POST("api/auth/google")
    suspend fun googleSignin(
        @Body request: GoogleLoginRequest
    ): Response<AuthResponseDto>


    @PUT("api/orders")
    suspend fun cancelOrder(
        @Body request: CancelOrderRequest
    ): Response<CancelOrderResponse>

    @GET("/api/doctors")
    suspend fun getDoctors(
        @Query("department") department: String? = null,
        @Query("search") search: String? = null
    ): Response<DoctorResponseDto>

    @GET("api/consultations")
    suspend fun getConsultations(
        @Query("userId") userId: String
    ): ConsultationResponseDto

    @POST("api/consultations")
    suspend fun bookConsultation(
        @Body request: BookConsultationRequestDto
    ): Response<BookConsultationResponseDto>

    @PUT("api/consultations/{id}")
    suspend fun cancelConsultation(
        @Path("id") consultationId: String,
        @Body request: CancelConsultationRequestDto
    ): Response<CancelConsultationResponseDto>

    @POST("api/agora/token")
    suspend fun generateAgoraToken(
        @Body request: AgoraTokenRequestDto
    ):Response<AgoraTokenResponseDto>

    @GET
    suspend fun getUserCountry(
        @Url url: String = "https://ipwho.is/"
    ): Response<CountryResponseDto>

    @GET
    suspend fun getExchangeRate(
        @Url url: String = "https://api.exchangerate-api.com/v4/latest/INR"
    ): Response<ExchangeRateResponseDto>
}