package com.mysanjeevni.mysanjeevni.features.home.presentation.ui.sekeleton

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun HomeScreenSkeleton() {
    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color(0xFF121212) else Color.White
    val shimmerColor = if (isDark) Color(0xFF2A2A2A) else Color(0xFFE0E0E0)

    Scaffold(
        containerColor = backgroundColor,
        topBar = {
            // Skeleton Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
                    .background(Color(0xFF38D6C6))
                    .statusBarsPadding()
                    .padding(bottom = 6.dp)
            ) {
                // Top Row Skeleton
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        ShimmerBox(width = 100.dp, height = 12.dp, color = Color.White.copy(alpha = 0.3f))
                        Spacer(modifier = Modifier.height(4.dp))
                        ShimmerBox(width = 120.dp, height = 16.dp, color = Color.White.copy(alpha = 0.5f))
                    }

                    Row {
                        ShimmerBox(width = 80.dp, height = 28.dp, color = Color.White.copy(alpha = 0.3f), shape = RoundedCornerShape(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        ShimmerBox(width = 22.dp, height = 22.dp, color = Color.White.copy(alpha = 0.3f), shape = CircleShape)
                        Spacer(modifier = Modifier.width(12.dp))
                        ShimmerBox(width = 22.dp, height = 22.dp, color = Color.White.copy(alpha = 0.3f), shape = CircleShape)
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Search Bar Skeleton
                ShimmerBox(
                    width = 0.dp,
                    height = 44.dp,
                    color = Color.White.copy(alpha = 0.9f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
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
            // Category Tabs Skeleton
            CategoryTabsSkeleton()

            Spacer(modifier = Modifier.height(16.dp))

            // Banner Skeleton
            BannerSkeleton(shimmerColor)

            Spacer(modifier = Modifier.height(8.dp))

            // Prescription Card Skeleton
            PrescriptionCardSkeleton(shimmerColor)

            Spacer(modifier = Modifier.height(16.dp))

            // Action Grid Skeleton
            ActionGridSkeleton(shimmerColor)

            Spacer(modifier = Modifier.height(20.dp))

            // Zero Fee Banner Skeleton
            ShimmerBox(
                width = 0.dp,
                height = 50.dp,
                color = shimmerColor,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Deal of the Day Skeleton
            DealOfTheDaySkeleton(shimmerColor)

            Spacer(modifier = Modifier.height(10.dp))

            // Medicines Section Skeleton
            MedicinesSectionSkeleton(shimmerColor)

            Spacer(modifier = Modifier.height(12.dp))

            // Diabetes Section Skeleton
            DiabetesSectionSkeleton(shimmerColor)

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ShimmerBox(
    width: Dp,
    height: Dp,
    color: Color,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    modifier: Modifier = Modifier
) {
    var shimmerTranslate by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (true) {
            shimmerTranslate = if (shimmerTranslate == 0f) 1000f else 0f
            delay(1000)
        }
    }

    Box(
        modifier = modifier
            .then(
                if (width > 0.dp) Modifier.width(width) else Modifier.fillMaxWidth()
            )
            .height(height)
            .clip(shape)
            .background(color)
    )
}

@Composable
fun CategoryTabsSkeleton() {
    Spacer(modifier = Modifier.height(10.dp))

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(5) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ShimmerBox(
                    width = 60.dp,
                    height = 60.dp,
                    color = Color.LightGray.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(16.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                ShimmerBox(
                    width = 50.dp,
                    height = 12.dp,
                    color = Color.LightGray.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
fun BannerSkeleton(shimmerColor: Color) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ShimmerBox(
            width = 0.dp,
            height = 180.dp,
            color = shimmerColor,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Dots Indicator
        Row(
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(3) {
                ShimmerBox(
                    width = 8.dp,
                    height = 8.dp,
                    color = shimmerColor,
                    shape = RoundedCornerShape(50)
                )
                if (it < 2) Spacer(modifier = Modifier.width(6.dp))
            }
        }
    }
}

@Composable
fun PrescriptionCardSkeleton(shimmerColor: Color) {
    ShimmerBox(
        width = 0.dp,
        height = 70.dp,
        color = shimmerColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun ActionGridSkeleton(shimmerColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        repeat(2) {
            ShimmerBox(
                width = 0.dp,
                height = 70.dp,
                color = shimmerColor,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun DealOfTheDaySkeleton(shimmerColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2196F3))
            .padding(vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBox(
                width = 120.dp,
                height = 18.dp,
                color = Color.White.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            ShimmerBox(
                width = 100.dp,
                height = 24.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(4) {
                MedicineCardSkeleton(shimmerColor)
            }
        }
    }
}

@Composable
fun MedicineCardSkeleton(shimmerColor: Color) {
    Card(
        modifier = Modifier.width(160.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            ShimmerBox(
                width = 0.dp,
                height = 110.dp,
                color = shimmerColor,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            ShimmerBox(width = 100.dp, height = 14.dp, color = shimmerColor)
            Spacer(modifier = Modifier.height(4.dp))
            ShimmerBox(width = 80.dp, height = 10.dp, color = shimmerColor)

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                ShimmerBox(width = 50.dp, height = 14.dp, color = shimmerColor)
                Spacer(modifier = Modifier.width(4.dp))
                ShimmerBox(width = 40.dp, height = 10.dp, color = shimmerColor)
            }

            Spacer(modifier = Modifier.height(12.dp))

            ShimmerBox(
                width = 0.dp,
                height = 36.dp,
                color = shimmerColor,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun MedicinesSectionSkeleton(shimmerColor: Color) {
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
            ShimmerBox(
                width = 100.dp,
                height = 20.dp,
                color = Color.White.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.weight(1f))
            ShimmerBox(
                width = 60.dp,
                height = 14.dp,
                color = Color.White.copy(alpha = 0.3f)
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) {
                ShimmerBox(
                    width = 150.dp,
                    height = 200.dp,
                    color = shimmerColor,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}

@Composable
fun DiabetesSectionSkeleton(shimmerColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF90CAF9))
            .padding(vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ShimmerBox(
                width = 80.dp,
                height = 20.dp,
                color = Color.White.copy(alpha = 0.3f)
            )
            Spacer(modifier = Modifier.weight(1f))
            ShimmerBox(
                width = 60.dp,
                height = 14.dp,
                color = Color.White.copy(alpha = 0.3f)
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(3) {
                ShimmerBox(
                    width = 150.dp,
                    height = 200.dp,
                    color = shimmerColor,
                    shape = RoundedCornerShape(12.dp)
                )
            }
        }
    }
}