package com.example.magnapp

import android.net.Uri
import android.util.Log
import com.example.magnapp.ui.model.Capitulo
import com.example.magnapp.ui.model.Topico
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.regex.Pattern


class NuevaConstitucionRepo {

    fun getNuevaConstitucion(state: (List<Capitulo>) -> Unit) {
        var listTopicos: List<Capitulo>
        Firebase.firestore.collection("constitucion_nueva")
            .addSnapshotListener { value, error ->
                value?.let {
                    listTopicos = it.toObjects()
                    state.invoke(listTopicos)
                }
                error?.let {
                    //TODO: implementar error al obtener topicos
                }
            }
    }

    fun getChipMatches(topicoId: String, key: String, state: (Int) -> Unit) {
        var counter = 0
        Firebase.firestore.collection("topicos").document(topicoId)
            .addSnapshotListener { value, error ->
                value?.let {
                    val topico = it.toObject<Topico>()
                    topico?.articulo?.forEach { articulo ->
                        val matcher = Pattern.compile(key).matcher(articulo.lowercase())
                        while (matcher.find()) {
                            counter++
                        }
                    }
                    state.invoke(counter)
                }
                error?.let {
                    //TODO: implementar error al obtener topicos
                }
            }
    }

    fun niceLink(state: (Uri) -> Unit) {
//        val storageRef: StorageReference = Firebase
//            .storage
//            .getReferenceFromUrl("gs://magnapp-894d2.appspot.com/local_hospital-48px.xml")
//        state.invoke(storageRef)

        val storageRef: StorageReference = Firebase.storage.reference
        storageRef
            .child("images/cb-base64-string - copia.svg")
            .downloadUrl
            .addOnSuccessListener {
                state.invoke(it)
            }.addOnFailureListener {
                // Handle any errors
                Log.e("sadfs", "sdf")
            }

    }

}