package com.mleiva.contenidosmega.ui.contenidos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.mleiva.contenidosmega.model.Contenido
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/***
 * Project: ContenidosMega
 * From: com.mleiva.contenidosmega
 * Creted by: Marcelo Leiva on 23-09-2025 at 14:27
 ***/
class ContenidosViewModel : ViewModel() {
    private val _contents = MutableStateFlow<List<Contenido>>(emptyList())
    val contents: StateFlow<List<Contenido>> = _contents

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    private val db = FirebaseFirestore.getInstance()

    init {
        loadContents()
    }

    fun loadContents() {
        _isRefreshing.value = true
        db.collection("contenidos")
            .get()
            .addOnSuccessListener { result ->
                val list = result.map { doc ->
                    doc.getString("imagenUrl")?.let {
                        Contenido(
                            titulo = doc.getString("titulo") ?: "",
                            descripcion = doc.getString("descripcion") ?: "",
                            imagenUrl = it,
                            videoUrl = doc.getString("videoUrl") ?: ""
                        )
                    }
                }
                _contents.value = list as List<Contenido>
                _isRefreshing.value = false
            }
            .addOnFailureListener {
                _contents.value = emptyList()
                _isRefreshing.value = false
            }
    }
}
