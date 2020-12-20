package com.ashunevich.multitimer

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ashunevich.multitimer.Util.NotificationUtil
import com.ashunevich.multitimer.Util.PrefUtil
import com.ashunevich.multitimer.databinding.TimerFragBinding
import java.util.*

class TimerFragment : Fragment() {

    companion object {
        fun setAlarm(context: Context, nowSeconds: Long, secondsRemaining: Long): Long {
            val wakeMeUp = (nowSeconds + secondsRemaining) * 1000
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ExpiredTime::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeMeUp, pendingIntent)
            PrefUtil.setAlarmSetTime(nowSeconds, context)
            return wakeMeUp
        }

        fun removeAlarm(context: Context) {
            val intent = Intent(context, ExpiredTime::class.java)
            val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
            PrefUtil.setAlarmSetTime(0, context)
        }

        val nowSeconds: Long get() = Calendar.getInstance().timeInMillis / 1000
    }

    enum class TimerState {
        Stopped, Paused, Running
    }

    private var _binding: TimerFragBinding? = null
    private lateinit var timer: CountDownTimer
    private var timerLenghtSeconds = 0L
    private var timerState = TimerState.Stopped
    private var secondsRemain = 0L
    private val binding get() = _binding


    fun newInstance(): TimerFragment {
        return TimerFragment()
    }


    override fun onResume() {
        super.onResume()
        initTimer()
        removeAlarm(requireContext())
        NotificationUtil.hideTimerNotification(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = TimerFragBinding.inflate(inflater, container, false)
        setTimerFabBindings()
        return binding?.root
    }

    override fun onPause() {
        super.onPause()

        if (timerState == TimerState.Running) {
            timer.cancel()
            val wakeUp = setAlarm(requireContext(), nowSeconds, secondsRemain)
            NotificationUtil.showTimerRunning(requireContext(), wakeUp)
        } else if (timerState == TimerState.Paused) {
            NotificationUtil.showTimerPaused(requireContext())
        }

        PrefUtil.setPrevTimerLenghtSec(timerLenghtSeconds, requireContext())
        PrefUtil.setSecRemaining(secondsRemain, requireContext())
        PrefUtil.setTimerState(timerState, requireContext())
    }

    private fun initTimer() {
        timerState = PrefUtil.getTimerState(requireContext())

        if (timerState == TimerState.Stopped)
            setNewTimerLength()
        else
            setPrevioustimerLength()

        secondsRemain =
            if (timerState == TimerState.Running || timerState == TimerState.Paused) PrefUtil.getSecRemaining(
                requireContext()
            )
            else
                timerLenghtSeconds

        val alarmSetTime = PrefUtil.getAlarmSetTime(requireContext())
        if (alarmSetTime > 0) secondsRemain -= nowSeconds - alarmSetTime

        if (secondsRemain <= 0) onTimerFinished()
        if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished() {
        timerState = TimerState.Stopped
        setNewTimerLength()

        binding?.progressCountdown?.progress = 0
        PrefUtil.setSecRemaining(timerLenghtSeconds, requireContext())
        secondsRemain = timerLenghtSeconds
        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer() {
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemain * 1000, 1000) {
            override fun onFinish() = onTimerFinished()
            override fun onTick(millisUntilFinished: Long) {
                secondsRemain = millisUntilFinished / 1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength() {
        val lengthInMinutes = PrefUtil.getTimerlength(requireContext())
        timerLenghtSeconds = (lengthInMinutes * 60L)
        binding?.progressCountdown?.max = timerLenghtSeconds.toInt()
    }

    private fun setPrevioustimerLength() {
        timerLenghtSeconds = PrefUtil.getPrevTimerLenghtSec(requireContext())
        binding?.progressCountdown?.max = timerLenghtSeconds.toInt()
    }

    @SuppressLint("SetTextI18n")
    private fun updateCountdownUI() {
        val minusUntilFinished = secondsRemain / 60
        val secondsInMinutesRemaining = secondsRemain - minusUntilFinished * 60
        val secondsSrt = secondsInMinutesRemaining.toString()
        binding?.countDown?.text = "$minusUntilFinished:${
        if (secondsSrt.length == 2) secondsSrt else "0$secondsSrt"}"
        binding?.progressCountdown?.progress = (timerLenghtSeconds - secondsRemain).toInt()
    }

    private fun updateButtons() {
        when (timerState) {
            TimerState.Running -> {
                binding?.startButton?.isEnabled = false
                binding?.pauseButton?.isEnabled = true
                binding?.stopButton?.isEnabled = true
            }
            TimerState.Stopped -> {
                binding?.startButton?.isEnabled = true
                binding?.pauseButton?.isEnabled = false
                binding?.stopButton?.isEnabled = false
            }
            TimerState.Paused -> {
                binding?.startButton?.isEnabled = true
                binding?.pauseButton?.isEnabled = false
                binding?.stopButton?.isEnabled = true
            }
        }
    }

    private fun setTimerFabBindings() {
        binding?.startButton?.setOnClickListener {
            startTimer()
            timerState = TimerState.Running
            updateButtons()
        }
        binding?.stopButton?.setOnClickListener {
            timer.cancel()
            timerState = TimerState.Stopped
            updateButtons()
            onTimerFinished()
        }
        binding?.pauseButton?.setOnClickListener {
            timer.cancel()
            timerState = TimerState.Paused
            updateButtons()
        }
    }
}

