package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.FollowUpsData

import org.impactindiafoundation.iifllemeddocket.databinding.AdapterFollowUpsBinding

class FollowUpsAdapter(context: Context, arrayList: ArrayList<FollowUpsData>):RecyclerView.Adapter<FollowUpsAdapter.ViewHolder>() {

    lateinit var context:Context
    var arrayList:ArrayList<FollowUpsData>?=null

    init {
        this.context=context
        this.arrayList=arrayList
    }

    inner class ViewHolder(val binding:AdapterFollowUpsBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowUpsAdapter.ViewHolder {
        return ViewHolder(
            AdapterFollowUpsBinding.inflate(
            LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: FollowUpsAdapter.ViewHolder, position: Int) {
       val data= arrayList!![position]
        holder.binding.TextViewComplications.text=data.complication
        holder.binding.TextViewComplicationsDetails.text=data.complication_details
    }

    override fun getItemCount(): Int {
       return arrayList!!.size
    }
}