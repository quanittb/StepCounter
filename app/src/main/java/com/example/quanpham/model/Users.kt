package com.example.quanpham.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

class Users(var id:String?="",var userName:String?="",
            var pass:String?="",var name:String?="",
            var age:Int?=18,var gender:Boolean?= true,var height:Float?=170f) : BaseModel<Users>()