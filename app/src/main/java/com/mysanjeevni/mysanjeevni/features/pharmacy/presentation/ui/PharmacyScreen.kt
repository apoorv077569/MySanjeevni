package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.location.LocationHelper
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.location.ui.LocationSearchSDialog
import com.mysanjeevni.mysanjeevni.features.pharmacy.data.mapper.toCartItem
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui.components.MedicineItem
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.viewmodel.PharmacyViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

// --- THEME COLORS ---
val LavenderPrimary = Color(0xFF7E57C2)
val BackgroundColor = Color(0xFFF5F5F5)

@Composable
fun PharmacyScreen(
    navController: NavController,
    viewModel: PharmacyViewModel = hiltViewModel()
) {
    val cartViewModel: CartViewModel =
        hiltViewModel(LocalContext.current as ComponentActivity)
    val state by viewModel.state.collectAsState()
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else BackgroundColor

    // --- 1. LOCATION & PERMISSION LOGIC (ADDED) ---
    val context = LocalContext.current
    val locationHelper = remember { LocationHelper(context) }

    // Local state for city (since PharmacyViewModel might not have updateCity)
    var userCity by remember { mutableStateOf("Detecting...") }
    var showLocationDialog by remember { mutableStateOf(false) }

    // Permission Launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            locationHelper.getCurrentCity { city -> userCity = city }
        } else {
            userCity = "Location Denied"
        }
    }

    // Fetch Location on Start
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationHelper.getCurrentCity { city -> userCity = city }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Location Dialog Popup
    if (showLocationDialog) {
        LocationSearchSDialog(
            onConfirm = { selectedCity ->
                userCity = selectedCity
                showLocationDialog = false
            },
            onDismiss = { showLocationDialog = false }
        )
    }
    // ---------------------------------------------

    // Header States
    var searchQuery by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        containerColor = bgColor,
        topBar = {
            Column {
                // Header with Real Location & Cart Navigation
                PharmacyHeader(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onClear = { searchQuery = "" },
                    focusRequester = focusRequester,
                    location = userCity, // Pass real location
                    onLocationClick = { showLocationDialog = true }, // Open Dialog
                    onCartClick = { navController.navigate(Screen.CartScreen.route) } // Real Navigation
                )

                FilterOptionsRow()
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Success State
            if (!state.isLoading && state.error.isBlank()) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        AutoText(
                            text = "${state.medicines.size} Products found",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    items(state.medicines) { medicine ->
                        MedicineItem(medicine = medicine,
                            onAddToCart = {


                                val cartItem = medicine.toCartItem()
                                Log.d("CART_DEBUG", "Pharmacy → Adding: ${cartItem.name}")

                                cartViewModel.addToCart(
                                    cartItem,
                                )

                                Log.d("CART_DEBUG", "Pharmacy → After Add Size: ${cartViewModel.state.value.cartItem.size}")


                                navController.navigate(Screen.CartScreen.route)
                            })
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            // Loading State
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = LavenderPrimary
                )
            }

            // Error State
            if (state.error.isNotBlank()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Close, null, tint = Color.Red, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    AutoText(
                        text = state.error,
                        color = Color.Red,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Empty State
            if (!state.isLoading && state.medicines.isEmpty() && state.error.isBlank()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AutoText("No medicines found", color = Color.Gray)
                }
            }
        }
    }
}


@Composable
fun FilterOptionsRow() {
    val filters = listOf("All", "Syrups", "Tablets", "Injections", "Ayurvedic")
    var selectedFilter by remember { mutableStateOf("All") }

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.background(Color.White)
    ) {
        item {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF5F5F5))
                    .clickable { },
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.FilterList, null, tint = Color.Black, modifier = Modifier.size(20.dp))
            }
        }
        items(filters) { filter ->
            val isSelected = filter == selectedFilter
            val bg = if (isSelected) LavenderPrimary else Color(0xFFF5F5F5)
            val textColor = if (isSelected) Color.White else Color.Black

            Box(
                modifier = Modifier
                    .height(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(bg)
                    .clickable { selectedFilter = filter }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                AutoText(text = filter, color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun PharmacyHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
    location: String,
    onLocationClick: () -> Unit,
    onCartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(Color(0xFF38D6C6)) // ✅ Same teal color
            .statusBarsPadding()
            .padding(bottom = 6.dp)
    ) {
        // 🔹 TOP ROW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // 📍 LOCATION
            Column(modifier = Modifier.clickable { onLocationClick() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    AutoText(text = "Delivering to", fontSize = 11.sp, color = Color.White)
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(14.dp)
                    )
                }
                AutoText(
                    text = location.ifEmpty { "Select Location" },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
            }

            // 🔔 ICONS ROW
            Row(verticalAlignment = Alignment.CenterVertically) {

                // 🔔 NOTIFICATION
                Box {
                    Icon(
                        Icons.Outlined.Notifications,
                        contentDescription = "Alerts",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.Red, CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // 🛒 CART
                Icon(
                    Icons.Outlined.ShoppingBag,
                    contentDescription = "Cart",
                    tint = Color.White,
                    modifier = Modifier
                        .size(22.dp)
                        .clickable { onCartClick() }
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // 🔍 SEARCH BAR
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { focusRequester.requestFocus() }
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        AutoText(
                            text = "Search medicines...",
                            color = Color.LightGray,
                            fontSize = 13.sp
                        )
                    }
                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                }
                if (query.isNotEmpty()) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = Color.Gray,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onClear() }
                    )
                }
            }
        }
    }
}