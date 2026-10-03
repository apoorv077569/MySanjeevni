package com.mysanjeevni.mysanjeevni.features.prescription.presentation.components

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun PrescriptionImagePreview(
    imageUri: Uri?
) {
    val context = LocalContext.current

    val mimeType = imageUri?.let {
        context.contentResolver.getType(it)
    }

    val fileName = imageUri?.let {
        getFileName(context, it)
    }

    val isPdf =
        mimeType == "application/pdf" ||
                fileName?.endsWith(
                    ".pdf",
                    ignoreCase = true
                ) == true

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when {
                imageUri == null -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(70.dp)
                        )

                        Spacer(Modifier.height(10.dp))

                        AutoText(
                            text = "No Prescription Selected",
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                isPdf -> {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.PictureAsPdf,
                            contentDescription = "PDF Prescription",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(72.dp)
                        )

                        Spacer(Modifier.height(12.dp))

                        AutoText(
                            text = "PDF Prescription",
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(Modifier.height(6.dp))

                        AutoText(
                            text = fileName ?: "prescription.pdf",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                else -> {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = "Prescription Preview",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }
        }
    }
}

private fun getFileName(
    context: Context,
    uri: Uri
): String? {

    context.contentResolver.query(
        uri,
        null,
        null,
        null,
        null
    )?.use { cursor ->

        val index = cursor.getColumnIndex(
            OpenableColumns.DISPLAY_NAME
        )

        if (index >= 0 && cursor.moveToFirst()) {
            return cursor.getString(index)
        }
    }

    return null
}