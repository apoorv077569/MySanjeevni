package com.mysanjeevni.mysanjeevni.features.orders.presentation.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MarkunreadMailbox
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.AddressRow
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.CancelOrderDialog
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.PaymentBreakdownRow
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.PaymentColumn
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.SectionCard
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.SectionHeader
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.StatusChip
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.TrackingStep
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderDetailViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import java.util.Locale

val AccentPurple = Color(0xFF6C5CE7)
val SuccessGreen = Color(0xFF4CAF50)
val PendingOrange = Color(0xFFFF9800)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(
    orderId: String,
    navController: NavController,
    viewModel: OrderDetailViewModel = hiltViewModel()
) {

    val state = viewModel.state
    var showCancelDialog by remember {
        mutableStateOf(false)
    }

    if (showCancelDialog) {

        CancelOrderDialog(
            isLoading = state.isCancelling,

            onDismiss = {
                if (!state.isCancelling) {
                    showCancelDialog = false
                }
            },

            onConfirm = {

                viewModel.cancelOrder(
                    orderId = orderId,
                    onSuccess = {
                        showCancelDialog = false
                    }
                )
            }
        )
    }

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.size(44.dp)
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    AutoText(
                        text = "Order Details",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    AutoText(
                        text = "View and track your order",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            when {

                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = AccentPurple)
                    }
                }

                state.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        AutoText(
                            text = state.error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                state.order != null -> {

                    val order = state.order
                    val originalPrice =
                        order.items.sumOf {
                            it.price * it.quantity
                        }

                    val deliveryFee =
                        viewModel.deliveryFee

                    val platformFee =
                        order.shippingCharge
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 45.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            SectionCard {
                                Column {
                                    AutoText(
                                        "Order #${order.id.takeLast(8).uppercase()}",
                                        style = MaterialTheme.typography.headlineSmall,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    AutoText(
                                        text = "₹${order.totalPrice}",
                                        style = MaterialTheme.typography.headlineMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = AccentPurple
                                    )
                                }
                            }
                        }
                        // ---------- TRACK ORDER ----------
                        item {
                            SectionCard {
                                Column {
                                    AutoText(
                                        text = "Track Order",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(18.dp))

                                    val confirmed = order.status.lowercase() != "pending"
                                    val shipped = order.status.lowercase() == "shipped" ||
                                            order.status.lowercase() == "delivered"
                                    val delivered = order.status.lowercase() == "delivered"
                                    val cancelled = order.status.lowercase() == "cancelled"

                                    TrackingStep(
                                        title = "Order Placed",
                                        completed = true,
                                        isLast = false
                                    )

                                    TrackingStep(
                                        title = "Pending",
                                        completed = true,
                                        isLast = false
                                    )

                                    if (cancelled) {
                                        TrackingStep(
                                            title = "Cancelled",
                                            completed = true,
                                            isLast = true,
                                            color = Color(0xFFD32F2F)
                                        )
                                    } else {
                                        TrackingStep(
                                            title = "Confirmed",
                                            completed = confirmed,
                                            isLast = false
                                        )

                                        TrackingStep(
                                            title = "Shipped",
                                            completed = shipped,
                                            isLast = false
                                        )

                                        TrackingStep(
                                            title = "Delivered",
                                            completed = delivered,
                                            isLast = true
                                        )
                                    }
                                }
                            }
                        }

                        // ---------- DELIVERY ADDRESS ----------
                        state.address?.let { address ->
                            item {
                                SectionCard {
                                    Column {

                                        SectionHeader(
                                            icon = Icons.Filled.LocationOn,
                                            title = "Delivery Address"
                                        )

                                        Spacer(modifier = Modifier.height(12.dp))

                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Row(modifier = Modifier.padding(16.dp)) {

                                                Column(modifier = Modifier.weight(1f)) {
                                                    AddressRow(Icons.Filled.Person, address.fullName)
                                                    AddressRow(Icons.Filled.Phone, address.phone)
                                                    AddressRow(Icons.Filled.Home, address.addressLine1)
                                                    AddressRow(Icons.Filled.Map, address.addressLine2)
                                                    AddressRow(Icons.Filled.Map, address.city)
                                                }

                                                Spacer(modifier = Modifier.width(12.dp))

                                                Column(modifier = Modifier.weight(1f)) {
                                                    AddressRow(Icons.Filled.Map, address.state)
                                                    AddressRow(
                                                        Icons.Filled.MarkunreadMailbox,
                                                        address.pincode
                                                    )
                                                    AddressRow(Icons.Filled.Language, address.country)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        item {
                            SectionCard {
                                Column {
                                    SectionHeader(
                                        Icons.AutoMirrored.Filled.ReceiptLong,
                                        "Order Summary"
                                    )
                                    state.medicines.forEachIndexed { index, medicine ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            AsyncImage(
                                                model = medicine.image,
                                                contentDescription = medicine.name,
                                                modifier = Modifier
                                                    .size(90.dp)
                                                    .clip(RoundedCornerShape(12.dp)),
                                                contentScale = ContentScale.Fit
                                            )

                                            Spacer(modifier = Modifier.width(12.dp))

                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {

                                                AutoText(
                                                    text = medicine.name,
                                                    style = MaterialTheme.typography.titleMedium,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                if (medicine.brand.isNotBlank()) {
                                                    AutoText(
                                                        text = medicine.brand,
                                                        style = MaterialTheme.typography.bodyMedium,
                                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                                    )
                                                }

                                                Spacer(modifier = Modifier.height(6.dp))

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {

                                                    AutoText(
                                                        text = "Qty : ${order.items.getOrNull(index)?.quantity ?: 1}",
                                                        style = MaterialTheme.typography.bodyMedium
                                                    )

                                                    AutoText(
                                                        text = "₹${medicine.price}",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        fontWeight = FontWeight.Bold,
                                                        color = AccentPurple
                                                    )
                                                }
                                            }
                                        }
                                        if (index != state.medicines.lastIndex) {
                                            HorizontalDivider(
                                                modifier = Modifier.padding(vertical = 16.dp),
                                                color = MaterialTheme.colorScheme.outlineVariant
                                            )
                                        }

                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                 HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
                                    Spacer(modifier = Modifier.height(16.dp))

                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    AutoText(
                                        text = "Payment Breakdown",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    PaymentBreakdownRow(
                                        label = "Original Price",
                                        value = "₹${String.format(Locale.getDefault(), "%.2f", originalPrice)}"
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    PaymentBreakdownRow(
                                        label = "Delivery Fee",
                                        value = "₹${String.format(Locale.getDefault(),"%.2f",deliveryFee)}"
                                    )

                                    Spacer(modifier = Modifier.height(8.dp))

                                    PaymentBreakdownRow(
                                        label = "Platform Fee",
                                        value = "₹${String.format(Locale.getDefault(),"%.2f", platformFee)}"
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    HorizontalDivider(
                                        color = MaterialTheme.colorScheme.outlineVariant
                                    )

                                    Spacer(modifier = Modifier.height(12.dp))

                                    PaymentBreakdownRow(
                                        label = "Total",
                                        value = "₹${String.format(Locale.getDefault(),"%.2f", order.totalPrice)}",
                                        labelWeight = FontWeight.Bold,
                                        valueWeight = FontWeight.Bold,
                                        valueColor = AccentPurple
                                    )

                                    Spacer(modifier = Modifier.height(16.dp))

                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.surfaceVariant
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.SpaceEvenly
                                        ) {
                                            PaymentColumn("Amount") {
                                                AutoText("₹${order.totalPrice}", fontWeight = FontWeight.Bold)
                                            }
                                            PaymentColumn("Payment Status") {
                                                StatusChip(
                                                    text = order.paymentStatus.uppercase(),
                                                    isPositive = order.paymentStatus.lowercase() !=
                                                            "pending"
                                                )
                                            }
                                            PaymentColumn("Order Status") {
                                                AutoText(
                                                    order.status.uppercase(),
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                    Spacer(modifier = Modifier.height(16.dp))

                                    if (!order.status.equals("cancelled", ignoreCase = true)) {
                                        OutlinedButton(
                                            onClick = {
                                                showCancelDialog = true
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = ButtonDefaults.outlinedButtonColors(
                                                contentColor = Color(0xFFD32F2F)
                                            )
                                        ) {
                                            AutoText(
                                                text = "Cancel Order",
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}










