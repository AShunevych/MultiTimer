package com.ashunevich.multitimer

import android.R
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.replace
import androidx.viewpager2.widget.ViewPager2
import com.ashunevich.multitimer.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    enum class TimerState{
        Stopped,Paused,Running
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init()
    }

    private fun init(){
         binding.viewPagerActivity.adapter = PageAdapter(supportFragmentManager,lifecycle)
        val names:ArrayList<String> = arrayListOf("Timer","Chronometer")
        TabLayoutMediator(binding.tabLayout,binding.viewPagerActivity){
            tab, position ->  tab.text = names[position]
        }.attach()
    }

    

    }


