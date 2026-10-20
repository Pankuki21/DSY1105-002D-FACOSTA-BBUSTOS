package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.viewmodel.PropiedadesViewModel

@Composable
fun RegistroDepartamentoScreen(
    onGuardado: () -> Unit = {},
    viewModel: PropiedadesViewModel = viewModel()
) {
    val form by viewModel.form.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Registrar propiedad", style = MaterialTheme.typography.headlineMedium)
        Text("Edificio", style = MaterialTheme.typography.titleMedium)
        CampoPropiedad("Nombre del edificio", form.edificio) { viewModel.actualizar("edificio", it) }
        CampoPropiedad("Dirección", form.direccion) { viewModel.actualizar("direccion", it) }
        CampoPropiedad("Comuna", form.comuna) { viewModel.actualizar("comuna", it) }
        CampoPropiedad("Cantidad de departamentos del edificio", form.cantidadDepartamentos) { viewModel.actualizar("cantidadDepartamentos", it) }
        Text("Departamento", style = MaterialTheme.typography.titleMedium)
        CampoPropiedad("Número", form.numero) { viewModel.actualizar("numero", it) }
        CampoPropiedad("Piso", form.piso) { viewModel.actualizar("piso", it) }
        CampoPropiedad("Superficie (m²)", form.metrosCuadrados) { viewModel.actualizar("metrosCuadrados", it) }
        CampoPropiedad("Dormitorios", form.dormitorios) { viewModel.actualizar("dormitorios", it) }
        CampoPropiedad("Baños", form.banos) { viewModel.actualizar("banos", it) }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = form.terraza, onCheckedChange = viewModel::cambiarTerraza)
            Text("Tiene terraza")
        }
        CampoPropiedad("Precio en pesos chilenos", form.precio) { viewModel.actualizar("precio", it) }
        CampoPropiedad("Operación (ARRIENDO o COMPRA)", form.tipoOperacion) { viewModel.actualizar("tipoOperacion", it) }
        CampoPropiedad("Estado (DISPONIBLE, RESERVADO, VENDIDO o ARRENDADO)", form.estado) { viewModel.actualizar("estado", it) }
        form.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        if (form.guardado) Text("Propiedad guardada en el dispositivo.", color = MaterialTheme.colorScheme.primary)
        Button(onClick = { viewModel.guardar(onGuardado) }, enabled = !form.guardando, modifier = Modifier.fillMaxWidth()) {
            if (form.guardando) CircularProgressIndicator() else Text("Guardar propiedad")
        }
    }
}

@Composable
internal fun CampoPropiedad(label: String, value: String, onValueChange: (String) -> Unit) {
    OutlinedTextField(value = value, onValueChange = onValueChange, label = { Text(label) }, modifier = Modifier.fillMaxWidth(), singleLine = true)
}
