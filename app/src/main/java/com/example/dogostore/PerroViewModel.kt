package com.example.dogostore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PerroViewModel(private val repository: PerroRepository) : ViewModel() {

    val perros: StateFlow<List<Perro>> = repository.perrosLocales
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertarPerro(perro: Perro){
        viewModelScope.launch {
            repository.insertarPerroLocal(perro)
        }
    }

    suspend fun obtenerPerroPorId(id: Int): Perro? {
        return repository.obtenerPerroLocalPorId(id)
    }

    // Estado para almacenar las razas descargadas de la API
    private val _razasInternet = MutableStateFlow<List<RazaRed>>(emptyList())
    val razasInternet: StateFlow<List<RazaRed>> = _razasInternet

    fun descargarRazas(){
        viewModelScope.launch {
            val lista = repository.obtenerRazasDelMundo()
            if (lista.isNotEmpty()){
                _razasInternet.value = lista
            }
        }
    }
}

class PerroViewModelFactory(private val repository: PerroRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PerroViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PerroViewModel(repository) as T
        }
        throw IllegalArgumentException("Clase ViewModel desconocida")
    }
}