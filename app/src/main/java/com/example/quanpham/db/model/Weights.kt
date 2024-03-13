package com.example.quanpham.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Weights")
class Weights (
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null ,
    @ColumnInfo
    var weight : Float? = null,
    @ColumnInfo
    var updateTime : String? = null,
    )