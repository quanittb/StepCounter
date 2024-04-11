package com.example.quanpham.db.model

import java.util.Date

class StartTime(
    var date: Int = 0,
    var day: Int = 0,
    var hours: Int = 0,
    var minutes: Int = 0,
    var month: Int = 0,
    var seconds: Int = 0,
    var time: Long = 0,
    var timezoneOffset: Int = 0,
    var year: Long = 0,
){
    constructor(): this(0,0,0,0,0,0,0,0,0)
}

