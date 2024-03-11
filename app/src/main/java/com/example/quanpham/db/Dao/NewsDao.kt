package com.example.quanpham.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quanpham.db.model.News

@Dao
interface NewsDao {
    @Insert
    fun insert(news: News)
}