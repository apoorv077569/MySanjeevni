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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import com.mysanjeevni.mysanjeevni.data.remote.model.RegisterRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {

    var fullName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("User") }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var fullAddress by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val isFormValid =
        email.isNotBlank() && password.isNotBlank() && confirmPassword.isNotBlank()
                && role.isNotBlank() && fullName.isNotBlank() && role.isNotBlank() && phoneNumber.isNotBlank()

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

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

//                Full Name
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

                // Phone Number
                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    label = { Text("Phone Number") },
                    leadingIcon = {
                        Icon(Icons.Default.Phone, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    singleLine = true
                )
                // 📧 Email
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
                        isLoading = true
                        scope.launch {
                            try {
                                val response = ApiClient.api.registerUser(
                                    RegisterRequest(
                                        fullName,
                                        role,
                                        email,
                                        phoneNumber,
                                        fullAddress,
                                        password
                                    )
                                )
                                if (response.isSuccessful) {
                                    Log.d("API", "Success: ${response.body()}")
                                    isLoading = false
                                    Toast.makeText(context,"Signup Successfull",Toast.LENGTH_SHORT).show()

                                    navController.navigate(Screen.Login.route) {
                                        popUpTo(Screen.Login.route) { inclusive = true }
                                    }
                                } else {
                                    Toast.makeText(context,response.errorBody()?.string(),Toast.LENGTH_SHORT).show()
                                    Log.e("API", "Error: ${response.errorBody()?.string()}")

                                }
                            } catch (e: Exception) {
                                Toast.makeText(context,e.localizedMessage,Toast.LENGTH_SHORT).show()
                                Log.e("API", "Exception: ${e.message}")
                            }
//                            delay(2000)
//                            isLoading = false
//                            navController.navigate(Screen.Login.route) {
//                                popUpTo(Screen.Login.route) { inclusive = true }
//                            }
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
                        Text("Sign Up", fontSize = 16.sp)
                    }
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
    }
}
