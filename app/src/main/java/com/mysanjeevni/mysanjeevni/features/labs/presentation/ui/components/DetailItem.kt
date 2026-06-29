package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun DetailItem(
    title: String,
    value: String
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {

        AutoText(
            text = title,
            style = MaterialTheme.typography.labelMedium
        )

        Spacer(
            modifier = Modifier.height(4.dp)
        )

        AutoText(
            text = value,
            fontWeight = FontWeight.SemiBold
        )

        HorizontalDivider(
            modifier = Modifier.padding(top = 12.dp)
        )
    }
}