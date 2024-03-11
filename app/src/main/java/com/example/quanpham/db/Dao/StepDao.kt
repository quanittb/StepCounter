package com.example.quanpham.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quanpham.db.model.Steps

@Dao
interface StepDao {
    @Insert
    fun insert(step: Steps)
}