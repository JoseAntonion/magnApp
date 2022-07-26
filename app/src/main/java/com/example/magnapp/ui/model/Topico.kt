package com.example.magnapp.ui.model

import androidx.compose.ui.graphics.Color
import com.google.firebase.firestore.DocumentId

class Topico {
    @DocumentId
    var id: String = ""
    var nombre: String = ""
    var color: String = ""
    var claves: List<String> = listOf()
    var descripcion: String = ""
    var articulo: List<String> = listOf()

    //set(value) {
    //    TopicColor(value)
    //}
    var contenido: String = ""
    //var androidColor: TopicColor = TopicColor()

    open class TopicColor(val color: String? = null) {
        class Red : TopicColor("rojo")
        object Blue : TopicColor("azul")
        object Green : TopicColor("verde")
    }

    sealed class Screen(val route: String) {
        object Home : Screen("home")
        object Player : Screen("player/{episodeUri}") {
            fun createRoute(episodeUri: String) = "player/$episodeUri"
        }
    }
}

fun Topico.getColor(): Color? {
    return if (this.color.isNotBlank())
        Color(android.graphics.Color.parseColor("#" + this.color))
    else
        null
}