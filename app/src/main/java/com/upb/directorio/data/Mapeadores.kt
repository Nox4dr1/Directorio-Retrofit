package com.upb.directorio.data

import com.upb.directorio.data.remote.UsuarioDto
import com.upb.directorio.model.Usuario

/** Convierte la forma de la API (DTO) en la forma de la app (modelo). */
fun UsuarioDto.aUsuario(): Usuario = Usuario(
    id = id,
    nombreCompleto = "$nombre $apellido",   // la app quiere el nombre junto
    email = email,
    fotoUrl = avatar
)