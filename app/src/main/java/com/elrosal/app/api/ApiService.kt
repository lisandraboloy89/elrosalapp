package com.elrosal.app.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface ApiService {
    //----->>>>>>GET-------Obtener detalles del paciente-------------------------<<<<<<<<
    @Headers(
        "accept: application/json",
        "X-Parse-Application-Id: 3cy8TUZoPrw4kICbVtghE0ISMsoPLceJ5RM8RXZo",
        "X-Parse-REST-API-Key: sMP0zd8mFhKeRtNyMxh8JFg8ey7Z48zIwlFV7DCG",
        "X-Parse-Revocable-Session: 1"
    )
    @GET("classes/menu")
    suspend fun getObtenerMenu (): Response<dato>
}