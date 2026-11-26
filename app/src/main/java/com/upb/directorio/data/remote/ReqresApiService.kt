package com.upb.directorio.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/** Endpoints de ReqRes. Todas las peticiones llevan la API key en la cabecera x-api-key. */
interface ReqresApiService {

    /** Lista paginada: /api/users?page=2 */
    @GET("users")
    suspend fun obtenerUsuarios(
        @Header("x-api-key") apiKey: String,
        @Query("page") pagina: Int
    ): UsuariosResponse

    /** Detalle: /api/users/2 */
    @GET("users/{id}")
    suspend fun obtenerUsuario(
        @Header("x-api-key") apiKey: String,
        @Path("id") id: Int
    ): UsuarioResponse

    /** POST: el objeto se convierte a JSON y viaja en el cuerpo. */
    @POST("users")
    suspend fun crearUsuario(
        @Header("x-api-key") apiKey: String,
        @Body usuario: UsuarioRequest
    ): UsuarioCreadoResponse

    /** PUT: reemplaza un recurso. El id va en la ruta y los datos en el cuerpo. */
    @PUT("users/{id}")
    suspend fun editarUsuario(
        @Header("x-api-key") apiKey: String,
        @Path("id") id: Int,
        @Body usuario: UsuarioRequest
    ): UsuarioEditadoResponse

    /** DELETE: no devuelve cuerpo, por eso Response<Unit> para leer solo el código. */
    @DELETE("users/{id}")
    suspend fun eliminarUsuario(
        @Header("x-api-key") apiKey: String,
        @Path("id") id: Int
    ): Response<Unit>
}