package com.example.quanpham.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.quanpham.db.Dao.StepDao
import com.example.quanpham.db.Dao.WeightDao
import com.example.quanpham.db.model.Steps
import com.example.quanpham.db.model.Weights
import com.example.quanpham.model.Users
import com.example.quanpham.utility.Converters


@Database(entities = [Steps::class, Weights::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun stepDao(): StepDao
    abstract fun weightDao(): WeightDao

}