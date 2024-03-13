package com.example.quanpham.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "News")
class Users (
    @PrimaryKey(autoGenerate = true)
    var id : String? = null ,
    @ColumnInfo
    var loginName : String? = null,
    @ColumnInfo
    var password : String? = null,
    @ColumnInfo
    var sex : Boolean? = null,
    @ColumnInfo
    var age : Int? = null,
    @ColumnInfo
    var tall : Float? = null
    )