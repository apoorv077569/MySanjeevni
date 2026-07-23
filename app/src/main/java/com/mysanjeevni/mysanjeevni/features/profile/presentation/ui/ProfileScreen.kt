package com.mysanjeevni.mysanjeevni.features.profile.presentation.ui

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Note
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel.ProfileViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun ProfileScreen(navController: NavController,viewModel: ProfileViewModel = hiltViewModel()
) {
    val isDark = LocalIsDarkTheme.current || isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryText = if (isDark) Color.LightGray else Color(0xFF26A69A)
    val state by viewModel.state.collectAsState()

    val currentBackStackEntry = navController.currentBackStackEntry
    val profileUpdatedFlow = remember(currentBackStackEntry) {
        currentBackStackEntry
            ?.savedStateHandle
            ?.getStateFlow(
                "profile_updated",
                false
            )
    }
    val profileUpdated by profileUpdatedFlow
        ?.collectAsState()
        ?: remember {
            mutableStateOf(false)
        }

    val user = state.user
    val context = LocalContext.current


    val initials = user?.fullName
        ?.split(" ")
        ?.take(2)
        ?.mapNotNull { it.firstOrNull()?.uppercase() }
        ?.joinToString("")?:"U"

    LaunchedEffect(profileUpdated) {
        if (profileUpdated) {
            Log.d("PROFILE_REFRESH", "Profile update result received")
            Log.d("PROFILE_REFRESH", "Refreshing profile from API")
            viewModel.getProfile()
            currentBackStackEntry?.savedStateHandle?.set("profile_updated", false)
            Log.d("PROFILE_REFRESH", "Profile update result consumed")
        }
    }


    Column(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxSize()
            .padding(bottom = 8.dp)
            .background(bgColor)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(cardColor)
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF26A69A)),
                    contentAlignment = Alignment.Center
                ) {
                    if (!user?.profileImage.isNullOrEmpty()) {
                        AsyncImage(
                            model = user.profileImage,
                            contentDescription = "Profile",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                    else {
                        Box(
                            modifier = Modifier.fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFF26A69A)),
                            contentAlignment = Alignment.Center
                        ){
                            AutoText(
                                initials,
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
// user info
                Column(modifier = Modifier.weight(1f)) {
                    AutoText(
                        text = user?.fullName ?: "Loading...",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    AutoText(
                        text = user?.phone ?: "",
                        fontSize = 14.sp,
                        color = textColor
                    )
                    AutoText(
                        text = user?.email ?: "",
                        fontSize = 14.sp,
                        color = textColor
                    )
                }
                AutoText(
                    text = "Edit",
                    color = Color(0xFF26A69A),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        navController.navigate(Screen.EditProfile.route)
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
//        menu options
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(12.dp)

        ) {
            ProfileStatCard(
                Icons.Outlined.ShoppingBag,
                "Orders",

                cardColor,
                textColor,
                Modifier.width(100.dp),
                onClick = {
                    navController.navigate(Screen.MyOrders.route)
                }
            )
            ProfileStatCard(


                Icons.Default.Science,
                "Lab Tests",
                cardColor,
                textColor,
                Modifier.width(100.dp),
                onClick = {
                    navController.navigate(Screen.BookingHistoryScreen.route)
                }
            )
            ProfileStatCard(
                Icons.Default.Favorite,
                "Wishlist",
                cardColor,
                textColor,
                Modifier.width(100.dp),
                onClick = {
                    navController.navigate(Screen.WishlistScreen.route)
                }
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(cardColor)
        ) {
//            ProfileMenuItem(
//                Icons.Default.MedicalServices, "My Health Records", textColor, secondaryText, onClick =
//                    { navController.navigate(Screen.HealthRecords.route) })
            HorizontalDivider(Modifier, DividerDefaults.Thickness, color = bgColor)
            ProfileMenuItem(Icons.Default.LocationOn, "Manage Addresses", textColor, secondaryText) {
                navController
                    .navigate("${Screen.ManageAddresses.route}?checkout=false&home=false")
            }
            HorizontalDivider(Modifier, DividerDefaults.Thickness, color = bgColor)
            ProfileMenuItem(
                Icons.AutoMirrored.Filled.Note, "My Prescription", textColor, secondaryText, onClick =
                    { navController.navigate(Screen.UploadPrescription.route) })
            HorizontalDivider(Modifier, DividerDefaults.Thickness, color = bgColor)
            ProfileMenuItem(Icons.Default.CardGiftcard, "Share With Friends", textColor, secondaryText, onClick = {
                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Hey! Check out this amazing app \nhttps://play.google.com/store/apps/details?id=${context.packageName}"
                    )
                }
                context.startActivity(Intent.createChooser(shareIntent, "Share With Friends"))
            })
        }
        Spacer(modifier = Modifier.height(16.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(cardColor)
        ) {
            ProfileMenuItem(Icons.AutoMirrored.Filled.Help, "Need Help?", textColor, secondaryText, onClick = {
                navController.navigate(Screen.TicketScreen.route)
            })
            HorizontalDivider(Modifier, DividerDefaults.Thickness, color = bgColor)
            ProfileMenuItem(Icons.Default.Settings, "Settings", textColor, secondaryText, onClick = {
                navController.navigate(Screen.SettingScreen.route)
            })
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.logout()
                navController.navigate(Screen.Login.route) {
                    popUpTo(0)
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            border = BorderStroke(1.dp, Color.Red),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(50.dp)

        ) {
            AutoText(stringResource(R.string.logout), color = Color.Red, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(30.dp))
    }
}

// Helper Component

@Composable
fun ProfileStatCard(
    icon: ImageVector,
    title: String,
    color: Color,
    textColor: Color,
    modifier: Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        colors = CardDefaults.cardColors(containerColor = color),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF26A69A))
            Spacer(modifier = Modifier.height(4.dp))
            AutoText(text = title, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = textColor)
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    textColor: Color,
    iconColor: Color,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        AutoText(text = title, fontSize = 16.sp, color = textColor, modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = iconColor
        )
    }
}