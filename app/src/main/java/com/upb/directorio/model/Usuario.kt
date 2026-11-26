package com.upb.directorio.model

/** Un usuario tal como lo usa la app. */
data class Usuario(
    val id: Int,
    val nombreCompleto: String,
    val email: String,
    val fotoUrl: String?
)
/** Una página de la lista: los usuarios y en qué página estamos. */
data class PaginaUsuarios(
    val usuarios: List<Usuario>,
    val pagina: Int,
    val totalPaginas: Int
)