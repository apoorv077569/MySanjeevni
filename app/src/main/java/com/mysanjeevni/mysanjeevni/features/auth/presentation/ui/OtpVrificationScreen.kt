package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.navigation.Screen

@Composable
fun OtpVerificationScreen(navController: NavController) {
    var otp1 by rememberSaveable { mutableStateOf("") }
    var otp2 by rememberSaveable { mutableStateOf("") }
    var otp3 by rememberSaveable { mutableStateOf("") }
    var otp4 by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("example@email.com") }

    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color(0xFF121212) else Color.White
    val textColor = if (isDark) Color.LightGray else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor) // <--- Apply Background
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = "App logo",
            modifier = Modifier.size(100.dp)
        )
        Text(
            text = stringResource(R.string.app_name),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.otp_verification),
            fontSize = 18.sp,
            color = textColor
        )
        Text(email, color = textColor)
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextField(
                otp1,
                onValueChange = { otp1 = it },
                modifier = Modifier.width(50.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFECECEC), // input_background
                    unfocusedContainerColor = Color(0xFFECECEC), // input_background
                    focusedTextColor = Color(0xFF212121), // primary_text
                    unfocusedTextColor = Color(0xFF212121), // primary_text
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
            )
            Spacer(modifier =Modifier.padding(horizontal = 5.dp))
            TextField(
                otp2, onValueChange = { otp2 = it },
                modifier = Modifier.width(50.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFECECEC), // input_background
                    unfocusedContainerColor = Color(0xFFECECEC), // input_background
                    focusedTextColor = Color(0xFF212121), // primary_text
                    unfocusedTextColor = Color(0xFF212121), // primary_text
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
            )
            Spacer(modifier = Modifier.padding(5.dp))
            TextField(
                otp3,
                onValueChange = { otp3 = it },
                modifier = Modifier.width(50.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFECECEC), // input_background
                    unfocusedContainerColor = Color(0xFFECECEC), // input_background
                    focusedTextColor = Color(0xFF212121), // primary_text
                    unfocusedTextColor = Color(0xFF212121), // primary_text
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            TextField(
                otp4,
                onValueChange = { otp4 = it },
                modifier = Modifier.width(50.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFECECEC), // input_background
                    unfocusedContainerColor = Color(0xFFECECEC), // input_background
                    focusedTextColor = Color(0xFF212121), // primary_text
                    unfocusedTextColor = Color(0xFF212121), // primary_text
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Button(onClick = {
            navController.navigate(Screen.ResetPassword.route)
        }) {
            Text(stringResource(R.string.verify_otp))
        }

    }
}