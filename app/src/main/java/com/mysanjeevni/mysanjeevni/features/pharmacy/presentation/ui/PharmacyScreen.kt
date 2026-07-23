package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui


import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.core.presentation.StylishHeader
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.home.presentation.viewmodel.HomeViewModel
import com.mysanjeevni.mysanjeevni.features.notification.presentation.viewmodel.NotificationViewModel
import com.mysanjeevni.mysanjeevni.features.pharmacy.data.mapper.toCartItem
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui.components.MedicineItem
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.viewmodel.PharmacyViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import kotlinx.coroutines.flow.distinctUntilChanged

// --- THEME COLORS ---
val TealPrimary = Color(0xFF38D6C6)
val LavenderPrimary = Color(0xFF7E57C2)

private val SortOptions = listOf("Relevance", "Price: Low to High", "Price: High to Low", "Discount")


@Composable
fun PharmacyScreen(
    navController: NavController,
    viewModel: PharmacyViewModel = hiltViewModel(),
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val activity = LocalActivity.current as ComponentActivity
    val cartViewModel: CartViewModel = hiltViewModel(activity)

    val state by viewModel.state.collectAsState()
    val colorScheme = MaterialTheme.colorScheme
    val bgColor = colorScheme.background
    val city by homeViewModel.userCity.collectAsState()

    val notificationViewModel: NotificationViewModel = hiltViewModel()
    val unreadCount by notificationViewModel.unreadCount.collectAsState()


    val searchQuery by viewModel.searchQuery.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }
    var selectedSort by remember { mutableStateOf("Relevance") }
    val focusRequester = remember { FocusRequester() }
    val listState = rememberLazyListState()

    val availableCategories: List<String> = remember(state.medicines) {
        state.medicines
            .mapNotNull { it.category.takeIf { c -> c.isNotBlank() } }
            .distinct()
    }


    LaunchedEffect(state.medicines) {

        Log.d("PHARMACY_DEBUG", "========== CATEGORIES ==========")

        availableCategories.forEach {
            Log.d("PHARMACY_DEBUG", "Category=$it")
        }

        Log.d("PHARMACY_DEBUG", "========== MEDICINES ==========")

        state.medicines.forEach {
            Log.d(
                "PHARMACY_DEBUG",
                "Medicine=${it.name}, category=${it.category}, productType=${it.productType}"
            )
        }
    }

    LaunchedEffect(city) {
        Log.d("CITY_DEBUG", "Pharmacy City = $city")
    }
    val displayedMedicines = remember(
        state.medicines,
        selectedFilter,
        selectedSort,
        searchQuery
    ) {

        var filtered = state.medicines

        // Category Filter
        if (selectedFilter != "All") {
            filtered = filtered.filter {
                it.category.equals(
                    selectedFilter,
                    ignoreCase = true
                )
            }
        }

        // Search Filter
        if (searchQuery.isNotBlank()) {

            filtered = filtered.filter { medicine ->

                medicine.name.contains(
                    searchQuery,
                    ignoreCase = true
                )
            }
        }

        // Sorting
        when (selectedSort) {

            "Price: Low to High" ->
                filtered.sortedBy { it.price }

            "Price: High to Low" ->
                filtered.sortedByDescending { it.price }

            "Discount" ->
                filtered.sortedByDescending {
                    val mrp = it.mrp
                    val price = it.price

                    if (mrp > 0) {
                        ((mrp - price) / mrp) * 100
                    } else {
                        0.0
                    }
                }

            else -> filtered
        }
    }


    LaunchedEffect(displayedMedicines) {
        Log.d(
            "PHARMACY_DEBUG",
            "Displayed Medicines = ${displayedMedicines.size}"
        )
    }

    LaunchedEffect(listState) {

        snapshotFlow {
            val layoutInfo = listState.layoutInfo

            val lastVisibleItem =
                layoutInfo.visibleItemsInfo.lastOrNull()?.index

            val totalItems =
                layoutInfo.totalItemsCount

            lastVisibleItem != null &&
                    lastVisibleItem >= totalItems - 3
        }
            .distinctUntilChanged()
            .collect { shouldLoadMore ->

                if (shouldLoadMore) {

                    Log.d(
                        "PHARMACY_PAGINATION",
                        "Bottom reached → loading next page"
                    )

                    viewModel.loadNextPage()
                }
            }
    }

    Scaffold(
        containerColor = bgColor,
        topBar = {
            Column {
                StylishHeader(
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                    onClear = { viewModel.onSearchQueryChanged("") },
                    focusRequester = focusRequester,
                    location = city,
                    onLocationClick = { navController.navigate("${Screen.ManageAddresses.route}?checkout=false&home=true") },
                    unreadCount = unreadCount,
                    onNotificationClick = { navController.navigate(Screen.NotificationScreen.route) }
                )
                FilterAndSortRow(
                    categories = availableCategories,
                    selectedFilter = selectedFilter,
                    onFilterSelected = { selectedFilter = it },
                    selectedSort = selectedSort,
                    onSortSelected = { selectedSort = it }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (!state.isLoading && state.error.isBlank()) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        AutoText(
                            text = "${displayedMedicines.size} Products found",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }

                    // ✅ Using displayedMedicines (filtered + sorted) instead of state.medicines
                    items(displayedMedicines) { medicine ->
                        MedicineItem(
                            medicine = medicine,
                            onClick = {
                                navController.navigate(Screen.MedicineDetail.createRoute(it.id))
                            },
                            onAddToCart = {
                                val cartItem = medicine.toCartItem()
                                Log.d("CART_DEBUG", "Pharmacy → Adding: ${cartItem.name}")
                                cartViewModel.addToCart(cartItem)
                                Log.d(
                                    "CART_DEBUG",
                                    "Pharmacy → After Add Size: ${cartViewModel.state.value.cartItem.size}"
                                )
                                navController.navigate(Screen.CartScreen.route)
                            }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }

            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = TealPrimary
                )
            }

            if (state.error.isNotBlank()) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(Icons.Default.Close, null, tint = Color.Red, modifier = Modifier.size(48.dp))
                    Spacer(modifier = Modifier.height(8.dp))
                    AutoText(text = state.error, color = Color.Red, fontWeight = FontWeight.Medium)
                }
            }

            if (
                !state.isLoading &&
                displayedMedicines.isEmpty() &&
                state.error.isBlank()
            ) {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            modifier = Modifier.size(90.dp),
                            tint = Color.Gray
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        AutoText(
                            text = "Product Not Found",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        AutoText(
                            text = "Try another medicine name",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterAndSortRow(
    categories: List<String>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    selectedSort: String,
    onSortSelected: (String) -> Unit
) {
    var showSortDropdown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            item {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            item {
                FilterChipItem(
                    label = "All",
                    isSelected = selectedFilter == "All",
                    onClick = { onFilterSelected("All") }
                )
            }

            items(categories) { category ->
                FilterChipItem(
                    label = category,
                    isSelected = selectedFilter == category,
                    onClick = { onFilterSelected(category) }
                )
            }
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 0.5.dp)

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            AutoText(text = "Sort by", fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

            Box {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { showSortDropdown = true }
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    AutoText(
                        text = selectedSort,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TealPrimary
                    )
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = TealPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                DropdownMenu(
                    expanded = showSortDropdown,
                    onDismissRequest = { showSortDropdown = false }
                ) {
                    SortOptions.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                AutoText(
                                    text = option,
                                    fontSize = 13.sp,
                                    color = if (option == selectedSort) TealPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onSortSelected(option)
                                showSortDropdown = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterChipItem(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val bg = if (isSelected) LavenderPrimary else MaterialTheme.colorScheme.surfaceVariant

    val textColor = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface

    Box(
        modifier = Modifier
            .height(36.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(bg)
            .clickable { onClick() }
            .padding(horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        AutoText(
            text = label,
            color = textColor,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
