package com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.currency.presentation.viewmodel.CurrencyViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import java.util.Locale

@Composable
fun CartItemRow(
    item: CartItem,
    isDark: Boolean,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {

    val currencyState by currencyViewModel.state.collectAsStateWithLifecycle()

    // Fallback to INR (rate = 1.0) while loading or if currency fetch failed
    val currencySymbol = currencyState.currencyInfo?.currencySymbol ?: "₹"
    val exchangeRate = currencyState.currencyInfo?.exchangeRate ?: 1.0

    val convertedPrice = item.price * exchangeRate
    val convertedOriginalPrice = item.originalPrice * exchangeRate

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

        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(12.dp),

            verticalAlignment =
                Alignment.CenterVertically

        ) {

            Card(

                shape =
                    RoundedCornerShape(4.dp),

                modifier =
                    Modifier.size(60.dp),

                colors =
                    CardDefaults.cardColors(

                        containerColor =
                            if (isDark)
                                Color.White.copy(alpha = 0.08f)
                            else
                                Color(0xFFF5F5F5)
                    )

            ) {

                AsyncImage(

                    model =
                        item.imageUrl,

                    contentDescription =
                        null,

                    modifier =
                        Modifier.fillMaxSize(),

                    contentScale =
                        ContentScale.Crop
                )
            }

            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                AutoText(

                    text =
                        item.name,

                    fontWeight =
                        FontWeight.SemiBold,

                    fontSize =
                        14.sp,

                    color =
                        textColor,

                    maxLines =
                        2
                )

                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    AutoText(

                        text =
                            "$currencySymbol${
                                String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    convertedPrice
                                )
                            }",

                        fontWeight =
                            FontWeight.Bold,

                        fontSize =
                            16.sp,

                        color =
                            textColor
                    )

                    Spacer(
                        modifier =
                            Modifier.width(8.dp)
                    )

                    AutoText(

                        text =
                            "$currencySymbol${
                                String.format(
                                    Locale.getDefault(),
                                    "%.2f",
                                    convertedOriginalPrice
                                )
                            }",

                        fontSize =
                            12.sp,

                        color =
                            Color.Gray,

                        textDecoration =
                            TextDecoration.LineThrough
                    )
                }
            }

            Row(

                verticalAlignment =
                    Alignment.CenterVertically,

                modifier =
                    Modifier
                        .border(
                            1.dp,
                            Color(0xFFFF6F61),
                            RoundedCornerShape(4.dp)
                        )
                        .padding(
                            horizontal = 4.dp,
                            vertical = 2.dp
                        )

            ) {

                Box(

                    modifier =
                        Modifier
                            .size(28.dp)
                            .clickable {
                                onDecrease()
                            },

                    contentAlignment =
                        Alignment.Center

                ) {

                    Icon(

                        imageVector =
                            if (item.qty > 1)
                                Icons.Default.Remove
                            else
                                Icons.Default.Delete,

                        contentDescription =
                            stringResource(
                                R.string.decrease
                            ),

                        tint =
                            Color(0xFFFF6F61),

                        modifier =
                            Modifier.size(20.dp)
                    )
                }

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                AutoText(

                    text =
                        "${item.qty}",

                    color =
                        Color(0xFFFF6F61),

                    fontWeight =
                        FontWeight.Bold
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                Box(

                    modifier =
                        Modifier
                            .size(28.dp)
                            .clickable {
                                onIncrease()
                            },

                    contentAlignment =
                        Alignment.Center

                ) {

                    Icon(

                        imageVector =
                            Icons.Default.Add,

                        contentDescription =
                            stringResource(
                                R.string.increase
                            ),

                        tint =
                            Color(0xFFFF6F61),

                        modifier =
                            Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}