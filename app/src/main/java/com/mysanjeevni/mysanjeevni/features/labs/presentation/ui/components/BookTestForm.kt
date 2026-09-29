package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mysanjeevni.mysanjeevni.features.currency.presentation.viewmodel.CurrencyViewModel
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabAvailabilityState
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.DatePickerField
import com.mysanjeevni.mysanjeevni.utils.TimeSlotDropdown


@Composable
fun BookTestForm(
    modifier: Modifier = Modifier,
    testName: String,
    testPrice: Double,
    testId: String,
    collectionType: String,
    onCollectionTypeChange: (String) -> Unit,
    collectionDate: String,
    onCollectionDateChange: (String) -> Unit,
    collectionTime: String,
    onCollectionTimeChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    pincode: String,
    onPincodeChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    gender: String,
    onGenderChange: (String) -> Unit,
    instructions: String,
    onInstructionsChange: (String) -> Unit,
    availabilityState: LabAvailabilityState,
    showValidationError: String?,
    onBackClick: () -> Unit,
    onPayClick: () -> Unit,
    isLoading: Boolean = false,
    currencyViewModel: CurrencyViewModel = hiltViewModel(),
) {
    val colorScheme = MaterialTheme.colorScheme
    val isServiceBlocked = availabilityState.serviceability?.isServiceable == false
    val isButtonDisabled = isServiceBlocked || isLoading

    val currencyState by currencyViewModel.state.collectAsState()

    // Fallback to INR (rate = 1.0) while loading or if currency fetch failed
    val currencySymbol = currencyState.currencyInfo?.currencySymbol ?: "₹"
    val exchangeRate = currencyState.currencyInfo?.exchangeRate ?: 1.0

    val convertedTestPrice = testPrice * exchangeRate
    val formattedTestPrice = "$currencySymbol${
        String.format(
            java.util.Locale.getDefault(),
            if (exchangeRate == 1.0) "%.0f" else "%.2f",
            convertedTestPrice
        )
    }"

    Column(
        modifier = modifier
            .background(colorScheme.background)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        SectionCard {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                IconBadge(icon = Icons.Default.LocalShipping)
                Spacer(Modifier.width(12.dp))
                AutoText(
                    "Collection Type",
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CollectionTypeOption(
                    label = "Home Collection",
                    subtitle = "We collect your sample",
                    selected = collectionType == "Home Collection",
                    modifier = Modifier.weight(1f),
                    onClick = { onCollectionTypeChange("Home Collection") }
                )
                CollectionTypeOption(
                    label = "Visit Centre",
                    subtitle = "Visit our lab centre",
                    selected = collectionType == "Visit Centre",
                    modifier = Modifier.weight(1f),
                    onClick = { onCollectionTypeChange("Visit Centre") }
                )
            }
        }

        // ── Test Info Card ────────────────────────────────────────────────
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(colorScheme.error),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Science,
                        contentDescription = null,
                        tint = colorScheme.onError,
                        modifier = Modifier.size(26.dp)
                    )
                }
                Spacer(Modifier.width(14.dp))
                Column {
                    AutoText(
                        text = testName,
                        color = colorScheme.onSurface,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    AutoText(
                        text = formattedTestPrice,
                        color = colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }

        // ── Collection Date ───────────────────────────────────────────────
        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(icon = Icons.Default.CalendarMonth)
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    DatePickerField(
                        selectedDate = collectionDate,
                        onDateSelected = onCollectionDateChange
                    )
                }

            }
        }

        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(icon = Icons.Default.Schedule)
                Spacer(Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    val slotLabels =
                        availabilityState.slotsResult?.slots?.map { it.label } ?: emptyList()
                    TimeSlotDropdown(
                        selectedTime = collectionTime,
                        onTimeSelected = onCollectionTimeChange,
                        timeSlots = slotLabels,
                        enabled = !availabilityState.isLoadingSlots
                    )
                }
            }
            when {
                availabilityState.isLoadingSlots -> StatusText("Fetching available slots...")
                availabilityState.slotsError != null ->
                    StatusText(availabilityState.slotsError ?: "Unable to fetch slots", isError = true)
            }
        }

        if (collectionType == "Home Collection") {
            DarkTextField(
                value = address,
                onValueChange = onAddressChange,
                placeholder = "Enter your home address",
                icon = Icons.Default.LocationOn,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            )
        }

        DarkTextField(
            value = pincode,
            onValueChange = { if (it.all { c -> c.isDigit() }) onPincodeChange(it) },
            placeholder = "6-digit pincode",
            icon = Icons.Default.Mail,
            isError = availabilityState.serviceability?.isServiceable == false,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )
        when {
            availabilityState.isCheckingServiceability ->
                StatusText("Checking availability...")
            availabilityState.serviceabilityError != null ->
                StatusText(availabilityState.serviceabilityError!!, isError = true)
            availabilityState.serviceability != null ->
                StatusText(
                    text = if (availabilityState.serviceability!!.isServiceable)
                        "✅ Service available at this pincode"
                    else "❌ Service not available at this pincode",
                    isError = !availabilityState.serviceability!!.isServiceable
                )
        }

        DarkTextField(
            value = age,
            onValueChange = { if (it.all { c -> c.isDigit() }) onAgeChange(it) },
            placeholder = "Age (Years)",
            icon = Icons.Default.Person,
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Next
        )

        SectionCard {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconBadge(icon = Icons.Default.Male)
                Spacer(Modifier.width(12.dp))
                Box(modifier = Modifier.weight(1f)) {
                    GenderDropdown(
                        selected = gender,
                        onGenderSelected = onGenderChange
                    )
                }
            }
            when {
                availabilityState.isLoadingSlots -> StatusText("Fetching available slots...")
                availabilityState.slotsError != null ->
                    StatusText(availabilityState.slotsError ?: "Unable to fetch slots", isError = true)
            }
        }



        SectionCard {
            Row(verticalAlignment = Alignment.Top) {
                IconBadge(icon = Icons.Default.Notes)
                Spacer(Modifier.width(12.dp))
                OutlinedTextField(
                    value = instructions,
                    onValueChange = onInstructionsChange,
                    placeholder = {
                        AutoText(
                            "Special Instructions (Optional)",
                            color = colorScheme.onSurfaceVariant
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = colorScheme.primary,
                        unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.3f),
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        cursorColor = colorScheme.primary,
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // ── Validation Error ──────────────────────────────────────────────
        showValidationError?.let {
            AutoText(
                text = it,
                color = colorScheme.error,
                fontSize = 12.sp,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        // ── Confirm Button ────────────────────────────────────────────────
        // Disabled when pincode is entered but service is not available there
        val isServiceBlocked = availabilityState.serviceability?.isServiceable == false
        val buttonBg = if (isServiceBlocked) colorScheme.onSurface.copy(alpha = 0.12f)
        else colorScheme.primary
        val buttonContentColor = if (isServiceBlocked) colorScheme.onSurface.copy(alpha = 0.38f)
        else colorScheme.onPrimary

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(buttonBg)
                .then(
                    if (!isServiceBlocked) Modifier.clickable { onPayClick() }
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = colorScheme.primary,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(Modifier.width(12.dp))
                    AutoText(
                        "Processing...",
                        color = colorScheme.onSurface.copy(alpha = 0.6f),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AutoText(
                        "Confirm Booking",
                        color = if (isButtonDisabled) colorScheme.onSurface.copy(alpha = 0.38f)
                        else colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                if (isButtonDisabled) colorScheme.onSurface.copy(alpha = 0.15f)
                                else colorScheme.onPrimary.copy(alpha = 0.15f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = if (isButtonDisabled) colorScheme.onSurface.copy(alpha = 0.38f)
                            else colorScheme.onPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Shield,
                contentDescription = null,
                tint = colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(Modifier.width(6.dp))
            AutoText(
                "Your details are safe and secure with us.",
                color = colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }

        Spacer(Modifier.height(16.dp))
    }
}

// ── Reusable components ────────────────────────────────────────────────────

@Composable
private fun SectionCard(content: @Composable ColumnScope.() -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorScheme.surface)
            .padding(16.dp),
        content = content
    )
}

@Composable
private fun IconBadge(icon: ImageVector) {
    val colorScheme = MaterialTheme.colorScheme
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = colorScheme.onPrimaryContainer,
            modifier = Modifier.size(20.dp)
        )
    }
}

@Composable
private fun DarkTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    icon: ImageVector,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done,
) {
    val colorScheme = MaterialTheme.colorScheme
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(colorScheme.surface)
            .border(
                width = if (isError) 1.dp else 0.dp,
                color = if (isError) colorScheme.error else colorScheme.outline.copy(alpha = 0f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconBadge(icon = icon)
        Spacer(Modifier.width(12.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { AutoText(placeholder, color = colorScheme.onSurfaceVariant) },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = colorScheme.primary,
                unfocusedBorderColor = colorScheme.outline.copy(alpha = 0.3f),
                focusedTextColor = colorScheme.onSurface,
                unfocusedTextColor = colorScheme.onSurface,
                cursorColor = colorScheme.primary,
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun CollectionTypeOption(
    label: String,
    subtitle: String,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val colorScheme = MaterialTheme.colorScheme
    val bg = if (selected) colorScheme.primaryContainer else colorScheme.surfaceVariant
    val borderColor = if (selected) colorScheme.primary else colorScheme.outline.copy(alpha = 0f)

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .border(1.dp, borderColor, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = colorScheme.primary,
                unselectedColor = colorScheme.onSurfaceVariant
            )
        )
        Spacer(Modifier.width(6.dp))
        Column {
            AutoText(
                label,
                color = colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp
            )
            AutoText(subtitle, color = colorScheme.onSurfaceVariant, fontSize = 11.sp)
        }
    }
}

@Composable
private fun StatusText(text: String, isError: Boolean = false) {
    val colorScheme = MaterialTheme.colorScheme
    AutoText(
        text = text,
        color = if (isError) colorScheme.error else colorScheme.onSurfaceVariant,
        fontSize = 11.sp,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderDropdown(
    selected: String,
    onGenderSelected: (String) -> Unit,
) {
    val options = listOf("Male", "Female", "Other")
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { AutoText("Select gender") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { AutoText(option) },
                    onClick = {
                        onGenderSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}