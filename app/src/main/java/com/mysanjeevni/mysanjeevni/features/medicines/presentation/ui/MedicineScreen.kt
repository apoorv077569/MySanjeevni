package com.mysanjeevni.mysanjeevni.features.medicines.presentation.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.R
import com.mysanjeevni.mysanjeevni.features.category.presenttion.data.bodyParts
import com.mysanjeevni.mysanjeevni.features.category.presenttion.data.categoryList
import com.mysanjeevni.mysanjeevni.features.category.presenttion.data.issues
import com.mysanjeevni.mysanjeevni.features.category.presenttion.model.CategoryModel

@Composable
fun MedicineScreen() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF38D6C6))
    ) {

        // 🔹 Section 1
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                SectionTitle("For your Body Parts")

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalList(bodyParts)
            }
        }

        // 🔹 Section 2
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                SectionTitle("Common Health Issues")

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalList(issues)
            }
        }

        // 🔹 Section 3
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {

                SectionTitle("Shop By Category")

                Spacer(modifier = Modifier.height(8.dp))

                HorizontalList(categoryList)
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(title, fontWeight = FontWeight.Bold)

        Text("View All", color = Color(0xFF38D6C6))
    }
}
@Composable
fun HorizontalList(list: List<CategoryModel>) {

    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {
        items(list) { item ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(end = 12.dp)
            ) {

                Card(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Image(
                        painter = painterResource(id = item.imgUrl), // ✅ FIX
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(item.name, fontSize = 12.sp)
            }
        }
    }
}