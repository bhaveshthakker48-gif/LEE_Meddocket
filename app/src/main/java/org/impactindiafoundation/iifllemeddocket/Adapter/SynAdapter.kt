package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.architecture.model.SyncSummaryEntity
import org.impactindiafoundation.iifllemeddocket.databinding.ItemSynBinding

class SynAdapter(
    private var syncList: List<SyncSummaryEntity>,
    private val context: Context
) : RecyclerView.Adapter<SynAdapter.SynViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SynViewHolder {
        val binding = ItemSynBinding.inflate(LayoutInflater.from(context), parent, false)
        return SynViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SynViewHolder, position: Int) {
        val currentItem = syncList[position]
        holder.bind(currentItem)
    }

    override fun getItemCount() = syncList.size

    fun updateData(newList: List<SyncSummaryEntity>) {
        syncList = newList
        notifyDataSetChanged()
    }

    inner class SynViewHolder(private val binding: ItemSynBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SyncSummaryEntity) {
//            binding.TextViewSyncType.text = item.formType
            binding.TextViewSyncDate.text = item.dateTime
//            binding.TextViewSyncTime.text = "" // You can split date/time if needed
            binding.TextViewTotalSynced.text = "${item.totalSynced}"
            binding.TextViewTotalUnsynced.text = "${item.totalUnsynced}"
        }
    }
}
