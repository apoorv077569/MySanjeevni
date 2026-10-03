package com.mysanjeevni.mysanjeevni.features.home.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.medicines.domain.model.Medicine
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.viewmodel.MedicineViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText

@Composable
fun TrendingProductsSection(
    navController: NavController,
    cartViewModel: CartViewModel,
    viewModel: MedicineViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val medicines = state.medicines.take(6)
    val cartState by cartViewModel.state.collectAsState()

    if (medicines.isEmpty()) return

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader("Trending Products")
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            medicines.forEach { medicine ->
                TrendingProductRow(
                    medicine = medicine,
                    qty = cartState.cartItem.find { it.id == medicine.id }?.qty ?: 0,
                    onAdd = {
                        cartViewModel.addToCart(CartItem(
                            id = medicine.id, name = medicine.name, price = medicine.price,
                            originalPrice = medicine.mrp, qty = 1, imageUrl = medicine.image, stock = medicine.stock, requirePrescription = medicine.requiresPrescription
                        ))
                    },
                    onIncrement = {
                        val qty = cartState.cartItem.find { it.id == medicine.id }?.qty ?: 0
                        cartViewModel.incrementQty(CartItem(
                            id = medicine.id, name = medicine.name, price = medicine.price,
                            originalPrice = medicine.mrp, qty = qty, imageUrl = medicine.image, stock = medicine.stock, requirePrescription = medicine.requiresPrescription
                        ))
                    },
                    onDecrement = {
                        val qty = cartState.cartItem.find { it.id == medicine.id }?.qty ?: 0
                        cartViewModel.decrementQty(CartItem(
                            id = medicine.id, name = medicine.name, price = medicine.price,
                            originalPrice = medicine.mrp, qty = qty, imageUrl = medicine.image, stock = medicine.stock, requirePrescription = medicine.requiresPrescription
                        ))
                    },
                    onClick = { navController.navigate(Screen.MedicineDetail.createRoute(medicine.id)) }
                )
            }
        }
    }
}

@Composable
fun TrendingProductRow(
    medicine: Medicine,
    qty: Int,
    onAdd: () -> Unit,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onClick: () -> Unit
) {
    val discount = if (medicine.mrp > 0 && medicine.mrp > medicine.price)
        "${((medicine.mrp - medicine.price) / medicine.mrp * 100).toInt()}% OFF" else null

    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier.size(70.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFFF5F5F5)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(model = medicine.image, contentDescription = null,
                    modifier = Modifier.size(56.dp), contentScale = ContentScale.Fit)
                if (discount != null) {
                    Box(
                        modifier = Modifier.align(Alignment.TopStart).padding(2.dp)
                            .clip(RoundedCornerShape(4.dp)).background(Color(0xFF43A047))
                            .padding(horizontal = 3.dp, vertical = 1.dp)
                    ) {
                        AutoText(discount, fontSize = 8.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
            Column(modifier = Modifier.weight(1f)) {
                AutoText(medicine.brand, fontSize = 10.sp, color = AppGreen, fontWeight = FontWeight.SemiBold)
                AutoText(medicine.name, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.Black,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
                AutoText("${medicine.quantity} ${medicine.quantityUnit}", fontSize = 11.sp, color = Color.Gray)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    AutoText("₹%.2f".format(medicine.price), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    AutoText("₹%.2f".format(medicine.mrp), fontSize = 11.sp, color = Color.Gray, textDecoration = TextDecoration.LineThrough)
                }
            }
            // Add/Counter
            if (medicine.stock <= 0) {
                Box(modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color.LightGray).padding(horizontal = 10.dp, vertical = 6.dp)) {
                    AutoText("Out of\nStock", fontSize = 10.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
                }
            } else if (qty == 0) {
                Box(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(AppGreen)
                        .clickable { onAdd() }.padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    AutoText("ADD", fontSize = 13.sp, color = Color.White, fontWeight = FontWeight.Bold)
                }
            } else {
                Row(
                    modifier = Modifier.clip(RoundedCornerShape(8.dp)).background(Color.White),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(AppGreenLight)
                        .clickable { onDecrement() }, contentAlignment = Alignment.Center) {
                        AutoText("−", fontSize = 18.sp, color = AppGreen, fontWeight = FontWeight.Bold)
                    }
                    AutoText("$qty", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black,
                        modifier = Modifier.padding(horizontal = 10.dp))
                    Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(8.dp)).background(AppGreen)
                        .clickable { onIncrement() }, contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Add, null, tint = Color.White, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

data class BlogPost(val title: String, val date: String, val readTime: String, val imageUrl: String)

@Composable
fun HealthBlogsSection() {
    val blogs = listOf(
        BlogPost("Boost Your Immunity Naturally – 10 Simple Tips", "12 Jun 2024", "5 min read",
            "https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=200"),
        BlogPost("Everything You Need to Know About Thyroid", "10 Jun 2024", "4 min read",
            "https://images.unsplash.com/photo-1559757175-0eb30cd8c063?w=200"),
        BlogPost("How to Manage Diabetes with the Right Diet", "8 Jun 2024", "6 min read",
            "https://images.unsplash.com/photo-1576086213369-97a306d36557?w=200"),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader("Health Tips & Blogs")
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            blogs.forEach { blog -> BlogCard(blog) }
        }
    }
}

@Composable
fun BlogCard(blog: BlogPost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AsyncImage(
                model = blog.imageUrl, contentDescription = null,
                modifier = Modifier.size(72.dp).clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.weight(1f)) {
                AutoText(blog.title, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color.Black,
                    maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 17.sp)
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    AutoText(blog.date, fontSize = 10.sp, color = Color.Gray)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        Icon(Icons.Outlined.Timer, null, tint = Color.Gray, modifier = Modifier.size(12.dp))
                        AutoText(blog.readTime, fontSize = 10.sp, color = Color.Gray)
                    }
                }
            }
        }
    }
}