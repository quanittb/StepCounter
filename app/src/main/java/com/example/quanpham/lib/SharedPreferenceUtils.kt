package com.example.quanpham.lib


import com.intuit.sdp.BuildConfig


object SharedPreferenceUtils {
    private const val FIRST_OPEN_APP = "FIRST_OPEN_APP"
    private const val LANGUAGE = "LANGUAGE"
    private const val SELECT_SEX = "SELECT_SEX"
    private const val AGE = "AGE"
    private const val HEIGHT = "HEIGHT"
    private const val START_COUNT_STEP = "START_COUNT_STEP"
    private const val DAY_COUNT_STEP = "DAY_COUNT_STEP"

    var firstOpenApp: Boolean
        get() = sharedPreferencesManager.getValueBool(FIRST_OPEN_APP, true)
        set(value) = sharedPreferencesManager.setValueBool(FIRST_OPEN_APP, value)

    var languageCode: String?
        get() =  sharedPreferencesManager.getValue(LANGUAGE, null)
        set(value) = sharedPreferencesManager.setValue(LANGUAGE, value)

    var startStep: Boolean
        get() = sharedPreferencesManager.getValueBool(START_COUNT_STEP, false)
        set(value) = sharedPreferencesManager.setValueBool(START_COUNT_STEP, value)

    var dayStep: Long
        get() = sharedPreferencesManager.getLongValue(DAY_COUNT_STEP, 0)
        set(value) = sharedPreferencesManager.setLongValue(DAY_COUNT_STEP, value)

}
