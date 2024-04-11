package com.example.quanpham.db.model

import java.util.Date

class StepsFirebase (
    var activeTime : Long = 0,
    var calo : Double = 0.0,
    var distance : Double = 0.0,
    var isPush : Boolean = false,
    var startTime : StartTime ,
    var step : Long = 0

    ){
    // Hàm tạo không tham số
    constructor() : this(0, 0.0, 0.0, false, StartTime(0,0,0,0,0,0,0,0,0), 0)
}

