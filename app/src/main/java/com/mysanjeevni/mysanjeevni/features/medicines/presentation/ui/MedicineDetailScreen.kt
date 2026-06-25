package com.mysanjeevni.mysanjeevni.features.medicines.presentation.ui

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.cart.domain.model.CartItem
import com.mysanjeevni.mysanjeevni.features.cart.presentation.viewmodel.CartViewModel
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.components.MedicineBottomBar
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.components.MedicineCard
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.components.MedicinePriceCard
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.components.MedicineStockCard
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.components.MedicineTabSection
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.components.RatingSummaryCard
import com.mysanjeevni.mysanjeevni.features.medicines.presentation.viewmodel.MedicineViewModel
import com.mysanjeevni.mysanjeevni.features.orders.presntation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.features.review.domain.model.Review
import com.mysanjeevni.mysanjeevni.features.review.presentation.ui.ReviewSection
import com.mysanjeevni.mysanjeevni.features.review.presentation.viewmodel.ReviewViewModel
import com.mysanjeevni.mysanjeevni.features.wishlist.presentation.viewmodel.WishlistViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.SessionManager
import com.mysanjeevni.mysanjeevni.utils.dilaog.AddReviewDialog

@Composable
fun MedicineDetailScreen(
    navController: NavController,
    id: String,
    viewModel: MedicineViewModel = hiltViewModel(),
    wishlistViewModel: WishlistViewModel = hiltViewModel(),
    orderViewModel: OrderViewModel

) {
    val state by viewModel.state.collectAsState()
    val wishlistState by wishlistViewModel.state.collectAsState()
    val reviewViewModel: ReviewViewModel = hiltViewModel()
    val cartViewModel: CartViewModel = hiltViewModel()
    val reviewState by reviewViewModel.state.collectAsState()
    var showReviewDialog by remember { mutableStateOf(false) }
    var showEditReviewDialog by remember { mutableStateOf(false) }
    var selectedReview by remember { mutableStateOf<Review?>(null) }
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val currentUserId = sessionManager.getUserId()

    Log.d(
        "ORDER_VM",
        "Medicine VM = ${orderViewModel.hashCode()}"
    )

    LaunchedEffect(id) {
        viewModel.getMedicineById(id)
        reviewViewModel.loadReviews(id)
    }

    if (state.isLoading) {
        AutoText(
            text = "Loading...",
            color = MaterialTheme.colorScheme.onSurface
        )
        return
    }

    state.error?.let {
        AutoText(
            text = it,
            color = MaterialTheme.colorScheme.error
        )
        return
    }

    val medicine = state.selectedMedicine ?: return
    val isWishlisted = wishlistState.items.any {
        it.productId == medicine.id
    }

    Scaffold(
        bottomBar = {
            MedicineBottomBar(
                onAddToCart = {
                    Log.d(
                        "CART_ADD",
                        "Adding ${medicine.name}"
                    )
                    cartViewModel.addToCart(
                        CartItem(
                            id = medicine.id,
                            name = medicine.name,
                            price = medicine.price,
                            originalPrice = medicine.mrp,
                            imageUrl = medicine.image,
                            qty = medicine.quantity
                        ),
                    )
                    navController.navigate(Screen.CartScreen.route)
                },
                onBuyNow = {
                    orderViewModel.setMedicine(medicine)
                    navController.navigate(
                        "${Screen.ManageAddresses.route}?checkout=true&home=false"
                    )
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {

            // ── Top Bar: Back + Wishlist + Share ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                AutoText(
                    text = "Back to Medicines",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { navController.popBackStack() }
                )
                IconButton(
                    onClick = {
                        wishlistViewModel.addToWishlist(
                            productId = medicine.id,
                            productName = medicine.name,
                            price = medicine.price,
                            image = medicine.image
                        )
                    }
                ) {
                    Icon(
                        imageVector = if (isWishlisted)
                            Icons.Default.Favorite
                        else
                            Icons.Default.FavoriteBorder,

                        contentDescription = "Wishlist",

                        tint = if (isWishlisted)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
                val context = LocalContext.current

                IconButton(
                    onClick = {

                        val shareIntent = Intent(Intent.ACTION_SEND).apply {

                            type = "text/plain"

                            putExtra(
                                Intent.EXTRA_SUBJECT,
                                medicine.name
                            )

                            putExtra(
                                Intent.EXTRA_TEXT,
                                """
                ${medicine.name}
                
                Price: ₹${medicine.price}
                
                Check this medicine on MySanjeevni:
                https://www.mysanjeevni.com/medicine/${medicine.id}
                Download MySanjeevni:
                https://www.mysanjeevni.com
                """.trimIndent()
                            )
                        }
                        context.startActivity(
                            Intent.createChooser(
                                shareIntent,
                                "Share Medicine"
                            )
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share"
                    )
                }
            }

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                MedicineCard(medicine = medicine)

                Spacer(Modifier.height(12.dp))

                MedicinePriceCard(
                    price = medicine.price,
                    mrp = medicine.mrp
                )

                Spacer(Modifier.height(12.dp))

                MedicineStockCard(stock = medicine.stock)

                Spacer(Modifier.height(16.dp))

                MedicineTabSection(
                    description = medicine.description,
                    specifications = medicine.specifications,
                    safetyInformation = medicine.safetyInformation
                )

                Spacer(Modifier.height(24.dp))

                RatingSummaryCard(
                    rating = medicine.rating,
                    reviews = medicine.reviews
                )

                Spacer(Modifier.height(16.dp))

                when {
                    reviewState.isLoading -> AutoText(
                        text = "Loading reviews...",
                        color = MaterialTheme.colorScheme.onSurface)
                    reviewState.error != null -> AutoText(
                        text = reviewState.error!!,
                        color = MaterialTheme.colorScheme.error)
                    else -> {
                        ReviewSection(
                            reviews = reviewState.reviews,
                            currentUserId = currentUserId,
                            onAddReview = { showReviewDialog = true },
                            onEditReview = { review ->
                                selectedReview = review
                                showEditReviewDialog = true
                            }
                        )
                    }
                }

                if (showReviewDialog) {
                    AddReviewDialog(
                        onSubmit = { rating, title, comment ->
                            showReviewDialog = false
                            reviewViewModel.addReview(
                                userId = sessionManager.getUserId() ?: "",
                                productId = medicine.id,
                                rating = rating,
                                title = title,
                                comment = comment,
                                userName = sessionManager.getUserName() ?: "User"
                            )
                        },
                        onDismiss = { showReviewDialog = false }
                    )
                }

                if (showEditReviewDialog && selectedReview != null) {
                    AddReviewDialog(
                        initialRating = selectedReview!!.rating,
                        initialTitle = selectedReview!!.title,
                        initialComment = selectedReview!!.comment,
                        onSubmit = { rating, title, comment ->
                            reviewViewModel.updateReview(
                                reviewId = selectedReview?.id ?: "",
                                userId = currentUserId ?: "",
                                productId = selectedReview?.productId ?: "",
                                rating = rating,
                                title = title,
                                comment = comment
                            )
                            showEditReviewDialog = false
                        },
                        onDismiss = { showEditReviewDialog = false }
                    )
                }

                Spacer(Modifier.height(80.dp))
            }
        }
    }
}