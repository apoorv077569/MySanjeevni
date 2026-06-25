package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.LabDetailScreen
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.LabDetailsViewModel

@Composable
fun LabDetailRoute(
    navController: NavController,
    viewModel: LabDetailsViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsState()

    LabDetailScreen(
        navController = navController,
        state = state,
        onAddToCart = {
            val test = state.labTestDetail ?: return@LabDetailScreen

            navController.navigate(
                "book_lab_test/" +
                        "${test.id}/" +
                        "${test.name}/" +
                        "${test.price}"
            )
        },
    )
}