package com.mysanjeevni.mysanjeevni.utils.dilaog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mysanjeevni.mysanjeevni.data.remote.model.address.AddressModel
import com.mysanjeevni.mysanjeevni.features.profile.presentation.state.AddressItem
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun AddressDialog(
    addressToEdit: AddressItem? = null,
    onDismiss: () -> Unit,
    onSave: (AddressModel) -> Unit
) {

    var fullName by remember {
        mutableStateOf(addressToEdit?.fullName ?: "")
    }

    var phone by remember {
        mutableStateOf(addressToEdit?.phone ?: "")
    }

    var addressLine1 by remember {
        mutableStateOf(addressToEdit?.addressLine1 ?: "")
    }

    var addressLine2 by remember {
        mutableStateOf(addressToEdit?.addressLine2 ?: "")
    }

    var city by remember {
        mutableStateOf(addressToEdit?.city ?: "")
    }

    var state by remember {
        mutableStateOf(addressToEdit?.state ?: "")
    }

    var pincode by remember {
        mutableStateOf(addressToEdit?.pincode ?: "")
    }

    var type by remember {
        mutableStateOf(addressToEdit?.type ?: "home")
    }

    var isDefault by remember {
        mutableStateOf(addressToEdit?.isDefault ?: false)
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    Dialog(
        onDismissRequest = onDismiss,
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
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                AutoText(
                    text = if (addressToEdit == null)
                        "Add Address"
                    else
                        "Edit Address",
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
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = phone,
                    onValueChange = {
                        phone = it
                    },
                    label = {
                        AutoText("Phone Number")
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone
                    ),
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
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    OutlinedTextField(
                        value = city,
                        onValueChange = {
                            city = it
                        },
                        label = {
                            AutoText("City")
                        },
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = pincode,
                        onValueChange = {
                            pincode = it
                        },
                        label = {
                            AutoText("Pincode")
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                OutlinedTextField(
                    value = state,
                    onValueChange = {
                        state = it
                    },
                    label = {
                        AutoText("State")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                AutoText(
                    text = "Address Type",
                    fontWeight = FontWeight.Medium
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                                    item.replaceFirstChar {
                                        it.uppercase()
                                    }
                                )
                            }
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Checkbox(
                        checked = isDefault,
                        onCheckedChange = {
                            isDefault = it
                        }
                    )

                    AutoText(
                        "Set as default address"
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {

                    TextButton(
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

                            onSave(
                                AddressModel(
                                    id = addressToEdit?.id ?: "",
                                    userId = "",
                                    type = type,
                                    fullName = fullName,
                                    phone = phone,
                                    addressLine1 = addressLine1,
                                    addressLine2 = addressLine2,
                                    city = city,
                                    state = state,
                                    pincode = pincode,
                                    isDefault = isDefault
                                )
                            )
                            onDismiss()
                        }
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            AutoText(
                                "Save Address"
                            )
                        }
                    }
                }
            }
        }
    }
}