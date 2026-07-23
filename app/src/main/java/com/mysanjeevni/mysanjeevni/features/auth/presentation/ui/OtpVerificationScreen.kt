package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui

import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.state.AuthUiState
import com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.components.OtpBox
import com.mysanjeevni.mysanjeevni.features.auth.presentation.viewmodel.AuthViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import kotlinx.coroutines.launch

@Composable
fun OtpVerificationScreen(navController: NavController, mobile: String) {

    var otp1 by rememberSaveable { mutableStateOf("") }
    var otp2 by rememberSaveable { mutableStateOf("") }
    var otp3 by rememberSaveable { mutableStateOf("") }
    var otp4 by rememberSaveable { mutableStateOf("") }
    var otp5 by rememberSaveable { mutableStateOf("") }
    var otp6 by rememberSaveable { mutableStateOf("") }

    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }
    val focusRequester4 = remember { FocusRequester() }
    val focusRequester5 = remember { FocusRequester() }
    val focusRequester6 = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val viewModel: AuthViewModel = hiltViewModel()
    val isLoading by viewModel.isLoading.collectAsState()
    val verifyOtp by viewModel.otpSent.collectAsState()
    val error by viewModel.error.collectAsState()
    val scope = rememberCoroutineScope()
    val colorScheme = MaterialTheme.colorScheme

    val isFormValid = otp1.isNotBlank() && otp2.isNotBlank() && otp3.isNotBlank() && otp4.isNotBlank() && otp5.isNotBlank() && otp6.isNotBlank()

    val buttonBg by animateColorAsState(
        targetValue = if (isFormValid) colorScheme.primary
        else colorScheme.onSurface.copy(alpha = 0.12f)
    )
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState) {

        when (uiState) {

            is AuthUiState.OtpVerified -> {

                val enteredOtp =
                    "$otp1$otp2$otp3$otp4$otp5$otp6"

                Toast.makeText(
                    context,
                    R.string.otp_verified_successfully,
                    Toast.LENGTH_SHORT
                ).show()

                navController.navigate(
                    Screen.ResetPassword.createRoute(
                        mobile = mobile,
                        resetPasswordToken = enteredOtp
                    )
                ) {
                    popUpTo(Screen.VERIFY.route) {
                        inclusive = true
                    }
                    launchSingleTop = true
                }

                viewModel.clearState()
            }

            is AuthUiState.Error -> {
                Toast.makeText(
                    context,
                    (uiState as AuthUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()

                viewModel.clearState()
            }

            else -> {}
        }
    }

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
                    text = stringResource(R.string.enter_6_digit_otp),
                    fontSize = 14.sp,
                    color = colorScheme.onSurfaceVariant
                )

                // ── OTP Boxes ─────────────────────────────────────────────
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OtpBox(otp1, focusRequester1,Modifier.weight(1f)) {
                        if (it.length <= 1) {
                            otp1 = it
                            if (it.isNotEmpty()) focusRequester2.requestFocus()
                        }
                    }
                    OtpBox(otp2, focusRequester2,Modifier.weight(1f)) {
                        if (it.length <= 1) {
                            otp2 = it
                            if (it.isNotEmpty()) focusRequester3.requestFocus()
                            if (it.isEmpty()) focusRequester1.requestFocus()
                        }
                    }
                    OtpBox(otp3, focusRequester3,Modifier.weight(1f)) {
                        if (it.length <= 1) {
                            otp3 = it
                            if (it.isNotEmpty()) focusRequester4.requestFocus()
                            if (it.isEmpty()) focusRequester2.requestFocus()
                        }
                    }
                    OtpBox(otp4, focusRequester4,Modifier.weight(1f)) {
                        if (it.length <= 1) {
                            otp4 = it
                            if (it.isNotEmpty()) {
                                focusRequester5.requestFocus()
                            } else {
                                focusRequester3.requestFocus()
                            }
                        }
                    }
                    OtpBox(otp5, focusRequester5,Modifier.weight(1f)) {
                        if (it.length <= 1) {
                            otp5 = it
                            if (it.isNotEmpty()) focusRequester6.requestFocus()
                            if (it.isEmpty()) focusRequester4.requestFocus()
                        }
                    }
                    OtpBox(otp6, focusRequester6,Modifier.weight(1f)) {
                        if (it.length <= 1) {
                            otp6 = it
                            if (it.isNotEmpty()) focusManager.clearFocus()
                            if (it.isEmpty()) focusRequester5.requestFocus()
                        }
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    AutoText(
                        text = stringResource(R.string.did_not_receive_the_otp),
                        fontSize = 14.sp,
                        color = colorScheme.onSurfaceVariant
                    )
                    TextButton(
                        onClick = {
                            scope.launch {
                                viewModel.sendOtp(
                                    phone = mobile
                                )
                            }
                        },
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        AutoText(
                            text = stringResource(R.string.resend_otp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorScheme.primary
                        )
                    }
                }
                Button(
                    onClick = {
                        val enteredOtp = "$otp1$otp2$otp3$otp4$otp5$otp6"

                        navController.navigate(
                            Screen.ResetPassword.createRoute(
                                mobile = mobile,
                                resetPasswordToken = enteredOtp
                            )
                        )
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
                )
                {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = colorScheme.onPrimary,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        AutoText(stringResource(R.string.verify_otp))
                    }
                }
            }
            LaunchedEffect(verifyOtp) {
                if (verifyOtp) {
                    Toast.makeText(context, R.string.otp_verified_successfully, Toast.LENGTH_SHORT).show()
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

