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
import androidx.compose.runtime.rememberCoroutineScope
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
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.home.presentation.viewmodel.HomeViewModel
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address
import com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel.AddressViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.dilaog.AddressDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageAddresses(
    navController: NavController,
    viewModel: AddressViewModel = hiltViewModel(),
    cartViewModel: CartViewModel,
    isCheckout: Boolean = false,
    orderViewModel: OrderViewModel,
    isHome: Boolean = false,
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val cartState by cartViewModel.state.collectAsState()

    val snackbarHostState = remember {
        SnackbarHostState()
    }
    var pendingCheckoutAddress by remember {
        mutableStateOf<Address?>(null)
    }

    var showDialog by remember {
        mutableStateOf(false)
    }

    var selectedAddressForEdit by remember {
        mutableStateOf<Address?>(null)
    }
    val serviceabilityState by viewModel
        .serviceabilityState
        .collectAsState()

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        Log.d(
            "ADDRESS_DEBUG",
            "isHome = $isHome"
        )

        Log.d(
            "ADDRESS_DEBUG",
            "isCheckout = $isCheckout"
        )
    }

    LaunchedEffect(state.error) {
        state.error?.let { error ->
            snackbarHostState.showSnackbar(error)
        }
    }

    LaunchedEffect(cartState.cartItem) {

        Log.d(
            "ADDRESS_SCREEN",
            "Cart Count = ${cartState.cartItem.size}"
        )

        cartState.cartItem.forEach { item ->
            Log.d(
                "ADDRESS_SCREEN",
                "Item=${item.name}, Qty=${item.qty}"
            )
        }
    }

    val isDark =
        LocalIsDarkTheme.current ||
                isSystemInDarkTheme()

    val bgColor =
        if (isDark) {
            Color(0xFF121212)
        } else {
            Color(0xFFF5F7FA)
        }

    val cardColor =
        if (isDark) {
            Color(0xFF1E1E1E)
        } else {
            Color.White
        }

    val textColor =
        if (isDark) {
            Color.White
        } else {
            Color.Black
        }

    val secondaryText =
        if (isDark) {
            Color.LightGray
        } else {
            Color.Gray
        }

    val primaryColor =
        MaterialTheme.colorScheme.primary

    // =====================================================
    // ADDRESS DIALOG
    // =====================================================

    if (showDialog) {

        AddressDialog(
            addressToEdit = selectedAddressForEdit,

            onDismiss = {
                showDialog = false
                selectedAddressForEdit = null
            },
            addressViewModel = viewModel,
            onSave = { address ->

                Log.d(
                    "ADDRESS_SCREEN",
                    "Dialog Save Address = $address"
                )

                if (selectedAddressForEdit == null) {

                    // ADD ADDRESS
                    viewModel.addAddress(
                        address = address
                    )

                } else {

                    // UPDATE ADDRESS
                    val addressId =
                        selectedAddressForEdit!!.id

                    viewModel.updateAddress(
                        id = addressId,
                        address = address.copy(
                            id = addressId,

                            // Existing values preserve
                            userId = selectedAddressForEdit!!
                                .userId,

                            country = selectedAddressForEdit!!
                                .country,

                            createdAt = selectedAddressForEdit!!
                                .createdAt,

                            updatedAt = selectedAddressForEdit!!
                                .updatedAt
                        )
                    )
                }

                showDialog = false
                selectedAddressForEdit = null
            }
        )
    }

    Scaffold(
        containerColor = bgColor,

        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },

        topBar = {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        Icons.AutoMirrored.Filled.ArrowBack,

                    contentDescription =
                        stringResource(R.string.back),

                    tint = textColor,

                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                AutoText(
                    text = stringResource(
                        R.string.manage_addresses
                    ),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
            }
        },

        floatingActionButton = {

            FloatingActionButton(
                onClick = {

                    // New address
                    selectedAddressForEdit = null
                    showDialog = true
                },
                containerColor = primaryColor,
                contentColor = Color.White
            ) {

                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription =
                        stringResource(
                            R.string.add_address
                        )
                )
            }
        }
    ) { paddingValues ->

        when {

            // =====================================================
            // LOADING
            // =====================================================

            state.isLoading -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),

                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator(
                        color = primaryColor
                    )
                }
            }

            // =====================================================
            // EMPTY
            // =====================================================

            state.addresses.isEmpty() -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        AutoText(
                            text = "No addresses saved",
                            fontSize = 16.sp,
                            color = secondaryText
                        )

                        Spacer(
                            modifier = Modifier.height(8.dp)
                        )

                        AutoText(
                            text = "Tap + to add new address",
                            fontSize = 14.sp,
                            color = secondaryText
                        )
                    }
                }
            }

            // =====================================================
            // ADDRESS LIST
            // =====================================================

            else -> {

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(
                            horizontal = 16.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(16.dp),

                    contentPadding =
                        PaddingValues(
                            bottom = 80.dp
                        )
                ) {

                    items(
                        items = state.addresses,
                        key = { address ->
                            address.id
                        }
                    ) { address ->

                        AddressCardItem(
                            address = address,

                            cardColor = cardColor,

                            textColor = textColor,

                            secondaryText = secondaryText,

                            primaryColor = primaryColor,

                            // =====================================
                            // EDIT
                            // =====================================

                            onEdit = {

                                Log.d(
                                    "ADDRESS_SCREEN",
                                    "Edit Address = ${address.id}"
                                )

                                selectedAddressForEdit =
                                    address

                                showDialog = true
                            },

                            // =====================================
                            // DELETE
                            // =====================================

                            onDelete = {

                                Log.d(
                                    "ADDRESS_SCREEN",
                                    "Delete Address = ${address.id}"
                                )

                                viewModel.deleteAddress(
                                    addressId = address.id
                                )
                            },

                            // =====================================
                            // SET DEFAULT
                            // =====================================

                            onSetDefault = {

                                Log.d(
                                    "ADDRESS_SCREEN",
                                    "Set Default = ${address.id}"
                                )

                                // address already Domain Address hai.
                                // AddressModel banane ki zarurat nahi.
                                val updatedAddress =
                                    address.copy(
                                        isDefault = true
                                    )

                                viewModel.updateAddress(
                                    id = address.id,
                                    address = updatedAddress
                                )
                            },

                            // =====================================
                            // SELECT
                            // =====================================

                            onSelect = {

                                Log.d(
                                    "ADDRESS_DEBUG",
                                    "CLICK -> isHome=$isHome " +
                                            "isCheckout=$isCheckout"
                                )

                                Log.d(
                                    "ADDRESS_DEBUG",
                                    "Selected Address = " +
                                            address.addressLine1
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
                                    "Full Address = " +
                                            "${address.city} - " +
                                            address.pincode
                                )
                                viewModel.checkServiceability(address.pincode)
                                if (isCheckout) {

                                    scope.launch {

                                        val available =
                                            viewModel.checkServiceabilityForCheckout(
                                                address.pincode
                                            )

                                        if (!available) {

                                            snackbarHostState.showSnackbar(
                                                "❌ Sorry! Delivery is not available for this pincode."
                                            )

                                            return@launch
                                        }

                                        snackbarHostState.showSnackbar(
                                            "✅ Deliverable via ${viewModel.serviceabilityState.value.courierName}"
                                        )

                                        // Save Shiprocket details
                                        orderViewModel.setShippingDetails(

                                            charge = viewModel.serviceabilityState.value.deliveryCharge,

                                            courier = viewModel.serviceabilityState.value.courierName,

                                            days = viewModel.serviceabilityState.value.estimatedDeliveryDays,

                                            date = viewModel.serviceabilityState.value.estimatedDeliveryDate
                                        )

                                        val selectedAddress =
                                            "${address.fullName}," +
                                                    "${address.type}," +
                                                    "${address.city}," +
                                                    address.phone

                                        // Existing logic (unchanged)
                                        orderViewModel.setAddress(address)

                                        orderViewModel.setCartItems(
                                            cartState.cartItem
                                        )

                                        Log.d(
                                            "ADDRESS_DEBUG",
                                            "Selected Address = $selectedAddress"
                                        )

                                        navController.navigate(
                                            Screen.SummaryScreen.route
                                        )
                                    }

                                } else if (isHome) {

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
}

@Composable
fun AddressCardItem(
    address: Address,
    cardColor: Color,
    textColor: Color,
    secondaryText: Color,
    primaryColor: Color,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onSetDefault: () -> Unit,
    onSelect: () -> Unit,
    isCheckOut: Boolean,
    isHome: Boolean
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onSelect()
            },

        shape = RoundedCornerShape(12.dp),

        colors = CardDefaults.cardColors(
            containerColor = cardColor
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),

        border =
            if (address.isDefault) {
                BorderStroke(
                    width = 1.dp,
                    color = primaryColor
                )
            } else {
                null
            }
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Icon(
                    imageVector =
                        if (
                            address.type.equals(
                                "home",
                                ignoreCase = true
                            )
                        ) {
                            Icons.Default.Home
                        } else {
                            Icons.Default.Work
                        },

                    contentDescription = null,

                    tint =
                        if (address.isDefault) {
                            primaryColor
                        } else {
                            secondaryText
                        },

                    modifier = Modifier.size(20.dp)
                )

                Spacer(
                    modifier = Modifier.width(8.dp)
                )

                AutoText(
                    text = address.type
                        .replaceFirstChar {
                            it.uppercase()
                        },

                    fontSize = 16.sp,

                    fontWeight = FontWeight.Bold,

                    color = textColor
                )

                if (address.isDefault) {

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    AutoText(
                        text = "(Default)",
                        fontSize = 12.sp,
                        color = primaryColor
                    )
                }

                Spacer(
                    modifier = Modifier.weight(1f)
                )

                // =====================================
                // EDIT ICON
                // =====================================

                Icon(
                    imageVector = Icons.Default.Edit,

                    contentDescription = "Edit",

                    tint = secondaryText,

                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            onEdit()
                        }
                )

                Spacer(
                    modifier = Modifier.width(16.dp)
                )

                // =====================================
                // DELETE ICON
                // =====================================

                Icon(
                    imageVector =
                        Icons.Default.DeleteOutline,

                    contentDescription = "Delete",

                    tint = Color.Red.copy(
                        alpha = 0.7f
                    ),

                    modifier = Modifier
                        .size(20.dp)
                        .clickable {
                            onDelete()
                        }
                )
            }

            HorizontalDivider(
                modifier = Modifier.padding(
                    vertical = 12.dp
                ),

                color = secondaryText.copy(
                    alpha = 0.2f
                )
            )

            AutoText(
                text = address.fullName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = textColor
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            AutoText(
                text = address.addressLine1,
                fontSize = 14.sp,
                color = secondaryText
            )

            if (address.addressLine2.isNotBlank()) {

                AutoText(
                    text = address.addressLine2,
                    fontSize = 14.sp,
                    color = secondaryText
                )
            }

            AutoText(
                text =
                    "${address.city}, " +
                            "${address.state} - " +
                            address.pincode,

                fontSize = 14.sp,

                color = secondaryText
            )

            if (address.country.isNotBlank()) {

                AutoText(
                    text = address.country,
                    fontSize = 14.sp,
                    color = secondaryText
                )
            }

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            AutoText(
                text = "Phone: ${address.phone}",
                fontSize = 14.sp,
                color = secondaryText,
                fontWeight = FontWeight.Medium
            )

            if (!address.isDefault) {

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                AutoText(
                    text = "Set as Default",

                    color = primaryColor,

                    fontSize = 14.sp,

                    fontWeight = FontWeight.Bold,

                    modifier = Modifier.clickable {
                        onSetDefault()
                    }
                )
            }

            // Optional visual hint based on mode
            if (isCheckOut || isHome) {

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                AutoText(
                    text =
                        if (isCheckOut) {
                            "Tap to deliver here"
                        } else {
                            "Tap to use this location"
                        },

                    fontSize = 12.sp,

                    color = primaryColor,

                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}