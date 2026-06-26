package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.app.MainActivity
import com.mysanjeevni.mysanjeevni.core.navigation.Screen
import com.mysanjeevni.mysanjeevni.features.labs.data.dto.CreateLabBookingRequestDto
import com.mysanjeevni.mysanjeevni.features.labs.domain.model.SlotsRequestParams
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.BookTestForm
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.BookingSuccessDialog
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.BookLabTestViewModel
import com.mysanjeevni.mysanjeevni.features.labs.presentation.viewmodel.LabAvailabilityViewModel
import com.mysanjeevni.mysanjeevni.features.payment.presentation.state.PaymentState
import com.mysanjeevni.mysanjeevni.features.payment.presentation.viewmodel.PaymentViewModel
import com.mysanjeevni.mysanjeevni.features.payment.utils.startRazorpayCheckout
import com.mysanjeevni.mysanjeevni.utils.convertToApiDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookTestScreen(
    navController: NavController,
    testId: String,
    testName: String,
    testPrice: Double,
    viewModel: BookLabTestViewModel,
    availabilityViewModel: LabAvailabilityViewModel = hiltViewModel()
) {
    var collectionType by remember { mutableStateOf("Home Collection") }
    var collectionDate by remember { mutableStateOf("16-06-2026") }
    var collectionTime by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var pincode by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") }
    var instructions by remember { mutableStateOf("") }
    var showValidationError by remember { mutableStateOf<String?>(null) }

    val state by viewModel.state.collectAsState()
    val availabilityState by availabilityViewModel.state.collectAsState()
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val paymentState by paymentViewModel.paymentState.collectAsState()
    val context = LocalContext.current
    val activity = context as Activity
    val showSuccessDialog = state.successMessage != null

    // ── Removed verticalScroll from here; BookTestForm already scrolls ──
    // (Previously both Screen and Form had .verticalScroll which caused nested
    //  scroll conflicts and performance issues.)

    LaunchedEffect(Unit) {
        Log.d("BOOK_SCREEN", "testId=$testId, testName=$testName, testPrice=$testPrice")
    }

//    LaunchedEffect(paymentState) {
//        when (val ps = paymentState) {
//            is PaymentState.RazorpayOrderCreated -> {
//                Log.d("RAZORPAY", "OrderId=${ps.order.id}, Amount=${ps.order.amount}")
//                startRazorpayCheckout(
//                    activity = activity,
//                    order = ps.order,
//                    onSuccess = { paymentId, orderId, signature ->
//                        Log.d("LAB_PAYMENT", "SUCCESS paymentId=$paymentId orderId=$orderId")
//                        viewModel.bookLabTest(
//                            CreateLabBookingRequestDto(
//                                testId = testId,
//                                testName = testName,
//                                testPrice = testPrice,
//                                collectionType = if (collectionType == "Home Collection") "home" else "lab",
//                                collectionDate = collectionDate,
//                                collectionTime = collectionTime,
//                                address = address,
//                                notes = instructions,
//                                razorpayOrderId = orderId,
//                                razorpayPaymentId = paymentId,
//                                razorpaySignature = signature,
//                                patientPincode = pincode,
//                                patientAge = age.toIntOrNull() ?: 0,
//                                patientGender = gender
//                            )
//                        )
//                    },
//                    onFailure = { error -> Log.e("LAB_PAYMENT", error) }
//                )
//            }
//            is PaymentState.Error -> Log.e("LAB_PAYMENT", "Error=${ps.message}")
//            else -> Unit
//        }
//    }

    var checkoutOpened by remember {
        mutableStateOf(false)
    }

    // --- REPLACE START ---
    LaunchedEffect(Unit) {
//        MainActivity.Companion.RazorpayCallbackHolder.onSuccess = { paymentId, orderId, signature ->
//            Log.d("RZP_CHECK", "Global Callback Success - Creating Booking")
//            checkoutOpened = false
//
//            viewModel.bookLabTest(
//                CreateLabBookingRequestDto(
//                    testId = testId,
//                    testName = testName,
//                    testPrice = testPrice,
//                    collectionType = if (collectionType == "Home Collection") "home" else "center",
//                    collectionDate = collectionDate,
//                    collectionTime = collectionTime,
//                    address = address,
//                    notes = instructions,
//                    razorpayOrderId = orderId,
//                    razorpayPaymentId = paymentId,
//                    razorpaySignature = signature,
//                    patientPincode = pincode,
//                    patientAge = age.toIntOrNull() ?: 0,
//                    patientGender = gender
//                )
//            )
//        }

        MainActivity.Companion.RazorpayCallbackHolder.onSuccess = { paymentId, orderId, signature ->

            Log.d("RZP_CHECK", "Payment Success Callback: $paymentId, $orderId, $signature")
            Log.d("RZP_CHECK", "Payment Success - Navigating to payment success screen")

            viewModel.setPendingRequest(
                CreateLabBookingRequestDto(
                    testId = testId,
                    testName = testName,
                    testPrice = testPrice,
                    collectionType = if (collectionType == "Home Collection") "home" else "center",
                    collectionDate = collectionDate,
                    collectionTime = collectionTime,
                    address = address,
                    notes = instructions,
                    razorpayOrderId = orderId,
                    razorpayPaymentId = paymentId,
                    razorpaySignature = signature,
                    patientPincode = pincode,
                    patientAge = age.toIntOrNull() ?: 0,
                    patientGender = gender
                )
            )
            navController.navigate(
                Screen.PaymentSuccess.createRoute(
                    flow = "lab",
                    paymentId = paymentId,
                    razorpayOrderId = orderId,
                    signature = signature
                )
            ) {
                popUpTo(Screen.BookLabTestScreen.route) { inclusive = true }
            }

        }

        MainActivity.Companion.RazorpayCallbackHolder.onFailure = { error ->
            checkoutOpened = false
            Log.e("RZP_CHECK", "Payment Failed Callback: $error")
            navController.navigate(Screen.PaymentFailed.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = false
                }
                launchSingleTop = true
            }
        }
    }

    // 2. Clean up callbacks when screen is disposed to prevent memory leaks
    DisposableEffect(Unit) {
        onDispose {
            MainActivity.Companion.RazorpayCallbackHolder.onSuccess = null
            MainActivity.Companion.RazorpayCallbackHolder.onFailure = null
        }
    }

    // 3. Launch the Checkout UI
    LaunchedEffect(paymentState) {
        val ps = paymentState
        if (ps is PaymentState.RazorpayOrderCreated && !checkoutOpened) {
            checkoutOpened = true
            Log.d("RZP_CHECK", "OPENING CHECKOUT ${ps.order.id}")

            startRazorpayCheckout(
                activity = activity,
                order = ps.order,
                onSuccess = { _, _, _ ->
                    // This local lambda is usually ignored by the SDK
                },
                onFailure = { _ ->
                    checkoutOpened = false
                }
            )
        }
    }
    // --- REPLACE END ---


    val colorScheme = MaterialTheme.colorScheme

    Scaffold(
        containerColor = colorScheme.background,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorScheme.background,
                    titleContentColor = colorScheme.onBackground
                ),
                title = {
                    Column {
                        Text(
                            "Book This Test",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = colorScheme.onBackground
                        )
                        Text(
                            "Fill in the details to schedule your test",
                            fontSize = 12.sp,
                            color = colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(colorScheme.surfaceVariant),
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Back",
                                tint = colorScheme.onSurface
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        BookTestForm(
            modifier = Modifier.padding(padding),
            testName = testName,
            testPrice = testPrice,
            testId = testId,
            collectionType = collectionType,
            onCollectionTypeChange = { collectionType = it },
            collectionDate = collectionDate,
            isLoading = paymentState is PaymentState.Loading,
            onCollectionDateChange = { newDate ->
                collectionDate = newDate
                collectionTime = ""
                availabilityViewModel.searchSlots(
                    SlotsRequestParams(
                        testId = testId,
                        testName = testName,
                        appointmentDate = convertToApiDateFormat(newDate),
                        pincode = pincode.ifBlank { "110001" },
                        patientAge = age.toIntOrNull(),
                        patientGender = when {
                            gender.equals("Male", true) -> "MALE"
                            gender.equals("Female", true) -> "FEMALE"
                            else -> "OTHER"
                        }
                    )
                )
            },
            collectionTime = collectionTime,
            onCollectionTimeChange = { collectionTime = it },
            address = address,
            onAddressChange = { address = it },
            pincode = pincode,
            onPincodeChange = { newValue ->
                if (newValue.length <= 6 && newValue.all { it.isDigit() }) {
                    pincode = newValue
                    availabilityViewModel.onPincodeChanged(testId, newValue)
                }
            },
            age = age,
            onAgeChange = { age = it },
            gender = gender,
            onGenderChange = { gender = it },
            instructions = instructions,
            onInstructionsChange = { instructions = it },
            availabilityState = availabilityState,
            showValidationError = showValidationError,
            onBackClick = { navController.popBackStack() },
            onPayClick = {
                when {
                    collectionDate.isBlank() ->
                        showValidationError = "Please select collection date"

                    collectionTime.isBlank() ->
                        showValidationError = "Please select collection time"

                    collectionType == "Home Collection" && address.isBlank() ->
                        showValidationError = "Please enter address"

                    pincode.length != 6 ->
                        showValidationError = "Please enter valid pincode"

                    age.isBlank() ->
                        showValidationError = "Please enter age"

                    else -> {
                        showValidationError = null
                        Log.d("LAB_BOOKING_UI", "Creating Razorpay Order for ₹$testPrice")
                        paymentViewModel.createRazorpayOrder(
                            amount = testPrice.toInt(),
                            receipt = "LAB_${System.currentTimeMillis()}"
                        )
                    }
                }
            }
        )
    }

    if (showSuccessDialog) {
        BookingSuccessDialog(
            testName = testName,
            collectionDate = collectionDate,
            collectionTime = collectionTime,
            onViewBooking = {
                viewModel.clearMessage()
                navController.navigate(Screen.BookingHistoryScreen.route) {
                    popUpTo(Screen.Home.route)
                }
            },
            onClose = {
                viewModel.clearMessage()
                navController.popBackStack()
            }
        )
    }
}