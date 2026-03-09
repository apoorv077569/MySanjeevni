package com.mysanjeevni.mysanjeevni.features.consult.presentation.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Doctor
import com.mysanjeevni.mysanjeevni.features.consult.domnain.model.Specialty
import com.mysanjeevni.mysanjeevni.features.consult.presentation.viewmodel.ConsultViewModel

// Dummy Data
val specialties = listOf(
    Specialty("General Physician", "https://cdn-icons-png.flaticon.com/512/387/387561.png"),
    Specialty("Skin & Hair", "https://cdn-icons-png.flaticon.com/512/3050/3050186.png"),
    Specialty("Women's Health", "https://cdn-icons-png.flaticon.com/512/2921/2921868.png"),
    Specialty("Dental Care", "https://cdn-icons-png.flaticon.com/512/2813/2813337.png"),
    Specialty("Child Specialist", "https://cdn-icons-png.flaticon.com/512/3209/3209041.png")
)

val doctors = listOf(
    Doctor(
        1,
        "Dr. Anjali Gupta",
        "Dermatologist",
        "8 years exp",
        4.8,
        120,
        499.0,
        "15 mins",
        "https://img.freepik.com/free-photo/pleased-young-female-doctor-wearing-medical-robe-stethoscope-around-neck-standing-with-closed-posture_409827-254.jpg"
    ),
    Doctor(
        2,
        "Dr. Rajesh Kumar",
        "General Physician",
        "12 years exp",
        4.5,
        340,
        399.0,
        "30 mins",
        "https://img.freepik.com/free-photo/doctor-with-his-arms-crossed-white-background_1368-5790.jpg"
    ),
    Doctor(
        3,
        "Dr. Sneha Roy",
        "Gynecologist",
        "15 years exp",
        4.9,
        500,
        699.0,
        "Available Now",
        "https://img.freepik.com/free-photo/woman-doctor-wearing-lab-coat-with-stethoscope-isolated_1303-29791.jpg"
    ),
    Doctor(
        4,
        "Dr. Vikram Singh",
        "Dentist",
        "6 years exp",
        4.4,
        80,
        299.0,
        "1 hour",
        "https://img.freepik.com/free-photo/portrait-smiling-handsome-male-doctor-man_171337-5055.jpg"
    )
)

@Composable
fun ConsultScreen(navController: NavController,
                  viewModel: ConsultViewModel = hiltViewModel()
) {
    val isDark = isSystemInDarkTheme()
    val bgColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F7FA)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    val searchQuery by viewModel.searchQuery.collectAsState()

    // 2. Initialize Focus Requester
    val focusRequester = remember { FocusRequester() }


    Scaffold(
        containerColor = bgColor,
        topBar = {
            Column(modifier = Modifier.background(cardColor).statusBarsPadding()) {
                Text(
                    stringResource(R.string.consult_a_doctor),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
                )
                ConsultSearchBar(
                    isDark = isDark,
                    query = searchQuery,
                    onQueryChange = { viewModel.onSearchQueryChanged(it) },
                    onClear = { viewModel.onSearchQueryChanged("") },
                    focusRequester = focusRequester
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item {
                Text(
                    stringResource(R.string.find_by_specialty),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(16.dp)
                )
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(specialties) { specialty ->
                        SpecialtyItem(specialty, isDark)
                    }
                }
            }
            item {
                Spacer(modifier = Modifier.height(24.dp))
                ConsultBanner()
                Spacer(modifier = Modifier.height(24.dp))
            }
            item {
                Text(
                    stringResource(R.string.top_doctors_near_you),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(doctors) { doctor ->
                DoctorCard(doctor, isDark, onBookClick = {

                })
            }
        }
    }
}

@Composable
fun ConsultSearchBar(
    isDark: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    focusRequester: FocusRequester
) {
    // Colors
    val containerColor = if (isDark) Color(0xFF2C2C2C) else Color(0xFFF0F2F5) // Light gray bg
    val textColor = if (isDark) Color.White else Color.Black
    val placeholderColor = Color.Gray

    // Keyboard & Focus Controllers
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    // Using BasicTextField for full custom styling
    BasicTextField(
        value = query,
        onValueChange = onQueryChange,
        singleLine = true,
        textStyle = TextStyle(
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal
        ),
        modifier = Modifier
            .focusRequester(focusRequester),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp) // Outer padding
                    .height(50.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(containerColor)
                    // Make the whole box clickable to focus input
                    .clickable {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    }
                    .padding(horizontal = 12.dp), // Inner padding
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Leading Icon
                Icon(
                    imageVector = Icons.Outlined.Search,
                    contentDescription = null,
                    tint = placeholderColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Input Area
                Box(modifier = Modifier.weight(1f)) {
                    if (query.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_for_doctors_symptoms___),
                            color = placeholderColor,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    innerTextField()
                }

                // Trailing Clear (X) Icon
                if (query.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear",
                        tint = placeholderColor,
                        modifier = Modifier
                            .clip(CircleShape)
                            .clickable {
                                onClear()
                                keyboardController?.hide()
                                focusManager.clearFocus()
                            }
                            .padding(4.dp)
                    )
                }
            }
        }
    )
}

@Composable
fun SpecialtyItem(item: Specialty, isDark: Boolean) {
    val textColor = if (isDark) Color.LightGray else Color.Black
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.width(70.dp)) {
        Box(
            modifier = Modifier
                .size(65.dp)
                .clip(CircleShape)
                .background(Color(0xFFE3F2FD)),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = null,
                modifier = Modifier.size(35.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = item.name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            textAlign = TextAlign.Center,
            lineHeight = 14.sp
        )
    }
}

@Composable
fun ConsultBanner(modifier: Modifier = Modifier) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F7FA))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    stringResource(R.string.flat_50_off),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = Color(0xFF006064)
                )
                Text(
                    stringResource(R.string.on_your_first_video_consultation),
                    fontSize = 12.sp,
                    color = Color(0xFF006064)
                )
            }
            Icon(
                Icons.Filled.Videocam,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = Color(0xFF00BCD4)
            )
        }
    }
}

@Composable
fun DoctorCard(doctor: Doctor, isDark: Boolean, onBookClick: () -> Unit) {
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val subTextColor = if(isDark) Color.Gray else Color.DarkGray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row {
                AsyncImage(
                    model = doctor.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray)
                )
                Spacer(modifier =   Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
                    Text(doctor.specialty, fontSize = 14.sp,color=Color(0xFF00897B), fontWeight = FontWeight.Medium)
                    Text(doctor.experience, fontSize = 12.sp,color=subTextColor)
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.clip(RoundedCornerShape(4.dp))
                            .background(Color(0xFF00C853))
                            .padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "${doctor.rating}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(10.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Available ${doctor.availableTime}", fontSize = 12.sp, color = Color(0xFF00C853), fontWeight
                    = FontWeight.Bold)
                    Text("₹${doctor.fee}", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
                Button(
                    onClick = onBookClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF6F61)),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(stringResource(R.string.consult_now))
                }
            }
        }
    }
}