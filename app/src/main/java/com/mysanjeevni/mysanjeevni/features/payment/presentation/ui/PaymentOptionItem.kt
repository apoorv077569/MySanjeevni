package com.mysanjeevni.mysanjeevni.features.payment.presentation.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.features.payment.domain.model.PaymentMethod
import com.mysanjeevni.mysanjeevni.utils.AutoText

private val PrimaryPurple = Color(0xFF4A3AFF)
private val PurpleBg = Color(0xFFEBE9FF)
private val PurpleIconBg = Color(0xFFD9D4FF)



@Composable
fun PaymentOptionItem(
    method: PaymentMethod,
    selected: Boolean,
    onSelect: () -> Unit
) {

    when (method) {
        PaymentMethod.RAZORPAY -> RazorpayOptionCard(selected = selected, onSelect = onSelect)
        PaymentMethod.COD -> CodOptionCard(selected = selected, onSelect = onSelect)
    }
}

@Composable
private fun RazorpayOptionCard(selected: Boolean, onSelect: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    val isDark = LocalIsDarkTheme.current

    val selectedBg =
        if (isDark)
            MaterialTheme.colorScheme.primaryContainer
        else
            PurpleBg
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(14.dp),
        border = if (selected)
            BorderStroke(2.dp, PrimaryPurple)
        else
            BorderStroke(1.dp, colorScheme.outline.copy(alpha = 0.4f)),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) selectedBg else colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Purple circle icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(PurpleIconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = PrimaryPurple,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                AutoText(
                    text = "Pay Online",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )
                AutoText(
                    text = "UPI, Cards, NetBanking",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                PaymentLogoBadges()
            }

            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = PrimaryPurple,
                    unselectedColor = colorScheme.outline
                )
            )
        }
    }
}

@Composable
private fun CodOptionCard(
    selected: Boolean,
    onSelect: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(14.dp),
        border = if (selected)
            BorderStroke(
                2.dp,
                colorScheme.primary
            )
        else
            BorderStroke(
                1.dp,
                colorScheme.outline.copy(alpha = 0.4f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = if (selected)
                colorScheme.primaryContainer
            else
                colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(
                        colorScheme.surfaceVariant
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Money,
                    contentDescription = null,
                    tint = colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                AutoText(
                    text = "Cash on Delivery",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = colorScheme.onSurface
                )

                AutoText(
                    text = "Pay when order arrives",
                    style = MaterialTheme.typography.bodySmall,
                    color = colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = Color(0xFF2ECC71),
                        modifier = Modifier.size(14.dp)
                    )

                    Spacer(modifier = Modifier.width(4.dp))

                    AutoText(
                        text = "Safe & Secure",
                        fontSize = 12.sp,
                        color = Color(0xFF2ECC71),
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            RadioButton(
                selected = selected,
                onClick = onSelect,
                colors = RadioButtonDefaults.colors(
                    selectedColor = colorScheme.primary,
                    unselectedColor = colorScheme.outline
                )
            )
        }
    }
}
@Composable
private fun PaymentLogoBadges() {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            LogoPillBadge(
                painter = painterResource(id = R.drawable.upi),
//                borderColor = Color(0xFFF97316),
                bgColor = Color(0xFFFFF3E8),
                contentDescription = "UPI"
            )
            LogoPillBadge(
                painter = painterResource(id = R.drawable.visa),
//                borderColor = Color(0xFF1A1F71),
                bgColor = Color(0xFFEEF0FF),
                contentDescription = "Visa"
            )
        }
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            LogoPillBadge(
                painter = painterResource(id = R.drawable.mastercard),
                bgColor = Color(0xFFFFEEEE),
                contentDescription = "Mastercard"
            )
            LogoPillBadge(
                painter = painterResource(id = R.drawable.rupay_logo),
//                borderColor = Color(0xFF1B8B4B),
                bgColor = Color(0xFFE8F5E9),
                contentDescription = "RuPay"
            )
        }
    }
}

@Composable
private fun LogoPillBadge(
    painter: Painter,
    bgColor: Color,
    contentDescription: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(bgColor)
//            .border(
//                width = 1.dp,
////                color = borderColor.copy(alpha = 0.5f),
//                shape = RoundedCornerShape(6.dp)
//            )
            .padding(horizontal = 10.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painter,
            contentDescription = contentDescription,
            modifier = Modifier
                .height(22.dp)
                .widthIn(min = 32.dp, max = 48.dp),
            contentScale = ContentScale.Fit
        )
    }
}