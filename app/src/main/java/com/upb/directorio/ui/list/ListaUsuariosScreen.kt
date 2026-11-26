package com.upb.directorio.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.upb.directorio.model.Usuario
import com.upb.directorio.ui.components.MensajeDeError

/** Lista paginada de usuarios. Solo dibuja el estado y avisa de los clics. */
@Composable
fun ListaUsuariosScreen(
    onUsuarioClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ListaUsuariosViewModel = viewModel()
) {
    val estado by viewModel.uiState.collectAsState()
    val error = estado.error

    Column(modifier.fillMaxSize()) {
        Text(
            "Directorio",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )
        when {
            estado.cargando -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            error != null -> MensajeDeError(mensaje = error, onReintentar = { viewModel.cargar() })
            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(estado.usuarios, key = { it.id }) { usuario ->
                        FilaUsuario(usuario = usuario, onClick = { onUsuarioClick(usuario.id) })
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(onClick = { viewModel.paginaAnterior() }, enabled = estado.pagina > 1) {
                        Text("Anterior")
                    }
                    Text("Página ${estado.pagina} de ${estado.totalPaginas}")
                    Button(
                        onClick = { viewModel.paginaSiguiente() },
                        enabled = estado.pagina < estado.totalPaginas
                    ) {
                        Text("Siguiente")
                    }
                }
            }
        }
    }
}

/** Una fila: foto redonda, nombre y correo. */
@Composable
private fun FilaUsuario(usuario: Usuario, onClick: () -> Unit) {
    Card(Modifier.fillMaxWidth().clickable(onClick = onClick)) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = usuario.fotoUrl,
                contentDescription = "Foto de ${usuario.nombreCompleto}",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(56.dp).clip(CircleShape)
            )
            Column(Modifier.padding(start = 12.dp)) {
                Text(usuario.nombreCompleto, style = MaterialTheme.typography.titleMedium)
                Text(usuario.email, style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}