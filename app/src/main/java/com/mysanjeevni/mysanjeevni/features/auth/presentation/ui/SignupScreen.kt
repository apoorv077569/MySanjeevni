package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui

import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.hbb20.CountryCodePicker
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
    var otp by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    androidx.compose.ui.window.Dialog(onDismissRequest = { if (uiState !is AuthUiState.Loading) onDismiss() }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AutoText(
                text = "Verify OTP",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6A00C8)
            )

            AutoText(
                text = "Enter the OTP sent to\n$phone",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )

            OutlinedTextField(
                value = otp,
                onValueChange = {
                    if (it.length <= 6 && it.all { char -> char.isDigit() }) {
                        otp = it
                        errorMessage = ""
                    }
                },
                label = { AutoText("Enter OTP") },
                placeholder = { AutoText("000000") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                isError = errorMessage.isNotEmpty()
            )

            if (errorMessage.isNotEmpty()) {
                AutoText(
                    text = errorMessage,
                    color = Color.Red,
                    fontSize = 12.sp,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    )
                ) {
                    AutoText("Cancel", fontSize = 15.sp)
                }

                // Verify Button
                Button(
                    onClick = {
                        when {
                            otp.isEmpty() -> {
                                errorMessage = "Please enter OTP"
                            }

                            otp.length != 6 -> {
                                errorMessage = "OTP must be 6 digits"
                            }

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
                        containerColor = Color(0XFFF97316)
                    )
                ) {
                    if (uiState is AuthUiState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        AutoText("Verify", fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {

    var fullName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("User") }
    val context = LocalContext.current
    var showOtpDialog by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var fullAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+91") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()


    val isFormValid =
        email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
                && role.isNotBlank() && fullName.isNotBlank() && role.isNotBlank() && phoneNumber.isNotBlank()

    val buttonColor by animateColorAsState(
        targetValue = if (isFormValid) Color(0XFFF97316) else Color.Gray
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF00C853)
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .imePadding(),
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
                    .padding(24.dp)
                    .imePadding()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Image(
                    painter = painterResource(R.drawable.app_logo),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp)
                )

                Text(
                    text = stringResource(R.string.app_name),
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6A00C8)
                )

                Text(
                    text = stringResource(R.string.signup),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    label = { Text("Full Name") },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
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
                        trailingIcon = {
                            TextButton(onClick = {
                                if (phoneNumber.length == 10) {
                                    showOtpDialog = true
                                }
                                viewModel.sendOtpBeforeSignup(countryCode + phoneNumber, fullName)
                            }) {
                                Text(
                                    text = "Verify",
                                    color = Color(0xFF00A86B)
                                )
                            }
                        },
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

                OutlinedTextField(
                    value = fullAddress,
                    onValueChange = { fullAddress = it },
                    label = { Text("Full Address") },
                    leadingIcon = {
                        Icon(Icons.Default.LocationOn, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // 🔒 Password
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = if (passwordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                passwordVisible = !passwordVisible
                            }
                        )
                    },
                    visualTransformation =
                        if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    trailingIcon = {
                        Icon(
                            imageVector = if (confirmPasswordVisible)
                                Icons.Default.Visibility
                            else
                                Icons.Default.VisibilityOff,
                            contentDescription = null,
                            modifier = Modifier.clickable {
                                confirmPasswordVisible = !confirmPasswordVisible
                            }
                        )
                    },
                    visualTransformation =
                        if (confirmPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                // 🚀 Signup Button
                Button(
                    onClick = {
                        Toast.makeText(
                            context,
                            "SIGNUP CLICKED",
                            Toast.LENGTH_LONG
                        ).show()
                        Log.e(
                            "REGISTER_DEBUG",
                            "SIGNUP BUTTON CLICKED"
                        )
                        viewModel.register(fullName, "user", email, countryCode + phoneNumber, fullAddress, password)
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
                        Text("Sign Up", fontSize = 16.sp)
                    }
                }

                if (showOtpDialog) {
                    OtpVerificationDialog(
                        phone = phoneNumber,
                        onDismiss = {
                            showOtpDialog = false
                        },
                        onVerified = {
                            showOtpDialog = false
                            Toast.makeText(
                                context,
                                "OTP Verified Successfully",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                }

                Row {
                    Text("Already have an account? ")
                    Text(
                        "Login",
                        color = Color(0xFF6A00C8),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.popBackStack()
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
        LaunchedEffect(uiState) {

            when (val state = uiState) {

                is AuthUiState.SignupOtpSent -> {
                    showOtpDialog = true
                }

                is AuthUiState.SignupOtpVerified -> {

                    showOtpDialog = false

                    Toast.makeText(
                        context,
                        "OTP Verified Successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is AuthUiState.SignupSuccess -> {

                    Toast.makeText(
                        context,
                        "Registration Successful",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.popBackStack()
                }

                is AuthUiState.Error -> {

                    Toast.makeText(
                        context,
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }
    }
}