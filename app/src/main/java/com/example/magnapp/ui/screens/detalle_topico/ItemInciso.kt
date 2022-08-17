package com.example.magnapp.ui.screens.detalle_topico

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ItemInciso(
    content: String?,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 24.dp,
                    top = 20.dp,
                    end = 24.dp,
                    bottom = 12.dp
                )
        ) {
            /**
             * Content
             */
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    textAlign = TextAlign.Justify,
                    fontSize = 20.sp,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    text = content ?: "sin contenido"
                )
            }
            /**
             * See more Button
             */
            if(content != null) {
                TextButton(
                    onClick = { onClick.invoke() },
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .align(Alignment.End)
                ) {
                    Text(
                        text = "Ver mas",
                        fontSize = 16.sp,
                        textAlign = TextAlign.End
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun ItemIncisoPreview() {
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(
                    start = 24.dp,
                    top = 20.dp,
                    end = 24.dp,
                    bottom = 12.dp
                )
        ) {
            /**
             * Content
             */
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    textAlign = TextAlign.Justify,
                    fontSize = 20.sp,
                    maxLines = 5,
                    overflow = TextOverflow.Ellipsis,
                    text = "id est laborum"
                )
            }
            /**
             * See more Button
             */
            TextButton(
                onClick = { },
                modifier = Modifier
                    .padding(top = 12.dp)
                    .align(Alignment.End)
            ) {
                Text(
                    text = "Ver mas",
                    fontSize = 16.sp,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}