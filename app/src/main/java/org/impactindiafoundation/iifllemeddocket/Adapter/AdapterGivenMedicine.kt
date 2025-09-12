package org.impactindiafoundation.iifllemeddocket.Adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.Activity.MedicineInsertedActivity
import org.impactindiafoundation.iifllemeddocket.CallBack.OnEditClickListener
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterMedicineReportBinding
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterMedicineReportInsertedBinding

class AdapterGivenMedicine(
    val context: Context,
    val sortedResponse: List<CreatePrescriptionModel>
):RecyclerView.Adapter<AdapterGivenMedicine.ViewHolder>(){
    inner class ViewHolder(val binding: AdapterMedicineReportInsertedBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CreatePrescriptionModel, position: Int) {
            binding.TextViewPatientName.text=data.generic_name
            binding.TextViewPatientId.text=data.quantity+" "+data.quantity_unit
            binding.TextViewDate.text=data.brand_name
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterGivenMedicine.ViewHolder {
        return ViewHolder(
            AdapterMedicineReportInsertedBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: AdapterGivenMedicine.ViewHolder, position: Int) {
        val data=sortedResponse[position]
        holder.bind(data,position)
    }

    override fun getItemCount(): Int {
        return sortedResponse.size
    }
}