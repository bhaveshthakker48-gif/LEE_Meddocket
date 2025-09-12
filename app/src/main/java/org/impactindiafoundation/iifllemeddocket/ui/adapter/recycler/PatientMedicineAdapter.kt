package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.FinalPrescriptionDrug
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientMedicine
import org.impactindiafoundation.iifllemeddocket.databinding.ItemCampPatientListBinding
import org.impactindiafoundation.iifllemeddocket.databinding.ItemPatientMedicineBinding


class PatientMedicineAdapter(
    val context: Context,
    val data: ArrayList<PatientMedicine>,
    val event: PatientMedicineAdapterEvent
) : RecyclerView.Adapter<PatientMedicineAdapter.PatientMedicineViewHolder>() {

    inner class PatientMedicineViewHolder(
        private var binding: ItemPatientMedicineBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            binding.apply {
                tvSrNo.text = "${position+1}"
                tvDateTime.text = content.createdDate
                tvPatientId.text = content.patient_temp_id
                tvDrugsCount.text = content.prescriptionItems.size.toString()
                tvPatientName.text = content.patient_name
            }

            binding.ivEditPatient.setOnClickListener {
                event.onItemClick(position,true)
            }

            binding.ivViewPatient.setOnClickListener {
                event.onItemClick(position,false)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PatientMedicineViewHolder {
        val binding =
            ItemPatientMedicineBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientMedicineViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: PatientMedicineViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface PatientMedicineAdapterEvent {
        fun onItemClick(position: Int,isEdit:Boolean)
    }

}