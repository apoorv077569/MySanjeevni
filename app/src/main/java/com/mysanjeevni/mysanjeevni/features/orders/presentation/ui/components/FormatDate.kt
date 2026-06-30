package com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components

import java.text.SimpleDateFormat
import java.util.Locale
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme

fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        outputFormat.format(date ?: return dateString)
    } catch (e: Exception) {
        dateString
    }
}
data class StatusColorSet(
    val container: Color,
    val content: Color
)

object StatusColors {
    @Composable
    fun forOrderStatus(status: String): StatusColorSet {
//        val isDark = isSystemInDarkTheme()
        val isDark = LocalIsDarkTheme.current

        return when (status.lowercase()) {
            "delivered" -> if (isDark)
                StatusColorSet(Color(0xFF1B3A1F), Color(0xFF81C784))
            else
                StatusColorSet(Color(0xFFE8F5E9), Color(0xFF2E7D32))
            "cancelled" -> if (isDark)
                StatusColorSet(Color(0xFF3A1B1B), Color(0xFFE57373))
            else
                StatusColorSet(Color(0xFFFFEBEE), Color(0xFFC62828))
            "processing", "shipped", "out for delivery" -> if (isDark)
                StatusColorSet(Color(0xFF3A2E0E), Color(0xFFFFD54F))
            else
                StatusColorSet(Color(0xFFFFF8E1), Color(0xFFF57F17))
            "pending" -> if (isDark)
                StatusColorSet(Color(0xFF3A2A0E), Color(0xFFFFB74D))
            else
                StatusColorSet(Color(0xFFFFF3E0), Color(0xFFEF6C00))
            else -> if (isDark)
                StatusColorSet(Color(0xFF12283A), Color(0xFF64B5F6))
            else
                StatusColorSet(Color(0xFFE3F2FD), Color(0xFF1565C0))
        }
    }
    @Composable
    fun forPaymentStatus(status: String): StatusColorSet {
//        val isDark = isSystemInDarkTheme()
        val isDark = LocalIsDarkTheme.current

        return when (status.lowercase()) {
            "paid" -> if (isDark)
                StatusColorSet(Color(0xFF1B3A1F), Color(0xFF81C784))
            else
                StatusColorSet(Color(0xFFE8F5E9), Color(0xFF2E7D32))
            "pending" -> if (isDark)
                StatusColorSet(Color(0xFF3A2A0E), Color(0xFFFFB74D))
            else
                StatusColorSet(Color(0xFFFFF3E0), Color(0xFFEF6C00))
            "failed" -> if (isDark)
                StatusColorSet(Color(0xFF3A1B1B), Color(0xFFE57373))
            else
                StatusColorSet(Color(0xFFFFEBEE), Color(0xFFC62828))
            else -> if (isDark)
                StatusColorSet(Color(0xFF2C2C2C), Color(0xFFBDBDBD))
            else
                StatusColorSet(Color(0xFFF5F5F5), Color(0xFF616161))
        }
    }
}