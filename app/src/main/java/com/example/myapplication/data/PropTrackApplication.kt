package com.example.myapplication.data

import android.app.Application
import com.example.myapplication.repository.PropTrackRepository

class PropTrackApplication : Application() {
    val database by lazy { PropTrackDatabase.getDatabase(this) }
    val repository by lazy { PropTrackRepository(database) }
}
