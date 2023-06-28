package com.elrosal.app.cache

import android.support.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "menu")
data class menu(
    @PrimaryKey
    @NonNull
    var objetId:String,
    @NonNull
    var nombre:String,
    @NonNull
    var precio:String,
    var descripcion:String,
    var gramaje:String,
    var estado:Boolean,
    var muestra:String,
    var tipo:String
)
