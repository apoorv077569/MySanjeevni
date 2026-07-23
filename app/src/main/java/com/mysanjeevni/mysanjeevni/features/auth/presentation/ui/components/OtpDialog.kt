package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.auth.presentation.state.AuthUiState
import com.mysanjeevni.mysanjeevni.features.auth.presentation.viewmodel.AuthViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun OtpVerificationDialog(
    phone: String,
    onDismiss: () -> Unit,
    onVerified: () -> Unit
) {
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
    var errorMessage by remember { mutableStateOf("") }
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val colorScheme = MaterialTheme.colorScheme

    val otp = otp1+otp2+otp3+otp4+otp5+otp6

    androidx.compose.ui.window.Dialog(
        onDismissRequest = { if (uiState !is AuthUiState.Loading) onDismiss() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorScheme.surface, RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AutoText(
                text = stringResource(R.string.verify_otp),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.primary
            )

            AutoText(
                text = stringResource(R.string.enter_the_otp_sent_to, phone),
                fontSize = 14.sp,
                color = colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

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

            if (errorMessage.isNotEmpty()) {
                AutoText(
                    text = errorMessage,
                    color = colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.surfaceVariant,
                        contentColor = colorScheme.onSurfaceVariant
                    )
                ) {
                    AutoText(stringResource(R.string.cancel), fontSize = 15.sp)
                }

                // Verify
                Button(
                    onClick = {
                        when {
                            otp.isEmpty() -> errorMessage = "Please enter OTP"
                            otp.length != 6 -> errorMessage = "OTP must be 6 digits"
                            else -> {
                                errorMessage = ""
                                viewModel.verifyOtpBeforeSignup(phone, otp, "user")
                            }
                        }
                    },
                    enabled = otp.length == 6 && uiState !is AuthUiState.Loading,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.primary,
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
                        AutoText(stringResource(R.string.verify), fontSize = 15.sp)
                    }
                }
            }
        }
    }
}