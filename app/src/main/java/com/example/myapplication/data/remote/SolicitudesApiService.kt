package com.example.myapplication.data.remote

import com.example.myapplication.data.model.SolicitudApiDto
import retrofit2.http.GET

interface SolicitudesApiService {
    @GET("posts")
    suspend fun obtenerSolicitudes(): List<SolicitudApiDto>
}
