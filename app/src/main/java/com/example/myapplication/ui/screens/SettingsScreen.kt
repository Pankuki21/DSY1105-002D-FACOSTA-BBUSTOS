package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.EstadoViewModel
import kotlinx.coroutines.delay

@Composable
fun SettingsScreen(
    estadoViewModel: EstadoViewModel = viewModel()
) {
    val modoActivado by estadoViewModel.modoActivado.collectAsState()
    val cargando by estadoViewModel.cargando.collectAsState()
    val guardado by estadoViewModel.guardado.collectAsState()

    val colorFondo by animateColorAsState(
        targetValue = if (modoActivado) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        label = "colorFondo"
    )

    LaunchedEffect(guardado) {
        if (guardado) {
            delay(2500)
            estadoViewModel.limpiarMensaje()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorFondo)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Configuración",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Opciones de configuración de PropTrack",
            style = MaterialTheme.typography.bodyLarge
        )

        if (cargando) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Modo especial",
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = if (modoActivado) {
                        "El modo especial está activado"
                    } else {
                        "El modo especial está desactivado"
                    }
                )

                Switch(
                    checked = modoActivado,
                    onCheckedChange = {
                        estadoViewModel.cambiarModo()
                    }
                )
            }

            AnimatedVisibility(
                visible = guardado,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Text(
                    text = "Configuración guardada correctamente",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}