package com.mysanjeevni.mysanjeevni.features.home.presentation.ui

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Medication
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.wear.compose.foundation.pager.HorizontalPager
import androidx.wear.compose.foundation.pager.rememberPagerState
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.core.location.LocationHelper
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.diabetes.presentation.ui.DiabetesScreen
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.FeaturedMedicine
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.GridCategory
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.HomeBannerData
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.HomeCategorySection
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.HomeCategoryType
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.promoBanners
import com.mysanjeevni.mysanjeevni.features.home.presentation.util.HomeCategoryItems
import com.mysanjeevni.mysanjeevni.features.home.presentation.util.HomeCategoryMock
import com.mysanjeevni.mysanjeevni.features.home.presentation.util.HomeMockData
import com.mysanjeevni.mysanjeevni.features.home.presentation.viewmodel.HomeViewModel
import com.mysanjeevni.mysanjeevni.features.location.ui.LocationSearchSDialog
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.ui.MedicineScreen
import kotlinx.coroutines.delay
import java.util.Locale

val LavenderPrimary = Color(0xFF00C853) 
val LavenderBg = Color(0xFFEDE7F6)      
val SearchBarBg = Color.White
val OrangeAction = Color(0xFFFF6F61)    
val TealAccent = Color(0xFF26A69A)     

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color(0xFF121212) else Color.White

    // --- EXISTING LOGIC ---
    val searchQuery by viewModel.searchQuery.collectAsState()
    val context = LocalContext.current
    val city by viewModel.userCity.collectAsState()
    val locationHelper = remember { LocationHelper(context) }
    val focusRequester = remember { FocusRequester() }
    var showLocationDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val medicineSectionRequester = remember { BringIntoViewRequester() }
    // Category Selection State
    var selectedCategory by remember { mutableStateOf(HomeCategoryType.ALL) }

    if (showLocationDialog) {
        LocationSearchSDialog(
            onConfirm = {
                viewModel.updateCity(it)
            },
            onDismiss = { }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            locationHelper.getCurrentCity { viewModel.updateCity(it) }
        }
    }
    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationHelper.getCurrentCity { viewModel.updateCity(it) }
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            StylishHeader(
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                onClear = { viewModel.onSearchQueryChanged("") },
                focusRequester = focusRequester,
                location = city,
                onLocationClick = { showLocationDialog = true },
                onCartClick = { navController.navigate(Screen.CartScreen.route) },
                onConsultClick = { navController.navigate(Screen.Consult.route) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Categories Tabs
            StylishCategoryTabs()

            Spacer(modifier = Modifier.height(16.dp))

            SlideableBannerSection()
            Spacer(modifier = Modifier.height(8.dp))

            // 3. Order with Prescription
            PrescriptionActionCard()

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Action Grid
            ActionGridSection()

            Spacer(modifier = Modifier.height(20.dp))

            ZeroFeeBanner()

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF2196F3)) // 🔵 LIGHT BLUE
                    .padding(vertical = 12.dp)
            ) {

                DealOfTheDayHeader(isDark)

                FeaturedMedicinesGrid(
                    isDark,
                    onSeeAllClick = { navController.navigate(Screen.PharmacyList.route) },
                    onAddToCartClick = { medicine -> viewModel.addToCart(medicine) }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Shop by Category",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            )

            // 🔹 MEDICINES (green)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF38D6C6))
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Medicines",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Medium
                    )
                }
                MedicineScreen()
            }

