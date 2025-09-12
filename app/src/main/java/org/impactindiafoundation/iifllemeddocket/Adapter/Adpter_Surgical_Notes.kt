package org.impactindiafoundation.iifllemeddocket.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Cataract_Surgery_Notes
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterSurgicalNotesBinding

class Adpter_Surgical_Notes(
   val context: Context,
    val CataractSurgeryList: MutableList<Cataract_Surgery_Notes>
) :RecyclerView.Adapter<Adpter_Surgical_Notes.ViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adpter_Surgical_Notes.ViewHolder {
        return ViewHolder(
            AdapterSurgicalNotesBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onBindViewHolder(holder: Adpter_Surgical_Notes.ViewHolder, position: Int) {
      val data=CataractSurgeryList!![position]

        holder.bind(data, position)
    }

    override fun getItemCount(): Int {
       return CataractSurgeryList.size
    }

    inner class ViewHolder(val binding:AdapterSurgicalNotesBinding):RecyclerView.ViewHolder(binding.root)
    {
        fun bind(data: Cataract_Surgery_Notes, position: Int) {
            binding.spinnerDifficultAirwayAspirationRisk.text=data.sn_airway
            binding.EditTextDifficultAirwayAspirationRisk.text =data.sn_airway_detail
            binding.edittextAnaesthetistReviews.text= data.sn_anaesthetist_concern
            binding.spinnerHistoryAnticoagulants.text=data.sn_anticoagulant
            binding.spinnerInstrumentSponge.text=data.sn_before_or_instrument
            data.sn_before_or_key_detail=" "
            binding.spinnerSpecimenLabelled.text=data.sn_before_or_specimen
            data.sn_before_or_weather_detail=" "
            binding.editTextCapsulotomy.text =data.sn_cataract_capsulotomy_detail

            binding.editTextCastroviejo.text =data.sn_cataract_castroviejo_detail

           binding.editTextColibri.text = data.sn_cataract_colibri_detail

            binding.editTextFormedIrrigatingCystotomes.text =data.sn_cataract_formed_detail

            binding.editTextHydrodissectiirs.text =data.sn_cataract_hydrodissectiirs_detail

            binding.editTextIrrigation.text =data.sn_cataract_irrigation_detail

           binding.editTextKeretome3.text = data.sn_cataract_keretome_detail

           binding.editTextKeretome2.text = data.sn_cataract_keretome_phaco_detail

            binding.editTextKnife.text =data.sn_cataract_knife_detail

            binding.editTextLieberman.text =data.sn_cataract_lieberman_detail

            binding.editTextLimb.text =data.sn_cataract_limb_detail

            binding.editTextMacPhersonForcep.text =data.sn_cataract_mac_detail

           binding.editTextNucleus.text = data.sn_cataract_nucleus_detail

            binding.editTextSinsky.text =data.sn_cataract_sinsky_detail

            binding.editTextUniversalStSidePort.text =data.sn_cataract_universal_detail

            binding.editTextViscoelasticCannula.text =data.sn_cataract_viscoelastic_detail

            binding.editTextDislocation.text =data.sn_common_dislocation_detail

           binding.edittextEndophthalmitis.text = data.sn_common_endophthalmitis_detail

            binding.editTextEndothelialDecompermation.text =data.sn_common_endothelial_detail

            binding.editTextFluidCollection.text =data.sn_common_fluid_detail

            binding.editTextHyphema.text =data.sn_common_hyphema_detail

           binding.editIextLightSensitivity.text = data.sn_common_light_detail

            binding.editTextMacularOdema.text =data.sn_common_macular_detail

            binding.edittextOcularHypertension.text =data.sn_common_ocular_detail

            binding.editTextPosteriorCapsularOpacification.text =data.sn_common_posterior_opacification_detail

            binding.editTextPosterior.text =data.sn_common_posterior_rent_detail

            binding.editTextRentinalTear.text =data.sn_common_retinal_detail

           binding.editTextVitreousDechatments.text = data.sn_common_vitreous_detail

            binding.edittextDateOfSurgery.text =data.sn_date_of_surgery
            binding.spinnerHistoryFlomax.text=data.sn_flomax
            binding.spinnerAllergies.text=data.sn_has_confirmed_allergies
           binding.spinnerConsent.text= data.sn_has_confirmed_consent
            binding.spinnerIdentity.text=data.sn_has_confirmed_identity
            binding.spinnerProcedure.text=data.sn_has_confirmed_procedure
            binding.spinnerSiteMarked.text=data.sn_has_confirmed_site
            binding.SpinnerCornea.text=data.sn_incision_cornea
            binding.SpinnerSclera1.text=data.sn_incision_sclera_1
            binding.SpinnerSclera2.text=data.sn_incision_sclera_2

            binding.edittextInjAdrenaline.text =data.sn_intra_adrenaline_detail

            binding.editTextCombinationOfInjGentamycin.text =data.sn_intra_combination_detail

            binding.editTextInjGentamycin.text =data.sn_intra_gentamycin_detail

            binding.editTextIntasol500.text =data.sn_intra_intasol_detail

            binding.editTextInjMannitol.text =data.sn_intra_mannitol_detail

            binding.editTextInjMoxifloxacin.text =data.sn_intra_moxifloxacin_detail

           /* val resultsn_intra_occular_lens=sn_intra_occular_lensArrayList.joinToString(",")
            Log.d(ConstantsApp.TAG,"resultsn_intra_occular_lens=>"+resultsn_intra_occular_lens)
            data.sn_intra_occular_lens=resultsn_intra_occular_lens*/

            binding.editTextPrednisolone.text =data.sn_intra_prednisolone_detail

            binding.edittextVigamox.text =data.sn_intra_vigamox_detail

            binding.editTextVisco.text =data.sn_intra_visco_detail
            data.sn_local_anaesthesia=" "
            binding.editTextAge.text =data.sn_nurse_age
            data.sn_nurse_age_unit=" "
            binding.editTextAnaesthesiaGiven.text =data.sn_nurse_anaesthesia
            binding.editTextAnaesthetist.text =data.sn_nurse_anaesthetist
            binding.editTextNRDiastolic.text =data.sn_nurse_bp_diastolic
            binding.TextViewNRBPInterpretation.text=data.sn_nurse_bp_interpretation



           binding.editTextNRSystolic.text=data.sn_nurse_bp_sistolic
            binding.edittextConcerns.text =data.sn_nurse_concern
           binding.editTextDiagnosis.text =data.sn_nurse_diagnosis
            binding.editTextDurationSurgery.text =data.sn_nurse_duration
            binding.edittextEquipmentIssues.text =data.sn_nurse_equipment_issue
            binding.editTextImplantDetails.text =data.sn_nurse_implant_detail
            binding.editTextSurgeryName.text =data.sn_nurse_name
            /*val resultString = selectedSurgenAndNurseOrallyConfirmList.joinToString(",")

            Log.d(ConstantsApp.TAG,"resultString=>"+resultString)*/


           // data.sn_nurse_orally_confirm=resultString
            binding.EditTextRandomBloodSugar.text =data.sn_nurse_rbs
            binding.TextViewNRRBSInterpretation.text =data.sn_nurse_rbs_interpretation
            binding.editTextRegistered.text =data.sn_nurse_registered
            binding.editTextScrubNurse.text =data.sn_nurse_scrub
            binding.editTextSerialNo.text =data.sn_nurse_serial
            binding.spinnerGender.text=data.sn_nurse_sex
            binding.editTextBiopsySite.text =data.sn_nurse_specimen_biopsy
            binding.editTextBiopsySiteDetails.text =data.sn_nurse_specimen_detail
            binding.edittextSterility.text =data.sn_nurse_sterility
            binding.editTextSurgeon.text =data.sn_nurse_surgeon
            binding.editTextSurgeryName.text =data.sn_nurse_surgery_name
            binding.editTextViralSerology.text =data.sn_nurse_viral_serology

            binding.edittextTabCifloxacin.text =data.sn_post_cifloxacin_detail

            binding.edittextTabDiclofenacSodium.text =data.sn_post_diclofenac_detail

            binding.editTextTabDimox.text =data.sn_post_dimox_detail

            binding.edittextEyeDropMoxifloxacin.text =data.sn_post_eye_1_detail

            binding.edittextEyeDropMoxifloxacin6.text =data.sn_post_eye_2_detail

            binding.edittextEyeDropMoxifloxacin4.text =data.sn_post_eye_3_detail

            binding.edittextEyeDropMoxifloxacin2times.text =data.sn_post_eye_4_detail

            binding.edittextEyeDropMoxifloxacin5w.text =data.sn_post_eye_5_detail

            binding.edittextEyeDropHomide.text =data.sn_post_eye_homide_detail

            binding.editTextEyeDropHypersolSos.text =data.sn_post_eye_hypersol_detail

            binding.editTextLubricantDropRefresh.text =data.sn_post_eye_lubricant_detail

            binding.edittextEyeDropMoxifloxacinP.text =data.sn_post_eye_moxifloxacin_detail

            binding.edittextEyeDropTimololSos.text =data.sn_post_eye_timolol_detail

            binding.editTextTabPantaprezol.text =data.sn_post_pantaprezol_detail
            binding.spinnerHistoryPhysical.text=data.sn_site_marked_history
            binding.spinnerPreAnaesthesiaAssessment.text=data.sn_site_marked_pre_anaesthesia
            binding.spinnerPreSurgicalAssessment.text=data.sn_site_marked_pre_surgical
           /* data.sn_type_of_surgery=sn_type_of_surgery_list.joinToString(",")
            Log.d(ConstantsApp.TAG,"sn_type_of_surgery=>"+sn_type_of_surgery)*/
            binding.editTextTypesOfSurgeryOther.text =data.sn_type_of_surgery_other
            binding.spinnerCriticalOrUnexpectedSteps.text=data.sn_unexpected_step
            binding.EditTextCriticalOrUnexpected.text =data.sn_unexpected_step_detail

            binding.checkboxLieberman.isChecked=(data.sn_cataract_lieberman=="Lieberman adjustable wire speculum")
            binding.checkboxCastroviejo.isChecked=(data.sn_cataract_castroviejo=="Castroviejo caliper suture forceps")
            binding.checkboxColibri.isChecked=(data.sn_cataract_colibri=="Colibri forceps")

            binding.checkboxCapsulotomy.isChecked=(data.sn_cataract_capsulotomy=="Capsulotomy forceps")
            binding.checkboxLimb.isChecked=(data.sn_cataract_limb=="Limb's forcep")
            binding.checkboxFormedIrrigatingCystotomes.isChecked=(data.sn_cataract_formed=="Formed irrigating cystotomes")

            binding.checkboxHydrodissectiirs.isChecked=(data.sn_cataract_hydrodissectiirs=="Hydrodissectiirs")

            binding.checkboxMacPhersonForcep.isChecked=(data.sn_cataract_mac=="Mac.Pherson forcep")

            binding.checkboxViscoelasticCannula.isChecked=(data.sn_cataract_viscoelastic=="Viscoelastic Cannula")

            binding.checkboxUniversalStSidePort.isChecked=(data.sn_cataract_universal=="Universal st.side-port blade 15°")

            binding.checkboxIrrigation.isChecked=(data.sn_cataract_irrigation=="Irrigation / Aspirations handpieces")

            binding.checkboxSinsky.isChecked=(data.sn_cataract_sinsky=="Sinsky's forceps (IOL Dialer)")

            binding.checkboxKeretome3.isChecked=(data.sn_cataract_keretome=="Keretome blade-3.2")

            binding.checkboxKeretome2.isChecked=(data.sn_cataract_keretome_phaco=="Keretome Blade-2.85 (In phaco)")

            binding.checkboxKnife.isChecked=(data.sn_cataract_knife=="Knife for micro incision coaxial surgery (MICS) - cresant blade")

            binding.checkboxNucleus.isChecked=(data.sn_cataract_nucleus=="Nucleus removing forecps")

            binding.checkboxPosterior.isChecked=(data.sn_common_posterior_rent=="Posterior capsular rent")

            binding.checkboxLightSensitivity.isChecked=(data.sn_common_light=="Light sensitivity")

            binding.checkboxFluidCollection.isChecked=(data.sn_common_fluid=="Fluid collection in the central retina")

            binding.checkboxPosteriorCapsularOpacification.isChecked=(data.sn_common_posterior_opacification=="Posterior capsular opacification")

            binding.checkboxEndothelialDecompermation.isChecked=(data.sn_common_endothelial=="Endothelial decompermation")

            binding.checkBoxHyphema.isChecked=(data.sn_common_hyphema=="Hyphema")

            binding.checkboxRentinalTear.isChecked=(data.sn_common_retinal=="Retinal tear")

            binding.checkboxVitreousDechatments.isChecked=(data.sn_common_vitreous=="Vitreous dechatments")

            binding.checkboxDislocation.isChecked=(data.sn_common_dislocation=="Dislocation of the IOL")

            binding.checkboxOcularHypertension.isChecked=(data.sn_common_ocular=="Ocular Hypertension")

            binding.checkboxEndophthalmitis.isChecked=(data.sn_common_endophthalmitis=="Endophthalmitis")

            binding.checkboxVigamox.isChecked=(data.sn_intra_vigamox=="Vigamox eye drop")

            binding.checkboxPrednisolone.isChecked=(data.sn_intra_prednisolone=="Prednisolone eye drop")

            binding.checkboxCombinationOfInjGentamycin.isChecked=(data.sn_intra_combination=="Combination of inj gentamycin + inj dexa 2-3 drops")

            binding.checkboxVisco.isChecked=(data.sn_intra_visco=="Visco lubricant gel")

            binding.checkboxIntasol500.isChecked=(data.sn_intra_intasol=="Intasol 500 irrigation fluid")

            binding.checkboxInjMannitol.isChecked=(data.sn_intra_mannitol=="Inj. Mannitol 100ml TDS sos")

            binding.checkboxInjGentamycin.isChecked=(data.sn_intra_gentamycin=="Inj.Gentamycin 80 mg sos")

            binding.checkboxInjMoxifloxacin.isChecked=(data.sn_intra_moxifloxacin=="Inj Moxifloxacin 400 mg sis")

            binding.checkboxInjAdrenaline.isChecked=(data.sn_intra_adrenaline=="Inj Adrenaline 1mg")

            binding.checkboxTabCifloxacin.isChecked=(data.sn_post_cifloxacin=="Tab. Cifloxacin 500 mg BD x 5days")
            binding.checkboxTabDiclofenacSodium.isChecked=(data.sn_post_diclofenac=="Tab. Diclofenac sodium 50mg BD × 5 days")

            binding.checkboxTabPantaprezol.isChecked=(data.sn_post_pantaprezol=="Tab. Pantaprezol 40 BD x 5 days")

            binding.checkboxTabDimox.isChecked=(data.sn_post_dimox=="Tab. Dimox stat and sos")

            binding.checkboxEyeDropMoxifloxacin.isChecked=(data.sn_post_eye_1=="Eye drop Moxifloxacin P 1° per 8 times a day for the 1st week")

            binding.checkboxEyeDropMoxifloxacin6.isChecked=(data.sn_post_eye_2=="Eye drop Moxifloxacin P 1° per 6 times a day for the 2nd week")

            binding.checkboxEyeDropMoxifloxacin4.isChecked=(data.sn_post_eye_3=="Eye drop Moxifloxacin P 1° per 4 times a day for the 3rd week")

            binding.checkboxEyeDropMoxifloxacin2times.isChecked=(data.sn_post_eye_4=="Eye drop Moxifloxacin P 1° per 2 times a day for the 4th week")

            binding.checkboxEyeDropMoxifloxacin5w.isChecked=(data.sn_post_eye_5=="Eye drop Moxifloxacin P 1°per day for the 5th week")

            binding.checkboxEyeDropMoxifloxacinP.isChecked=(data.sn_post_eye_moxifloxacin=="Eye drop Moxifloxacin P")

            binding.checkboxEyeDropHomide.isChecked=(data.sn_post_eye_homide=="Eye drop Homide")
            binding.checkboxMacularOdema.isChecked=(data.sn_common_macular=="Macular odema")

            binding.checkboxEyeDropTimololSos.isChecked=(data.sn_post_eye_timolol=="Eye drop timolol sos")

            binding.checkboxEyeDropHypersolSos.isChecked=(data.sn_post_eye_hypersol=="Eye drop hypersol sos")

            binding.checkboxLubricantDropRefresh.isChecked=(data.sn_post_eye_lubricant=="Lubricant drop refresh")

            binding.checkboxWheatherEuipmentAddressed.isChecked=(data.sn_before_or_weather=="Wheather there are any equipment issues to be addressed")

            binding.checkboxKeyConcerns.isChecked=(data.sn_before_or_key=="Key concerns for recovery and management")

            binding.checkboxBeforeIncision.isChecked=(data.sn_before_incision_all_team=="All team members have introduced themselves by name and role")



            val sn_nurse_orally_confirm=data.sn_nurse_orally_confirm
            binding.checkboxSNOrallyConfirmAntibiotic.isChecked="Antibiotic" in sn_nurse_orally_confirm

            binding.checkboxSNOrallyConfirmImplants.isChecked="Implants / Devices" in sn_nurse_orally_confirm
            binding.checkboxSNOrallyConfirmDyes.isChecked="Dyes" in sn_nurse_orally_confirm
            binding.checkboxSNOrallyConfirmGas.isChecked="Gas" in sn_nurse_orally_confirm
            binding.checkboxSNOrallyConfirmImplantsStyle.isChecked="Implants style and power" in sn_nurse_orally_confirm
            binding.checkboxSNOrallyConfirmMitomycin.isChecked="Mitomycin / Anti-Neoplastics" in sn_nurse_orally_confirm

            val sn_intra_occular_lens=data.sn_intra_occular_lens


            binding.checkboxAcIol.isChecked="AC IOL" in sn_intra_occular_lens
            binding.checkboxPcIol.isChecked="PC IOL" in sn_intra_occular_lens
            binding.checkboxIRIS.isChecked="IRIS CLAW IOL" in sn_intra_occular_lens

            val sn_type_of_surgery=data.sn_type_of_surgery
            binding.checkboxSmallIncisionCataractSurgeryWithIOL.isChecked="Small Incision Cataract Surgery with IOL" in sn_type_of_surgery
            binding.checkboxSmallIncisionCataractSurgeryWithoutIOL.isChecked="Small incision Cataract Surgery without IOL" in sn_type_of_surgery
            binding.checkboxExtracapsularCataractExtraction.isChecked="Extracapsular Cataract Extraction" in sn_type_of_surgery
            binding.checkboxIntracapsularCataractExcisionr.isChecked="(Intracapsular Cataract Excisionr) Phacoemulsification Surgery" in sn_type_of_surgery
            binding.checkboxPterygium.isChecked="Pterygium" in sn_type_of_surgery
            binding.checkboxSurgery.isChecked="Surgery" in sn_type_of_surgery
            binding.checkboxStyExcision.isChecked="Sty excision" in sn_type_of_surgery
            binding.checkboxPtosisCorrection.isChecked="Ptosis correction" in sn_type_of_surgery
            binding.checkboxOtherTypesOfSurgery.isChecked="Other" in sn_type_of_surgery












        }

    }
}