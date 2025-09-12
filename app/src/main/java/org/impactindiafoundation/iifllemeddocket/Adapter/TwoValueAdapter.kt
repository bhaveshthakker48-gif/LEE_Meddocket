package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.TwoValueModel
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterTwoValueBinding

class TwoValueAdapter(context: Context, arrayList: ArrayList<TwoValueModel>?):RecyclerView.Adapter<TwoValueAdapter.ViewHolder>() {

    var context:Context?=null
    var arrayList: ArrayList<TwoValueModel>?=null

    init {
        this.context=context
        this.arrayList=arrayList
    }


    inner class ViewHolder(val binding: AdapterTwoValueBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TwoValueAdapter.ViewHolder {
        return ViewHolder(
            AdapterTwoValueBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: TwoValueAdapter.ViewHolder, position: Int) {
       val data=arrayList!![position]

        holder.binding.TextView1.text=data.text
        holder.binding.TextView2.text=data.interpretation
    }

    override fun getItemCount(): Int {
        return arrayList!!.size
    }
}