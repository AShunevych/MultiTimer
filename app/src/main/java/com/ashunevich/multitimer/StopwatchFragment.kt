package com.ashunevich.multitimer

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ashunevich.multitimer.databinding.StopwatchFragBinding

class StopwatchFragment : Fragment() {

    enum class ChronometerState {
        Stopped, Paused, Running
    }

    private var _binding: StopwatchFragBinding? = null

    private val binding get() = _binding
    private val format: String = "00:%s"
    private var elapse:Int = 0
    private var timerState = ChronometerState.Stopped


    fun newInstance(): StopwatchFragment {
        return StopwatchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = StopwatchFragBinding.inflate(inflater, container, false)
        binding?.totalChronometre?.format =format

        initButton()
        return binding?.root
    }

    fun initButton() {
        binding?.fabStartPause?.setOnClickListener {
            if (timerState == ChronometerState.Stopped) {
                timerState = ChronometerState.Running
                binding?.totalChronometre?.base = SystemClock.elapsedRealtime()
                binding?.totalChronometre?.start()
                //binding?.currentInterval?.start()
                binding?.fabStartPause?.setImageResource(R.drawable.ic_pause)
            }
            else if (timerState ==ChronometerState.Running){
                binding?.totalChronometre?.stop()
               // binding?.currentInterval?.stop()
                timerState = ChronometerState.Paused
                elapse = (SystemClock.elapsedRealtime() - binding?.totalChronometre?.base!!).toInt();
                binding?.fabStartPause?.setImageResource(R.drawable.ic_play)
            }
            else if (timerState ==ChronometerState.Paused){
                timerState = ChronometerState.Running
                binding?.totalChronometre?.base= SystemClock.elapsedRealtime() - elapse
                binding?.totalChronometre?.start()
               //binding?.currentInterval?.start()
                binding?.fabStartPause?.setImageResource(R.drawable.ic_pause)
            }
            _binding?.fabIntervalClear?.setOnClickListener { }
        }
    }

    // TODO -- implement recyclrview with result
    // TODO -- init all functionality
}
