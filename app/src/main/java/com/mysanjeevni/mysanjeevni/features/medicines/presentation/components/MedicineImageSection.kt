package com.mysanjeevni.mysanjeevni.features.medicines.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun MedicineImageSection(
    image: String?,
    icon: String = "💊",
    discount: Double,
) {
    var showZoom by remember { mutableStateOf(false) }
    var scale by remember { mutableFloatStateOf(1f) }

    val maxScale = 5f
    var offsetX by remember { mutableFloatStateOf(0f) }
    var offsetY by remember { mutableFloatStateOf(0f) }
    val isMaxZoom = scale >= maxScale

    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Box {

            if (!image.isNullOrBlank()) {
                AsyncImage(
                    model = image,
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .pointerInput(scale) {
                            if (scale > 1f) {
                                detectDragGestures { change, dragAmount ->
                                    change.consume()
                                    offsetX += dragAmount.x
                                    offsetY += dragAmount.y
                                }
                            }
                        }
                        .graphicsLayer {
                            scaleX = scale
                            scaleY = scale
                            translationX = offsetX
                            translationY = offsetY
                        }
                )
            } else {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    AutoText(
                        text = icon.ifBlank { "💊" },
                        fontSize = 72.sp
                    )
                }
            }

            if (discount > 0) {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(
                            Color(0xFF00C853),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        )
                        .align(Alignment.TopStart)
                ) {
                    AutoText(
                        text = "${discount.toInt()}% OFF",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Box(
                modifier = Modifier
                    .padding(12.dp)
                    .size(36.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        CircleShape
                    )
                    .align(Alignment.BottomEnd)
                    .clickable {
                        if (!isMaxZoom) {
                            scale = (scale + 1f)
                                .coerceAtMost(maxScale)

                        } else {

                            scale = 1f

                            offsetX = 0f
                            offsetY = 0f
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector =
                        if (isMaxZoom)
                            Icons.Default.ZoomOut
                        else
                            Icons.Default.ZoomIn,

                    contentDescription = null,

                    tint = MaterialTheme.colorScheme.onSurface,

                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
    if (showZoom && !image.isNullOrBlank()) {
        ImageZoomDialog(
            imageUrl = image,
            onDismiss = { })
    }
}