package com.mysanjeevni.mysanjeevni.features.prescription.presentation.ui.screen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.components.CameraGallerySection
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.components.PrescriptionImagePreview
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.components.UploadGuidelinesCard
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.viewmodel.PrescriptionViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadPrescriptionScreen(
    navController: NavController,
    productId: String,
    productName: String,
    userId: String,
    viewModel: PrescriptionViewModel = hiltViewModel()
) {

    var selectedFileUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.d("PRESCRIPTION_UI", "Screen opened")
        Log.d("PRESCRIPTION_UI", "Product ID: $productId")
        Log.d("PRESCRIPTION_UI", "Product Name: $productName")
        Log.d("PRESCRIPTION_UI", "User ID: $userId")
    }

    LaunchedEffect(state.isSuccess) {

        if (state.isSuccess) {

            Log.d(
                "PRESCRIPTION_UI",
                "Upload successful"
            )

            Log.d(
                "PRESCRIPTION_UI",
                "URL: ${state.data?.prescriptionUrl}"
            )

            Toast.makeText(
                context,
                "Prescription uploaded successfully!",
                Toast.LENGTH_SHORT
            ).show()

            // Reset prescription upload state
            viewModel.resetState()

            // Go to Address Screen
            navController.navigate(
                "${Screen.ManageAddresses.route}?checkout=true&home=false"
            ) {
                // Upload screen back stack se remove
                popUpTo(Screen.UploadPrescription.route) {
                    inclusive = true
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AutoText("Upload Prescription")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AutoText(
                text = "Upload Your Prescription",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Upload a valid doctor's prescription to order medicines.",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            PrescriptionImagePreview(
                imageUri = selectedFileUri
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            CameraGallerySection(
                onFileSelected = { uri ->

                    Log.d(
                        "PRESCRIPTION_UI",
                        "Selected file URI: $uri"
                    )

                    selectedFileUri = uri
                }
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            UploadGuidelinesCard()

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = {

                    // SAME STATE USED FOR UPLOAD
                    val uri = selectedFileUri

                    if (uri == null) {

                        Toast.makeText(
                            context,
                            "Please select prescription",
                            Toast.LENGTH_SHORT
                        ).show()

                        return@Button
                    }

                    Log.d(
                        "PRESCRIPTION_UI",
                        "Upload button clicked"
                    )

                    Log.d(
                        "PRESCRIPTION_UI",
                        "Uploading URI: $uri"
                    )

                    viewModel.uploadPrescription(
                        context = context,
                        fileUri = uri,
                        productId = productId,
                        productName = productName,
                        userId = userId
                    )
                },

                // SAME STATE USED FOR ENABLE/DISABLE
                enabled =
                    selectedFileUri != null &&
                            !state.isLoading,

                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                shape = RoundedCornerShape(14.dp)
            ) {

                if (state.isLoading) {

                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )

                } else {

                    AutoText(
                        text = "Upload Prescription"
                    )
                }
            }
            state.error?.let { error ->
                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                AutoText(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }

}