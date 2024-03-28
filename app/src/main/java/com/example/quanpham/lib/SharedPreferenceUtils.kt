package com.example.quanpham.lib


import com.intuit.sdp.BuildConfig


object SharedPreferenceUtils {
    private const val FIRST_OPEN_APP = "FIRST_OPEN_APP"
    private const val LANGUAGE = "LANGUAGE"
    private const val SELECT_SEX = "SELECT_SEX"
    private const val AGE = "AGE"
    private const val HEIGHT = "HEIGHT"
    private const val WEIGHT = "WEIGHT"
    private const val START_COUNT_STEP = "START_COUNT_STEP"
    private const val DAY_COUNT_STEP = "DAY_COUNT_STEP"
    private const val YESTERDAY_COUNT_STEP = "YESTERDAY_COUNT_STEP"
    private const val TARGET_STEP = "TARGET_STEP"
    private const val UNIT = "UNIT"
    private const val HOUR_ALARM = "HOUR_ALARM"
    private const val MINUTE_ALARM = "MINUTE_ALARM"
    private const val FIRST_PERMISSION_REQUIRED = "FIRST_PERMISSION_REQUIRED"
    private const val CHECK_COUNT_REJECT = "CHECK_COUNT_REJECT"
    private const val SET_OR_CLOSE_STEP_GOAL = "SET_OR_CLOSE_STEP_GOAL"

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
    var yesterdayStep: Long
        get() = sharedPreferencesManager.getLongValue(YESTERDAY_COUNT_STEP, 0)
        set(value) = sharedPreferencesManager.setLongValue(YESTERDAY_COUNT_STEP, value)
    var targetStep: Long
        get() = sharedPreferencesManager.getLongValue(TARGET_STEP, 6000)
        set(value) = sharedPreferencesManager.setLongValue(TARGET_STEP, value)
    var selectSex: Int
        get() = sharedPreferencesManager.getIntValue(SELECT_SEX, -1)
        set(value) = sharedPreferencesManager.setIntValue(SELECT_SEX, value)
    var age: Int
        get() = sharedPreferencesManager.getIntValue(AGE, 0)
        set(value) = sharedPreferencesManager.setIntValue(AGE, value)

    //cm : Float
    var height: Float
        get() = sharedPreferencesManager.getValue(HEIGHT, 0f)
        set(value) = sharedPreferencesManager.setValue(HEIGHT, value)

    //kg : Float
    var weight: Float
        get() = sharedPreferencesManager.getValue(WEIGHT, 0f)
        set(value) = sharedPreferencesManager.setValue(WEIGHT, value)
    var unit: Boolean
        get() = sharedPreferencesManager.getValueBool(UNIT, true)
        set(value) = sharedPreferencesManager.setValueBool(UNIT, value)
    var hourAlarm: Int
        get() = sharedPreferencesManager.getIntValue(HOUR_ALARM, -1)
        set(value) = sharedPreferencesManager.setIntValue(HOUR_ALARM, value)
    var minuteAlarm: Int
        get() = sharedPreferencesManager.getIntValue(MINUTE_ALARM, 0)
        set(value) = sharedPreferencesManager.setIntValue(MINUTE_ALARM, value)
    var firstPermissionRequired: Boolean
        get() = sharedPreferencesManager.getValueBool(FIRST_PERMISSION_REQUIRED, true)
        set(value) = sharedPreferencesManager.setValueBool(FIRST_PERMISSION_REQUIRED, value)
    var checkCountRejectPermission: Int
        get() = sharedPreferencesManager.getIntValue(CHECK_COUNT_REJECT, 1)
        set(value) = sharedPreferencesManager.setIntValue(CHECK_COUNT_REJECT, value)
    var setOrStartGoal: Boolean
        get() = sharedPreferencesManager.getValueBool(SET_OR_CLOSE_STEP_GOAL, false)
        set(value) = sharedPreferencesManager.setValueBool(SET_OR_CLOSE_STEP_GOAL, value)
}
