package com.mysanjeevni.mysanjeevni.features.orders.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderViewModel
import androidx.core.graphics.toColorInt
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mysanjeevni.mysanjeevni.features.orders.domain.model.CheckoutType
import com.mysanjeevni.mysanjeevni.utils.AutoText


private val TealButton = Color(0xFF1A9B82)

@Composable
fun OrderSummaryScreen(
    navController: NavController,
    orderViewModel: OrderViewModel
) {
    val medicine by orderViewModel.selectedMedicine.collectAsState()
    val address by orderViewModel.selectedAddress.collectAsState()
    val cartItems by orderViewModel.cartItems.collectAsState()

    val checkoutType by orderViewModel
        .checkOutType
        .collectAsStateWithLifecycle()

    val resolvedCheckoutType = checkoutType ?: when {
        medicine != null -> CheckoutType.BUY_NOW
        cartItems.isNotEmpty() -> CheckoutType.CART
        else -> null
    }

    val subtotal = when (resolvedCheckoutType) {

        CheckoutType.CART -> {
            cartItems.sumOf { item ->
                item.price * item.qty
            }
        }

        CheckoutType.BUY_NOW -> {
            medicine?.price ?: 0.0
        }

        null -> {
            0.0
        }
    }

    val deliveryFee = when {
        subtotal == 0.0 -> 0.0
        subtotal >= 299.0 -> 0.0
        else -> 50.0
    }

    val totalAmount = subtotal + deliveryFee

    LaunchedEffect(
        checkoutType,
        resolvedCheckoutType,
        medicine,
        cartItems
    ) {
        Log.d("SUMMARY_CHECKOUT", "================================")
        Log.d("SUMMARY_CHECKOUT", "VM Hash = ${orderViewModel.hashCode()}")
        Log.d("SUMMARY_CHECKOUT", "Raw Checkout Type = $checkoutType")
        Log.d("SUMMARY_CHECKOUT", "Resolved Type = $resolvedCheckoutType")
        Log.d("SUMMARY_CHECKOUT", "Medicine = ${medicine?.name}")
        Log.d("SUMMARY_CHECKOUT", "Medicine Price = ${medicine?.price}")
        Log.d("SUMMARY_CHECKOUT", "Cart Count = ${cartItems.size}")
        Log.d("SUMMARY_CHECKOUT", "Subtotal = $subtotal")
        Log.d("SUMMARY_CHECKOUT", "Delivery Fee = $deliveryFee")
        Log.d("SUMMARY_CHECKOUT", "Total = $totalAmount")
        Log.d("SUMMARY_CHECKOUT", "================================")
    }

    Log.d("ORDER_VM", "Summary VM = ${orderViewModel.hashCode()}")

    LaunchedEffect(Unit) {
        Log.d("SUMMARY_DEBUG", "Address = $address")
    }

    LaunchedEffect(cartItems) {
        Log.d("SUMMARY", "Received Cart = ${cartItems.size}")
        cartItems.forEach {
            Log.d(
                "SUMMARY_ITEM",
                """
                Name=${it.name}
                Price=${it.price}
                Qty=${it.qty}
                OriginalPrice=${it.originalPrice}
                """.trimIndent()
            )
        }
    }


    // ── End business logic ────────────────────────────────────────────────────

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {

            // ── Delivery Address card ─────────────────────────────────────────
            item {
                SummaryCard {
                    // Header row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    ) {
                        IconCircle(icon = Icons.Outlined.LocationOn)
                        AutoText(
                            text = "Delivery Address",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Address rows + map illustration side-by-side
                    Row(modifier = Modifier.fillMaxWidth()) {
                        // Left: address fields
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            AddressRow(Icons.Outlined.Person, address?.fullName ?: "", bold = true)
                            AddressRow(Icons.Outlined.Phone, address?.phone ?: "")
                            AddressRow(Icons.Outlined.Home, address?.addressLine1 ?: "")
                            AddressRow(Icons.Outlined.Business, address?.addressLine2 ?: "")
                            AddressRow(
                                Icons.Outlined.LocationOn,
                                "${address?.city},  ${address?.state}"
                            )
                            AddressRow(Icons.Outlined.MailOutline, address?.pincode ?: "")
                        }
                        // Right: decorative map SVG placeholder
                        MapIllustration(
                            modifier = Modifier
                                .size(110.dp)
                                .align(Alignment.Bottom)
                        )
                    }
                }
            }

            // ── Product(s) card ───────────────────────────────────────────────
            when (resolvedCheckoutType) {
                CheckoutType.CART -> {
                    items(cartItems) { item ->
                        ProductCard(
                            imageUrl = item.imageUrl,
                            name = item.name,
                            qty = item.qty,
                            price = item.price,
                            subtotal = subtotal,
                            deliveryFee = deliveryFee
                        )
                    }
                }
                CheckoutType.BUY_NOW -> {
                    item {
                        ProductCard(
                            imageUrl = medicine?.image,
                            name = medicine?.name ?: "",
                            qty = 1,
                            price = medicine?.price ?: 0.0,
                            subtotal = subtotal,
                            deliveryFee = deliveryFee
                        )
                    }
                }
                null -> {
                    item {
                        SummaryCard {
                            AutoText(
                                text = "Checkout information unavailable",
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
            // ── Total Amount card ─────────────────────────────────────────────
            item {
                SummaryCard {
                    AutoText(
                        text = "Payment Summary",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    PriceRow(
                        label = "Subtotal",
                        value = "₹$subtotal",
                        bold = false
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    PriceRow(
                        label = "Delivery Fee",
                        value = "₹$deliveryFee",
                        bold = false
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                    PriceRow(
                        label = "Total Amount",
                        value = "₹$totalAmount",
                        bold = true
                    )
                }
            }
        }
        // ── Proceed To Payment button ─────────────────────────────────────────
        Button(
            onClick = { navController.navigate(Screen.PaymentScreen.route) },
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealButton)
        ) {
            Icon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .padding(end = 0.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            AutoText(
                text = "Proceed To Payment",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )
        }
    }
}

// ── Product card composable ────────────────────────────────────────────────────
@Composable
private fun ProductCard(
    imageUrl: Any?,
    name: String,
    qty: Int,
    price: Double,
    subtotal: Double,
    deliveryFee: Double
) {
    SummaryCard {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Left: product image
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(160.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )

            // Right: details
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AutoText(
                    text = name,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Qty pill
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    AutoText(
                        text = "Qty : $qty",
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                PriceRow(label = "Price", value = "₹$price", bold = false)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

    }
}


@Composable
private fun SummaryCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            content = content
        )
    }
}

@Composable
private fun IconCircle(icon: ImageVector) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorScheme.primary,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun AddressRow(
    icon: ImageVector,
    text: String,
    bold: Boolean = false
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorScheme.primary,
            modifier = Modifier.size(18.dp)
        )

        AutoText(
            text = text,
            fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = if (bold)
                colorScheme.onSurface
            else
                colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun PriceRow(
    label: String,
    value: String,
    bold: Boolean
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        AutoText(
            text = label,
            fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = colorScheme.onSurfaceVariant
        )

        AutoText(
            text = value,
            fontSize = 13.sp,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
            color = colorScheme.onSurface
        )
    }
}

@Composable
private fun MapIllustration(modifier: Modifier = Modifier) {
    // A simple Compose-drawn map pin + grid illustration matching the image style
    androidx.compose.foundation.Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val purple50 = "#EEEDFE".toColorInt()
        val purplePin = "#AFA9EC".toColorInt()

        val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG)

        // Grid lines (very light)
        paint.color = purplePin
        paint.alpha = 60
        paint.strokeWidth = 1.5f
        for (i in 0..4) {
            val x = w * i / 4f
            drawContext.canvas.nativeCanvas.drawLine(x, h * 0.3f, x + w * 0.25f, h, paint)
        }
        for (i in 0..4) {
            val y = h * 0.3f + (h * 0.7f) * i / 4f
            drawContext.canvas.nativeCanvas.drawLine(0f, y + w * 0.1f, w, y - w * 0.1f, paint)
        }

        // Pin circle (body)
        paint.color = purplePin
        paint.alpha = 200
        val cx = w * 0.65f
        val cy = h * 0.38f
        val r = w * 0.22f
        drawContext.canvas.nativeCanvas.drawCircle(cx, cy, r, paint)

        // Pin inner white dot
        paint.color = android.graphics.Color.WHITE
        paint.alpha = 255
        drawContext.canvas.nativeCanvas.drawCircle(cx, cy, r * 0.45f, paint)

        // Pin tail
        paint.color = purplePin
        paint.alpha = 200
        val path = android.graphics.Path()
        path.moveTo(cx - r * 0.5f, cy + r * 0.7f)
        path.lineTo(cx + r * 0.5f, cy + r * 0.7f)
        path.lineTo(cx, cy + r * 1.6f)
        path.close()
        drawContext.canvas.nativeCanvas.drawPath(path, paint)
    }
}