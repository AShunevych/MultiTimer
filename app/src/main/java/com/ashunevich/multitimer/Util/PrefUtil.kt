package com.ashunevich.multitimer.util

import android.content.Context
import androidx.preference.PreferenceManager
import com.ashunevich.multitimer.TimerFragment

class PrefUtil {
    companion object {
        private const val TIMER_LENGTH_ID = "timer_length"
        fun getTimerlength(context: Context): Int {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(TIMER_LENGTH_ID, 10)
        }

        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID =
            "com.ashunevich.multitimer.TimerFragment.preovious_timer_length"

        fun getPrevTimerLenghtSec(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPrevTimerLenghtSec(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds).apply()
        }

        private const val TIMER_STATE_UD = "com.ashunevich.multitimer.timer_state"

        fun getTimerState(context: Context): TimerFragment.TimerState {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_UD, 0)
            return TimerFragment.TimerState.values()[ordinal]
        }

        fun setTimerState(state: TimerFragment.TimerState, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_UD, ordinal).apply()
        }

        private const val SECONDS_REMAINING =
            "com.ashunevich.multitimer.TimerFragment.seconds_remaining"

        fun getSecRemaining(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING, 0)
        }

        fun setSecRemaining(seconds: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING, seconds).apply()
        }

        private const val ALARM_SET_TIME = "Timer.background_item"

        fun getAlarmSetTime(context: Context): Long {
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context) {
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME, time).apply()
        }


    }
}