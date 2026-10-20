package com.example.myapplication.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.saveable.rememberSaveable
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.utils.crearUriImagen
import com.example.myapplication.viewmodel.PerfilViewModel

@Composable
fun ProfileScreen(
    perfilViewModel: PerfilViewModel = viewModel()
) {
    val contexto = LocalContext.current

    val imagenPerfil by perfilViewModel.imagenPerfil.collectAsState()

    val selectorGaleria = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            try {
                contexto.contentResolver.takePersistableUriPermission(
                    uri,
                    android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )

                perfilViewModel.actualizarImagen(uri)
            } catch (e: Exception) {
                perfilViewModel.actualizarImagen(uri)
            }
        }
    }

    var uriTemporal by rememberSaveable {
        androidx.compose.runtime.mutableStateOf<String?>(null)
    }

    val tomarFotografia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            uriTemporal?.let { uri ->
                perfilViewModel.actualizarImagen(Uri.parse(uri))
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Perfil",
            style = MaterialTheme.typography.headlineMedium
        )

        ImagenInteligente(
            imagenUri = imagenPerfil,
            tamano = 140.dp
        )

        Text(
            text = "Agente inmobiliario",
            style = MaterialTheme.typography.titleMedium
        )

        Text("Usuario principal de PropTrack")

        Button(
            onClick = {
                selectorGaleria.launch(arrayOf("image/*"))
            }
        ) {
            Text("Seleccionar foto de perfil")
        }

        Button(
            onClick = {
                val nuevaUri = crearUriImagen(contexto)
                uriTemporal = nuevaUri.toString()
                tomarFotografia.launch(nuevaUri)
            }
        ) {
            Text("Tomar fotografía")
        }
    }
}
