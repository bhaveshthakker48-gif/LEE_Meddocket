package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.databinding.ItemCampPatientListBinding



class VitalsAdapter(
    val context: Context,
    val data: List<Vitals>,
    val event: VitalsAdapterEvent
) :
    RecyclerView.Adapter<VitalsAdapter.VitalsViewHolder>() {

    inner class VitalsViewHolder(
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
    ): VitalsViewHolder {
        val binding =
            ItemCampPatientListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VitalsViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: VitalsViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface VitalsAdapterEvent {
        fun onItemClick(position: Int)
    }

}