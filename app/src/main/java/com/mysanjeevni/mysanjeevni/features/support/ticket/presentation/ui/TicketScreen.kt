package com.mysanjeevni.mysanjeevni.features.support.ticket.presentation.ui

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Headset
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Sell
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.features.support.chat.presentation.viewmodel.ChatViewModel
import com.mysanjeevni.mysanjeevni.features.support.returns.data.dto.ReturnRequestDto
import com.mysanjeevni.mysanjeevni.features.support.returns.presentation.viewmodel.ReturnViewModel
import com.mysanjeevni.mysanjeevni.features.support.ticket.presentation.viewmodel.TicketViewModel
import com.mysanjeevni.mysanjeevni.utils.AutoText
import com.mysanjeevni.mysanjeevni.utils.SessionManager

private val AccentTeal = Color(0xFF1E8F73)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportCenterScreen(navController: NavController, ticketViewModel: TicketViewModel = hiltViewModel(),
                        returnViewModel: ReturnViewModel = hiltViewModel(),
                        chatViewModel: ChatViewModel = hiltViewModel()

) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Returns", "Tickets", "Chat")
    val context = LocalContext.current

    val sessionManager = remember {
        SessionManager(context)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(modifier = Modifier.fillMaxSize()) {

            // ---------- HEADER ----------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = AccentTeal
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Column(modifier = Modifier.weight(1f)) {
                    AutoText(
                        text = "Support Center",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    AutoText(
                        text = "We're here to help you",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = Icons.Filled.Headset,
                    contentDescription = null,
                    tint = AccentTeal,
                    modifier = Modifier.size(28.dp)
                )
            }

            // ---------- CUSTOM TAB ROW ----------
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 1.dp
            ) {
                val tabIcons = listOf(
                    Icons.Filled.Inventory2,
                    Icons.Filled.ConfirmationNumber,
                    Icons.Filled.ChatBubbleOutline
                )

                Row(modifier = Modifier.fillMaxWidth()) {
                    tabs.forEachIndexed { index, title ->
                        val selected = selectedTab == index
                        val tint = if (selected) AccentTeal else MaterialTheme.colorScheme.onSurfaceVariant

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedTab = index }
                                .padding(vertical = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = tabIcons[index],
                                contentDescription = title,
                                tint = tint,
                                modifier = Modifier.size(26.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            AutoText(
                                text = title,
                                color = tint,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                                style = MaterialTheme.typography.bodyMedium
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Box(
                                modifier = Modifier
                                    .width(36.dp)
                                    .height(3.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(if (selected) AccentTeal else Color.Transparent)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(start = 16.dp, end = 16.dp,top=16.dp, bottom = 45.dp)
            ) {

                when (selectedTab) {
                    0 -> ReturnsTab(returnViewModel = returnViewModel)

                    1 -> TicketsTab(ticketViewModel = ticketViewModel)

                    2 -> ChatTab(
                        viewModel = chatViewModel,
                        userId = sessionManager.getUserId() ?: "",
                        userName = sessionManager.getUserName() ?: ""
                    )
                }
            }
        }
    }
}

@Composable
fun ReturnsTab(
    returnViewModel: ReturnViewModel
) {

    val uiState by returnViewModel.uiState.collectAsState()

    val context = LocalContext.current

    val sessionManager = remember {
        SessionManager(context)
    }

    var orderId by remember {
        mutableStateOf("")
    }

    var productName by remember {
        mutableStateOf("")
    }

    var reason by remember {
        mutableStateOf("")
    }

    var resolution by remember {
        mutableStateOf("refund")
    }

    LaunchedEffect(Unit) {
        returnViewModel.getReturns(
            sessionManager.getUserId() ?: ""
        )
    }

    Column {

        BannerCard(
            icon = Icons.Filled.Replay,
            title = "Raise a Return Request",
            description = "Let us know what went wrong and our team will help you with the return process."
        )

        Spacer(modifier = Modifier.height(16.dp))

        SupportFieldCard(
            icon = Icons.Filled.Description,
            label = "Order ID",
            value = orderId,
            onValueChange = {
                orderId = it
            },
            placeholder = "Enter your order ID"
        )

        Spacer(modifier = Modifier.height(12.dp))

        SupportFieldCard(
            icon = Icons.Filled.ShoppingBag,
            label = "Product Name",
            value = productName,
            onValueChange = {
                productName = it
            },
            placeholder = "Enter product name"
        )

        Spacer(modifier = Modifier.height(12.dp))

        SupportFieldCard(
            icon = Icons.Filled.Sell,
            label = "Reason",
            value = reason,
            onValueChange = {
                reason = it
            },
            placeholder = "Why are you returning?"
        )

        Spacer(modifier = Modifier.height(12.dp))

        SupportFieldCard(
            icon = Icons.Filled.Shield,
            label = "Preferred Resolution",
            value = resolution,
            onValueChange = {
                resolution = it
            },
            placeholder = "refund / replacement"
        )

        Spacer(modifier = Modifier.height(16.dp))

        InfoNoteCard(
            icon = Icons.Filled.CheckCircle,
            text = "Our support team will review your request and get back to you."
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            CircularProgressIndicator()

            Spacer(modifier = Modifier.height(12.dp))
        }

        PrimaryButton(
            text = "Submit Return Request",
            icon = Icons.Filled.AssignmentTurnedIn,
            onClick = {

                if (
                    orderId.isBlank() ||
                    productName.isBlank() ||
                    reason.isBlank()
                ) {
                    return@PrimaryButton
                }

                returnViewModel.submitReturn(
                    ReturnRequestDto(
                        userId = sessionManager.getUserId() ?: "",
                        userName = sessionManager.getUserName() ?: "",
                        userEmail = sessionManager.getUserEmail() ?: "",
                        orderId = orderId,
                        productName = productName,
                        reason = reason,
                        preferredResolution = resolution
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AutoText(
            text = "Return History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.returns.isEmpty()) {

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {

                AutoText(
                    text = "No return requests found",
                    modifier = Modifier.padding(16.dp)
                )
            }

        } else {

            uiState.returns.forEach { item ->

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    shape = RoundedCornerShape(14.dp),
                    tonalElevation = 2.dp
                ) {

                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {

                        AutoText(
                            text = item.productName,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        AutoText(
                            text = "Order ID: ${item.orderId}"
                        )

                        AutoText(
                            text = "Reason: ${item.reason}"
                        )

                        AutoText(
                            text = "Resolution: ${item.preferredResolution}"
                        )

                        AutoText(
                            text = "Status: ${item.status}",
                            color = AccentTeal,
                            fontWeight = FontWeight.Bold
                        )

                        if (item.supportNote.isNotBlank()) {

                            Spacer(modifier = Modifier.height(4.dp))

                            AutoText(
                                text = "Support Note: ${item.supportNote}"
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketsTab(
    ticketViewModel: TicketViewModel
) {

    val uiState by ticketViewModel.uiState.collectAsState()

    val context = LocalContext.current

    val sessionManager = remember {
        SessionManager(context)
    }

    var selectedCategory by remember {
        mutableStateOf("General")
    }

    var subject by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        ticketViewModel.getTickets(
            userId = sessionManager.getUserId() ?: ""
        )
    }

    LaunchedEffect(uiState.successMessage) {

        if (!uiState.successMessage.isNullOrEmpty()) {

            subject = ""
            message = ""

            ticketViewModel.getTickets(
                sessionManager.getUserId() ?: ""
            )

            ticketViewModel.clearMessages()
        }
    }

    Column {

        BannerCard(
            icon = Icons.Filled.ConfirmationNumber,
            title = "Raise a Ticket",
            description = "Tell us about your issue and our support team will get back to you as soon as possible."
        )

        Spacer(modifier = Modifier.height(16.dp))

        // ---------------- CATEGORY ----------------

        Surface(
            shape = RoundedCornerShape(14.dp),
            color = MaterialTheme.colorScheme.surface,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outlineVariant
            ),
            modifier = Modifier.fillMaxWidth()
        ) {

            Column(
                modifier = Modifier.padding(12.dp)
            ) {

                AutoText(
                    text = "Category",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = {
                        expanded = !expanded
                    }
                ) {

                    OutlinedTextField(
                        value = selectedCategory,
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = expanded
                            )
                        }
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = {
                            expanded = false
                        }
                    ) {

                        listOf(
                            "General",
                            "Order",
                            "Payment",
                            "Doctor",
                            "Account",
                            "Lab Test"
                        ).forEach { category ->

                            DropdownMenuItem(
                                text = {
                                    AutoText(category)
                                },
                                onClick = {
                                    selectedCategory = category
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // ---------------- SUBJECT ----------------

        SupportFieldCard(
            icon = Icons.Filled.Description,
            label = "Subject",
            value = subject,
            onValueChange = {
                subject = it
            },
            placeholder = "Enter subject"
        )

        Spacer(modifier = Modifier.height(12.dp))

        // ---------------- MESSAGE ----------------

        SupportFieldCard(
            icon = Icons.Filled.Description,
            label = "Describe your issue",
            value = message,
            onValueChange = {
                message = it
            },
            placeholder = "Describe your issue",
            minHeight = 120.dp
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {

            CircularProgressIndicator()

            Spacer(modifier = Modifier.height(12.dp))
        }

        // ---------------- SUBMIT ----------------

        PrimaryButton(
            text = "Submit Ticket",
            icon = Icons.Filled.AssignmentTurnedIn,
            onClick = {

                if (
                    subject.isBlank() ||
                    message.isBlank()
                ) {
                    return@PrimaryButton
                }

                ticketViewModel.raiseTicket(
                    userId = sessionManager.getUserId() ?: "",
                    userName = sessionManager.getUserName() ?: "",
                    email = sessionManager.getUserEmail() ?: "",
                    role = sessionManager.getUserRole() ?: "",
                    category = selectedCategory.lowercase(),
                    subject = subject,
                    message = message
                )
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AutoText(
            text = "Ticket History",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.tickets.isEmpty()) {

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {

                AutoText(
                    text = "No tickets raised yet",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(16.dp)
                )
            }

        } else {

            Column {

                uiState.tickets.forEach { ticket ->

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        shape = RoundedCornerShape(14.dp),
                        tonalElevation = 2.dp
                    ) {

                        Column(
                            modifier = Modifier.padding(14.dp)
                        ) {

                            AutoText(
                                text = ticket.subject,
                                fontWeight = FontWeight.Bold,
                                style = MaterialTheme.typography.titleSmall
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            AutoText(
                                text = "Category: ${ticket.category}",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            AutoText(
                                text = "Status: ${ticket.status}",
                                color = AccentTeal,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            AutoText(
                                text = ticket.message,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun ChatTab(
    viewModel: ChatViewModel,
    userId: String,
    userName: String
) {

    var message by remember { mutableStateOf("") }

    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadMessages(userId)
    }

    Column {

        BannerCard(
            icon = Icons.Filled.ChatBubbleOutline,
            title = "Live Support Chat",
            description = "Chat with our support team in real time and get instant help."
        )

        Spacer(modifier = Modifier.height(16.dp))

        SupportFieldCard(
            icon = Icons.Filled.Description,
            label = "Message",
            value = message,
            onValueChange = {
                message = it
            },
            placeholder = "Type your message"
        )

        Spacer(modifier = Modifier.height(12.dp))

        PrimaryButton(
            text = "Send Message",
            icon = Icons.AutoMirrored.Filled.Send,
            onClick = {

                if (message.isBlank()) return@PrimaryButton

                viewModel.sendMessage(
                    userId = userId,
                    userName = userName,
                    message = message
                )

                message = ""
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AutoText(
            text = "Conversation",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (uiState.isLoading) {

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }

        } else {

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(uiState.messages) { chat ->

                    val isUser = chat.sender.equals(
                        "user",
                        ignoreCase = true
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            if (isUser)
                                Arrangement.End
                            else
                                Arrangement.Start
                    ) {

                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color =
                                if (isUser)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.surfaceVariant
                        ) {

                            AutoText(
                                text = chat.message,
                                modifier = Modifier.padding(12.dp),
                                color =
                                    if (isUser)
                                        MaterialTheme.colorScheme.onPrimary
                                    else
                                        MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// REUSABLE UI PIECES
// ---------------------------------------------------------------------------

@Composable
private fun BannerCard(
    icon: ImageVector,
    title: String,
    description: String
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = AccentTeal.copy(alpha = 0.10f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {

            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AccentTeal.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AccentTeal,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                AutoText(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.height(6.dp))
                AutoText(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SupportFieldCard(
    icon: ImageVector,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    showDropdownIcon: Boolean = false,
    minHeight: androidx.compose.ui.unit.Dp = 0.dp
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surface,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {

            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AccentTeal.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AccentTeal,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                AutoText(
                    text = label,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(2.dp))

                OutlinedTextField(
                    value = value,
                    onValueChange = onValueChange,
                    placeholder = {
                        AutoText(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    singleLine = minHeight == 0.dp,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .then(
                            if (minHeight > 0.dp) Modifier.height(minHeight) else Modifier
                        )
                )
            }

            if (showDropdownIcon) {
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun InfoNoteCard(icon: ImageVector, text: String) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = AccentTeal.copy(alpha = 0.10f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(AccentTeal.copy(alpha = 0.18f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = AccentTeal,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            AutoText(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PrimaryButton(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AccentTeal),
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        AutoText(
            text = text,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}