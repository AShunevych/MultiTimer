package com.ashunevich.multitimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ashunevich.multitimer.databinding.StopwatchFragBinding
import com.ashunevich.multitimer.databinding.TimerFragBinding

class StopwatchFragment : Fragment() {

    private var _binding: StopwatchFragBinding? = null

    private val binding get() = _binding

    fun newInstance():StopwatchFragment{
        return StopwatchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = StopwatchFragBinding.inflate(inflater, container, false)
        return binding?.root
    }

}