package com.mysanjeevni.mysanjeevni.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.ForgetScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.LoginScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.OtpVerificationScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.ResetPasswordScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.SignupScreen
import com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.CartScreen
import com.mysanjeevni.mysanjeevni.features.category.presenttion.ui.CategoryScreen
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.ConsultScreen
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.MyConsultsScreen
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.ui.DoctorDashboardScreen
import com.mysanjeevni.mysanjeevni.features.health.presentation.ui.HealthScreen
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.HomeScreen
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.SplashScreen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.MyLabTestsScreen
import com.mysanjeevni.mysanjeevni.features.onboarding.ui.onBoarding.OnBoardingScreen
import com.mysanjeevni.mysanjeevni.features.orders.presntation.ui.OrderSummaryScreen
import com.mysanjeevni.mysanjeevni.features.orders.presntation.ui.OrdersScreen
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui.PharmacyScreen
import com.mysanjeevni.mysanjeevni.features.plan.presentation.ui.PlanScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.EditProfileScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.HealthRecordsScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.ManageAddresses
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.ProfileScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.TransactionHistoryScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.WalletScreen
import com.mysanjeevni.mysanjeevni.features.referral.presentation.ui.ReferralScreen
import com.mysanjeevni.mysanjeevni.features.settings.presentation.ui.SettingsScreen
import com.mysanjeevni.mysanjeevni.ui.theme.MySanjeevniTheme
import dagger.hilt.android.AndroidEntryPoint

