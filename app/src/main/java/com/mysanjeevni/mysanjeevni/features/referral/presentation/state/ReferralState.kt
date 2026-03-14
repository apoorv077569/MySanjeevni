package com.mysanjeevni.mysanjeevni.features.referral.presentation.state

data class ReferralItem(
    val id:String,
    val friendName:String,
    val date:String,
    val amountEarned: Double,
    val status: String
)

data class ReferralState(
    val isLoading: Boolean = false,
    val referralCode:String="",
    val totalEarned: Double = 0.0,
    val rewardDescription: String = "Invite your friends and earn ₹500 for every successful referral!",
    val referralHistory:List<ReferralItem> = emptyList()
)
