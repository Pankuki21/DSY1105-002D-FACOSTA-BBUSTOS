package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.viewmodel.UsuarioViewModel

@Composable
fun ResumenIntegralScreen(
    usuarioViewModel: UsuarioViewModel,
    onVerListado: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val estado by usuarioViewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Resumen del usuario",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Nombre: ${estado.nombre}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Correo: ${estado.correo}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Dirección: ${estado.direccion}",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = "Modo: ${
                        if (estado.modoEspecial) "Premium" else "Básico"
                    }",
                    style = MaterialTheme.typography.bodyLarge
                )

                Text(
                    text = if (estado.fotoUri != null) {
                        "Fotografía seleccionada"
                    } else {
                        "Sin fotografía"
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onVerListado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver usuarios guardados")
        }
    }
}