// 🔥 GAP
            Spacer(modifier = Modifier.height(12.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF90CAF9)) // 🔵 blue
                    .padding(vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = "Diabetes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }
                DiabetesScreen()
            }
            Spacer(modifier = Modifier.height(12.dp))

            // 10. DYNAMIC SECTIONS (Logic to show items based on selection)
            // -------------------------------------------------------------
            val sectionsToShow = remember(selectedCategory) {
                if (selectedCategory == HomeCategoryType.ALL) {
                    HomeCategoryMock.getSections()
                } else {
                    // Filter items based on selected category type
                    val items = HomeCategoryItems.getItems(selectedCategory)
                    if (items.isNotEmpty()) {
                        listOf(
                            HomeCategorySection(
                                id = selectedCategory.name,
                                // Convert ENUM_NAME to Title Case (e.g. PERSONAL_CARE -> Personal Care)
                                title = selectedCategory.name.replace("_", " ")
                                    .lowercase()
                                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                                items = items
                            )
                        )
                    } else emptyList()
                }
            }

            if (sectionsToShow.isNotEmpty()) {
                CategorySectionsWithItems(sections = sectionsToShow)
            }
            // -------------------------------------------------------------

            FooterTextInfo(isDark)

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}


@Composable
fun CategorySectionsWithItems(
    sections: List<HomeCategorySection>,
    onItemClick: (String) -> Unit = {}
) {

    Column(modifier = Modifier.fillMaxWidth()) {

        sections.forEach { section ->
            val title = section.title.lowercase()

            val bgColor = when {
                title.contains("ayurvedic") -> Color(0xFFFFCC80)     // 🔵 blue
                title.contains("personal care") -> Color(0xFFFCE4EC)  // 🌸 pink
                title.contains("health") -> Color(0xFFE8F5E9)         // 💚 green
                else -> Color(0xFFF1F8E9)                             // fallback light green
            }

            // 🔥 Section Container (Main Upgrade)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(bgColor)
                    .padding(vertical = 16.dp)
            ) {

                // 🏷️ Section Title
                Text(
                    text = section.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                // 📦 Grid Layout
                val rows = section.items.chunked(3)

                Column(modifier = Modifier.padding(horizontal = 10.dp)) {
                    rows.forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            rowItems.forEach { item ->
                                CategoryItemCard(
                                    title = item.title,
                                    imageUrl = item.imageUrl,
                                    modifier = Modifier.weight(1f),
                                    onClick = { onItemClick(item.id) }
                                )
                            }

                            repeat(3 - rowItems.size) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItemCard(
    title: String,
    imageUrl: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier
            .padding(4.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Square Card for Image
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp),
            modifier = Modifier.size(90.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Item Name
        Text(
            text = title,
            fontSize = 12.sp,
            color = if (isSystemInDarkTheme()) Color.White else Color.Black,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 14.sp
        )
    }
}


@Composable
fun StylishCategoryTabs() {
    val tabs = listOf(
        "All" to Icons.Default.Dashboard,
        "Medicine" to Icons.Default.Medication,
        "Lab Test" to Icons.Default.Science,
        "Wellness" to Icons.Default.Spa,
        "Devices" to Icons.Default.MonitorHeart
    )

    var selectedIndex by remember { mutableIntStateOf(0) }

    Spacer(modifier = Modifier.height(10.dp))

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        LazyRow(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(tabs.size) { index ->

                val (name, icon) = tabs[index]
                val isSelected = index == selectedIndex

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { selectedIndex = index }
                ) {

                    Card(
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(if (isSelected) 8.dp else 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) Color(0xFF38D6C6) else Color.White
                        ),
                        modifier = Modifier.size(60.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else Color(0xFF38D6C6),
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = name,
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = if (isSelected) Color(0xFF38D6C6) else Color.Gray
                    )

                    if (isSelected) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF38D6C6))
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SlideableBannerSection() {
    val pagerState = rememberPagerState(pageCount = { promoBanners.size })
    LaunchedEffect(Unit) {
        while (true) {
            delay(3000)
            val nextPage = (pagerState.currentPage + 1) % promoBanners.size
            pagerState.animateScrollToPage(nextPage)
        }
    }
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        HorizontalPager(state = pagerState, contentPadding = PaddingValues(horizontal = 16.dp)) { page ->
            BannerCardItem(promoBanners[page])
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            repeat(promoBanners.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) LavenderPrimary else Color.LightGray
                val width = if (pagerState.currentPage == iteration) 24.dp else 8.dp
                Box(
                    modifier = Modifier
                        .padding(3.dp)
                        .height(8.dp)
                        .width(width)
                        .clip(CircleShape)
                        .background(color)
                        .animateContentSize()
                )
            }
        }
    }
}


@Composable
fun BannerCardItem(banner: HomeBannerData) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Image(
            painter = painterResource(id = banner.imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
            alignment = Alignment.CenterStart
        )
    }
}


@Composable
fun PrescriptionActionCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2F1)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween  // ✅ SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)  // ✅ weight(1f) diya
            ) {
                Icon(Icons.Default.Description, null, tint = TealAccent, modifier = Modifier.size(24.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Order with prescription", fontWeight = FontWeight.Bold, color = Color.Black, fontSize = 14.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))  // ✅ gap before button

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),  // ✅ proper padding
                modifier = Modifier.wrapContentSize()
            ) {
                Text("Upload Now", fontSize = 12.sp, maxLines = 1)  // ✅ maxLines = 1
            }
        }
    }
}

