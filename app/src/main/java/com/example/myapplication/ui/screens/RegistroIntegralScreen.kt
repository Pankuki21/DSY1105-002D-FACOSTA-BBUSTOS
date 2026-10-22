package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplication.components.PremiumCard
import com.example.myapplication.viewmodel.UsuarioViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.LaunchedEffect
import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.animateColorAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.graphics.Color
import com.example.myapplication.utils.crearUriImagen

@Composable
fun RegistroIntegralScreen(
    usuarioViewModel: UsuarioViewModel,
    onRegistroExitoso: () -> Unit = {},
    onSeleccionarFoto: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val estado by usuarioViewModel.uiState.collectAsState()

    android.util.Log.d(
        "PropTrackPantalla",
        "La pantalla se dibuja. cargando = ${estado.cargando}"
    )

    val textoDerivado by remember(estado.modoEspecial) {
        derivedStateOf {
            if (estado.modoEspecial) {
                "¡Modo especial activado!"
            } else {
                "Activar modo especial"
            }
        }
    }

    val colorModoEspecial by animateColorAsState(
        targetValue = if (estado.modoEspecial) {
            Color(0xFF2E7D32)
        } else {
            Color.Gray
        },
        label = "ColorModoEspecial"
    )

    val context = LocalContext.current

    var uriCamara by remember {
        androidx.compose.runtime.mutableStateOf<android.net.Uri?>(null)
    }

    val tomarFotografia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { guardada ->
        if (guardada) {
            uriCamara?.let { uri -> usuarioViewModel.actualizarFoto(uri) }
        }
    }

    LaunchedEffect(estado.registroExitoso) {
        if (estado.registroExitoso) {
            usuarioViewModel.consumirRegistroExitoso()
            onRegistroExitoso()
        }
    }

    val selectorImagen = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            usuarioViewModel.actualizarFoto(uri)
        }
    }

    Scaffold(modifier = modifier.fillMaxSize()) { paddingValues ->
        if (estado.cargando) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()

                Spacer(modifier = Modifier.height(16.dp))

                Text("Preparando el formulario...")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Text(
                    text = "Registro integral",
                    style = MaterialTheme.typography.headlineMedium
                )

                Text(
                    text = "Completa tus datos para crear tu perfil.",
                    style = MaterialTheme.typography.bodyMedium
                )

                OutlinedTextField(
                    value = estado.nombre,
                    onValueChange = usuarioViewModel::actualizarNombre,
                    label = { Text("Nombre completo") },
                    isError = estado.errores.nombre != null,
                    supportingText = {
                        estado.errores.nombre?.let { Text(it) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = estado.correo,
                    onValueChange = usuarioViewModel::actualizarCorreo,
                    label = { Text("Correo electrónico") },
                    isError = estado.errores.correo != null,
                    supportingText = {
                        estado.errores.correo?.let { Text(it) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = estado.clave,
                    onValueChange = usuarioViewModel::actualizarClave,
                    label = { Text("Contraseña") },
                    visualTransformation = PasswordVisualTransformation(),
                    isError = estado.errores.clave != null,
                    supportingText = {
                        estado.errores.clave?.let { Text(it) }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = estado.direccion,
                    onValueChange = usuarioViewModel::actualizarDireccion,
                    label = { Text("Dirección") },
                    isError = estado.errores.direccion != null,
                    supportingText = {
                        estado.errores.direccion?.let { Text(it) }
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        usuarioViewModel.actualizarModoEspecial(!estado.modoEspecial)
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                        containerColor = colorModoEspecial
                    )
                ) {
                    Text(textoDerivado)
                }

                //PremiumCard
                AnimatedVisibility(
                    visible = estado.modoEspecial,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) { PremiumCard(visible = true) }

                ImagenInteligente(
                    fotoUri = estado.fotoUri,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            try {
                                val uri = crearUriImagen(context)
                                uriCamara = uri
                                tomarFotografia.launch(uri)
                            } catch (e: Exception) {
                                Toast.makeText(
                                    context,
                                    "No se pudo abrir la cámara.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cámara")
                    }

                    Button(
                        onClick = {
                            selectorImagen.launch("image/*")
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Galería")
                    }
                }

                estado.errores.foto?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = estado.aceptaTerminos,
                        onCheckedChange = usuarioViewModel::actualizarAceptacionTerminos
                    )

                    Text("Acepto los términos y condiciones")
                }

                estado.errores.terminos?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error
                    )
                }

                AnimatedVisibility(
                    visible = estado.mensaje != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    estado.mensaje?.let { mensaje ->
                        Text(
                            text = mensaje,
                            color = if (estado.registroExitoso) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Button(
                    onClick = {
                        usuarioViewModel.registrarUsuario()
                    },
                    enabled = !estado.guardando,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (estado.guardando) {
                        CircularProgressIndicator()
                    } else {
                        Text("Registrar usuario y ver resumen")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}