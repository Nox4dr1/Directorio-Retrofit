package com.upb.directorio.data.remote

import com.upb.directorio.BuildConfig
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Crea instancias de Retrofit que comparten un solo cliente HTTP. */
object FabricaRetrofit {

    private val clienteHttp: OkHttpClient by lazy {
        val builder = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS) // tiempo máximo para conectarse
            .readTimeout(15, TimeUnit.SECONDS)    // tiempo máximo esperando la respuesta
            .writeTimeout(15, TimeUnit.SECONDS)   // tiempo máximo enviando el cuerpo
        if (BuildConfig.DEBUG) {
            // Solo en desarrollo. La clave se oculta para que no aparezca en Logcat.
            builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
                redactHeader("x-api-key")
            })
        }
        builder.build()
    }

    /** `baseUrl` debe terminar en "/". */
    fun crear(baseUrl: String): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(clienteHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
}