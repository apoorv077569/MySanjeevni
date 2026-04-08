package com.mysanjeevni.mysanjeevni.features.auth.presentation.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
fun ForgetScreen(navController: NavController) {

    var email by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val isFormValid = email.isNotBlank()
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
                    text = stringResource(R.string.forget_password),
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                // 📧 Email Field (Floating Label)
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

                // 🚀 Send OTP Button
                Button(
                    onClick = {
                        isLoading = true
                        scope.launch {
                            delay(2000)
                            isLoading = false
                            navController.navigate(Screen.VERIFY.route)
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
                        Text("Send OTP", fontSize = 16.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}