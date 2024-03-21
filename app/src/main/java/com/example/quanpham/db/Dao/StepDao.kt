package com.example.quanpham.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.quanpham.db.model.Steps
import com.example.quanpham.lib.SharedPreferenceUtils
import java.util.Date

@Dao
interface StepDao {
    @Insert
    fun insert(step: Steps)
    @Query("Select * from Steps")
    fun getAll() : List<Steps>

    @Query("Select * from Steps where startTime >= :minTime And startTime <= :maxTime")
    fun getStepsHour(minTime : Long, maxTime: Long): Steps
    @Query("Select SUM(step) from Steps where startTime >= :minTime And startTime <= :maxTime")
    fun getStepsDay(minTime: Long, maxTime: Long): Long

    @Update
    fun updateStep(step: Steps)
}