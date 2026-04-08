package com.mysanjeevni.mysanjeevni.features.home.presentation.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.mysanjeevni.mysanjeevni.R

// Banner Data Model
data class HomeBannerData(
    val id:Int,
    val title: String,
    val subtitle: String,
    val offer: String,
    val price: String,
    val bgColor: Color,
    val icon: ImageVector,
    val iconColor: Color,
    val imageRes:Int,
    val btnText: String
)

// Banners List (3-4 different offers)
val promoBanners = listOf(
    // 1. Membership Banner (Existing)
    HomeBannerData(
        1,
        title = "MySanjeevni First",
        subtitle = "6-Month Membership",
        offer = "Worth ₹999",
        price = "For ₹49 Only!",
        bgColor = Color(0xFFFFF3E0), // Light Yellow
        icon = Icons.Default.WorkspacePremium,
        iconColor = Color(0xFFE91E63),
        R.drawable.app_banner,
        btnText = "Join Now"
    ),
    // 2. Lab Test Banner
    HomeBannerData(
        2,
        title = "Full Body Checkup",
        subtitle = "Includes 80+ Tests",
        offer = "Home Collection",
        price = "Starts @ ₹899",
        bgColor = Color(0xFFE3F2FD), // Light Blue
        icon = Icons.Default.Science,
        iconColor = Color(0xFF1976D2),
        R.drawable.app_banner1,
        btnText = "Book Now"
    ),
    // 3. Medicine Delivery
    HomeBannerData(
        3,
        title = "Express Delivery",
        subtitle = "Flat 25% OFF",
        offer = "On First Order",
        price = "Use Code: NEW25",
        bgColor = Color(0xFFE8F5E9), // Light Green
        icon = Icons.Default.LocalShipping,
        iconColor = Color(0xFF43A047),
        R.drawable.app_banner2,
        btnText = "Order Now"
    ),
    // 4. Doctor Consult
    HomeBannerData(
        4,
        title = "Expert Consultation",
        subtitle = "Call MD Specialists",
        offer = "Wait time < 5mins",
        price = "From ₹199",
        bgColor = Color(0xFFF3E5F5),
        icon = Icons.Default.VideoCall,
        iconColor = Color(0xFF7E57C2),
        R.drawable.app_banner3,
        btnText = "Call Now"
    ),
    HomeBannerData(
        5,
        title = "Vitamin Test",
        subtitle = "Analysis for Optimal health and vitality",
        offer = "Nothing",
        price = "From ₹ 199",
        bgColor = Color(0xFFF3E5F5),
        icon = Icons.Default.HealthAndSafety,
        imageRes = R.drawable.banner5,
        iconColor = Color(0xFF7E57C2),
        btnText = "Vitamin"
    ),
    HomeBannerData(
        id = 6,
        title = "Health Laboratory",
        subtitle = "Accurate lab test",
        offer = "Nothing",
        price = "From ₹ 199",
        bgColor = Color(0xFFF3E5F5),
        icon = Icons.Filled.Science,
        imageRes = R.drawable.banner6,
        iconColor = Color(0xFF7E57C2),
        btnText = "Health Laboratory"
    ),
    HomeBannerData(
        id = 7,
        title = "Surgical Essential",
        subtitle = "Trusted solution for fast efficiency",
        offer = "Nothing",
        price = "From ₹ 199",
        bgColor = Color(0xFFF3E5F5),
        icon = Icons.Filled.Science,
        imageRes = R.drawable.banner7,
        iconColor = Color(0xFF7E57C2),
        btnText = "Health Laboratory"
    ),
    HomeBannerData(
        id = 8,
        title = "Surgery",
        subtitle = "Life Saving effective",
        offer = "Nothing",
        price = "From ₹ 199",
        bgColor = Color(0xFFF3E5F5),
        icon = Icons.Filled.Science,
        imageRes = R.drawable.banner8,
        iconColor = Color(0xFF7E57C2),
        btnText = "Health Laboratory"
    )
)