package com.example.myapplication.ui.screens

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import com.example.myapplication.viewmodel.CuentaAuthViewModel

@Composable
fun IniciarSesionScreen(
    viewModel: CuentaAuthViewModel,
    onCrearCuenta: () -> Unit,
    onInicioExitoso: () -> Unit
) {
    val estado by viewModel.estado.collectAsState()
    val activity = LocalContext.current as? Activity

    LaunchedEffect(estado.cuentaActiva) {
        if (estado.cuentaActiva != null) onInicioExitoso()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("¿Aún no tienes cuenta?", style = MaterialTheme.typography.bodySmall)
            TextButton(onClick = onCrearCuenta) { Text("Crear cuenta →") }
        }

        Column(
            modifier = Modifier
                .widthIn(max = 460.dp)
                .fillMaxWidth()
                .padding(top = 36.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text("Iniciar sesión en PropTrack", style = MaterialTheme.typography.headlineSmall)
            Text("Accede para continuar gestionando tus propiedades y clientes.")

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
                value = estado.correoInicio,
                onValueChange = viewModel::actualizarCorreoInicio,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Correo electrónico") },
                singleLine = true
            )
            OutlinedTextField(
                value = estado.claveInicio,
                onValueChange = viewModel::actualizarClaveInicio,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true
            )

            estado.errorInicio?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            estado.errorProveedor?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
            estado.mensaje?.let {
                Text(it, color = MaterialTheme.colorScheme.primary)
            }

            Button(
                onClick = viewModel::iniciarSesion,
                enabled = !estado.procesando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (estado.procesando) {
                    CircularProgressIndicator()
                } else {
                    Text("Iniciar sesión")
                }
            }
        }
    }
}
