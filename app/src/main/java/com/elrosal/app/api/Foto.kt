package com.elrosal.app.api

import com.google.gson.annotations.SerializedName

data class Foto(
    @SerializedName("__type")
    var type: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("url")
    var url: String
)
