package com.upb.directorio.ui.form

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.directorio.data.UsuarioRepository
import com.upb.directorio.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Lo que escribe el usuario y el resultado de la última operación. */
data class FormularioUiState(
    val nombre: String = "",
    val trabajo: String = "",
    val enviando: Boolean = false,
    val mensaje: String? = null,
    val error: String? = null
)

/** Envía datos a la API: crear (POST), editar (PUT) y eliminar (DELETE). */
class FormularioViewModel(
    private val repositorio: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(FormularioUiState())
    val uiState: StateFlow<FormularioUiState> = _uiState.asStateFlow()

    fun cambiarNombre(texto: String) {
        _uiState.update { it.copy(nombre = texto) }
    }

    fun cambiarTrabajo(texto: String) {
        _uiState.update { it.copy(trabajo = texto) }
    }

    /** POST /api/users */
    fun crear() {
        if (!datosValidos()) return
        val e = _uiState.value
        enviar({ repositorio.crearUsuario(e.nombre.trim(), e.trabajo.trim()) }) { id ->
            "Se creó a ${e.nombre.trim()} con id $id"
        }
    }

    /** PUT /api/users/{id} */
    fun editar(id: Int) {
        if (!datosValidos()) return
        val e = _uiState.value
        enviar({ repositorio.editarUsuario(id, e.nombre.trim(), e.trabajo.trim()) }) { fecha ->
            "Se actualizó el usuario $id ($fecha)"
        }
    }

    /** DELETE /api/users/{id} */
    fun eliminar(id: Int) {
        enviar({ repositorio.eliminarUsuario(id) }) { codigo ->
            "Se eliminó el usuario $id (código $codigo)"
        }
    }

    private fun datosValidos(): Boolean {
        val e = _uiState.value
        if (e.nombre.isBlank() || e.trabajo.isBlank()) {
            _uiState.update { it.copy(error = "Completa el nombre y el trabajo.", mensaje = null) }
            return false
        }
        return true
    }

    /** Lanza la llamada y traduce el Resultado en mensaje de éxito o de error. */
    private fun <T> enviar(llamada: suspend () -> Resultado<T>, textoExito: (T) -> String) {
        viewModelScope.launch {
            _uiState.update { it.copy(enviando = true, mensaje = null, error = null) }
            when (val resultado = llamada()) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(enviando = false, mensaje = textoExito(resultado.datos))
                }
                is Resultado.Error -> _uiState.update {
                    it.copy(enviando = false, error = resultado.mensaje)
                }
            }
        }
    }
}