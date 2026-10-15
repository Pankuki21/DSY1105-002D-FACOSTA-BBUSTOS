package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.navigation.Screen
import com.example.myapplication.viewmodel.EstadoViewModel

@Composable
fun SettingsScreen(
    onNavigate: (Screen) -> Unit,
    estadoViewModel: EstadoViewModel = viewModel()
) {
    val modoActivado by estadoViewModel.modoActivado.collectAsState()
    val cargando by estadoViewModel.cargando.collectAsState()
    val guardado by estadoViewModel.guardado.collectAsState()

    val colorFondo by animateColorAsState(
        targetValue = if (modoActivado) {
            Color(0xFFD8F3DC)
        } else {
            Color(0xFFF5F5F5)
        },
        label = "colorFondo"
    )

    LaunchedEffect(guardado) {
        if (guardado) {
            kotlinx.coroutines.delay(2500)
            estadoViewModel.limpiarMensaje()
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigate(Screen.Home) },
                    icon = { Text("⌂") },
                    label = { Text("Inicio") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = { onNavigate(Screen.Profile) },
                    icon = { Text("●") },
                    label = { Text("Perfil") }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = { onNavigate(Screen.Settings) },
                    icon = { Text("⚙") },
                    label = { Text("Configuración") }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Configuración")

            Text(
                text = "Opciones de la aplicación",
                modifier = Modifier.padding(top = 12.dp, bottom = 24.dp)
            )

            if (cargando) {
                CircularProgressIndicator()
                Text(
                    text = "Cargando preferencias...",
                    modifier = Modifier.padding(top = 12.dp)
                )
            } else {
                Text(
                    text = if (modoActivado) {
                        "Modo especial activado"
                    } else {
                        "Modo especial desactivado"
                    }
                )

                Switch(
                    checked = modoActivado,
                    onCheckedChange = {
                        estadoViewModel.cambiarModo()
                    },
                    modifier = Modifier.padding(top = 12.dp)
                )

                AnimatedVisibility(visible = guardado) {
                    Text(
                        text = "¡Estado guardado correctamente!",
                        modifier = Modifier.padding(top = 16.dp),
                        color = Color(0xFF2E7D32)
                    )
                }
            }
        }
    }
}