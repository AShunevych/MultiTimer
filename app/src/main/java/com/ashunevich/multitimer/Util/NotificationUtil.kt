package com.ashunevich.multitimer.Util

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.RingtoneManager
import android.net.Uri
import androidx.core.app.NotificationCompat
import com.ashunevich.multitimer.*
import java.text.SimpleDateFormat
import java.util.*

class NotificationUtil {
    companion object{
        private const val CHANNEL_ID_TIMER = "menu_timer"
        private const val CHANNEL_NAME_TIMER = "Timer App Timer "
        private const val TIMER_ID = 0

        @SuppressLint("UnspecifiedImmutableFlag")
        fun showTimerExpired(context: Context){
            val startIntent  = Intent(context,TImerNotificatinReceiver::class.java)
            startIntent.action = AppConstants.ACTION_START
            val startPendingIntent = PendingIntent.getBroadcast(context,0,startIntent,PendingIntent.FLAG_UPDATE_CURRENT)
            val nBuilder = getNotificationBuild(context, CHANNEL_ID_TIMER,true)
            nBuilder.setContentTitle("Timer Expired!")
                .setContentText("Start Again?")
                .setContentIntent(getPendingIntentWithStack(context,MainActivity::class.java))
                .addAction(R.drawable.ic_play,"Start",startPendingIntent)
           val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER,CHANNEL_NAME_TIMER,true)
            nManager.notify(TIMER_ID,nBuilder.build())
        }

        fun showTimerRunning(context: Context,wakeUpTime:Long){
            val stopIntent  = Intent(context,TImerNotificatinReceiver::class.java)
            stopIntent.action = AppConstants.ACTION_STOP
            val stopPendingIntent = PendingIntent.getBroadcast(context,0,stopIntent,PendingIntent.FLAG_UPDATE_CURRENT)

            val df = SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT)

            val pauseIntent  = Intent(context,TImerNotificatinReceiver::class.java)
            pauseIntent.action = AppConstants.ACTION_PAUSE
            val pausePendingIntent = PendingIntent.getBroadcast(context,0,pauseIntent,PendingIntent.FLAG_UPDATE_CURRENT)

            val nBuilder = getNotificationBuild(context, CHANNEL_ID_TIMER,true)

            nBuilder.setContentTitle("Timer is Running")
                .setContentText("End:${df.format(Date(wakeUpTime))}")
                .setContentIntent(getPendingIntentWithStack(context,MainActivity::class.java))
                .setOngoing(true)
                .addAction(R.drawable.ic_stop,"Stop",stopPendingIntent)
                .addAction(R.drawable.ic_pause,"Pause",pausePendingIntent)

            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER,CHANNEL_NAME_TIMER,true)
            nManager.notify(TIMER_ID,nBuilder.build())
        }

        @SuppressLint("UnspecifiedImmutableFlag")
        fun showTimerPaused(context: Context){
            val resumeTimer  = Intent(context,TImerNotificatinReceiver::class.java)
            resumeTimer.action = AppConstants.ACTION_RESUME
            val resumePendingIntent = PendingIntent.getBroadcast(context,0,resumeTimer,PendingIntent.FLAG_UPDATE_CURRENT)
            val nBuilder = getNotificationBuild(context, CHANNEL_ID_TIMER,true)
            nBuilder.setContentTitle("Timer is Paused")
                .setContentText("Resume?")
                .setContentIntent(getPendingIntentWithStack(context,MainActivity::class.java))
                .setOngoing(true)
                .addAction(R.drawable.ic_play,"Resume",resumePendingIntent)
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.createNotificationChannel(CHANNEL_ID_TIMER,CHANNEL_NAME_TIMER,true)
            nManager.notify(TIMER_ID,nBuilder.build())
        }


        private fun getNotificationBuild(context: Context, channelId: String, playSound: Boolean) :NotificationCompat.Builder{
            val notificationSound :Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val nBuilder= NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.drawable.ic_timer)
                .setAutoCancel(true).setDefaults(0)
            if(playSound) nBuilder.setSound(notificationSound)
            return nBuilder
        }

        private fun <T> getPendingIntentWithStack(context: Context,javaClass:Class<T>):PendingIntent{
            val resultIntent = Intent(context,javaClass)
            resultIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            val stackBuilder = TaskStackBuilder.create(context)
            stackBuilder.addParentStack(javaClass)
            stackBuilder.addNextIntent(resultIntent)
            return stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT)
        }

        private fun NotificationManager.createNotificationChannel(channelID:String,
                                                                  channelName:String,
                                                                  playSound: Boolean){
            if(playSound){
                val channerImportance =  NotificationManager.IMPORTANCE_LOW
                val nChannel = NotificationChannel(channelID,channelName,channerImportance)
                nChannel.enableLights(true)
                nChannel.lightColor = Color.GREEN
                this.createNotificationChannel(nChannel)
            }
        }

        fun hideTimerNotification(context: Context){
            val nManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nManager.cancel(TIMER_ID)
        }
    }
}