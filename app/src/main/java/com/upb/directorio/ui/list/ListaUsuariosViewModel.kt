package com.upb.directorio.ui.list

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

/** Todo lo que la pantalla de lista necesita para dibujarse. */
data class ListaUiState(
    val cargando: Boolean = false,
    val usuarios: List<Usuario> = emptyList(),
    val pagina: Int = 1,
    val totalPaginas: Int = 1,
    val error: String? = null
)

/** Guarda el estado de la lista y pide las páginas al repositorio. */
class ListaUsuariosViewModel(
    private val repositorio: UsuarioRepository = UsuarioRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListaUiState())
    val uiState: StateFlow<ListaUiState> = _uiState.asStateFlow()

    init {
        cargar(1)
    }

    /** Pide la página [pagina]. Sin argumento, recarga la actual (sirve como Reintentar). */
    fun cargar(pagina: Int = _uiState.value.pagina) {
        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }
            when (val resultado = repositorio.obtenerUsuarios(pagina)) {
                is Resultado.Exito -> _uiState.update {
                    it.copy(
                        cargando = false,
                        usuarios = resultado.datos.usuarios,
                        pagina = resultado.datos.pagina,
                        totalPaginas = resultado.datos.totalPaginas
                    )
                }
                is Resultado.Error -> _uiState.update {
                    it.copy(cargando = false, error = resultado.mensaje)
                }
            }
        }
    }

    fun paginaSiguiente() {
        val estado = _uiState.value
        if (estado.pagina < estado.totalPaginas) cargar(estado.pagina + 1)
    }

    fun paginaAnterior() {
        val estado = _uiState.value
        if (estado.pagina > 1) cargar(estado.pagina - 1)
    }
}