package com.elrosal.app.cache

import androidx.room.*

@Dao
interface menuDao {
    @Query("SELECT * FROM menu ")
    suspend fun getListaMenuAll():List<menu>

    @Query("SELECT * FROM menu WHERE objetId = :id ")
    suspend fun getByIdMenu(id:String):menu

    @Update
    suspend fun updateMenu(menu:menu)

    @Insert
    suspend fun insertListaMenu(data:List<menu>)

    @Insert
    suspend fun insertMenu(data:menu)

    @Delete
    suspend fun deleteMenu(data: menu)

    //----------borrar toda la tabla---------------
    @Query("DELETE FROM menu")
    suspend fun allTableDeleteMenu()
}