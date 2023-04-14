package com.elrosal.app.cache

import androidx.room.*

@Dao
interface pedidoDao {
    @Query("SELECT * FROM pedido ")
    suspend fun getListaPedidosAll():List<pedido>

    @Query("SELECT * FROM pedido WHERE IdPedido = :id ")
    suspend fun getByIdPedido(id:String):pedido

    @Update
    suspend fun updatePedido(pedido:pedido)

    @Insert
    suspend fun insertListaPedido(data:List<pedido>)

    @Insert
    suspend fun insertPedido(data:pedido)

    @Delete
    suspend fun deletePedido(pedido: pedido)

    //----------borrar toda la tabla---------------
    @Query("DELETE FROM pedido")
    suspend fun allTableDelete()
}