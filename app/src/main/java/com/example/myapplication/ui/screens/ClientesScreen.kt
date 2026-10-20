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
import com.example.myapplication.data.ClienteEntity
import com.example.myapplication.viewmodel.ClientesViewModel

@Composable
fun ClientesScreen(viewModel: ClientesViewModel = viewModel()) {
    val clientes by viewModel.clientes.collectAsStateWithLifecycle(initialValue = emptyList())
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Clientes", style = MaterialTheme.typography.headlineMedium)
        if (clientes.isEmpty()) Text("Aún no hay clientes. Registra un contacto para comenzar a buscar coincidencias.")
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(clientes, key = { it.id }) { ClienteCard(it) }
        }
    }
}

@Composable
internal fun ClienteCard(cliente: ClienteEntity) {
    Card(Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(cliente.nombre, style = MaterialTheme.typography.titleMedium)
            Text("${cliente.tipoOperacion.lowercase().replaceFirstChar(Char::uppercase)} · ${cliente.correo}")
            Text("Teléfono: ${cliente.telefono}")
            if (cliente.tipoOperacion == "ARRIENDO") Text("Renta mensual: ${cliente.rentaMensual.toLong()}")
            else Text("Presupuesto: ${cliente.presupuesto.toLong()}")
            if (cliente.preferencias.isNotBlank()) Text("Preferencias: ${cliente.preferencias}")
        }
    }
}
