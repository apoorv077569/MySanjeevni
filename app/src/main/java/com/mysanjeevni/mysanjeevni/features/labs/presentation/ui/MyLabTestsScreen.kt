package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.LabFilterBottomSheet
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.LabTestViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

val Teal = Color(0xFF00897B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLabTestsScreen(
    navController: NavController,
    viewModel: LabTestViewModel
) {
    val labTests = viewModel.labTests.collectAsLazyPagingItems()
    val currentFilter by viewModel.filter.collectAsState()
    val isInitialLoading = labTests.loadState.refresh is LoadState.Loading
    var showFilterSheet by remember { mutableStateOf(false) }

//    val isDark = isSystemInDarkTheme()
    val isDark = LocalIsDarkTheme.current || isSystemInDarkTheme()
    val screenBg = if (isDark) Color(0xFF121212) else Color(0xFFF5F5F5)
    val textMuted = if (isDark) Color(0xFFAAAAAA) else Color(0xFF757575)

    if (isInitialLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator(
                    color = Teal,
                    strokeWidth = 3.dp
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                AutoText(
                    text = "Loading tests...",
                    fontSize = 13.sp,
                    color = textMuted
                )
            }
        }

    } else {

        Scaffold(
            topBar = {
                LabTestsTopBar(
                    onBack = {
                        navController.popBackStack()
                    },
                    onFilter = {
                        showFilterSheet = true
                    },
                    isFilterActive =
                        currentFilter.category != "All" ||
                                currentFilter.maxPrice < 10000f
                )
            },
            containerColor = screenBg
        ) { paddingValues ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
            ){
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = paddingValues.calculateTopPadding()
                        ),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {

                    items(count = labTests.itemCount) { index ->

                        labTests[index]?.let { test ->

                            LabTestCard(
                                test = test,
                                onViewDetails = { selectedTest ->
                                    navController.navigate(
                                        Screen.LabTestDetail.createRoute(
                                            selectedTest.id
                                        )
                                    )
                                }
                            )
                        }
                    }

                    when (labTests.loadState.append) {

                        is LoadState.Loading -> {

                            item {

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {

                                    CircularProgressIndicator(
                                        color = Teal,
                                        modifier = Modifier.size(28.dp),
                                        strokeWidth = 2.dp
                                    )
                                }
                            }
                        }

                        is LoadState.Error -> {

                            item {

                                AutoText(
                                    text = "Failed to load more",
                                    color = Color.Red,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        else -> Unit
                    }
                }
            }
        }
    }

    if (showFilterSheet) {
        LabFilterBottomSheet(
            currentFilter = currentFilter,
            onApply = { viewModel.applyFilter(it) },
            onDismiss = { showFilterSheet = false }
        )
    }
}

@Composable
fun LabTestsTopBar(
    onBack: () -> Unit,
    onFilter: () -> Unit,
    isFilterActive: Boolean = false
) {
//    val isDark = isSystemInDarkTheme()
    val isDark = LocalIsDarkTheme.current
    val barBg = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textPrimary = if (isDark) Color.White else Color(0xFF1A1A1A)
    val textMuted = if (isDark) Color(0xFFAAAAAA) else Color(0xFF757575)
    val filterInactiveBg = if (isDark) Color(0xFF1E1E1E) else Color.White

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(barBg)
            .padding(start = 4.dp, end = 16.dp, top = 8.dp, bottom = 12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onBack) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textPrimary)
                }
                AutoText("Lab Tests", fontWeight = FontWeight.Bold, fontSize = 22.sp, color = textPrimary)
            }

            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isFilterActive) Teal else filterInactiveBg)
                    .border(1.5.dp, Teal, RoundedCornerShape(10.dp))
                    .clickable { onFilter() }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = if (isFilterActive) Color.White else Teal,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                AutoText(
                    text = "Filter",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isFilterActive) Color.White else Teal
                )
            }
        }

        AutoText(
            text = "Choose the right test package for your health",
            fontSize = 13.sp,
            color = textMuted,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}