package com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.screen

import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.components.BillSummary
import com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.components.CartBottomBar
import com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.components.CartItemRow
import com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.components.EmptyCartView
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.currency.presentation.viewmodel.CurrencyViewModel
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel.AddressViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import kotlinx.coroutines.delay
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    navController: NavController,
    viewModel: CartViewModel,
    orderViewModel: OrderViewModel,
    addressViewModel: AddressViewModel
) {
    val currencyViewModel: CurrencyViewModel = hiltViewModel()
    val currencyState by currencyViewModel.state.collectAsState()
    val currencyInfo = currencyState.currencyInfo
    val state by viewModel.state.collectAsState()
    val addressState by addressViewModel.state.collectAsState()
    val defaultAddress = addressState.selectedAddress
    val serviceabilityState by addressViewModel.serviceabilityState.collectAsState()
    val isDark = LocalIsDarkTheme.current || isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val itemTotal = state.cartItem.sumOf { it.price * it.qty }
    val deliveryFee = if (serviceabilityState.serviceable) {
        serviceabilityState.deliveryCharge
    } else {
        0.0
    }
    val grandTotal = itemTotal + deliveryFee
    val snackbarHostState = remember { SnackbarHostState() }
    val userId = remember { viewModel.getUserId() }

    LaunchedEffect(state.cartItem) {
        Log.d("CART_DEBUG", "CART VM = ${viewModel.hashCode()}")
        Log.d("CART_DEBUG", "CartScreen → Items Size: ${state.cartItem.size}")
    }
    LaunchedEffect(addressState.selectedAddress) {
        val address = addressState.selectedAddress
        Log.d("CART_SHIPPING", "==============================")
        Log.d("CART_SHIPPING", "ADDRESS STATE CHANGED")
        Log.d("CART_SHIPPING", "Selected Address = $address")
        Log.d("CART_SHIPPING", "Selected Pincode = ${address?.pincode}")

        if (address != null && address.pincode.length == 6) {
            Log.d("CART_SHIPPING", "Calling Shiprocket Serviceability")
            Log.d("CART_SHIPPING", "Pincode = ${address.pincode}")
            addressViewModel.checkServiceability(address.pincode)
        } else {
            Log.d("CART_SHIPPING", "No valid default/selected address")
            Log.d("CART_SHIPPING", "Pincode = ${address?.pincode}")
        }
        Log.d("CART_SHIPPING", "==============================")
    }
    LaunchedEffect(serviceabilityState) {
        Log.d("CART_SHIPPING", "------------------------------")
        Log.d("CART_SHIPPING", "SERVICEABILITY STATE UPDATED")
        Log.d("CART_SHIPPING", "Loading = ${serviceabilityState.isLoading}")
        Log.d("CART_SHIPPING", "Serviceable = ${serviceabilityState.serviceable}")
        Log.d("CART_SHIPPING", "Courier = ${serviceabilityState.courierName}")
        Log.d("CART_SHIPPING", "Delivery Charge = ₹${serviceabilityState.deliveryCharge}")
        Log.d("CART_SHIPPING", "Delivery Days = ${serviceabilityState.estimatedDeliveryDays}")
        Log.d("CART_SHIPPING", "Delivery Date = ${serviceabilityState.estimatedDeliveryDate}")
        Log.d("CART_SHIPPING", "COD Available = ${serviceabilityState.codAvailable}")
        Log.d("CART_SHIPPING", "Error = ${serviceabilityState.error}")
        Log.d("CART_SHIPPING", "------------------------------")
    }
    LaunchedEffect(state.error) {
        if (state.error.isNotBlank()) {
            snackbarHostState.showSnackbar(
                message = state.error
            )
            viewModel.clearError()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AutoText(
                        stringResource(R.string.my_cart),
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = cardColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                )
            )
        },
        bottomBar = {
            if (state.cartItem.isNotEmpty()) {
                CartBottomBar(
                    grandTotal,
                    isDark,
                    navController,
                    state.cartItem,
                    userId,
                    orderViewModel
                )
            }
        },
        containerColor = bgColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (state.isLoading || currencyState.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )
            } else if (currencyInfo == null) {
                AutoText(
                    text = currencyState.error ?: "Unable to load currency",
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.cartItem.isEmpty()) {
                EmptyCartView(textColor)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.cartItem) { item ->
                        CartItemRow(
                            item = item,
                            isDark = isDark,
                            onIncrease = { viewModel.incrementQty(item) },
                            onDecrease = { viewModel.decrementQty(item) }
                        )
                    }
                    item {
                        BillSummary(
                            cartItems = state.cartItem,
                            isDark = isDark,
                            deliveryFee = deliveryFee
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(
                        top = 12.dp,
                        start = 16.dp,
                        end = 16.dp
                    )

            ) { snackbarData ->
                LaunchedEffect(snackbarData) {
                    delay(1000.milliseconds)
                    snackbarData.dismiss()
                    viewModel.clearError()
                }
                Snackbar(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    containerColor = Color(0xFFD32F2F),
                    contentColor = Color.White
                ) {
                    AutoText(
                        text = snackbarData.visuals.message,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}






