package com.example.magnapp.ui.screens.detalle_topico

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.DarkGray
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Color.Companion.Unspecified
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TextChip(
    isSelected: Boolean,
    text: String,
    onChecked: (Boolean) -> Unit,
    selectedColor: Color = DarkGray
) {
    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                vertical = 2.dp,
                horizontal = 4.dp
            )
            .border(
                width = 1.dp,
                color = if (isSelected) selectedColor else LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (isSelected) selectedColor else Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable {
                onChecked(!isSelected)
            }
            .padding(4.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) White else Unspecified,
            modifier = Modifier
                .padding(start = 8.dp, top = 5.dp, bottom = 5.dp, end = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TextChipPreview() {
    val textChipRememberOneState = remember {
        mutableStateOf(false)
    }
    val text = "Prueba CHIP 1"
    val selectedColor: Color = Color.Green

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(
                vertical = 2.dp,
                horizontal = 4.dp
            )
            .border(
                width = 0.dp,
                color = if (textChipRememberOneState.value)
                    selectedColor
                else
                    LightGray,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = if (textChipRememberOneState.value)
                    selectedColor
                else
                    Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable {
                textChipRememberOneState.value = !textChipRememberOneState.value
            }
            .padding(4.dp)
    ) {
        Text(
            text = text,
            color = if (textChipRememberOneState.value) White else Unspecified,
            modifier = Modifier
                .padding(start = 8.dp, top = 5.dp, bottom = 5.dp, end = 8.dp)
        )
    }
}