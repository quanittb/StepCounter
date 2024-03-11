package com.example.quanpham.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.quanpham.db.Dao.NewsDao
import com.example.quanpham.db.Dao.StepDao
import com.example.quanpham.db.Dao.UserDao
import com.example.quanpham.db.Dao.WeightDao
import com.example.quanpham.db.model.News
import com.example.quanpham.db.model.Steps
import com.example.quanpham.db.model.Users
import com.example.quanpham.db.model.Weights


@Database(entities = [Users::class, Steps::class, Weights::class, News::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun userDao(): UserDao
    abstract fun stepDao(): StepDao
    abstract fun weightDao(): WeightDao
    abstract fun newsDao(): NewsDao

}