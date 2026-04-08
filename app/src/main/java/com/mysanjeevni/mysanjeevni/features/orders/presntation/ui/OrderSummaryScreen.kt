package com.mysanjeevni.mysanjeevni.features.orders.presntation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun OrderSummaryScreen(navController: NavController) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Order Summary 🧾", fontSize = 20.sp)

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            navController.navigate("payment")
        }) {
            Text("Proceed to Payment")
        }
    }
}