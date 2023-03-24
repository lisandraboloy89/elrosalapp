package com.elrosal.app.api

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import java.io.File
import java.lang.reflect.Array

data class envioDatoMenu(
    @SerializedName("nombre")
    val nombre: String,
    @SerializedName("precio")
    val precio: String,
    @SerializedName("gramaje")
    val gramaje: String,
    @SerializedName("descripcion")
    val descripcion: String,
    @SerializedName("muestra")
    val muestraFoto: String,
    @SerializedName("tipo")
    val tipo: String,
    @SerializedName("estado")
    val estado: String
)
