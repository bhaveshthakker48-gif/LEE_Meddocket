package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.CallBack.OnEditClickListener
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterMedicineReportInsertedBinding

class AdapterMedicineReportInserted(
    val context: Context,
    val sortedResponse: List<CreatePrescriptionModel>,
    val OnEditClickListener: OnEditClickListener
):RecyclerView.Adapter<AdapterMedicineReportInserted.ViewHolder>(){

    inner class ViewHolder(val binding: AdapterMedicineReportInsertedBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CreatePrescriptionModel, position: Int) {
            binding.TextViewPatientName.text=data.patient_name
            binding.TextViewPatientId.text=data.patient_id
            binding.TextViewDate.setOnClickListener {
                OnEditClickListener.onEditClick(data,position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterMedicineReportInserted.ViewHolder {
        return ViewHolder(
            AdapterMedicineReportInsertedBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: AdapterMedicineReportInserted.ViewHolder, position: Int) {
        val data=sortedResponse[position]
        holder.bind(data,position)
    }

    override fun getItemCount(): Int {
       return sortedResponse.size
    }
}