package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import com.example.myapplication.R

@Composable
fun HomeScreen(
    widthSizeClass: WindowWidthSizeClass,
    onRegistrarDepartamento: () -> Unit,
    onRegistrarCliente: () -> Unit = {},
    onConsultarPropiedades: () -> Unit = {},
    onVerClientes: () -> Unit = {}
) {
    val expanded = widthSizeClass == WindowWidthSizeClass.Expanded
    val contentModifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(if (expanded) 40.dp else 20.dp)

    Column(modifier = contentModifier, verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "PropTrack",
            modifier = Modifier.fillMaxWidth().height(if (expanded) 160.dp else 120.dp),
            contentScale = ContentScale.Fit
        )
        Text("Gestión inmobiliaria", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        Text("Registra propiedades y encuentra clientes que cumplen sus requisitos.", style = MaterialTheme.typography.bodyLarge)

        if (expanded) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                AccionCard("Propiedades", "Registra un departamento y sus datos de edificio.", "Registrar propiedad", onRegistrarDepartamento, Modifier.weight(1f))
                AccionCard("Clientes", "Guarda presupuesto, renta y preferencias.", "Registrar cliente", onRegistrarCliente, Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp), modifier = Modifier.fillMaxWidth()) {
                AccionCard("Disponibilidad", "Consulta inmuebles disponibles y compatibilidad.", "Ver propiedades", onConsultarPropiedades, Modifier.weight(1f))
                AccionCard("Cartera de clientes", "Revisa los contactos guardados.", "Ver clientes", onVerClientes, Modifier.weight(1f))
            }
        } else {
            Button(onClick = onRegistrarDepartamento, modifier = Modifier.fillMaxWidth()) { Text("Registrar propiedad") }
            Button(onClick = onRegistrarCliente, modifier = Modifier.fillMaxWidth()) { Text("Registrar cliente") }
            OutlinedButton(onClick = onConsultarPropiedades, modifier = Modifier.fillMaxWidth()) { Text("Consultar propiedades y coincidencias") }
            OutlinedButton(onClick = onVerClientes, modifier = Modifier.fillMaxWidth()) { Text("Ver clientes registrados") }
        }
    }
}

@Composable
private fun AccionCard(titulo: String, detalle: String, accion: String, onClick: () -> Unit, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleLarge)
            Text(detalle, style = MaterialTheme.typography.bodyMedium)
            Button(onClick = onClick, modifier = Modifier.fillMaxWidth()) { Text(accion) }
        }
    }
}
