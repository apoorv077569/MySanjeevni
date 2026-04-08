package com.mysanjeevni.mysanjeevni.features.cart.presentation.ui

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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, viewModel: CartViewModel = hiltViewModel()) {

    val state by viewModel.state.collectAsState()
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.my_cart), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
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
                CartBottomBar(state.totalBill, isDark,navController)
            }
        }, containerColor = bgColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            else if (state.cartItem.isEmpty()) {
                EmptyCartView(textColor)
            }
            else{
                LazyColumn(modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(state.cartItem){item ->
                        CartItemRow(
                            item = item,
                            isDark = isDark,
                            onIncrease = {viewModel.incrementQty(item)},
                            onDecrease = {viewModel.decrementQty(item)}
                        )
                    }
                    item{
                        BillSummary(state.cartItem,state.totalBill,isDark)
                    }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }
}

@Composable
fun EmptyCartView(textColor: Color) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = Color.Gray
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("Your cart is empty!", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
        Text("Add medicines to proceed", fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun CartItemRow(
    item : CartItem,
    isDark: Boolean,
    onIncrease :() -> Unit,
    onDecrease :() -> Unit
) {
    val cardColor = if(isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if(isDark) Color.White else Color.Black

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Card(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.size(60.dp),
                colors = CardDefaults.cardColors(containerColor = Color.LightGray.copy(alpha = 0.2f))
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.name,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = textColor,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text="₹${item.price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text= "₹${item.originalPrice}",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .border(1.dp, Color(0xFFFF6F61), RoundedCornerShape(4.dp))
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) { 
                Icon(
                    imageVector = if (item.qty > 1) Icons.Default.Remove else Icons.Default.Delete,
                    contentDescription = stringResource(R.string.decrease),
                    tint = Color(0xFFFF6F61),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onDecrease() }
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "${item.qty}",
                    color = Color(0xFFFF6F61),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(12.dp))
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.increase),
                    tint = Color(0xFFFF6F61),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onIncrease() }
                )
            }
        }
    }
}

@Composable
fun BillSummary(cartItems: List<CartItem>,grandTotal: Double,isDark: Boolean) {
    val cardColor = if(isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if(isDark) Color.White else Color.Black

    val itemTotal = cartItems.sumOf { it.price * it.qty }
    val deliveryFee = if(itemTotal > 500) 0.0 else 40.0

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                stringResource(R.string.bill_summary),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = textColor
                )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.item_total),
                    color = Color.Gray,
                    fontSize = 14.sp
                    )
                val roundedTotal = String.format("%.2f",itemTotal).toDouble()

                Text("₹$roundedTotal",color=textColor, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.delivery_fee),
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                if(deliveryFee == 0.0){
                    Text(
                        stringResource(R.string.free),
                        color = Color(0xFF008000),
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }else{
                    Text(
                        "₹$deliveryFee",
                        color = textColor,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier  = Modifier.height(12.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.grand_total),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = textColor
                )
                Text(
                    "₹$grandTotal",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = textColor
                )
            }
        }
    }
}

@Composable
fun CartBottomBar(grandTotal: Double,isDark: Boolean,navController: NavController) {
    val containerColor = if(isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if(isDark) Color.White else Color.Black

    Surface(
        shadowElevation = 16.dp,
        color = containerColor
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column{
                Text(
                    stringResource(R.string.total_to_pay),
                    fontSize = 12.sp,
                    color = Color.Gray
                    )
                Text(
                    "₹$grandTotal",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
            Button(
                onClick = {navController.navigate("${Screen.ManageAddresses.route}?checkout=true")},
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F61)),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .height(48.dp)
                    .width(180.dp)
            ) {
                Text(
                    stringResource(R.string.checkout),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                    )
            }
        }
    }
}