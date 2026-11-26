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
// Lo que se ENVÍA en POST y PUT (el cuerpo de la petición).
// @SerializedName también sirve al enviar: "nombre" viaja como "name".
data class UsuarioRequest(
    @SerializedName("name") val nombre: String,
    @SerializedName("job") val trabajo: String
)

// Lo que se RECIBE al crear (POST): el servidor asigna id y fecha.
data class UsuarioCreadoResponse(
    val id: String,                                   // ReqRes manda el id como texto
    @SerializedName("createdAt") val creadoEn: String
)

// Lo que se RECIBE al editar (PUT).
data class UsuarioEditadoResponse(
    @SerializedName("updatedAt") val editadoEn: String
)