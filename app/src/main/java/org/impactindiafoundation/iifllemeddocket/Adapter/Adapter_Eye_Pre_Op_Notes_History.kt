package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterEyePreOpNotesHistoryBinding

class Adapter_Eye_Pre_Op_Notes_History(context: Context, historyOfArrayList: ArrayList<String>):RecyclerView.Adapter<Adapter_Eye_Pre_Op_Notes_History.ViewHolder>() {

    lateinit var context:Context
    var historyOfArrayList: ArrayList<String>?=null

    init {
        this.context=context
        this.historyOfArrayList=historyOfArrayList

    }
    inner class ViewHolder(val binding:AdapterEyePreOpNotesHistoryBinding):RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adapter_Eye_Pre_Op_Notes_History.ViewHolder {
        return ViewHolder(
            AdapterEyePreOpNotesHistoryBinding.inflate(
            LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(
        holder: Adapter_Eye_Pre_Op_Notes_History.ViewHolder,
        position: Int
    ) {
       val data=historyOfArrayList!![position]
        holder.binding.TextViewHistory.text=data
    }

    override fun getItemCount(): Int {
        return historyOfArrayList!!.size
    }
}