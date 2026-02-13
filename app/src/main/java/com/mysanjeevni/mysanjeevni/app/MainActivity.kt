package com.mysanjeevni.mysanjeevni.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.ForgetScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.LoginScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.OtpVerificationScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.ResetPasswordScreen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.SignupScreen
import com.mysanjeevni.mysanjeevni.features.home.presentation.ui.HomeScreen
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui.PharmacyScreen
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
                NavHost(
                    navController=navController,
                    startDestination = Screen.Login.route
                ){
                    composable(route=Screen.Login.route){
                        LoginScreen(navController=navController)
                    }
                    composable(route= Screen.Signup.route){
                        SignupScreen(navController=navController)
                    }
                    composable(route= Screen.Home.route){
                        HomeScreen(navController = navController)
                    }
                    composable(route= Screen.Forget.route){
                        ForgetScreen(navController = navController)
                    }
                    composable(route = Screen.VERIFY.route){
                        OtpVerificationScreen(navController = navController)
                    }
                    composable(route = Screen.ResetPassword.route){
                        ResetPasswordScreen(navController = navController)
                    }
                }
            }
        }
    }
}