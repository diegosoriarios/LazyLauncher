package com.diego.dlauncher.screens

import android.graphics.Typeface
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontSynthesis
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView

@Preview(showBackground = true)
@Composable
fun MyClock(
    modifier: Modifier = Modifier,
    format: String = "kk:mm:ss",
    color: Color = Color.Unspecified,
    style: TextStyle = MaterialTheme.typography.labelLarge,
) {
    val textColor = color.takeOrElse {
        style.color.takeOrElse {
            LocalContentColor.current
        }
    }

    val resolver = LocalFontFamilyResolver.current
    val face: Typeface = remember(resolver, style) {
        resolver.resolve(
            fontFamily = style.fontFamily,
            fontWeight = FontWeight.Bold,
            fontStyle = style.fontStyle ?: FontStyle.Normal,
            fontSynthesis = style.fontSynthesis ?: FontSynthesis.All
        )
    }.value as Typeface

    AndroidView(
        modifier = modifier.fillMaxWidth().wrapContentSize(Alignment.Center),
        factory = { context ->
            android.widget.TextClock(context).apply {
                format24Hour?.let {
                    this.format24Hour = format
                }

                format12Hour?.let {
                    this.format12Hour = format
                }

                timeZone?.let { this.timeZone = it }
                textSize.let { this.textSize = 48.0f }

                setTextColor(textColor.toArgb())
                typeface = face
            }
        }
    )
}