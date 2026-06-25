package com.mysanjeevni.mysanjeevni.core.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun StylishHeader(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
    location: String,
    onLocationClick: () -> Unit,
    onNotificationClick: () -> Unit
) {

    val colorScheme = MaterialTheme.colorScheme

    val headerColor = colorScheme.primary
    val onHeaderColor = colorScheme.onPrimary

    val searchCardColor = colorScheme.surface
    val searchTextColor = colorScheme.onSurface
    val hintColor = colorScheme.onSurfaceVariant

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 15.dp, bottomEnd = 15.dp))
            .background(headerColor)
            .statusBarsPadding()
            .padding(bottom = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp), // 🔥 reduced
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.clickable { onLocationClick() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = onHeaderColor,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    AutoText(
                        text = "Delivering to",
                        fontSize = 11.sp,
                        color = onHeaderColor
                    )
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = onHeaderColor.copy(alpha = 0.8f),
                        modifier = Modifier.size(14.dp)
                    )
                }

                AutoText(
                    text = location.ifEmpty { "Select Location" },
                    fontSize = 14.sp, // 🔥 slightly reduced
                    fontWeight = FontWeight.Bold,
                    color =onHeaderColor,
                    maxLines = 1
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {

                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier.clickable {
                        onNotificationClick()
                    }
                ) {
                    Icon(
                        Icons.Outlined.Notifications,
                        contentDescription = "Alerts",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .background(Color.Red, CircleShape)
                            .align(Alignment.TopEnd)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp)) // 🔥 reduced

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(44.dp), // 🔥 compact
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = searchCardColor),
            elevation = CardDefaults.cardElevation(4.dp) // 🔥 soft shadow
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { focusRequester.requestFocus() }
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(
                    Icons.Default.Search,
                    contentDescription = null,
                    tint = hintColor,
                    modifier = Modifier.size(18.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(modifier = Modifier.weight(1f)) {

                    if (query.isEmpty()) {
                        AutoText(
                            text = "Search medicines, products...",
                            color = hintColor,
                            fontSize = 13.sp
                        )
                    }

                    BasicTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        textStyle = TextStyle(
                            color = searchTextColor,
                            fontSize = 14.sp
                        ),
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                }

                if (query.isNotEmpty()) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = hintColor,
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onClear() }
                    )
                }
            }
        }
    }
}