package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTest
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.CategoryIconBox
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.LabTestInfo
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.LabTestPricePanel


@Composable
fun LabTestCard(
    test: LabTest,
    onViewDetails: (LabTest) -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            CategoryIconBox(
                icon = test.icon,
                category = test.category
            )

            Spacer(Modifier.width(12.dp))
            LabTestInfo(
                modifier = Modifier.weight(1f),
                test = test
            )
            Spacer(Modifier.width(8.dp))

            LabTestPricePanel(
                test = test,
                onViewDetails = {
                    onViewDetails(test)
                }
            )
        }
    }

}
