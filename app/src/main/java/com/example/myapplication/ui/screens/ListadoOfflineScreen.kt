package com.example.myapplication.ui.screens

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.repository.UsuarioRepository
import java.io.File

@Composable
fun ListadoOfflineScreen(
    usuarioRepository: UsuarioRepository,
    modifier: Modifier = Modifier
) {
    var busqueda by remember {
        mutableStateOf("")
    }

    val usuarios by remember(busqueda) {
        if (busqueda.isBlank()) {
            usuarioRepository.obtenerUsuarios()
        } else {
            usuarioRepository.buscarUsuariosPorCorreo(busqueda.trim())
        }
    }.collectAsState(initial = emptyList())

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Text(
                text = "Usuarios guardados",
                style = MaterialTheme.typography.headlineMedium
            )

            OutlinedTextField(
                value = busqueda,
                onValueChange = { busqueda = it },
                label = {
                    Text("Buscar por correo electrónico")
                },
                placeholder = {
                    Text("Ejemplo: usuario@correo.com")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                singleLine = true
            )

            Text(
                text = if (busqueda.isBlank()) {
                    "Usuarios registrados: ${usuarios.size}"
                } else {
                    "Resultados encontrados: ${usuarios.size}"
                },
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 12.dp)
            )

            if (usuarios.isEmpty()) {
                Text(
                    text = if (busqueda.isBlank()) {
                        "Todavía no hay usuarios registrados."
                    } else {
                        "No se encontraron usuarios con ese correo."
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = usuarios,
                        key = { usuario -> usuario.id }
                    ) { usuario ->

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme
                                    .colorScheme
                                    .surfaceVariant
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ImagenInteligente(
                                    fotoUri = usuario.fotoPath?.let { ruta ->
                                        if (ruta.startsWith("/")) {
                                            Uri.fromFile(File(ruta))
                                        } else {
                                            Uri.parse(ruta)
                                        }
                                    }
                                )

                                Spacer(
                                    modifier = Modifier.width(16.dp)
                                )

                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = usuario.nombre,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Text(
                                        text = "Correo: ${usuario.correo}"
                                    )

                                    Text(
                                        text = "Dirección: ${usuario.direccion}"
                                    )

                                    Text(
                                        text = "Modo: ${
                                            if (usuario.modoEspecial) {
                                                "Premium"
                                            } else {
                                                "Básico"
                                            }
                                        }"
                                    )

                                    Text(
                                        text = "Fecha de registro: ${
                                            usuario.fechaRegistro
                                        }",
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}