package com.example.magnapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import com.example.magnapp.ui.theme.MagnAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MagnAppTheme {
                // A surface container using the 'background' color from the theme
                Scaffold {
                    // TOOLBAR
                    MainToolbar()
                    // MAIN
                    MainView()
                    // NAVBAR
                    NavigationBar()
                }
            }
        }
    }
}

@Composable
fun MainToolbar() {

}

@Preview(showBackground = true)
@Composable
fun MainView() {
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
            height = Dimension.fillToConstraints
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Box {
            /**
             * Background Image
             */
            Image(
                alignment = Alignment.TopCenter,
                painter = painterResource(R.drawable.ic_logopueblo),
                contentDescription = "back surface",
                alpha = 0.6F
            )
            ConstraintLayout(
                constraintSet = constraints,
                modifier = Modifier.fillMaxSize()
            ) {
                /**
                 * Main Rounded Container
                 */
                Surface(
                    color = Color.Green,
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
                            .layoutId("innerMainColumn")
                            .padding(top = 90.dp)
                    ) {
                        LazyRow(
                            contentPadding = PaddingValues(dimensionResource(id = R.dimen.pding_l)),// padding derecho/izquierdo del Row
                            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.pding_l))
                        ) {
                            val listTopico = (1..10).map {
                                "Item $it"
                            }
                            items(listTopico) {item->
                                ItemTopico(item)
                            }
                        }
                    }
                }
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
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
//@Preview(showBackground = true)
@Composable
fun ItemTopico(item: String) {
    val makeText = Toast.makeText(LocalContext.current, item, Toast.LENGTH_SHORT)
    Card(
        onClick = { makeText.show() },
        backgroundColor = Color.Magenta,
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .size(120.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = item,
                fontSize = 30.sp,
                color = Color.White
            )
        }
    }
}

@Composable
fun NavigationBar() {

}