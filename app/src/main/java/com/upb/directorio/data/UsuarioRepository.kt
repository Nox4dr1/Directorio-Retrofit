package com.upb.directorio.data

import com.upb.directorio.BuildConfig
import com.upb.directorio.data.remote.ReqresApiService
import com.upb.directorio.data.remote.RetrofitClient
import com.upb.directorio.model.PaginaUsuarios
import com.upb.directorio.model.Usuario
import com.upb.directorio.util.Resultado
import java.io.IOException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException

/**
 * Única clase que llama a la API. Convierte DTO en modelo y traduce las excepciones
 * en un Resultado que la pantalla entiende.
 */
class UsuarioRepository(
    private val api: ReqresApiService = RetrofitClient.api,
    private val apiKey: String = BuildConfig.API_KEY
) {

    suspend fun obtenerUsuarios(pagina: Int): Resultado<PaginaUsuarios> = llamar {
        api.obtenerUsuarios(apiKey, pagina).aPagina()
    }

    suspend fun obtenerUsuario(id: Int): Resultado<Usuario> = llamar {
        api.obtenerUsuario(apiKey, id).data.aUsuario()
    }

    /** Ejecuta la llamada y convierte cualquier excepción en Resultado.Error. */
    private suspend fun <T> llamar(bloque: suspend () -> T): Resultado<T> =
        try {
            Resultado.Exito(bloque())
        } catch (e: CancellationException) {
            throw e // cancelar la corrutina no es un error
        } catch (e: HttpException) {
            Resultado.Error(mensajeHttp(e.code()))
        } catch (e: IOException) {
            Resultado.Error("No hay conexión a internet. Revisa tu red e inténtalo de nuevo.")
        } catch (e: Exception) {
            Resultado.Error("Ocurrió un error inesperado.")
        }

    private fun mensajeHttp(codigo: Int): String = when (codigo) {
        401, 403 -> "La API key falta o es incorrecta (código $codigo)."
        404 -> "No se encontró ese usuario (código 404)."
        else -> "El servidor respondió con un error (código $codigo)."
    }
}