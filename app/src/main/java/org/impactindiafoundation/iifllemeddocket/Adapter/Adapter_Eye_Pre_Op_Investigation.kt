package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Investigation
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.AddSymptomsModel
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.ExtractDescription
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.ExtractedData
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterEyePreOpInvestigationBinding

class Adapter_Eye_Pre_Op_Investigation(
    val context: Context,
    val eyePreOpInvestiogation: MutableList<Eye_Pre_Op_Investigation>
) :RecyclerView.Adapter<Adapter_Eye_Pre_Op_Investigation.ViewHolder>() {

    var AddSliteLampArrayList:ArrayList<AddSymptomsModel>?=null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adapter_Eye_Pre_Op_Investigation.ViewHolder {
        return ViewHolder(
            AdapterEyePreOpInvestigationBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(
        holder: Adapter_Eye_Pre_Op_Investigation.ViewHolder,
        position: Int
    ) {
       val data=eyePreOpInvestiogation[position]
        holder.bind(data,position)
    }

    override fun getItemCount(): Int {
        return eyePreOpInvestiogation.size
    }

    inner class ViewHolder(val binding:AdapterEyePreOpInvestigationBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: Eye_Pre_Op_Investigation, position: Int) {

            AddSliteLampArrayList= ArrayList()


            binding.EditTextBPDiastolic.text=data.opd_eye_blood_pressure_diastolic
            binding.EditTextBPSystolic.text=data.opd_eye_blood_pressure_systolic
            binding.TextViewBPInterpretation.text=data.opd_eye_blood_pressure_interpretation

           binding.EditTextBSF.text= data.opd_eye_blood_sugar_fasting
            binding.TextViewSugarInterpretation.text=data.opd_eye_blood_sugar_interpretation
            binding.EditTextPP.text=data.opd_eye_blood_sugar_pp

            binding.EditTextHaemoglobin.text=data.opd_eye_haemoglobin
            binding.textViewHBInterpretation.text=data.opd_eye_haemoglobin_interpretation

            binding.EditTextCBC.text=data.opd_eye_cbc
            binding.EditTextProthrombinTime.text=data.opd_eye_pt
           binding.EditTextBleedingTime.text= data.opd_eye_bt

            binding.SpinnerHIV.text=data.opd_eye_hiv
            binding.SpinnerHBsAg.text=data.opd_eye_hbsag
            binding.SpinnerHCV.text=data.opd_eye_hcv
            binding.EditTextECG.text=data.opd_eye_ecg
            binding.EditTextIOPLeftEye.text=data.opd_eye_iop_left
            binding.EditTextIOPRightEye.text=data.opd_eye_iop_right

            binding.EditTextHorizontalAxisRightEye.text=data.opd_eye_ha_left
           binding.SpinnerHorizontalAxisRightEye.text= data.opd_eye_ha_left_unit
            binding.EditTextHorizontalAxisLeftEye.text=data.opd_eye_ha_right
            binding.SpinnerHorizontalAxisLeftEye.text=data.opd_eye_ha_right_unit

            binding.EditTextVerticalAxisLeftEye.text=data.opd_eye_va_left
            binding.SpinnerVerticalAxisLeftEye.text=data.opd_eye_va_left_unit
            binding.EditTextVerticalAxisRightEye.text=data.opd_eye_va_right
            binding.SpinnerVerticalAxisRightEye.text=data.opd_eye_va_right_unit

            binding.EditTextAverageValueLeftEye.text=data.opd_eye_av_left
            binding.SpinnerAverageValueLeftEye.text=data.opd_eye_av_left_unit
            binding.EditTextAverageValueRightEye.text=data.opd_eye_av_right
            binding.SpinnerAverageValueRightEye.text=data.opd_eye_av_right_unit


            //
            binding.EditTextAScan1ValueLeftEye.text=data.opd_eye_fa_left
            binding.EditTextAScan1ValueRightEye.text=data.opd_eye_fa_right

            binding.EditTextAScan2ValueLeftEye.text=data.opd_eye_sv_left
            binding.EditTextAScan2ValueRightEye.text=data.opd_eye_sv_right

            binding.EditTextAScan3ValueLeftEye.text=data.opd_eye_tv_left
            binding.EditTextAScan3ValueRightEye.text=data.opd_eye_tv_right

            binding.EditTextAScanMedianValueLeftEye.text=data.opd_eye_mv_left
            binding.EditTextAScanMedianValueRightEye.text=data.opd_eye_mv_right
            binding.spinnerIOLPower.text=data.opd_eye_iol_power
            binding.EditTextClottingTime.text=data.opd_eye_ct

            val opd_eye_slit_location=data.opd_eye_slit_location
            val opd_eye_slit_location_description=data.opd_eye_slit_location_description

            val extractedDataList = extractDataFromString(opd_eye_slit_location)
            val extractedDataList1 = extractDataFromString1(opd_eye_slit_location_description)

            for (extractedData in extractedDataList) {
                val model = AddSymptomsModel()
                model.selected_eye = extractedData.selected_eye
                model.selected_symptoms = extractedData.selected_symptoms
                AddSliteLampArrayList?.add(model)

                Log.d(ConstantsApp.TAG,"extractedData=>"+extractedData.selected_eye)
                Log.d(ConstantsApp.TAG,"extractedData=>"+extractedData.selected_symptoms)
            }

            for (i in extractedDataList1.indices) {
                if (i < AddSliteLampArrayList!!.size) {
                    AddSliteLampArrayList!![i].selected_eye_symptoms_details = extractedDataList1[i].selected_eye_symptoms_details
                } else {
                    // Handle the case where the sizes of both lists are different
                }
            }
            binding.RecyclerViewSliteLamp.adapter =
                Add_AddSymptomsArrayList_Adapter(context, AddSliteLampArrayList!!)
            val layoutManager= LinearLayoutManager(context)
            layoutManager.orientation= RecyclerView.VERTICAL
            binding.RecyclerViewSliteLamp!!.layoutManager=layoutManager
            binding.RecyclerViewSliteLamp!!.setHasFixedSize(true)

        }

        private fun extractDataFromString1(opdEyeSymptomsDescription: String):  List<ExtractDescription>  {
            val regex = Regex("\\b(\\w+)\\s*=\\s*([^,}]+)\\b")
            val matchResults = regex.findAll(opdEyeSymptomsDescription)

            return matchResults.map { matchResult ->
                val (selectedSymptoms, symptomsDescription) = matchResult.destructured
                ExtractDescription(selectedSymptoms, symptomsDescription.trim())
            }.toList()

        }

        fun extractDataFromString(inputString: String): List<ExtractedData> {
            val regex = Regex("\\b(\\w+)\\s*=\\s*(\\w+)\\b")
            val matchResults = regex.findAll(inputString)

            return matchResults.map { matchResult ->
                val (selectedSymptoms, selectedEye) = matchResult.destructured
                ExtractedData(selectedSymptoms, selectedEye)
            }.toList()
        }



    }
}