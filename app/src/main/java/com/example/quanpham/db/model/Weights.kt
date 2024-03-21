package com.example.quanpham.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.quanpham.model.Users

@Entity(tableName = "Weights")
class Weights (
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null ,
    var weight : Float? = null,
    var updateTime : String? = null,
    )