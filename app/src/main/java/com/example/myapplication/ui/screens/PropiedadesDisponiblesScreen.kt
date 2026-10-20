package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.DepartamentoEntity
import com.example.myapplication.viewmodel.PropiedadesViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PropiedadesDisponiblesScreen(viewModel: PropiedadesViewModel = viewModel()) {
    val propiedades by viewModel.disponibles.collectAsStateWithLifecycle(initialValue = emptyList())
    val edificios by viewModel.edificios.collectAsStateWithLifecycle(initialValue = emptyList())
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Propiedades disponibles", style = MaterialTheme.typography.headlineMedium)
        Text("Los candidatos se comparan por operación, capacidad económica y preferencias declaradas.")
        if (propiedades.isEmpty()) Text("No hay propiedades disponibles. Registra un departamento para empezar.")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(propiedades, key = { it.id }) { propiedad ->
                val edificio = edificios.firstOrNull { it.id == propiedad.edificioId }
                DepartamentoCompatibilidadCard(propiedad, edificio?.nombre ?: "Edificio", viewModel)
            }
        }
    }
}

@Composable
private fun DepartamentoCompatibilidadCard(propiedad: DepartamentoEntity, edificio: String, viewModel: PropiedadesViewModel) {
    val candidatos by viewModel.coincidencias(propiedad).collectAsStateWithLifecycle(initialValue = emptyList())
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text("$edificio · Departamento ${propiedad.numero}", style = MaterialTheme.typography.titleMedium)
            Text("${propiedad.tipoOperacion.lowercase().replaceFirstChar(Char::uppercase)} · ${propiedad.estado.lowercase().replaceFirstChar(Char::uppercase)}")
            Text("${propiedad.dormitorios} dormitorios · ${propiedad.banos} baños · ${propiedad.metrosCuadrados} m² · ${if (propiedad.terraza) "con terraza" else "sin terraza"}")
            Text("Precio: ${formatPesos(propiedad.precio)}")
            Text("${candidatos.size} cliente(s) compatible(s)", color = MaterialTheme.colorScheme.primary, style = MaterialTheme.typography.titleSmall)
            if (candidatos.isEmpty()) Text("Aún no hay clientes que cumplan los requisitos de esta propiedad.")
            candidatos.forEach { Text("• ${it.nombre} · ${it.telefono}") }
        }
    }
}

internal fun formatPesos(valor: Double): String = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("es-CL")).format(valor)
