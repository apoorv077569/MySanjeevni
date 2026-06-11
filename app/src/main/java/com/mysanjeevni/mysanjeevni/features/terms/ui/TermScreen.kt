package com.mysanjeevni.mysanjeevni.features.terms.ui

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
fun TermsScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        AutoText(
            text = "Terms of Use",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        AutoText(
            text = """
PLEASE READ THESE TERMS CAREFULLY.

My Sanjeevni is a healthcare platform that connects users with:
• Pharmacies
• Labs
• Doctors

We act only as an intermediary and are not responsible for services provided by third parties.

• Prescription required for medicines
• Not for emergency use
• Payments via secure gateways
• No refund after service

Grievance Officer:
Email: mysanjeevni3693@gmail.com
            """.trimIndent()
        )
    }
}