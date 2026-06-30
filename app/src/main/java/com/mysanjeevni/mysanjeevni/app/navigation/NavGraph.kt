package com.mysanjeevni.mysanjeevni.app.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.ForgetScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.LoginScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.OtpVerificationScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.ResetPasswordScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.SignupScreen
import com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.CartScreen
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.category.presentation.ui.CategoryScreen
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.ConsultScreen
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.MyConsultsScreen
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.HomeScreen
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.SplashScreen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.BookTestScreen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.BookingDetailScreen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.MyLabTestsScreen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.BookingHistoryScreen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.LabDetailRoute
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.BookLabTestViewModel
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.ui.MedicineDetailScreen
import com.mysanjeevni.mysanjeevni.features.notification.presentation.ui.NotificationScreen
import com.mysanjeevni.mysanjeevni.features.onboarding.ui.onBoarding.OnBoardingScreen
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.OrderDetailScreen
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.OrderSummaryScreen
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.OrdersScreen
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.payment.presentation.ui.OrderSuccessScreen
import com.mysanjeevni.mysanjeevni.features.payment.presentation.ui.PaymentScreen
import com.mysanjeevni.mysanjeevni.features.payment.presentation.ui.result.PaymentFailedScreen
import com.mysanjeevni.mysanjeevni.features.payment.presentation.ui.result.PaymentSuccessScreen
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui.PharmacyScreen
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.screen.UploadPrescriptionScreen
import com.mysanjeevni.mysanjeevni.features.privacy.presentation.ui.PrivacyScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.EditProfileScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.HealthRecordsScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.ManageAddresses
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.ProfileScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.TransactionHistoryScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.WalletScreen
import com.mysanjeevni.mysanjeevni.features.referral.presentation.ui.ReferralScreen
import com.mysanjeevni.mysanjeevni.features.settings.presentation.ui.SettingsScreen
import com.mysanjeevni.mysanjeevni.features.settings.presentation.viewmodel.SettingsViewModel
import com.mysanjeevni.mysanjeevni.features.support.ticket.presentation.ui.SupportCenterScreen
import com.mysanjeevni.mysanjeevni.features.terms.ui.TermsScreen
import com.mysanjeevni.mysanjeevni.features.wishlist.presentation.ui.WishlistScreen
import com.mysanjeevni.mysanjeevni.features.wishlist.presentation.viewmodel.WishlistViewModel
import com.mysanjeevni.mysanjeevni.utils.dilaog.PaymentErrorHolder
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun NavGraph(navController: NavHostController,
             cartViewModel: CartViewModel,
             paddingValues: PaddingValues,
             orderViewModel: OrderViewModel,
             settingsViewModel: SettingsViewModel,
             labViewModel: BookLabTestViewModel,
             startDestination: String = Screen.Splash.route) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = Modifier.padding(paddingValues)
    ) {

        composable(Screen.Splash.route) {
            SplashScreen(navController = navController)
        }

        composable(
            route = Screen.OrderDetailScreen.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->

            val orderId =
                backStackEntry.arguments?.getString("id") ?: ""

            OrderDetailScreen(
                orderId = orderId,
                navController = navController
            )
        }

        composable(Screen.WishlistScreen.route) {
            val viewModel: WishlistViewModel = hiltViewModel()
            val state by viewModel.state.collectAsState()

            LaunchedEffect(Unit) {
                viewModel.loadWishlist()
            }

            WishlistScreen(
                items = state.items,
                onRemove = { productId ->
                    viewModel.removeFromWishlist(productId)
                },
                onMoveToCart = { item ->
                    viewModel.moveToCart(item)
                },
                onViewDetails = { item ->
                    navController.navigate(Screen.MedicineDetail.createRoute(item.productId))
                },
                onBack = {
                    navController.popBackStack()
                },
                onContinueShopping = {
                    navController.navigate(Screen.Home.route)
                }
            )
        }

        composable(Screen.UploadPrescription.route) {
            UploadPrescriptionScreen(navController = navController)
        }
        composable(Screen.BookingHistoryScreen.route) {
            BookingHistoryScreen(navController)
        }
        composable(Screen.OnBoarding.route) {
            OnBoardingScreen(onFinish = {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.OnBoarding.route) { inclusive = true }
                }
            })
        }
        composable(
            route = Screen.LabBookingDetail.route
        ) {
            BookingDetailScreen(
                navController = navController
            )
        }
        composable(
            route = "book_lab_test/{testId}/{testName}/{testPrice}"
        ) { backStackEntry ->

            val testId =
                backStackEntry.arguments?.getString("testId") ?: ""

            val testName =
                backStackEntry.arguments?.getString("testName") ?: ""

            val testPrice =
                backStackEntry.arguments?.getString("testPrice")
                    ?.toDoubleOrNull() ?: 0.0

            BookTestScreen(
                navController = navController,
                testId = testId,
                testName = testName,
                testPrice = testPrice,
                labViewModel

            )
        }

        composable(Screen.NotificationScreen.route) {
            NotificationScreen(navController)
        }

        composable(Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Screen.Signup.route) {
            SignupScreen(navController = navController)
        }
        composable(
            route = Screen.Home.route,
            arguments = listOf(
                navArgument("scrollTo") {
                    nullable = true
                    defaultValue = null
                }
            )
        ) { backStackEntry ->
            val scrollTo = backStackEntry.arguments?.getString("scrollTo")
            HomeScreen(
                navController = navController,
                scrollTo = scrollTo,
                cartViewModel = cartViewModel
            )
        }
        composable(Screen.Forget.route) {
            ForgetScreen(navController = navController)
        }
        composable(Screen.TermsScreen.route) {
            TermsScreen(navController)
        }
        composable(Screen.PrivacyScreen.route) {
            PrivacyScreen(navController)
        }
        composable(Screen.VERIFY.route) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            OtpVerificationScreen(navController, phone)
        }
        composable(Screen.ResetPassword.route) { backStackEntry ->
            val phone = backStackEntry.arguments?.getString("phone") ?: ""
            ResetPasswordScreen(navController = navController, phone)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }
        composable(
            route = "medicine_detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            MedicineDetailScreen(
                navController, id,
                orderViewModel = orderViewModel
            )
        }
        composable(Screen.CartScreen.route) {
            CartScreen(navController = navController, cartViewModel)
        }
        composable(
            route = Screen.LabTestDetail.route,
            arguments = listOf(
                navArgument("id") {
                    type = NavType.StringType
                }
            )
        ) {
            LabDetailRoute(
                navController = navController
            )
        }
        composable(Screen.Consult.route) {
            ConsultScreen(navController = navController)
        }

        composable(Screen.PharmacyList.route) {
            PharmacyScreen(navController = navController)
        }
        composable(
            route = "my_orders?address={address}",
            arguments = listOf(
                navArgument("address") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val encodedAddress = backStackEntry.arguments?.getString("address") ?: ""
            val decodedAddress = URLDecoder.decode(
                encodedAddress,
                StandardCharsets.UTF_8.toString()
            )

            OrdersScreen(
                navController = navController
            )
        }
        composable(
            route = "${Screen.ManageAddresses.route}?checkout={checkout}&home={home}",
            arguments = listOf(
                navArgument("checkout") {
                    type = NavType.BoolType
                    defaultValue = false
                },
                navArgument("home") {
                    type = NavType.BoolType
                    defaultValue = false
                }
            )
        ) { backStackEntry ->

            val isCheckout =
                backStackEntry.arguments?.getBoolean("checkout")
                    ?: false

            val isHome = backStackEntry.arguments?.getBoolean("home")
                ?: false

            ManageAddresses(
                navController = navController,
                isCheckout = isCheckout,
                isHome = isHome,
                orderViewModel = orderViewModel,
                cartViewModel = cartViewModel
            )
        }

        composable(
            Screen.PaymentSuccess.route
        ) { backStackEntry ->

            PaymentSuccessScreen(
                flow =
                    backStackEntry.arguments?.getString("flow") ?: "medicine",
                paymentId =
                    backStackEntry.arguments?.getString("paymentId") ?: "",
                razorpayOrderId =
                    backStackEntry.arguments?.getString("razorpayOrderId") ?: "",
                signature =
                    backStackEntry.arguments?.getString("signature") ?: "",
                navController = navController,
                orderViewModel = orderViewModel,
                labViewModel
            )
        }

        composable(Screen.PaymentFailed.route) {

            PaymentFailedScreen(
                reason = PaymentErrorHolder.errorMessage.toString(),
                onHome = {

                    navController.navigate(Screen.Home.route) {
                        popUpTo(0)
                    }

                }
            )
        }

        composable(
            route = Screen.PaymentScreen.route,
            arguments = listOf(
                navArgument("address") {
                    type = NavType.StringType
                    defaultValue = ""
                }
            )
        ) { backStackEntry ->
            val encodedAddress = backStackEntry.arguments?.getString("address") ?: ""
            val decodedAddress = URLDecoder.decode(
                encodedAddress,
                StandardCharsets.UTF_8.toString()
            )

            val savedStateHandle = navController.previousBackStackEntry?.savedStateHandle
            val items = savedStateHandle?.get<List<OrderItemDto>>("items") ?: emptyList()
            val totalPrice = savedStateHandle?.get<Double>("totalPrice") ?: 0.0

            PaymentScreen(
                navController = navController,
                items = items,
                totalPrice = totalPrice,
                deliveryAddress = decodedAddress,
                orderViewModel = orderViewModel,
                onPaymentSuccess = {
                    navController.navigate(Screen.OrderSuccessScreen.route) {
                        popUpTo(Screen.PaymentScreen.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(Screen.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }
        composable(Screen.HealthRecords.route) {
            HealthRecordsScreen(navController = navController)
        }
        composable(Screen.WalletScreen.route) {
            WalletScreen(navController = navController)
        }
        composable(Screen.TransactionHistoryScreen.route) {
            TransactionHistoryScreen(navController = navController)
        }
        composable(Screen.MyLabTestsScreen.route) {
            MyLabTestsScreen(navController = navController)
        }


        composable(Screen.SettingScreen.route) {
            SettingsScreen(
                navController = navController,
                viewModel = settingsViewModel
            )
        }

        composable(Screen.MyConsultScreen.route) {
            MyConsultsScreen(navController = navController)
        }
        composable(Screen.ReferralScreen.route) {
            ReferralScreen(navController = navController)
        }
        composable(Screen.TicketScreen.route) {
            SupportCenterScreen(navController)
        }
        composable(Screen.CategoryScreen.route) {
            CategoryScreen(navController = navController)
        }
        composable(
            route = Screen.SummaryScreen.route,
            arguments = listOf(
                navArgument("address") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = ""
                }
            )
        ) { _ ->
            OrderSummaryScreen(
                navController = navController,
                orderViewModel = orderViewModel
            )
        }

        composable(Screen.OrderSuccessScreen.route) { _ ->
            OrderSuccessScreen(
                orderViewModel = orderViewModel,
                onGoToOrders = {
                    navController.navigate(Screen.MyOrders.plain) {
                        popUpTo(Screen.OrderSuccessScreen.route) { inclusive = true }
                    }
                },
                onGoToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.OrderSuccessScreen.route) { inclusive = true }
                    }
                },
                navController = navController
            )
        }
    }
}