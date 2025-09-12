package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.AddSymptomsModel
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterAddEyeOpdDoctorsNoteBinding

class Add_AddSymptomsArrayList_Adapter(
    context: Context,
    AddSymptomsArrayList: ArrayList<AddSymptomsModel>
):RecyclerView.Adapter<Add_AddSymptomsArrayList_Adapter.AddSymptomsArrayListViewHolder>() {

    lateinit var context: Context
    var AddSymptomsArrayList:ArrayList<AddSymptomsModel>?=null

    init {
        this.context=context
        this.AddSymptomsArrayList=AddSymptomsArrayList
    }


    inner class AddSymptomsArrayListViewHolder(val binding:AdapterAddEyeOpdDoctorsNoteBinding):RecyclerView.ViewHolder(binding.root)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AddSymptomsArrayListViewHolder {
        return AddSymptomsArrayListViewHolder(AdapterAddEyeOpdDoctorsNoteBinding.inflate(
            LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(
        holder: Add_AddSymptomsArrayList_Adapter.AddSymptomsArrayListViewHolder,
        position: Int
    ) {
       val data=AddSymptomsArrayList!![position]

        holder.binding.textViewEye.text=data.selected_eye
        holder.binding.textViewEyeOpdNote.text=data.selected_symptoms
        holder.binding.textViewEyeOpdNoteDescription.text=data.selected_eye_symptoms_details
    }

    override fun getItemCount(): Int {
      return AddSymptomsArrayList!!.size
    }
}