package com.example.quanpham.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

class Users(var id:String?="",var logginName:String?="",
            var email:String?="",var password:String?="",
            var age:Int?=0,var sex:String?="",var tall:Float?=0f) : BaseModel<Users>()