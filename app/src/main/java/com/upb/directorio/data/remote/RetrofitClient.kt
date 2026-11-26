package com.upb.directorio.data.remote

/** Única instancia del servicio de ReqRes en toda la app (singleton con object). */
object RetrofitClient {

    // Debe terminar en "/" para que Retrofit pueda concatenar las rutas relativas.
    private const val BASE_URL = "https://reqres.in/api/"

    val api: ReqresApiService by lazy {
        FabricaRetrofit.crear(BASE_URL).create(ReqresApiService::class.java)
    }
}