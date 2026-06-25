package com.mysanjeevni.mysanjeevni.features.profile.presentation.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.data.remote.model.AddressModel
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.home.presentation.viewmodel.HomeViewModel
import com.mysanjeevni.mysanjeevni.features.orders.presntation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressItem
import com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel.AddressViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.dilaog.AddressDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAddresses(
    navController: NavController,
    viewModel: AddressViewModel = hiltViewModel(),
    cartViewModel: CartViewModel,
    isCheckout: Boolean = false,
    orderViewModel: OrderViewModel,
    isHome:Boolean = false,
    homeViewModel: HomeViewModel = hiltViewModel()

) {
    val state by viewModel.state.collectAsState()
    val cartState by cartViewModel.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDialog by remember { mutableStateOf(false) }
    var selectedAddressForEdit by remember { mutableStateOf<AddressItem?>(null) }
    LaunchedEffect(Unit) {
        Log.d("ADDRESS_DEBUG", "isHome = $isHome")
        Log.d("ADDRESS_DEBUG", "isCheckout = $isCheckout")
    }

    state.error?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(error)
        }
    }

    LaunchedEffect(cartState.cartItem) {

        Log.d(
            "ADDRESS_SCREEN",
            "Cart Count = ${cartState.cartItem.size}"
        )

        cartState.cartItem.forEach {
            Log.d(
                "ADDRESS_SCREEN",
                "Item=${it.name}, Qty=${it.qty}"
            )
        }
    }
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryText = if (isDark) Color.LightGray else Color.Gray
    val primaryColor = MaterialTheme.colorScheme.primary

    if (showDialog) {
        AddressDialog(
            addressToEdit = selectedAddressForEdit,
            onDismiss = {
                showDialog = false
                selectedAddressForEdit = null
            },
            onSave = { addressModel ->
                if (selectedAddressForEdit == null) {
                    viewModel.addAddress(addressModel)
                } else {
                    viewModel.updateAddress(selectedAddressForEdit!!.id, addressModel)
                }
            })
    }

    Scaffold(
        containerColor = bgColor,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    tint = textColor,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                AutoText(
                    text = stringResource(R.string.manage_addresses),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedAddressForEdit = null // Reset for new entry
                    showDialog = true
                },
                containerColor = primaryColor,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_address))
            }
        }
    ) { paddingValues ->
        if (state.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = primaryColor)
            }
        } else if (state.addresses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    AutoText(
                        text = "No addresses saved",
                        fontSize = 16.sp,
                        color = secondaryText
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    AutoText(
                        text = "Tap + to add new address",
                        fontSize = 14.sp,
                        color = secondaryText
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(state.addresses) { address ->
                    AddressCardItem(
                        address = address,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryText = secondaryText,
                        primaryColor = primaryColor,
                        onEdit = {
                            selectedAddressForEdit = address
                            showDialog = true

                        },
                        onDelete = {
                            viewModel.deleteAddress(address.id)
                        },
                        onSetDefault = {
                            val addressModel = AddressModel(
                                userId = "",
                                type = address.type,
                                fullName = address.fullName,
                                phone = address.phone,
                                addressLine1 = address.addressLine1,
                                addressLine2 = address.addressLine2,
                                city = address.city,
                                state = address.state,
                                pincode = address.pincode,
                                isDefault = true
                            )

                            viewModel.updateAddress(address.id, addressModel)
                        },

                        onSelect = {
                            Log.d(
                                "ADDRESS_DEBUG",
                                "CLICK -> isHome=$isHome isCheckout=$isCheckout"
                            )
                            Log.d(
                                "ADDRESS_DEBUG",
                                "Selected Address = ${address.addressLine1}"
                            )

                            Log.d(
                                "ADDRESS_DEBUG",
                                "City = ${address.city}"
                            )

                            Log.d(
                                "ADDRESS_DEBUG",
                                "Pincode = ${address.pincode}"
                            )

                            Log.d(
                                "ADDRESS_DEBUG",
                                "Full Address = ${address.city} - ${address.pincode}"
                            )

                            if (isCheckout) {
                                val selectedAddress = "${address.fullName},${address.type}, ${
                                    address
                                        .city
                                }, ${address.phone}"
                                orderViewModel.setAddress(address)
                                orderViewModel.setCartItems(cartState.cartItem)
                                Log.d("ADDRESS_DEBUG", "Selected Address = $selectedAddress")
                                navController.navigate(Screen.SummaryScreen.route)
                            }
                            else if(isHome){

                                homeViewModel.updateCity(
                                    "${address.city}-${address.pincode}"
                                )
                                Log.d(
                                    "ADDRESS_DEBUG",
                                    "Updated Home City = ${address.city} - ${address.pincode}"
                                )
                                navController.popBackStack()
                            }
                        },
                        isCheckOut = isCheckout,
                        isHome = isHome
                    )
                }
            }
        }
    }
}



@Composable
fun AddressCardItem(
    address: AddressItem,
    cardColor: Color,
    textColor: Color,
    secondaryText: Color,
    primaryColor: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetDefault: () -> Unit,
    onSelect: () -> Unit,
    isCheckOut: Boolean,
    isHome:Boolean
) {
    Card(
        modifier = Modifier.clickable {
            onSelect()
        },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp),
        border = if (address.isDefault) BorderStroke(1.dp, primaryColor) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (address.type == "Home") Icons.Default.Home else Icons.Default.Work,
                    contentDescription = null,
                    tint = if (address.isDefault) primaryColor else secondaryText,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                AutoText(
                    text = address.type,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                if (address.isDefault) {
                    Spacer(modifier = Modifier.width(8.dp))
                    AutoText(
                        text = "(Default)",
                        fontSize = 12.sp,
                        color = primaryColor
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit",
                    tint = secondaryText,
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onEdit() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Delete",
                    tint = Color.Red.copy(alpha = 0.7f),
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onDelete() }
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 12.dp),
                color = secondaryText.copy(alpha = 0.2f)
            )

            AutoText(
                text = address.fullName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            AutoText(
                text = address.addressLine1,
                fontSize = 14.sp,
                color = secondaryText
            )
            AutoText(
                text = address.addressLine2,
                fontSize = 14.sp,
                color = secondaryText
            )
            AutoText(
                text = address.city,
                fontSize = 14.sp,
                color = secondaryText
            )
            Spacer(modifier = Modifier.height(8.dp))
            AutoText(
                text = "Phone: ${address.phone}",
                fontSize = 14.sp,
                color = secondaryText,
                fontWeight = FontWeight.Medium
            )

            if (!address.isDefault) {
                Spacer(modifier = Modifier.height(16.dp))
                AutoText(
                    text = "Set as Default",
                    color = primaryColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onSetDefault() }
                )
            }
        }
    }
}

