package com.mysanjeevni.mysanjeevni.features.prescription.presentation.screen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.components.CameraGallerySection
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.components.PrescriptionImagePreview
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.components.UploadGuidelinesCard
import com.mysanjeevni.mysanjeevni.features.prescription.presentation.viewmodel.PrescriptionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadPrescriptionScreen(
    navController: NavController,
    viewModel: PrescriptionViewModel = hiltViewModel()

) {

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Upload Prescription")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            null
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

            Text(
                text = "Upload Your Prescription",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Upload a valid doctor's prescription to order medicines.",
                color = MaterialTheme.colorScheme.onSurfaceVariant

            )
            Spacer(Modifier.height(20.dp))
            PrescriptionImagePreview(imageUri = selectedImageUri)
            Spacer(Modifier.height(20.dp))
            CameraGallerySection(
                onImageSelected = { uri ->
                    Log.d("PRESCRIPTION", "URI = $uri")
                    selectedImageUri = uri
                    viewModel.selectImage(uri)
                },
            )
            Spacer(Modifier.height(20.dp))
            UploadGuidelinesCard()
            Button(
                onClick = {
                    viewModel.uploadPrescription(context)
                },
                enabled = selectedImageUri != null && !state.isUploading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (state.isUploading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Upload Prescription")
                }
            }
            LaunchedEffect(state.isUploaded) {
                if (state.isUploaded) {
                    Toast.makeText(context, "Prescription sent!", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        }
    }
}