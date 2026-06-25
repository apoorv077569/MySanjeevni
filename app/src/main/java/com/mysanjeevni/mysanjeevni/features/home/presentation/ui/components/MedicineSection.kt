package com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.TealAccent
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.utils.AutoText
import java.util.Locale

@Composable
fun FeaturedMedicinesGrid(
    medicines: List<Medicine>,
    isDark: Boolean,
    onSeeAllClick: () -> Unit,
    onAddToCartClick: (Medicine) -> Unit,
    navController: NavController,
    cartViewModel: CartViewModel
) {
    val cardColor = MaterialTheme.colorScheme.surface
    val textColor = MaterialTheme.colorScheme.onSurface

    LazyRow(
        modifier = Modifier.wrapContentHeight(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(medicines) { medicine ->
            FeaturedMedicinesItem(
                medicine, cardColor, textColor,
                onAddClick = { onAddToCartClick(medicine) },
                cartViewModel, navController
            )
        }

        item {
            SeeAllCard(isDark) { onSeeAllClick() }
        }
    }
}



@Composable
fun FeaturedMedicinesItem(
    item: Medicine,
    cardColor: Color,
    textColor: Color,
    onAddClick: () -> Unit,
    cartViewModel: CartViewModel,
    navController: NavController
) {
    val cartState by cartViewModel.state.collectAsState()

    val quantity = cartState.cartItem
        .find { it.id == item.id }
        ?.qty ?: 0

    val discountPercent = remember(item.price, item.mrp) {
        if (item.mrp > 0 && item.mrp > item.price) {
            String.format(
                Locale.getDefault(),
                "%.0f%% OFF",
                ((item.mrp - item.price) / item.mrp) * 100
            )
        } else null
    }

    val cartItem = CartItem(
        id = item.id,
        name = item.name,
        price = item.price,
        originalPrice = item.mrp,
        imageUrl = item.image,
        qty = quantity
    )

    Card(
        modifier = Modifier
            .width(160.dp)
            .height(250.dp)
            .clickable {
                navController.navigate(
                    Screen.MedicineDetail.createRoute(item.id)
                )
            },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .height(110.dp)
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (!item.image.isNullOrBlank()) {

                        AsyncImage(
                            model = item.image,
                            contentDescription = item.name,
                            modifier = Modifier.wrapContentSize(),
                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Text(
                            text = item.icon ?: "💊",
                            fontSize = 32.sp
                        )
                    }
                }

                if (item.stock <= 0) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .background(
                                Color.Red,
                                RoundedCornerShape(4.dp)
                            )
                            .padding(
                                horizontal = 4.dp,
                                vertical = 2.dp
                            )
                    ) {
                        AutoText(
                            text = "Out of Stock",
                            fontSize = 7.sp,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // SCROLLABLE CONTENT
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {

                AutoText(
                    text = item.brand,
                    fontSize = 9.sp,
                    color = Color(0xFF26A69A),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(3.dp))

                AutoText(
                    text = item.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    lineHeight = 15.sp
                )

                Spacer(modifier = Modifier.height(4.dp))

                AutoText(
                    text = "${item.quantity} ${item.quantityUnit}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AutoText(
                        text = "₹${item.price}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    AutoText(
                        text = "₹${item.mrp}",
                        fontSize = 10.sp,
                        textDecoration = TextDecoration.LineThrough,
                        color = MaterialTheme.colorScheme.onSurfaceVariant                    )
                }

                discountPercent?.let {
                    Spacer(modifier = Modifier.height(2.dp))

                    AutoText(
                        text = it,
                        fontSize = 10.sp,
                        color = Color(0xFF43A047),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // FIXED BOTTOM BUTTON
            if (item.stock <= 0) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(
                            Color.LightGray,
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AutoText(
                        text = "Out of Stock",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }

            } else if (quantity == 0) {

                Button(
                    onClick = onAddClick,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TealAccent
                    ),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    AutoText(
                        text = "ADD",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

            } else {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(MaterialTheme.colorScheme.surface)                        .border(
                            1.dp,
                            TealAccent,
                            RoundedCornerShape(8.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable {
                                cartViewModel.decrementQty(cartItem)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = null,
                            tint = TealAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    AutoText(
                        text = quantity.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable {
                                cartViewModel.incrementQty(cartItem)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null,
                            tint = TealAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}