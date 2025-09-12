package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.BloodPressureData
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterBpBinding


class BloodPressureAdapter(
    context: Context,
    bloodPressureDataArrayList: ArrayList<BloodPressureData>
) :RecyclerView.Adapter<BloodPressureAdapter.ViewHolder>() {

    lateinit var context:Context
    var bloodPressureDataArrayList:ArrayList<BloodPressureData>?=null

    init {
        this.context=context
        this.bloodPressureDataArrayList=bloodPressureDataArrayList
    }
    inner class ViewHolder(val binding:AdapterBpBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BloodPressureAdapter.ViewHolder {
        return ViewHolder(
            AdapterBpBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: BloodPressureAdapter.ViewHolder, position: Int) {
        val data=bloodPressureDataArrayList!![position]
        holder.binding.textView1.text=data.systolic
        holder.binding.textView2.text=data.diastolic
        holder.binding.textView3.text=data.bp_interpretation

    }

    override fun getItemCount(): Int {
        return bloodPressureDataArrayList!!.size
    }
}