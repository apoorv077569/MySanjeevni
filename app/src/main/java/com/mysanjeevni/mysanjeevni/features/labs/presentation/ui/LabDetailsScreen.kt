package com.mysanjeevni.mysanjeevni.features.labs.presentation.ui

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mysanjeevni.mysanjeevni.app.LocalIsDarkTheme
import com.mysanjeevni.mysanjeevni.features.labs.presentation.state.LabDetailState
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.LabBottomBar
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.LabHeaderSection
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.LabInfoSection
import com.mysanjeevni.mysanjeevni.features.labs.presentation.ui.components.LabTestsSection
import com.mysanjeevni.mysanjeevni.utils.AutoText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabDetailScreen(
    navController: NavController,
    state: LabDetailState,
    onAddToCart: () -> Unit
) {

    val isDark = LocalIsDarkTheme.current

    val screenBg =
        if (isDark) Color(0xFF121212)
        else Color(0xFFF5F5F5)

    val topBarBg =
        if (isDark) Color(0xFF1E1E1E)
        else Color.White

    val onTopBar =
        if (isDark) Color.White
        else Color.Black

    if (state.isLoading) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                CircularProgressIndicator()

                Spacer(modifier = Modifier.height(12.dp))

                AutoText(
                    text = "Loading test details...",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        return
    }

// Error State
    if (state.error.isNotEmpty()) {

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            AutoText(
                text = state.error,
                color = MaterialTheme.colorScheme.error
            )
        }

        return
    }

    val labTest = state.labTestDetail ?: return

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AutoText(
                        text = "Lab Test Details",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    val context = LocalContext.current
                    IconButton(
                        onClick = {

                            val shareIntent = Intent(Intent.ACTION_SEND).apply {

                                type = "text/plain"

                                putExtra(
                                    Intent.EXTRA_SUBJECT,
                                    labTest.name
                                )

                                putExtra(
                                    Intent.EXTRA_TEXT,
                                    """
                                🧪 ${labTest.name}
                                    Price: ₹${labTest.price}
                                    Book this test on MySanjeevni:
                                    https://www.mysanjeevni.com/labtest/${labTest.id}
                                    Download MySanjeevni:
                                    https://www.mysanjeevni.com
                                    """.trimIndent()
                                )
                            }
                            context.startActivity(
                                Intent.createChooser(
                                    shareIntent,
                                    "Share Lab Test"
                                )
                            )
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Share,
                            contentDescription = "Share"
                        )
                    }

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarBg,
                    titleContentColor = onTopBar,
                    navigationIconContentColor = onTopBar,
                    actionIconContentColor = onTopBar
                )
            )
        },
        bottomBar = {
            LabBottomBar(
                price = labTest.price,
                originalPrice = labTest.mrp,
                testCount = labTest.testsIncluded.size,
                onAddToCart = onAddToCart
            )
        },
        containerColor = screenBg
    ) { padding ->

        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            item {
                LabHeaderSection(labTest)
            }

            item {
                LabInfoSection(labTest)
            }

            item {
                LabTestsSection(
                    tests = labTest.testsIncluded
                )
            }
        }
    }
}