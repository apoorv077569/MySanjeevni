package com.mysanjeevni.mysanjeevni.features.health.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow

import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.features.health.domain.model.HealthArticle
import com.mysanjeevni.mysanjeevni.features.health.domain.model.HealthTool
import com.mysanjeevni.mysanjeevni.features.health.presentation.viemodel.HealthViewModel

// --- DUMMY DATA ---
val healthTools = listOf(
    HealthTool("BMI", "https://cdn-icons-png.flaticon.com/512/3373/3373123.png"),
    HealthTool("Period", "https://cdn-icons-png.flaticon.com/512/2747/2747059.png"),
    HealthTool("Calories", "https://cdn-icons-png.flaticon.com/512/1043/1043445.png"),
    HealthTool("Pregnancy", "https://cdn-icons-png.flaticon.com/512/3050/3050225.png")
)

val articles = listOf(
    HealthArticle("10 Superfoods for Immunity", "Nutrition", "https://img.freepik.com/free-photo/fresh-fruit-stalls-san-miguel-market_53876-146829.jpg", "5 min read"),
    HealthArticle("Yoga for Back Pain", "Fitness", "https://img.freepik.com/free-photo/young-woman-doing-yoga-mat_23-2148025178.jpg", "8 min read"),
    HealthArticle("Signs of Vitamin D Deficiency", "Wellness", "https://img.freepik.com/free-photo/doctor-holding-vitamin-d-pill_23-2148768846.jpg", "4 min read")
)

val concerns = listOf("Diabetes", "Heart", "Stomach", "Kidney", "Bone", "Mental")

@Composable
fun HealthScreen(navController: NavController, viewModel: HealthViewModel = hiltViewModel()) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val brandColor = Color(0xFF009688) // Teal color for Health section

    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()

    // Focus Control
    val focusRequester = remember { FocusRequester() }

    Scaffold(
        containerColor = bgColor,
        topBar = {
            Column(
                modifier = Modifier
                    .background(cardColor)
                    .statusBarsPadding()
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Health Hub",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )
                Text(
                    text = "Trusted advice for a healthier you",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // 1. Search Bar
            HealthSearchBar(
                isDark = isDark,
                query = searchQuery,
                onQueryChange = { viewModel.onSearchQueryChanged(it) },
                onClear = { viewModel.onSearchQueryChanged("") },
                focusRequester = focusRequester
            )

            // 2. Health Tools (Horizontal)
            Text(
                "Health Tools",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = textColor,
                modifier = Modifier.padding(16.dp)
            )
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(healthTools) { tool ->
                    HealthToolItem(tool, isDark)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Featured Banner
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(140.dp),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Box {
                    AsyncImage(
                        model = "https://img.freepik.com/free-photo/healthy-lifestyle-concept-with-food_23-2147754630.jpg",
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.4f))
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Text("Summer Health Guide", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Stay hydrated and safe", color = Color.White, fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Trending Articles
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Trending Articles", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                Text("View All", color = brandColor, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }

            LazyRow(
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(articles) { article ->
                    HealthArticleCard(article, isDark)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 5. Shop by Concern (Grid)
            Text("Health Concerns", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor, modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(modifier = Modifier.height(10.dp))

            // Note: In a vertical scroll column, we can't use LazyVerticalGrid easily without fixed height.
            // So we use FlowRow or a manual loop. Here is a manual grid row loop for simplicity.
            val chunkedConcerns = concerns.chunked(3)
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                chunkedConcerns.forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        rowItems.forEach { concern ->
                            ConcernChip(concern, isDark)
                        }
                        // Fill gaps
                        repeat(3 - rowItems.size) { Spacer(modifier = Modifier.width(100.dp)) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// --- COMPONENTS ---

@Composable
fun HealthSearchBar(
    isDark: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester
) {
    val containerColor = if (isDark) Color(0xFF2C2C2C) else Color(0xFFE0F2F1)
    val textColor = if (isDark) Color.White else Color.Black
    val placeholderColor = if (isDark) Color.Gray else Color.DarkGray

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = TextStyle(color = textColor, fontSize = 16.sp),
        modifier = Modifier.focusRequester(focusRequester),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(containerColor)
                    .clickable {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = placeholderColor)
                Spacer(modifier = Modifier.width(8.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text("Search health topics...", color = placeholderColor)
                    }
                    innerTextField()
                }

                if (query.isNotEmpty()) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = placeholderColor,
                        modifier = Modifier.clickable {
                            onClear()
                            keyboardController?.hide()
                            focusManager.clearFocus()
                        }
                    )
                }
            }
        }
    )
}

// --- NEW COMPONENT FOR SEARCH RESULT ---
@Composable
fun SearchResultRow(title: String, imageUrl: String, isDark: Boolean) {
    val cardColor = if (isDark) Color(0xFF2C2C2C) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Card(
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth().height(60.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(4.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(title, fontWeight = FontWeight.Medium, color = textColor, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color.Gray)
        }
    }
}

@Composable
fun HealthToolItem(tool: HealthTool, isDark: Boolean) {
    val textColor = if (isDark) Color.White else Color.Black
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0F2F1)), // Light Teal
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = tool.iconUrl,
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(tool.name, fontSize = 12.sp, color = textColor, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun HealthArticleCard(article: HealthArticle, isDark: Boolean) {
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Card(
        modifier = Modifier.width(200.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            AsyncImage(
                model = article.imgUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.height(110.dp).fillMaxWidth()
            )
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = article.category.uppercase(),
                    fontSize = 10.sp,
                    color = Color(0xFF009688),
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = article.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = article.readTime,
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun ConcernChip(name: String, isDark: Boolean) {
    val cardColor = if (isDark) Color(0xFF2C2C2C) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Card(
        modifier = Modifier
            .width(100.dp)
            .height(40.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(name, fontSize = 12.sp, color = textColor, fontWeight = FontWeight.SemiBold)
        }
    }
}