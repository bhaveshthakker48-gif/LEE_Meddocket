package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.MeasurementPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientForm
import org.impactindiafoundation.iifllemeddocket.databinding.ItemCampPatientListBinding
import org.impactindiafoundation.iifllemeddocket.databinding.ItemOrthosisMeasurementBinding


class CampPatientAdapter(
    val context: Context,
    val data: List<CampPatientDataItem>,
    val event: CampPatientAdapterEvent
) :
    RecyclerView.Adapter<CampPatientAdapter.CampPatientViewHolder>() {

    inner class CampPatientViewHolder(
        private var binding: ItemCampPatientListBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            binding.apply {

                if (content.fromDevice) {
                    llPatientSection.setBackgroundColor(context.getColor(R.color.light_orange))

                } else {
                    llPatientSection.setBackgroundColor(context.getColor(R.color.white))

                }
                tvPatientDetails.text = "${content.patient_name} - ${content.temp_patient_id}"

                tvCampDetails.text = "Camp : ${content.camp_name}(${content.camp_id})"

                tvPatientGender.text = "Gender : ${content.patient_gender}"
                tvPatientAge.text = "Age: ${content.patient_age_years}"
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
    ): CampPatientViewHolder {
        val binding =
            ItemCampPatientListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CampPatientViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: CampPatientViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface CampPatientAdapterEvent {
        fun onItemClick(position: Int)
    }

}