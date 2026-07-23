package com.mysanjeevni.mysanjeevni.features.payment.presentation.ui.result

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieClipSpec
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.BookLabTestViewModel
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SendOrderSmsRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SmsOrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SmsShippingAddressDto
import com.mysanjeevni.mysanjeevni.features.orders.presentation.state.OrderSmsUiState
import com.mysanjeevni.mysanjeevni.features.orders.presentation.state.OrderState
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.formatDate
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderSmsViewModel
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.payment.presentation.state.PaymentState
import com.mysanjeevni.mysanjeevni.features.payment.presentation.viewmodel.PaymentViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.FcmHelper
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun PaymentSuccessScreen(
    flow: String,
    paymentId: String,
    razorpayOrderId: String,
    signature: String,
    navController: NavController,
    orderViewModel: OrderViewModel,
    labViewModel: BookLabTestViewModel,
    viewModel: PaymentViewModel = hiltViewModel(),
    smsViewModel: OrderSmsViewModel = hiltViewModel()
) {
    val paymentState by viewModel.paymentState.collectAsStateWithLifecycle()
    val sessionManager = SessionManager(LocalContext.current)
    val userId = sessionManager.getUserId()

    val medicine by orderViewModel.selectedMedicine.collectAsState()
    val address by orderViewModel.selectedAddress.collectAsState()
    val cartItems by orderViewModel.cartItems.collectAsState()
    val labState by labViewModel.state.collectAsState()

    val smsState by smsViewModel.smsState.collectAsState()

    // Lottie Animation Configuration
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.order_success)
    )

    val clipSpec = composition?.let {
        LottieClipSpec.Frame(max = it.durationFrames.toInt() - 4)
    }

    var waitingForMedicineSms by remember {
        mutableStateOf(false)
    }

    val subtotal: Double
    val shippingCharge: Double
    val total: Double
    val orderItems: List<OrderItemDto>

    if (flow == "medicine") {

        subtotal =
            if (cartItems.isNotEmpty()) {
                cartItems.sumOf { it.price * it.qty }
            } else {
                medicine?.price ?: 0.0
            }

        shippingCharge =
            if (subtotal >= 299) 0.0 else 50.0

        total = subtotal + shippingCharge

        orderItems =
            if (cartItems.isNotEmpty()) {
                cartItems.map {
                    OrderItemDto(
                        productId = it.id,
                        quantity = it.qty,
                        name = it.name,
                        price = it.price
                    )
                }
            } else {
                medicine?.let {
                    listOf(
                        OrderItemDto(
                            productId = it.id,
                            quantity = 1,
                            name = it.name,
                            price = it.price
                        )
                    )
                } ?: emptyList()
            }

    } else {
        shippingCharge = 0.0
        total = 0.0
        orderItems = emptyList()
    }
    LaunchedEffect(labState.successMessage) {
        if (labState.successMessage != null) {
            delay(1500.milliseconds)
            navController.navigate(Screen.BookingHistoryScreen.route) {
                popUpTo(Screen.PaymentSuccess.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(smsState) {
        if (!waitingForMedicineSms) {
            return@LaunchedEffect
        }
        when (val state = smsState) {
            is OrderSmsUiState.Success -> {
                Log.d("RAZORPAY_SMS_FLOW", "================================")
                Log.d("RAZORPAY_SMS_FLOW", "SMS SUCCESS")
                Log.d("RAZORPAY_SMS_FLOW", "Message: ${state.message}")
                waitingForMedicineSms = false
                viewModel.resetState()
                navController.navigate(Screen.OrderSuccessScreen.createRoute(address!!.id)) {
                    popUpTo(Screen.PaymentSuccess.route) { inclusive = true }
                    launchSingleTop = true
                }
                Log.d("RAZORPAY_SMS_FLOW", "Navigation triggered")
            }
            is OrderSmsUiState.Error -> {
                Log.e("RAZORPAY_SMS_FLOW", "SMS FAILED: ${state.message}")
                waitingForMedicineSms = false
                viewModel.resetState()
                navController.navigate(Screen.OrderSuccessScreen.createRoute(address!!.id)
                ) {
                    popUpTo(Screen.PaymentSuccess.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }
            is OrderSmsUiState.Loading -> {
                Log.d("RAZORPAY_SMS_FLOW", "SMS sending...")
            }
            OrderSmsUiState.Idle -> Unit
        }
    }
    LaunchedEffect(labState.error) {
        if (labState.error != null){
            navController.navigate(Screen.PaymentFailed.route) {
                popUpTo(Screen.PaymentSuccess.route) { inclusive = true }
            }
        }
    }

    LaunchedEffect(Unit) {

        delay(3000.milliseconds)

        when(flow){

            "medicine" -> {

                viewModel.verifyPayment(
                    razorpayOrderId,
                    paymentId,
                    signature
                )
            }
            "lab" ->{
                labViewModel.bookPendingLabTest(paymentId,razorpayOrderId,signature)
            }

        }
    }

    LaunchedEffect(paymentState) {
        when (paymentState) {
            is PaymentState.PaymentVerified -> {

                when(flow){

                    "medicine" -> {

                        viewModel.createOrderWithPayment(

                            userId = userId,

                            items = orderItems,

                            totalPrice = total,

                            deliveryAddress = address!!.id,

                            notes = "Online Payment",

                            shippingCharge = shippingCharge
                        )
                    }

                    "lab" -> {
                        labViewModel.bookPendingLabTest(paymentId,razorpayOrderId,signature)
                    }
                }
            }

            is PaymentState.PaymentSuccess -> {

                if (flow == "medicine") {

                    Log.d("RAZORPAY_SMS_FLOW", "================================")
                    Log.d("RAZORPAY_SMS_FLOW", "PAYMENT SUCCESS")
                    Log.d("RAZORPAY_SMS_FLOW", "Fetching latest order...")

                    orderViewModel.getOrders(userId)

                    orderViewModel.orderState
                        .first { it is OrderState.Success }
                        .let { orderState ->

                            val orders =
                                (orderState as OrderState.Success).orders

                            val latest = orders.firstOrNull()

                            if (latest != null) {

                                orderViewModel.setRecentOrder(latest.order)

                                val shortOrderId =
                                    latest.order.id
                                        .takeLast(8)
                                        .uppercase()

                                val orderDate =
                                    formatDate(latest.order.createdAt)

                                val phone =
                                    address?.phone ?: ""

                                Log.d(
                                    "RAZORPAY_SMS_FLOW",
                                    "Full Order ID: ${latest.order.id}"
                                )

                                Log.d(
                                    "RAZORPAY_SMS_FLOW",
                                    "Short Order ID: $shortOrderId"
                                )

                                Log.d(
                                    "RAZORPAY_SMS_FLOW",
                                    "Phone: $phone"
                                )

                                FcmHelper.sendNotification(
                                    userId = userId ?: "",
                                    title = "Order Confirmed 🎉",
                                    body = "Your order #$shortOrderId has been placed successfully.\nDate: $orderDate"
                                )

                                if (phone.isNotBlank()) {

                                    val smsRequest =
                                        SendOrderSmsRequest(
                                            phone = phone,
                                            email =
                                                sessionManager.getUserEmail()
                                                    ?: "",
                                            items =
                                                orderItems.map { item ->

                                                    SmsOrderItemDto(
                                                        productId =
                                                            item.productId,
                                                        quantity =
                                                            item.quantity,
                                                        price =
                                                            item.price
                                                    )
                                                },
                                            shippingAddress =
                                                SmsShippingAddressDto(
                                                    street =
                                                        listOfNotNull(
                                                            address?.addressLine1,
                                                            address?.addressLine2
                                                        ).joinToString(" "),
                                                    city =
                                                        address?.city ?: "",
                                                    state =
                                                        address?.state ?: "",
                                                    zipCode =
                                                        address?.pincode ?: ""
                                                )
                                        )

                                    Log.d(
                                        "RAZORPAY_SMS_FLOW",
                                        "SMS Request: $smsRequest"
                                    )

                                    waitingForMedicineSms = true

                                    smsViewModel
                                        .sendOrderConfirmationSms(
                                            smsRequest
                                        )

                                } else {

                                    Log.e(
                                        "RAZORPAY_SMS_FLOW",
                                        "Phone empty, navigating without SMS"
                                    )

                                    viewModel.resetState()

                                    navController.navigate(
                                        Screen.OrderSuccessScreen
                                            .createRoute(address!!.id)
                                    ) {
                                        popUpTo(
                                            Screen.PaymentSuccess.route
                                        ) {
                                            inclusive = true
                                        }
                                    }
                                }

                            } else {

                                Log.e(
                                    "RAZORPAY_SMS_FLOW",
                                    "Latest order not found"
                                )

                                viewModel.resetState()

                                navController.navigate(
                                    Screen.OrderSuccessScreen
                                        .createRoute(address!!.id)
                                ) {
                                    popUpTo(
                                        Screen.PaymentSuccess.route
                                    ) {
                                        inclusive = true
                                    }
                                }
                            }
                        }
                }
            }
            is PaymentState.Error -> {
                FcmHelper.sendNotification(
                    userId = userId ?: "",
                    title = "Payment Failed ❌",
                    body = "Your payment could not be completed. Please try again."
                )
                navController.navigate(Screen.PaymentFailed.route) {
                    popUpTo(Screen.PaymentSuccess.route) { inclusive = true }
                }
            }
            else -> Unit
        }
    }

    // Updated UI with Lottie and Text
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LottieAnimation(
                composition = composition,
                modifier = Modifier.size(200.dp),
                isPlaying = true,
                restartOnPlay = false,
                clipSpec = clipSpec,
                iterations = 1
            )

            Spacer(modifier = Modifier.height(16.dp))

            AutoText(
                text = if (flow == "lab") "Creating your lab test booking..." else "Creating your order....",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Gray
                )
            )
        }
    }
}