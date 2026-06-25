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
import androidx.compose.material3.Text
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
import com.mysanjeevni.mysanjeevni.features.orders.presntation.state.OrderState
import com.mysanjeevni.mysanjeevni.features.orders.presntation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.payment.domain.model.PaymentMethod
import com.mysanjeevni.mysanjeevni.features.payment.presentation.state.PaymentState
import com.mysanjeevni.mysanjeevni.features.payment.presentation.viewmodel.PaymentViewModel
import com.mysanjeevni.mysanjeevni.features.payment.utils.startRazorpayCheckout
import com.mysanjeevni.mysanjeevni.utils.FcmHelper
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import com.mysanjeevni.mysanjeevni.utils.dilaog.PaymentErrorDialog
import kotlinx.coroutines.flow.first

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    items: List<OrderItemDto>,
    totalPrice: Double,
    deliveryAddress: String,
    orderViewModel: OrderViewModel,
    viewModel: PaymentViewModel = hiltViewModel(),
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
    val colorScheme = MaterialTheme.colorScheme
    var selectedMethod by remember { mutableStateOf(PaymentMethod.RAZORPAY) }
    val userId = sessionManager.getUserId()
    val subtotal = if (cartItems.isNotEmpty()) {
        cartItems.sumOf { it.price * it.qty }
    } else {
        medicine?.price ?: 0.0
    }

    val deliveryFee = when {
        subtotal == 0.0 -> 0.0
        subtotal >= 299.0 -> 0.0
        else -> 50.0
    }

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

    LaunchedEffect(Unit) {
        MainActivity.Companion.RazorpayCallbackHolder.onSuccess =
            { paymentId, orderId, signature ->

                navController.navigate(
                    Screen.PaymentSuccess.createRoute(
                        paymentId,
                        orderId,
                        signature
                    )
                ) {
                    popUpTo(Screen.PaymentScreen.route) {
                        inclusive = true
                    }
                }
            }

        MainActivity.Companion.RazorpayCallbackHolder.onFailure = { error ->
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
                        onSuccess = { paymentId, orderId, signature ->

                            navController.navigate(
                                Screen.PaymentSuccess.createRoute(
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



            else -> Unit
        }
    }
    DisposableEffect(Unit) {
        onDispose {
            MainActivity.Companion.RazorpayCallbackHolder.onSuccess = null
            MainActivity.Companion.RazorpayCallbackHolder.onFailure = null
        }
    }

    PaymentErrorDialog()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Payment") },
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
                isLoading = paymentState is PaymentState.Loading,
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
                                shippingCharge = 0.0
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
            Text(
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
                Text(
                    text = (paymentState as PaymentState.Error).message,
                    color = MaterialTheme.colorScheme.error,
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
                .navigationBarsPadding()
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
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Total Amount",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "₹$totalPrice",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Button(
                    onClick = onProceed,
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = Color(0xFF5B3FD9).copy(alpha = 0.6f)
                    ),
                    modifier = Modifier.height(52.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            MaterialTheme.colorScheme.onPrimary,
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
                        Text(
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
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "100% Secure Payments",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

//private fun startRazorpayCheckout(
//    activity: Activity,
//    order: RazorpayOrder,
//    userId: String?,
//    onSuccess: (String, String, String) -> Unit,
//    onFailure: (String) -> Unit
//) {
//    val checkout = Checkout()
////    checkout.setKeyID("rzp_live_SUcsurW9fkbXe3")
////    checkout.setKeyID("rzp_test_1DP5mmOlF5G5ag")
//
//    checkout.setKeyID("rzp_test_T4zV3iEH7GKUvL")
//
//    val options = JSONObject().apply {
//        put("name", R.string.app_name)
//        put("description", "Order Payment")
//        put("order_id", order.id)
//        put("amount", order.amount)
//        put("currency", order.currency)
//        put("prefill", JSONObject().apply {
//            put("contact", "")
//            put("email", "")
//        })
//    }
//
//    checkout.open(activity, options)
//
//    activity.let {
//        object : PaymentResultWithDataListener {
//            override fun onPaymentSuccess(
//                razorpayPaymentId: String?,
//                paymentData: PaymentData?
//            ) {
//                onSuccess(
//                    razorpayPaymentId ?: "",
//                    paymentData?.orderId ?: "",
//                    paymentData?.signature ?: ""
//                )
//            }
//
//            override fun onPaymentError(
//                errorCode: Int,
//                errorDescription: String?,
//                paymentData: PaymentData?
//            ) {
//                onFailure(errorDescription ?: "Payment failed")
//            }
//        }
//    }
//}