package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.Activity.PrescriptionDisbributionActivity
import org.impactindiafoundation.iifllemeddocket.Activity.ViewStatusActivity
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientPrescriptionRegistrationCombined
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterAllPatientListBinding
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterCombineDataBinding

class CombinedDataAdapter(
    val context: Context,
    val combinedData: List<PatientPrescriptionRegistrationCombined>
):RecyclerView.Adapter<CombinedDataAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CombinedDataAdapter.ViewHolder {
        return ViewHolder(
            AdapterCombineDataBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: CombinedDataAdapter.ViewHolder, position: Int) {
       val data=combinedData[position]
        holder.bind(data,position)
    }

    override fun getItemCount(): Int {
       return combinedData.size
    }

    inner class ViewHolder(val binding:AdapterCombineDataBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: PatientPrescriptionRegistrationCombined, position: Int) {

            val patientName=data.registrationData.fname+" "+data.registrationData.mname+" "+data.registrationData.lname
            //binding.patientName.text=patientName

            val spectacle=data.SpectacleDisdributionStatusModel
            val registration=data.registrationData
            val precription=data.prescription

            binding.TextViewAadharNo.text=registration.aadharno
            binding.TextViewPatientID.text=registration.patient_id.toString()
            binding.TextViewContactNo.text=registration.mobileno
            binding.TextViewPatientName.text=patientName
            binding.TextViewSpectacleType.text=precription!!.presc_type

            val spectacle_given = spectacle.spectacle_given
            val spectacle_not_matching = spectacle.spectacle_not_matching
            val spectacle_not_received = spectacle.spectacle_not_received
            val patient_call_again = spectacle.patient_call_again
            val patient_not_come = spectacle.patient_not_come


            when {
                !spectacle_given && !spectacle_not_matching && !spectacle_not_received && !patient_call_again && !patient_not_come -> {
                }
                spectacle_given ->
                {
                    val message="Spectacle is already given."
                    binding.TextViewStatus.text=message
                }
                spectacle_not_matching ->
                {
                    val message="Spectacle not matching. Patient called again."
                    binding.TextViewStatus.text=message
                }
                spectacle_not_received ->
                {
                    val message="Spectacle not received. Patient called again."
                    binding.TextViewStatus.text=message
                }
            }
        }

    }
}