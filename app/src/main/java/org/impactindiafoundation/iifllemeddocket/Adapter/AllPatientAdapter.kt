package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.Activity.PatientListActivity

import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterAllPatientListBinding


class AllPatientAdapter(
    val context: Context,
    val AllPatientList: ArrayList<PatientDataLocal>,
    val cardViewClick: PatientListActivity
):RecyclerView.Adapter<AllPatientAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllPatientAdapter.ViewHolder {
        return ViewHolder(
            AdapterAllPatientListBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: AllPatientAdapter.ViewHolder, position: Int) {
       val data=AllPatientList[position]
        holder.bind(data,position)
    }

    override fun getItemCount(): Int {
       return AllPatientList.size
    }

    inner class ViewHolder(val binding:AdapterAllPatientListBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: PatientDataLocal, position: Int) {
            binding.TextViewPatientID.text= "Patient ID  :-"+data.patientId.toString()
            binding.TextViewPatientAge.text= "Age(Yrs) :-"+data.patientAge.toString()
            binding.TextViewPatientName.text= "Name :-"+data.patientFname.toString()+" "+data.patientLname
            binding.TextViewPatientGender.text= "Gender :-"+data.patientGen.toString()
            binding.TextViewPatientVillage.text= "Village :-"

            binding.CardViewPatientList.setOnClickListener(object :View.OnClickListener
            {
                override fun onClick(v: View?) {
                   cardViewClick.onClick(data,position, v!!)
                }
            })
        }

    }
}