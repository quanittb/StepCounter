package com.example.quanpham.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.util.foreignKeyCheck
import com.example.quanpham.db.AppDatabase

@Entity(tableName = "Steps")
class Steps (
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null ,
    @ColumnInfo
    var step : Long? = null,
    @ColumnInfo
    var startTime : String? = null,
    @ColumnInfo
    var activeTime : Long? = null,
    @ColumnInfo
    var calo : Long? = null,
    @ColumnInfo
    var distance : Long? = null
    )