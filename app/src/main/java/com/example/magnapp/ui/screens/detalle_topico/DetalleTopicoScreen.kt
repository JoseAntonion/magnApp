package com.example.magnapp.ui.screens.detalle_topico

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magnapp.ui.model.Topico
import com.example.magnapp.ui.model.getColor
import com.example.magnapp.ui.screens.main.AppBarWithBack
import com.example.magnapp.ui.theme.ExtendedTheme

var detalleViewModel: DetalleTopicoViewModel? = null
lateinit var mContext: Context
var topicChips: State<List<String>>? = null
var selectedTopic: State<Topico>? = null

@Composable
fun DetalleTopicoScreen(
    viewModel: DetalleTopicoViewModel,
    topicoId: String,
    onBackClick: () -> Unit
) {
    detalleViewModel = viewModel
    mContext = LocalContext.current
    selectedTopic = viewModel.topico.observeAsState(Topico())
    topicChips = viewModel.chips.observeAsState(emptyList())
    viewModel.getTopicoById(topicoId)
    Scaffold(
        //bottomBar = { MainBottomNav(navController = navController) }, // NavBar
        topBar = {
            selectedTopic?.value?.let {
                AppBarWithBack(
                    title = it.nombre,
                    backgroundColor = it.getColor() ?: Color.White,
                    contentColor = Color.White,
                    onBackClick
                )
            }
        }
    ) { padding ->
        selectedTopic?.value?.nombre?.let { MainView(it) }
    }
}

