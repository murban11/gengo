package com.example.gengo.ui.theme

import androidx.annotation.StringRes
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.gengo.R

enum class FontSizePrefs(
    @StringRes val key: Int,
    val fontSizeExtra: Int,
) {
    SMALL(R.string.small, -2),
    DEFAULT(R.string.default_, 0),
    LARGE(R.string.large, 2);
}

private const val LINE_HEIGHT_MULTIPLIER = 1.15

fun getPersonalizedTypography(fontSizePrefs: FontSizePrefs, color: Color): Typography {
    return Typography(
        bodyLarge = TextStyle(
            color = color,
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (16 + fontSizePrefs.fontSizeExtra).sp,
            lineHeight = ((16 + fontSizePrefs.fontSizeExtra) * LINE_HEIGHT_MULTIPLIER).sp,
            letterSpacing = 0.5.sp
        ),
        titleLarge = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = (22 + fontSizePrefs.fontSizeExtra).sp,
            lineHeight = ((22 + fontSizePrefs.fontSizeExtra) * LINE_HEIGHT_MULTIPLIER).sp,
            letterSpacing = 0.sp
        ),
        labelSmall = TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Medium,
            fontSize = (11 + fontSizePrefs.fontSizeExtra).sp,
            lineHeight = ((16 + fontSizePrefs.fontSizeExtra) * LINE_HEIGHT_MULTIPLIER).sp,
            letterSpacing = 0.5.sp
        )
    )
}