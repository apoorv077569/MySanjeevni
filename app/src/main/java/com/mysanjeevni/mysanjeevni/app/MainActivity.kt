package com.mysanjeevni.mysanjeevni.app

import android.Manifest
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.firebase.messaging.FirebaseMessaging
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.*
import com.mysanjeevni.mysanjeevni.features.cart.presentation.ui.CartScreen
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.category.presenttion.ui.CategoryScreen
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.ConsultScreen
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.MyConsultsScreen
import com.mysanjeevni.mysanjeevni.features.doctor.presentation.ui.DoctorDashboardScreen
import com.mysanjeevni.mysanjeevni.features.health.presentation.ui.HealthScreen
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.HomeScreen
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.SplashScreen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.MyLabTestsScreen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.LabDetailRoute
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.ui.MedicineDetailScreen
import com.mysanjeevni.mysanjeevni.features.onboarding.ui.onBoarding.OnBoardingScreen
import com.mysanjeevni.mysanjeevni.features.orders.data.dto.OrderItemDto
import com.mysanjeevni.mysanjeevni.features.orders.presntation.ui.OrderSummaryScreen
import com.mysanjeevni.mysanjeevni.features.orders.presntation.ui.OrdersScreen
import com.mysanjeevni.mysanjeevni.features.orders.presntation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.payment.presentation.ui.OrderSuccessScreen
import com.mysanjeevni.mysanjeevni.features.payment.presentation.ui.PaymentScreen
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui.PharmacyScreen
import com.mysanjeevni.mysanjeevni.features.plan.presentation.ui.PlanScreen
import com.mysanjeevni.mysanjeevni.features.privacy.PrivacyScreen
import com.mysanjeevni.mysanjeevni.features.profile.presentation.ui.*
import com.mysanjeevni.mysanjeevni.features.referral.presentation.ui.ReferralScreen
import com.mysanjeevni.mysanjeevni.features.settings.presentation.state.AppTheme
import com.mysanjeevni.mysanjeevni.features.settings.presentation.ui.SettingsScreen
import com.mysanjeevni.mysanjeevni.features.settings.presentation.viewmodel.SettingsViewModel
import com.mysanjeevni.mysanjeevni.features.terms.ui.TermsScreen
import com.mysanjeevni.mysanjeevni.ui.theme.MySanjeevniTheme
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.util.Locale

