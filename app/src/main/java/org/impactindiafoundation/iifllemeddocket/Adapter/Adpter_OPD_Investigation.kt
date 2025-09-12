package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.OPD_Investigations
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterOpdInvestigationBinding

class Adpter_OPD_Investigation(
    val context: Context,
    val opdInvestigationsList: MutableList<OPD_Investigations>
) :RecyclerView.Adapter<Adpter_OPD_Investigation.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adpter_OPD_Investigation.ViewHolder {
        return ViewHolder(
            AdapterOpdInvestigationBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: Adpter_OPD_Investigation.ViewHolder, position: Int) {
       val data=opdInvestigationsList[position]

        holder.bind(data,position)
    }

    override fun getItemCount(): Int {
        return opdInvestigationsList.size
    }

    inner class ViewHolder(val binding:AdapterOpdInvestigationBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: OPD_Investigations, position: Int) {

            Log.d(ConstantsApp.TAG,""+data.has_refused)
            val has_refused=data.has_refused
            when(has_refused)
            {
                true->
                {
                    binding.checkboxRBS.isChecked=true
                }
                false->
                {
                    binding.checkboxRBS.isChecked=false
                }
            }

            binding.edtRBS.text=data.random_blood_sugar
            binding.tvRBS.text=data.rbs_interpretation
            binding.edtHaemoglobin.text=data.haemoglobin.toString()
            binding.tvHae.text=data.haemoglobin_interpretation
            binding.edtO2.text=data.o2_saturation
            binding.tvO2.text=data.o2s_interpretation

        }

    }
}