package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components.ConsultCard
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel.MyConsultViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun MyConsultsScreen(
    navController: NavController,
    viewModel: MyConsultViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()
    val isDark = LocalIsDarkTheme.current || isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryText = if (isDark) Color.LightGray else Color.Gray
    val primaryColor = MaterialTheme.colorScheme.primary
    val topBarColor = if (isDark) Color(0xFF162A25) else Color(0xFFE9FAF6)
    val topCircleColor = if (isDark) Color(0xFF1D3932) else Color(0xFFD4F5EA)
    val topCircleColor2 = if (isDark) Color(0xFF23483E) else Color(0xFFC8F0E2)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp)
                .clip(
                    RoundedCornerShape(
                        bottomStart = 28.dp,
                        bottomEnd = 28.dp
                    )
                )
                .background(topBarColor)
        ) {

            // Decorative circle
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .offset(x = 285.dp, y = (-35).dp)
                    .background(
                        color = topCircleColor,
                        shape = CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(85.dp)
                    .offset(x = 325.dp, y = 55.dp)
                    .background(
                        color = topCircleColor2,
                        shape = CircleShape
                    )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 20.dp,
                        vertical = 16.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = if (isDark) {
                        Color(0xFF23483E)
                    } else {
                        Color.White
                    },
                    shadowElevation = 2.dp
                ) {

                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                            tint = textColor,
                            modifier = Modifier.size(25.dp)
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.width(14.dp)
                )

                Column {

                    AutoText(
                        text = stringResource(R.string.my_consultations),
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )

                    Spacer(
                        modifier = Modifier.height(2.dp)
                    )

                    AutoText(
                        text = "Manage your appointments",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = secondaryText
                    )
                }
            }
        }

        // -----------------------------------------------------
        // Tabs
        // -----------------------------------------------------

        TabRow(
            selectedTabIndex = state.selectedTab,
            containerColor = cardColor,
            contentColor = primaryColor,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(14.dp)),
            indicator = { tabPositions ->
                if (state.selectedTab < tabPositions.size) {
                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(tabPositions[state.selectedTab])
                            .padding(4.dp)
                            .fillMaxHeight()
                            .background(
                                color = primaryColor,
                                shape = RoundedCornerShape(12.dp)
                            )
                    )
                }
            },
            divider = {}
        ) {
            Tab(
                selected = state.selectedTab == 0,
                onClick = { viewModel.onTabSelected(0) },
                modifier = Modifier
                    .height(56.dp)
                    .zIndex(1f)
            ) {

                AutoText(
                    text = stringResource(R.string.upcoming),
                    fontSize = 16.sp,
                    fontWeight = if (state.selectedTab == 0) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Medium
                    },
                    color = if (state.selectedTab == 0) {
                        Color.White
                    } else {
                        secondaryText
                    }
                )
            }

            // Past Consults
            Tab(
                selected = state.selectedTab == 1,
                onClick = {
                    viewModel.onTabSelected(1)
                },
                modifier = Modifier
                    .height(56.dp)
                    .zIndex(1f)
            ) {

                AutoText(
                    text = stringResource(R.string.past_consults),
                    fontSize = 16.sp,
                    fontWeight = if (state.selectedTab == 1) {
                        FontWeight.Bold
                    } else {
                        FontWeight.Medium
                    },
                    color = if (state.selectedTab == 1) {
                        Color.White
                    } else {
                        secondaryText
                    }
                )
            }
        }

        // -----------------------------------------------------
        // Loading
        // -----------------------------------------------------

        if (state.isLoading) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                CircularProgressIndicator(
                    color = primaryColor
                )
            }

            return@Column
        }

        // -----------------------------------------------------
        // Error
        // -----------------------------------------------------

        if (state.error != null) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    AutoText(
                        text = state.error
                            ?: "Something went wrong",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 15.sp
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    Button(
                        onClick = {
                            viewModel.loadConsults()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor
                        )
                    ) {
                        AutoText(
                            text = "Retry",
                            color = Color.White
                        )
                    }
                }
            }

            return@Column
        }

        // -----------------------------------------------------
        // Select List
        // -----------------------------------------------------

        val listToShow =
            if (state.selectedTab == 0) {
                state.upcomingConsults
            } else {
                state.pastConsults
            }

        // -----------------------------------------------------
        // Empty
        // -----------------------------------------------------

        if (listToShow.isEmpty()) {

            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                AutoText(
                    text = stringResource(
                        R.string.no_consultations_found
                    ),
                    color = secondaryText
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(
                    items = listToShow,
                    key = { it.id }
                ) { consult ->

                    ConsultCard(
                        consult = consult,
                        cardColor = cardColor,
                        textColor = textColor,
                        secondaryText = secondaryText,
                        primaryColor = primaryColor,
                        onViewPrescriptionClick = { consultationId ->
                            navController.navigate(
                                Screen.PrescriptionScreen.createRoute(
                                    consultationId = consultationId
                                )
                            )
                        },
                        onBookAgainClick = {
                            navController.navigate(Screen.Consult.route)
                        },
                        onViewDetailClick = { consultationId ->

                            val consultation =
                                viewModel.getConsultationById(consultationId)

                            if (consultation != null) {

                                navController.currentBackStackEntry
                                    ?.savedStateHandle
                                    ?.set(
                                        "consultation",
                                        consultation
                                    )

                                navController.navigate(
                                    Screen.ConsultationDetail.route
                                )
                            }
                        }
                    )
                }

                item {
                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )
                }
            }
        }
    }
}