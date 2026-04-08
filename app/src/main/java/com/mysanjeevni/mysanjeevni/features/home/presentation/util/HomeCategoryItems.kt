package com.mysanjeevni.mysanjeevni.features.home.presentation.util

import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.HomeCategoryType
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.HomeCategoryItem

object HomeCategoryItems {

    fun getItems(category: HomeCategoryType): List<HomeCategoryItem> {

        val allItems = listOf(
            // Medicines
            HomeCategoryItem("1", "Heart Health", R.drawable.heart_health, HomeCategoryType.MEDICINES),


            HomeCategoryItem("8", "Pet Supplements", R.drawable.pet_suppliments, HomeCategoryType.PETCARE),

            // Eyewear
            HomeCategoryItem("9", "Sunglasses", R.drawable.sun_glasses, HomeCategoryType.EYEWEAR)
        )

        return if (category == HomeCategoryType.ALL) {
            allItems
        } else {
            allItems.filter { it.type == category }
        }
    }
}