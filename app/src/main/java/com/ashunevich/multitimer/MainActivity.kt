package com.ashunevich.multitimer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ashunevich.multitimer.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    enum class TimerState{
        Stopped,Paused,Running
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var timer: CountDownTimer
    private var timerLenghtSeconds: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    fun init(){
         binding.viewPagerActivity.adapter = PageAdapter(supportFragmentManager,lifecycle)
        val names:ArrayList<String> = arrayListOf("Stopwatch","Timer")
        TabLayoutMediator(binding.tabLayout,binding.viewPagerActivity){
            tab, position ->  tab.text = names[position]
        }.attach()
    }


    }