val SelectedIconColor = Color(0xFF26A69A)
val UnselectedIconColor = Color.Gray
val PlanCircleBg = Color(0xFFE0F2F1) // Light Teal Background
val PlanTextParams = Color(0xFF00695C) // Darker Teal Text

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MySanjeevniTheme {
                val navController = rememberNavController()

                // 1. Check current route
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // 2. Define which screens have the bottom bar
                val showBottomBar = currentRoute in listOf(
                    Screen.Home.route,
                    Screen.CategoryScreen.route,
                    Screen.Consult.route,
                    Screen.ProfileScreen.route,
                    Screen.Health.route,
                    Screen.Plan.route
                )

                Scaffold(
                    // 3. Use the NEW STYLISH BOTTOM BAR here
                    bottomBar = {
                        if (showBottomBar) {
                            BottomBar(
                                currentRoute = currentRoute, onNavigate = { route ->
                                    navController.navigate(route) {
                                        // Standard navigation logic to avoid stack buildup
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                })
                        }
                    }
                ) { innerPadding ->
                    // 4. Navigation Host
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route,
                        modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                    ) {
                        composable(route = Screen.Splash.route) {
                            SplashScreen(navController = navController)
                        }
                        composable(route = Screen.OnBoarding.route) {
                            OnBoardingScreen(
                                onFinish = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.OnBoarding.route) { inclusive = true }
                                    }
                                })
                        }
                        composable(route = Screen.Login.route) {
                            LoginScreen(navController = navController)
                        }
                        composable(route = Screen.Signup.route) {
                            SignupScreen(navController = navController)
                        }
                        composable(route = Screen.Home.route) {
                            // Ensure HomeScreen DOES NOT have its own Scaffold/BottomBar
                            HomeScreen(navController = navController)
                        }
                        composable(route = Screen.Forget.route) {
                            ForgetScreen(navController = navController)
                        }
                        composable(route = Screen.VERIFY.route) {
                            OtpVerificationScreen(navController = navController)
                        }
                        composable(route = Screen.ResetPassword.route) {
                            ResetPasswordScreen(navController = navController)
                        }
                        composable(route = Screen.ProfileScreen.route) {
                            ProfileScreen(navController = navController)
                        }
                        composable(route = Screen.CartScreen.route) {
                            CartScreen(navController = navController)
                        }
                        composable(route = Screen.Consult.route) {
                            ConsultScreen(navController = navController)
                        }
                        composable(route = Screen.Health.route) {
                            HealthScreen(navController = navController)
                        }
                        composable(route = Screen.Plan.route) {
                            PlanScreen(navController = navController)
                        }
                        composable(route = Screen.PharmacyList.route) {
                            PharmacyScreen(navController = navController)
                        }
                        composable(route = Screen.MyOrders.route) {
                            OrdersScreen(navController = navController)
                        }
                        composable(
                            route = "${Screen.ManageAddresses.route}?checkout={checkout}",
                            arguments = listOf(
                                navArgument("checkout") {
                                    type = NavType.BoolType
                                    defaultValue = false
                                }
                            )
                        ) { backStackEntry ->

                            val isCheckout = backStackEntry.arguments?.getBoolean("checkout") ?: false

                            ManageAddresses(
                                navController = navController,
                                isCheckout = isCheckout
                            )
                        }
                        composable(route = Screen.EditProfile.route) {
                            EditProfileScreen(navController = navController)
                        }
                        composable(route = Screen.HealthRecords.route) {
                            HealthRecordsScreen(navController = navController)
                        }
                        composable(route = Screen.WalletScreen.route) {
                            WalletScreen(navController = navController)
                        }
                        composable(route = Screen.TransactionHistoryScreen.route) {
                            TransactionHistoryScreen(navController = navController)
                        }
                        composable(route = Screen.MyLabTestsScreen.route) {
                            MyLabTestsScreen(navController = navController)
                        }
                        composable(route = Screen.SettingScreen.route) {
                            SettingsScreen(navController = navController)
                        }
                        composable(route = Screen.MyConsultScreen.route) {
                            MyConsultsScreen(navController = navController)
                        }
                        composable(route = Screen.ReferralScreen.route) {
                            ReferralScreen(navController = navController)
                        }
                        composable(route = Screen.DoctorDashboard.route) {
                            DoctorDashboardScreen(navController = navController)
                        }
                        composable(route = Screen.CategoryScreen.route) {
                            CategoryScreen(navController = navController)
                        }
                        composable(route = Screen.SummaryScreen.route) {
                            OrderSummaryScreen(navController = navController)
                        }

                    }
                }
            }
        }
    }

    @Composable
    fun BottomBar(
        currentRoute: String?, onNavigate: (String) -> Unit
    ) {
        androidx.compose.material3.Surface(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                    spotColor = Color.LightGray
                )
                .navigationBarsPadding(),
            color = Color.White,
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
                    icon = if (currentRoute == Screen.Home.route) Icons.Filled.Home else Icons.Outlined.Home,
                    label = "Home",
                    isSelected = currentRoute == Screen.Home.route,
                    onClick = { onNavigate(Screen.Home.route) }
                )
                BottomNavItem(
                    icon = Icons.Default.Category,
                    label = "Category",
                    isSelected = currentRoute == Screen.CategoryScreen.route,
                    onClick = { onNavigate(Screen.CategoryScreen.route) }
                )


                CenterPlanButton(
                    isSelected = currentRoute == Screen.Plan.route, onClick = { onNavigate(Screen.Plan.route) })

                BottomNavItem(
                    icon = Icons.Outlined.MedicalServices,
                    label = "Health",
                    isSelected = currentRoute == Screen.Health.route,
                    onClick = { onNavigate(Screen.Health.route) })

                BottomNavItem(
                    icon = Icons.Outlined.Person,
                    label = "Profile",
                    isSelected = currentRoute == Screen.ProfileScreen.route,
                    onClick = { onNavigate(Screen.ProfileScreen.route) })
            }
        }
    }

    @Composable
    fun CenterPlanButton(
        isSelected: Boolean, onClick: () -> Unit
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = (-2).dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) SelectedIconColor else PlanCircleBg)
                    .clickable { onClick() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Plan",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) Color.White else PlanTextParams
                )
            }
        }
    }


    @Composable
    fun BottomNavItem(
        icon: ImageVector, label: String, isSelected: Boolean, onClick: () -> Unit
    ) {
        val contentColor by animateColorAsState(
            targetValue = if (isSelected) SelectedIconColor else UnselectedIconColor, label = "color"
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() }
                .padding(8.dp)) {
            Icon(
                imageVector = icon, contentDescription = label, tint = contentColor, modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                fontSize = 10.sp,
                color = contentColor,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}