@Composable
fun ActionGridSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFFF1F8E9))) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("No Prescription?", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.AutoMirrored.Filled.ArrowForwardIos,
                        null,
                        tint = Color(0xFF558B2F),
                        modifier = Modifier.size(10.dp)
                    )
                }
                Text("Get FREE Consultation!", fontSize = 11.sp, color = Color.Gray)
            }
        }
        Card(modifier = Modifier.weight(1f), colors = CardDefaults.cardColors(containerColor = Color(0xFFF3E5F5))) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Call to order", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color.Black)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.Call, null, tint = LavenderPrimary, modifier = Modifier.size(14.dp))
                }
                Text("Our team will assist you.", fontSize = 11.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun ZeroFeeBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF38D6C6)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Stars, null, tint = Color.White)
            Spacer(modifier = Modifier.width(8.dp))
            Text("ZERO ", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFFFFD54F))
            Text("Handling Charges", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}

@Composable
fun DealOfTheDayHeader(isDark: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Deal Of The Day",
            fontSize = 18.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (isDark) Color.White else Color.Black
        )
        Spacer(modifier = Modifier.width(12.dp))
        Surface(color = Color.Red, shape = RoundedCornerShape(4.dp)) {
            Text(
                "10h : 42m : 38s",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
}

@Composable
fun FeaturedMedicinesGrid(
    isDark: Boolean,
    onSeeAllClick: () -> Unit,
    onAddToCartClick: (FeaturedMedicine) -> Unit
) {
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val medicines = HomeMockData.getMedicines()

    LazyHorizontalGrid(
        rows = GridCells.Fixed(2),
        modifier = Modifier.height(560.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(medicines) { medicine ->
            FeaturedMedicinesItem(medicine, cardColor, textColor, onAddClick = { onAddToCartClick(medicine) })
        }
        item {
            SeeAllCard(isDark) { onSeeAllClick() }
        }
    }
}

@Composable
fun StylishHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
    location: String,
    onLocationClick: () -> Unit,
    onCartClick: () -> Unit,
    onConsultClick: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(Color(0xFF38D6C6))
            .statusBarsPadding()
            .padding(bottom = 6.dp) // 🔥 compact
    ) {

        // 🔹 TOP ROW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp), // 🔥 reduced
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
                    Text(
                        text = "Delivering to",
                        fontSize = 11.sp,
                        color = Color.White
                    )
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color.White.copy(alpha = 0.8f),
                        modifier = Modifier.size(14.dp)
                    )
                }

                Text(
                    text = location.ifEmpty { "Select Location" },
                    fontSize = 14.sp, // 🔥 slightly reduced
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    maxLines = 1
                )
            }


            Row(verticalAlignment = Alignment.CenterVertically) {

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.2f))
                        .clickable { onConsultClick() }
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Call,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Consult",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

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

        Spacer(modifier = Modifier.height(6.dp)) // 🔥 reduced

        // 🔍 SEARCH BAR
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(44.dp), // 🔥 compact
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp) // 🔥 soft shadow
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
                        Text(
                            text = "Search medicines, doctors...",
                            color = Color.LightGray,
                            fontSize = 13.sp
                        )
                    }

                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp
                        ),
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

