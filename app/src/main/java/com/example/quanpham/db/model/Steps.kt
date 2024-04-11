package com.example.quanpham.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.room.util.foreignKeyCheck
import com.example.quanpham.db.AppDatabase
import com.example.quanpham.utility.Converters
import com.google.firebase.database.PropertyName
import java.util.Date

@Entity(tableName = "Steps")
class Steps (
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null ,
    var step : Long = 0,
    @TypeConverters(Converters::class)
    var startTime : Date ,
    var activeTime : Long = 0,
    var calo : Double = 0.0,
    var distance : Double = 0.0,
    var isPush : Boolean = false
    )

