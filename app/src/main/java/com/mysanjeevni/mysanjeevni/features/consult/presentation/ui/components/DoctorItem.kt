package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
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
    val isDarkTheme = isSystemInDarkTheme()
    val cardBackground = if (isDarkTheme) Color(0xFF1A2421) else Color.White
    val textColor = if (isDarkTheme) Color(0xFFF1F5F3) else Color(0xFF071B35)
    val greyColor = if (isDarkTheme) Color(0xFFAABBB5) else Color(0xFF60738A)
    val imageBackground = if (isDarkTheme) Color(0xFF252333) else Color(0xFFF5F3FF)
    val lightGreen = if (isDarkTheme) Color(0xFF17382C) else LightGreen
    val iconColor = if (isDarkTheme)  Color(0xFF35C99A)  else  ConsultGreen
    val availabilityColor = if (isDarkTheme) Color(0xFF35C99A)  else  ConsultGreen
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = cardBackground,
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
                    color = lightGreen
                ) {

                    AutoText(
                        text = "Popular",
                        modifier = Modifier.padding(
                            horizontal = 14.dp,
                            vertical = 7.dp
                        ),
                        color = textColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Surface(
                    shape = RoundedCornerShape(50),
                    color = lightGreen
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
                        color = availabilityColor,
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
                    color = imageBackground
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
                            tint = iconColor
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
                        color = greyColor,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    AutoText(
                        text = doctor.name,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
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
                            color = textColor
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        AutoText(
                            text = "(${doctor.totalReviews})",
                            fontSize = 14.sp,
                            color = greyColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            Text(
                doctor.department,
                fontSize = 14.sp,
                color = greyColor
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
                    color = greyColor
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = lightGreen
                ) {

                    AutoText(
                        text = "In Stock",
                        modifier = Modifier.padding(
                            horizontal = 12.dp,
                            vertical = 6.dp
                        ),
                        color = iconColor,
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
                    color = textColor
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
                        color = textColor
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