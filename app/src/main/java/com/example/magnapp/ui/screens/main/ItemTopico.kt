package com.example.magnapp.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.magnapp.R
import com.example.magnapp.ui.model.Topico
import com.example.magnapp.ui.model.getColor

@OptIn(ExperimentalMaterialApi::class)
//@Preview(showBackground = true)
@Composable
fun ItemTopico(
    item: Topico,
    onClick: () -> Unit
) {
    item.getColor()?.let {
        Column {
            Card(
                onClick = onClick,
                backgroundColor = it,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .size(120.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    val image = painterResource(id = R.drawable.ic_salud)
                    Image(
                        painter = image,
                        contentDescription = "",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
            Text(
                text = item.nombre,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.subtitle1
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true)
@Composable
fun ItemTopicoPreview() {
    Column {
        Card(
            onClick = {},
            backgroundColor = Color.Black,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                val image = painterResource(id = R.drawable.ic_salud)
                Image(
                    painter = image,
                    contentDescription = "",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        Text(
            text = "TOPICO",
            color = Color.Black,
            modifier = Modifier
                .padding(top = 5.dp)
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.subtitle2
        )
    }
}