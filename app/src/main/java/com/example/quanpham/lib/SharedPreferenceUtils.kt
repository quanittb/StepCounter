package com.example.quanpham.lib


import com.intuit.sdp.BuildConfig
import org.checkerframework.checker.units.qual.A


object SharedPreferenceUtils {
    private const val FIRST_OPEN_APP = "FIRST_OPEN_APP"
    private const val LANGUAGE = "LANGUAGE"
    private const val SELECT_SEX = "SELECT_SEX"
    private const val AGE = "AGE"
    private const val HEIGHT = "HEIGHT"
    private const val HEIGHT0_TEMPORARY = "HEIGHT0_TEMPORARY"
    private const val HEIGHT1_TEMPORARY = "HEIGHT1_TEMPORARY"
    private const val WEIGHT = "WEIGHT"
    private const val WEIGHT0_TEMPORARY = "WEIGHT0_TEMPORARY"
    private const val WEIGHT1_TEMPORARY = "WEIGHT1_TEMPORARY"
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
    private const val STEP_LENGTH = "STEP_LENGTH"
    private const val AUTO_CALCULATE_STEP_LENGTH = "AUTO_CALCULATE_STEP_LENGTH"
    private const val BMI ="BMI"
    private const val NAME ="NAME"
    private const val ALARM ="ALARM"


    var firstOpenApp: Boolean
        get() = sharedPreferencesManager.getValueBool(FIRST_OPEN_APP, true)
        set(value) = sharedPreferencesManager.setValueBool(FIRST_OPEN_APP, value)

    var languageCode: String?
        get() =  sharedPreferencesManager.getValue(LANGUAGE, null)
        set(value) = sharedPreferencesManager.setValue(LANGUAGE, value)

    var startStep: Boolean
        get() = sharedPreferencesManager.getValueBool(START_COUNT_STEP, true)
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

    // Nam : 1, Nữ : 0
    var selectSex: Int
        get() = sharedPreferencesManager.getIntValue(SELECT_SEX, -1)
        set(value) = sharedPreferencesManager.setIntValue(SELECT_SEX, value)
    var age: Int
        get() = sharedPreferencesManager.getIntValue(AGE, 20)
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
        get() = sharedPreferencesManager.getIntValue(HOUR_ALARM, 6)
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
    // true : Đã mở -> Ko hiện
    var setOrStartGoal: Boolean
        get() = sharedPreferencesManager.getValueBool(SET_OR_CLOSE_STEP_GOAL, false)
        set(value) = sharedPreferencesManager.setValueBool(SET_OR_CLOSE_STEP_GOAL, value)
    var bmi: Float?
        get() = sharedPreferencesManager.getFloatValue(BMI, 18f)
        set(value) = sharedPreferencesManager.setFloatValue(BMI, value!!)
    //cm : Float
    var stepLength: Float
        get() = sharedPreferencesManager.getValue(STEP_LENGTH, 68.00f)
        set(value) = sharedPreferencesManager.setValue(STEP_LENGTH, value)
    var height0_temporary: Float
        get() = sharedPreferencesManager.getValue(HEIGHT0_TEMPORARY, 0f)
        set(value) = sharedPreferencesManager.setValue(HEIGHT0_TEMPORARY, value)
    var weight0_temporary: Float
        get() = sharedPreferencesManager.getValue(WEIGHT0_TEMPORARY, 0f)
        set(value) = sharedPreferencesManager.setValue(WEIGHT0_TEMPORARY, value)
    var height1_temporary: Float
        get() = sharedPreferencesManager.getValue(HEIGHT1_TEMPORARY, 0f)
        set(value) = sharedPreferencesManager.setValue(HEIGHT1_TEMPORARY, value)
    var weight1_temporary: Float
        get() = sharedPreferencesManager.getValue(WEIGHT1_TEMPORARY, 0f)
        set(value) = sharedPreferencesManager.setValue(WEIGHT1_TEMPORARY, value)
    var autoCalculateLength: Boolean
        get() = sharedPreferencesManager.getValueBool(AUTO_CALCULATE_STEP_LENGTH, false)
        set(value) = sharedPreferencesManager.setValueBool(AUTO_CALCULATE_STEP_LENGTH, value)
    var name: String?
        get() =  sharedPreferencesManager.getValue(NAME, "" )
        set(value) = sharedPreferencesManager.setValue(NAME, value)
    var alarm: Boolean
        get() = sharedPreferencesManager.getValueBool(ALARM, false)
        set(value) = sharedPreferencesManager.setValueBool(ALARM, value)

}
