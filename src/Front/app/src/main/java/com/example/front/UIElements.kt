package com.example.front

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class UIElements {

    @Composable
    fun MediumBlueButton(text:String,onClick: () -> Unit,width:Float) {
        val primaryColor = MaterialTheme.colorScheme.primary
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth(width)
                .padding(8.dp),
                //.background(Color.Blue)
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            shape = RoundedCornerShape(20)
        ) {
            Text(text = text)
        }
    }
}