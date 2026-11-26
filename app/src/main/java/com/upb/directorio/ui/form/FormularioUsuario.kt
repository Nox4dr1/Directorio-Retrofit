package com.upb.directorio.ui.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

/** Pantalla para crear un usuario (POST). */
@Composable
fun NuevoUsuarioScreen(
    onVolver: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FormularioViewModel = viewModel(key = "nuevo")
) {
    val estado by viewModel.uiState.collectAsState()
    Column(
        modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TextButton(onClick = onVolver) { Text("← Volver") }
        Text("Nuevo usuario", style = MaterialTheme.typography.headlineSmall)
        CamposUsuario(estado, viewModel)
        Button(
            onClick = { viewModel.crear() },
            enabled = !estado.enviando,
            modifier = Modifier.fillMaxWidth()
        ) { Text("Crear (POST)") }
        ResultadoOperacion(estado)
    }
}

/** Acciones sobre un usuario que ya existe: editar (PUT) y eliminar (DELETE). */
@Composable
fun AccionesUsuario(
    id: Int,
    modifier: Modifier = Modifier,
    viewModel: FormularioViewModel = viewModel(key = "editar-$id")
) {
    val estado by viewModel.uiState.collectAsState()
    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        CamposUsuario(estado, viewModel)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { viewModel.editar(id) }, enabled = !estado.enviando) {
                Text("Guardar (PUT)")
            }
            OutlinedButton(onClick = { viewModel.eliminar(id) }, enabled = !estado.enviando) {
                Text("Eliminar (DELETE)")
            }
        }
        ResultadoOperacion(estado)
    }
}

/** Los dos campos de texto; los usan el formulario nuevo y el de edición. */
@Composable
private fun CamposUsuario(estado: FormularioUiState, viewModel: FormularioViewModel) {
    OutlinedTextField(
        value = estado.nombre,
        onValueChange = viewModel::cambiarNombre,
        label = { Text("Nombre") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
    OutlinedTextField(
        value = estado.trabajo,
        onValueChange = viewModel::cambiarTrabajo,
        label = { Text("Trabajo") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth()
    )
}

/** Muestra el círculo de carga, el error en rojo o el mensaje de éxito. */
@Composable
private fun ResultadoOperacion(estado: FormularioUiState) {
    val error = estado.error
    val mensaje = estado.mensaje
    when {
        estado.enviando -> CircularProgressIndicator()
        error != null -> Text(error, color = MaterialTheme.colorScheme.error)
        mensaje != null -> Text(mensaje, color = MaterialTheme.colorScheme.primary)
    }
}