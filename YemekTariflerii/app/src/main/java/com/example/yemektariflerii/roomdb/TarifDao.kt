package com.example.yemektariflerii.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.yemektariflerii.model.Tarif
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Flowable
import java.util.concurrent.Flow


@Dao
interface TarifDao {

    @Query("SELECT * FROM Tarif")
    fun getAll() : Flowable<List<Tarif>>

    @Query("SELECT * FROM Tarif WHERE id = :id")
    fun findById(id : Int) : Flowable<Tarif>

    @Insert
    fun insert(tarif: Tarif) : Completable

    @Delete
    fun delete(tarif: Tarif) : Completable



}