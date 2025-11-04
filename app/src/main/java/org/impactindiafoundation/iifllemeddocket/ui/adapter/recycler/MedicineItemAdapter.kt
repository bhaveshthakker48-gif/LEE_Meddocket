package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine
import org.impactindiafoundation.iifllemeddocket.databinding.ItemSingleMedicineBinding

class MedicineItemAdapter(
    val context: Context,
    val data: ArrayList<PatientMedicine.PrescriptionItem>
) : RecyclerView.Adapter<MedicineItemAdapter.MedicineItemViewHolder>() {

    inner class MedicineItemViewHolder(
        private var binding: ItemSingleMedicineBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            binding.apply {
                tvMedicineName.text = content.item_name
                tvMedicineBrand.text = content.brand_name
                tvMedicineCount.text = content.qty.toString()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedicineItemViewHolder {
        val binding =
            ItemSingleMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MedicineItemViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: MedicineItemViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }



}