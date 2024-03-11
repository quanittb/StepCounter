package com.example.quanpham.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "News")
class News (
    @PrimaryKey(autoGenerate = true)
    var id : Long? = null ,
    @ColumnInfo
    var tieuDe : String? = null,
    @ColumnInfo
    var noiDung : String? = null,
    @ColumnInfo
    var tacGia : String? = null,
    @ColumnInfo
    var thoiDiemTao : String? = null,
    )