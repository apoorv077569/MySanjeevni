package com.mysanjeevni.mysanjeevni.features.home.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.core.presentation.StylishHeader
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.category.presenttion.viewmodel.CategoryViewModel
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components.CategoryProductsSection
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components.FooterTextInfo
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components.PopularProductsSection
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components.PrescriptionActionCard
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components.ProductNotFound
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components.SlideableBannerSection
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.sekeleton.HomeScreenSkeleton
import com.mysanjeevni.mysanjeevni.features.home.presentation.viewmodel.HomeViewModel
import com.mysanjeevni.mysanjeevni.features.location.ui.LocationSearchSDialog
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.components.MedicineCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val LavenderPrimary = Color(0xFF00C853)
val TealAccent = Color(0xFF26A69A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, scrollTo: String? = null, viewModel: HomeViewModel = hiltViewModel(),
    cartViewModel: CartViewModel
) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color(0xFF121212) else Color.White

    val isLoading by viewModel.isLoading.collectAsState()

    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val context = LocalContext.current
    val city by viewModel.userCity.collectAsState()
    val popularProducts by viewModel.popularProducts.collectAsState()
    val focusRequester = remember { FocusRequester() }
    var showLocationDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    var medicineOffset by remember { mutableIntStateOf(0) }
    val categoryViewModel: CategoryViewModel = hiltViewModel()
    val categoryState = categoryViewModel.state
    val medicines by viewModel.allMedicines.collectAsState()
    val productsByType = remember(medicines) {
        medicines.groupBy { it.productType }
    }
    val highlightBg = if (isDark) Color(0xFF16242B) else Color(0xFFE3F2FD)



    if (showLocationDialog) {
        LocationSearchSDialog(
            onConfirm = {
                viewModel.updateCity(it)
            },
            onDismiss = { }
        )
    }
    Log.d("CART_DEBUG", "HOME VM = ${cartViewModel.hashCode()}")
    LaunchedEffect(city) {
        Log.d("CITY_DEBUG", "Home City = $city")
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            viewModel.fetchCurrentCity()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            viewModel.fetchCurrentCity()
        } else {
            permissionLauncher.launch(
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }
    LaunchedEffect(scrollTo) {
        if (scrollTo == "medicines") {
            delay(400)
            coroutineScope.launch {
                scrollState.animateScrollTo(medicineOffset)
            }
        }
    }
    if (isLoading) {
        HomeScreenSkeleton()
    } else {
        Scaffold(
            containerColor = backgroundColor,
            topBar = {
                StylishHeader(
                    query = searchQuery,
                    onQueryChange = {
                        viewModel.onSearchQueryChanged(it)
                    },
                    onClear = {
                        viewModel.onSearchQueryChanged("")
                    },
                    focusRequester = focusRequester,
                    location = city,
                    onLocationClick = {
                        navController.navigate("${Screen.ManageAddresses.route}?checkout=false&home=true")
                    },
                    onNotificationClick = {
                        navController.navigate(
                            Screen.NotificationScreen.route
                        )
                    }
                )
            }
        ) { paddingValues ->

            if (searchQuery.isNotBlank()) {

                if (searchResults.isEmpty()) {
                    ProductNotFound()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(12.dp)
                    ) {

                        item {

                            Text(
                                text = "${searchResults.size} Products Found",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }

                        items(searchResults) { medicine ->
                            MedicineCard(
                                medicine = medicine,
                            )
                        }
                    }
                }

            } else {

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {

                    Spacer(modifier = Modifier.height(16.dp))
                    SlideableBannerSection()

                    Spacer(modifier = Modifier.height(8.dp))

                    PrescriptionActionCard(navController)

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(highlightBg)
                            .padding(vertical = 12.dp)
                    ) {

                        PopularProductsSection(
                            products = popularProducts,
                            onProductClick = { medicine ->
                                navController.navigate(
                                    Screen.MedicineDetail.createRoute(
                                        medicine.id
                                    )
                                )
                            },
                            onAddToCart = { medicine ->
                                cartViewModel.addToCart(
                                    CartItem(
                                        id = medicine.id,
                                        name = medicine.name,
                                        price = medicine.price,
                                        originalPrice = medicine.mrp,
                                        imageUrl = medicine.image,
                                        qty = 1
                                    )
                                )
                            },
                            cartViewModel = cartViewModel
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    categoryState.categories.forEach { category ->

                        val categoryProducts =
                            productsByType[category.name]
                                ?: emptyList()

                        CategoryProductsSection(
                            title = category.name,
                            subCategories = category.children.map { it.name },
                            medicines = categoryProducts,
                            isDark = isDark,
                            navController = navController,
                            cartViewModel = cartViewModel,
                            onAddToCartClick = { medicine ->
                                cartViewModel.addToCart(
                                    CartItem(
                                        id = medicine.id,
                                        name = medicine.name,
                                        price = medicine.price,
                                        originalPrice = medicine.mrp,
                                        imageUrl = medicine.image,
                                        qty = 1
                                    )
                                )
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    FooterTextInfo(isDark)

                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

















