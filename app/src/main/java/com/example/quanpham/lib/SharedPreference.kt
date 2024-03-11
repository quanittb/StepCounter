//package com.example.quanpham.lib
//
//import android.content.Context
//import android.content.SharedPreferences
//import com.example.quanpham.activity.MainActivity
//import com.example.quanpham.app.AppContext
//
//class SharedPreference {
//    companion object{
//        const val FIRST_OPEN_APP = "FIRST_OPEN_APP"
//    }
//    private val sharedPreferences: SharedPreferences =
//        AppContext.context?.getSharedPreferences("SharePreferencesStep", Context.MODE_PRIVATE)!!
//
//    fun edit(block: SharedPreferences.Editor.() -> Unit) {
//        with(sharedPreferences.edit()) {
//            block()
//            apply()
//        }
//    }
//
//    var firstOpenApp
//        get() = getBoolean(FIRST_OPEN_APP, true)
//        set(value) = putBoolean(FIRST_OPEN_APP, value)
//    fun getBoolean(key: String, default: Boolean = false): Boolean {
//        return sharedPreferences.getBoolean(key, default)
//    }
//
//    fun putBoolean(key: String, value: Boolean) {
//        edit {
//            putBoolean(key, value)
//        }
//    }
//
//    fun getString(key: String, default: String? = null): String? {
//        return sharedPreferences.getString(key, default)
//    }
//
//    fun putString(key: String, value: String) {
//        edit {
//            putString(key, value)
//        }
//    }
//    fun getInt(key: String, default: Int = 0): Int {
//        return sharedPreferences.getInt(key, default)
//    }
//
//    fun putInt(key: String, value: Int) {
//        edit {
//            putInt(key, value)
//        }
//    }
//
//    fun getLong(key: String, default: Long = 0L): Long {
//        return sharedPreferences.getLong(key, default)
//    }
//
//    fun putLong(key: String, value: Long) {
//        edit {
//            putLong(key, value)
//        }
//    }
//
//    fun clear() {
//        edit {
//            clear()
//        }
//    }
//}
//
//
//
//val sharedPreferences: SharedPreference
//    get() {
//        return SharedPreference()
//    }