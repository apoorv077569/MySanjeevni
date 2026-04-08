package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Brush
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
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun OtpVerificationScreen(navController: NavController) {

    var otp1 by rememberSaveable { mutableStateOf("") }
    var otp2 by rememberSaveable { mutableStateOf("") }
    var otp3 by rememberSaveable { mutableStateOf("") }
    var otp4 by rememberSaveable { mutableStateOf("") }

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    val isFormValid =
        otp1.isNotBlank() && otp2.isNotBlank() && otp3.isNotBlank() && otp4.isNotBlank()

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    val buttonColor by animateColorAsState(
        targetValue = if (isFormValid) Color(0XFFF97316) else Color.Gray
    )

    // 🌈 Gradient Background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00C853))
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f))

            // 🧊 Glass Card
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White.copy(alpha = 0.95f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )

                Text(
                    text = stringResource(R.string.otp_verification),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A00C8)
                )

                Text(
                    text = "Enter 4-digit code",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                // 🔢 OTP BOXES
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OtpBox(otp1, focusRequester1) {
                        if (it.length <= 1) {
                            otp1 = it
                            if (it.isNotEmpty()) focusRequester2.requestFocus()
                        }
                    }

                    OtpBox(otp2, focusRequester2) {
                        if (it.length <= 1) {
                            otp2 = it
                            if (it.isNotEmpty()) focusRequester3.requestFocus()
                            if (it.isEmpty()) focusRequester1.requestFocus()
                        }
                    }

                    OtpBox(otp3, focusRequester3) {
                        if (it.length <= 1) {
                            otp3 = it
                            if (it.isNotEmpty()) focusRequester4.requestFocus()
                            if (it.isEmpty()) focusRequester2.requestFocus()
                        }
                    }

                    OtpBox(otp4, focusRequester4) {
                        if (it.length <= 1) {
                            otp4 = it
                            if (it.isNotEmpty()) focusManager.clearFocus()
                            if (it.isEmpty()) focusRequester3.requestFocus()
                        }
                    }
                }

                // 🚀 VERIFY BUTTON
                Button(
                    onClick = {
                        isLoading = true
                        scope.launch {
                            delay(2000)
                            isLoading = false
                            navController.navigate(Screen.ResetPassword.route)
                        }
                    },
                    enabled = isFormValid && !isLoading,
                    modifier = Modifier
                        .wrapContentWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("Verify OTP")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun OtpBox(
    value: String,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .width(60.dp)
            .focusRequester(focusRequester),
        textStyle = androidx.compose.ui.text.TextStyle(
            fontSize = 20.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        ),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF3E5F5),
            unfocusedContainerColor = Color(0xFFF3E5F5),
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}