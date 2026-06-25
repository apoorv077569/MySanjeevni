package com.mysanjeevni.mysanjeevni.utils.dilaog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ReviewGreen = Color(0xFF00897B)

@Composable
fun AddReviewDialog(
    initialRating: Int = 5,
    initialTitle: String = "",
    initialComment: String = "",
    onSubmit: (Int, String, String) -> Unit,
    onDismiss: () -> Unit
) {
    var rating by remember { mutableIntStateOf(initialRating) }
    var title by remember { mutableStateOf(initialTitle) }
    var comment by remember { mutableStateOf(initialComment) }

    val colorScheme = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = colorScheme.surface,

        confirmButton = {
            Button(
                onClick = {
                    onSubmit(
                        rating,
                        title.trim(),
                        comment.trim()
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = ReviewGreen
                ),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text(
                    text = "Submit",
                    color = colorScheme.onPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp
                )
            }
        },

        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(
                    text = "Cancel",
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 15.sp
                )
            }
        },

        title = {
            Text(
                text = "Review Product",
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                color = colorScheme.onSurface
            )
        },

        text = {
            Column {

                Text(
                    text = "Your Rating",
                    fontSize = 14.sp,
                    color = colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                Row {
                    for (i in 1..5) {
                        Icon(
                            imageVector =
                                if (i <= rating)
                                    Icons.Filled.Star
                                else
                                    Icons.Outlined.StarBorder,
                            contentDescription = null,
                            tint = Color(0xFFFFC107),
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    rating = i
                                }
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                TextField(
                    value = title,
                    onValueChange = {
                        title = it
                    },
                    placeholder = {
                        Text(
                            text = "Title",
                            color = colorScheme.onSurfaceVariant,
                            fontSize = 15.sp
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = colorScheme.primary,
                        unfocusedIndicatorColor = colorScheme.outline,
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        cursorColor = colorScheme.primary
                    ),
                    singleLine = true
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )

                TextField(
                    value = comment,
                    onValueChange = {
                        comment = it
                    },
                    placeholder = {
                        Text(
                            text = "Comment",
                            color = colorScheme.onSurfaceVariant,
                            fontSize = 15.sp
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = colorScheme.surfaceVariant,
                        unfocusedContainerColor = colorScheme.surfaceVariant,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = colorScheme.onSurface,
                        unfocusedTextColor = colorScheme.onSurface,
                        cursorColor = colorScheme.primary
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 4
                )
            }
        }
    )
}