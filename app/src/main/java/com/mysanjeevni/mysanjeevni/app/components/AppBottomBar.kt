package com.mysanjeevni.mysanjeevni.app.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Science
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen

@Composable
fun AppBottomBar(
    navController: NavHostController, currentRoute: String?
) {
    val showBottomBar = currentRoute in listOf(
        Screen.Home.route,
        Screen.MyLabTestsScreen.route,
        Screen.ProfileScreen.route,
        Screen.Health.route,
        Screen.Plan.route
    )

    if (!showBottomBar) return

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                10.dp,
                RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            )
            .navigationBarsPadding(),
        color = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomNavItem(
                icon = if (currentRoute == Screen.Home.route)
                    Icons.Filled.Home
                else
                    Icons.Outlined.Home,
                label = "Home",
                isSelected = currentRoute == Screen.Home.route,
                onClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            BottomNavItem(
                icon = Icons.Outlined.ShoppingCart,
                label = "Cart",
                isSelected = currentRoute == Screen.CartScreen.route,
                onClick = {
                    navController.navigate(Screen.CartScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            BottomNavItem(
                icon = Icons.Outlined.Science,
                label = "Lab Test",
                isSelected = currentRoute == Screen.MyLabTestsScreen.route,
                onClick = {
                    navController.navigate(Screen.MyLabTestsScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            BottomNavItem(
                icon = Icons.Outlined.Person,
                label = "Profile",
                isSelected = currentRoute == Screen.ProfileScreen.route,
                onClick = {
                    navController.navigate(Screen.ProfileScreen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

        }
    }
}

@Composable
fun BottomNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme
    val selectedColor = colorScheme.primary
    val unselectedColor = colorScheme.onSurfaceVariant

    val contentColor by animateColorAsState(
        targetValue = if (isSelected)
            selectedColor
        else
            unselectedColor,
        label = "color"
    )

    val bgColor by animateColorAsState(
        targetValue = if (isSelected)
            selectedColor.copy(alpha = 0.15f)
        else
            Color.Transparent,
        label = "bg"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable(
                interactionSource = remember {
                    MutableInteractionSource()
                },
                indication = null
            ) {
                onClick()
            }
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(bgColor)
                .padding(horizontal = 16.dp, vertical = 6.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = label,
            fontSize = 10.sp,
            color = contentColor,
            fontWeight = if (isSelected)
                FontWeight.Bold
            else
                FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(CircleShape)
                .background(
                    if (isSelected)
                        contentColor
                    else
                        Color.Transparent
                )
        )
    }

}