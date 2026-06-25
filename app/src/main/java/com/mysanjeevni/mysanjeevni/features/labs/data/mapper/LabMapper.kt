package com.mysanjeevni.mysanjeevni.features.labs.data.mapper

import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabTestDetailDto
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.LabTestDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTest
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.LabTestDetail

fun LabTestDto.toDomain(): LabTest {

    return LabTest(
        id = id ?: "",
        name = name ?: "",
        description = description ?: "",
        price = price ?: 0,
        mrp = mrp ?: 0,
        category = category ?: "",
        rating = rating ?: 0.0,
        reportTime = reportTime ?: "",
        fasting = fasting ?: false,
        testCount = testsIncluded?.size ?: 0,
        icon = icon ?: "",
        homeCollectionAvailable = homeCollectionAvailable ?: false
    )
}

fun LabTestDetailDto.toDomain(): LabTestDetail {
    return LabTestDetail(
        id = id,
        name =name,
        description = description,
        price = price,
        mrp = mrp,
        category = category,
        rating = rating,
        reportTime = reportTime,
        sampleType = sampleType,
        fasting = fasting,
        fastingHour = fastingHour,
        homeCollectionAvailable = homeCollectionAvailable,
        testsIncluded = testsIncluded
    )
}

