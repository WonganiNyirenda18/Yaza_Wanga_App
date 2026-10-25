package com.example.yaza_wanga.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.yaza_wanga.ui.theme.*

@Composable
fun YazaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String = "",
    leadingIcon: ImageVector? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    readOnly: Boolean = false,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Column(modifier = modifier.fillMaxWidth()) {
        if (!label.isNullOrBlank()) {
            Text(
                text = label ?: "",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = textDark
            )
            Spacer(modifier = Modifier.height(6.dp))
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = textGray, fontSize = 13.sp) },
            leadingIcon = leadingIcon?.let {
                { Icon(it, contentDescription = null, tint = textGray, modifier = Modifier.size(20.dp)) }
            },
            trailingIcon = trailingIcon,
            isError = isError,
            singleLine = singleLine,
            minLines = minLines,
            readOnly = readOnly,
            enabled = enabled,
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = containerBg,
                unfocusedContainerColor = containerBg,
                disabledContainerColor = containerBg,
                focusedBorderColor = primaryBlue,
                unfocusedBorderColor = Color(0xFFE2E8F0),
                errorBorderColor = MaterialTheme.colorScheme.error,
                focusedTextColor = textDark,
                unfocusedTextColor = textDark
            ),
            modifier = Modifier.fillMaxWidth()
        )

        if (isError && !errorMessage.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
                fontSize = 12.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun YazaTextFieldPreview() {
    Yaza_WangaTheme {
        YazaTextField(
            value = "bsc-inf-15-23@unima.ac.mw",
            onValueChange = {},
            label = "School Email",
            placeholder = "Enter your email"
        )
    }
}
