package com.mysanjeevni.mysanjeevni.features.pharmacy.data.repository

import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository.PharmacyRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockPharmacyRepositoryImpl @Inject constructor() : PharmacyRepository {
    override suspend fun getMedicines(): List<Medicine> {
        delay(1000)

        return listOf(
            Medicine(
                1,
                "Dabur Triphala Tablet",
                24.28,
                "Ayurvedic Tablet for healthy gut and overall wellness",
                R.drawable.dabur_triphala // replace with original url later
            ), Medicine(
                2,
                "Mulatni Pachmeena",
                32.58,
                "Enhance the function of digestive enzymes,offering relief from gas,bloating,and abdominal discomfort",
                R.drawable.multani_pachmeena // replace with original url later
            ), Medicine(
                3,
                "Mahabhringaraj Oil",
                41.429,
                "Hair oil to prevent hair fall and prevents premature greying of hair and helps in strengthen of hair" + " from the roots",
                R.drawable.mahabhringraj // replace with original url later
            )
        )
    }
}