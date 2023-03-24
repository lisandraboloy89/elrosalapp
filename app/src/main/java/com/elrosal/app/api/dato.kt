package com.elrosal.app.api

import com.google.gson.annotations.SerializedName

data class dato(
    @SerializedName("results")
    var results: List<respuestaMenu>,
)
