package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.VideoCall
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.ConsultItem
import com.mysanjeevni.mysanjeevni.features.consult.presentation.state.ConsultStatus
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun ConsultationBottomSection(
    consult: ConsultItem,
    textColor: Color,
    secondaryText: Color,
    primaryColor: Color,
    onViewPrescriptionClick: (String) -> Unit,
    onBookAgainClick: (String) -> Unit,
    onViewDetailClick: (String) -> Unit
) {

    val colorScheme = MaterialTheme.colorScheme

    /*
     * Theme-aware semantic colors
     */

    val successColor = colorScheme.tertiary
    val successContainer = colorScheme.tertiaryContainer
    val onSuccessContainer = colorScheme.onTertiaryContainer

    val warningContainer = colorScheme.primaryContainer
    val onWarningContainer = colorScheme.onPrimaryContainer

    val scheduledContainer = colorScheme.secondaryContainer
    val onScheduledContainer = colorScheme.onSecondaryContainer

    val errorContainer = colorScheme.errorContainer
    val errorColor = colorScheme.error
    val onErrorContainer = colorScheme.onErrorContainer

    when (consult.status) {

        // =====================================================
        // COMPLETED
        // =====================================================

        ConsultStatus.COMPLETED -> {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = successContainer,
                border = BorderStroke(
                    width = 1.dp,
                    color = colorScheme.outline
                )
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {

                    // -----------------------------------------
                    // Completed header
                    // -----------------------------------------

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(
                                    color = successColor.copy(alpha = 0.12f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = successColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            AutoText(
                                text = "Consultation Completed",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSuccessContainer
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            AutoText(
                                text = "Your consultation has been completed",
                                fontSize = 12.sp,
                                color = onSuccessContainer.copy(alpha = 0.75f)
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    HorizontalDivider(
                        color = colorScheme.outline.copy(alpha = 0.5f)
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    // -----------------------------------------
                    // Payment information
                    // -----------------------------------------

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            AutoText(
                                text = "Payment",
                                fontSize = 12.sp,
                                color = onSuccessContainer.copy(alpha = 0.7f)
                            )

                            Spacer(
                                modifier = Modifier.height(2.dp)
                            )

                            AutoText(
                                text = if (consult.fees <= 0) {
                                    "Free"
                                } else {
                                    "₹${consult.fees.toInt()}"
                                },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSuccessContainer
                            )
                        }

                        Surface(
                            color = successColor.copy(alpha = 0.12f),
                            shape = RoundedCornerShape(20.dp)
                        ) {

                            AutoText(
                                text = "Paid",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = successColor,
                                modifier = Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )

                    // -----------------------------------------
                    // Actions
                    // -----------------------------------------

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {

                        OutlinedButton(
                            onClick = {
                                onViewPrescriptionClick(consult.id)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(
                                1.dp,
                                primaryColor
                            )
                        ) {

                            AutoText(
                                text = "View Prescription",
                                fontSize = 12.sp,
                                color = primaryColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Button(
                            onClick = {
                                onBookAgainClick(consult.id)
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = primaryColor,
                                contentColor = colorScheme.onPrimary
                            )
                        ) {

                            AutoText(
                                text = "Book Again",
                                fontSize = 12.sp,
                                color = colorScheme.onPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // =====================================================
        // WAITING
        // =====================================================

        ConsultStatus.WAITING -> {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = warningContainer,
                border = BorderStroke(
                    1.dp,
                    colorScheme.outline.copy(alpha = 0.5f)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .background(
                                    color = primaryColor.copy(alpha = 0.12f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = primaryColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(
                            modifier = Modifier.width(12.dp)
                        )

                        Column {

                            AutoText(
                                text = "Doctor is ready",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = onWarningContainer
                            )

                            AutoText(
                                text = "Join your consultation now",
                                fontSize = 12.sp,
                                color = onWarningContainer.copy(alpha = 0.75f)
                            )
                        }
                    }

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        onClick = {
                            // TODO Join consultation
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = colorScheme.onPrimary
                        )
                    ) {

                        Icon(
                            imageVector = Icons.Default.VideoCall,
                            contentDescription = null,
                            modifier = Modifier.size(19.dp)
                        )

                        Spacer(
                            modifier = Modifier.width(8.dp)
                        )

                        AutoText(
                            text = "Join Consultation",
                            color = colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // =====================================================
        // SCHEDULED
        // =====================================================

        ConsultStatus.SCHEDULED -> {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = scheduledContainer,
                border = BorderStroke(
                    1.dp,
                    colorScheme.outline.copy(alpha = 0.5f)
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    AutoText(
                        text = "Upcoming Consultation",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = onScheduledContainer
                    )

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    AutoText(
                        text = "Your appointment is scheduled.",
                        fontSize = 12.sp,
                        color = onScheduledContainer.copy(alpha = 0.75f)
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Button(
                        onClick = {
                            onViewDetailClick(consult.id)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = primaryColor,
                            contentColor = colorScheme.onPrimary
                        )
                    ) {

                        AutoText(
                            text = "View",
                            fontSize = 12.sp,
                            color = colorScheme.onPrimary
                        )
                    }
                }
            }
        }

        // =====================================================
        // CANCELLED
        // =====================================================

        ConsultStatus.CANCELLED -> {

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = errorContainer,
                border = BorderStroke(
                    1.dp,
                    colorScheme.outline.copy(alpha = 0.5f)
                )
            ) {

                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = errorColor.copy(alpha = 0.10f),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {

                        AutoText(
                            text = "×",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = errorColor
                        )
                    }

                    Spacer(
                        modifier = Modifier.width(12.dp)
                    )

                    Column {

                        AutoText(
                            text = "Consultation Cancelled",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = onErrorContainer
                        )

                        Spacer(
                            modifier = Modifier.height(2.dp)
                        )

                        AutoText(
                            text = "This consultation was cancelled.",
                            fontSize = 12.sp,
                            color = onErrorContainer.copy(alpha = 0.75f)
                        )
                    }
                }
            }
        }
    }
}