package com.example.myapplication.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import androidx.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenCompacta(onRegistrarDepartamento: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(text = "Gestión inmobiliaria") })
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "¡Bienvenida a PropTrack!",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )

            Button(onClick = onRegistrarDepartamento) {
                Text(text = "Registrar propiedad")
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo de PropTrack",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Preview(
    name = "Compacta",
    showBackground = true,
    widthDp = 360,
    heightDp = 800
)
@Composable
fun HomeScreenCompactaPreview() {
    HomeScreenCompacta(
        onRegistrarDepartamento = {}
    )
}
