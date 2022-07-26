package com.example.magnapp.ui.screens.detalle_topico

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.magnapp.TopicosRepo
import com.example.magnapp.ui.model.Topico
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetalleTopicoViewModel : ViewModel(), CoroutineScope {

    private val _topico = MutableLiveData<Topico>()
    val topico: LiveData<Topico>
        get() = _topico

    private val _chips = MutableLiveData<List<String>>()
    val chips: LiveData<List<String>>
        get() = _chips

    private val _newMatches = MutableLiveData<Int>()
    val newMatches: LiveData<Int>
        get() = _newMatches

    private val _oldMatches = MutableLiveData<Int>()
    val oldMatches: LiveData<Int>
        get() = _oldMatches

    val topicosRepo = TopicosRepo()

    fun getTopicoById(id: String) {
        launch {
            topicosRepo.getTopico(id) {
                _topico.value = it
                _chips.value = it?.claves
            }
        }
    }

    fun getChipMatches(key: String) {
        launch {
            topicosRepo.getChipMatches(key, "constitucion_actual") {
                _oldMatches.value = it
            }
            topicosRepo.getChipMatches(key, "constitucion_nueva") {
                _newMatches.value = it
            }
        }
    }

//    init {
//        launch {
//            topicosRepo.getTopico(topicoId) {
//                _topico.value = it
//            }
//        }
//    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}