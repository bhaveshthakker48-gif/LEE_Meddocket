package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.databinding.ItemCampPatientListBinding


class VisualAcuityAdapter(
    val context: Context,
    val data: List<VisualAcuity>,
    val event: VisualAcuityAdapterEvent
) :
    RecyclerView.Adapter<VisualAcuityAdapter.VisualAcuityViewHolder>() {

    inner class VisualAcuityViewHolder(
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
    ): VisualAcuityViewHolder {
        val binding =
            ItemCampPatientListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VisualAcuityViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: VisualAcuityViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface VisualAcuityAdapterEvent {
        fun onItemClick(position: Int)
    }

}