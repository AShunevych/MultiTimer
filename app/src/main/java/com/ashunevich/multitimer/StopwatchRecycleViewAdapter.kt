package com.ashunevich.multitimer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.ashunevich.multitimer.databinding.RecItemBinding

class StopwatchRecycleViewAdapter (val items : ArrayList<StopwatchPOJO>) : RecyclerView.Adapter<StopwatchRecycleViewAdapter.MyViewHolder>() {
    //Creating an arraylist of POJO objects
    private  var padList: ArrayList<StopwatchPOJO> = ArrayList()


    //This method inflates view present in the RecyclerView
    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemBinding = RecItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(itemBinding)

    }

    //Binding the data using get() method of POJO object
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItems(padList[position])
        val stopwatchPOJO: StopwatchPOJO = padList[position]
        holder.bindItems(stopwatchPOJO)
    }


    override fun getItemCount(): Int {
        return padList.size
    }


    fun setData(serverData: ArrayList<StopwatchPOJO>) {
        padList = serverData
        notifyDataSetChanged()
    }

    //View holder class, where all view components are defined
     inner class MyViewHolder(itemBinding: RecItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        private val binding: RecItemBinding = itemBinding
        fun bindItems(stopwatchPOJO: StopwatchPOJO){
            binding.numberView.text = stopwatchPOJO.number
            binding.totalTime.text =stopwatchPOJO.totalChronoTime
            binding.intervalView.text= stopwatchPOJO.intervalChronoTime
        }

    }

}