package com.mysanjeevni.mysanjeevni.utils.dilaog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mysanjeevni.mysanjeevni.features.profile.domain.model.Address
import com.mysanjeevni.mysanjeevni.features.profile.presentation.viewmodel.AddressViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun AddressDialog(
    addressToEdit: Address? = null,
    onDismiss: () -> Unit,
    addressViewModel: AddressViewModel,
    onSave: (Address) -> Unit
) {

    val serviceabilityState by addressViewModel
        .serviceabilityState
        .collectAsState()

    var fullName by remember(addressToEdit) {
        mutableStateOf(
            addressToEdit?.fullName.orEmpty()
        )
    }

    var phone by remember(addressToEdit) {
        mutableStateOf(
            addressToEdit?.phone.orEmpty()
        )
    }

    var addressLine1 by remember(addressToEdit) {
        mutableStateOf(
            addressToEdit?.addressLine1.orEmpty()
        )
    }

    var addressLine2 by remember(addressToEdit) {
        mutableStateOf(
            addressToEdit?.addressLine2.orEmpty()
        )
    }

    var city by remember(addressToEdit) {
        mutableStateOf(
            addressToEdit?.city.orEmpty()
        )
    }

    var state by remember(addressToEdit) {
        mutableStateOf(
            addressToEdit?.state.orEmpty()
        )
    }

    var pincode by remember(addressToEdit) {
        mutableStateOf(
            addressToEdit?.pincode.orEmpty()
        )
    }

    var type by remember(addressToEdit) {
        mutableStateOf(
            addressToEdit?.type
                ?.takeIf { it.isNotBlank() }
                ?: "home"
        )
    }

    var isDefault by remember(addressToEdit) {
        mutableStateOf(
            addressToEdit?.isDefault ?: false
        )
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = {
            if (!isLoading) {
                onDismiss()
            }
        },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {

        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(
                        rememberScrollState()
                    ),
                verticalArrangement =
                    Arrangement.spacedBy(12.dp)
            ) {

                AutoText(
                    text = if (addressToEdit == null) {
                        "Add Address"
                    } else {
                        "Edit Address"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                    },
                    label = {
                        AutoText("Full Name")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = { value ->
                        phone = value
                            .filter { it.isDigit() }
                            .take(10)
                    },
                    label = {
                        AutoText("Phone Number")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = addressLine1,
                    onValueChange = {
                        addressLine1 = it
                    },
                    label = {
                        AutoText("Address Line 1")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = addressLine2,
                    onValueChange = {
                        addressLine2 = it
                    },
                    label = {
                        AutoText("Address Line 2")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedTextField(
                        value = city,
                        onValueChange = {
                            city = it
                        },
                        label = {
                            AutoText("City")
                        },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = pincode,
                        onValueChange = { value ->
                            pincode = value
                                .filter { it.isDigit() }
                                .take(6)
                            if (pincode.length == 6) {
                                addressViewModel.checkServiceability(
                                    pincode
                                )
                            } else {
                                addressViewModel.clearServiceability()
                            }
                        },
                        label = {
                            AutoText("Pincode")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )

                }
                if (serviceabilityState.isLoading) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp
                        )

                        Spacer(Modifier.width(8.dp))

                        AutoText("Checking delivery...")
                    }
                }

                else if (serviceabilityState.serviceable) {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            AutoText(
                                text = "✅ Deliverable via ${serviceabilityState.courierName}",
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(Modifier.height(6.dp))

                            AutoText(
                                text = "Shipping Charge: ₹${serviceabilityState.deliveryCharge}"
                            )

                            AutoText(
                                text = "Estimated Delivery: ${serviceabilityState.estimatedDeliveryDate}"
                            )

                            AutoText(
                                text = "Delivery Time: ${serviceabilityState.estimatedDeliveryDays} Days"
                            )

                            AutoText(
                                text = if (serviceabilityState.codAvailable)
                                    "COD Available"
                                else
                                    "COD Not Available"
                            )
                        }
                    }
                }

                else {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {

                            AutoText(
                                text = "❌ Sorry! Delivery is not available for this pincode.",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                OutlinedTextField(
                    value = state,
                    onValueChange = {
                        state = it
                    },
                    label = {
                        AutoText("State")
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                AutoText(
                    text = "Address Type",
                    fontWeight = FontWeight.Medium
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    listOf(
                        "home",
                        "work",
                        "other"
                    ).forEach { item ->

                        FilterChip(
                            selected = type == item,
                            onClick = {
                                type = item
                            },
                            label = {
                                AutoText(
                                    text = item.replaceFirstChar {
                                        it.uppercase()
                                    }
                                )
                            }
                        )
                    }
                }

                Row(
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = isDefault,
                        onCheckedChange = {
                            isDefault = it
                        }
                    )

                    AutoText(
                        text = "Set as default address"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                        Arrangement.End,
                    verticalAlignment =
                        Alignment.CenterVertically
                ) {

                    TextButton(
                        enabled = !isLoading,
                        onClick = onDismiss
                    ) {
                        AutoText("Cancel")
                    }

                    Spacer(
                        modifier = Modifier.width(8.dp)
                    )

                    Button(
                        enabled = !isLoading,
                        onClick = {

                            if (
                                fullName.isBlank() ||
                                phone.isBlank() ||
                                addressLine1.isBlank() ||
                                city.isBlank() ||
                                state.isBlank() ||
                                pincode.isBlank()
                            ) {
                                return@Button
                            }

                            isLoading = true

                            val address = Address(
                                id = addressToEdit?.id.orEmpty(),

                                // Add ke case me ViewModel
                                // SessionManager se userId set karega.
                                // Edit ke case me existing userId preserve hoga.
                                userId = addressToEdit
                                    ?.userId
                                    .orEmpty(),

                                type = type,
                                fullName = fullName.trim(),
                                phone = phone.trim(),
                                addressLine1 =
                                    addressLine1.trim(),
                                addressLine2 =
                                    addressLine2.trim(),
                                city = city.trim(),
                                state = state.trim(),
                                pincode = pincode.trim(),

                                // Existing value preserve karo.
                                country = addressToEdit
                                    ?.country
                                    ?.takeIf { it.isNotBlank() }
                                    ?: "India",

                                isDefault = isDefault,

                                // Edit me server values preserve honge.
                                // Add me empty rahenge.
                                createdAt = addressToEdit
                                    ?.createdAt
                                    .orEmpty(),

                                updatedAt = addressToEdit
                                    ?.updatedAt
                                    .orEmpty()
                            )

                            onSave(address)
                        }
                    ) {

                        if (isLoading) {

                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )

                        } else {

                            AutoText(
                                text = if (addressToEdit == null) {
                                    "Save Address"
                                } else {
                                    "Update Address"
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}