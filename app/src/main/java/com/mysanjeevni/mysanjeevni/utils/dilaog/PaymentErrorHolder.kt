package com.mysanjeevni.mysanjeevni.utils.dilaog

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

object PaymentErrorHolder {

    var errorMessage by mutableStateOf<String?>(null)

}