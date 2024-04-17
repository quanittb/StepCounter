package com.example.quanpham.app

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.quanpham.db.AppDatabase
import com.example.quanpham.utility.Constant


class ContextProvider : Application() {
    companion object {
        lateinit var appContext: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }

}

object AppContext{
    val context: Context
        get() = ContextProvider.appContext
}
val database = Room.databaseBuilder(
    AppContext.context,
    AppDatabase::class.java, Constant.STEP_DB
).allowMainThreadQueries().build()