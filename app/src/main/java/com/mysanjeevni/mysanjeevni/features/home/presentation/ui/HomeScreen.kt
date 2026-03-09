package com.mysanjeevni.mysanjeevni.features.home.presentation.ui

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.FeaturedMedicine
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.GridCategory
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.QuickCategory
import com.mysanjeevni.mysanjeevni.features.home.presentation.util.HomeMockData
import com.mysanjeevni.mysanjeevni.features.home.presentation.viewmodel.HomeViewModel
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.navigation.Screen

// 1mg Brand Colors
val Orange1mg = Color(0xFFFF6F61)
//val BgColor = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color(0xFF121212) else Color.White
    val contentColor = if (isDark) Color.White else Color.Black

    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    // 1. Create the FocusRequester
    val focusRequester = remember { FocusRequester() }
    Scaffold(
        topBar = {
            HomeTopBar(isDark, onCartClick = {
                navController.navigate(Screen.CartScreen.route)
            })
        },
//        bottomBar = { HomeBottomBar(isDark,navController) },
        containerColor = Color.White
    ) { paddingValues ->

        // Main Scrollable Content
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(backgroundColor)
        ) {

            // 1. Search Bar
            SearchBar(
                isDark = isDark,
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                onClear = { viewModel.onSearchQueryChanged("") },
                focusRequester = focusRequester
            )

            // 2. Quick Categories (Horizontal Scroll)
            QuickCategoriesSection(isDark)

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Prescription Banner
            PrescriptionBanner(isDark)

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Promo Banner (Carousel style)
            PromoBanner()

            Spacer(modifier = Modifier.height(24.dp))

            FeaturedMedicinesGrid(
                isDark, onSeeAllClick = {
                    navController.navigate(Screen.PharmacyList.route)
                },
                onAddToCartClick = { medicine ->
                    viewModel.addToCart(medicine)
                })

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Main Grid Categories
            Text(
                text = "Shop by Category",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = contentColor,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))

            CategoryGrid(isDark)

            // Extra spacing at bottom so content isn't hidden by nav bar
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

// --- COMPONENTS ---

@Composable
fun HomeTopBar(isDark: Boolean, onCartClick: () -> Unit) {
    val textColor = if (isDark) Color.White else Color.Black
    val containerColor = if (isDark) Color(0xFF121212) else Color.White
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(containerColor)
            .statusBarsPadding()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Orange Brand Icon
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Orange1mg),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "India",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = textColor
                )
                Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = textColor)
            }
        }

        // Cart Icon
        Icon(
            imageVector = Icons.Outlined.ShoppingBag,
            contentDescription = "Cart",
            modifier = Modifier
                .size(28.dp)
                .clickable { onCartClick() },
            tint = textColor
        )
    }
}

@Composable
fun SearchBar(
    isDark: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester // 1. Add this parameter
) {
    val borderColor = if (isDark) Color.Gray else Color.LightGray
    val textColor = if (isDark) Color.White else Color.Black
    val placeholderColor = if (isDark) Color.LightGray else Color.Gray
    val containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    // 2. Get keyboard controller to hide it manually when X is clicked
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = TextStyle(
            color = textColor,
            fontSize = 16.sp
        ),
        modifier = Modifier
            .focusRequester(focusRequester), // 3. Attach requester here
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(50.dp)
                    .background(containerColor, RoundedCornerShape(8.dp))
                    .border(1.dp, borderColor, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp)
                    // 4. Make the whole box clickable to focus input and show keyboard
                    .clickable {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = placeholderColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = "Search for 'medicines'",
                            color = placeholderColor,
                            fontSize = 16.sp
                        )
                    }
                    innerTextField()
                }

                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = placeholderColor,
                        modifier = Modifier.clickable {
                            onClear()
                            // 5. Hide keyboard and clear focus when X is clicked
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }
    )
}

@Composable
fun QuickCategoriesSection(isDark: Boolean) {
    val items = listOf(
        QuickCategory("Pharmacy", "https://cdn-icons-png.flaticon.com/512/3022/3022606.png"),
        QuickCategory("Pet Care", "https://cdn-icons-png.flaticon.com/512/3047/3047928.png", isNew = true),
        QuickCategory("Consults", "https://cdn-icons-png.flaticon.com/512/4228/4228730.png"),
        QuickCategory("Vaccines", "https://cdn-icons-png.flaticon.com/512/2855/2855843.png")
    )

    LazyRow(
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items.size) { index ->
            QuickCategoryItem(items[index], isDark = isDark)
        }
    }
}

