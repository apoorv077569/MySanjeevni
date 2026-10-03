package com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.components

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.currency.presentation.viewmodel.CurrencyViewModel
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import java.util.Locale

@Composable
fun CartBottomBar(
    grandTotal: Double,
    isDark: Boolean,
    navController: NavController,
    cartItems: List<CartItem>,
    userId: String,
    orderViewModel: OrderViewModel,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {

    val currencyState by currencyViewModel.state.collectAsStateWithLifecycle()

    // Fallback to INR (rate = 1.0) while loading or if currency fetch failed
    val currencySymbol = currencyState.currencyInfo?.currencySymbol ?: "₹"
    val exchangeRate = currencyState.currencyInfo?.exchangeRate ?: 1.0

    // grandTotal comes in as INR from the parent, convert for display
    val convertedGrandTotal = grandTotal * exchangeRate

    val containerColor =
        if (isDark)
            Color(0xFF1E1E1E)
        else
            Color.White

    val textColor =
        if (isDark)
            Color.White
        else
            Color.Black

    Surface(

        shadowElevation =
            16.dp,

        color =
            containerColor

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp,
                    vertical = 12.dp
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Column {

                AutoText(

                    stringResource(
                        R.string.total_to_pay
                    ),

                    fontSize =
                        12.sp,

                    color =
                        Color.Gray
                )

                AutoText(

                    "$currencySymbol${
                        String.format(
                            Locale.getDefault(),
                            "%.2f",
                            convertedGrandTotal
                        )
                    }",

                    fontSize =
                        18.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        textColor
                )
            }

            Button(

                onClick = {

                    // =================================================
                    // EXISTING CHECKOUT LOGIC — UNCHANGED
                    // =================================================

                    orderViewModel.startCartCheckout()

                    Log.d(
                        "CHECKOUT_FLOW",
                        "Checkout started from CART | Items=${cartItems.size}"
                    )

                    val rxMedicine =
                        cartItems.firstOrNull {
                            it.requirePrescription
                        }

                    if (rxMedicine != null) {

                        Log.d(
                            "RX_CHECKOUT",
                            "Rx medicine found: ${rxMedicine.name}"
                        )

                        Log.d(
                            "RX_CHECKOUT",
                            "Product ID: ${rxMedicine.id}"
                        )

                        navController.navigate(
                            Screen.UploadPrescription.createRoute(
                                productId =
                                    rxMedicine.id,

                                productName =
                                    rxMedicine.name,

                                userId =
                                    userId
                            )
                        )

                    } else {

                        Log.d(
                            "RX_CHECKOUT",
                            "No Rx medicine → Address Screen"
                        )

                        navController.navigate(
                            "${Screen.ManageAddresses.route}?checkout=true&home=false"
                        )
                    }
                },

                colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                            Color(0xFFFF6F61)
                    ),

                shape =
                    RoundedCornerShape(8.dp),

                modifier =
                    Modifier
                        .height(48.dp)
                        .width(180.dp)

            ) {

                AutoText(

                    stringResource(
                        R.string.checkout
                    ),

                    color =
                        Color.White,

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        16.sp
                )
            }
        }
    }
}