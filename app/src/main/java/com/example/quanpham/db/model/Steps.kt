package com.example.quanpham.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.util.foreignKeyCheck
import com.example.quanpham.db.AppDatabase
import com.google.firebase.database.PropertyName
import java.util.Date

@Entity(tableName = "Steps")
class Steps (
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null ,
    var step : Long = 0,
    var startTime : Date? = null,
    var activeTime : Long? = null,
    var calo : Long? = null,
    var distance : Long? = null,
    ){
    @get:PropertyName("id")
    @set:PropertyName("id")
    var idValue: Long?
        get() = id
        set(value) {
            id = value
        }

    // Getter và setter cho trường step
    @get:PropertyName("step")
    @set:PropertyName("step")
    var stepValue: Long
        get() = step
        set(value) {
            step = value
        }

    // Getter và setter cho trường startTime
    @get:PropertyName("startTime")
    @set:PropertyName("startTime")
    var startTimeValue: Date?
        get() = startTime
        set(value) {
            startTime = value
        }

    // Getter và setter cho trường activeTime
    @get:PropertyName("activeTime")
    @set:PropertyName("activeTime")
    var activeTimeValue: Long?
        get() = activeTime
        set(value) {
            activeTime = value
        }

    // Getter và setter cho trường calo
    @get:PropertyName("calo")
    @set:PropertyName("calo")
    var caloValue: Long?
        get() = calo
        set(value) {
            calo = value
        }

    // Getter và setter cho trường distance
    @get:PropertyName("distance")
    @set:PropertyName("distance")
    var distanceValue: Long?
        get() = distance
        set(value) {
            distance = value
        }
}

