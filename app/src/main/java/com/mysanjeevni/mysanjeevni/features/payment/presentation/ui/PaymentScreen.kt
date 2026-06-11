package com.mysanjeevni.mysanjeevni.features.payment.presentation.ui

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.app.MainActivity
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.presntation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.payment.data.remote.RazorpayOrder
import com.mysanjeevni.mysanjeevni.features.payment.domain.model.PaymentMethod
import com.mysanjeevni.mysanjeevni.features.payment.state.PaymentState
import com.mysanjeevni.mysanjeevni.features.payment.viewmodel.PaymentViewModel
import com.mysanjeevni.mysanjeevni.utils.FcmHelper
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import com.razorpay.R
import org.json.JSONObject

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
    var selectedMethod by remember { mutableStateOf(PaymentMethod.RAZORPAY) }
    val userId = sessionManager.getUserId()

    LaunchedEffect(Unit) {

        MainActivity.Companion.RazorpayCallbackHolder.onSuccess =
            { paymentId, orderId, signature ->

                Log.d("PAYMENT_UI", "Razorpay Success Callback")
                Toast.makeText(context,"Payment Successful",Toast.LENGTH_SHORT).show()

                viewModel.verifyPayment(
                    razorpayOrderId = orderId,
                    razorpayPaymentId = paymentId,
                    razorpaySignature = signature
                )
            }

        MainActivity.Companion.RazorpayCallbackHolder.onFailure =
            { error ->
                Toast.makeText(
                    context,
                    error,
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    LaunchedEffect(paymentState) {
        when (val state = paymentState) {
            is PaymentState.RazorpayOrderCreated -> {
                if (selectedMethod == PaymentMethod.RAZORPAY) {
                    startRazorpayCheckout(
                        activity = activity,
                        order = state.order,
                        userId = userId,
                        onSuccess = { paymentId, orderId, signature ->
                            viewModel.verifyPayment(
                                razorpayOrderId = orderId,
                                razorpayPaymentId = paymentId,
                                razorpaySignature = signature
                            )
                        },
                        onFailure = { reason ->
                            viewModel.resetState()
                            Toast.makeText(context, reason, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }

            is PaymentState.PaymentSuccess -> {
                FcmHelper.sendNotification(
                    userId = userId ?: "",
                    title = "Order Confirmed 🎉",
                    body = "Your order has been placed successfully."
                )

                viewModel.resetState()
                onPaymentSuccess()
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
                totalPrice = medicine!!.price,
                isLoading = paymentState is PaymentState.Loading,
                onProceed = {
                    when (selectedMethod) {
                        PaymentMethod.RAZORPAY -> {

                            Log.d("PAYMENT_VM", "Creating Razorpay Order")

                            viewModel.createRazorpayOrder(
                                amount = medicine!!.price.toInt(),
                                receipt = "order_${System.currentTimeMillis()}"
                            )
                        }
                        PaymentMethod.COD -> {
                            viewModel.createOrderWithPayment(
                                userId = userId,

                                items = listOf(
                                    OrderItemDto(
                                        productId = medicine?.id ?: "",
                                        quantity = 1,
                                        name = medicine?.name?:"",
                                        price = medicine?.price?:0.0
                                    )
                                ),
                                totalPrice = medicine?.price ?: 0.0,
                                deliveryAddress = address!!.id,
                                notes = "COD"
                            )
//                            onPaymentSuccess()
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
        shadowElevation = 8.dp,
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Total",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "₹$totalPrice",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            Button(
                onClick = onProceed,
                enabled = !isLoading,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.height(48.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Proceed to Pay")
                }
            }
        }
    }
}

private fun startRazorpayCheckout(
    activity: Activity,
    order: RazorpayOrder,
    userId: String?,
    onSuccess: (String, String, String) -> Unit,
    onFailure: (String) -> Unit
) {
    val checkout = Checkout()
    checkout.setKeyID("rzp_live_SUcsurW9fkbXe3")  // <- apni key daalo

    val options = JSONObject().apply {
        put("name", R.string.app_name)           // <- app name daalo
        put("description", "Order Payment")
        put("order_id", order.id)
        put("amount", order.amount)
        put("currency", order.currency)
        put("prefill", JSONObject().apply {
            put("contact", "")
            put("email", "")
        })
    }

    checkout.open(activity, options)

    activity.let {
        val paymentResultListener = object : PaymentResultWithDataListener {
            override fun onPaymentSuccess(
                razorpayPaymentId: String?,
                paymentData: PaymentData?
            ) {
                onSuccess(
                    razorpayPaymentId ?: "",
                    paymentData?.orderId ?: "",
                    paymentData?.signature ?: ""
                )
            }

            override fun onPaymentError(
                errorCode: Int,
                errorDescription: String?,
                paymentData: PaymentData?
            ) {
                onFailure(errorDescription ?: "Payment failed")
            }
        }
    }
}