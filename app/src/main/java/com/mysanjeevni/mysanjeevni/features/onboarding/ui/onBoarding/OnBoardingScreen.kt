package com.mysanjeevni.mysanjeevni.features.onboarding.ui.onBoarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mysanjeevni.mysanjeevni.features.onboarding.data.onBoardingPages
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnBoardingScreen(onFinish: () -> Unit) {

    val pagerState = rememberPagerState(
        pageCount = { onBoardingPages.size }
    )

    val coroutineScope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF00C853))
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            // Skip Button
            Text(
                text = "Skip",
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(end = 20.dp)
                    .clickable { onFinish() }
            )

            // Pager Content
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.weight(1f)
            ) { page ->
                OnBoardPageUI(onBoardingPages[page])
            }

            // Indicator
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)
            ) {
                repeat(onBoardingPages.size) { index ->

                    val color =
                        if (pagerState.currentPage == index)
                            Color.White
                        else
                            Color(0xFFB2DFDB) // soft green

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .size(10.dp)
                            .background(
                                color,
                                shape = RoundedCornerShape(50)
                            )
                    )
                }
            }

            // Button
            Button(
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF00796B) // dark green
                ),
                onClick = {
                    coroutineScope.launch {
                        if (pagerState.currentPage == onBoardingPages.lastIndex) {
                            onFinish()
                        } else {
                            pagerState.animateScrollToPage(
                                pagerState.currentPage + 1
                            )
                        }
                    }
                },
                modifier = Modifier
                    .wrapContentHeight()
                    .navigationBarsPadding()
                    .padding(24.dp)
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp)
            ) {
                Text(
                    text =
                        if (pagerState.currentPage == onBoardingPages.lastIndex)
                            "Get Started"
                        else
                            "Next",
                    color = Color.White
                )
            }
        }
    }
}