package com.upb.directorio.util

/** "Salió bien" con los datos o "salió mal" con un mensaje para el usuario. */
sealed class Resultado<out T> {
    data class Exito<out T>(val datos: T) : Resultado<T>()
    data class Error(val mensaje: String) : Resultado<Nothing>()
}