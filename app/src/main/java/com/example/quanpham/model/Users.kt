package com.example.quanpham.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

// giới tính : true: Nam
class Users(var id:String?="",var userName:String="", var name : String="",
            var password:String?="",
            var age:Int?=0,var gender:Boolean=true,var height:Float?=0f) : BaseModel<Users>()