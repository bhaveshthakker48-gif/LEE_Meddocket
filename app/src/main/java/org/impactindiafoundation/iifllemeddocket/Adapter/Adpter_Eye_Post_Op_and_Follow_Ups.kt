package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Eye_Post_Op_AND_Follow_ups
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.BloodPressureData
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.FollowUpsData
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.TwoValueModel
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterEyePostAndFollowsBinding


class Adpter_Eye_Post_Op_and_Follow_Ups(
    val context: Context,
    val vitalList: MutableList<Eye_Post_Op_AND_Follow_ups>
) :RecyclerView.Adapter<Adpter_Eye_Post_Op_and_Follow_Ups.ViewHolder>() {

    var BloodPressureDataArrayList:ArrayList<BloodPressureData>?=null
    var PulseRateArrayList:ArrayList<TwoValueModel>?=null
    var RespiratoryRateArrayList:ArrayList<TwoValueModel>?=null
    var FollowUpEarly:ArrayList<FollowUpsData>?=null
    var FollowUpFundus:ArrayList<FollowUpsData>?=null
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adpter_Eye_Post_Op_and_Follow_Ups.ViewHolder {
        return ViewHolder(
            AdapterEyePostAndFollowsBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(
        holder: Adpter_Eye_Post_Op_and_Follow_Ups.ViewHolder,
        position: Int
    ) {
       val data=vitalList[position]

        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
      return vitalList.size
    }

    inner class ViewHolder(val binding:AdapterEyePostAndFollowsBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Eye_Post_Op_AND_Follow_ups, position: Int) {

            BloodPressureDataArrayList= ArrayList()
            PulseRateArrayList= ArrayList()
            RespiratoryRateArrayList= ArrayList()
            FollowUpEarly= ArrayList()
            FollowUpFundus= ArrayList()


            binding.editTextMonitorTemperature.text=data.eye_post_op_temp

            binding.editTextMonitorEyePressure.text=data.eye_post_op_pressure_regular
            binding.editTextMonitorHeadPosition.text=data.eye_post_op_head_position
            binding.edittextTabCifloxacin.text=data.eye_post_op_cifloxacin_detail
            binding.EditTextTabDiclofenac.text=data.eye_post_op_diclofenac_detail
            binding.EditTextTabPantaprezol.text=data.eye_post_op_pantaprezol_detail
            binding.EditTextTabDimox.text=data.eye_post_op_dimox_detail
            binding.EditTextEyeDropMoxifloxacin8.text=data.eye_post_op_eye_1_detail
            binding.EditTextEyeDropMoxifloxacin6.text=data.eye_post_op_eye_2_detail
            binding.EditTextEyeDropMoxifloxacin4.text=data.eye_post_op_eye_3_detail
            binding.EditTextEyeDropMoxifloxacin2.text=data.eye_post_op_eye_4_detail
            binding.EditTextEyeDropMoxifloxacin5thweek.text=data.eye_post_op_eye_5_detail
            binding.EditTextEyeDropMoxifloxacinP.text=data.eye_post_op_moxifloxacin_detail
            binding.EditTextEyeDropHomide.text=data.eye_post_op_homide_detail
            binding.EditTextEyeDropTimololSos.text=data.eye_post_op_timolol_detail
            binding.EditTextEyeDropHypersolSos.text=data.eye_post_op_hypersol_detail
            binding.EditTextLubricantDropRefresh.text=data.eye_post_op_lubricant_detail
            binding.EditTextOnTheDayOfDischargeED.text=data.eye_post_op_ed_homide_detail
            binding.EditTextOtherMedication.text=data.eye_post_op_other_detail
            binding.EditTextCounselingAndHealthEducation.text=data.eye_post_op_discharge_check
            binding.EditText2ndFollowUp.text=data.eye_post_op_2nd_date
            binding.EditText3ndFollowUp.text=data.eye_post_op_3rd_date
            binding.EditText4weeksFollowUp.text=data.eye_post_op_last_date
            binding.EditTextAssessTheImmediatePostoperative.text=data.eye_post_op_asses_imedi
            binding.EditTextAdditionalDetailsRightEye.text=data.eye_post_op_w_addi_detail_right
            binding.EditTextAdditionalDetailsLeftEye.text=data.eye_post_op_w_addi_detail_left
            binding.EditTextWithoutPinHoleAdditionalDetailsRightEye.text=data.eye_post_op_addi_detail_right
            binding.EditTextWithoutPinHoleAdditionalDetailsLeftEye.text=data.eye_post_op_addi_detail_left
            binding.EditTextSlitLampExamination.text=data.eye_post_op_slit_lamp_exam
            binding.EditTextFundusExamination.text=data.eye_post_op_fundus_exam
            binding.EditTextAssessTheCataractWound.text=data.eye_post_op_assess_catract_detail
           binding.EditTextTheLocationAndCentrationOfTheIOL.text=data.eye_post_op_location_centration
            binding.EditTextCheckThePupil.text=data.eye_post_op_check_pupil_detail
            binding.EditTextCounseling.text=data.eye_post_op_counseling
            binding.SpinnerWithPinHoleNVRightEye.text=data.eye_post_op_w_near_vision_right
            binding.SpinnerWithPinHoleNVLeftEye.text=data.eye_post_op_w_near_vision_left
            binding.SpinnerWithPinHoleDVRightEyeDetails.text=data.eye_post_op_w_distant_vision_right
            binding.SpinnerWithPinHoleDVLeftEyeDetails.text=data.eye_post_op_w_distant_vision_left
            binding.SpinnerWithPinHolePinHoleRightEyeDetails.text=data.eye_post_op_w_pinhole_improve_right
            binding.SpinnerWithPinHolePinHoleLeftEyeDetails.text=data.eye_post_op_w_pinhole_improve_left
            binding.SpinnerWithoutPinHoleNVRightEye.text=data.eye_post_op_near_vision_right
            binding.SpinnerWithoutPinHoleNVLeftEye.text=data.eye_post_op_near_vision_left
            binding.SpinnerWithoutPinHoleDVRightEyeDetails.text=data.eye_post_op_distant_vision_right
            binding.SpinnerWithoutPinHoleDVLeftEyeDetails.text=data.eye_post_op_distant_vision_left
            binding.SpinnerWithPinHoleDVRightEye.text=data.eye_post_op_w_distant_vision_unit_right
            binding.SpinnerAssessTheCataractWound.text=data.eye_post_op_assess_catract
            binding.SpinnerWithPinHoleDVLeftEye.text=data.eye_post_op_w_distant_vision_unit_left
            binding.SpinnerWithPinHolePinHoleRightEye.text=data.eye_post_op_w_pinhole_right
            binding.SpinnerWithPinHolePinHoleLeftEye.text=data.eye_post_op_w_pinhole_left
            binding.SpinnerWithPinHolePinHoleRightEyeUnit.text=data.eye_post_op_w_pinhole_improve_unit_right
            binding.SpinnerWithoutPinHoleDVRightEye.text=data.eye_post_op_distant_vision_unit_right
            binding.SpinnerWithoutPinHoleDVLeftEye.text=data.eye_post_op_distant_vision_unit_left
            binding.SpinnerCheckPupil.text=data.eye_post_op_check_pupil
            binding.SpinnerWithPinHolePinHoleLeftEyeUnit.text=data.eye_post_op_w_pinhole_improve_unit_left
           binding.EditTextAssessTheImmediatePostoperative.text=data.eye_post_op_asses_imedi
            binding.checkboxTabCifloxacin.isChecked=(data.eye_post_op_cifloxacin=="Tab. Cifloxacin 500 mg BD × 5days")
            binding.checkboxTabDiclofenac.isChecked=(data.eye_post_op_diclofenac=="Tab. Diclofenac sodium 50mg BD x 5 days")
            binding.checkboxTabPantaprezol.isChecked=(data.eye_post_op_pantaprezol=="Tab. Pantaprezol 40 BD x 5 days")
            binding.checkboxTabDimox.isChecked=(data.eye_post_op_dimox=="Tab. Dimox stat and sos")
            binding.checkboxEyeDropMoxifloxacin8.isChecked=(data.eye_post_op_eye_1=="Eye drop Moxifloxacin P 1° per 8 times a day for the 1st week")
            binding.checkboxEyeDropMoxifloxacin6.isChecked=(data.eye_post_op_eye_2=="Eye drop Moxifloxacin P 1° per 6 times a day for the 2nd week")

            binding.checkboxEyeDropMoxifloxacin4.isChecked=(data.eye_post_op_eye_3=="Eye drop Moxifloxacin P 1° per 4 times a day for the 3rd week")

            binding.checkboxEyeDropMoxifloxacin2.isChecked=(data.eye_post_op_eye_4=="Eye drop Moxifloxacin P 1° per 2 times a day for the 4th week")

            binding.checkboxEyeDropMoxifloxacin5thweek.isChecked=(data.eye_post_op_eye_5=="Eye drop Moxifloxacin P 1°per day for the 5th week")

            binding.checkboxEyeDropMoxifloxacinP.isChecked=(data.eye_post_op_moxifloxacin=="Eye drop Moxifloxacin P")

            binding.checkboxEyeDropHomide.isChecked=(data.eye_post_op_homide=="Eye drop Homide")

            binding.checkboxEyeDropTimololSos.isChecked=(data.eye_post_op_timolol=="Eye drop timolol sos")

            binding.checkboxEyeDropHypersolSos.isChecked=(data.eye_post_op_hypersol=="Eye drop hypersol sos")

            binding.checkboxLubricantDropRefresh.isChecked=(data.eye_post_op_lubricant=="Lubricant drop refresh")

            binding.checkboxOnTheDayOfDischarge.isChecked=(data.eye_post_op_ed_homide=="On the day of discharge instillation of the E/D Homide 1°")

            binding.checkboxOtherMedication.isChecked=(data.eye_post_op_other=="Other")


            Log.d(ConstantsApp.TAG,"eye_post_op_bp=>"+data.eye_post_op_bp)
            Log.d(ConstantsApp.TAG,"eye_post_op_pr=>"+data.eye_post_op_pr)
            Log.d(ConstantsApp.TAG,"eye_post_op_rr=>"+data.eye_post_op_rr)
            Log.d(ConstantsApp.TAG,"eye_post_op_early_post_op=>"+data.eye_post_op_early_post_op)
            Log.d(ConstantsApp.TAG,"eye_post_op_fundus_pathology=>"+data.eye_post_op_fundus_pathology)

            val bloodPressureDataList = parseBloodPressureData(data.eye_post_op_bp)

            BloodPressureDataArrayList?.addAll(bloodPressureDataList)



            binding.RecyclerViewBp.adapter =
                BloodPressureAdapter(context, BloodPressureDataArrayList!!)
            val layoutManager = LinearLayoutManager(context)
            layoutManager.orientation = RecyclerView.VERTICAL
            binding.RecyclerViewBp!!.layoutManager = layoutManager
            binding.RecyclerViewBp!!.setHasFixedSize(true)


            val twoValueDataList = parseTwoValueData(data.eye_post_op_pr)

            PulseRateArrayList!!.addAll(twoValueDataList)

            binding.RecyclerViewPulseRate.adapter=TwoValueAdapter(context,PulseRateArrayList)
            val layoutManager3 = LinearLayoutManager(context)
            layoutManager3.orientation = RecyclerView.VERTICAL
            binding.RecyclerViewPulseRate!!.layoutManager = layoutManager3
            binding.RecyclerViewPulseRate!!.setHasFixedSize(true)

            val twoValueDataList1 = parseTwoValueData(data.eye_post_op_rr)

            RespiratoryRateArrayList!!.addAll(twoValueDataList1)

            binding.RecyclerViewRespirationRate.adapter=TwoValueAdapter(context,RespiratoryRateArrayList)

            val layoutManager4 = LinearLayoutManager(context)
            layoutManager4.orientation = RecyclerView.VERTICAL
            binding.RecyclerViewRespirationRate!!.layoutManager = layoutManager4
            binding.RecyclerViewRespirationRate!!.setHasFixedSize(true)


            val twoValueDataList2 = parseFollowUpsData(data.eye_post_op_early_post_op)

            FollowUpEarly!!.addAll(twoValueDataList2)

            binding.RecyclerViewEarlyPostoperative.adapter=FollowUpsAdapter(context,
                FollowUpEarly!!
            )

            val layoutManager5 = LinearLayoutManager(context)
            layoutManager5.orientation = RecyclerView.VERTICAL
            binding.RecyclerViewEarlyPostoperative!!.layoutManager = layoutManager5
            binding.RecyclerViewEarlyPostoperative!!.setHasFixedSize(true)

            val twoValueDataList3 = parseFollowUpsData(data.eye_post_op_fundus_pathology)

            FollowUpFundus!!.addAll(twoValueDataList3)

            binding.RecyclerViewFundusPathology.adapter=FollowUpsAdapter(context,
                FollowUpFundus!!
            )

            val layoutManager6 = LinearLayoutManager(context)
            layoutManager6.orientation = RecyclerView.VERTICAL
            binding.RecyclerViewFundusPathology!!.layoutManager = layoutManager6
            binding.RecyclerViewFundusPathology!!.setHasFixedSize(true)
        }
    }

    fun parseBloodPressureData(inputString: String): List<BloodPressureData> {
        val regex = Regex("(\\d+)\\s*=\\s*(\\d+)\\s*=\\s*(\\w+)")
        val matchResults = regex.findAll(inputString)

        return matchResults.map { matchResult ->
            val (systolic, diastolic, interpretation) = matchResult.destructured
            BloodPressureData(systolic, diastolic, interpretation)
        }.toList()
    }
    fun parseTwoValueData(inputString: String): List<TwoValueModel> {
        val regex = Regex("(\\w+)\\s*=\\s*(\\w+)")
        val matchResults = regex.findAll(inputString)

        return matchResults.map { matchResult ->
            val (text, interpretation) = matchResult.destructured
            TwoValueModel(text, interpretation)
        }.toList()
    }

    fun parseFollowUpsData(inputString: String): List<FollowUpsData> {
        val regex = Regex("(\\w+)\\s*=\\s*(\\w+)")
        val matchResults = regex.findAll(inputString)

        return matchResults.map { matchResult ->
            val (complication, complicationDetails) = matchResult.destructured
            FollowUpsData(complication, complicationDetails)
        }.toList()
    }


}