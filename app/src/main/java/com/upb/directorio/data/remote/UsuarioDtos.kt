package com.upb.directorio.data.remote

import com.google.gson.annotations.SerializedName

// GET https://reqres.in/api/users?page=2
data class UsuariosResponse(
    val page: Int,
    @SerializedName("total_pages") val totalPaginas: Int,
    val data: List<UsuarioDto>
)

// GET https://reqres.in/api/users/2
data class UsuarioResponse(
    val data: UsuarioDto
)

data class UsuarioDto(
    val id: Int,
    val email: String,
    @SerializedName("first_name") val nombre: String,
    @SerializedName("last_name") val apellido: String,
    val avatar: String?
)