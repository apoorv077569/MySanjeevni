package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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

    val cardColor =
        if (isDark) Color(0xFF1E1E1E)
        else Color.White

    val textColor =
        if (isDark) Color.White
        else Color.Black

    val secondaryText =
        if (isDark) Color.LightGray
        else Color.Gray

    val primaryColor = MaterialTheme.colorScheme.primary

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .statusBarsPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = textColor,
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            AutoText(
                text = stringResource(R.string.my_consultations),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }

        // -----------------------------------------------------
        // Tabs
        // -----------------------------------------------------

        TabRow(
            selectedTabIndex = state.selectedTab,
            containerColor = cardColor,
            contentColor = primaryColor,
            modifier = Modifier.padding(horizontal = 16.dp),

            indicator = { tabPositions ->

                if (state.selectedTab < tabPositions.size) {

                    Box(
                        modifier = Modifier
                            .tabIndicatorOffset(
                                tabPositions[state.selectedTab]
                            )
                            .padding(
                                horizontal = 4.dp,
                                vertical = 4.dp
                            )
                            .fillMaxHeight()
                            .background(
                                color = primaryColor,
                                shape = RoundedCornerShape(10.dp)
                            )
                    )
                }
            },

            divider = {}
        ) {

            // -----------------------------------------------------
            // Upcoming
            // -----------------------------------------------------

            Tab(
                selected = state.selectedTab == 0,

                onClick = {
                    viewModel.onTabSelected(0)
                },

                modifier = Modifier
                    .height(48.dp)
                    .zIndex(1f)
            ) {

                AutoText(
                    text = stringResource(R.string.upcoming),
                    fontSize = 14.sp,
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

            // -----------------------------------------------------
            // Past Consults
            // -----------------------------------------------------

            Tab(
                selected = state.selectedTab == 1,

                onClick = {
                    viewModel.onTabSelected(1)
                },

                modifier = Modifier
                    .height(48.dp)
                    .zIndex(1f)
            ) {

                AutoText(
                    text = stringResource(R.string.past_consults),
                    fontSize = 14.sp,
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