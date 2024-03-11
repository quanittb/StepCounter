package com.example.quanpham.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User")
class Users (
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null ,
    @ColumnInfo
    var tenDN : String? = null,
    @ColumnInfo
    var matKhau : String? = null,
    @ColumnInfo
    var hoTen : String? = null,
    @ColumnInfo
    var tuoi : Int? = null,
    @ColumnInfo
    var gioiTinh : Boolean? = null,
    @ColumnInfo
    var chieuCao : Float? = null,
    )