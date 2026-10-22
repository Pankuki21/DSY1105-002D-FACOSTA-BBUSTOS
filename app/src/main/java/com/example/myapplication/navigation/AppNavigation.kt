package com.example.myapplication.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.screens.ClientesScreen
import com.example.myapplication.ui.screens.HomeScreen
import com.example.myapplication.ui.screens.ProfileScreen
import com.example.myapplication.ui.screens.PropiedadesDisponiblesScreen
import com.example.myapplication.ui.screens.RegistroClienteScreen
import com.example.myapplication.ui.screens.RegistroDepartamentoScreen
import com.example.myapplication.ui.screens.ResumenScreen
import com.example.myapplication.ui.screens.SettingsScreen
import com.example.myapplication.viewmodel.UsuarioViewModel
import com.example.myapplication.ui.screens.RegistroIntegralScreen
import com.example.myapplication.ui.screens.ResumenIntegralScreen
import com.example.myapplication.repository.UsuarioRepository
import com.example.myapplication.ui.screens.ListadoOfflineScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    widthSizeClass: WindowWidthSizeClass,
    usuarioViewModel: UsuarioViewModel,
    usuarioRepository: UsuarioRepository
    )
{
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val backStack by navController.currentBackStackEntryAsState()
    val currentRoute = backStack?.destination?.route

    fun navigate(route: Screen) {
        navController.navigate(route.route) { launchSingleTop = true }
        scope.launch { drawerState.close() }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Text("PropTrack", modifier = Modifier.padding(20.dp))
                DrawerLink("Inicio", currentRoute == Screen.Home.route, Icons.Default.Home) { navigate(Screen.Home) }
                DrawerLink("Registrar propiedad", currentRoute == Screen.RegistrarDepartamento.route, Icons.Default.Home) { navigate(Screen.RegistrarDepartamento) }
                DrawerLink("Registrar cliente", currentRoute == Screen.RegistrarCliente.route, Icons.Default.Person) { navigate(Screen.RegistrarCliente) }
                DrawerLink("Propiedades", currentRoute == Screen.Propiedades.route, Icons.Default.Home) { navigate(Screen.Propiedades) }
                DrawerLink("Clientes", currentRoute == Screen.Clientes.route, Icons.Default.Person) { navigate(Screen.Clientes) }
                DrawerLink("Perfil", currentRoute == Screen.Profile.route, Icons.Default.Person) { navigate(Screen.Profile) }
                DrawerLink("Configuración", currentRoute == Screen.Settings.route, Icons.Default.Settings) { navigate(Screen.Settings) }
                DrawerLink(label = "Registro integral", selected = currentRoute == Screen.RegistroIntegral.route, icon = Icons.Default.Person) { navigate(Screen.RegistroIntegral) }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("PropTrack") },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Abrir menú")
                        }
                    }
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(selected = currentRoute == Screen.Home.route, onClick = { navigate(Screen.Home) }, icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }, label = { Text("Inicio") })
                    NavigationBarItem(selected = currentRoute == Screen.Propiedades.route, onClick = { navigate(Screen.Propiedades) }, icon = { Icon(Icons.Default.Home, contentDescription = "Propiedades") }, label = { Text("Propiedades") })
                    NavigationBarItem(selected = currentRoute == Screen.Clientes.route, onClick = { navigate(Screen.Clientes) }, icon = { Icon(Icons.Default.Person, contentDescription = "Clientes") }, label = { Text("Clientes") })
                }
            }
        ) { innerPadding ->
            NavHost(navController, startDestination = Screen.Home.route, modifier = Modifier.padding(innerPadding)) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        widthSizeClass = widthSizeClass,
                        onRegistrarDepartamento = { navigate(Screen.RegistrarDepartamento) },
                        onRegistrarCliente = { navigate(Screen.RegistrarCliente) },
                        onConsultarPropiedades = { navigate(Screen.Propiedades) },
                        onVerClientes = { navigate(Screen.Clientes) }
                    )
                }
                composable(Screen.RegistrarDepartamento.route) {
                    RegistroDepartamentoScreen(onGuardado = { navigate(Screen.Resumen) })
                }
                composable(Screen.RegistrarCliente.route) {
                    RegistroClienteScreen(onGuardado = { navigate(Screen.Clientes) })
                }
                composable(Screen.Propiedades.route) { PropiedadesDisponiblesScreen() }
                composable(Screen.Clientes.route) { ClientesScreen() }
                composable(Screen.Resumen.route) { ResumenScreen() }
                composable(Screen.Profile.route) { ProfileScreen() }
                composable(Screen.Settings.route) { SettingsScreen() }
                composable(Screen.RegistroIntegral.route) { RegistroIntegralScreen(
                        usuarioViewModel = usuarioViewModel,
                        onRegistroExitoso = {
                            navigate(Screen.ResumenIntegral)
                        }) }
                composable(Screen.ResumenIntegral.route) { ResumenIntegralScreen(
                        usuarioViewModel = usuarioViewModel, onVerListado = {
                            navigate(Screen.ListadoOffline)
                        }) }
                composable(Screen.ListadoOffline.route) { ListadoOfflineScreen(
                        usuarioRepository = usuarioRepository) }
            }
        }
    }
}

@Composable
private fun DrawerLink(label: String, selected: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    NavigationDrawerItem(label = { Text(label) }, selected = selected, onClick = onClick, icon = { Icon(icon, contentDescription = label) })
}
