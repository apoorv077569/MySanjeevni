package com.mysanjeevni.mysanjeevni.features.privacy.presentation.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun PrivacyScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        AutoText(
            text = "Privacy Policy",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        AutoText(
            text = """
We collect user data to provide healthcare services:

• Personal Info (name, phone, address)
• Health Data (reports, prescriptions)
• Payment Data (via secure gateways)

We share data with:
• Pharmacies
• Labs
• Doctors

Your data is protected with encryption and secure servers.

You can:
• Edit your data
• Request deletion
            """.trimIndent()
        )
    }
}