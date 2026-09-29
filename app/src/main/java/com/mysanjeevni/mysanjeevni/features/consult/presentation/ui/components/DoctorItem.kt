package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor
import com.mysanjeevni.mysanjeevni.utils.AutoText

private val ConsultGreen = Color(0xFF00A878)

private val ScreenBackground = Color(0xFFF2FFFB)
private val TextDark = Color(0xFF071B35)
private val TextGrey = Color(0xFF60738A)
@Composable
fun DoctorCard(
    doctor: Doctor,
    onViewSlotsClick: () -> Unit,
    onBookNowClick: () -> Unit
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 3.dp
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            // Top badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    shape = RoundedCornerShape(50),
                    color = ConsultGreen
                ) {

                    AutoText(
                        text = "Popular",
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 7.dp
                        ),
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = LightGreen
                ) {

                    AutoText(
                        text = if (doctor.isAvailable) {
                            "Available"
                        } else {
                            "Unavailable"
                        },
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 7.dp
                        ),
                        color = ConsultGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Doctor information
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Surface(
                    modifier = Modifier.size(110.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0xFFF5F3FF)
                ) {

                    if (doctor.avatar.isNotBlank()) {

                        AsyncImage(
                            model = doctor.avatar,
                            contentDescription = doctor.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                    } else {

                        Icon(
                            imageVector = Icons.Outlined.MedicalServices,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(28.dp)
                                .fillMaxSize(),
                            tint = ConsultGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier.weight(1f)
                ) {

                    AutoText(
                        text = doctor.specialization.uppercase(),
                        fontSize = 13.sp,
                        color = TextGrey,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    AutoText(
                        text = doctor.name,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Icon(
                            imageVector = Icons.Outlined.Star,
                            contentDescription = null,
                            tint = Color(0xFFFFA000),
                            modifier = Modifier.size(20.dp)
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        AutoText(
                            text = "${doctor.rating}",
                            fontSize = 14.sp,
                            color = TextDark
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        AutoText(
                            text = "(${doctor.totalReviews})",
                            fontSize = 14.sp,
                            color = TextGrey
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                doctor.department,
                fontSize = 14.sp,
                color = TextGrey
            )

            // Availability
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "View Slots: ${doctor.isAvailable}",
                    fontSize = 14.sp,
                    color = TextGrey
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = LightGreen
                ) {

                    AutoText(
                        text = "In Stock",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        color = ConsultGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Price + experience
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AutoText(
                    text = if (doctor.consultationFee <= 0) {
                        "Free"
                    } else {
                        "₹${doctor.consultationFee.toInt()}"
                    },
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                AutoText(
                    text = doctor.experience.toString(),
                    fontSize = 14.sp,
                    color = ConsultGreen,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                OutlinedButton(
                    onClick = onViewSlotsClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp)
                ) {

                    Text(
                        text = "View Slots",
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark
                    )
                }

                Button(
                    onClick = onBookNowClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ConsultGreen
                    )
                ) {

                    AutoText(
                        text = "Book Now",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}