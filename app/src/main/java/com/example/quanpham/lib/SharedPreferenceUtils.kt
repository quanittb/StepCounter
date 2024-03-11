package com.example.quanpham.lib


import com.intuit.sdp.BuildConfig


object SharedPreferenceUtils {
    private const val FIRST_OPEN_APP = "FIRST_OPEN_APP"
    private const val LANGUAGE = "LANGUAGE"
    private const val SELECT_SEX = "SELECT_SEX"
    private const val AGE = "AGE"
    private const val HEIGHT = "HEIGHT"
    var firstOpenApp: Boolean
        get() = sharedPreferencesManager.getValueBool(FIRST_OPEN_APP, true)
        set(value) = sharedPreferencesManager.setValueBool(FIRST_OPEN_APP, value)

    var languageCode: String?
        get() =  sharedPreferencesManager.getValue(LANGUAGE, null)
        set(value) = sharedPreferencesManager.setValue(LANGUAGE, value)
}
