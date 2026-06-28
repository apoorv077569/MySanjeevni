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
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.messaging.FirebaseMessaging
import com.mysanjeevni.mysanjeevni.app.components.AppBottomBar
import com.mysanjeevni.mysanjeevni.app.navigation.NavGraph
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.BookLabTestViewModel
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.settings.presentation.state.AppTheme
import com.mysanjeevni.mysanjeevni.features.settings.presentation.viewmodel.SettingsViewModel
import com.mysanjeevni.mysanjeevni.ui.theme.MySanjeevniTheme
import com.mysanjeevni.mysanjeevni.utils.NetworkMonitor
import com.mysanjeevni.mysanjeevni.utils.NoInternetScreen
import com.mysanjeevni.mysanjeevni.utils.dilaog.PaymentErrorHolder
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale


val LocalAppLanguage = staticCompositionLocalOf { "en" }

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {

    override fun onPaymentSuccess(
        razorpayPaymentId: String?,
        paymentData: PaymentData?
    ) {

        Log.d("RZP_CHECK", "onPaymentSuccess Called")
        Log.d("RZP_CHECK", "PaymentId = $razorpayPaymentId")
        Log.d("RZP_CHECK", "OrderId = ${paymentData?.orderId}")
        Log.d("RZP_CHECK", "Signature = ${paymentData?.signature}")

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
        Log.d("PAYMENT_TRACE", "1. MainActivity onPaymentError")

        PaymentErrorHolder.errorMessage =
            when (errorCode) {

                Checkout.NETWORK_ERROR ->
                    "Unable to connect to the payment server. Please check your internet connection and try again."

                Checkout.INVALID_OPTIONS ->
                    "Payment configuration issue detected. Please try again later."

                Checkout.PAYMENT_CANCELED ->
                    "You cancelled the payment process."

                else ->
                    errorDescription
                        ?: "We couldn't complete your payment. Please try again."
            }
        Log.d(
            "PAYMENT_TRACE",
            "Callback Null = ${RazorpayCallbackHolder.onFailure == null}"
        )
        PaymentErrorHolder.errorMessage?.let {
            RazorpayCallbackHolder.onFailure?.invoke(
                it
            )
        }
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
        val locale = Locale.forLanguageTag(localeCode)
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


                    val context = LocalContext.current
                    val networkMonitor = remember {
                        NetworkMonitor(context)
                    }
                    val isConnected by networkMonitor.isConnected.collectAsState(initial = true)
                    if (!isConnected) {
                        NoInternetScreen(onRetry = {})
                    } else {
                        val navController = rememberNavController()
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        val cartViewModel: CartViewModel = hiltViewModel(this@MainActivity)
                        val orderViewModel: OrderViewModel = hiltViewModel(this@MainActivity)
                        val labViewModel: BookLabTestViewModel = hiltViewModel(this@MainActivity)

                        Scaffold(
                            bottomBar = {
                                AppBottomBar(navController,currentRoute)
                            }
                        ) { innerPadding ->
                            NavGraph(
                                navController = navController,
                                paddingValues = innerPadding,
                                cartViewModel = cartViewModel,
                                orderViewModel = orderViewModel,
                                settingsViewModel = settingsViewModel,
                                labViewModel = labViewModel
                            )
                        }
                    }
                }

            }
        }
    }

}