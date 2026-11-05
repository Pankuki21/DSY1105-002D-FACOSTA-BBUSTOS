package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.remote.ServicioRemoto
import com.example.myapplication.model.ClientePotencialRemoto
import com.example.myapplication.model.ClientesPotencialesUiState
import com.example.myapplication.repository.ClientesPotencialesRepository
import com.example.myapplication.ui.themes.MyApplicationTheme
import com.example.myapplication.viewmodel.ClientesPotencialesViewModel
import com.example.myapplication.viewmodel.ClientesPotencialesViewModelFactory

@Composable
fun ClientesPotencialesScreen(viewModel: ClientesPotencialesViewModel) {
    val estado by viewModel.estado.collectAsStateWithLifecycle()

    MyApplicationTheme {
        ClientesPotencialesContent(
            estado = estado,
            onReintentar = viewModel::cargarClientes
        )
    }
}

@Composable
fun ClientesPotencialesContent(
    estado: ClientesPotencialesUiState,
    onReintentar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Clientes potenciales remotos", style = MaterialTheme.typography.headlineSmall)
        Text(
            "Referencias recibidas desde el servicio externo de demostración.",
            style = MaterialTheme.typography.bodyMedium
        )

        when {
            estado.cargando -> Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator()
                Text("Consultando solicitudes…", modifier = Modifier.padding(start = 12.dp))
            }

            estado.mensajeError != null -> {
                Text(estado.mensajeError, color = MaterialTheme.colorScheme.error)
                Button(onClick = onReintentar) { Text("Reintentar") }
            }

            estado.clientes.isEmpty() -> Text("No hay solicitudes remotas disponibles.")

            else -> LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(estado.clientes, key = ClientePotencialRemoto::solicitudId) { cliente ->
                    ClientePotencialCard(cliente)
                }
            }
        }
    }
}

@Composable
private fun ClientePotencialCard(cliente: ClientePotencialRemoto) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text("Solicitud remota #${cliente.solicitudId}", style = MaterialTheme.typography.titleMedium)
            Text("Referencia de cliente: ${cliente.referenciaCliente}")
            Text("Descripción recibida: ${cliente.largoDescripcion} caracteres")
        }
    }
}
