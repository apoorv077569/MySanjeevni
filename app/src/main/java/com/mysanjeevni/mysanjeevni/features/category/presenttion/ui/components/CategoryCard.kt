package com.mysanjeevni.mysanjeevni.features.category.presenttion.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mysanjeevni.mysanjeevni.features.category.domain.model.Category

@Composable
fun CategoryCard(
    category: Category
) {

    Card(
        modifier = Modifier.width(120.dp)
    ) {

        Column(
            modifier = Modifier.padding(12.dp)
        ) {

            Text(
                text = category.name
            )

        }
    }
}