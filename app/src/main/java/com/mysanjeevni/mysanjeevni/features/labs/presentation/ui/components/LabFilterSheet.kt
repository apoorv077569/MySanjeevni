package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mysanjeevni.mysanjeevni.features.currency.presentation.viewmodel.CurrencyViewModel
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabFilterState
import com.mysanjeevni.mysanjeevni.utils.AutoText

val CATEGORIES = listOf("All", "General", "Thyroid", "Diabetes", "Cardiac", "Vitamin", "Liver")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabFilterBottomSheet(
    currentFilter: LabFilterState,
    onApply: (LabFilterState) -> Unit,
    onDismiss: () -> Unit,
    currencyViewModel: CurrencyViewModel = hiltViewModel()
) {
    var selectedCategory by remember { mutableStateOf(currentFilter.category) }
    var priceRange by remember { mutableStateOf(currentFilter.maxPrice) }
    val colors = MaterialTheme.colorScheme

    val currencyState by currencyViewModel.state.collectAsStateWithLifecycle()

    // Fallback to INR (rate = 1.0) while loading or if currency fetch failed
    val currencySymbol = currencyState.currencyInfo?.currencySymbol ?: "₹"
    val exchangeRate = currencyState.currencyInfo?.exchangeRate ?: 1.0

    // priceRange itself stays in INR (used for actual filtering against backend data),
    // only the displayed labels are converted for the user's local currency
    fun formatConverted(inrValue: Float): String {
        val converted = inrValue * exchangeRate
        return "$currencySymbol${
            String.format(
                java.util.Locale.getDefault(),
                if (exchangeRate == 1.0) "%.0f" else "%.2f",
                converted
            )
        }"
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = colors.surface,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 32.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AutoText("Filter Tests", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = colors.onSurface)
                TextButton(onClick = {
                    selectedCategory = "All"
                    priceRange = 10000f
                }) {
                    AutoText("Reset", color = Teal, fontSize = 13.sp)
                }
            }

            Spacer(Modifier.height(20.dp))

            // Category
            AutoText("Category", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.onSurface)
            Spacer(Modifier.height(10.dp))
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CATEGORIES.forEach { cat ->
                    val isSelected = selectedCategory == cat
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(if (isSelected) Teal else colors.surface)
                            .border(1.5.dp, if (isSelected) Teal else colors.outline.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                            .clickable { selectedCategory = cat }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        AutoText(
                            text = cat,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isSelected) Color.White else colors.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Price Range
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AutoText("Max Price", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.onSurface)
                AutoText(formatConverted(priceRange), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = colors.surfaceVariant)
            }
            Spacer(Modifier.height(8.dp))
            Slider(
                value = priceRange,
                onValueChange = { priceRange = it },
                valueRange = 100f..10000f,
                steps = 0,
                colors = SliderDefaults.colors(
                    thumbColor = Teal,
                    activeTrackColor = Teal,
                    inactiveTrackColor =
                        colors.surfaceVariant                )
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                AutoText(formatConverted(100f), fontSize = 11.sp, color = Color(0xFF9E9E9E))
                AutoText(formatConverted(10000f), fontSize = 11.sp, color = Color(0xFF9E9E9E))
            }

            Spacer(Modifier.height(28.dp))

            // Apply Button
            Button(
                onClick = {
                    onApply(
                        LabFilterState(
                            category = selectedCategory,
                            maxPrice = priceRange
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Teal)
            ) {
                AutoText("Apply Filters", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White)
            }
        }
    }
}