package com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.ui.components.MedicineItem
import com.mysanjeevni.mysanjeevni.features.pharmacy.presentation.viewmodel.PharmacyViewModel

@Composable
fun PharmacyScreen(
    viewModel: PharmacyViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState().value

    Box(modifier = Modifier.fillMaxSize()) {
        // Success State - Show List
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(state.medicines) { medicine ->
                MedicineItem(medicine = medicine)
            }
        }
        // Loading State - Show Spinner
        if (state.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
        // Error State - Show Text
        if (state.error.isNotBlank()) {
            Text(
                text = state.error,
                color = Color.Red,
                modifier = Modifier.align(Alignment.Center)
            )
        }

    }
}
