package com.mysanjeevni.mysanjeevni.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.navigation.Screen

@Composable
fun AppBottomBar(navController: NavController) {
    val isDark = isSystemInDarkTheme()
    val containerColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val contentColor = if (isDark) Color.LightGray else Color.Black
    val selectedColor = Color(0xFFFF6F61)

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = containerColor,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == Screen.Home.route,
            onClick = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                }
            },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text(stringResource(R.string.home)) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                indicatorColor = Color.Transparent,
                unselectedIconColor = contentColor,
                unselectedTextColor = contentColor
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.Consult.route,
            onClick = {
                navController.navigate(Screen.Consult.route) {
                    popUpTo(Screen.Home.route)
                }
            },
            icon = { Icon(Icons.Default.Call, contentDescription = null) },
            label = { Text(stringResource(R.string.consult), fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                indicatorColor = Color.Transparent,
                unselectedIconColor = contentColor,
                unselectedTextColor = contentColor
            )
        )
        Box(
            modifier = Modifier.weight(1f),
            Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate(Screen.Plan.route) {
                                popUpTo(Screen.Home.route)
                            }
                        }
                        .background(Color(0xFFFFE0B2)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(stringResource(R.string.plan), color = Color(0xFFE65100), fontSize = 10.sp)
                }
            }
        }
        NavigationBarItem(
            selected = currentRoute == Screen.Health.route,
            onClick = {
                navController.navigate(Screen.Health.route) {
                    popUpTo(Screen.Home.route)
                }
            },
            icon = { Icon(Icons.Default.MedicalServices, contentDescription = null) },
            label = { Text(stringResource(R.string.health), fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                indicatorColor = Color.Transparent,
                unselectedIconColor = contentColor,
                unselectedTextColor = contentColor
            )
        )
        NavigationBarItem(
            selected = currentRoute == Screen.ProfileScreen.route,
            onClick = {
                navController.navigate(Screen.ProfileScreen.route) {
                    popUpTo(Screen.Home.route)
                }
            },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text(stringResource(R.string.profile), fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = selectedColor,
                selectedTextColor = selectedColor,
                indicatorColor = Color.Transparent,
                unselectedIconColor = contentColor,
                unselectedTextColor = contentColor
            )
        )
    }
}