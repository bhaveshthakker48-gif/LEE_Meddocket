package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.databinding.ItemCampPatientListBinding


class PatientReportAdapter(
    val context: Context,
    val data: ArrayList<PatientReport>,
    val event: PatientReportAdapterEvent
) :
    RecyclerView.Adapter<PatientReportAdapter.PatientReportViewHolder>() {

    inner class PatientReportViewHolder(
        private var binding: ItemCampPatientListBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            binding.apply {
                tvPatientDetails.text = "${content.patientFname}"

                tvCampDetails.text = "Camp : ${content.location}(${content.camp_id})"

                tvPatientGender.text = "Gender : ${content.patientGen}"
                tvPatientAge.text = "Age: ${content.patientAge}"
                tvCampNo.text = "Camp no. ${content.camp_id}"


            }


            binding.llPatientSection.setOnClickListener {
                event.onItemClick(position)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PatientReportViewHolder {
        val binding =
            ItemCampPatientListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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