package com.example.magnapp.ui.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import com.example.magnapp.R

var vModel: MainViewModel? = null
var topicoClick: ((String) -> Unit)? = null
val constraints = ConstraintSet {
    val mainSurface = createRefFor("mainSurface")
    val mainLogo = createRefFor("mainLogo")
    val innerMainColumn = createRefFor("innerMainColumn")

    constrain(mainSurface) {
        start.linkTo(parent.start)
        end.linkTo(parent.end)
        top.linkTo(parent.top, margin = 120.dp)
        bottom.linkTo(parent.bottom)
        width = Dimension.fillToConstraints
    }

    constrain(mainLogo) {
        top.linkTo(mainSurface.top)
        bottom.linkTo(mainSurface.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }

    constrain(innerMainColumn) {
        top.linkTo(mainSurface.top, margin = 100.dp)
        bottom.linkTo(mainSurface.bottom)
        start.linkTo(mainSurface.start)
        end.linkTo(mainSurface.end)
        //height = Dimension.fillToConstraints
    }
}

@Composable
fun MainScreen(
    navController: NavHostController,
    viewModel: MainViewModel,
    onTopicoClick: (String) -> Unit
) {
    vModel = viewModel
    topicoClick = onTopicoClick
    Scaffold(
        bottomBar = { MainBottomNav(navController = navController) }, // NavBar
        topBar = { AppBar() } // TOOLBAR
    ) { padding ->
        MainView(
            scaffoldPadding = padding
        )
    }
}

//@Preview
@Composable
fun MainView(scaffoldPadding: PaddingValues) {
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .verticalScroll(rememberScrollState())
            .padding(scaffoldPadding)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
        ) {
            ConstraintLayout(
                constraintSet = constraints,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                BackgroundImage()
                MainRoundedContainer()
                OverlapTopLogo()
            }
        }
    }
}

@Preview
@Composable
fun MainRoundedContainer() {
    /**
     * Main Rounded Container
     */

    val listaTopicosOberver = vModel?.topicos?.observeAsState(emptyList())

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp, 24.dp, 0.dp, 0.dp))
            .layoutId("mainSurface"),
        elevation = 8.dp
    ) {
        /**
         * Main Inner Column
         */
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                //.layoutId("innerMainColumn")
                .padding(
                    top = 90.dp,
                    start = dimensionResource(id = R.dimen.pding_xxl),
                    end = dimensionResource(id = R.dimen.pding_xxl),
                    bottom = 32.dp
                )
        ) {
            /**
             * Topicos generales (Educación, Salud, Vivienda, etc.)
             */
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Tópicos",
                    style = MaterialTheme.typography.h5
                )
            }
            LazyRow(
                contentPadding = PaddingValues(dimensionResource(id = R.dimen.pding_xxl)),// padding derecho/izquierdo del Row
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.pding_l))
            ) {
                listaTopicosOberver?.let {
                    items(it.value) { item ->
                        ItemTopico(
                            item = item,
                            onClick = { topicoClick?.invoke(item.id) }
                        )
                    }
                }
            }

            /**
             * Diferencias entre antigua/nueva constitucion
             */
            Spacer(modifier = Modifier.size(32.dp))
            // Title Section 1
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Comparativa",
                    style = MaterialTheme.typography.h5
                )
            }
            Card(
                backgroundColor = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.pding_xxl))
                ) {
                    Text(
                        text = "1980\nVS\n2022",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp
                    )
                }
            }
            Spacer(modifier = Modifier.size(32.dp))
            // Title Section 1
            Box(
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(bottom = 16.dp)
            ) {
                Text(
                    text = "Comparativa",
                    style = MaterialTheme.typography.h5
                )
            }
            Card(
                backgroundColor = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.pding_xxl))
                ) {
                    Text(
                        text = "1980\nVS\n2022",
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun OverlapTopLogo() {
    /**
     * Main Surface Logo
     */
    Box(
        modifier = Modifier
            .layoutId("mainLogo")
    ) {
        Image(
            painter = painterResource(R.drawable.ic_base_logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(80.dp)
                .background(color = Color.White, shape = CircleShape)
        )
    }
}

@Composable
fun BackgroundImage() {
    /**
     * Background Image
     */
    Image(
        alignment = Alignment.TopCenter,
        painter = painterResource(R.drawable.ic_logopueblo),
        contentDescription = "back surface",
        alpha = 0.6F
    )
}