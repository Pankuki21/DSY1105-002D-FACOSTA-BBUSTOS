package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.ClientesViewModel

@Composable
fun RegistroClienteScreen(onGuardado: () -> Unit = {}, viewModel: ClientesViewModel = viewModel()) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    Column(Modifier.verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("Registrar cliente", style = MaterialTheme.typography.headlineMedium)
        Text("Ingresa sus datos de contacto y capacidad para filtrar propiedades compatibles.")
        CampoPropiedad("Nombre", form.nombre) { viewModel.actualizar("nombre", it) }
        CampoPropiedad("Teléfono", form.telefono) { viewModel.actualizar("telefono", it) }
        CampoPropiedad("Correo", form.correo) { viewModel.actualizar("correo", it) }
        CampoPropiedad("Operación que busca (ARRIENDO o COMPRA)", form.tipoOperacion) { viewModel.actualizar("tipoOperacion", it) }
        if (form.tipoOperacion == "ARRIENDO") {
            CampoPropiedad("Renta mensual ($)", form.rentaMensual) { viewModel.actualizar("rentaMensual", it) }
        } else {
            CampoPropiedad("Presupuesto de compra ($)", form.presupuesto) { viewModel.actualizar("presupuesto", it) }
        }
        CampoPropiedad("Preferencias (ej. 2 dormitorios, terraza)", form.preferencias) { viewModel.actualizar("preferencias", it) }
        form.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (form.guardado) Text("Cliente guardado en el dispositivo.", color = MaterialTheme.colorScheme.primary)
        Button(onClick = { viewModel.guardar(onGuardado) }, enabled = !form.guardando, modifier = Modifier.fillMaxWidth()) {
            if (form.guardando) CircularProgressIndicator() else Text("Guardar cliente")
        }
    }
}
