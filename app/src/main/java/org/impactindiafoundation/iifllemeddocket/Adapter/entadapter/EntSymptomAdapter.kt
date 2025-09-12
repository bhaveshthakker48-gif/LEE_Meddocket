package org.impactindiafoundation.iifllemeddocket.Adapter.entadapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntSymptomsEntity
import org.impactindiafoundation.iifllemeddocket.databinding.ItemSymptomBinding
class EntSymptomAdapter(
    private var items: List<EntSymptomsEntity>,
    private var canEdit: Boolean,
    private val onDeleteClick: (EntSymptomsEntity) -> Unit
) : RecyclerView.Adapter<EntSymptomAdapter.SymptomViewHolder>() {

    inner class SymptomViewHolder(val binding: ItemSymptomBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SymptomViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSymptomBinding.inflate(inflater, parent, false)
        return SymptomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SymptomViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvOrgan.text = item.organ
        holder.binding.tvPart.text = item.part
        holder.binding.tvSymptom.text = item.symptom

        holder.binding.btnDelete.visibility = if (canEdit) View.VISIBLE else View.GONE
        holder.binding.btnDelete.setOnClickListener {
            if (canEdit) onDeleteClick(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateEditState(newEditState: Boolean) {
        this.canEdit = newEditState
        notifyDataSetChanged()
    }
}
