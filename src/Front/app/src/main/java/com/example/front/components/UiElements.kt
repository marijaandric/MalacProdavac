package com.example.front.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.example.front.R

@Composable
fun MediumBlueButton(text:String,onClick: () -> Unit,width:Float) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(width)
            .padding(8.dp)
            .height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
        shape = RoundedCornerShape(20)
    ) {
        Text(text = text, fontFamily = FontFamily(Font(R.font.lexend)))
    }
}