package com.mysanjeevni.mysanjeevni.features.payment.presentation.ui

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.app.MainActivity
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SendOrderSmsRequest
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SmsOrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.SmsShippingAddressDto
import com.mysanjeevni.mysanjeevni.features.orders.presentation.state.OrderSmsUiState
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.formatDate
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderSmsViewModel
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.payment.domain.model.PaymentMethod
import com.mysanjeevni.mysanjeevni.features.payment.presentation.state.PaymentState
import com.mysanjeevni.mysanjeevni.features.payment.presentation.viewmodel.PaymentViewModel
import com.mysanjeevni.mysanjeevni.features.payment.utils.startRazorpayCheckout
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.FcmHelper
import com.mysanjeevni.mysanjeevni.utils.SessionManager


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    items: List<OrderItemDto>,
    totalPrice: Double,
    deliveryAddress: String,
    orderViewModel: OrderViewModel,
    viewModel: PaymentViewModel = hiltViewModel(),
    smsViewModel: OrderSmsViewModel = hiltViewModel(),
    onPaymentSuccess: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val activity = context as Activity
    val sessionManager = SessionManager(LocalContext.current)
    val paymentState by viewModel.paymentState.collectAsStateWithLifecycle()
    val medicine by orderViewModel
        .selectedMedicine
        .collectAsState()
    val address by orderViewModel.selectedAddress.collectAsState()
    val cartItems by orderViewModel.cartItems.collectAsState()
    val deliveryCharge by orderViewModel
        .deliveryCharge
        .collectAsState()
    colorScheme
    var selectedMethod by remember { mutableStateOf(PaymentMethod.RAZORPAY) }
    val userId = sessionManager.getUserId()
    val subtotal = if (cartItems.isNotEmpty()) {
        cartItems.sumOf { it.price * it.qty }
    } else {
        medicine?.price ?: 0.0
    }
    val smsState by smsViewModel
        .smsState
        .collectAsStateWithLifecycle()

    val isProcessing =
        paymentState is PaymentState.Loading ||
                smsState is OrderSmsUiState.Loading

    val deliveryFee = deliveryCharge

    val finalAmount = subtotal + deliveryFee
    var checkoutOpened by remember {
        mutableStateOf(false)
    }

    val orderItems = if (cartItems.isNotEmpty()) {
        cartItems.map {
            OrderItemDto(
                productId = it.id,
                quantity = it.qty,
                name = it.name,
                price = it.price
            )
        }
    } else {
        listOf(
            OrderItemDto(
                productId = medicine?.id ?: "",
                name = medicine?.name ?: "",
                quantity = 1,
                price = medicine?.price ?: 0.0
            )
        )
    }

    LaunchedEffect(smsState) {

        when (val state = smsState) {

            is OrderSmsUiState.Success -> {

                Log.d("ORDER_SMS_FLOW", "================================")
                Log.d("ORDER_SMS_FLOW", "SMS SUCCESS RECEIVED")
                Log.d("ORDER_SMS_FLOW", "Message: ${state.message}")
                Log.d("ORDER_SMS_FLOW", "Now navigating to Order Success...")

                val addressId = address?.id

                if (addressId.isNullOrBlank()) {
                    Log.e(
                        "ORDER_SMS_FLOW",
                        "Navigation failed: Address ID is empty"
                    )
                    return@LaunchedEffect
                }

                // Reset before navigation
                viewModel.resetState()
                smsViewModel.resetSmsState()

                navController.navigate(
                    Screen.OrderSuccessScreen.createRoute(addressId)
                ) {
                    popUpTo(Screen.PaymentScreen.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

                Log.d(
                    "ORDER_SMS_FLOW",
                    "Navigation triggered successfully"
                )
            }

            is OrderSmsUiState.Error -> {

                Log.e("ORDER_SMS_FLOW", "================================")
                Log.e("ORDER_SMS_FLOW", "SMS FAILED")
                Log.e("ORDER_SMS_FLOW", "Error: ${state.message}")
                Log.e(
                    "ORDER_SMS_FLOW",
                    "Order already created, navigating anyway..."
                )

                val addressId = address?.id

                if (addressId.isNullOrBlank()) {
                    Log.e(
                        "ORDER_SMS_FLOW",
                        "Navigation failed: Address ID is empty"
                    )
                    return@LaunchedEffect
                }

                viewModel.resetState()
                smsViewModel.resetSmsState()

                navController.navigate(
                    Screen.OrderSuccessScreen.createRoute(addressId)
                ) {
                    popUpTo(Screen.PaymentScreen.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }
            }

            OrderSmsUiState.Loading -> {
                Log.d(
                    "ORDER_SMS_FLOW",
                    "SMS request loading..."
                )
            }

            OrderSmsUiState.Idle -> Unit
        }
    }

    LaunchedEffect(Unit) {
        MainActivity.Companion.RazorpayCallbackHolder.onSuccess =
            { paymentId, orderId, signature ->

                navController.navigate(
                    Screen.PaymentSuccess.createRoute(
                        flow = "medicine",
                        paymentId = paymentId,
                        razorpayOrderId = orderId,
                        signature = signature
                    )
                ) {
                    popUpTo(Screen.PaymentScreen.route) {
                        inclusive = true
                    }
                }
            }

        MainActivity.Companion.RazorpayCallbackHolder.onFailure = { _ ->
            Log.d("PAYMENT_TRACE", "2. PaymentScreen onFailure")

            checkoutOpened = false
            viewModel.resetState()

            navController.navigate(Screen.PaymentFailed.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    LaunchedEffect(paymentState) {
        when (val state = paymentState) {
            is PaymentState.RazorpayOrderCreated -> {
                Log.d("PAYMENT_TRACE", "3. Launching Razorpay")

                if (selectedMethod == PaymentMethod.RAZORPAY && !checkoutOpened) {

                    checkoutOpened = true

                    startRazorpayCheckout(
                        activity = activity,
                        order = state.order,
                        description = "Medicine Order",
                        onSuccess = { paymentId, orderId, signature ->
                            navController.navigate(
                                Screen.PaymentSuccess.createRoute(
                                    flow ="medicine",
                                    paymentId,
                                    orderId,
                                    signature
                                )
                            )
                        },
                        onFailure = { reason ->

                            checkoutOpened = false

                            viewModel.resetState()

                            Toast.makeText(
                                context,
                                reason,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }
            }

//            is PaymentState.CodOrderCreated -> {
//
//                viewModel.resetState()
//                orderViewModel.setRecentOrder(state.order)
//
//                val shorterId = state.order.id.takeLast(8).uppercase()
//                val orderDate = formatDate(state.order.createdAt)
//
//                val phone = address?.phone?:""
//                Log.d("ORDER_SMS_FLOW", "Full Order ID : ${state.order.id}")
//                Log.d("ORDER_SMS_FLOW", "Short Order ID: $shorterId")
//                Log.d("ORDER_SMS_FLOW", "Phone         : $phone")
//                Log.d("ORDER_SMS_FLOW", "Order Date    : $orderDate")
//
//
//                if (phone.isNotBlank()) {
//
//                    Log.d("ORDER_SMS_FLOW", "Calling SMS ViewModel...")
//
//                    smsViewModel.sendOrderConfirmationSms(
//                        phone = phone,
//                        orderId = shorterId
//                    )
//
//                } else {
//                    Log.e(
//                        "ORDER_SMS_FLOW",
//                        "SMS NOT SENT: Customer phone is empty"
//                    )
//                }
//
//
//                navController.navigate(
//                    Screen.OrderSuccessScreen.createRoute(address!!.id)
//                ) {
//                    popUpTo(Screen.PaymentScreen.route) {
//                        inclusive = true
//                    }
//                    launchSingleTop = true
//                }
//
//                FcmHelper.sendNotification(
//                    userId = userId ?: "",
//                    title = "Order Confirmed 🎉 \t\t  $orderDate",
//                    body = "Your order #$shorterId has been placed successfully."
//                )
//            }

            is PaymentState.CodOrderCreated -> {

                Log.d("ORDER_SMS_FLOW", "================================")
                Log.d("ORDER_SMS_FLOW", "COD ORDER CREATED SUCCESSFULLY")

                orderViewModel.setRecentOrder(state.order)

                val fullOrderId = state.order.id
                val shorterId = fullOrderId.takeLast(8).uppercase()
                val orderDate = formatDate(state.order.createdAt)
                val phone = address?.phone ?: ""

                Log.d("ORDER_SMS_FLOW", "Full Order ID : $fullOrderId")
                Log.d("ORDER_SMS_FLOW", "Short Order ID: $shorterId")
                Log.d("ORDER_SMS_FLOW", "Phone         : $phone")
                Log.d("ORDER_SMS_FLOW", "Order Date    : $orderDate")

                // FCM
                FcmHelper.sendNotification(
                    userId = userId ?: "",
                    title = "Order Confirmed 🎉  $orderDate",
                    body = "Your order #$shorterId has been placed successfully."
                )

                if (phone.isNotBlank()) {

                    Log.d(
                        "ORDER_SMS_FLOW",
                        "Calling SMS ViewModel..."
                    )
                    val smsRequest = SendOrderSmsRequest(
                        phone = phone,
                        email = sessionManager.getUserEmail() ?: "",
                        items = orderItems.map { item ->
                            SmsOrderItemDto(
                                productId = item.productId,
                                quantity = item.quantity,
                                price = item.price
                            )
                        },
                        shippingAddress = SmsShippingAddressDto(
                            street = (address?.addressLine1 + address?.addressLine2),
                            city = address?.city ?: "",
                            state = address?.state ?: "",
                            zipCode = address?.pincode ?: ""
                        )
                    )

                    smsViewModel.sendOrderConfirmationSms(
                        smsRequest
                    )

                } else {

                    Log.e(
                        "ORDER_SMS_FLOW",
                        "SMS NOT SENT: Customer phone is empty"
                    )

                    viewModel.resetState()

                    navController.navigate(
                        Screen.OrderSuccessScreen.createRoute(
                            address!!.id
                        )
                    ) {
                        popUpTo(Screen.PaymentScreen.route) {
                            inclusive = true
                        }

                        launchSingleTop = true
                    }
                }
            }

            else -> Unit
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            MainActivity.Companion.RazorpayCallbackHolder.onSuccess = null
            MainActivity.Companion.RazorpayCallbackHolder.onFailure = null
        }
    }

//    PaymentErrorDialog()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { AutoText("Payment") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            PaymentBottomBar(
                totalPrice = finalAmount,
                isLoading = isProcessing,
                onProceed = {
                    when (selectedMethod) {
                        PaymentMethod.RAZORPAY -> {

                            Log.d("PAYMENT_VM", "Creating Razorpay Order")

                            viewModel.createRazorpayOrder(
                                amount = finalAmount.toInt(),
                                receipt = "order_${System.currentTimeMillis()}"
                            )
                        }

                        PaymentMethod.COD -> {
                            viewModel.createOrderWithPayment(
                                userId = userId,
                                items = orderItems,
                                totalPrice = finalAmount,
                                deliveryAddress = address!!.id,
                                notes = "COD",
                                shippingCharge = deliveryFee
                            )

                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AutoText(
                text = "Select Payment Method",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            PaymentMethod.entries.forEach { method ->
                PaymentOptionItem(
                    method = method,
                    selected = selectedMethod == method,
                    onSelect = { selectedMethod = method }
                )
            }

            if (paymentState is PaymentState.Error) {
                AutoText(
                    text = (paymentState as PaymentState.Error).message,
                    color = colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun PaymentBottomBar(
    totalPrice: Double,
    isLoading: Boolean,
    onProceed: () -> Unit
) {
    Surface(
        shadowElevation = 12.dp,
        color = colorScheme.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(
                                colorScheme.primaryContainer
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            tint = colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        AutoText(
                            text = "Total Amount",
                            style = MaterialTheme.typography.bodySmall,
                            color = colorScheme.onSurfaceVariant
                        )
                        AutoText(
                            text = "₹$totalPrice",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.onSurface
                        )
                    }
                }

                Button(
                    onClick = onProceed,
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
                        disabledContainerColor = Color(0xFF5B3FD9).copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.height(52.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        AutoText(
                            text = "Proceed to Pay",
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 100% Secure Payments badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                AutoText(
                    text = "100% Secure Payments",
                    fontSize = 11.sp,
                    color = colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
