package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.CallBack.OnDeleteClickListener
import org.impactindiafoundation.iifllemeddocket.CallBack.OnEditClickListener

import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterPrescriptionDataBinding

import java.util.ArrayList

class AdapterPrescriptionData(
    context: Context,
    PrescriptionDataArrayList: ArrayList<CreatePrescriptionModel>,
    OnDeleteClickListener:OnDeleteClickListener,
    OnEditClickListener: OnEditClickListener
):RecyclerView.Adapter<AdapterPrescriptionData.ViewHolder>()
{
    lateinit var context: Context
     var PrescriptionDataArrayList: ArrayList<CreatePrescriptionModel>?=null
    private var onDeleteClickListener: OnDeleteClickListener? = null
    private var OnEditClickListener:OnEditClickListener?=null
    init {
        this.PrescriptionDataArrayList=PrescriptionDataArrayList
        this.context=context
        this.onDeleteClickListener=OnDeleteClickListener
        this.OnEditClickListener=OnEditClickListener
    }

    inner class ViewHolder(val binding: AdapterPrescriptionDataBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: CreatePrescriptionModel, position: Int) {

            binding.TextViewGeneric.text=data.generic_name
            binding.TextViewBrand.text=data.brand_name
            binding.TextViewDose.text=data.dose+"\n"+data.dose_frequency
            binding.TextViewDuration.text=data.duration+" "+data.selected_duration
            binding.TextViewQuantity.text=data.quantity.toString()+" "+data.quantity_unit
            binding.TextViewFrequency.text=data.frequency.toString()

            binding.ImageViewDelete.setOnClickListener {
                onDeleteClickListener!!.onDeleteClick(adapterPosition)
            }
            binding.ImageViewEdit.setOnClickListener {
                OnEditClickListener!!.onEditClick(data,adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterPrescriptionDataBinding.inflate(
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