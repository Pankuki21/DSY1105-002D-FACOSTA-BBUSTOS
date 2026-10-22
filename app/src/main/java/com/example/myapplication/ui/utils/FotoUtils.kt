package com.example.myapplication.utils

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun guardarFotoLocal(
    context: Context,
    uri: Uri
): String {
    val directorio = File(context.filesDir, "fotografias")

    if (!directorio.exists() && !directorio.mkdirs()) {
        throw IllegalStateException(
            "No se pudo crear el directorio de fotografías."
        )
    }

    val archivo = File.createTempFile(
        "usuario_",
        ".jpg",
        directorio
    )

    try {
        val input = context.contentResolver.openInputStream(uri)
            ?: throw IllegalStateException(
                "No se pudo abrir la fotografía seleccionada."
            )

        input.use { entrada ->
            FileOutputStream(archivo).use { salida ->
                entrada.copyTo(salida)
            }
        }

        return archivo.absolutePath
    } catch (e: Exception) {
        archivo.delete()
        throw e
    }
}