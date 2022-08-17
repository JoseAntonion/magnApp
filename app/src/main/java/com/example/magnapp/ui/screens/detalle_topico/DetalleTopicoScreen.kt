package com.example.magnapp.ui.screens.detalle_topico

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.magnapp.R
import com.example.magnapp.ui.model.Topico
import com.example.magnapp.ui.model.getColor
import com.example.magnapp.ui.screens.main.AppBarWithBack

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
        val animationTime = 300
        var visible by rememberSaveable { mutableStateOf(false) }
        val newMatchesState = detalleViewModel?.newMatches?.observeAsState(0)
        val oldMatchesState = detalleViewModel?.oldMatches?.observeAsState(0)
        val newArticleMatchesState =
            detalleViewModel?.newArticleMatch?.observeAsState(mutableMapOf())
        val oldArticleMatchesState =
            detalleViewModel?.oldArticleMatch?.observeAsState(mutableMapOf())
        var newSelected by rememberSaveable { mutableStateOf(false) }
        var oldSelected by rememberSaveable { mutableStateOf(false) }
        var newShownArticle by rememberSaveable { mutableStateOf(0) }
        var oldShownArticle by rememberSaveable { mutableStateOf(0) }

        /**
         * Agrupador por constitucion
         */
        Spacer(modifier = Modifier.size(24.dp))
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            /**
             * Seleccion de constitucion
             */
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = CenterVertically,
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            visible = true
                            oldSelected = true
                            newSelected = false
                        },
                    shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 0.dp),
                    backgroundColor = if (oldSelected) selectedTopic?.value?.getColor()!! else Color.DarkGray
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "ACTUAL",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.button,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "$oldShownArticle de ${oldMatchesState?.value}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row {
                                val arrowUp = painterResource(id = R.drawable.ic_arrow_drop_up)
                                Image(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .align(CenterVertically)
                                        .clickable {
                                            if (oldShownArticle < oldMatchesState?.value!!)
                                                oldShownArticle++
                                        },
                                    painter = arrowUp,
                                    contentDescription = null,
                                )
                                val arrowDown = painterResource(id = R.drawable.ic_arrow_drop_down)
                                Image(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .align(CenterVertically)
                                        .clickable {
                                            if (oldShownArticle > 0)
                                                oldShownArticle--
                                        },
                                    painter = arrowDown,
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            visible = false
                            oldSelected = false
                            newSelected = true
                        },
                    shape = RoundedCornerShape(0.dp, 8.dp, 0.dp, 0.dp),
                    backgroundColor = if (newSelected) selectedTopic?.value?.getColor()!! else Color.DarkGray
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "NUEVA",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.button,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "$newShownArticle de ${newMatchesState?.value}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row {
                                val arrowUp = painterResource(id = R.drawable.ic_arrow_drop_up)
                                Image(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .align(CenterVertically)
                                        .clickable {
                                            if (newShownArticle < newMatchesState?.value!!)
                                                newShownArticle++
                                        },
                                    painter = arrowUp,
                                    contentDescription = null,
                                )
                                val arrowDown = painterResource(id = R.drawable.ic_arrow_drop_down)
                                Image(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .align(CenterVertically)
                                        .clickable {
                                            if (newShownArticle > 0)
                                                newShownArticle--
                                        },
                                    painter = arrowDown,
                                    contentDescription = null,
                                )
                            }
                        }
                    }
                }
            }

            /**
             * Comparador
             */
            if (oldSelected || newSelected) {
                Row(
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
                ) {
                    if (visible) {
                        Column(
                            modifier = Modifier
                                .height(230.dp)
                                .background(selectedTopic?.value?.getColor() ?: Color.LightGray)
                        ) {
                            //if (oldArticleMatchesState?.value?.size!! > 1) {
                            val first = oldArticleMatchesState?.value?.get(oldShownArticle)
                            ItemInciso(content = first) {
                                Toast.makeText(
                                    mContext,
                                    "$first Seleccionado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            //}
                            /*oldArticleMatchesState?.value?.forEach {
                                if (it.key > 0) {
                                    ItemInciso(content = it.value) {
                                        Toast.makeText(
                                            mContext,
                                            "$it Seleccionado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }*/
                        }
                    }
                    if (!visible) {
                        Column(
                            modifier = Modifier
                                .height(230.dp)
                                .background(selectedTopic?.value?.getColor() ?: Color.LightGray)
                        ) {
                            val first = newArticleMatchesState?.value?.get(newShownArticle+1)
                            ItemInciso(content = first) {
                                Toast.makeText(
                                    mContext,
                                    "$first Seleccionado",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            /*newArticleMatchesState?.value?.forEach {
                                if (it.key > 0) {
                                    ItemInciso(content = it.value) {
                                        Toast.makeText(
                                            mContext,
                                            "$it Seleccionado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            }*/
                        }
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
                    }
                )
            }
        }

        /**
         * Tabla comparativa
         */
        var visible by remember { mutableStateOf(true) }
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            visible = !visible
                        },
                    shape = RoundedCornerShape(8.dp, 0.dp, 0.dp, 0.dp),
                    backgroundColor = Color.DarkGray,
                    elevation = 0.dp
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "ACTUAL",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.button,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "0 de 9",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row {
                                val arrowUp = painterResource(id = R.drawable.ic_arrow_drop_up)
                                Image(
//                                    modifier = Modifier
//                                        .weight(0.03f, fill = false)
//                                        .aspectRatio(
//                                            arrowUp.intrinsicSize.width /
//                                                    arrowUp.intrinsicSize.height
//                                        )
//                                        .fillMaxWidth(),
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .align(CenterVertically),
                                    painter = arrowUp,
                                    contentDescription = null,
                                    //contentScale = ContentScale.FillWidth
                                )
                                val arrowDown = painterResource(id = R.drawable.ic_arrow_drop_down)
                                Image(
//                                    modifier = Modifier
//                                        .weight(0.03f, fill = false)
//                                        .aspectRatio(
//                                            arrowDown.intrinsicSize.width /
//                                                    arrowDown.intrinsicSize.height
//                                        )
//                                        .fillMaxWidth(),
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .align(CenterVertically),
                                    painter = arrowDown,
                                    contentDescription = null,
                                    //contentScale = ContentScale.FillWidth
                                )
                            }
                        }
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            visible = !visible
                        },
                    shape = RoundedCornerShape(0.dp, 8.dp, 0.dp, 0.dp),
                    backgroundColor = Color.DarkGray,
                    elevation = 0.dp
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "NUEVA",
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.button,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            verticalAlignment = CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "000 de 900",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Row {
                                val arrowUp = painterResource(id = R.drawable.ic_arrow_drop_up)
                                Image(
//                                    modifier = Modifier
//                                        .weight(0.03f, fill = false)
//                                        .aspectRatio(
//                                            arrowUp.intrinsicSize.width /
//                                                    arrowUp.intrinsicSize.height
//                                        )
//                                        .fillMaxWidth(),
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .align(CenterVertically),
                                    painter = arrowUp,
                                    contentDescription = null,
                                    //contentScale = ContentScale.FillWidth
                                )
                                val arrowDown = painterResource(id = R.drawable.ic_arrow_drop_down)
                                Image(
//                                    modifier = Modifier
//                                        .weight(0.03f, fill = false)
//                                        .aspectRatio(
//                                            arrowDown.intrinsicSize.width /
//                                                    arrowDown.intrinsicSize.height
//                                        )
//                                        .fillMaxWidth(),
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(shape = CircleShape)
                                        .align(CenterVertically),
                                    painter = arrowDown,
                                    contentDescription = null,
                                    //contentScale = ContentScale.FillWidth
                                )
                            }
                        }
                    }
                }
            }

            /**
             * Comparador
             */
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(0.dp, 0.dp, 8.dp, 8.dp))
            ) {
                AnimatedVisibility(
                    visible,
                    modifier = Modifier.fillMaxSize(),
                    enter = slideInHorizontally(
                        initialOffsetX = { -300 }, // it == fullWidth
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { -300 },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    )
                ) {
                    val testList = listOf(
                        "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum",
                        "hola 2",
                        "hola 3",
                        "hola 4",
                        "hola 5",
                        "hola 6"
                    )
                    Column(
                        modifier = Modifier
                            .height(230.dp)
                            .background(selectedTopic?.value?.getColor() ?: Color.LightGray)
                    ) {
                        testList.forEach {
                            ItemInciso(content = it) {
                                Toast.makeText(mContext, "$it Seleccionado", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = !visible,
                    modifier = Modifier.fillMaxSize(),
                    enter = slideInHorizontally(
                        initialOffsetX = { it }, // small slide 300px
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing // interpolator
                        )
                    ),
                    exit = slideOutHorizontally(
                        targetOffsetX = { it },
                        animationSpec = tween(
                            durationMillis = 300,
                            easing = LinearEasing
                        )
                    )
                ) {
                    val testList = listOf(
                        "hola 1",
                        "hola 2",
                        "hola 3",
                        "hola 4",
                        "hola 5",
                        "hola 6"
                    )
                    LazyColumn(
                        contentPadding = PaddingValues(dimensionResource(id = R.dimen.pding_xxl)),// padding derecho/izquierdo del Row
                        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.pding_l))
                    ) {
                        items(testList) { item ->
                            ItemInciso(content = item) {
                                Toast.makeText(mContext, "$item Seleccionado", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
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