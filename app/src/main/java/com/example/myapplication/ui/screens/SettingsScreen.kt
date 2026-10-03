package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.navigation.Screen

@Composable
fun SettingsScreen(
    onNavigate: (Screen) -> Unit
) {
    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onNavigate(Screen.Home)
                    },
                    icon = {
                        Text("⌂")
                    },
                    label = {
                        Text("Inicio")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        onNavigate(Screen.Profile)
                    },
                    icon = {
                        Text("●")
                    },
                    label = {
                        Text("Perfil")
                    }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {
                        onNavigate(Screen.Settings)
                    },
                    icon = {
                        Text("⚙")
                    },
                    label = {
                        Text("Configuración")
                    }
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

            Text(
                text = "Configuración"
            )

            Text(
                text = "Opciones de la aplicación",
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}