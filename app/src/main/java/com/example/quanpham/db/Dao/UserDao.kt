package com.example.quanpham.db.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.quanpham.db.model.Users

@Dao
interface UserDao {
    @Insert
    fun insert(users: Users)
    @Query("Select * from User")
    fun getAll():List<Users>
}