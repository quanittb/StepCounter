package com.example.quanpham.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quanpham.db.Dao.StepDao
import com.example.quanpham.db.Dao.WeightDao
import com.example.quanpham.db.model.Steps
import com.example.quanpham.db.model.Weights


@Database(entities = [Steps::class, Weights::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun stepDao(): StepDao
    abstract fun weightDao(): WeightDao

}