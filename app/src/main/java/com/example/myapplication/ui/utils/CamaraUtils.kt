package com.example.myapplication.utils

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import java.io.File

fun crearUriImagen(context: Context): Uri {
    val directorio = File(
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
        "PropTrack"
    )

    if (!directorio.exists()) {
        directorio.mkdirs()
    }

    val archivo = File.createTempFile(
        "foto_perfil_",
        ".jpg",
        directorio
    )

    return FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        archivo
    )
}