package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui

import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.data.remote.ApiClient
import com.mysanjeevni.mysanjeevni.data.remote.model.LoginRequest
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("User") }
    var expanded by remember { mutableStateOf(false) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val sessionManager = SessionManager(context)


    val scope = rememberCoroutineScope()

    val isFormValid = email.isNotBlank() && password.isNotBlank() && role.isNotBlank()

    val buttonColor by animateColorAsState(
        targetValue = if (isFormValid) Color(0XFFF97316) else Color.Gray
    )
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
                    text = stringResource(R.string.login),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                // 🔽 Register As Dropdown
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = role,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Register As") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                        },

                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        listOf("User", "Doctor").forEach {
                            DropdownMenuItem(
                                text = { Text(it) },
                                onClick = {
                                    role = it
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // 📧 Email
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

                // 🔒 Password with eye
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Next
                    ),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null)
                    },
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

                Text(
                    text = stringResource(R.string.forget_password),
                    color = Color(0xFF6A00C8),
                    modifier = Modifier
                        .align(Alignment.End)
                        .clickable {
                            navController.navigate(Screen.Forget.route)
                        }
                )

                // 🚀 Button
                Button(
                    onClick = {
                        isLoading = true
                        scope.launch {
                            try {

                                // 🔥 REQUEST LOG (ADD THIS)
                                val requestJson = """
    {
        "email":"$email",
        "password":"$password"
    }
    """.trimIndent()

                                Log.d("LOGIN_REQUEST", requestJson)

                                val response = ApiClient.api.loginUser(
                                    LoginRequest(role,email, password)
                                )

                                isLoading = false

                                // 🔥 RESPONSE LOG
                                Log.d(
                                    "LOGIN_DEBUG", """
        Code: ${response.code()}
        Message: ${response.message()}
        Body: ${response.body()}
    """.trimIndent()
                                )

                                if (response.isSuccessful) {

                                    val body = response.body()
                                    val user = body?.user
                                    val token = response.body()?.token

                                    if (token != null){
                                        sessionManager.saveLogin(token,user!!._id)
                                    }
                                    Toast.makeText(context,"Login Successfull",Toast.LENGTH_SHORT).show()
                                    Log.d("LOGIN_SUCCESS", "User: $user")

                                    if (user?.role == "Doctor") {
                                        navController.navigate(Screen.DoctorDashboard.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                    } else {
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Login.route) { inclusive = true }
                                        }
                                    }

                                } else {
                                    val error = response.errorBody()?.string()
                                    Toast.makeText(context,error,Toast.LENGTH_SHORT).show()

                                    Log.e("LOGIN_ERROR", "Error Body: $error")
                                }

                            } catch (e: Exception) {
                                isLoading = false
                                Toast.makeText(context,e.localizedMessage,Toast.LENGTH_SHORT).show()

                                Log.e("LOGIN_EXCEPTION", "Exception: ${e.message}")
                            }
                        }
                    },
                    enabled = isFormValid && !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
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
                        Text("Login", fontSize = 16.sp)
                    }
                }

                Row {
                    Text("Don't have an account? ")
                    Text(
                        "Signup",
                        color = Color(0xFF6A00C8),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Signup.route)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}