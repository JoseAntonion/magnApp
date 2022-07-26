package com.example.magnapp.ui.model

import com.google.firebase.firestore.DocumentId

class Constitucion {
    @DocumentId
    var id: String = ""
    var capitulos: List<Capitulo> = listOf()
}