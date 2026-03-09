package com.mysanjeevni.mysanjeevni.features.home.presentation.util

import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.home.presentation.model.FeaturedMedicine

object HomeMockData {
    fun getMedicines(): List<FeaturedMedicine> {
        return listOf(
            FeaturedMedicine(1, "M2-Tone Syrup", "₹299", R.drawable.m2_tone),
            FeaturedMedicine(  2,"Neeri","₹399",R.drawable.neeri),
            FeaturedMedicine(  3,"Panchmeena","₹499",R.drawable.panchmeena),
            FeaturedMedicine(  4,"Omeo Cough","₹299",R.drawable.omeo_cough),
            FeaturedMedicine(  5,"PatharChur Juice","₹199",R.drawable.patharchur_juice),
            FeaturedMedicine(  6,"Sankh Pushpi","₹249",R.drawable.sankh_pushpi),
            FeaturedMedicine(  7,"Shila Jeet","₹59",R.drawable.shilajeet),
            FeaturedMedicine(  8,"Pulsatilla Compositum","₹249",R.drawable.pulsatilla_compositum),
            FeaturedMedicine(  9,"Spondin","₹99",R.drawable.spondin),
            FeaturedMedicine(  10,"Sundari Jeevak","₹299",R.drawable.sundari_jeevak),
            FeaturedMedicine(  11,"Turmeric Juice","₹199",R.drawable.turmeric_juice),
            FeaturedMedicine(  12,"Tomic","₹99",R.drawable.tomic),
            FeaturedMedicine(  13,"Alfalfa Tonic","₹399",R.drawable.alfalfa_tonic),
            FeaturedMedicine(  14,"Multani Pachmeena","₹99",R.drawable.multani_pachmeena),
            FeaturedMedicine(  15,"Maha Bhringraj","₹159",R.drawable.mahabhringraj),
            FeaturedMedicine(  16,"Women's Wellness","₹299",R.drawable.womens_wellness),
            FeaturedMedicine(  17,"Aloevera","₹199",R.drawable.aloevera),
            FeaturedMedicine(18,"AmlyCure DS","₹299",R.drawable.amlycure_ds),
            FeaturedMedicine(19,"Apple Juice","₹99",R.drawable.apple_juice),
            FeaturedMedicine(20,"Dabur Triphala","₹299",R.drawable.dabur_triphala),
            FeaturedMedicine(21,"M2-Tone Syrup","₹299",R.drawable.jondila),
            FeaturedMedicine(22,"M2-Tone Syrup","₹299",R.drawable.m2_tone),
            FeaturedMedicine(23,"M2-Tone Syrup","₹299",R.drawable.m2_tone),
        )
    }
}