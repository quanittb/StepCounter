package com.example.quanpham.db.Dao

import androidx.room.Dao
import androidx.room.Delete
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
    @Query("Select SUM(calo) from Steps where startTime >= :minTime And startTime <= :maxTime")
    fun getCaloDay(minTime: Long, maxTime: Long): Long
    @Query("Select SUM(activeTime) from Steps where startTime >= :minTime And startTime <= :maxTime")
    fun getTimeDay(minTime: Long, maxTime: Long): Long
    @Query("Select SUM(distance) from Steps where startTime >= :minTime And startTime <= :maxTime")
    fun getDistanceDay(minTime: Long, maxTime: Long): Double
    @Query("Select * from Steps where startTime >= :minTime And startTime <= :maxTime")
    fun getRecordStepsDay(minTime : Long, maxTime: Long): List<Steps>
    @Query("Select * from Steps where startTime >= :minTime And startTime <= :maxTime And isPush = 0")
    fun getRecordStepsToPush(minTime : Long, maxTime: Long): List<Steps>
    @Update
    fun updateStep(step: Steps)
    @Query("UPDATE Steps SET isPush = 1 WHERE isPush = 0")
    fun updateStatePushForStep()
    @Query("Delete from Steps")
    fun deleteAllStep()

}