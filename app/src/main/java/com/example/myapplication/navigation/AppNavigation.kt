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
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.myapplication.ui.screens.ClientesPotencialesScreen
import com.example.myapplication.repository.ClientesPotencialesRepository
import com.example.myapplication.data.remote.ServicioRemoto
import com.example.myapplication.viewmodel.ClientesPotencialesViewModel
import com.example.myapplication.viewmodel.ClientesPotencialesViewModelFactory
import com.example.myapplication.repository.CuentaRepository
import com.example.myapplication.repository.ProveedorOAuthRepository
import com.example.myapplication.viewmodel.CuentaAuthViewModel
import com.example.myapplication.viewmodel.CuentaAuthViewModelFactory
import com.example.myapplication.ui.screens.IniciarSesionScreen
import com.example.myapplication.ui.screens.CrearCuentaScreen
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    widthSizeClass: WindowWidthSizeClass,
    usuarioViewModel: UsuarioViewModel,
    usuarioRepository: UsuarioRepository,
    cuentaRepository: CuentaRepository,
    proveedorOAuthRepository: ProveedorOAuthRepository
    )
{
    val navController = rememberNavController()
    val cuentaAuthViewModel: CuentaAuthViewModel = viewModel(
        factory = CuentaAuthViewModelFactory(cuentaRepository, proveedorOAuthRepository)
    )
    val estadoCuenta by cuentaAuthViewModel.estado.collectAsState()
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
                DrawerLink(label = "Clientes potenciales", selected = currentRoute == Screen.ClientesPotenciales.route, icon = Icons.Default.Person) { navigate(Screen.ClientesPotenciales) }
                DrawerLink(
                    label = if (estadoCuenta.cuentaActiva == null) "Iniciar sesión" else "Cerrar sesión",
                    selected = currentRoute == Screen.IniciarSesion.route,
                    icon = Icons.Default.Person
                ) {
                    if (estadoCuenta.cuentaActiva != null) {
                        cuentaAuthViewModel.cerrarSesion()
                    }
                    navigate(Screen.IniciarSesion)
                }
                DrawerLink("Crear cuenta", currentRoute == Screen.CrearCuenta.route, Icons.Default.Person) { navigate(Screen.CrearCuenta) }
            }
        }
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("PropTrack") },
                    actions = {
                        TextButton(
                            onClick = {
                                if (estadoCuenta.cuentaActiva != null) {
                                    cuentaAuthViewModel.cerrarSesion()
                                    navController.navigate(Screen.IniciarSesion.route) {
                                        popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                        launchSingleTop = true
                                    }
                                } else {
                                    navigate(Screen.IniciarSesion)
                                }
                            }
                        ) {
                            Text(if (estadoCuenta.cuentaActiva == null) "Iniciar sesión" else "Cerrar sesión")
                        }
                    },
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
                composable(Screen.ClientesPotenciales.route) {
                    val clientesViewModel: ClientesPotencialesViewModel = viewModel(
                        factory = ClientesPotencialesViewModelFactory(
                            ClientesPotencialesRepository(ServicioRemoto.solicitudes)
                        )
                    )
                    ClientesPotencialesScreen(viewModel = clientesViewModel)
                }
                composable(Screen.IniciarSesion.route) {
                    IniciarSesionScreen(
                        viewModel = cuentaAuthViewModel,
                        onCrearCuenta = { navigate(Screen.CrearCuenta) },
                        onInicioExitoso = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
                composable(Screen.CrearCuenta.route) {
                    CrearCuentaScreen(
                        viewModel = cuentaAuthViewModel,
                        onIniciarSesion = { navigate(Screen.IniciarSesion) },
                        onInicioExitoso = {
                            navController.navigate(Screen.Home.route) {
                                popUpTo(navController.graph.startDestinationId) { inclusive = false }
                                launchSingleTop = true
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun DrawerLink(label: String, selected: Boolean, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    NavigationDrawerItem(label = { Text(label) }, selected = selected, onClick = onClick, icon = { Icon(icon, contentDescription = label) })
}
