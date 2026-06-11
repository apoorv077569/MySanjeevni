package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui

import android.view.LayoutInflater
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hbb20.CountryCodePicker
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.state.AuthUiState
import com.mysanjeevni.mysanjeevni.features.auth.presentation.viewmodel.AuthViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun ForgetScreen(navController: NavController) {

    var phoneNumber by remember { mutableStateOf("") }


    val isFormValid = phoneNumber.isNotBlank()
    val scope = rememberCoroutineScope()
    var countryCode by remember { mutableStateOf("+91") }
    val viewModel: AuthViewModel = hiltViewModel()

    val uiState by viewModel.uiState.collectAsState()
    val otpSent by viewModel.otpSent.collectAsState()
    val error by viewModel.error.collectAsState()


    val buttonColor by animateColorAsState(
        targetValue = if (isFormValid) Color(0XFFF97316) else Color.Gray
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00C853)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White.copy(alpha = 0.95f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )

                AutoText(
                    text = stringResource(R.string.app_name),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A00C8)
                )

                AutoText(
                    text = stringResource(R.string.forget_password),
                    fontSize = 14.sp,
                    color = Color.Gray
                )


                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AndroidView(
                        modifier = Modifier
                            .width(90.dp)   // 🔥 FIXED
                            .height(56.dp)
                            .background(
                                Color.White,
                                RoundedCornerShape(
                                    topStart = 12.dp,
                                    bottomStart = 12.dp
                                )
                            ),

                        factory = { context ->
                            val view = LayoutInflater.from(context)
                                .inflate(R.layout.country_code_picker, null)

                            val ccp = view.findViewById<CountryCodePicker>(R.id.ccp)

                            countryCode = ccp.selectedCountryCodeWithPlus

                            ccp.setOnCountryChangeListener {
                                countryCode = ccp.selectedCountryCodeWithPlus
                            }

                            view
                        }
                    )

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        placeholder = { AutoText("Phone Number") },

                        modifier = Modifier
                            .weight(1f)   // 👈 correct use here
                            .height(56.dp),

                        shape = RoundedCornerShape(
                            topEnd = 12.dp,
                            bottomEnd = 12.dp
                        ),

                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),

                        singleLine = true
                    )
                }

                Button(
                    onClick = {
                        val fullPhone = "$countryCode$phoneNumber"
                        viewModel.sendOtp(fullPhone, "user")
                    },
                    enabled = isFormValid && uiState !is AuthUiState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    if (uiState is AuthUiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        AutoText("Send OTP", fontSize = 16.sp)
                    }
                }
            }
            LaunchedEffect(uiState) {

                when (val state = uiState) {

                    is AuthUiState.OtpSent -> {

                        Toast.makeText(
                            navController.context,
                            "OTP Sent Successfully",
                            Toast.LENGTH_SHORT
                        ).show()

                        val fullPhone =
                            "$countryCode$phoneNumber"

                        navController.navigate(
                            Screen.VERIFY.createRoute(fullPhone)
                        )
                    }

                    is AuthUiState.Error -> {

                        Toast.makeText(
                            navController.context,
                            state.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    else -> {}
                }
            }
        }
    }
}
