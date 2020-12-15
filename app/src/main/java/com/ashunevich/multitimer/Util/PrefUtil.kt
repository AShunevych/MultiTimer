package com.ashunevich.multitimer.Util

import android.content.Context
import com.ashunevich.multitimer.TimerFragment

class PrefUtil {
    companion object{
        fun getTimerlength(context: Context):Int{
            return 1
        }

        private const val  PREVIOUS_TIMER_LENGTH_SECONDS_ID =
            "com.ashunevich.multitimer.TimerFragment.preovious_timer_length"

        fun getPrevTimerLenghtSec(context: Context): Long{
            val preferences =  androidx.preference.PreferenceManager.
            getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID,0)
        }

        fun setPrevTimerLenghtSec(seconds:Long, context: Context){
            val editor = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context).
            edit();
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID,seconds)
            editor.apply()
        }

        private const val TIMER_STATE_UD = "com.ashunevich.multitimer.timer_state"

        fun getTimerState(context: Context): TimerFragment.TimerState{
            val preferences =  androidx.preference.PreferenceManager.
            getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_UD,0)
            return TimerFragment.TimerState.values()[ordinal]
        }

        fun setTimerState(state:TimerFragment.TimerState,context: Context){
            val editor = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context).
            edit();
            val ordinal =state.ordinal
            editor.putInt(TIMER_STATE_UD,ordinal)
            editor.apply()
        }

        private const val  SECONDS_REMAINING =
            "com.ashunevich.multitimer.TimerFragment.seconds_remaining"

        fun getSecRemaining(context: Context): Long{
            val preferences =  androidx.preference.PreferenceManager.
            getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING,0)
        }

        fun setSecRemaining(seconds:Long,context: Context){
            val editor = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context).
            edit();
            editor.putLong(SECONDS_REMAINING,seconds)
            editor.apply()
        }

        private const val ALARM_SET_TIME = "Timer.background_item"

        fun getAlarmSetTime(context: Context):Long{
            val preferences =  androidx.preference.PreferenceManager.
            getDefaultSharedPreferences(context)
            return preferences.getLong(ALARM_SET_TIME,0)
        }

        fun setAlarmSetTime(time:Long,context: Context){
            val editor = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context).
            edit();
             editor.putLong(ALARM_SET_TIME,time)
            editor.apply()
        }



    }
}