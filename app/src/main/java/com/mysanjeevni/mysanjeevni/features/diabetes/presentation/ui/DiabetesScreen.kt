package com.mysanjeevni.mysanjeevni.features.diabetes.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mysanjeevni.mysanjeevni.features.category.presenttion.data.diabetesCareList
import com.mysanjeevni.mysanjeevni.features.category.presenttion.data.diabetesDevicesList
import com.mysanjeevni.mysanjeevni.features.category.presenttion.data.sugarControlList
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.ui.HorizontalList
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.ui.SectionTitle

@Composable
fun DiabetesScreen() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF90CAF9))
    ) {

        SectionTitle("Diabetes Care")
        HorizontalList(diabetesCareList)

        SectionTitle("Sugar Control")
        HorizontalList(sugarControlList)

        SectionTitle("Monitoring Devices")
        HorizontalList(diabetesDevicesList)
    }
}