package com.mysanjeevni.mysanjeevni.features.orders.presntation.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.orders.presntation.viewmodel.OrderViewModel

@Composable
fun OrderSummaryScreen(
    navController: NavController,
    orderViewModel: OrderViewModel
) {

    val medicine by orderViewModel
        .selectedMedicine
        .collectAsState()

    val address by orderViewModel.selectedAddress.collectAsState()
    Log.d(
        "ORDER_VM",
        "Summary VM = ${orderViewModel.hashCode()}"
    )

    LaunchedEffect(Unit) {
        Log.d("SUMMARY_DEBUG", "Address = $address")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Order Summary",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {

                Text(
                    text = "Delivery Address",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))


                    Text(
                        text = address?.fullName ?: ""
                    )

                    Text(
                        text = address?.phone ?: ""
                    )

                    Text(
                        text = address?.addressLine1 ?: ""
                    )

                    Text(
                        text = address?.addressLine2 ?: ""
                    )

                    Text(
                        text = "${address?.city}, ${address?.state}"
                    )

                    Text(
                        text = address?.pincode ?: ""
                    )
                }

            }


        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(12.dp)
            ) {

                AsyncImage(
                    model = medicine?.image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = medicine?.name ?: "",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Qty : 1")

                Text("Price : ₹${medicine?.price ?: 0}")

                Text(
                    text = "Subtotal : ₹${medicine?.price ?: 0}",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Total : ₹${medicine?.price ?: 0}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                navController.navigate(
                    Screen.PaymentScreen.route
                )
            }
        ) {
            Text("Proceed To Payment")
        }
    }
}