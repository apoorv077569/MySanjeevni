package com.mysanjeevni.mysanjeevni.core.navigation

import android.net.Uri

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object OnBoarding : Screen("onBoarding")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home?scrollTo={scrollTo}") {
        const val plain = "home?scrollTo="
        fun withScrollTo(section: String) = "home?scrollTo=$section"
    }

    object Forget : Screen("forget")
    object VERIFY : Screen("verify/{mobile}") {
        fun createRoute(mobile: String) = "verify/$mobile"
    }

    object ResetPassword : Screen("resetPassword/{mobile}") {
        fun createRoute(mobile: String) = "resetPassword/$mobile"
    }

    object MedicineDetail : Screen("medicine_detail/{id}") {
        fun createRoute(id: String) =
            "medicine_detail/$id"
    }

    object LabTestDetail : Screen("lab_test_detail/{id}") {

        fun createRoute(id: String): String {
            return "lab_test_detail/$id"
        }
    }

    object ProfileScreen : Screen("profile_screen")
    object CartScreen : Screen("cart_screen")
    object Consult : Screen("consult")
    object Health : Screen("health")
    object Plan : Screen("plan")
    object PharmacyList : Screen("pharmacy_list")
    object MyOrders : Screen("my_orders?address={address}") {
        const val plain = "my_orders"
        fun createRoute(address: String): String {
            return "my_orders?address=${Uri.encode(address)}"
        }
    }

    object ManageAddresses :
        Screen("manage_addresses?checkout={checkout}") {

        fun createRoute(
            checkout: Boolean
        ) = "manage_addresses?checkout=$checkout"
    }

    object EditProfile : Screen("edit_profile")
    object CategoryScreen : Screen("category_screen")
    object HealthRecords : Screen("health_records")
    object MyLabTestsScreen : Screen("my_lab_tests_screen")
    object SettingScreen : Screen("setting_screen")
    object MyConsultScreen : Screen("my_consult_screen")
    object WalletScreen : Screen("wallet_screen")
    object ReferralScreen : Screen("wallet_screen")
    object TransactionHistoryScreen : Screen("transaction_history_screen")
    object SummaryScreen : Screen("summary_screen?address={address}") {
        fun createRoute(address: String): String {
            return "summary_screen?address=${Uri.encode(address)}"
        }
    }

    object TicketScreen : Screen("ticket_screen")

    object PaymentScreen : Screen("payment_screen/{address}") {
        fun createRoute(address: String) = "payment_screen/$address"
    }

    object OrderSuccessScreen : Screen("OrderSuccess_Screen?address={address}") {
        const val plain = "OrderSuccess_Screen"
        fun createRoute(address: String): String {
            return "OrderSuccess_Screen?address=${Uri.encode(address)}"
        }
    }

    object TermsScreen : Screen("terms")
    object Payment : Screen("payment")
    object PrivacyScreen : Screen("privacy")

    object OrderDetailScreen : Screen("order_detail/{id}") {
        fun createRoute(id: String): String {
            return "order_detail/$id"
        }
    }

    object UploadPrescription : Screen("upload_prescription")
    object WishlistScreen : Screen("wishlist_screen")
    object BookLabTestScreen {
        const val route =
            "book_lab_test/{testId}/{testName}/{testPrice}"
    }

    object NotificationScreen : Screen("notification_screen")
    object BookingHistoryScreen : Screen("booking_history")
    object LabBookingDetail : Screen("lab_booking_detail")
    object PaymentSuccess : Screen(
        "payment_success/{paymentId}/{razorpayOrderId}/{signature}"
    ) {
        fun createRoute(
            paymentId: String,
            razorpayOrderId: String,
            signature: String
        ) =
            "payment_success/$paymentId/$razorpayOrderId/$signature"
    }

    object PaymentFailed : Screen("payment_failed")
}