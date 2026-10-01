package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myapplication.navigation.NavigationEvent
import com.example.myapplication.navigation.Screen
import kotlinx.coroutines.flow.MutableSharedFLow
import kotlinx.coroutines. flow.SharedFLow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel: ViewModel(){

    private val navigationEvent = MutableSharedFlow<NavigationEvent>()

    val navigationEvents: SharedFlow<NavigationEvent> = navigationEvents.asSharedFlow()

    fun navigateTo(screen: Screen) {
        CoroutineScope(context = Dispatchers.Main).launch {
            navigationEvents.emit(value = NavigationEvent.NavigateTo(route = screen))
        }
    }

    fun navigateBack() {
        CoroutineScope(context = Dispatchers.Main).launch {
            navigationEvents.emit(value = NavigationEvent.PopBackStack)
        }
    }

    fun navigateup() {
        CoroutineScope(context = Dispatchers.Main).launch {
            navigationEvents.emit(value = NavigationEvent.NavigateUp)
        }
    }
}