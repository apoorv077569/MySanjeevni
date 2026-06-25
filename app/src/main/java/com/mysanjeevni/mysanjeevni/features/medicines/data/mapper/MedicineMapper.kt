package com.mysanjeevni.mysanjeevni.features.medicines.data.mapper

import com.mysanjeevni.mysanjeevni.features.medicines.data.dto.MedicineDto
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine

fun MedicineDto.toDomain(): Medicine {
    return Medicine(

        id = _id,

        name = name,

        description = description.orEmpty(),

        price = price,

        mrp = mrp,

        category = category,

        diseaseCategory = diseaseCategory.orEmpty(),

        diseaseSubcategory = diseaseSubcategory.orEmpty(),

        productType = productType,

        brand = brand.orEmpty(),

        stock = stock,

        quantity = quantity,

        quantityUnit = quantityUnit,

        image = image.orEmpty(),

        images = images.orEmpty(),

        specifications = specifications.orEmpty(),

        safetyInformation = safetyInformation.orEmpty(),

        requiresPrescription = requiresPrescription,

        vendorName = vendorName.orEmpty(),

        vendorRating = vendorRating ?: 0.0,

        rating = rating,

        reviews = reviews,
        icon = icon
    )
}