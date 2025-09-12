package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterRefractiveErrorBinding

class Adpter_RefractiveError(
    val context: Context,
    val Refractive_ErrorList: MutableList<RefractiveError>
) :RecyclerView.Adapter<Adpter_RefractiveError.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adpter_RefractiveError.ViewHolder {
        return ViewHolder(
            AdapterRefractiveErrorBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: Adpter_RefractiveError.ViewHolder, position: Int) {
       val data=Refractive_ErrorList[position]
        holder.bind(data,position)
    }

    override fun getItemCount(): Int {
        return Refractive_ErrorList.size
    }

    inner class ViewHolder(val binding:AdapterRefractiveErrorBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: RefractiveError, position: Int) {
            binding.edittextNotes.text=data.fundus_notes
            Log.d(ConstantsApp.TAG,"data. is_given=>"+data. is_given)
            Log.d(ConstantsApp.TAG,"data. is_given=>"+data. is_ordered)

            when(data.is_given)
            {
                true->
                {
                    binding.checkBoxReading!!.isChecked=true
                    binding.checkBoxOrdered.visibility=View.GONE
                    binding.nearVisionSpectacleSpinner!!.text=data.reading_glass
                    binding.nearDistanceVisionSpectacleSpinner.visibility=View.GONE
                }
                false->
                {
                    binding.checkBoxReading.visibility=View.GONE
                    binding.checkBoxOrdered!!.isChecked=true
                    binding.nearVisionSpectacleSpinner!!.visibility=View.GONE
                    binding.nearDistanceVisionSpectacleSpinner.text=data.presc_type
                }
            }

            binding.bilateralVertexDistanceSpinner.text=data.re_bvd
           binding.leftEyeDVAxisSpinner.text=data.re_distant_vision_axis_left
            binding.rightEyeDVAxisSpinner.text=data.re_distant_vision_axis_right
            binding.leftEyeDVCylinderSpinner.text=data.re_distant_vision_cylinder_left
            binding.rightEyeDVCylinderSpinner.text=data.re_distant_vision_cylinder_right

            binding.leftEyeDVVisualAcuityDetailsSpinner.text=data.re_distant_vision_left
            binding.rightEyeDVVisualAcuityDetailsSpinner.text=data.re_distant_vision_right

            binding.leftEyeDVSphereSpinner.text=data.re_distant_vision_sphere_left
            binding.rightEyeDVSphereSpinner.text=data.re_distant_vision_sphere_right

            binding.leftEyeDVVisualAcuitySpinner.text=data.re_distant_vision_unit_left
            binding.rightEyeDVVisualAcuitySpinner.text=data.re_distant_vision_unit_right

            binding.leftEyeNVAxisSpinner.text=data.re_near_vision_axis_left
            binding.rightEyeNVAxisSpinner.text=data.re_near_vision_axis_right
            binding.leftEyeNVCylinderSpinner.text=data.re_near_vision_cylinder_left

            binding.rightEyeNVCylinderSpinner.text=data.re_near_vision_cylinder_right
            binding.leftNearVisualAcuitySpinner.text=data.re_near_vision_left
            binding.rightNearVisualAcuitySpinner.text=data.re_near_vision_right
            binding.leftEyeNVSphereSpinner.text=data.re_near_vision_sphere_left
            binding.rightEyeNVSphereSpinner.text=data.re_near_vision_sphere_right
           binding.leftEyePrismSpinner.text=data.re_prism_left
            binding.rightEyePrismSpinner.text=data.re_prism_right
            binding.leftEyePrismBaseSpinner.text=data.re_prism_unit_left
            binding.rightEyePrismBaseSpinner.text=data.re_prism_unit_right
            binding.pupillaryDistanceSpinner.text=data.re_pupipllary_distance
            binding.leftEyeNVReadingAdditionSpinner.text=data.re_reading_addition_left
            binding.leftEyeNVReadingAdditionDetailsSpinner.text=data.re_reading_addition_left_details
            binding.rightEyeNVReadingAdditionSpinner.text=data.re_reading_addition_right
            binding.rightEyeNVReadingAdditionDetailsSpinner.text=data.re_reading_addition_right_details
            binding.editTextLeftPrism.text=data.re_remark_left.toString()
            binding.editTextRightPrism.text=data.re_remark_right
            binding.editTextRefractiveErrorRemarks.text=data.re_remarks

        }
    }
}