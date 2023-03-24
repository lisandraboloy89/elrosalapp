package com.elrosal.app.api

data class datoPagos(
    val results: List<dataPagos>
)
data class dataPagos(
    val objectId: String,
    val productos: String,
    val importe: String,
    val fecha: String,
    val createdAt: String,
    val updatedAt: String
)
data class enviardataPagos(
    val productos: String,
    val importe: String,
    val fecha: String
)