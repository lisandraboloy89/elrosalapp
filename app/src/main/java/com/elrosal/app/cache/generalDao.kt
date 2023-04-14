package com.elrosal.app.cache

import androidx.room.*

@Dao
interface generalDao {
    @Query("SELECT * FROM general ")
    suspend fun getListaInfoAll():List<general>

    @Query("SELECT * FROM general WHERE objetId = :id ")
    suspend fun getByIdInfoGeneral(id:String):general

    @Update
    suspend fun updateInfoGeneral(dato:general)

    @Insert
    suspend fun insertListaInfoGeneral(data:List<general>)

    @Insert
    suspend fun insertInfoGeneral(data:general)

    @Delete
    suspend fun deleteInfoGeneral(data: general)

    //----------borrar toda la tabla---------------
    @Query("DELETE FROM general")
    suspend fun allTableDeleteInfoGeneral()
}