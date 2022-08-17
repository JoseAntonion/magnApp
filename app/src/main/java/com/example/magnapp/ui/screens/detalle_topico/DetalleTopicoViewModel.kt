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

    fun getChipMatches3(key: String) {
        launch {
            topicosRepo.getConstitutionMatches("constitucion_actual", key) {
                _oldMatches.value = it
            }
            topicosRepo.getConstitutionMatches("constitucion_nueva", key) {
                _newMatches.value = it
            }
            topicosRepo.getPartOfMatchByKey {
                _newArticleMatch.value = it
            }
        }
    }

    fun getChipMatches(key: String) {
        launch {
            getConstitutionMatches("constitucion_actual", key)
            getConstitutionMatches("constitucion_nueva", key)
            showMatchedArticles()
        }
    }

    private suspend fun showMatchedArticles() {
        try {
            topicosRepo.getPartOfMatchByKey {
                when (it[0]) {
                    "constitucion_actual" -> {
                        _oldArticleMatch.value = it
                    }
                    "constitucion_nueva" -> {
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
                    "constitucion_actual" -> {
                        _oldMatches.value = it
                    }
                    "constitucion_nueva" -> {
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