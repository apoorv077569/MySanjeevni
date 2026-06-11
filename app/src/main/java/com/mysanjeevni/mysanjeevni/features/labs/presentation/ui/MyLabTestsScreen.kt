package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.LabTestViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyLabTestsScreen(
    navController: NavController,
    viewModel: LabTestViewModel = hiltViewModel()
) {

    val labTests = viewModel.labTests.collectAsLazyPagingItems()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lab Tests",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF1A1A1A)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color(0xFF1A1A1A)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        LazyColumn(modifier = Modifier.padding(paddingValues)) {

            items(
                count = labTests.itemCount
            ) { index ->

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
                        CircularProgressIndicator()
                    }
                }

                is LoadState.Error -> {
                    item {
                        Text("Failed to load more")
                    }
                }

                else -> {}
            }
        }
    }
}