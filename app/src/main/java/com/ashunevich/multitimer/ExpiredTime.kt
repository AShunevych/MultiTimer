package com.ashunevich.multitimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ashunevich.multitimer.Util.NotificationUtil
import com.ashunevich.multitimer.Util.PrefUtil

class ExpiredTime : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        NotificationUtil.showTimerExpired(context)
      PrefUtil.setTimerState(TimerFragment.TimerState.Stopped,context)
        PrefUtil.setAlarmSetTime(0,context)
    }
}