val SelectedIconColor = Color(0xFF26A69A)
val UnselectedIconColor = Color.Gray
val PlanCircleBg = Color(0xFFE0F2F1)
val PlanTextParams = Color(0xFF00695C)
val LocalAppLanguage = staticCompositionLocalOf { "en" }

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {

    override fun onPaymentSuccess(
        razorpayPaymentId: String?,
        paymentData: PaymentData?
    ) {

        RazorpayCallbackHolder.onSuccess?.invoke(
            razorpayPaymentId ?: "",
            paymentData?.orderId ?: "",
            paymentData?.signature ?: ""
        )
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show()
    }

    override fun onPaymentError(
        errorCode: Int,
        errorDescription: String?,
        paymentData: PaymentData?
    ) {

        Log.e("RAZORPAY", errorDescription ?: "Payment Failed")
        Toast.makeText(this, errorDescription ?: "Payment Failed", Toast.LENGTH_SHORT).show()
        RazorpayCallbackHolder.onFailure?.invoke(
            errorDescription ?: "Payment Failed"
        )
    }

    companion object {

        object RazorpayCallbackHolder {

            var onSuccess: ((String, String, String) -> Unit)? = null

            var onFailure: ((String) -> Unit)? = null
        }
    }



    override fun attachBaseContext(newBase: Context) {
        val prefs = newBase.getSharedPreferences("settings_prefs", MODE_PRIVATE)
        val language = prefs.getString("language", "English") ?: "English"

        val localeMap = mapOf(
            "English" to "en",
            "हिंदी (Hindi)" to "hi",
            "বাংলা (Bengali)" to "bn",
            "తెలుగు (Telugu)" to "te",
            "मराठी (Marathi)" to "mr",
            "தமிழ் (Tamil)" to "ta",
            "ગુજરાતી (Gujarati)" to "gu",
            "ಕನ್ನಡ (Kannada)" to "kn",
            "മലയാളം (Malayalam)" to "ml",
            "ਪੰਜਾਬੀ (Punjabi)" to "pa",
            "ଓଡ଼ିଆ (Odia)" to "or",
            "অসমীয়া (Assamese)" to "as",
            "اردو (Urdu)" to "ur",
            "संस्कृत (Sanskrit)" to "sa",
            "Konkani" to "kok",
            "मणिपुरी (Manipuri)" to "mni",
            "नेपाली (Nepali)" to "ne",
            "सिंधी (Sindhi)" to "sd",
            "Bodo" to "brx",
            "Dogri" to "doi",
            "Kashmiri" to "ks",
            "Maithili" to "mai",
            "Santali" to "sat"
        )

        val localeCode = localeMap[language] ?: "en"
        val locale = Locale(localeCode)
        Locale.setDefault(locale)
        val config = Configuration(newBase.resources.configuration)
        config.setLocale(locale)
        super.attachBaseContext(newBase.createConfigurationContext(config))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("FCM_TOKEN", token)
        }
        if (Build.VERSION.SDK_INT >= 33) {
            requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1)
        }

        enableEdgeToEdge()

        setContent {

            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val state by settingsViewModel.state.collectAsState()

            val currentLocaleCode = settingsViewModel.getLocaleCode()

            val darkTheme = when (state.selectedTheme) {
                AppTheme.DARK -> true
                AppTheme.LIGHT -> false
                AppTheme.SYSTEM -> isSystemInDarkTheme()
            }
            CompositionLocalProvider(LocalAppLanguage provides currentLocaleCode) {
                MySanjeevniTheme(darkTheme = darkTheme) {
                    val navController = rememberNavController()
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentRoute = navBackStackEntry?.destination?.route

                    val showBottomBar = currentRoute in listOf(
                        Screen.Home.route,
                        Screen.CategoryScreen.route,
                        Screen.Consult.route,
                        Screen.ProfileScreen.route,
                        Screen.Health.route,
                        Screen.Plan.route
                    )

                    Scaffold(
                        bottomBar = {
                            if (showBottomBar) {
                                BottomBar(
                                    currentRoute = currentRoute,
                                    onNavigate = { route ->
                                        navController.navigate(route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    ) { innerPadding ->
                        val cartViewModel: CartViewModel = hiltViewModel(this@MainActivity)
                        val orderViewModel: OrderViewModel = hiltViewModel(this@MainActivity)
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Splash.route,
                            modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
                        ) {
                            composable(Screen.Splash.route) {
                                SplashScreen(navController = navController)
                            }
                            composable(Screen.OnBoarding.route) {
                                OnBoardingScreen(onFinish = {
                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.OnBoarding.route) { inclusive = true }
                                    }
                                })
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
                                HomeScreen(navController = navController, scrollTo = scrollTo, cartViewModel = cartViewModel)
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
                            composable(Screen.Health.route) {
                                HealthScreen(navController = navController)
                            }
                            composable(Screen.Plan.route) {
                                PlanScreen(navController = navController)
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
                                    navController = navController,
                                    address = decodedAddress,
                                    onOrderClick = { orderId ->
                                        navController.navigate(
                                            Screen.OrderDetailScreen.createRoute(orderId)
                                        )
                                    }
                                )
                            }
                            composable(
                                route = Screen.ManageAddresses.route,
                                arguments = listOf(
                                    navArgument("checkout") {
                                        type = NavType.BoolType
                                        defaultValue = false
                                    }
                                )
                            ) { backStackEntry ->

                                val isCheckout =
                                    backStackEntry.arguments?.getBoolean("checkout")
                                        ?: false

                                ManageAddresses(
                                    navController = navController,
                                    isCheckout = isCheckout,
                                    orderViewModel = orderViewModel,
                                    cartViewModel = cartViewModel
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

//                            composable(
//                                route = "order_detail/{id}",
//                                arguments = listOf(
//                                    navArgument("id") { type = NavType.StringType }
//                                )
//                            ) { backStackEntry ->
//                                val id = backStackEntry.arguments?.getString("id") ?: ""
//                                Log.d("MAIN_ACTIVITY", "🔑 orderId: $id")
//
//                                OrderDetailScreen(
//                                    navController = navController
//                                )
//                            }

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
                            composable(Screen.DoctorDashboard.route) {
                                DoctorDashboardScreen(navController = navController)
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
                            ) { backStackEntry ->
                                OrderSummaryScreen(
                                    navController = navController,
                                    orderViewModel = orderViewModel
                                )
                            }

//                            composable(
//                                route = "payment_screen/{address}",
//                                arguments = listOf(
//                                    navArgument("address") {
//                                        type = NavType.StringType
//                                        defaultValue = ""
//                                    }
//                                )
//                            ) { backStackEntry ->
//
//                                // ✅ Decode the address
//                                val encodedAddress = backStackEntry.arguments?.getString("address") ?: ""
//                                val decodedAddress = java.net.URLDecoder.decode(
//                                    encodedAddress,
//                                    StandardCharsets.UTF_8.toString()
//                                )
//
//                                // ✅ Logcat print
//                                Log.d("PAYMENT_SCREEN", "Encoded Address: $encodedAddress")
//                                Log.d("PAYMENT_SCREEN", "Decoded Address: $decodedAddress")
//
//                                PaymentScreen(
//                                    navController = navController,
//                                    address = decodedAddress  // ✅ Pass to PaymentScreen
//                                )
//                            }

                            composable(Screen.OrderSuccessScreen.route) {
                                OrderSuccessScreen(
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
                }
            }


        }
    }

    @Composable
    fun BottomBar(currentRoute: String?, onNavigate: (String) -> Unit) {
        Surface(
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
                    icon = if (currentRoute == Screen.Home.route)
                        Icons.Filled.Home else Icons.Outlined.Home,
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
                    isSelected = currentRoute == Screen.Plan.route,
                    onClick = { onNavigate(Screen.Plan.route) }
                )
                BottomNavItem(
                    icon = Icons.Outlined.MedicalServices,
                    label = "Health",
                    isSelected = currentRoute == Screen.Health.route,
                    onClick = { onNavigate(Screen.Health.route) }
                )
                BottomNavItem(
                    icon = Icons.Outlined.Person,
                    label = "Profile",
                    isSelected = currentRoute == Screen.ProfileScreen.route,
                    onClick = { onNavigate(Screen.ProfileScreen.route) }
                )
            }
        }
    }

    @Composable
    fun CenterPlanButton(isSelected: Boolean, onClick: () -> Unit) {
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
        icon: ImageVector,
        label: String,
        isSelected: Boolean,
        onClick: () -> Unit
    ) {
        val contentColor by animateColorAsState(
            targetValue = if (isSelected) SelectedIconColor else UnselectedIconColor,
            label = "color"
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { onClick() }
                .padding(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(24.dp)
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