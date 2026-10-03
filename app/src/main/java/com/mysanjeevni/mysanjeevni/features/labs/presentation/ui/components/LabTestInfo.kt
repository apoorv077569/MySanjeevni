package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTest
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun LabTestInfo(
    modifier: Modifier = Modifier,
    test: LabTest
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        AutoText(
            text = test.name,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        CategoryChip(category = test.category)

        Spacer(modifier = Modifier.height(8.dp))

        TestCountPill(count = test.testCount)

        Column(
            modifier = Modifier.padding(top = 8.dp)
        ) {

            RatingPill(
                rating = test.rating
            )

            if (test.homeCollectionAvailable) {

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                HomeCollectionTag()
            }
        }
    }

}
