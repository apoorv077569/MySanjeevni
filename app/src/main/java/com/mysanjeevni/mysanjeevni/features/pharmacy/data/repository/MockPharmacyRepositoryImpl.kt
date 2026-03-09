package com.mysanjeevni.mysanjeevni.features.pharmacy.data.repository

import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository.PharmacyRepository
import kotlinx.coroutines.delay
import javax.inject.Inject

class MockPharmacyRepositoryImpl @Inject constructor() : PharmacyRepository {
    override suspend fun getMedicines(): List<Medicine> {
        delay(500) // Simulate network delay

        return listOf(
            Medicine(1, "M2-Tone Syrup", 299.0, "Comprehensive formulation for menstrual disorders", R.drawable.m2_tone),
            Medicine(2, "Neeri", 399.0, "Natural kidney stone treatment", R.drawable.neeri),
            Medicine(3, "Panchmeena", 499.0, "Digestive health tonic", R.drawable.panchmeena),
            Medicine(4, "Omeo Cough", 299.0, "Effective relief for cough and cold", R.drawable.omeo_cough),
            Medicine(5, "PatharChur Juice", 199.0, "Herbal juice for kidney stones", R.drawable.patharchur_juice),
            Medicine(6, "Sankh Pushpi", 249.0, "Memory booster and brain tonic", R.drawable.sankh_pushpi),
            Medicine(7, "Shila Jeet", 59.0, "Strength and stamina booster", R.drawable.shilajeet),
            Medicine(8, "Pulsatilla Compositum", 249.0, "Homeopathic remedy for women", R.drawable.pulsatilla_compositum),
            Medicine(9, "Spondin", 99.0, "Relief from spondylitis pain", R.drawable.spondin),
            Medicine(10, "Sundari Jeevak", 299.0, " uterine tonic for women", R.drawable.sundari_jeevak),
            Medicine(11, "Turmeric Juice", 199.0, "Immunity booster with Curcumin", R.drawable.turmeric_juice),
            Medicine(12, "Tomic", 99.0, "General health syrup", R.drawable.tomic),
            Medicine(13, "Alfalfa Tonic", 399.0, "Appetite and energy booster", R.drawable.alfalfa_tonic),
            Medicine(14, "Multani Pachmeena", 99.0, "Ayurvedic digestive aid", R.drawable.multani_pachmeena),
            Medicine(15, "Maha Bhringraj", 159.0, "Oil for hair growth and scalp", R.drawable.mahabhringraj),
            Medicine(16, "Women's Wellness", 299.0, "Complete care for women's health", R.drawable.womens_wellness),
            Medicine(17, "Aloevera Juice", 199.0, "Detox and skin health", R.drawable.aloevera),
            Medicine(18, "AmlyCure DS", 299.0, "Liver corrective and protective", R.drawable.amlycure_ds),
            Medicine(19, "Apple Juice", 99.0, "Fresh and organic fruit juice", R.drawable.apple_juice),
            Medicine(20, "Dabur Triphala", 299.0, "Colon cleanser and digestion aid", R.drawable.dabur_triphala),

            // I fixed the names below based on your image resources
            Medicine(21, "Jondila Sugar Free", 299.0, "Liver tonic for diabetics", R.drawable.jondila),
            Medicine(22, "M2-Tone Syrup (Pack of 2)", 599.0, "Value pack for long term care", R.drawable.m2_tone),
            Medicine(23, "M2-Tone Forte", 350.0, "Advanced formula", R.drawable.m2_tone)
        )
    }
}