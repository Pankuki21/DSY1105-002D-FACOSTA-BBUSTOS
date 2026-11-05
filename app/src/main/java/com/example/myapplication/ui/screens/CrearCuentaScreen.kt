package com.example.myapplication.ui.screens

import android.app.Activity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.viewmodel.CuentaAuthViewModel

private val paisesDisponibles = listOf("Chile", "Argentina", "Colombia", "México", "Perú", "España", "Otro")

@Composable
fun CrearCuentaScreen(
    viewModel: CuentaAuthViewModel,
    onIniciarSesion: () -> Unit,
    onInicioExitoso: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val activity = LocalContext.current as? Activity

    LaunchedEffect(estado.registroExitoso) {
        if (estado.registroExitoso) {
            viewModel.consumirRegistroExitoso()
            onIniciarSesion()
        }
    }
    LaunchedEffect(estado.cuentaActiva) {
        if (estado.cuentaActiva != null) onInicioExitoso()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("¿Ya tienes una cuenta?", style = MaterialTheme.typography.bodySmall)
            TextButton(onClick = onIniciarSesion) { Text("Inicia sesión →") }
        }

        Column(
            modifier = Modifier
                .widthIn(max = 460.dp)
                .fillMaxWidth()
                .padding(top = 22.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Crear cuenta en PropTrack", style = MaterialTheme.typography.headlineSmall)

            OutlinedButton(
                onClick = { activity?.let(viewModel::iniciarConGoogle) },
                enabled = !estado.procesando,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Continuar con Google") }
            OutlinedButton(
                onClick = { activity?.let(viewModel::iniciarConApple) },
                enabled = !estado.procesando,
                modifier = Modifier.fillMaxWidth()
            ) { Text("Continuar con Apple") }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Text("o", style = MaterialTheme.typography.bodySmall)
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            OutlinedTextField(
                value = estado.correoRegistro,
                onValueChange = viewModel::actualizarCorreoRegistro,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo electrónico") },
                singleLine = true
            )

            OutlinedTextField(
                value = estado.claveRegistro,
                onValueChange = viewModel::actualizarClaveRegistro,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                supportingText = {
                    Text("Usa 15 caracteres o más, o al menos 8 con un número y una minúscula.")
                },
                singleLine = true
            )

            OutlinedTextField(
                value = estado.nombreUsuario,
                onValueChange = viewModel::actualizarNombreUsuario,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Nombre de usuario") },
                supportingText = { Text("Usa letras, números y guiones internos; no puede empezar ni terminar con guion.") },
                singleLine = true
            )

            SelectorPais(
                seleccionado = estado.paisRegion,
                onSeleccionar = viewModel::actualizarPaisRegion
            )
            Text(
                "Usamos esta información para mantener actualizada tu cuenta y enviarte avisos relevantes.",
                style = MaterialTheme.typography.bodySmall
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(
                    checked = estado.recibirNovedades,
                    onCheckedChange = viewModel::actualizarPreferenciaNovedades
                )
                Text("Recibir novedades y anuncios de PropTrack")
            }

            estado.errorRegistro?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            estado.errorProveedor?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }

            Button(
                onClick = viewModel::registrarCuenta,
                enabled = !estado.procesando,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF188038))
            ) {
                Text(if (estado.procesando) "Creando cuenta…" else "Crear cuenta  ›")
            }
        }
    }
}

@Composable
private fun SelectorPais(
    seleccionado: String,
    onSeleccionar: (String) -> Unit
) {
    var desplegado by remember { mutableStateOf(false) }
    Box {
        OutlinedTextField(
            value = seleccionado,
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .clickable { desplegado = true },
            label = { Text("País o región") },
            readOnly = true,
            trailingIcon = { Text("⌄") }
        )
        DropdownMenu(
            expanded = desplegado,
            onDismissRequest = { desplegado = false }
        ) {
            paisesDisponibles.forEach { pais ->
                DropdownMenuItem(
                    text = { Text(pais) },
                    onClick = {
                        onSeleccionar(pais)
                        desplegado = false
                    }
                )
            }
        }
    }
}
