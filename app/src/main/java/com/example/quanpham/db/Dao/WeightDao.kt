package com.example.quanpham.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quanpham.db.model.Steps
import com.example.quanpham.db.model.Weights

@Dao
interface WeightDao {
    @Insert
    fun insert(weight: Weights)
    @Query("Select * from Weights where updateTime >= :minTime And updateTime <= :maxTime")
    fun getWeights(minTime : Long, maxTime: Long): List<Weights>
    @Query("Select * from Weights where updateTime >= :startDay And updateTime <= :endDay")
    fun getWeightDay(startDay : Long, endDay: Long): Weights
    @Update
    fun updateWeight(weight : Weights)
    @Query("Delete from Weights")
    fun deleteAllWeights()
}