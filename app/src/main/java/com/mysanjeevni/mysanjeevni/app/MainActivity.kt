package com.mysanjeevni.mysanjeevni.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mysanjeevni.mysanjeevni.core.presentation.AppBottomBar // <--- Import this
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.*
import com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.CartScreen
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.ConsultScreen
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.MyConsultsScreen
import com.mysanjeevni.mysanjeevni.features.health.presentation.ui.HealthScreen
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.HomeScreen
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.SplashScreen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.MyLabTestsScreen
import com.mysanjeevni.mysanjeevni.features.orders.presntation.ui.OrdersScreen
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.navigation.Screen
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MySanjeevniTheme {
                val navController = rememberNavController()

                // 1. Check current route to decide visibility
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                // 2. Define which screens have the bottom bar
                val showBottomBar = currentRoute in listOf(
                    Screen.Home.route,
                    Screen.Consult.route,
                    Screen.ProfileScreen.route,
                    Screen.Health.route,
                    Screen.Plan.route
                )

                Scaffold(
                    // 3. Show Bottom Bar conditionally
                    bottomBar = {
                        if (showBottomBar) {
                            AppBottomBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    // 4. Pass innerPadding to NavHost so content isn't hidden behind bar
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Splash.route,
                        modifier = Modifier.padding(innerPadding) // <--- CRITICAL
                    ) {
                        composable(route = Screen.Splash.route) {
                            SplashScreen(navController = navController)
                        }
                        composable(route = Screen.Login.route) {
                            LoginScreen(navController = navController)
                        }
                        composable(route = Screen.Signup.route) {
                            SignupScreen(navController = navController)
                        }
                        composable(route = Screen.Home.route) {
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
                        composable(route = Screen.ManageAddresses.route) {
                            ManageAddresses(navController = navController)
                        }
                        composable(route = Screen.EditProfile.route) {
                            EditProfileScreen(navController = navController)
                        }
                        composable(route = Screen.HealthRecords.route) {
                            HealthRecordsScreen( navController = navController)
                        }
                        composable(route = Screen.WalletScreen.route) {
                            WalletScreen( navController = navController)
                        }
                        composable(route = Screen.TransactionHistoryScreen.route) {
                            TransactionHistoryScreen( navController = navController)
                        }
                        composable(route = Screen.MyLabTestsScreen.route) {
                            MyLabTestsScreen( navController = navController)
                        }
                        composable(route = Screen.SettingScreen.route) {
                            SettingsScreen( navController = navController)
                        }
                        composable(route = Screen.MyConsultScreen.route) {
                            MyConsultsScreen( navController = navController)
                        }
                        composable(route = Screen.ReferralScreen.route) {
                            ReferralScreen( navController = navController)
                        }
                    }
                }
            }
        }
    }
}