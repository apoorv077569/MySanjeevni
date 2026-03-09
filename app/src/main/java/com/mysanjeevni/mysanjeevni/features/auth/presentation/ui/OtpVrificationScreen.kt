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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtpVerificationScreen(navController: NavController) {
    var otp1 by rememberSaveable { mutableStateOf("") }
    var otp2 by rememberSaveable { mutableStateOf("") }
    var otp3 by rememberSaveable { mutableStateOf("") }
    var otp4 by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("example@email.com") }

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val isFormValid = otp1.isNotBlank() && otp2.isNotBlank() && otp3.isNotBlank() && otp4.isNotBlank()
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val isDark = isSystemInDarkTheme()
    val backgroundColor = if (isDark) Color(0xFF121212) else Color.White
    val textColor = if (isDark) Color.LightGray else Color.Black

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedContainerColor = Color(0xFFECECEC),
        unfocusedContainerColor = Color(0xFFECECEC),
        focusedTextColor = Color(0xFF212121),
        unfocusedTextColor = Color(0xFF212121),
        focusedBorderColor = Color.Transparent,
        unfocusedBorderColor = Color.Transparent
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
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
            text = stringResource(R.string.app_name), fontSize = 32.sp, fontWeight = FontWeight.Bold, color = textColor
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(R.string.otp_verification), fontSize = 18.sp, color = textColor
        )
        Text(email, color = textColor)
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextField(
                otp1,
                onValueChange = {
                    if (it.length <= 1) {
                        otp1 = it
                        if (it.isNotEmpty()) focusRequester2.requestFocus()
                    }
                },
                modifier = Modifier
                    .width(50.dp)
                    .focusRequester(focusRequester1),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFECECEC), // input_background
                    unfocusedContainerColor = Color(0xFFECECEC), // input_background
                    focusedTextColor = Color(0xFF212121), // primary_text
                    unfocusedTextColor = Color(0xFF212121), // primary_text
                    focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            TextField(
                otp2,
                onValueChange = {
                    if (it.length <= 1) {
                        otp2 = it
                        if (it.isNotEmpty()) focusRequester3.requestFocus()
                        if (it.isEmpty()) focusRequester1.requestFocus()
                    }
                },
                modifier = Modifier
                    .width(50.dp)
                    .focusRequester(focusRequester2),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFECECEC), // input_background
                    unfocusedContainerColor = Color(0xFFECECEC), // input_background
                    focusedTextColor = Color(0xFF212121), // primary_text
                    unfocusedTextColor = Color(0xFF212121), // primary_text
                    focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
            )
            Spacer(modifier = Modifier.padding(5.dp))
            TextField(
                otp3,
                onValueChange = {
                    if (it.length <= 1) {
                        otp3 = it
                        if (it.isNotEmpty()) focusRequester4.requestFocus()
                        if (it.isEmpty()) focusRequester2.requestFocus()
                    }
                },
                modifier = Modifier
                    .width(50.dp)
                    .focusRequester(focusRequester3),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFECECEC), // input_background
                    unfocusedContainerColor = Color(0xFFECECEC), // input_background
                    focusedTextColor = Color(0xFF212121), // primary_text
                    unfocusedTextColor = Color(0xFF212121), // primary_text
                    focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                ),
                singleLine = true,
                shape = RoundedCornerShape(10.dp),
            )
            Spacer(modifier = Modifier.padding(horizontal = 5.dp))
            TextField(
                otp4,
                onValueChange = {
                    if (it.length <= 1) {
                        otp4 = it
                        if (it.isNotEmpty()) focusManager.clearFocus()
                        if (it.isEmpty()) focusRequester3.requestFocus()
                    }
                },
                modifier = Modifier
                    .width(50.dp)
                    .focusRequester(focusRequester4),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Next
                ),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFECECEC), // input_background
                    unfocusedContainerColor = Color(0xFFECECEC), // input_background
                    focusedTextColor = Color(0xFF212121), // primary_text
                    unfocusedTextColor = Color(0xFF212121), // primary_text
                    focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(10.dp),
            )
        }
        Spacer(modifier = Modifier.padding(vertical = 10.dp))
        Button(
            onClick = {
                isLoading = true
                scope.launch {
                    delay(2000)
                    isLoading = false
                    navController.navigate(Screen.ResetPassword.route)
                }
            }, enabled = isFormValid && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White, modifier = Modifier.size(24.dp), strokeWidth = 2.5.dp
                )
            } else {
                Text(stringResource(R.string.verify_otp))
            }
        }

    }
}