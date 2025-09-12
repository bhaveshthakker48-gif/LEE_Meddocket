package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_OPD_Doctors_Note
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.AddSymptomsModel
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.ExtractDescription
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.ExtractedData
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterEyeOpdDoctorNoteBinding


class Adpter_EyeOPDDoctorNote(
    val context: Context,
    val eyeOPDDoctorNoteList: MutableList<Eye_OPD_Doctors_Note>
) :RecyclerView.Adapter<Adpter_EyeOPDDoctorNote.ViewHolder>() {

    var AddSymptomsArrayList:ArrayList<AddSymptomsModel>?=null
    var AddExaminationArrayList:ArrayList<AddSymptomsModel>?=null
    var AddDiagnosisArrayList:ArrayList<AddSymptomsModel>?=null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adpter_EyeOPDDoctorNote.ViewHolder {
        return ViewHolder(
            AdapterEyeOpdDoctorNoteBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: Adpter_EyeOPDDoctorNote.ViewHolder, position: Int) {
        val data=eyeOPDDoctorNoteList[position]

        holder.bind(data,position)
    }

    override fun getItemCount(): Int {
      return eyeOPDDoctorNoteList.size
    }

    inner class ViewHolder(val binding:AdapterEyeOpdDoctorNoteBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: Eye_OPD_Doctors_Note, position: Int) {

            binding.EditTextNotes.text=data.opd_eye_notes

            Log.d(ConstantsApp.TAG,"opd_eye_recommended=>"+data.opd_eye_recommended)

            if (data.opd_eye_recommended.equals("Surgery", ignoreCase = true)) {
                binding.radioButtonSurgery.isChecked = true
            } else {
                binding.radioButtonMedication.isChecked = true
            }



            AddSymptomsArrayList= ArrayList()
            AddExaminationArrayList= ArrayList()
            AddDiagnosisArrayList= ArrayList()

            val extractedDataList = extractDataFromString(data.opd_eye_symptoms)
            val extractedDataList1 = extractDataFromString1(data.opd_eye_symptoms_description)

            val extractedDataList2 = extractDataFromString(data.opd_eye_examination)
            val extractedDataList3 = extractDataFromString1(data.opd_eye_examination_description)

            val extractedDataList4 = extractDataFromString(data.opd_eye_diagnosis)
            val extractedDataList5 = extractDataFromString1(data.opd_eye_diagnosis_description)


            for (extractedData in extractedDataList) {
                val model = AddSymptomsModel()
                model.selected_eye = extractedData.selected_eye
                model.selected_symptoms = extractedData.selected_symptoms
                AddSymptomsArrayList?.add(model)

                Log.d(ConstantsApp.TAG,"extractedData=>"+extractedData.selected_eye)
                Log.d(ConstantsApp.TAG,"extractedData=>"+extractedData.selected_symptoms)
            }

            for (extractedData in extractedDataList2) {
                val model = AddSymptomsModel()
                model.selected_eye = extractedData.selected_eye
                model.selected_symptoms = extractedData.selected_symptoms
                AddExaminationArrayList?.add(model)

                Log.d(ConstantsApp.TAG,"extractedData=>"+extractedData.selected_eye)
                Log.d(ConstantsApp.TAG,"extractedData=>"+extractedData.selected_symptoms)
            }

            for (extractedData in extractedDataList4) {
                val model = AddSymptomsModel()
                model.selected_eye = extractedData.selected_eye
                model.selected_symptoms = extractedData.selected_symptoms
                AddDiagnosisArrayList?.add(model)

                Log.d(ConstantsApp.TAG,"extractedData=>"+extractedData.selected_eye)
                Log.d(ConstantsApp.TAG,"extractedData=>"+extractedData.selected_symptoms)
            }

            for (extractedData in extractedDataList1) {
                println("Selected Symptoms: ${extractedData.selected_symptoms}")
                println("Selected Eye: ${extractedData.selected_eye_symptoms_details}")

                Log.d(ConstantsApp.TAG,"extractedData=>"+extractedData.selected_eye_symptoms_details)
                Log.d(ConstantsApp.TAG,"extractedData=>"+extractedData.selected_symptoms)
            }



            for (i in extractedDataList1.indices) {
                if (i < AddSymptomsArrayList!!.size) {
                    AddSymptomsArrayList!![i].selected_eye_symptoms_details = extractedDataList1[i].selected_eye_symptoms_details
                } else {
                    // Handle the case where the sizes of both lists are different
                }
            }

            for (i in extractedDataList3.indices) {
                if (i < AddExaminationArrayList!!.size) {
                    AddExaminationArrayList!![i].selected_eye_symptoms_details = extractedDataList3[i].selected_eye_symptoms_details
                } else {
                    // Handle the case where the sizes of both lists are different
                }
            }

            for (i in extractedDataList5.indices) {
                if (i < AddDiagnosisArrayList!!.size) {
                    AddDiagnosisArrayList!![i].selected_eye_symptoms_details = extractedDataList5[i].selected_eye_symptoms_details
                } else {
                    // Handle the case where the sizes of both lists are different
                }
            }


            for (model in AddSymptomsArrayList!!) {
                println("Selected Eye: ${model.selected_eye}")
                println("Selected Symptoms: ${model.selected_symptoms}")
                println("Selected Eye Symptoms Details: ${model.selected_eye_symptoms_details}")
            }


            if (AddSymptomsArrayList!!.size!=0)
            {
                binding.LinearLayoutSymtoms.visibility=View.VISIBLE
                binding.TextViewSymtoms.visibility=View.GONE
                binding.RecyclerViewSymptoms.adapter =
                    Add_AddSymptomsArrayList_Adapter(context, AddSymptomsArrayList!!)
                val layoutManager= LinearLayoutManager(context)
                layoutManager.orientation= RecyclerView.VERTICAL
                binding.RecyclerViewSymptoms!!.layoutManager=layoutManager
                binding.RecyclerViewSymptoms!!.setHasFixedSize(true)

            }
            else
            {
                binding.LinearLayoutSymtoms.visibility=View.GONE
                binding.TextViewSymtoms.visibility=View.VISIBLE
            }


            if (AddExaminationArrayList!!.size!=0)
            {

                binding.LinearLayoutExamination.visibility=View.VISIBLE
                binding.TextViewExamination.visibility=View.GONE
                binding.RecyclerViewExamination.adapter =
                    Add_AddSymptomsArrayList_Adapter(context, AddExaminationArrayList!!)
                val layoutManager1= LinearLayoutManager(context)
                layoutManager1.orientation= RecyclerView.VERTICAL
                binding.RecyclerViewExamination!!.layoutManager=layoutManager1
                binding.RecyclerViewExamination!!.setHasFixedSize(true)
            }
            else
            {
                binding.LinearLayoutExamination.visibility=View.GONE
                binding.TextViewExamination.visibility=View.VISIBLE
            }



            if (AddDiagnosisArrayList!!.size!=0)
            {
                binding.LinearLayoutDiagnosis.visibility=View.VISIBLE
                binding.TextViewDiagnosis.visibility=View.GONE
                binding.RecyclerViewDiagnosis.adapter =
                    Add_AddSymptomsArrayList_Adapter(context, AddDiagnosisArrayList!!)
                val layoutManager2= LinearLayoutManager(context)
                layoutManager2.orientation= RecyclerView.VERTICAL
                binding.RecyclerViewDiagnosis!!.layoutManager=layoutManager2
                binding.RecyclerViewDiagnosis!!.setHasFixedSize(true)
            }
            else
            {
                binding.LinearLayoutDiagnosis.visibility=View.GONE
                binding.TextViewDiagnosis.visibility=View.VISIBLE
            }
        }
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
