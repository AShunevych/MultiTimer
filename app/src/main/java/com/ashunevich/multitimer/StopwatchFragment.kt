package com.ashunevich.multitimer

import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ashunevich.multitimer.databinding.StopwatchFragBinding
import kotlin.collections.ArrayList

class StopwatchFragment : Fragment() {

    enum class ChronometerState {
        Stopped, Paused, Running
    }

    private var _binding: StopwatchFragBinding? = null
    private var adapter: StopwatchRecycleViewAdapter? = null
    private val binding get() = _binding
    private val format: String = "00:%s"
    private val listContentArr: ArrayList<StopwatchPOJO> = ArrayList()
    private var elapse:Int = 0
    private var elapseInterwal:Int = 0
    private var timerState = ChronometerState.Stopped


    fun newInstance(): StopwatchFragment {
        return StopwatchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        _binding = StopwatchFragBinding.inflate(inflater, container, false)
        binding?.totalChronometre?.format =format
        binding?.currentInterval?.visibility =View.GONE
        setVisibility(true)
        initButton()
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = StopwatchRecycleViewAdapter(listContentArr)
        binding?.intervalView?.layoutManager = LinearLayoutManager(activity)
        binding?.intervalView?.setHasFixedSize(true)
        binding?.intervalView?.layoutManager = LinearLayoutManager(activity)

                binding?.intervalView?.adapter = adapter
    }

// (TODO) refactor this code

    private fun initButton() {
        binding?.fabStartPause?.setOnClickListener {
            if (timerState == ChronometerState.Stopped) {
                timerState = ChronometerState.Running
                binding?.currentInterval?.visibility =View.VISIBLE
                binding?.totalChronometre?.base = SystemClock.elapsedRealtime()
                binding?.currentInterval?.base = SystemClock.elapsedRealtime()
                binding?.totalChronometre?.start()
                binding?.currentInterval?.start()
                binding?.fabStartPause?.setImageResource(R.drawable.ic_pause)
                binding?.fabIntervalClear?.setImageResource(R.drawable.ic_interval)
                setVisibility(false)
            } else if (timerState == ChronometerState.Running) {
                binding?.totalChronometre?.stop()
                binding?.currentInterval?.stop()
                timerState = ChronometerState.Paused
                elapse = (SystemClock.elapsedRealtime() - binding?.totalChronometre?.base!!).toInt()
                elapseInterwal = (SystemClock.elapsedRealtime() - binding?.currentInterval?.base!!).toInt()
                binding?.fabStartPause?.setImageResource(R.drawable.ic_play)
                binding?.fabIntervalClear?.setImageResource(R.drawable.ic_clear)
            } else if (timerState == ChronometerState.Paused) {
                timerState = ChronometerState.Running
                binding?.totalChronometre?.base = SystemClock.elapsedRealtime() - elapse
                binding?.currentInterval?.base = SystemClock.elapsedRealtime() - elapseInterwal
                binding?.totalChronometre?.start()
                binding?.currentInterval?.start()
                binding?.fabStartPause?.setImageResource(R.drawable.ic_pause)
                binding?.fabIntervalClear?.setImageResource(R.drawable.ic_interval)
            }
        }
            _binding?.fabIntervalClear?.setOnClickListener {
                if(timerState == ChronometerState.Running){
                    addNewItem(StopwatchPOJO())
                }
                else if (timerState == ChronometerState.Paused){
                    clearRecyclerView ()
                }

            }
        }

    private fun clearRecyclerView (){
        listContentArr.clear()
        adapter?.notifyDataSetChanged()
        binding?.totalChronometre?.base = SystemClock.elapsedRealtime()
        binding?.currentInterval?.base = SystemClock.elapsedRealtime()
        binding?.totalChronometre?.stop()
        binding?.currentInterval?.stop()
        binding?.currentInterval?.visibility = View.GONE
        binding?.fabIntervalClear?.visibility= View.GONE
        timerState = ChronometerState.Stopped
    }

     private fun addNewItem (item: StopwatchPOJO) {
         val totalElapsedTime : Long =  SystemClock.elapsedRealtime() - binding?.totalChronometre?.base!!
         val totalElapsedInterval : Long =  SystemClock.elapsedRealtime() - binding?.currentInterval?.base!!
         binding?.currentInterval?.stop()
         binding?.currentInterval?.base = SystemClock.elapsedRealtime()
         binding?.currentInterval?.start()
         item.number = (listContentArr.size + 1).toString()
         item.totalChronoTime = formatString(totalElapsedTime)
         item.intervalChronoTime = formatString(totalElapsedInterval)
            listContentArr.add(item)
         binding?.intervalView?.smoothScrollToPosition(listContentArr.size-1)
         adapter?.setData(listContentArr)
         adapter?.notifyDataSetChanged()
    }

    private fun formatString (elapsedTime:Long):String{
        val millis = elapsedTime % 1000
        val second = (elapsedTime / 1000) % 60
         val minute : Long = (elapsedTime / (1000 * 60)) % 60
        val hour : Long = (elapsedTime / (1000 * 60 * 60)) % 24
        return String.format("%02d:%02d:%02d.%d", hour, minute, second, millis)
    }




    private fun setVisibility (boolean: Boolean){
      if(boolean){
          binding?.fabIntervalClear?.visibility = View.GONE
          binding?.fabIntervalClear?.isClickable = false
        }
        else{
          binding?.fabIntervalClear?.visibility = View.VISIBLE
          binding?.fabIntervalClear?.isClickable = true
      }
    }
    
}

