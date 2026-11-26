package com.upb.directorio.ui.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.upb.directorio.ui.components.MensajeDeError

/** Detalle de un usuario: GET /api/users/{id}. */
@Composable
fun DetalleUsuarioScreen(
    id: Int,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetalleUsuarioViewModel = viewModel()
) {
    // Avisa al ViewModel qué id mostrar. La llamada a la API la hace el ViewModel, no la pantalla.
    LaunchedEffect(id) { viewModel.cargar(id) }

    val estado by viewModel.uiState.collectAsState()
    val error = estado.error
    val usuario = estado.usuario

    Column(
        modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth()) {
            TextButton(onClick = onVolver) { Text("← Volver") }
        }
        when {
            error != null -> MensajeDeError(mensaje = error, onReintentar = { viewModel.cargar(id) })
            estado.cargando || usuario?.id != id -> CircularProgressIndicator(Modifier.padding(32.dp))
            else -> {
                AsyncImage(
                    model = usuario!!.fotoUrl,
                    contentDescription = "Foto de ${usuario.nombreCompleto}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(160.dp).clip(CircleShape)
                )
                Spacer(Modifier.height(16.dp))
                Text(usuario!!.nombreCompleto, style = MaterialTheme.typography.headlineSmall)
                Text(usuario!!.email, style = MaterialTheme.typography.bodyLarge)
                Text("ID: ${usuario!!.id}", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}