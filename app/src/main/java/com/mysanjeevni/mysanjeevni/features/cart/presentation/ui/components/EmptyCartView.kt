package com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun EmptyCartView(
    textColor: Color
) {

    Column(

        modifier =
            Modifier.fillMaxSize(),

        horizontalAlignment =
            Alignment.CenterHorizontally,

        verticalArrangement =
            Arrangement.Center

    ) {

        Icon(

            imageVector =
                Icons.Outlined.ShoppingCart,

            contentDescription = null,

            modifier =
                Modifier.size(100.dp),

            tint =
                Color.Gray
        )

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        AutoText(

            stringResource(
                R.string.your_cart_is_empty
            ),

            fontSize = 18.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                textColor
        )

        AutoText(

            stringResource(
                R.string.add_medicines_to_proceed
            ),

            fontSize = 14.sp,

            color =
                Color.Gray
        )
    }
}