package com.elrosal.app.api

import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    //----->>>>>>GET-------Obtener datos del menu-------------------------<<<<<<<<
    @Headers(
        "accept: application/json",
        "X-Parse-Application-Id: 3cy8TUZoPrw4kICbVtghE0ISMsoPLceJ5RM8RXZo",
        "X-Parse-REST-API-Key: sMP0zd8mFhKeRtNyMxh8JFg8ey7Z48zIwlFV7DCG",
        "X-Parse-Revocable-Session: 1"
    )
    @GET("classes/menu?where=%7B%20%22estado%22%3A%20true%20%7D")
    suspend fun getObtenerMenu (): Response<dato>
    //----->>>>>>GET-------Eliminar datos del menu-------------------------<<<<<<<<
    @Headers(
        "accept: application/json",
        "X-Parse-Application-Id: 3cy8TUZoPrw4kICbVtghE0ISMsoPLceJ5RM8RXZo",
        "X-Parse-REST-API-Key: sMP0zd8mFhKeRtNyMxh8JFg8ey7Z48zIwlFV7DCG",
        "X-Parse-Revocable-Session: 1"
    )
    @DELETE("classes/menu/{userId}")
    suspend fun eliminarMenu (@Path("userId") userId: String): Response<respuestaRegistroMenu>
    //------------Registrar datos del menu metodo post--------------------
    @Headers(
        "accept: application/json",
        "X-Parse-Application-Id: 3cy8TUZoPrw4kICbVtghE0ISMsoPLceJ5RM8RXZo",
        "X-Parse-REST-API-Key: sMP0zd8mFhKeRtNyMxh8JFg8ey7Z48zIwlFV7DCG",
        "X-Parse-Revocable-Session: 1"
    )
    @POST("classes/menu")
    suspend fun postRegistrarMenu(@Body Registrar: envioDatoMenu): Response<respuestaRegistroMenu>
    //------------Registrar datos Generales metodo post--------------------
    @Headers(
        "accept: application/json",
        "X-Parse-Application-Id: 3cy8TUZoPrw4kICbVtghE0ISMsoPLceJ5RM8RXZo",
        "X-Parse-REST-API-Key: sMP0zd8mFhKeRtNyMxh8JFg8ey7Z48zIwlFV7DCG",
        "X-Parse-Revocable-Session: 1"
    )
    @POST("classes/datos")
    suspend fun postRegistrarDatos(@Body Registrar: envioDatosGenerales): Response<respuestaRegistroMenu>
    //----->>>>>>GET-------Obtener datos Generales----------------------<<<<<<<<
    @Headers(
        "accept: application/json",
        "X-Parse-Application-Id: 3cy8TUZoPrw4kICbVtghE0ISMsoPLceJ5RM8RXZo",
        "X-Parse-REST-API-Key: sMP0zd8mFhKeRtNyMxh8JFg8ey7Z48zIwlFV7DCG",
        "X-Parse-Revocable-Session: 1"
    )
    @GET("classes/datos")
    suspend fun getObtenerDatosG (): Response<datosGenerales>
    //------------Registrar Pagos metodo post--------------------
    @Headers(
        "accept: application/json",
        "X-Parse-Application-Id: 3cy8TUZoPrw4kICbVtghE0ISMsoPLceJ5RM8RXZo",
        "X-Parse-REST-API-Key: sMP0zd8mFhKeRtNyMxh8JFg8ey7Z48zIwlFV7DCG",
        "X-Parse-Revocable-Session: 1"
    )
    @POST("classes/pagos")
    suspend fun postRegistrarPagos(@Body Registrar: enviardataPagos): Response<respuestaRegistroMenu>
    //----->>>>>>GET-------Obtener pagos----------------------<<<<<<<<
    @Headers(
        "accept: application/json",
        "X-Parse-Application-Id: 3cy8TUZoPrw4kICbVtghE0ISMsoPLceJ5RM8RXZo",
        "X-Parse-REST-API-Key: sMP0zd8mFhKeRtNyMxh8JFg8ey7Z48zIwlFV7DCG",
        "X-Parse-Revocable-Session: 1"
    )
    @GET("classes/pagos")
    suspend fun getObtenerPagos (): Response<datoPagos>
}