package com.mysanjeevni.mysanjeevni.features.medicines.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun MedicineHeader(medicine: Medicine) {
    AutoText(
        medicine.name,
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    )
    AutoText(
        "By ${medicine.vendorName}",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    AutoText(
        "${medicine.quantity} ${medicine.quantityUnit}",
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    AutoText(
        " ${medicine.rating} (${medicine.reviews} Reviews)"
    )
}