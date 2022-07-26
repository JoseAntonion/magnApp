package com.example.magnapp.ui.model

import com.google.firebase.firestore.DocumentId

class Articulo(
    @DocumentId
    var id: String = "",
    var incisos: List<String> = listOf()
)