package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.Activity.AnalyticActivity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable

import org.impactindiafoundation.iifllemeddocket.databinding.ItemSynBinding

class SynAdapter(private var synList: List<SynTable>, val context: Context) : RecyclerView.Adapter<SynAdapter.SynViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SynViewHolder {
        return SynViewHolder(
            ItemSynBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: SynViewHolder, position: Int) {
        val currentItem = synList[position]
        holder.bind(currentItem,position)
    }

    override fun getItemCount() = synList.size
    inner class SynViewHolder(val binding:ItemSynBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(currentItem: SynTable, position: Int) {
            if (currentItem.isSyn==1)
            {
                binding.TextViewSyncDate.text=currentItem.date
                binding.TextViewSyncTime.text=currentItem.time
                binding.TextViewSyncType.text=currentItem.syn_type
            }
        }
    }
}
