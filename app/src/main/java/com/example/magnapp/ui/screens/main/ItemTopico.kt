package com.example.magnapp.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
    //val makeText = Toast.makeText(LocalContext.current, item.nombre, Toast.LENGTH_SHORT)
    item.getColor()?.let {
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
    }
}