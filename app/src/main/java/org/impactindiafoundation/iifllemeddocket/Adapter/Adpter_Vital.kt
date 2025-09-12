package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals

import org.impactindiafoundation.iifllemeddocket.databinding.AdpterVitalFormBinding

class Adpter_Vital(val context: Context, val vitalList: List<Vitals>) :RecyclerView.Adapter<Adpter_Vital.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Adpter_Vital.ViewHolder {
        return ViewHolder(
            AdpterVitalFormBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: Adpter_Vital.ViewHolder, position: Int) {
        val data=vitalList[position]

        holder.bind(data,position)
    }

    override fun getItemCount(): Int {
        return vitalList.size
    }

    inner class ViewHolder(val binding:AdpterVitalFormBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: Vitals, position: Int) {

            binding.edtSys.text=data.systolic
            binding.edtDia.text=data.diastolic
            binding.tvInterpretationBP.text=data.bpInterpretation
            binding.edtHeight.text=data.height
            binding.spnHeight.text=data.heightUnit
            binding.edtWeight.text=data.weight
            binding.spnWeight.text=data.weightUnit
            binding.edtBMI.text=data.bmi
            binding.tvInterpretationBMI.text=data.bmiInterpretation
            binding.edtPr.text=data.pulseRate.toString()
            binding.tvInterpretationPR.text=data.prInterpretation

        }

    }
}