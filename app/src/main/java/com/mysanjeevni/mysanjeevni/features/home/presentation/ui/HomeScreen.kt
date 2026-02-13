package com.mysanjeevni.mysanjeevni.features.home.presentation.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.GridCategory
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.QuickCategory

// 1mg Brand Colors
val Orange1mg = Color(0xFFFF6F61)
val BgColor = Color(0xFFF5F7FA)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if(isDark) Color(0xFF121212) else Color.White
    val contentColor = if(isDark) Color.White else Color.Black
    Scaffold(
        topBar = { HomeTopBar(isDark) },
        bottomBar = { HomeBottomBar(isDark) },
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
            SearchBar(isDark)

            // 2. Quick Categories (Horizontal Scroll)
            QuickCategoriesSection(isDark)

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Prescription Banner
            PrescriptionBanner(isDark)

            Spacer(modifier = Modifier.height(16.dp))

            // 4. Promo Banner (Carousel style)
            PromoBanner()

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
fun HomeTopBar(isDark: Boolean) {
    val textColor = if(isDark) Color.White else Color.Black
    val containerColor = if(isDark) Color(0xFF121212) else Color.White
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
            modifier = Modifier.size(28.dp),
            tint = textColor
        )
    }
}

@Composable
fun SearchBar(isDark: Boolean) {
    val borderColor = if(isDark) Color.Gray else Color.LightGray
    val textColor = if(isDark) Color.LightGray else Color.Gray
    val iconColor = if(isDark) Color.White else Color.Black

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(50.dp)
            .border(1.dp, borderColor, RoundedCornerShape(8.dp))
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, contentDescription = null, tint = textColor)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Search for 'medicines'", color = textColor)
            Spacer(modifier = Modifier.weight(1f))
            Icon(Icons.Default.Search, contentDescription = null, tint = iconColor) // Right search icon
        }
    }
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
            QuickCategoryItem(items[index], isDark=isDark)
        }
    }
}

@Composable
fun QuickCategoryItem(item: QuickCategory,isDark: Boolean) {
   val cardColor = if(isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if(isDark) Color.LightGray else Color.Black

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
                    text = "NEW",
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
            text = "Order with prescription",
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
            Text("Order now", fontSize = 12.sp, color = if(isDark) Color.Black else Color.White)
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
fun GridCategoryItem(item: GridCategory, modifier: Modifier,isDark: Boolean) {

    val cardColor = if(isDark) Color(0xFF1E1E1E) else Color(0xFFFFF0ED)
    val textColor = if(isDark) Color.LightGray else Color.Black

    Column(
        modifier = modifier.padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.TopCenter) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor =cardColor), // Light Peach bg
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

// --- BOTTOM NAVIGATION ---

@Composable
fun HomeBottomBar(isDark: Boolean) {
    val containerColor = if(isDark) Color(0xFF1E1E1E) else Color.White
    val contentColor = if(isDark) Color.LightGray else Color.Black
    NavigationBar(
        containerColor = containerColor,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = true,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Home", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(selectedIconColor = Orange1mg, indicatorColor = Color.Transparent)
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.MedicalServices, contentDescription = null) },
            label = { Text("Health", fontSize = 10.sp, color = contentColor) }
        )

        // Center Special Button
        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFE0B2)), // Light Orange
                    contentAlignment = Alignment.Center
                ) {
                    Text("Plan", color = Color(0xFFE65100), fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
            }
        }

        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profile", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Call, contentDescription = null) },
            label = { Text("Consult", fontSize = 10.sp) }
        )
    }
}