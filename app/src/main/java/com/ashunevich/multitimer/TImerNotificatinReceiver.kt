package com.ashunevich.multitimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ashunevich.multitimer.Util.NotificationUtil
import com.ashunevich.multitimer.Util.PrefUtil

class TImerNotificatinReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when(intent.action){
            AppConstants.ACTION_STOP->{
                TimerFragment.removeAlarm(context)
                PrefUtil.setTimerState(TimerFragment.TimerState.Stopped,context)
                NotificationUtil.hideTimerNotification(context)
            }
            AppConstants.ACTION_PAUSE->{
                var secondsRemaining = PrefUtil.getSecRemaining(context)
                val alarmSetTime = PrefUtil.getAlarmSetTime(context)
                val nowSeconds = TimerFragment.nowSeconds

                secondsRemaining -= nowSeconds - alarmSetTime
                PrefUtil.setSecRemaining(secondsRemaining,context)

                TimerFragment.removeAlarm(context)
                PrefUtil.setTimerState(TimerFragment.TimerState.Paused,context)
                NotificationUtil.showTimerPaused(context)
            }
            AppConstants.ACTION_RESUME->{
                val secondsRemaining = PrefUtil.getSecRemaining(context)
                val wakeUpTime = TimerFragment.setAlarm(context,TimerFragment.nowSeconds,secondsRemaining)
                PrefUtil.setTimerState(TimerFragment.TimerState.Running,context)
                NotificationUtil.showTimerRunning(context,wakeUpTime)
            }
            AppConstants.ACTION_START->{
                val minRemaining = PrefUtil.getTimerlength(context)
                val secondsRemaining = minRemaining*60L
                val wakeUpTime = TimerFragment.setAlarm(context,TimerFragment.nowSeconds,secondsRemaining)
                PrefUtil.setTimerState(TimerFragment.TimerState.Running,context)
                PrefUtil.setSecRemaining(secondsRemaining,context)
                NotificationUtil.showTimerRunning(context,wakeUpTime)
            }
        }

    }
}