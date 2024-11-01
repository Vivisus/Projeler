package com.example.yemektariflerii.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.yemektariflerii.model.Tarif

@Database(entities = [Tarif::class], version = 1)
abstract class TarifDataBase : RoomDatabase() {
    abstract fun TarifDao(): TarifDao
}