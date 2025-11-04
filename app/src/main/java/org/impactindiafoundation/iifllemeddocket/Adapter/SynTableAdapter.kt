package org.impactindiafoundation.iifllemeddocket.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.architecture.model.SyncPrescriptionRecordEntity
import org.impactindiafoundation.iifllemeddocket.databinding.ItemSynTableBinding

class SynTableAdapter(
    private var dataList: List<SyncPrescriptionRecordEntity>
) : RecyclerView.Adapter<SynTableAdapter.SynTableViewHolder>() {

    inner class SynTableViewHolder(val binding: ItemSynTableBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SynTableViewHolder {
        val binding = ItemSynTableBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SynTableViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SynTableViewHolder, position: Int) {
        val item = dataList[position]
        with(holder.binding) {
            tvSrNo.text = (position + 1).toString()
            tvDate.text = item.date
            tvTime.text = item.time
            syncCount.text = item.syncCount.toString()
            unSyncCount.text = item.unsyncCount.toString()
        }
    }

    override fun getItemCount(): Int = dataList.size

    fun updateList(newList: List<SyncPrescriptionRecordEntity>) {
        dataList = newList
        notifyDataSetChanged()
    }
}
