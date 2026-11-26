package com.upb.directorio.data

import com.upb.directorio.data.remote.UsuarioDto
import com.upb.directorio.model.Usuario
import com.upb.directorio.data.remote.UsuariosResponse
import com.upb.directorio.model.PaginaUsuarios

/** Convierte la forma de la API (DTO) en la forma de la app (modelo). */
fun UsuarioDto.aUsuario(): Usuario = Usuario(
    id = id,
    nombreCompleto = "$nombre $apellido",   // la app quiere el nombre junto
    email = email,
    fotoUrl = avatar
)
/** Convierte la respuesta de la lista en una página del modelo. */
fun UsuariosResponse.aPagina(): PaginaUsuarios = PaginaUsuarios(
    usuarios = data.map { it.aUsuario() },
    pagina = page,
    totalPaginas = totalPaginas
)