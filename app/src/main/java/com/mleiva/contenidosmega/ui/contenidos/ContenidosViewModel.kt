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

    private val firestore = FirebaseFirestore.getInstance()

    private val _contenidos = MutableStateFlow<List<Contenido>>(emptyList())
    val contenidos: StateFlow<List<Contenido>> = _contenidos

    init {
        cargarContenidos()
    }

    private fun cargarContenidos() {
        viewModelScope.launch {
            firestore.collection("contenidos")
                .get()
                .addOnSuccessListener { result ->
                    val lista = result.documents.mapNotNull { it.toObject(Contenido::class.java) }
                    _contenidos.value = lista
                }
        }
    }
}
