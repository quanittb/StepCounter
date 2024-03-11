package com.example.quanpham.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Steps")
class Steps (
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null ,
    @ColumnInfo
    var buocChan : Float? = null,
    @ColumnInfo
    var thoiDiemBatDau : String? = null,
    @ColumnInfo
    var thoiGianHoatDong : Long? = null,
    )