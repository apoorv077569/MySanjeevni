package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui

import android.view.LayoutInflater
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var countryCode by remember { mutableStateOf("+91") }
    val isFormValid = phoneNumber.isNotBlank()
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    val buttonBg by animateColorAsState(
        targetValue = if (isFormValid) colorScheme.primary
        else colorScheme.onSurface.copy(alpha = 0.12f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ── Card ─────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surface, RoundedCornerShape(20.dp))
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
                    color = colorScheme.primary
                )

                AutoText(
                    text = stringResource(R.string.forget_password),
                    fontSize = 14.sp,
                    color = colorScheme.onSurfaceVariant
                )

                // ── Phone + Country Code ──────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AndroidView(
                        modifier = Modifier
                            .width(90.dp)
                            .height(56.dp)
                            .background(
                                colorScheme.surfaceVariant,
                                RoundedCornerShape(topStart = 12.dp, bottomStart = 12.dp)
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
                        placeholder = { AutoText("Phone Number", color = colorScheme.onSurfaceVariant) },
                        modifier = Modifier.weight(1f).height(56.dp),
                        shape = RoundedCornerShape(topEnd = 12.dp, bottomEnd = 12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = colorScheme.primary,
                            unfocusedBorderColor = colorScheme.outline,
                            focusedTextColor = colorScheme.onSurface,
                            unfocusedTextColor = colorScheme.onSurface,
                            cursorColor = colorScheme.primary,
                        )
                    )
                }

                // ── Send OTP Button ───────────────────────────────────────
                Button(
                    onClick = { viewModel.sendOtp("$countryCode$phoneNumber", "user") },
                    enabled = isFormValid && uiState !is AuthUiState.Loading,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonBg,
                        contentColor = colorScheme.onPrimary,
                        disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f),
                        disabledContentColor = colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                ) {
                    if (uiState is AuthUiState.Loading) {
                        CircularProgressIndicator(
                            color = colorScheme.onPrimary,
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
                        Toast.makeText(navController.context, "OTP Sent Successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.VERIFY.createRoute("$countryCode$phoneNumber"))
                    }
                    is AuthUiState.Error -> {
                        Toast.makeText(navController.context, state.message, Toast.LENGTH_SHORT).show()
                    }
                    else -> {}
                }
            }
        }
    }
}