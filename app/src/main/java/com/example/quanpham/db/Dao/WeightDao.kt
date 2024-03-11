package com.example.quanpham.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quanpham.db.model.Weights

@Dao
interface WeightDao {
    @Insert
    fun insert(weight: Weights)
}