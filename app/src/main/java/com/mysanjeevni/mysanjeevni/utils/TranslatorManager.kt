package com.mysanjeevni.mysanjeevni.utils

import com.google.mlkit.common.model.DownloadConditions
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions

object TranslatorManager {
    private var translator: Translator?= null

    fun translate(text:String,targetLanguageCode: String,onResult:(String) -> Unit){
        val options = TranslatorOptions.Builder()
            .setSourceLanguage(TranslateLanguage.ENGLISH)
            .setTargetLanguage(targetLanguageCode)
            .build()

        val conditions = DownloadConditions.Builder()
            .requireWifi()
            .build()
        val translator = Translation.getClient(options)

        translator.downloadModelIfNeeded(conditions)
            .addOnSuccessListener {
                translator.translate(text)
                    .addOnSuccessListener { translateText ->
                        onResult(translateText)
                    }.addOnFailureListener{onResult(text)}
            }.addOnFailureListener {
                onResult(text)
            }
    }
}