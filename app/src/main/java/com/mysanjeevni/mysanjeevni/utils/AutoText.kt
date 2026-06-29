package com.mysanjeevni.mysanjeevni.utils

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import com.mysanjeevni.mysanjeevni.app.LocalAppLanguage

@Composable
fun AutoText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    textDecoration: TextDecoration? = null,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val targetCode = LocalAppLanguage.current

    val finalStyle = style.copy(
        textDecoration = textDecoration,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight
    )

    // English - No translation needed
    if (targetCode == "en") {
        Text(
            text = text,
            modifier = modifier,
            color = color,
            fontSize = fontSize,
            fontWeight = fontWeight,
            textAlign = textAlign,
            maxLines = maxLines,
            overflow = overflow,
            style = finalStyle
        )
        return
    }

    var translatedText by remember(text, targetCode) {
        mutableStateOf(text)
    }

    LaunchedEffect(text, targetCode) {
        TranslatorManager.translate(text, targetCode) { result ->
            translatedText = result
        }
    }

    Text(
        text = translatedText,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow,
        style = finalStyle
    )
}


@Composable
fun AutoText(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    style: TextStyle = LocalTextStyle.current,
    color: Color = Color.Unspecified,
    fontWeight: FontWeight? = null,
    textAlign: TextAlign = TextAlign.Unspecified,
    maxLines: Int = Int.MAX_VALUE,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    lineHeight: TextUnit = TextUnit.Unspecified,
    overflow: TextOverflow = TextOverflow.Clip,
    textDecoration: TextDecoration? = null,
    fontSize: TextUnit = TextUnit.Unspecified
) {
    val finalStyle = style.copy(
        textDecoration = textDecoration,
        letterSpacing = letterSpacing,
        lineHeight = lineHeight
    )

    Text(
        text = text,
        modifier = modifier,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = textAlign,
        maxLines = maxLines,
        overflow = overflow,
        style = finalStyle
    )
}