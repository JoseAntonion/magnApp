package com.example.magnapp.ui.screens.main

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.magnapp.TopicosRepo
import com.example.magnapp.ui.model.Topico
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel : ViewModel(), CoroutineScope {

    private val _topicos = MutableLiveData<List<Topico>>()
    val topicos: LiveData<List<Topico>>
        get() = _topicos

    private val _imagen = MutableLiveData<Uri>()
    val imagen: LiveData<Uri>
        get() = _imagen

    val topicosRepo = TopicosRepo()

    init {
        launch {
            topicosRepo.getTopicos {
                _topicos.value = it
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main
}