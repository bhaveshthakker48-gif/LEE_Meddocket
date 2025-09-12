package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.databinding.ItemCampPatientListBinding


class OpdAdapter(
    val context: Context,
    val data: ArrayList<OPD_Investigations>,
    val event: OpdAdapterEvent
) :
    RecyclerView.Adapter<OpdAdapter.OpdViewHolder>() {

    inner class OpdViewHolder(
        private var binding: ItemCampPatientListBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            binding.apply {

            }

            binding.llPatientSection.setOnClickListener {
                event.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OpdViewHolder {
        val binding =
            ItemCampPatientListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OpdViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: OpdViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OpdAdapterEvent {
        fun onItemClick(position: Int)
    }

}