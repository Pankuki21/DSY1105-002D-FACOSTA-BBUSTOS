package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.PropiedadesViewModel

@Composable
fun ResumenScreen(viewModel: PropiedadesViewModel = viewModel()) {
    val propiedades by viewModel.propiedades.collectAsStateWithLifecycle(initialValue = emptyList())
    val ultima = propiedades.firstOrNull()
    Column(Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Propiedad registrada", style = MaterialTheme.typography.headlineMedium)
        if (ultima == null) {
            Text("No hay una propiedad disponible para mostrar. Puedes revisar el listado completo en Propiedades.")
        } else {
            Text("Departamento ${ultima.numero}", style = MaterialTheme.typography.titleLarge)
            Text("Piso: ${ultima.piso}")
            Text("Superficie: ${ultima.metrosCuadrados} m²")
            Text("Dormitorios: ${ultima.dormitorios} · Baños: ${ultima.banos}")
            Text("Terraza: ${if (ultima.terraza) "Sí" else "No"}")
            Text("Operación: ${ultima.tipoOperacion}")
            Text("Estado: ${ultima.estado}")
            Text("Precio: ${formatPesos(ultima.precio)}")
            Text("Los datos quedaron guardados localmente en Room y estarán disponibles sin conexión.")
        }
    }
}
