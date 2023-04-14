package com.elrosal.app.api

import com.google.gson.annotations.SerializedName

data class datosGenerales(
    @SerializedName("results")
    val results: List<dataGenerales>
)

data class dataGenerales(
    @SerializedName("objectId")
    val objectId: String,
    @SerializedName("direccion")
    val direccion: String,
    @SerializedName("hora_inicio")
    val hora_inicio: String,
    @SerializedName("hora_cierre")
    val hora_cierre: String,
    @SerializedName("servicios")
    val servicios: String,
    @SerializedName("telefono")
    val telefono: String,
    @SerializedName("numero_pago")
    val numero_pago: String,
    @SerializedName("info")
    val info: String,
    @SerializedName("fijo")
    val fijo: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)
data class envioDatosGenerales(
    @SerializedName("direccion")
    val direccion: String,
    @SerializedName("hora_inicio")
    val hora_inicio: String,
    @SerializedName("hora_cierre")
    val hora_cierre: String,
    @SerializedName("servicios")
    val servicios: String,
    @SerializedName("telefono")
    val telefono: String,
    @SerializedName("numero_pago")
    val numero_pago: String,
    @SerializedName("info")
    val info: String,
    @SerializedName("fijo")
    val fijo: String
)

data class pulldatosGenerales(
    @SerializedName("updatedAt")
    val results: String
)