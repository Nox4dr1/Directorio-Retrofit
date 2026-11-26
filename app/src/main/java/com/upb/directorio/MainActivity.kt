package com.upb.directorio

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
                    val id = idSeleccionado
                    if (id == null) {
                        ListaUsuariosScreen(
                            onUsuarioClick = { idSeleccionado = it },
                            modifier = Modifier.padding(innerPadding)
                        )
                    } else {
                        BackHandler { idSeleccionado = null } // botón atrás del teléfono
                        DetalleUsuarioScreen(
                            id = id,
                            onVolver = { idSeleccionado = null },
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}