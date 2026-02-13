package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.navigation

sealed class Screen(val route:String) {
    object Login: Screen("login")
    object Signup: Screen("signup")
    object Home: Screen("home")
    object Forget :Screen("forget")
    object VERIFY :Screen("verify")
    object ResetPassword :Screen("resetPassword")
}