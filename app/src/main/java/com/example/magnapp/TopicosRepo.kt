package com.example.magnapp

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.example.magnapp.ui.model.Articulo
import com.example.magnapp.ui.model.Capitulo
import com.example.magnapp.ui.model.Topico
import com.example.magnapp.ui.screens.detalle_topico.mContext
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.util.regex.Pattern

class TopicosRepo {

    var matchArticle = mutableMapOf<Int, String>()
    val TAG = TopicosRepo::class.java.simpleName

    fun getTopicos(state: (List<Topico>) -> Unit) {
        Log.i(TAG,"PASO getTopicos")
        var listTopicos: List<Topico>
        Firebase.firestore.collection("topicos")
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

    fun getTopico(topicId: String, state: (Topico?) -> Unit) {
        Log.i(TAG,"PASO getTopico")
        var topico: Topico?
        Firebase.firestore.collection("topicos").document(topicId)
            .addSnapshotListener { value, error ->
                value?.let {
                    topico = it.toObject<Topico>()
                    state.invoke(topico)
                }
                error?.let {
                    //TODO: implementar error al obtener topicos
                }
            }
    }

    fun getConstitutionMatches(wichOne: String, word: String, state: (Int) -> Unit) {
        Log.i(TAG,"PASO getConstitutionMatches")
        var counter = 0
        matchArticle[counter] = wichOne
        Firebase.firestore
            .collection(wichOne)
            .get()
            .addOnSuccessListener { query ->
                val capitulos = query.toObjects<Capitulo>()
                capitulos.forEach { capitulo ->
                    val capituloName = capitulo.id
                    Firebase.firestore
                        .collection(wichOne)
                        .document(capituloName)
                        .collection("articulos")
                        .addSnapshotListener { value, error ->
                            value.let {
                                val articulos = it?.toObjects<Articulo>()
                                articulos?.forEach { articulo ->
                                    articulo.incisos.forEach { inciso ->
                                        val matcher =
                                            Pattern.compile(word).matcher(inciso.lowercase())
                                        while (matcher.find()) {
                                            counter++
                                            matchArticle[counter] = inciso
                                        }
                                    }
                                }
                                state.invoke(counter)
                                //counter = 0
                            }
                            error.let {
                                Log.d("TAG", "getChipMatches: ")
                            }
                        }
                }
                //state.invoke(counter)
                //counter = 0
            }
            .addOnFailureListener { exception ->
                Log.d("TAG", "Error getting documents: ", exception)
            }
    }

    fun getPartOfMatchByKey(state: (MutableMap<Int, String>) -> Unit) {
        Log.i(TAG,"PASO getPartOfMatchByKey")
        state.invoke(matchArticle)
        matchArticle = mutableMapOf()
    }

    private fun deleteMassive() {
        Firebase.firestore.collection("constitucion_nueva")
            .addSnapshotListener { value, error ->
                value?.let { query ->
                    val capitulos = query.documents
                    capitulos.forEach { documentSnap ->
                        val capitulo = documentSnap.id
                        val articulos = documentSnap.data
                        articulos?.forEach { articulo ->
                            val name = articulo.key
                            val updates = hashMapOf<String, Any>(
                                name to FieldValue.delete()
                            )

                            Firebase.firestore
                                .collection("constitucion_nueva")
                                .document(capitulo)
                                .update(updates)
                                .addOnCompleteListener {
                                    Log.d("TAG", "getChipMatches: $it")
                                }.addOnFailureListener {
                                    Log.d("TAG", "getChipMatches: $it")
                                }


                        }
                    }
                }
                error?.let {
                    Log.d("TAG", "getChipMatches: ")
                }
            }
    }

    private fun passFromToAnother() {
        Firebase.firestore.collection("constitucion_actual")
            .addSnapshotListener { value, error ->
                value?.let { query ->
                    val asdf = query.documents
                    asdf.forEach { documentSnap ->
                        val capitulo = documentSnap.id
                        val articulos = documentSnap.data
                        var count = 1
                        articulos?.forEach { articulo ->
                            val name = articulo.key
                            val name2 = name.substringAfter("_")
                            val incisos = articulo.value as List<String>
                            val nestedData = hashMapOf("incisos" to incisos)

                            Firebase.firestore
                                .collection("constitucion_actual")
                                .document(capitulo)
                                .collection("articulos")
                                .document(name2)
                                .set(nestedData)
                                .addOnSuccessListener {
                                    Log.d(
                                        "TAG",
                                        "DocumentSnapshot successfully written!"
                                    )
                                    Toast.makeText(
                                        mContext,
                                        "FIN DEL TRASPASO",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                                .addOnFailureListener { Log.w("TAG", "Error writing document") }
                            count++
                        }
                        Log.d("TAG", "getChipMatches: ")
                    }
                }
                error?.let {
                    Log.d("TAG", "getChipMatches: ")
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