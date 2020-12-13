package com.ashunevich.multitimer

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ashunevich.multitimer.Util.prefUtil
import com.ashunevich.multitimer.databinding.TimerFragBinding

class TimerFragment : Fragment() {


    enum class TimerState{
        Stopped,Paused,Running
    }

    private var _binding: TimerFragBinding? = null
    private lateinit var timer: CountDownTimer
    private var timerLenghtSeconds = 0L
    private var timerState = TimerState.Stopped
    private var secondsRemain = 0L
    private val binding get() = _binding



    fun newInstance():TimerFragment{
        return TimerFragment()
    }



    override fun onResume() {
        super.onResume()
        initTimer()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = TimerFragBinding.inflate(inflater, container, false)
        setTimerFabBindings()
        return binding?.root
    }

    override fun onPause() {
        super.onPause()

        if (timerState==TimerState.Running){timer.cancel()}
        else if (timerState==TimerState.Paused){}

         prefUtil.setPrevTimerLenghtSec(timerLenghtSeconds, requireContext())
         prefUtil.setSecRemaining(secondsRemain, requireContext())
        prefUtil.setTimerState(timerState, requireContext())
    }

    private fun initTimer(){
        timerState = prefUtil.getTimerState(requireContext())

        if (timerState==TimerState.Stopped)
            setNewTimerLength()
        else
            setPrevioustimerLength()

        secondsRemain = if (timerState ==TimerState.Running||timerState==TimerState.Paused) prefUtil.getSecRemaining(requireContext())
        else
            timerLenghtSeconds

        if (timerState == TimerState.Running)
            startTimer()

        updateButtons()
        updateCountdownUI()
    }

    private fun onTimerFinished(){
        timerState = TimerState.Stopped
        setNewTimerLength()

        binding?.progressCountdown?.progress = 0
        prefUtil.setSecRemaining(timerLenghtSeconds,requireContext())
        secondsRemain=timerLenghtSeconds
        updateButtons()
        updateCountdownUI()
    }

    private fun startTimer(){
        timerState = TimerState.Running
        timer = object : CountDownTimer(secondsRemain*1000,1000){
            override fun onFinish() = onTimerFinished()
            override fun onTick(millisUntilFinished:Long) {
                secondsRemain = millisUntilFinished/1000
                updateCountdownUI()
            }
        }.start()
    }

    private fun setNewTimerLength(){
        val lengthInMinutes = prefUtil.getTimerlength(requireContext())
        timerLenghtSeconds = (lengthInMinutes*60L)
        binding?.progressCountdown?.max = timerLenghtSeconds.toInt()
    }

    private fun setPrevioustimerLength(){
        timerLenghtSeconds = prefUtil.getPrevTimerLenghtSec(requireContext())
        binding?.progressCountdown?.max = timerLenghtSeconds.toInt()
    }

    private fun updateCountdownUI(){
        val minusUntilFinished = secondsRemain/60
        val secondsInMinutesRemaining = secondsRemain - minusUntilFinished*60
        val secondsSrt = secondsInMinutesRemaining.toString()
        binding?.countDown?.text  = "$minusUntilFinished:${
        if (secondsSrt.length==2)secondsSrt else "0"+secondsSrt}"
        binding?.progressCountdown?.progress = (timerLenghtSeconds-secondsRemain).toInt()
    }

    private fun updateButtons(){
        when(timerState){
            TimerState.Running->{
                binding?.startButton?.isEnabled  =false
                binding?.pauseButton?.isEnabled  =true
                binding?.stopButton?.isEnabled  =true
            }
            TimerState.Stopped->{
                binding?.startButton?.isEnabled  =true
                binding?.pauseButton?.isEnabled  =false
                binding?.stopButton?.isEnabled  =false
            }
            TimerState.Paused->{
                binding?.startButton?.isEnabled  =true
                binding?.pauseButton?.isEnabled  =false
                binding?.stopButton?.isEnabled  =true
            }
        }
    }

    private fun setTimerFabBindings (){
        binding?.startButton?.setOnClickListener {
            startTimer()
            timerState= TimerState.Running
        updateButtons()}
        binding?.stopButton?.setOnClickListener {
            timer.cancel()
            timerState= TimerState.Stopped
            updateButtons()}
        binding?.pauseButton?.setOnClickListener {
            timer.cancel()
            onTimerFinished()}
            timerState= TimerState.Paused }
    }


