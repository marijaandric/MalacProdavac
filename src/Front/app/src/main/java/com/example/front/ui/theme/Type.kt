package com.example.front.ui.theme

import androidx.compose.material.Colors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.front.R
import com.example.front.ui.*

// Set of Material typography styles to start with
val Typography = Typography(
        bodyLarge = TextStyle(
                fontFamily =FontFamily(Font(R.font.lexend)),
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                lineHeight = 24.sp,
                letterSpacing = 0.5.sp
        ),
        titleLarge = TextStyle(
                lineHeight = 35.sp,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.lexend)),
                color = Black
        ),
        titleMedium = TextStyle(
                lineHeight = 35.sp,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily(Font(R.font.lexend)),
                color = Black
        )
        ,
        labelSmall = TextStyle(
                fontFamily = FontFamily(Font(R.font.lexend)),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 0.5.sp,
                color = DarkBlue
        ),
        displaySmall = TextStyle(
                fontFamily = FontFamily(Font(R.font.lexend)),
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                letterSpacing = 0.5.sp,
                color = DarkBlue
        ),
        titleSmall = TextStyle(
                fontFamily = FontFamily(Font(R.font.lexend)),
                fontWeight = FontWeight.SemiBold,
                fontSize = 17.sp,
                letterSpacing = 0.5.sp,
                color = LightBlue
        ),
)