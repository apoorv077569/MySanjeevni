package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.auth.presentation.state.AuthUiState
import com.mysanjeevni.mysanjeevni.features.auth.presentation.viewmodel.AuthViewModel
import com.mysanjeevni.mysanjeevni.features.support.ticket.workers.TicketStatusWorker
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("user") }
    var passwordVisible by remember { mutableStateOf(false) }
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        Log.d("GOOGLE_AUTH", "Package = ${context.packageName}")

        val playServices = com.google.android.gms.common.GoogleApiAvailability
            .getInstance()
            .isGooglePlayServicesAvailable(context)

        Log.d("GOOGLE_AUTH", "Google Play Services = $playServices")
    }
    val sessionManager = SessionManager(context)
    val viewModel: AuthViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()
    val googleLoginResponse by viewModel.googleLoginResponse.collectAsState()
    val isFormValid = email.isNotBlank() && password.isNotBlank() && role.isNotBlank()

    val colorScheme = MaterialTheme.colorScheme

//    val gso = remember {
//        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken("842557074955-pd1onnbeiijj3pjgmnvqdooa72lfurp3.apps.googleusercontent.com")
//            .requestEmail()
//            .build()
//    }

    val webClientId = stringResource(R.string.default_web_client_id)

    Log.d("GOOGLE_AUTH", "default_web_client_id = $webClientId")

    val gso = remember(webClientId) {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(webClientId)
            .requestEmail()
            .build()
    }
    val googleSignInClient = remember(gso) {
        GoogleSignIn.getClient(context, gso)
    }
    val googleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
