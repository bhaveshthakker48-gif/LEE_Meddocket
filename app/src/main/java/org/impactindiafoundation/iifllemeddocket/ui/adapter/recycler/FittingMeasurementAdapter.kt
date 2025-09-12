package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.architecture.model.MeasurementPatientData
import org.impactindiafoundation.iifllemeddocket.databinding.ItemFittingMeasurementBinding
import org.impactindiafoundation.iifllemeddocket.databinding.ItemOrthosisMeasurementBinding


class FittingMeasurementAdapter(
    val context: Context,
    val data: List<MeasurementPatientData>,
    val measurementEvent:MeasurementAdapterEvent
) :
    RecyclerView.Adapter<FittingMeasurementAdapter.FittingMeasurementViewHolder>() {

    inner class FittingMeasurementViewHolder(
        private var binding: ItemFittingMeasurementBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            binding.apply {

                tvMeasurementSrNo.text = "${position+1}"
                tvMeasurementName.text = "${content.orthosisMeasurement.fieldName}"
                if (content.unit.isNullOrEmpty()){
                    tvMeasurementValue.text = "${content.value} (in)"
                }
                else{
                    tvMeasurementValue.text = "${content.value} (${content.unit})"
                }
//                binding.etlOrthosisMeasurement.hint = content.orthosisMeasurement.fieldName
//
//                binding.etOrthosisMeasurement.doOnTextChanged { text, start, before, count ->
//                    if (!text.isNullOrEmpty()){
//                        measurementEvent.onGetData(position)
//                    }
//                }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FittingMeasurementViewHolder {
        val binding =
            ItemFittingMeasurementBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FittingMeasurementViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: FittingMeasurementViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface MeasurementAdapterEvent{
        fun onGetData(position: Int)
    }

}