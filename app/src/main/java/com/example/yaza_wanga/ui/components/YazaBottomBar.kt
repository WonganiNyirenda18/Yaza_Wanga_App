package com.example.yaza_wanga.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun YazaBottomBar(
    tabs: List<Triple<Int, String, ImageVector>>,
    currentTab: Int,
    onSelectTab: (Int) -> Unit
) {
    val primaryBlue = Color(0xFF1D4ED8)
    val textGray = Color(0xFF64748B)

    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        tabs.forEach { (index, label, icon) ->
            val selected = currentTab == index
            NavigationBarItem(
                selected = selected,
                onClick = { onSelectTab(index) },
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = label,
                            tint = if (selected) primaryBlue else textGray
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height(3.dp)
                                .clip(RoundedCornerShape(1.5.dp))
                                .background(if (selected) primaryBlue else Color.Transparent)
                        )
                    }
                },
                label = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) primaryBlue else textGray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = primaryBlue,
                    selectedTextColor = primaryBlue,
                    unselectedIconColor = textGray,
                    unselectedTextColor = textGray
                )
            )
        }
    }
}
