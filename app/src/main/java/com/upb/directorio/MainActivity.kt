package com.upb.directorio

import com.upb.directorio.ui.form.NuevoUsuarioScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.upb.directorio.ui.detail.DetalleUsuarioScreen
import com.upb.directorio.ui.list.ListaUsuariosScreen
import com.upb.directorio.ui.theme.DirectorioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DirectorioTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // null = se ve la lista; un número = se ve el detalle de ese usuario
                    var idSeleccionado by rememberSaveable { mutableStateOf<Int?>(null) }
                    // true = se ve el formulario para crear
                    var creando by rememberSaveable { mutableStateOf(false) }
                    val id = idSeleccionado
                    val modificador = Modifier.padding(innerPadding)

                    when {
                        creando -> {
                            BackHandler { creando = false }
                            NuevoUsuarioScreen(onVolver = { creando = false }, modifier = modificador)
                        }
                        id != null -> {
                            BackHandler { idSeleccionado = null }
                            DetalleUsuarioScreen(
                                id = id,
                                onVolver = { idSeleccionado = null },
                                modifier = modificador
                            )
                        }
                        else -> ListaUsuariosScreen(
                            onUsuarioClick = { idSeleccionado = it },
                            onAgregarClick = { creando = true },
                            modifier = modificador
                        )
                    }
                }
            }
        }
    }
}