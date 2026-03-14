package com.mysanjeevni.mysanjeevni.features.referral.presentation.viewmodel

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mysanjeevni.mysanjeevni.features.referral.presentation.state.ReferralItem
import com.mysanjeevni.mysanjeevni.features.referral.presentation.state.ReferralState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReferralViewModel: ViewModel() {
    private val _state = MutableStateFlow(ReferralState())
    val state: StateFlow<ReferralState> = _state.asStateFlow()

    init {
        loadReferralDate()
    }

    private fun loadReferralDate() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            delay(800)

            val history = listOf(
                ReferralItem(
                    "1",
                    "Rahul Sharma",
                    "20 Oct 2023",
                    500.0,
                    "Success"
                ),
                ReferralItem(
                    "2",
                    "Priya Verma",
                    "18 Oct 2023",
                    0.0,
                    "Pending"
                ),
                ReferralItem(
                    "3",
                    "Amit Singh",
                    "15 Sep 2023",
                    450.0,
                    "Success"
                ),
            )
            _state.update {
                it.copy(
                    isLoading = false,
                    referralCode = "SANJEEVANI2026",
                    totalEarned = 950.00,
                    referralHistory = history
                )
            }
        }
    }
    fun copyCodeToClipboard(context: Context){
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Referral Code",_state.value.referralCode)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context,"Code Copied to clipboard", Toast.LENGTH_SHORT).show()
    }
    fun shareReferral(context: Context){
        val code = _state.value.referralCode
        val sendIntent:Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT,"Hey! Join MySanjeevni using my code \$code and get ₹500 off on your first order. Download now: https://mysanjeevni.com/app")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent,"Share Referral Code")
        context.startActivity(shareIntent)
    }

}