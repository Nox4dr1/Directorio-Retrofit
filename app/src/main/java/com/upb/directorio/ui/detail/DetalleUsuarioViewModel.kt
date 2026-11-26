package com.upb.directorio.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.upb.directorio.data.UsuarioRepository
import com.upb.directorio.model.Usuario
import com.upb.directorio.util.Resultado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Estado de la pantalla de detalle. */
data class DetalleUiState(
    val cargando: Boolean = false,
    val usuario: Usuario? = null,
    val error: String? = null
)

/** Pide un usuario por su id (GET con @Path). */
class DetalleUsuarioViewModel(
    private val repositorio: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState.asStateFlow()

    /** Si ese usuario ya está cargado, no vuelve a llamar a la API. */
    fun cargar(id: Int) {
        if (_uiState.value.usuario?.id == id) return
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            when (val resultado = repositorio.obtenerUsuario(id)) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(cargando = false, usuario = resultado.datos)
                }
                is Resultado.Error -> _uiState.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }
}