package com.mysanjeevni.mysanjeevni.features.onboarding.ui.onBoarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.onboarding.model.OnBoardingPage

@Composable
fun OnBoardPageUI(page: OnBoardingPage) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(30.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(page.image),
            null,
            modifier = Modifier.size(260.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        Text(
            page.title,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            page.description,
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}