package com.mysanjeevni.mysanjeevni.features.pharmacy.domain.model

import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine

data class MedicinePage(
    val medicines:List<Medicine>,
    val total :Int,
    val totalPages:Int
)
