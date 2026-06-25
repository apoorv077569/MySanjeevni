package com.mysanjeevni.mysanjeevni.features.prescription.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun UploadGuidelinesCard() {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = "Guidelines",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(10.dp))

            Text(
                "✓ JPG, PNG supported",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "✓ Maximum file size 5 MB",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "✓ Upload a clear prescription image",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                "✓ Doctor details should be visible",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}