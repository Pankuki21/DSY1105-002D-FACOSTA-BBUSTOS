package com.example.myapplication.ui.screens

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.model.ClientePotencialRemoto
import com.example.myapplication.model.ClientesPotencialesUiState
import com.example.myapplication.ui.themes.MyApplicationTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClientesPotencialesScreenTest {
    @get:Rule
    val reglaCompose = createComposeRule()

    @Test
    fun muestraReferenciasRemotasConEtiquetasEnEspanol() {
        reglaCompose.setContent {
            MyApplicationTheme {
                ClientesPotencialesContent(
                    estado = ClientesPotencialesUiState(
                        cargando = false,
                        clientes = listOf(ClientePotencialRemoto(18, 4, 96))
                    ),
                    onReintentar = {}
                )
            }
        }

        reglaCompose.onNodeWithText("Clientes potenciales remotos").assertExists()
        reglaCompose.onNodeWithText("Solicitud remota #18").assertExists()
        reglaCompose.onNodeWithText("Referencia de cliente: 4").assertExists()
        reglaCompose.onNodeWithText("Descripción recibida: 96 caracteres").assertExists()
    }

    @Test
    fun permiteReintentarCuandoNoHayConexion() {
        var reintentos = 0
        reglaCompose.setContent {
            MyApplicationTheme {
                ClientesPotencialesContent(
                    estado = ClientesPotencialesUiState(
                        cargando = false,
                        mensajeError = "No se pudieron cargar los clientes potenciales."
                    ),
                    onReintentar = { reintentos++ }
                )
            }
        }

        reglaCompose.onNodeWithText("Reintentar").performClick()

        assert(reintentos == 1)
    }
}
