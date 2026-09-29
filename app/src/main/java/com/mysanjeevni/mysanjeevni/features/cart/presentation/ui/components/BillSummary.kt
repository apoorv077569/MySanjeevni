package com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.currency.presentation.viewmodel.CurrencyViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import java.util.Locale

@Composable
fun BillSummary(
    cartItems: List<CartItem>,
    isDark: Boolean,
    deliveryFee: Double,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {

    val currencyState by currencyViewModel.state.collectAsStateWithLifecycle()
    val currencySymbol = currencyState.currencyInfo?.currencySymbol ?: "₹"
    val exchangeRate = currencyState.currencyInfo?.exchangeRate ?: 1.0

    val cardColor =
        if (isDark)
            Color(0xFF1E1E1E)
        else
            Color.White

    val textColor =
        if (isDark)
            Color.White
        else
            Color.Black

    // All prices are stored in INR in the database/cart,
    // so convert to the user's local currency using exchangeRate
    val itemTotalInr =
        cartItems.sumOf {
            it.price * it.qty
        }

    val grandTotalInr =
        itemTotalInr + deliveryFee

    val itemTotal = itemTotalInr * exchangeRate
    val convertedDeliveryFee = deliveryFee * exchangeRate
    val grandTotal = grandTotalInr * exchangeRate

    val dividerColor =
        if (isDark)
            Color.White.copy(alpha = 0.12f)
        else
            Color.LightGray.copy(alpha = 0.5f)

    Card(

        colors =
            CardDefaults.cardColors(
                containerColor = cardColor
            ),

        elevation =
            CardDefaults.cardElevation(2.dp),

        shape =
            RoundedCornerShape(8.dp)

    ) {

        Column(
            modifier =
                Modifier.padding(16.dp)
        ) {

            AutoText(

                stringResource(
                    R.string.bill_summary
                ),

                fontWeight =
                    FontWeight.Bold,

                fontSize =
                    16.sp,

                color =
                    textColor
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // =====================================================
            // ITEM TOTAL
            // =====================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween

            ) {

                AutoText(

                    stringResource(
                        R.string.item_total
                    ),

                    color =
                        Color.Gray,

                    fontSize =
                        14.sp
                )

                val roundedTotal =
                    String.format(
                        Locale.getDefault(),
                        "%.2f",
                        itemTotal
                    ).toDouble()

                AutoText(
                    "$currencySymbol$roundedTotal",
                    color = textColor,
                    fontSize = 14.sp
                )
            }

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )
            Row(
                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween

            ) {

                AutoText(

                    stringResource(
                        R.string.delivery_fee
                    ),

                    color =
                        Color.Gray,

                    fontSize =
                        14.sp
                )

                if (deliveryFee == 0.0) {

                    AutoText(

                        stringResource(
                            R.string.free
                        ),

                        color =
                            Color(0xFF008000),

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            14.sp
                    )

                } else {

                    AutoText(

                        text =
                            "$currencySymbol${
                                String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    convertedDeliveryFee
                                )
                            }",

                        color =
                            textColor,

                        fontSize =
                            14.sp
                    )
                }
            }

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            HorizontalDivider(
                color = dividerColor,
                thickness = 1.dp
            )

            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )

            // =====================================================
            // GRAND TOTAL
            // =====================================================

            Row(

                modifier =
                    Modifier.fillMaxWidth(),

                horizontalArrangement =
                    Arrangement.SpaceBetween

            ) {

                AutoText(

                    stringResource(
                        R.string.grand_total
                    ),

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        16.sp,

                    color =
                        textColor
                )

                AutoText(

                    text =
                        "$currencySymbol${
                            String.format(
                                Locale.getDefault(),
                                "%.2f",
                                grandTotal
                            )
                        }",

                    fontWeight =
                        FontWeight.Bold,

                    fontSize =
                        16.sp,

                    color =
                        textColor
                )
            }
        }
    }
}