@Composable
fun QuickCategoryItem(item: QuickCategory, isDark: Boolean) {
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.LightGray else Color.Black

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(70.dp)
    ) {
        Box(contentAlignment = Alignment.TopEnd) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.size(70.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = item.iconUrl,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            if (item.isNew) {
                Text(
                    text = stringResource(R.string.new_),
                    fontSize = 8.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Orange1mg, RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = item.name, fontSize = 12.sp, textAlign = TextAlign.Center, lineHeight = 14.sp, color = textColor)
    }
}

@Composable
fun PrescriptionBanner(isDark: Boolean) {
    val bannerColor = if (isDark) Color(0xFF102027) else Color(0xFFE3F2FD)
    val textColor = if (isDark) Color.White else Color.Black
    val iconColor = if (isDark) Color(0xFF64B5F6) else Color(0xFF1976D2)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .background(bannerColor, RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Description,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(32.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.order_with_prescription),
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = {},
            colors = ButtonDefaults.buttonColors(containerColor = if (isDark) Color.White else Color.Black),
            shape = RoundedCornerShape(6.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
            modifier = Modifier.height(36.dp)
        ) {
            Text("Order now", fontSize = 12.sp, color = if (isDark) Color.Black else Color.White)
        }
    }
}

@Composable
fun PromoBanner() {
    // A Simple Banner Image
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(140.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        AsyncImage(
            model = "https://img.freepik.com/free-vector/flat-design-medical-webinar-template_23-2149596468.jpg",
            contentDescription = "Promo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CategoryGrid(isDark: Boolean) {
    // Manually creating a grid inside a Column to avoid nesting scrollable errors
    val categories = listOf(
        GridCategory("Winter Care", "https://cdn-icons-png.flaticon.com/512/2903/2903558.png", "Trending"),
        GridCategory("Pet Care", "https://cdn-icons-png.flaticon.com/512/2171/2171991.png", "New"),
        GridCategory("Vitamins", "https://cdn-icons-png.flaticon.com/512/822/822165.png"),
        GridCategory("Ayurveda", "https://cdn-icons-png.flaticon.com/512/3365/3365318.png"),
        GridCategory("Devices", "https://cdn-icons-png.flaticon.com/512/2866/2866296.png"),
        GridCategory("Skin Care", "https://cdn-icons-png.flaticon.com/512/3050/3050186.png", "Best Seller"),
        GridCategory("Sexual Wellness", "https://cdn-icons-png.flaticon.com/512/2424/2424683.png"),
        GridCategory("Diabetes", "https://cdn-icons-png.flaticon.com/512/2855/2855846.png")
    )

    // Creating rows of 4 items
    val chunked = categories.chunked(4)

    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
        chunked.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                rowItems.forEach { item ->
                    GridCategoryItem(item, Modifier.weight(1f), isDark = isDark)
                }
                // Fill empty spaces if row has less than 4 items
                repeat(4 - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun GridCategoryItem(item: GridCategory, modifier: Modifier, isDark: Boolean) {

    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFFFF0ED)
    val textColor = if (isDark) Color.LightGray else Color.Black

    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor), // Light Peach bg
                modifier = Modifier
                    .size(75.dp)
                    .padding(top = 6.dp) // Space for badge
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            // Badge (Trending/New)
            if (item.badge != null) {
                Text(
                    text = item.badge,
                    fontSize = 8.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .background(Orange1mg, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = item.name,
            fontSize = 11.sp,
            textAlign = TextAlign.Center,
            color = textColor,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
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

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Featured Products",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
            Text(
                "See All",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF6F61),
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyHorizontalGrid(
            rows = GridCells.Fixed(2),
            modifier = Modifier
                .height(420.dp)
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(medicines) { medicine ->
                FeaturedMedicinesItem(medicine, cardColor, textColor, onAddClick = { onAddToCartClick(medicine) })
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
    Card(
        modifier = Modifier
            .width(160.dp)
            .height(200.dp)
            .clickable {},
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .height(100.dp)
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = item.imageRes,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    item.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "Bottle of 200ml",
                    fontSize = 10.sp,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        item.price,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color(0xFFFF6F61), RoundedCornerShape(4.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                            .clickable { onAddClick() }
                    ) {
                        Text(
                            text = "ADD",
                            color = Color(0xFFFF6F61),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}