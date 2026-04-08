package com.mysanjeevni.mysanjeevni.features.home.presentation.util

import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.HomeCategoryItem
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.HomeCategorySection
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.HomeCategoryType

object HomeCategoryMock {

    fun getSections(): List<HomeCategorySection> {
        return listOf(
            // Section 1: Ayurvedic
            HomeCategorySection(
                id = "ayurvedic_section",
                title = "Popular in Ayurvedic",
                items = listOf(
                    HomeCategoryItem(
                        id = "1",
                        title = "Ashoka Rishta",
                        imageUrl = R.drawable.ashoka_rishta,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "2",
                        title = "Dashmularish",
                        imageUrl = R.drawable.dashmularisht,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "3",
                        title = "Ashwagandha Tablets",
                        imageUrl = R.drawable.ashwagandha_tablet,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "4",
                        title = "ChyawanPrash",
                        imageUrl = R.drawable.chyawanprash,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "5",
                        title = "AmlyCure",
                        imageUrl = R.drawable.amlycure,
                        type = HomeCategoryType.MEDICINES
                    ),

                    HomeCategoryItem(
                        id = "6",
                        title = "Triphala Powder",
                        imageUrl = R.drawable.triphala_powder,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "7",
                        title = "NEERI",
                        imageUrl = R.drawable.neeri,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "8",
                        title = "Amla Juice",
                        imageUrl = R.drawable.amla_juice,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "9",
                        title = "Pancharishta",
                        imageUrl = R.drawable.pancharishta,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "10",
                        title = "Shilajit",
                        imageUrl = R.drawable.shilajit,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "11",
                        title = "Basant Kusumakar Ras",
                        imageUrl = R.drawable.basant_kusumakar_ras,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "12",
                        title = "Dia Card",
                        imageUrl = R.drawable.diacard,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "13",
                        title = "Prostonum",
                        imageUrl = R.drawable.prostonum,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "14",
                        title = "Dibonil",
                        imageUrl = R.drawable.dibonil,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "15",
                        title = "Berb Vulg",
                        imageUrl = R.drawable.berb_vulg,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "16",
                        title = "Alpha MP",
                        imageUrl = R.drawable.alpha_mp,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "17",
                        title = "Jondila",
                        imageUrl = R.drawable.jondila,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "18",
                        title = "Vita C Forte 15",
                        imageUrl = R.drawable.vita_c_forte_15,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "19",
                        title = "Vita C Forte",
                        imageUrl = R.drawable.vita_c_forete,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "20",
                        title = "Koffeez",
                        imageUrl = R.drawable.koffeez,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "21",
                        title = "Kali Phos",
                        imageUrl = R.drawable.kali_phos,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "22",
                        title = "Tranquil",
                        imageUrl = R.drawable.tranquil,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "23",
                        title = "Guatteria Guameri",
                        imageUrl = R.drawable.gautteria_guameri,
                        type = HomeCategoryType.MEDICINES
                    ),

                ),
            ),

            // Section 2: Personal Care
            HomeCategorySection(
                id = "personal_care",
                title = "Personal Care Bestsellers",
                items = listOf(
                    HomeCategoryItem(
                        id = "p1",
                        title = "Skin Care",
                        imageUrl = R.drawable.skin_care,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "p2",
                        title = "Hair Oil",
                        imageUrl = R.drawable.hair_oil,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "p3",
                        title = "Oral Care",
                        imageUrl = R.drawable.oral_care,
                        type = HomeCategoryType.MEDICINES
                    )
                )
            ),

            // Section 3: Devices
            HomeCategorySection(
                id = "devices_section",
                title = "Health Devices",
                items = listOf(
                    HomeCategoryItem(
                        id = "d1",
                        title = "Thermometer",
                        imageUrl = R.drawable.thermometer,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "d2",
                        title = "BP Monitor",
                        R.drawable.bp_monitor,
                        type = HomeCategoryType.MEDICINES
                    ),
                    HomeCategoryItem(
                        id = "d3",
                        title = "Oximeter",
                        imageUrl = R.drawable.oximeter,
                        type = HomeCategoryType.MEDICINES
                    )
                )
            )
        )
    }
}