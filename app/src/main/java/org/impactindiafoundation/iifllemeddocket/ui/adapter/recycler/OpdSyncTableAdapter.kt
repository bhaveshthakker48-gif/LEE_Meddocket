package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.architecture.model.OpdSyncTable
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine
import org.impactindiafoundation.iifllemeddocket.databinding.ItemOpdSyncTableBinding
import org.impactindiafoundation.iifllemeddocket.databinding.ItemPatientMedicineBinding


class OpdSyncTableAdapter(
    val context: Context,
    val data: ArrayList<OpdSyncTable>,
    val totalSyncedCount:Int,
    val totalUnSyncedCount:Int
) : RecyclerView.Adapter<OpdSyncTableAdapter.OpdSyncTableViewHolder>() {

    inner class OpdSyncTableViewHolder(
        private var binding: ItemOpdSyncTableBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            binding.apply {
                if (data.size == position+1){
                    tvSrNo.text = ""
                    tvDateTime.text = "Total Synced Count"
                    tvSyncedCount.text = totalSyncedCount.toString()
                    tvUnSyncedCount.text = ""
                }
                else{
                    tvSrNo.text = "${position+1}"
                    tvDateTime.text = content.dateTime
                    tvSyncedCount.text = content.syncedCount.toString()
                    tvUnSyncedCount.text = content.unsyncFormCount.toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OpdSyncTableViewHolder {
        val binding =
            ItemOpdSyncTableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OpdSyncTableViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: OpdSyncTableViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}