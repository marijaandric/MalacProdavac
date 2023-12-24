package com.example.front.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Paginator(
    currentPage: Int,
    totalPages: Int,
    onPageSelected: (Int) -> Unit
) {
    if(totalPages > 1)
    {
        Column(
            modifier = Modifier.padding(bottom=16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp, top = 0.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if(currentPage != 1)
                {
                    IconButton(
                        onClick = {
                            if (currentPage > 1) {
                                onPageSelected(currentPage - 1)
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Previous Page", tint = MaterialTheme.colorScheme.secondary)
                    }
                }


                val visiblePages = calculateVisiblePages(currentPage, totalPages)

                for (page in visiblePages) {
                    PageIndicator(page, currentPage, onPageSelected)
                }

                if(currentPage != totalPages)
                {
                    IconButton(
                        onClick = {
                            if (currentPage < totalPages) {
                                onPageSelected(currentPage + 1)
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "Next Page", tint = MaterialTheme.colorScheme.secondary)
                    }
                }

            }
            Text(
                text = "Page $currentPage of $totalPages",
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                color = Color.Gray
            )
        }
    }
}

@Composable
fun PageIndicator(
    page: Int,
    currentPage: Int,
    onPageSelected: (Int) -> Unit
) {
    val textColor = if (page == currentPage) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.tertiary
    Card(
        shape = CircleShape,
        modifier = Modifier
            .width(40.dp)
            .height(40.dp)
    ){
        Box(
            modifier = Modifier
                .background(color = if (page == currentPage) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.background)
                .width(40.dp)
                .height(40.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = page.toString(),
                color = textColor,
                modifier = Modifier
                    .clickable { onPageSelected(page) }
                    .padding(8.dp)
            )
        }
    }

}

private fun calculateVisiblePages(currentPage: Int, totalPages: Int): List<Int> {
    return when {
        totalPages <= 5 -> {
            (1..totalPages).toList()
        }
        currentPage <= 3 -> {
            (1..5).toList()
        }
        currentPage >= totalPages - 2 -> {
            (totalPages - 4..totalPages).toList()
        }
        else -> {
            (currentPage - 2..currentPage + 2).toList()
        }
    }
}