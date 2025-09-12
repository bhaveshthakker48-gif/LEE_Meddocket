package org.impactindiafoundation.iifllemeddocket.Adapter



import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterPrescriptionData1Binding


import java.util.ArrayList

class AdapterPrescriptionData1(
    context: Context,
    PrescriptionDataArrayList: ArrayList<CreatePrescriptionModel>

):RecyclerView.Adapter<AdapterPrescriptionData1.ViewHolder>()
{
    lateinit var context: Context
    var PrescriptionDataArrayList: ArrayList<CreatePrescriptionModel>?=null

    init {
        this.PrescriptionDataArrayList=PrescriptionDataArrayList
        this.context=context

    }

    inner class ViewHolder(val binding: AdapterPrescriptionData1Binding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CreatePrescriptionModel, position: Int) {

            binding.TextViewGeneric.text=data.generic_name
            binding.TextViewBrand.text=data.brand_name
            binding.TextViewFrequency.text=data.frequency
            binding.TextViewDose.text=data.dose+"\n"+data.dose_frequency
            binding.TextViewDuration.text=data.duration+" "+data.selected_duration
            binding.TextViewQuantity.text=data.quantity.toString()+" "+data.quantity_unit
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterPrescriptionData1Binding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun getItemCount(): Int {
        return PrescriptionDataArrayList!!.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data= PrescriptionDataArrayList!![position]
        holder.bind(data,position)
    }
}