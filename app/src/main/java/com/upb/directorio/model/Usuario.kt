package com.upb.directorio.model

/** Un usuario tal como lo usa la app. */
data class Usuario(
    val id: Int,
    val nombreCompleto: String,
    val email: String,
    val fotoUrl: String?
)