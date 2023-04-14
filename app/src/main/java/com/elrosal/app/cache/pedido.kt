package com.elrosal.app.cache

import android.support.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedido")
data class pedido(
    @PrimaryKey(autoGenerate = true)
    var IdPedido:Int=0,
    @NonNull
    var producto:String,
    @NonNull
    var cantidad:String,
    var precio:String
)
