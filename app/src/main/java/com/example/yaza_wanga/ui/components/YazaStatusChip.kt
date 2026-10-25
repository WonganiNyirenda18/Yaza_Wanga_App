package com.example.yaza_wanga.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.theme.Yaza_WangaTheme

@Composable
fun YazaStatusChip(
    status: String,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor) = when (status.uppercase()) {
        "ACTIVE", "VERIFIED", "APPROVED", "FREE" -> Color(0xFFE6F4EA) to Color(0xFF137333)
        "PENDING", "DRAFT" -> Color(0xFFFEF3C7) to Color(0xFFD97706)
        "SUSPENDED", "FLAGGED", "QUARANTINED", "REJECTED" -> Color(0xFFFEF2F2) to Color(0xFFDC2626)
        else -> Color(0xFFF1F5F9) to Color(0xFF64748B)
    }

    Surface(
        shape = RoundedCornerShape(50.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Text(
            text = status,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun YazaStatusChipPreview() {
    Yaza_WangaTheme {
        YazaStatusChip(status = "ACTIVE")
    }
}
