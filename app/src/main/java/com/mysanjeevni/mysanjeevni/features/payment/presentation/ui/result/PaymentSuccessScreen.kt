package com.mysanjeevni.mysanjeevni.features.payment.presentation.ui.result

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
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
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.BookLabTestViewModel
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.presntation.state.OrderState
import com.mysanjeevni.mysanjeevni.features.orders.presntation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.payment.presentation.state.PaymentState
import com.mysanjeevni.mysanjeevni.features.payment.presentation.viewmodel.PaymentViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.FcmHelper
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

@Composable
fun PaymentSuccessScreen(
    flow: String,
    paymentId: String,
    razorpayOrderId: String,
    signature: String,
    navController: NavController,
    orderViewModel: OrderViewModel,
    labViewModel: BookLabTestViewModel,
    viewModel: PaymentViewModel = hiltViewModel()
) {
    val paymentState by viewModel.paymentState.collectAsStateWithLifecycle()
    val sessionManager = SessionManager(LocalContext.current)
    val userId = sessionManager.getUserId()

    val medicine by orderViewModel.selectedMedicine.collectAsState()
    val address by orderViewModel.selectedAddress.collectAsState()
    val cartItems by orderViewModel.cartItems.collectAsState()
    val labState by labViewModel.state.collectAsState()

    // Lottie Animation Configuration
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.order_success)
    )

    val clipSpec = composition?.let {
        LottieClipSpec.Frame(max = it.durationFrames.toInt() - 4)
    }

    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = 1,
        restartOnPlay = false
    )

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

        subtotal = 0.0
        shippingCharge = 0.0
        total = 0.0
        orderItems = emptyList()
    }
    LaunchedEffect(labState.successMessage) {
        if (labState.successMessage != null) {
            delay(1500)
            navController.navigate(Screen.BookingHistoryScreen.route) {
                popUpTo(Screen.PaymentSuccess.route) { inclusive = true }
            }
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

        delay(3000)

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
                FcmHelper.sendNotification(
                    userId = userId ?: "",
                    title = "Order Confirmed 🎉",
                    body = "Your order has been placed successfully."
                )
                orderViewModel.getOrders(userId)
                orderViewModel.orderState
                    .first { it is OrderState.Success }
                    .let {
                        val orders = (it as OrderState.Success).orders
                        orders.firstOrNull()?.let { latest ->
                            orderViewModel.setRecentOrder(latest.order)
                        }
                    }
                viewModel.resetState()
                navController.navigate(Screen.OrderSuccessScreen.createRoute(address!!.id)) {
                    popUpTo(Screen.PaymentSuccess.route) { inclusive = true }
                }
            }
            is PaymentState.Error -> {
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
                progress = {progress},
                modifier = Modifier.size(200.dp)
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