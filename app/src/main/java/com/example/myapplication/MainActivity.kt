package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.MyApplicationTheme
import com.example.myapplication.viewmodel.ProductoViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: ProductoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaPrincipal(viewModel)
                }
            }
        }
    }
}

@Composable
fun PantallaPrincipal(viewModel: ProductoViewModel) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Estructura Base MVVM Cargada",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        viewModel.listaProductos.forEach { producto ->
            Text(text = "• ${producto.nombre} - $${producto.precio}")
        }
    }
}