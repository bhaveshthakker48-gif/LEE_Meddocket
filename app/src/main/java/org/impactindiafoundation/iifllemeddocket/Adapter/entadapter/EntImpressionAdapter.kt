package org.impactindiafoundation.iifllemeddocket.Adapter.entadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntImpressionEntity
import org.impactindiafoundation.iifllemeddocket.databinding.ItemImpressionBinding

class EntImpressionAdapter(
    private var items: List<EntImpressionEntity>,
    private var canEdit: Boolean,
    private val onDeleteClick: (EntImpressionEntity) -> Unit
) : RecyclerView.Adapter<EntImpressionAdapter.ImpressionViewHolder>() {

    inner class ImpressionViewHolder(val binding : ItemImpressionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImpressionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemImpressionBinding.inflate(inflater, parent, false)
        return ImpressionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImpressionViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvPart.text = item.part
        holder.binding.tvSymptom.text = item.impression

        holder.binding.btnDelete.visibility = if (canEdit) View.VISIBLE else View.GONE

        holder.binding.btnDelete.setOnClickListener {
            onDeleteClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateEditState(newEditState: Boolean) {
        this.canEdit = newEditState
        notifyDataSetChanged()
    }
}
