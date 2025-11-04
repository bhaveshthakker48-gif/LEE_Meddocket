package org.impactindiafoundation.iifllemeddocket.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.SynTable
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisSynTable

class OrthosisSynTableAdapter(private var dataList: List<OrthosisSynTable>) :
    RecyclerView.Adapter<OrthosisSynTableAdapter.SynTableViewHolder>() {

    class SynTableViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val srNo: TextView = itemView.findViewById(R.id.tv_sr_no)
        val date: TextView = itemView.findViewById(R.id.tv_date)
        val time: TextView = itemView.findViewById(R.id.tv_time)
        val sucess: TextView = itemView.findViewById(R.id.sucess)
        val fail: TextView = itemView.findViewById(R.id.fail)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SynTableViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_orthosis_syn_table, parent, false)
        return SynTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: SynTableViewHolder, position: Int) {
        val item = dataList[position]
        holder.srNo.text = item.syn_type
        holder.date.text = item.date
        holder.time.text = item.time
        holder.sucess.text = item.syncItemCount.toString()
        holder.fail.text = item.notSyncItemCount.toString()
    }

    override fun getItemCount(): Int = dataList.size

    fun updateList(newList: List<OrthosisSynTable>) {
        dataList = newList
        notifyDataSetChanged()
    }
}
