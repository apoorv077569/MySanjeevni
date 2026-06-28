package com.mysanjeevni.mysanjeevni.features.orders.presentation.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.orders.presentation.state.OrderState
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.EmptyOrdersView
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.ErrorView
import com.mysanjeevni.mysanjeevni.features.orders.presentation.ui.components.OrderCard
import com.mysanjeevni.mysanjeevni.features.orders.presentation.viewmodel.OrderViewModel
import com.mysanjeevni.mysanjeevni.utils.SessionManager

@Composable
fun OrdersScreen(
    navController: NavController,
    viewModel: OrderViewModel = hiltViewModel()
) {

    val sessionManager = SessionManager(LocalContext.current)
    val orderState by viewModel.orderState.collectAsStateWithLifecycle()
    val userId = sessionManager.getUserId()

    var selectedStatus by remember { mutableStateOf<String?>(null) }
    var showFilterSheet by remember { mutableStateOf(false) }

    LaunchedEffect(userId) {
        viewModel.getOrders(userId)
    }
    LaunchedEffect(orderState) {
        Log.d("ORDER_TIME","UI Received State = ${System.currentTimeMillis()}")
    }


    when (orderState) {

        is OrderState.Loading -> {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    CircularProgressIndicator()

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )

                    Text(
                        text = "Loading Orders..."
                    )
                }
            }

            return
        }

        else -> Unit
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            OrdersHeader(
                onBackClick = { navController.popBackStack() },
                isFilterActive = selectedStatus != null,
                onFilterClick = { showFilterSheet = true }
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            when (val state = orderState) {

                is OrderState.Success -> {
                    val filteredOrders = remember(state.orders, selectedStatus) {
                        if (selectedStatus == null) {
                            state.orders
                        } else {
                            state.orders.filter {
                                it.order.status.equals(selectedStatus, ignoreCase = true)
                            }
                        }
                    }

                    when {
                        state.orders.isEmpty() -> {
                            EmptyOrdersView(modifier = Modifier.align(Alignment.Center))
                        }

                        filteredOrders.isEmpty() -> {
                            EmptyOrdersView(
                                modifier = Modifier.align(Alignment.Center),
                                title = "No matching orders",
                                subtitle = "Try a different filter"
                            )
                        }

                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(filteredOrders, key = { it.order.id }) { order ->
                                    OrderCard(orderUi = order, onClick = {
                                        navController.navigate(
                                            Screen.OrderDetailScreen.createRoute(order.order.id)
                                        )
                                    })
                                }
                            }
                        }
                    }

                    if (showFilterSheet) {
                        OrderFilterSheet(
                            statuses = state.orders.map { it.order.status }.distinct(),
                            selectedStatus = selectedStatus,
                            onStatusSelected = { selectedStatus = it },
                            onDismiss = { showFilterSheet = false }
                        )
                    }                }

                is OrderState.Error -> {
                    ErrorView(
                        message = state.message,
                        onRetry = {
                            viewModel.getOrders(userId)
                        },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                else -> Unit
            }
        }
    }
}
@Composable
private fun OrdersHeader(
    onBackClick: () -> Unit,
    isFilterActive: Boolean,
    onFilterClick: () -> Unit
) {
    Surface(color = MaterialTheme.colorScheme.background) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircleIconButton(
                icon = Icons.AutoMirrored.Outlined.ArrowBack,
                contentDescription = "Back",
                onClick = onBackClick
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "My Orders",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Track and manage all your orders",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Box {
                CircleIconButton(
                    icon = Icons.Outlined.FilterList,
                    contentDescription = "Filter orders",
                    onClick = onFilterClick
                )
                if (isFilterActive) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.error)
                    )
                }
            }
        }
    }
}

@Composable
private fun CircleIconButton(
    icon: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.primaryContainer)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun OrderFilterSheet(
    statuses: List<String>,
    selectedStatus: String?,
    onStatusSelected: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        ) {
            Text(
                text = "Filter by status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(12.dp))

            FilterOptionRow(
                label = "All Orders",
                selected = selectedStatus == null,
                onClick = {
                    onStatusSelected(null)
                    onDismiss()
                }
            )

            statuses.forEach { status ->
                FilterOptionRow(
                    label = status.replaceFirstChar { it.uppercase() },
                    selected = selectedStatus.equals(status, ignoreCase = true),
                    onClick = {
                        onStatusSelected(status)
                        onDismiss()
                    }
                )
            }
        }
    }
}

@Composable
private fun FilterOptionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .background(
                if (selected) MaterialTheme.colorScheme.primaryContainer
                else Color.Transparent
            )
            .padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface
        )
        if (selected) {
            Icon(
                imageVector = Icons.Outlined.Check,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}