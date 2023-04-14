package com.elrosal.app.cache

import android.support.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "general")
data class general(
    @PrimaryKey
    @NonNull
    var objetId:String,
    @NonNull
    var info:String,
    @NonNull
    var horarioInicio:String,
    var horarioCierre:String,
    var telf_fijo:String,
    var telf_celular:String,
    var direccion:String,
    var servicios:String,
    var tarjetaPago:String
)
