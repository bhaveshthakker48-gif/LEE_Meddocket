package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Pre_Op_Notes
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterEyePreOpNotesBinding

class Adapter_Pre_Op_Notes(
    val context: Context,
    val eyePreOpNotesList: MutableList<Eye_Pre_Op_Notes>
) : RecyclerView.Adapter<Adapter_Pre_Op_Notes.ViewHolder>() {

    var HistoryOfArrayList1: ArrayList<String>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adapter_Pre_Op_Notes.ViewHolder {
        return ViewHolder(
            AdapterEyePreOpNotesBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: Adapter_Pre_Op_Notes.ViewHolder, position: Int) {
        val data = eyePreOpNotesList[position]
        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
        return eyePreOpNotesList.size
    }

    inner class ViewHolder(val binding: AdapterEyePreOpNotesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Eye_Pre_Op_Notes, position: Int) {

            HistoryOfArrayList1 = ArrayList()
            val historyValues = data.eye_pre_op_historyof.split(",")
            HistoryOfArrayList1!!.addAll(historyValues)

            binding.RecyclerViewHistoryOf.adapter =
                Adapter_Eye_Pre_Op_Notes_History(context, HistoryOfArrayList1!!)
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = RecyclerView.VERTICAL
            binding.RecyclerViewHistoryOf!!.layoutManager = layoutManager
            binding.RecyclerViewHistoryOf!!.setHasFixedSize(true)

            Log.d(ConstantsApp.TAG, "eye_pre_op_recommendation=>" + data.eye_pre_op_recommendation)
            val eye_pre_op_recommendation = data.eye_pre_op_recommendation

            when (eye_pre_op_recommendation) {
                "Line up for surgery" -> {
                    binding.radioButonLineUpForSurgery.isChecked = true
                }
                "Unfit for surgery. Come after few days" -> {
                    binding.radioButonUnfitForSurgery.isChecked = true
                }
                "Can not operate" -> {
                    binding.radioButonCanNotOperate.isChecked = true
                }
            }

            val eye_pre_op_antibiotic = data.eye_pre_op_antibiotic
            Log.d(ConstantsApp.TAG, "eye_pre_op_antibiotic=>" + eye_pre_op_antibiotic)

            when (eye_pre_op_antibiotic) {
                "Antibiotic" -> {
                    binding.CheckBoxAlleryTestAntibiotic.isChecked = true
                }
                else -> {
                    binding.CheckBoxAlleryTestAntibiotic.isChecked = false
                }
            }

            binding.CheckBoxCheckBoxAlleryTestXylocaine.isChecked = (data.eye_pre_op_xylocaine == "Xylocaine")
            binding.CheckBoxIdentifyCorrectEye.isChecked = (data.eye_pre_op_identify_eye == "Identify correct eye for surgery and mark it")
            binding.CheckBoxWashTheFace.isChecked = (data.eye_pre_op_wash_face == "Wash the face thoroughly before surgery")
            binding.CheckBoxNilByMouth4Hours.isChecked = (data.eye_pre_op_nil_mouth == "Nil by mouth - 4 hours")
            binding.CheckBoxHeadBath.isChecked = (data.eye_pre_op_head_bath == "Head bath")
            binding.CheckBoxAntihypertensiveMedication.isChecked = (data.eye_pre_op_antihyp == "Antihypertensive Medication taken")
            binding.CheckBoxHeartMedicationTaken.isChecked = (data.eye_pre_op_heart == "Heart Medication taken")
            binding.CheckBoxDiabetesMedicationTaken.isChecked = (data.eye_pre_op_dia == "Diabetes Medication taken")
            binding.CheckBoxAnyOtherInstruction.isChecked = (data.eye_pre_op_other == "Any other instruction")
            binding.CheckBoxDiamox.isChecked = (data.eye_pre_op_diamox == "Diamox (250mg)")
            binding.CheckBoxAlprax.isChecked = (data.eye_pre_op_alprax == "Alprax (0.25mg)")
            binding.CheckBoxCiplox.isChecked = (data.eye_pre_op_ciplox == "Ciplox (500)")
            binding.CheckBoxTropicacylPlusEyeDrop.isChecked = (data.eye_pre_op_tropical_drop == "Tropicacyl plus eye drop")
            binding.CheckBoxPlainTropicacylIfMyopia.isChecked = (data.eye_pre_op_plain_tropical == "Plain Tropicacyl if Myopia")
            binding.CheckBoxCiploxAntibioticDrops.isChecked = (data.eye_pre_op_ciplox_drop == "Ciplox antibiotic drops")
            binding.CheckBoxFlurEyeDrop.isChecked = (data.eye_pre_op_flur_eye == "Flur eye drop")
            binding.CheckBoxAmlodipine.isChecked = (data.eye_pre_op_amlodipine == "Amlodipine")
            binding.CheckBoxDiscussedWith.isChecked = (data.eye_pre_op_discussed_with.isNotEmpty())
            binding.spinnerBloodSugarFasting.text = data.eye_pre_op_bs_f
            binding.EditTextDateOfAdmission.text = data.eye_pre_op_admission_date
            binding.EditTextSymptoms.text = data.eye_pre_op_symptoms
            binding.EditTextTemperature.text = data.eye_pre_op_temp
            binding.EditTextHeartRate.text = data.eye_pre_op_heart_rate
            binding.EditTextSystolic.text = data.eye_pre_op_bp_systolic
            binding.EditTextDiastolic.text = data.eye_pre_op_bp_diastolic
            binding.TextViewBPInterpretation.text = data.eye_pre_op_bp_interpretation
            binding.EditTextO2Saturation.text = data.eye_pre_op_o2_saturation
            binding.TextViewO2SaturationInterpretation.text = data.eye_pre_op_o2_saturation_interpretation
            binding.EditTextDiscussedWith.text = data.eye_pre_op_discussed_with_detail
            binding.EditTextNotes.text = data.eye_pre_op_notes
            binding.EditTextAntihypertensiveMedication.text = data.eye_pre_op_antihyp_detail
            binding.EditTextHeartMedicationTaken.text = data.eye_pre_op_heart_detail
            binding.EditTextDiabetesMedicationTaken.text = data.eye_pre_op_dia_detail
            binding.EditTextAnyOtherInstruction.text = data.eye_pre_op_other_detail
            binding.EditTextAntiBioticOther.text = data.eye_pre_op_antibiotic_other
            binding.EditTextXylocaineOther.text = data.eye_pre_op_xylocaine_other
            binding.spinnerBloodSugarFasting.text = data.eye_pre_op_bs_f
            binding.spinnerHaemoglobin.text = data.eye_pre_op_haemoglobin
            binding.spinnerBloodSugarPp.text = data.eye_pre_op_bs_pp
            binding.spinnerProthrombinTime.text = data.eye_pre_op_pt
            binding.spinnerCbc.text = data.eye_pre_op_cbc
            binding.spinnerBleedingTime.text = data.eye_pre_op_bt
            binding.spinnerHiv.text = data.eye_pre_op_hiv
            binding.spinnerClottingTime.text = data.eye_pre_op_ct
            binding.spinnerHbsag.text = data.eye_pre_op_hbsag
            binding.spinnerHcv.text = data.eye_pre_op_hcv
            binding.SpinnerECG.text = data.eye_pre_op_ecg
            binding.SpinnerXylocaine.text = data.eye_pre_op_xylocaine_detail
            binding.SpinnerXylocaineTestResult.text = data.eye_pre_op_xylocaine_result
            binding.Spinner2DropsOfTropicamide.text = data.eye_pre_op_tropicamide
            binding.Spinner2DropsOfBetadine.text = data.eye_pre_op_betadine
            binding.spinnerIOLPower.text = data.eye_pre_op_iol_power
            binding.spinnerTemperatureUnit.text = data.eye_pre_op_temp_unit
            binding.SpinnerAnitiBioticTestResult.text = data.eye_pre_op_antibiotic_result
            binding.SpinnerCheckedSkinAfter20Minutes.text = data.eye_pre_op_antibiotic_detail
            binding.SpinnerDiscussedWith.text = data.eye_pre_op_discussed_with
            binding.SpinnerDiscussedWith.text = data.eye_pre_op_discussed_with
        }
    }
}