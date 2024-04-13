package com.example.quanpham.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.quanpham.model.Users
import com.example.quanpham.utility.Converters
import java.util.Date

class WeightsFirebase (
    var id : Long? = null ,
    var weight : Float? = null,
    var updateTime : StartTime
){
    constructor() : this(null,0f,StartTime())
}