package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.VisualAcuity
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp

import org.impactindiafoundation.iifllemeddocket.databinding.AdapterVisualAcuityBinding

class Adpter_Visual_Acuity(val context: Context, val visualAcuityList: MutableList<VisualAcuity>) :RecyclerView.Adapter<Adpter_Visual_Acuity.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adpter_Visual_Acuity.ViewHolder {
        return ViewHolder(
            AdapterVisualAcuityBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: Adpter_Visual_Acuity.ViewHolder, position: Int) {
       val data=visualAcuityList[position]
        holder.bind(data,position)
    }

    override fun getItemCount(): Int {
       return visualAcuityList.size
    }

    inner class ViewHolder(val binding:AdapterVisualAcuityBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: VisualAcuity, position: Int) {

            Log.d(ConstantsApp.TAG,"data=>"+data.va_distant_vision_left)

            binding.spnNVRight.text=data.va_near_vision_right
            binding.spnNVLeft.text=data.va_near_vision_left
            binding.spnDVUnitRight.text=data.va_distant_vision_unit_right
            binding.spnDVRight.text=data.va_distant_vision_right
            binding.spnDVUnitLeft.text=data.va_distant_vision_unit_left
            binding.spnDVLeft.text=data.va_distant_vision_left

            binding.spnPinRightValueHoleRight.text=data.va_pinhole_right
            binding.spnPinRightUnitHoleRight.text=data.va_pinhole_improve_unit_right
            binding.spnPinHoleRight.text=data.va_pinhole_improve_right

            binding.spnPinLeftValueHoleLeft.text=data.va_pinhole_left
            binding.spnPinLeftUnitHoleLeft.text=data.va_pinhole_improve_unit_left
            binding.spnPinHoleLeft.text=data.va_pinhole_improve_left

            binding.edtAddRight.text=data.va_addi_details_right

            binding.edtAddLeft.text=data.va_addi_details_left

        }
    }
}