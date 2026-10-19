package com.example.yaza_wanga.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun YazaWangaLogo(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.22f))
            .background(Color(0xFF0C24A0)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size * 0.7f)) {
            val w = size.toPx() * 0.7f
            val h = size.toPx() * 0.7f

            // White Y-person head
            drawCircle(
                color = Color.White,
                radius = w * 0.18f,
                center = Offset(w * 0.32f, h * 0.22f)
            )

            // White Y-person body / arms path
            val yPath = Path().apply {
                moveTo(w * 0.12f, h * 0.38f)
                lineTo(w * 0.28f, h * 0.38f)
                lineTo(w * 0.38f, h * 0.55f)
                lineTo(w * 0.38f, h * 0.88f)
                arcTo(
                    rect = Rect(w * 0.22f, h * 0.78f, w * 0.38f, h * 0.88f),
                    startAngleDegrees = 0f,
                    sweepAngleDegrees = 90f,
                    forceMoveTo = false
                )
                lineTo(w * 0.22f, h * 0.88f)
                lineTo(w * 0.22f, h * 0.55f)
                close()
            }
            drawPath(yPath, Color.White, style = Fill)

            // Right arm of Y
            val yArm2 = Path().apply {
                moveTo(w * 0.38f, h * 0.38f)
                lineTo(w * 0.54f, h * 0.38f)
                lineTo(w * 0.44f, h * 0.55f)
                lineTo(w * 0.38f, h * 0.55f)
                close()
            }
            drawPath(yArm2, Color.White, style = Fill)

            // Blue W-person head
            drawCircle(
                color = Color(0xFF3B82F6),
                radius = w * 0.16f,
                center = androidx.compose.ui.geometry.Offset(w * 0.72f, h * 0.26f)
            )

            // Blue W / wave body path
            val wPath = Path().apply {
                moveTo(w * 0.48f, h * 0.50f)
                lineTo(w * 0.62f, h * 0.72f)
                lineTo(w * 0.72f, h * 0.58f)
                lineTo(w * 0.88f, h * 0.85f)
                lineTo(w * 0.72f, h * 0.85f)
                lineTo(w * 0.62f, h * 0.68f)
                lineTo(w * 0.54f, h * 0.78f)
                lineTo(w * 0.48f, h * 0.65f)
                close()
            }
            drawPath(wPath, Color(0xFF3B82F6), style = Fill)
        }
    }
}
