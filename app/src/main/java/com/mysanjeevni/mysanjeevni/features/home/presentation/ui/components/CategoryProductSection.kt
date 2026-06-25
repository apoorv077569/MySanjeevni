package com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun CategoryProductsSection(
    title: String,
    subCategories: List<String> = emptyList(), // children ke names pass karo
    medicines: List<Medicine>,
    isDark: Boolean,
    navController: NavController,
    cartViewModel: CartViewModel,
    onAddToCartClick: (Medicine) -> Unit
) {
    if (medicines.isEmpty()) return

    val lavender = MaterialTheme.colorScheme.surfaceVariant
    val subtitle = if (subCategories.isNotEmpty()) {
        subCategories.take(3).joinToString(", ") // max 3 dikhao
    } else {
        "Explore trusted products"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 10.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(lavender)
            .padding(bottom = 16.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    AutoText(
                        text = title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface                    )
                    AutoText(
                        text = subtitle,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }

            FeaturedMedicinesGrid(
                medicines = medicines.take(4),
                isDark = isDark,
                onSeeAllClick = { navController.navigate(Screen.PharmacyList.route) },
                onAddToCartClick = onAddToCartClick,
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
    }
}