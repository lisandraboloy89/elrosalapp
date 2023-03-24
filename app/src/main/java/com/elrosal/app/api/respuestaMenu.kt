package com.elrosal.app.api

import com.google.gson.annotations.SerializedName

data class respuestaMenu(
    @SerializedName("objectId")
    var objectId: String,
    @SerializedName("nombre")
    var nombre: String,
    @SerializedName("precio")
    var precio: String,
    @SerializedName("descripcion")
    var descripcion: String,
    @SerializedName("fecha")
    var fecha: String,
    @SerializedName("createdAt")
    var createdAt: String,
    @SerializedName("updatedAt")
    var updatedAt: String,
    @SerializedName("muestra")
    var muestraFoto: String,
    @SerializedName("tipo")
    var tipo: String,
    @SerializedName("estado")
    var estado: String
)
