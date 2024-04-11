package com.example.quanpham.db.model

import java.util.Date

data class Rank(val id : String = "",val name: String? = "", val steps: Int = 0  ){
    constructor() : this("","",0)
}
