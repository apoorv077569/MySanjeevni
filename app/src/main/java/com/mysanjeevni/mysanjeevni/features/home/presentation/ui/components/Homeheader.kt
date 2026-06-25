package com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.*
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

val AppGreen = Color(0xFF1AAB8B)
val AppGreenLight = Color(0xFFE8F8F4)

@Composable
fun HomeHeader(
    location: String,
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester,
    onLocationClick: () -> Unit,
    onCartClick: () -> Unit,
    onConsultClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppGreen)
            .statusBarsPadding()
            .padding(bottom = 12.dp)
    ) {
        // Top Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.clickable { onLocationClick() }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(2.dp))
                    AutoText("Delivering to", fontSize = 11.sp, color = Color.White.copy(alpha = 0.85f))
                    Icon(Icons.Default.KeyboardArrowDown, null, tint = Color.White, modifier = Modifier.size(16.dp))
                }
                AutoText(
                    text = location.ifEmpty { "Select Location" },
                    fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color.White, maxLines = 1
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.18f))
                        .clickable { onConsultClick() }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Call, null, tint = Color.White, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.width(4.dp))
                    AutoText("Consult", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
                Box {
                    Icon(Icons.Outlined.Notifications, null, tint = Color.White, modifier = Modifier.size(24.dp))
                    Box(Modifier.size(7.dp).background(Color(0xFFFF5252), CircleShape).align(Alignment.TopEnd))
                }
                Box(modifier = Modifier.clickable { onCartClick() }) {
                    Icon(Icons.Outlined.ShoppingCart, null, tint = Color.White, modifier = Modifier.size(24.dp))
                }
            }
        }

        // Search Bar
        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(46.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Search, null, tint = Color.Gray, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(10.dp))
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) AutoText("Search medicines, doctors, lab tests...", color = Color(0xFFBBBBBB), fontSize = 13.sp)
                    BasicTextField(
                        value = query, onValueChange = onQueryChange, singleLine = true,
                        textStyle = TextStyle(color = Color.Black, fontSize = 14.sp),
                        modifier = Modifier.focusRequester(focusRequester)
                    )
                }
                if (query.isNotEmpty()) {
                    Icon(Icons.Default.Close, null, tint = Color.Gray, modifier = Modifier.size(18.dp).clickable { onClear() })
                }
            }
        }
    }
}