//            val account = task.getResult(ApiException::class.java)
//            val idToken = account?.idToken
//            if (idToken != null) {
//                Log.d("GOOGLE_AUTH", "idToken: $idToken")
//                viewModel.googleLogin(idToken)
//            }
            val account = task.getResult(ApiException::class.java)
            val googleIdToken = account?.idToken
            if (googleIdToken != null) {
                viewModel.googleLogin(googleIdToken)
            }
            Log.d("GOOGLE_AUTH", "idToken: $googleIdToken")
        } catch (e: ApiException) {
            val statusCode = e.statusCode
            val errorName = CommonStatusCodes.getStatusCodeString(statusCode)
            Log.e("GOOGLE_AUTH", "Status=${e.statusCode}", e)

            e.status?.let {
                Log.e("GOOGLE_AUTH", "Resolution = ${it.resolution}")
            }
            Log.e("GOOGLE_AUTH", "------ GOOGLE ERROR BODY ------")
            Log.e("GOOGLE_AUTH", "Status Code: $statusCode")
            Log.e("GOOGLE_AUTH", "Error Name: $errorName")
            Log.e("GOOGLE_AUTH", "Message: ${e.message}")
            Log.e("GOOGLE_AUTH", "-------------------------------")
            when (statusCode) {
                10 -> Log.e(
                    "GOOGLE_AUTH",
                    "Hint: DEVELOPER_ERROR. Check SHA-1 in Firebase and ensure you are using the WEB Client ID."
                )

                7 -> Log.e("GOOGLE_AUTH", "Hint: NETWORK_ERROR. Check your internet connection.")
                12500 -> Log.e(
                    "GOOGLE_AUTH",
                    "Hint: SIGN_IN_FAILED. Check your Firebase config or google-services.json."
                )
            }
        }
    }

    // Button animates between primary and disabled surface color
    val buttonBg by animateColorAsState(
        targetValue = if (isFormValid) colorScheme.primary else colorScheme.onSurface.copy(alpha = 0.12f)
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
                    text = stringResource(R.string.login),
                    fontSize = 14.sp,
                    color = colorScheme.onSurfaceVariant
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { AutoText("Email") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Email,
                            contentDescription = null,
                            tint = colorScheme.onSurfaceVariant
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.outline,
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        cursorColor = colorScheme.primary,
                        focusedLabelColor = colorScheme.primary,
                        unfocusedLabelColor = colorScheme.onSurfaceVariant,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { AutoText("Password") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Lock,
                            contentDescription = null,
                            tint = colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility
                            else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) stringResource(R.string.hide_password) else stringResource(R.string.show_password),
                            tint = colorScheme.onSurfaceVariant,
                            modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                        )
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.outline,
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        cursorColor = colorScheme.primary,
                        focusedLabelColor = colorScheme.primary,
                        unfocusedLabelColor = colorScheme.onSurfaceVariant,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )

                AutoText(
                    text = stringResource(R.string.forget_password),
                    color = colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable { navController.navigate(Screen.Forget.route) }
                )

                // ── Login Button ──────────────────────────────────────────
                Button(
                    onClick = { viewModel.login(role, email, password) },
                    enabled = isFormValid && uiState !is AuthUiState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
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
                        AutoText("Login", fontSize = 16.sp)
                    }
                }

                // ── Google Sign-In Button ─────────────────────────────────
                Button(
                    onClick = {
                        Log.d(
                            "GOOGLE_AUTH",
                            "Last Account = ${GoogleSignIn.getLastSignedInAccount(context)}"
                        )
                        googleSignInClient.signOut().addOnCompleteListener {
                            Log.d(
                                "GOOGLE_AUTH",
                                "Launching Google Sign-In..."
                            )
                            googleLauncher.launch(googleSignInClient.signInIntent)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorScheme.surface,
                        contentColor = colorScheme.onSurface
                    ),
                    border = BorderStroke(1.dp, colorScheme.outline),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.google),
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        AutoText(
                            text = "Sign in with Google",
                            color = colorScheme.onSurfaceVariant,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Row {
                    AutoText("Don't have an account? ", color = colorScheme.onSurface)
                    AutoText(
                        "Sign up",
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { navController.navigate(Screen.Signup.route) }
                    )
                }
            }

            // ── Side effects ──────────────────────────────────────────────
            LaunchedEffect(uiState) {
                when (val state = uiState) {
                    is AuthUiState.LoginSuccess -> {
                        val token = state.data.token
                        val user = state.data.user
                        Log.d("LOGIN_DEBUG", "Token: $token")
                        Log.d("LOGIN_DEBUG", "User: $user")
                        Log.d("LOGIN_DEBUG", "UserId: ${user?.id}")
                        Log.d("LOGIN_DEBUG", "UserRole: ${user?.role}")
                        Log.d("LOGIN_DEBUG", "UserName: ${user?.fullName}")
                        sessionManager.saveLogin(token, user?.id)
                        sessionManager.saveUserRole(user?.role ?: "user")
                        sessionManager.saveUserName(user?.fullName ?: "")
                        sessionManager.saveUserEmail(user?.email ?: "")
                        sessionManager.saveUserAddress(user?.address ?: "")
                        Log.d("LOGIN_DEBUG", "Session Saved - Token: ${sessionManager.getToken()}")
                        Log.d(
                            "LOGIN_DEBUG",
                            "Session Saved - UserId: ${sessionManager.getUserId()}"
                        )
                        Log.d(
                            "LOGIN_DEBUG",
                            "Session Saved - Role: ${sessionManager.getUserRole()}"
                        )

                        val request =
                            PeriodicWorkRequestBuilder<
                                    TicketStatusWorker>(
                                15,
                                TimeUnit.MINUTES
                            ).build()

                        WorkManager.getInstance(context)
                            .enqueueUniquePeriodicWork(
                                "ticket_status_worker",
                                ExistingPeriodicWorkPolicy.KEEP,
                                request
                            )

                        viewModel.clearState()
                        Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    }

                    is AuthUiState.Error -> {
                        Log.d("LOGIN_DEBUG", "Login Error: ${state.message}")
                        Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                    }

                    else -> {}
                }
            }

            LaunchedEffect(googleLoginResponse) {
                googleLoginResponse?.let { body ->
                    val token = body.token
                    val user = body.user
                    sessionManager.saveLogin(token, user?._id)
                    sessionManager.saveUserRole(user?.role ?: "user")
                    sessionManager.saveUserAddress(user?.address ?: "")
                    sessionManager.saveUserName(user?.fullName ?: "")
                    sessionManager.saveUserEmail(user?.email ?: "")
                    val request =
                        PeriodicWorkRequestBuilder<
                                TicketStatusWorker>(
                            15,
                            TimeUnit.MINUTES
                        ).build()

                    WorkManager.getInstance(context)
                        .enqueueUniquePeriodicWork(
                            "ticket_status_worker",
                            ExistingPeriodicWorkPolicy.KEEP,
                            request
                        )
                    viewModel.clearState()
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}