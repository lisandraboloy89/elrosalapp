package com.elrosal.app.cache

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(
    entities = [pedido::class,menu::class,general::class],
    version = 1
)
abstract class cacheDB:RoomDatabase() {
    abstract fun pedidoDao():pedidoDao
    abstract fun menuDao():menuDao
    abstract fun generalDao():generalDao

    companion object{
        const val   DATABASE_NAME="cacheDB"
    }
}