@Composable
fun MainView(nombreTopico: String) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
    ) {
        /**
         * Titulo Topico
         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = nombreTopico,
                style = MaterialTheme.typography.h4,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
        Spacer(modifier = Modifier.size(32.dp))
        /**
         * Descripcion Topico
         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            val descriptionAsList = selectedTopic?.value?.descripcion!!.split(" ")
            DescriptionStyling(descriptionAsList)
        }
        Spacer(modifier = Modifier.size(32.dp))
        /**
         * Chips de claves
         */
        val chipStateNew = rememberSaveable { mutableStateOf("") }
        val chipStateOld = rememberSaveable { mutableStateOf("") }
        ChipVerticalGrid(
            spacing = 5.dp,
            /*moreItemsView = {
                selectedTopic?.value?.getColor()?.let { color ->
                    TextChip(
                        isSelected = chipStateOld.value,
                        text = "$it more items",
                        selectedColor = color,
                        onChecked = { selected ->
                            chipStateOld.value = chipStateNew.value
                            chipStateNew.value = selected
                        }
                    )
                }
            },*/
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            topicChips?.value?.forEach { clave ->
                selectedTopic?.value?.getColor()?.let {
                    TextChip(
                        isSelected = chipStateNew.value == clave,
                        text = clave,
                        selectedColor = it,
                        onChecked = {
                            chipStateOld.value = chipStateNew.value
                            chipStateNew.value = clave
                            detalleViewModel?.getChipMatches(clave)
                        }
                    )
                }
            }
        }
        /**
         * Tabla comparativa
         */
        val newMatchesState = detalleViewModel?.newMatches?.observeAsState(0)
        val oldMatchesState = detalleViewModel?.oldMatches?.observeAsState(0)
        Spacer(modifier = Modifier.size(24.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        Toast
                            .makeText(mContext, "PINOCHO QLO", Toast.LENGTH_SHORT)
                            .show()
                    },
                shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp),
                backgroundColor = ExtendedTheme.colors.antiguaC
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CONSTITUCIÓN\nACTUAL",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 6.dp, top = 8.dp, end = 6.dp)
                            .fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "VER COINCIDENCIAS :",
                            color = Color.White,
                            style = MaterialTheme.typography.overline
                        )
                        Text(
                            text = oldMatchesState?.value.toString(),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        Toast
                            .makeText(mContext, "NUEVO TEXTO", Toast.LENGTH_SHORT)
                            .show()
                    },
                shape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp),
                backgroundColor = ExtendedTheme.colors.nuevaC
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CONSTITUCIÓN\nNUEVA",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 6.dp, top = 8.dp, end = 6.dp)
                            .fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "VER COINCIDENCIAS :",
                            color = Color.White,
                            style = MaterialTheme.typography.overline
                        )
                        Text(
                            text = newMatchesState?.value.toString(),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MainViewPreview() {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
        //.background(Color.Blue.copy(alpha = 0.2f))
    ) {
        /**
         * Titulo Topico
         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "TOPICO",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
        Spacer(modifier = Modifier.size(24.dp))

        /**
         * Descripcion Topico
         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color.DarkGray)) {
                        append("Toda persona tiene")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primaryVariant,
                            //fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    ) {
                        append(" derecho a la salud ")
                    }
                    withStyle(style = SpanStyle(color = Color.DarkGray)) {
                        append("y al bienestar integral, incluyendo sus dimensiones")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.primaryVariant,
                            //fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    ) {
                        append(" física y mental")
                    }
                },
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.size(24.dp))

        /**
         * Chips Clave
         */
        val testTopicChip = listOf(
            "Pruebarrrrrrrrrrr", "de", "los", "chips",
            "Pruebarrrrrrrrrrr", "de", "los", "chips",
            "Pruebarrrrrrrrrrr", "de", "los", "chips"
        )
        val chipStateNew = rememberSaveable { mutableStateOf("") }
        val chipStateOld = rememberSaveable { mutableStateOf("") }
        ChipVerticalGrid(
            spacing = 5.dp,
            //moreItemsView = {
            //    val chipState = remember { mutableStateOf(false) }
            //    itemView("$it more items", chipState)
            //},
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterHorizontally)
                .padding(16.dp)
        ) {
            testTopicChip.forEach { word ->
                TextChip(
                    isSelected = chipStateNew.value == word,
                    text = word,
                    selectedColor = Color.Green,
                    onChecked = {
                        chipStateOld.value = chipStateNew.value
                        chipStateNew.value = word
                        //viewModel.
                    }
                )
            }
        }

        /**
         * Tabla comparativa
         */
        Spacer(modifier = Modifier.size(24.dp))
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        Toast
                            .makeText(mContext, "PINOCHO QLO", Toast.LENGTH_SHORT)
                            .show()
                    },
                shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 8.dp),
                backgroundColor = ExtendedTheme.colors.antiguaC
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CONSTITUCIÓN\nACTUAL",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 6.dp, top = 8.dp, end = 6.dp)
                            .fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "VER COINCIDENCIAS :",
                            color = Color.White,
                            style = MaterialTheme.typography.overline
                        )
                        Text(
                            text = "15",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        Toast
                            .makeText(mContext, "NUEVO TEXTO", Toast.LENGTH_SHORT)
                            .show()
                    },
                shape = RoundedCornerShape(0.dp, 8.dp, 8.dp, 0.dp),
                backgroundColor = ExtendedTheme.colors.nuevaC
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CONSTITUCIÓN\nNUEVA",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.button,
                        color = Color.White,
                        modifier = Modifier
                            .padding(start = 6.dp, top = 8.dp, end = 6.dp)
                            .fillMaxWidth()
                    )
                    Row(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "VER COINCIDENCIAS :",
                            color = Color.White,
                            style = MaterialTheme.typography.overline
                        )
                        Text(
                            text = "15",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DescriptionStyling(desc: List<String>) {
    Text(
        text = buildAnnotatedString {
            desc.forEach {
                if (it.startsWith("<")) {
                    val formatWord = it.substring(1, it.length)
                    withStyle(
                        style = SpanStyle(
                            color = MaterialTheme.colors.secondaryVariant,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    ) {
                        append(" $formatWord ")
                    }
                } else {
                    withStyle(
                        style = SpanStyle(
                            fontSize = 20.sp,
                            color = MaterialTheme.colors.onBackground
                        )
                    ) {
                        append(" $it ")
                    }
                }
            }
        },
        color = Color.Black,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.subtitle1,
        modifier = Modifier.fillMaxWidth()
    )
}