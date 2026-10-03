package com.mysanjeevni.mysanjeevni.features.pharmacy.domain.repository

import com.mysanjeevni.mysanjeevni.features.pharmacy.domain.model.MedicinePage


interface PharmacyRepository {
    suspend fun getMedicines(page:Int,limit:Int,category:String?=null): MedicinePage
    suspend fun getCategories(): List<String>


}