@Composable
fun FeaturedMedicinesItem(
    item: FeaturedMedicine,
    cardColor: Color,
    textColor: Color,
    onAddClick: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(0) }
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable {},
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier
                    .height(110.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = item.imageRes,
                    contentDescription = null,
                    modifier = Modifier.padding(4.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = textColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = "Bottle of 200ml", fontSize = 10.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = item.price, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "₹999", fontSize = 10.sp, textDecoration = TextDecoration.LineThrough, color = Color.Gray)
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "5% OFF", fontSize = 10.sp, color = Color(0xFF43A047), fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (quantity == 0) {
                Button(
                    onClick = { quantity = 1; onAddClick() },
                    colors = ButtonDefaults.buttonColors(containerColor = TealAccent),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("ADD", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(36.dp)
                        .background(Color.White)
                        .border(1.dp, TealAccent, RoundedCornerShape(8.dp)),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable { if (quantity > 0) quantity-- },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Remove,
                            contentDescription = "Remove",
                            tint = TealAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Text(text = "$quantity", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .weight(1f)
                            .clickable { quantity++; onAddClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add",
                            tint = TealAccent,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryGrid(
    isDark: Boolean,
    onCategorySelected: (HomeCategoryType) -> Unit
) {

    val categories = listOf(
        GridCategory("All", "https://cdn-icons-png.flaticon.com/512/1828/1828859.png", HomeCategoryType.ALL),
        GridCategory(
            "Medicines",
            "https://cdn-icons-png.flaticon.com/128/3022/3022827.png",
            HomeCategoryType.MEDICINES
        ),
        GridCategory(
            "Medicines",
            "https://www.flaticon.com/free-icon/diabetes-test_12310015",
            HomeCategoryType.DIABETES
        ),
        GridCategory("Devices", "https://cdn-icons-png.flaticon.com/128/10648/10648054.png", HomeCategoryType.DEVICES),
        GridCategory(
            "Personal Care",
            "https://cdn-icons-png.flaticon.com/512/3050/3050186.png",
            HomeCategoryType.PERSONAL_CARE
        ),
        GridCategory("Fitness", "https://cdn-icons-png.flaticon.com/512/2964/2964514.png", HomeCategoryType.FITNESS),
        GridCategory("Petcare", "https://cdn-icons-png.flaticon.com/512/2171/2171991.png", HomeCategoryType.PETCARE),
        GridCategory("Eyewear", "https://cdn-icons-png.flaticon.com/128/3204/3204190.png", HomeCategoryType.EYEWEAR)
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            categories.take(4).forEach { category ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CategoryItem(category, onCategorySelected)
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 🔥 Second Row (remaining items)
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            val secondRowItems = categories.drop(4)

            secondRowItems.forEach { category ->
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CategoryItem(category, onCategorySelected)
                }
            }

            // 🔥 EMPTY SPACE FILL (IMPORTANT)
            repeat(4 - secondRowItems.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: GridCategory,
    onCategorySelected: (HomeCategoryType) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            onCategorySelected(category.type)
        }
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(3.dp),
            modifier = Modifier.size(70.dp)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                AsyncImage(
                    model = category.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = category.name,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun SeeAllCard(
    isDark: Boolean,
    onClick: () -> Unit
) {
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val contentColor = if (isDark) Color.White else Color(0xFF38D6C6)

    Card(
        modifier = Modifier
            .width(160.dp)
            .fillMaxHeight()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Circle with Arrow
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(contentColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                        contentDescription = "See All",
                        tint = contentColor,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "See All",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor
                )

                Text(
                    text = "View all products",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun FooterTextInfo(isDark: Boolean) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Our Services",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = if (isDark) Color.White else Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "MySanjeevni is India's most preferred online healthcare portal that offers end-to-end solutions for many of the pressing health issues faced by Indians today.",
            fontSize = 12.sp,
            color = Color.Gray,
            lineHeight = 18.sp
        )
    }
}




