package com.mysanjeevni.mysanjeevni.features.prescription.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mysanjeevni.mysanjeevni.utils.AutoText

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

            AutoText(
                text = "Guidelines",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(Modifier.height(10.dp))

            AutoText(
                "✓ JPG, PNG supported",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AutoText(
                "✓ Maximum file size 5 MB",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AutoText(
                "✓ Upload a clear prescription image",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            AutoText(
                "✓ Doctor details should be visible",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}