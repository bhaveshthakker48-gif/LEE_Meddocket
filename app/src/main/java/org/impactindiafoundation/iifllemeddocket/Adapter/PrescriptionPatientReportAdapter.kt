package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.architecture.model.PrescriptionPatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.model.entdatabasemodel.EntPatientReport
import org.impactindiafoundation.iifllemeddocket.databinding.ItemEntCampPatientListBinding
import org.impactindiafoundation.iifllemeddocket.databinding.ItemPrescriptionCampPatientListBinding


class PrescriptionPatientReportAdapter(
    val context: Context,
    val data: ArrayList<PrescriptionPatientReport>,
    val event: PatientReportAdapterEvent
) :
    RecyclerView.Adapter<PrescriptionPatientReportAdapter.PatientReportViewHolder>() {

    inner class PatientReportViewHolder(
        private var binding: ItemPrescriptionCampPatientListBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            binding.apply {
                tvPatientDetails.text = "${content.patientFname} ${content.patientLname}"
                tvCampDetails.text = "Camp : ${content.location}(${content.camp_id})"
                tvPatientGender.text = "Gender : ${content.patientGen}"
                tvPatientAge.text = "Age: ${content.patientAge}"
                tvCampNo.text = "Camp no. ${content.camp_id}"

                // 👉 Handle color change based on app_id == "1"
                if (content.isSyn == 1) {
                    llPatientDetailTab.setBackgroundResource(R.color.teal_200)
                    llPatientSection.setBackgroundResource(R.color.light_blue)

                    // Text color
                    tvPatientDetails.setTextColor(context.getColor(R.color.black))
                    tvCampDetails.setTextColor(context.getColor(R.color.black))
                    tvPatientGender.setTextColor(context.getColor(R.color.black))
                    tvPatientAge.setTextColor(context.getColor(R.color.black))
                    tvCampNo.setTextColor(context.getColor(R.color.black))
                } else {
                    // Default colors
                    llPatientDetailTab.setBackgroundResource(R.color.main_color)
                    llPatientSection.setBackgroundResource(R.color.light_orange)

                    tvPatientDetails.setTextColor(context.getColor(R.color.white))
                    tvCampDetails.setTextColor(context.getColor(R.color.black))
                    tvPatientGender.setTextColor(context.getColor(R.color.black))
                    tvPatientAge.setTextColor(context.getColor(R.color.black))
                    tvCampNo.setTextColor(context.getColor(R.color.white))
                }

                llPatientSection.setOnClickListener {
                    event.onItemClick(position)
                }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PatientReportViewHolder {
        val binding =
            ItemPrescriptionCampPatientListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PatientReportViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: PatientReportViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface PatientReportAdapterEvent {
        fun onItemClick(position: Int)
    }

}