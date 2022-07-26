package com.example.magnapp.ui.model

import com.google.firebase.firestore.DocumentId

class Capitulo {
    @DocumentId
    var id: String = ""
    var articulos: List<Articulo> = listOf()
}