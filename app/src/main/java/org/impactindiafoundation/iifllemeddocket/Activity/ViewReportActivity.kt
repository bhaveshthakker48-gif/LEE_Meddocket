package org.impactindiafoundation.iifllemeddocket.Activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.impactindiafoundation.iifllemeddocket.Adapter.ViewReportAdapter
import org.impactindiafoundation.iifllemeddocket.CallBack.FormClick
import org.impactindiafoundation.iifllemeddocket.Fragment.Eye_OPD_Doctor_NoteFragment
import org.impactindiafoundation.iifllemeddocket.Fragment.Eye_Post_Op_and_Follow_UpsFragment
import org.impactindiafoundation.iifllemeddocket.Fragment.Eye_Pre_Op_InvestigationFragment
import org.impactindiafoundation.iifllemeddocket.Fragment.Eye_Pre_Op_NotesFragment
import org.impactindiafoundation.iifllemeddocket.Fragment.OPD_InvestigationFragment
import org.impactindiafoundation.iifllemeddocket.Fragment.RefractiveErrorFragment
import org.impactindiafoundation.iifllemeddocket.Fragment.Surgical_NotesFragment
import org.impactindiafoundation.iifllemeddocket.Fragment.VisualAcuityFragment
import org.impactindiafoundation.iifllemeddocket.Fragment.VitalFragment
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityViewReportBinding

class ViewReportActivity:AppCompatActivity(),FormClick {

     lateinit var binding:ActivityViewReportBinding
     lateinit var adapter: ViewReportAdapter
     var patientID:String=""
    var patientData:PatientDataLocal?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityViewReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        binding.toolbarViewReport.toolbar.title="Patient Details"
        patientData= intent.getParcelableExtra("PatientDataLocal")
        patientID= patientData!!.patientId.toString()
        val patientFname=patientData!!.patientFname
        val patientLname=patientData!!.patientLname
        val patientAge=patientData!!.patientAge
        val ageUnit=patientData!!.AgeUnit
        val patientGender=patientData!!.patientGen
        val camp=patientData!!.location
        binding.edtPatientName.setText("Name:- "+patientFname+" "+patientLname)
        binding.edtAge.setText("Age:- " +patientAge.toString()+" "+ageUnit)
        binding.edtId.text="Patient ID:- "+patientID.toString()
        binding.edtGend.setText("Gender:- "+patientGender)
        binding.edtCampLoc.setText("Camp :- "+camp)
        Log.d(ConstantsApp.TAG,"patientData=>"+patientData)
        val labels1 = arrayOf("Vital", "OPD Investigation", "Visual Acuity", "Refractive Error", "Eye OPD Doctor Note", "Eye Pre-Op Investigation", "Eye Pre-Op Notes", "Surgical Notes", "Eye Post-Op and Follow Ups")
        adapter = ViewReportAdapter(this,labels1,this)
        binding.RecyclerViewViewReport.adapter = adapter
        binding.RecyclerViewViewReport.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        setFirstFragment()
    }


    override fun onBackPressed() {
        super.onBackPressed()
        gotoScreen(this,MainActivity::class.java)
    }

    fun gotoScreen(context: Context?, cls: Class<*>?) {
        val intent = Intent(context, cls)
        startActivity(intent)
        finish()
    }

    override fun FormClick(form: String, position: Int, view: View) {
       Log.d(ConstantsApp.TAG,"Clicked form=>"+form)
        setForm(form,patientID)
        adapter.setSelectedItem(position)
        adapter.notifyDataSetChanged()
    }

    private fun setForm(form: String, patientID: String) {
        when(form) {"Vital"-> {
                val vitalFormFragment = VitalFragment()
                val bundle = Bundle()
                bundle.putParcelable("patientData", patientData)
                vitalFormFragment.arguments = bundle
                setFragment(vitalFormFragment, bundle)
            }

            "OPD Investigation"-> {
                val vitalFormFragment = OPD_InvestigationFragment()
                val bundle = Bundle()
                bundle.putParcelable("patientData", patientData)
                vitalFormFragment.arguments = bundle
                setFragment(vitalFormFragment, bundle)
            }

            "Visual Acuity"-> {
                val vitalFormFragment = VisualAcuityFragment()
                val bundle = Bundle()
                bundle.putParcelable("patientData", patientData)
                vitalFormFragment.arguments = bundle
                setFragment(vitalFormFragment, bundle)
            }

            "Refractive Error"-> {
                val vitalFormFragment = RefractiveErrorFragment()
                val bundle = Bundle()
                bundle.putParcelable("patientData", patientData)
                vitalFormFragment.arguments = bundle
                setFragment(vitalFormFragment, bundle)
            }

            "Eye OPD Doctor Note"-> {
                val vitalFormFragment = Eye_OPD_Doctor_NoteFragment()
                val bundle = Bundle()
                bundle.putParcelable("patientData", patientData)
                vitalFormFragment.arguments = bundle
                setFragment(vitalFormFragment, bundle)
            }

            "Eye Pre-Op Investigation"-> {
                val vitalFormFragment = Eye_Pre_Op_InvestigationFragment()
                val bundle = Bundle()
                bundle.putParcelable("patientData", patientData)
                vitalFormFragment.arguments = bundle
                setFragment(vitalFormFragment, bundle)
            }

            "Eye Pre-Op Notes"-> {
                val vitalFormFragment = Eye_Pre_Op_NotesFragment()
                val bundle = Bundle()
                bundle.putParcelable("patientData", patientData)
                vitalFormFragment.arguments = bundle
                setFragment(vitalFormFragment, bundle)
            }

            "Surgical Notes"-> {
                val vitalFormFragment = Surgical_NotesFragment()
                val bundle = Bundle()
                bundle.putParcelable("patientData", patientData)
                vitalFormFragment.arguments = bundle
                setFragment(vitalFormFragment, bundle)
            }

            "Eye Post-Op and Follow Ups"-> {
                val vitalFormFragment = Eye_Post_Op_and_Follow_UpsFragment()
                val bundle = Bundle()
                bundle.putParcelable("patientData", patientData)
                vitalFormFragment.arguments = bundle
                setFragment(vitalFormFragment, bundle)
            }
        }
    }

    fun setFragment(fragment: Fragment, bundle: Bundle? = null) {
        if (bundle != null) {
            fragment.arguments = bundle
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment, "")
            .commit()
    }

    fun setFirstFragment() {
        val vitalFormFragment = VitalFragment()
        val bundle = Bundle()
        bundle.putParcelable("patientData", patientData)
        vitalFormFragment.arguments = bundle
        setFragment(vitalFormFragment, bundle)
    }
}