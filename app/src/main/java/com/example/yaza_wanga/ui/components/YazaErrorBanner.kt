package com.example.yaza_wanga.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@Composable
fun YazaErrorBanner(
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    if (errorMessage.isBlank()) return

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFFEF2F2),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ErrorOutline,
                contentDescription = null,
                tint = Color(0xFFDC2626),
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = errorMessage,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFFDC2626)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun YazaErrorBannerPreview() {
    Yaza_WangaTheme {
        YazaErrorBanner(errorMessage = "Invalid email or password")
    }
}
