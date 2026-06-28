package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.viewmodel.AuthViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import kotlinx.coroutines.launch

@Composable
fun OtpVerificationScreen(navController: NavController, mobile: String) {

    var otp1 by rememberSaveable { mutableStateOf("") }
    var otp2 by rememberSaveable { mutableStateOf("") }
    var otp3 by rememberSaveable { mutableStateOf("") }
    var otp4 by rememberSaveable { mutableStateOf("") }

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val viewModel: AuthViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val verifyOtp by viewModel.otpSent.collectAsState()
    val error by viewModel.error.collectAsState()
    val scope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme

    val isFormValid = otp1.isNotBlank() && otp2.isNotBlank() && otp3.isNotBlank() && otp4.isNotBlank()

    val buttonBg by animateColorAsState(
        targetValue = if (isFormValid) colorScheme.primary
        else colorScheme.onSurface.copy(alpha = 0.12f)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.primary)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // ── Card ─────────────────────────────────────────────────────
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(colorScheme.surface, RoundedCornerShape(20.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.size(70.dp)
                )

                AutoText(
                    text = stringResource(R.string.otp_verification),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.primary
                )

                AutoText(
                    text = "Enter 4-digit code",
                    fontSize = 14.sp,
                    color = colorScheme.onSurfaceVariant
                )

                // ── OTP Boxes ─────────────────────────────────────────────
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

                Button(
                    onClick = {
                        scope.launch { viewModel.verifyOtp("$otp1$otp2$otp3$otp4", mobile) }
                    },
                    enabled = isFormValid && !isLoading,
                    modifier = Modifier.wrapContentWidth().height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonBg,
                        contentColor = colorScheme.onPrimary,
                        disabledContainerColor = colorScheme.onSurface.copy(alpha = 0.12f),
                        disabledContentColor = colorScheme.onSurface.copy(alpha = 0.38f)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        AutoText("Verify OTP")
                    }
                }
            }

            LaunchedEffect(verifyOtp) {
                if (verifyOtp) {
                    Toast.makeText(context, "OTP Verified Successfully", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.ResetPassword.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }
            LaunchedEffect(error) {
                error?.let { Toast.makeText(context, it, Toast.LENGTH_SHORT).show() }
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
    val colorScheme = MaterialTheme.colorScheme
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .width(60.dp)
            .focusRequester(focusRequester),
        textStyle = TextStyle(
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            color = colorScheme.onSurface
        ),
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = colorScheme.primaryContainer,
            unfocusedContainerColor = colorScheme.surfaceVariant,
            focusedIndicatorColor = colorScheme.primary,   // bottom line on focus
            unfocusedIndicatorColor = colorScheme.outline.copy(alpha = 0f),
            cursorColor = colorScheme.primary,
        )
    )
}