package com.example.magnapp.ui.screens.detalle_topico

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.magnapp.TopicosRepo
import com.example.magnapp.ui.model.Topico
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class DetalleTopicoViewModel : ViewModel(), CoroutineScope {

    val TAG = DetalleTopicoViewModel::class.java.simpleName
    val CONSTITUCION_ACTUAL = "constitucion_actual"
    val CONSTITUCION_NUEVA = "constitucion_nueva"
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

    private val _newArticleMatch = MutableLiveData<MutableMap<Int, String>>()
    val newArticleMatch: LiveData<MutableMap<Int, String>>
        get() = _newArticleMatch

    private val _oldArticleMatch = MutableLiveData<MutableMap<Int, String>>()
    val oldArticleMatch: LiveData<MutableMap<Int, String>>
        get() = _oldArticleMatch

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
            getConstitutionMatches(CONSTITUCION_ACTUAL, key)
            getConstitutionMatches(CONSTITUCION_NUEVA, key)
            showMatchedArticles()
        }
    }

    private suspend fun showMatchedArticles() {
        try {
            topicosRepo.getPartOfMatchByKey {
                when (it[0]) {
                    CONSTITUCION_ACTUAL -> {
                        _oldArticleMatch.value = it
                    }
                    CONSTITUCION_NUEVA -> {
                        _newArticleMatch.value = it
                    }
                }
            }
            delay(500)
        } catch (e: Exception) {
            e.message?.let { Log.e(TAG, it) }
        }
    }

    private suspend fun getConstitutionMatches(wichOne: String, key: String) {
        try {
            topicosRepo.getConstitutionMatches(wichOne, key) {
                when (wichOne) {
                    CONSTITUCION_ACTUAL -> {
                        _oldMatches.value = it
                    }
                    CONSTITUCION_NUEVA -> {
                        _newMatches.value = it
                    }
                }
            }
            delay(500)
        } catch (e: Exception) {
            e.message?.let { Log.e(TAG, it) }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}