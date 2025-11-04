package org.impactindiafoundation.iifllemeddocket.ui.activity

import android.R
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.Activity.PatientForms
import org.impactindiafoundation.iifllemeddocket.Activity.PrintActivity1
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Vitals
import org.impactindiafoundation.iifllemeddocket.Model.PatientData
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import org.impactindiafoundation.iifllemeddocket.Utils.SearchAbleList
import org.impactindiafoundation.iifllemeddocket.Utils.SessionManager
import org.impactindiafoundation.iifllemeddocket.Utils.SingleSelectBottomSheetDialogFragment
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Constants
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Status
import org.impactindiafoundation.iifllemeddocket.architecture.model.PatientReport
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.OrthosisViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.PatientReportViewModel
import org.impactindiafoundation.iifllemeddocket.architecture.viewModel.RefractiveErrorViewModel
import org.impactindiafoundation.iifllemeddocket.databinding.ActivityRefractiveErrorFormBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class RefractiveErrorFormActivity : BaseActivity() {

    private lateinit var binding: ActivityRefractiveErrorFormBinding

    private var isNearVision = false
    private var isDistantVision = false
    private var ndClickedId = 0
    private val refractiveViewModel: RefractiveErrorViewModel by viewModels()
    private val patientReportVM: PatientReportViewModel by viewModels()


    private lateinit var sessionManager: SessionManager


    //form data fields
    //user data
    private var campId = 0
    private var patientId = 0
    private var userId = ""

    //right eye data
    private var rdvSphere = ""
    private var rdvCylinder = ""
    private var rdvAxis = ""
    private var rdvVisualActivity = ""
    private var rdvVisualActivityUnit = ""

    private var rnvSphere = ""
    private var rnvCylinder = ""
    private var rnvAxis = ""
    private var rnvReadingAddition = ""
    private var rnvVisualActivity = ""
    private var rightPrism = ""
    private var rightBase = ""
    private var rightRemarks = ""

    //left eye data
    private var ldvSphere = ""
    private var ldvCylinder = ""
    private var ldvAxis = ""
    private var ldvVisualActivity = ""
    private var ldvVisualActivityUnit = ""


    private var lnvSphere = ""
    private var lnvCylinder = ""
    private var lnvAxis = ""
    private var lnvReadingAddition = ""
    private var lnvVisualActivity = ""
    private var leftPrism = ""
    private var leftBase = ""
    private var leftRemarks = ""

    //other details
    private var pupillaryDistance = ""
    private var bilateralVertexDistance = ""
    private var commonRemarks = ""
    private var fundusNotes = ""

    //spectacles given details
    private var isPrescriptionGlassesOrdered = false
    private var prescriptionGlassesData = ""
    private var frameCode = ""
    private var frameColor = ""

    private var isReadingGlassesGiven = false
    private var readingGlassesData = ""


    private var intentFormId = 0
    private var localFormId = 0
    private var patientReportFormId = 0
    private var canEdit = true
    private var isFormLocal = false
    private var intentDecodeText = ""


    var patientFname = ""
    var patientLname = ""
    var patientAge = 0
    var patientID = 0
    var campID = 0
    var patientGender = ""
    var camp = ""
    var ageUnit = ""

    private var isEditing = false
    private var savedReportFormId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityRefractiveErrorFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        WindowCompat.getInsetsController(window, window.decorView)?.isAppearanceLightStatusBars = true
        window.statusBarColor = Color.WHITE

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, insets ->
            val imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime())
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Choose whichever bottom inset is larger (IME or system bars)
            val bottom = maxOf(systemBarsInsets.bottom, imeInsets.bottom)

            view.setPadding(
                systemBarsInsets.left,
                systemBarsInsets.top,
                systemBarsInsets.right,
                bottom
            )

            insets
        }

        initUi()
        initObserver()
        setPatientData()
    }

    private fun initUi() {
        sessionManager = SessionManager(this)
        var (patientId, campId, userId) = ConstantsApp.extractPatientAndLoginData(sessionManager)
//        this@RefractiveErrorFormActivity.campId = campId ?: 0
        this@RefractiveErrorFormActivity.patientId = patientId ?: 0
        this@RefractiveErrorFormActivity.userId = userId ?: ""

        intentFormId = intent.getIntExtra("localFormId", 0)
        this@RefractiveErrorFormActivity.campId = intent.getIntExtra("campId", 0)
        patientReportFormId = intent.getIntExtra("reportFormId", 0)

        refractiveViewModel.getRefractiveFormById(intentFormId)


        binding.refractiveToolBar.toolbar.title = "Refractive Error"

        binding.btnSubmit.setOnClickListener {
            if (validateForm()) {
                saveRefractiveErrorForm()
            }
        }


        //Arrow Down Click logic
        binding.llRightEyeDetailTab.setOnClickListener {
            var isArrowDown = binding.llRightEyeSection.isVisible
            //for up arrow logic for degree's for rotation
            val fromDegree = if (isArrowDown) 0f else 180f
            val toDegree = if (isArrowDown) 180f else 360f

            val rotateAnimator = ObjectAnimator.ofFloat(
                binding.ivRightArrowDown,
                "rotation",
                fromDegree,
                toDegree
            )
            rotateAnimator.duration = 300 // duration of the animation in milliseconds
            rotateAnimator.start()

            if (isArrowDown) {
                binding.llRightEyeSection.visibility = View.GONE
            } else {
                binding.llRightEyeSection.visibility = View.VISIBLE
            }
            isArrowDown = !isArrowDown
        }

        binding.llLeftEyeDetailTab.setOnClickListener {
            var isArrowDown = binding.llLeftEyeSection.isVisible
            //for up arrow logic for degree's for rotation
            val fromDegree = if (isArrowDown) 0f else 180f
            val toDegree = if (isArrowDown) 180f else 360f
            val rotateAnimator = ObjectAnimator.ofFloat(
                binding.ivLeftArrowDown,
                "rotation",
                fromDegree,
                toDegree
            )
            rotateAnimator.duration = 300 // duration of the animation in milliseconds
            rotateAnimator.start()

            if (isArrowDown) {
                binding.llLeftEyeSection.visibility = View.GONE
            } else {
                binding.llLeftEyeSection.visibility = View.VISIBLE
            }
            isArrowDown = !isArrowDown
        }

        binding.llSpectacleDistribution.setOnClickListener {
            var isArrowDown = binding.llSpectacleDistributionSection.isVisible
            //for up arrow logic for degree's for rotation
            val fromDegree = if (isArrowDown) 0f else 180f
            val toDegree = if (isArrowDown) 180f else 360f

            //for down arrow logic for degree's for rotation
//                    val fromDegree = if (isArrowDown) 180f else 0f
//                    val toDegree = if (isArrowDown) 360f else 180f

            val rotateAnimator = ObjectAnimator.ofFloat(
                binding.ivSpectacleArrowDown,
                "rotation",
                fromDegree,
                toDegree
            )
            rotateAnimator.duration = 300 // duration of the animation in milliseconds
            rotateAnimator.start()

            if (isArrowDown) {
                binding.llSpectacleDistributionSection.visibility = View.GONE
            } else {
                binding.llSpectacleDistributionSection.visibility = View.VISIBLE
            }
            isArrowDown = !isArrowDown
        }

        binding.etRDVSphere.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Sphere", provideSphereData(), binding.etRDVSphere)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        binding.etRDVCylinder.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Cylinder", provideCylinderData(), binding.etRDVCylinder)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }


        }

        binding.etRDVAxis.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Axis", provideAxisData(), binding.etRDVAxis)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }


        val visualUnitData = listOf("Acuity Unit","Meters", "logMAR")
        val visualUnitAdapter =
            ArrayAdapter(
                this@RefractiveErrorFormActivity,
                R.layout.simple_dropdown_item_1line,
                visualUnitData
            )
        binding.etRDVUnit.setAdapter(visualUnitAdapter)
        binding.etRDVUnit.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                binding.etRDVUnit.showDropDown()
                setNearDistantClickLogic(ndClickedId)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }


        }

        binding.etRDVVisualActivity.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                if (!binding.etRDVUnit.text.isNullOrEmpty()) {
                    val unitData = binding.etRDVUnit.text.toString()
                    if (unitData == "Acuity Unit") {
                        Utility.warningToast(this@RefractiveErrorFormActivity, "Please Choose Unit")
                    }else if (unitData == "Meters") {
                        inflateBottomSheet(
                            "Select Visual Acuity",
                            provideMetersVisualData(),
                            binding.etRDVVisualActivity
                        )
                    } else if (unitData == "logMAR") {
                        inflateBottomSheet(
                            "Select Visual Acuity",
                            provideLogMarVisualData(),
                            binding.etRDVVisualActivity
                        )
                    }
                } else {
                    Utility.warningToast(this@RefractiveErrorFormActivity, "Please Choose Unit")
                }
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }


        }

        binding.etRightPrism.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Prism", providePrismData(), binding.etRightPrism)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }


        val baseData = listOf("Up", "Down", "In", "Out")
        val baseAdapter =
            ArrayAdapter(
                this@RefractiveErrorFormActivity,
                R.layout.simple_dropdown_item_1line,
                baseData
            )
        binding.etRightPrismBase.setAdapter(baseAdapter)
        binding.etRightPrismBase.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                binding.etRightPrismBase.showDropDown()
                setNearDistantClickLogic(ndClickedId)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }
        //Right Eye Near
        binding.etRNVSphere.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Sphere", provideSphereData(), binding.etRNVSphere)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }
        }


        binding.etRNVCylinder.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Cylinder", provideCylinderData(), binding.etRNVCylinder)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        binding.etRNVAxis.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Axis", provideAxisData(), binding.etRNVAxis)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }
        }


        binding.etRNVReadingAddition.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet(
                    "Select Reading Addition",
                    provideAdditionalReadingData(),
                    binding.etRNVReadingAddition
                )
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }
        }

        binding.etRNVVisualActivity.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet(
                    "Select Sphere",
                    provideNearVisionVisualData(),
                    binding.etRNVVisualActivity
                )
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }
        }

        //Left Eye Distant
        binding.etLDVSphere.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Sphere", provideSphereData(), binding.etLDVSphere)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }
        }

        binding.etLDVCylinder.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Cylinder", provideCylinderData(), binding.etLDVCylinder)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        binding.etLDVAxis.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Axis", provideAxisData(), binding.etLDVAxis)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }
        }


        val visualLeftUnitData = listOf("Acuity Unit","Meters", "logMAR")
        val visualLeftUnitAdapter =
            ArrayAdapter(
                this@RefractiveErrorFormActivity,
                R.layout.simple_dropdown_item_1line,
                visualLeftUnitData
            )
        binding.etLDVUnit.setAdapter(visualLeftUnitAdapter)
        binding.etLDVUnit.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                binding.etLDVUnit.showDropDown()
                setNearDistantClickLogic(ndClickedId)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        binding.etLDVVisualActivity.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                if (!binding.etLDVUnit.text.isNullOrEmpty()) {
                    val unitData = binding.etLDVUnit.text.toString()
                    if (unitData == "Acuity Unit") {
                        Utility.warningToast(this@RefractiveErrorFormActivity, "Please Choose Unit")
                    } else if (unitData == "Meters") {
                        inflateBottomSheet(
                            "Select Visual Acuity",
                            provideMetersVisualData(),
                            binding.etLDVVisualActivity
                        )
                    } else if (unitData == "logMAR") {
                        inflateBottomSheet(
                            "Select Visual Acuity",
                            provideLogMarVisualData(),
                            binding.etLDVVisualActivity
                        )
                    }
                } else {
                    Utility.warningToast(this@RefractiveErrorFormActivity, "Please Choose Unit")
                }
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }


        }

        binding.etLeftPrism.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Prism", providePrismData(), binding.etLeftPrism)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        val baseLeftData = listOf("Up", "Down", "In", "Out")
        val baseLeftAdapter =
            ArrayAdapter(
                this@RefractiveErrorFormActivity,
                R.layout.simple_dropdown_item_1line,
                baseLeftData
            )
        binding.etLeftPrismBase.setAdapter(baseLeftAdapter)
        binding.etLeftPrismBase.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                binding.etLeftPrismBase.showDropDown()
                setNearDistantClickLogic(ndClickedId)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }


        }

        //Left Eye Near
        binding.etLNVSphere.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Sphere", provideSphereData(), binding.etLNVSphere)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }
        }


        binding.etLNVCylinder.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Cylinder", provideCylinderData(), binding.etLNVCylinder)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        binding.etLNVAxis.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet("Select Axis", provideAxisData(), binding.etLNVAxis)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }
        }


        binding.etLNVReadingAddition.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet(
                    "Select Addition Reading",
                    provideAdditionalReadingData(),
                    binding.etLNVReadingAddition
                )
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        binding.etLNVVisualActivity.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet(
                    "Select Visual Acuity",
                    provideNearVisionVisualData(),
                    binding.etLNVVisualActivity
                )
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        //other detail dropdowns
        binding.etPupillaryDistance.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet(
                    "Select Pupillary Distance(mm)",
                    providePupillaryData(),
                    binding.etPupillaryDistance
                )
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        binding.etFrameCode.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet(
                    "Select Frame Code",
                    provideFrameCode(),
                    binding.etFrameCode
                )
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        binding.etFrameColor.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet(
                    "Select Frame Color",
                    provideFrameColor(),
                    binding.etFrameColor
                )
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        binding.etBilateralVertexDistance.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet(
                    "Select Bilateral Vertex Distance(mm)",
                    provideBvdData(),
                    binding.etBilateralVertexDistance
                )
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }
        }

        binding.etPrescriptionGiven.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                inflateBottomSheet(
                    "Select Given Prescription",
                    providePrescriptionGivenData(),
                    binding.etPrescriptionGiven
                )
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        val readingGlassData = provideReadingGivenData()
        val readingGlassAdapter =
            ArrayAdapter(
                this@RefractiveErrorFormActivity,
                R.layout.simple_dropdown_item_1line,
                readingGlassData
            )
        binding.etReadingGlassesGiven.setAdapter(readingGlassAdapter)
        binding.etReadingGlassesGiven.setOnClickListener {
            if (canEdit) {
                ndClickedId = it.id
                binding.etReadingGlassesGiven.showDropDown()
                setNearDistantClickLogic(ndClickedId)
            } else {
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }

        binding.cbPrescriptionGiven.setOnCheckedChangeListener { _, isChecked ->
                isPrescriptionGlassesOrdered = isChecked

        }

        binding.cbReadingSpectaclesGiven.setOnCheckedChangeListener { _, isChecked ->
                isReadingGlassesGiven = isChecked

        }

        binding.btnEdit.setOnClickListener {
            if (isEditing){
                binding.btnEdit.visibility = View.GONE
                binding.btnSubmit.visibility = View.VISIBLE
            }
            else{
                if (!canEdit) {
                    onFormEditClick()
                    allowClickableEditText(true)
                }
            }

        }
    }

    private fun initObserver() {

        refractiveViewModel.insertRefractiveResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        intentFormId = it.data?.toInt()?:0
                        val patientReport = PatientReport(
                            id = patientReportFormId,
                            formType = Constants.REFRACTIVE_FORM,
                            formId = it.data?.toInt() ?: 0,
                            patientFname = patientFname,
                            patientLname = patientLname,
                            patientId = patientID,
                            patientGen = patientGender,
                            patientAge = patientAge,
                            camp_id = campID,
                            location = camp,
                            AgeUnit = ageUnit,
                            RegNo = ""
                        )
                        patientReportVM.insertPatientReport(patientReport)
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@RefractiveErrorFormActivity, "Unexpected error")

                }
            }
        }

        patientReportVM.insertPatientReportResponse.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        Utility.successToast(
                            this@RefractiveErrorFormActivity,
                            "Form Submitted Successfully"
                        )


                        if (isDistantVision) {
                            patientReportFormId = it.data?.toInt()?:0

                            binding.btnEdit.visibility = View.VISIBLE
                            binding.btnSubmit.visibility = View.GONE
                            isEditing = true
                            val intent =
                                Intent(this@RefractiveErrorFormActivity, PrintActivity1::class.java)
                            startActivity(intent)
//                            finish()
                        } else {
                            onBackPressed()

                        }
                    } catch (e: Exception) {
                        Log.e("FormSaveError", e.message!!)

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@RefractiveErrorFormActivity, "Unexpected error")

                }
            }
        }

        refractiveViewModel.refractiveFormListById.observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    //    progress.show()
                }

                Status.SUCCESS -> {
                    //  progress.dismiss()
                    try {
                        if (!it.data.isNullOrEmpty()) {
                            localFormId = it.data[0]._id
                            isFormLocal = true
                            val refractiveForm = it.data[0]
                            canEdit = false
                            binding.btnEdit.visibility = View.VISIBLE
                            binding.btnSubmit.visibility = View.GONE


                            setUpFormData(refractiveForm)
                            allowClickableEditText(false)
                        } else {
                            allowClickableEditText(true)


                            localFormId = 0
                            isFormLocal = false
                            canEdit = true
                            binding.btnEdit.visibility = View.GONE
                            binding.btnSubmit.visibility = View.VISIBLE
                        }
                    } catch (e: Exception) {

                        Log.e("FormSaveError", e.message!!)

                    }
                }

                Status.ERROR -> {
                    Utility.errorToast(this@RefractiveErrorFormActivity, "Unexpected error")

                }
            }
        }
    }

    //Providing Data For All Dropdowns
    private fun provideSphereData(): List<String> {
        val spehereData = ArrayList<String>()
        spehereData!!.add("Sphere")
        spehereData!!.add("0.00")
        spehereData!!.add("+0.25")
        spehereData!!.add("+0.50")
        spehereData!!.add("+0.75")
        spehereData!!.add("+1.00")
        spehereData!!.add("+1.25")
        spehereData!!.add("+1.50")
        spehereData!!.add("+1.75")
        spehereData!!.add("+2.00")
        spehereData!!.add("+2.25")
        spehereData!!.add("+2.50")
        spehereData!!.add("+2.75")
        spehereData!!.add("+3.00")
        spehereData!!.add("+3.25")
        spehereData!!.add("+3.50")
        spehereData!!.add("+3.75")
        spehereData!!.add("+4.00")
        spehereData!!.add("+4.25")
        spehereData!!.add("+4.50")
        spehereData!!.add("+4.75")
        spehereData!!.add("+5.00")
        spehereData!!.add("+5.25")
        spehereData!!.add("+5.50")
        spehereData!!.add("+5.75")
        spehereData!!.add("+6.00")
        spehereData!!.add("+6.25")
        spehereData!!.add("+6.50")
        spehereData!!.add("+6.75")
        spehereData!!.add("+7.00")
        spehereData!!.add("+7.25")
        spehereData!!.add("+7.50")
        spehereData!!.add("+7.75")
        spehereData!!.add("+8.00")
        spehereData!!.add("+8.25")
        spehereData!!.add("+8.50")
        spehereData!!.add("+8.75")
        spehereData!!.add("+9.00")
        spehereData!!.add("+9.25")
        spehereData!!.add("+9.50")
        spehereData!!.add("+9.75")
        spehereData!!.add("+10.00")
        spehereData!!.add("+10.25")
        spehereData!!.add("+10.50")
        spehereData!!.add("+10.75")
        spehereData!!.add("+11.00")
        spehereData!!.add("+11.25")
        spehereData!!.add("+11.50")
        spehereData!!.add("+11.75")
        spehereData!!.add("+12.00")
        spehereData!!.add("+12.25")
        spehereData!!.add("+12.50")
        spehereData!!.add("+12.75")
        spehereData!!.add("+13.00")
        spehereData!!.add("+13.25")
        spehereData!!.add("+13.50")
        spehereData!!.add("+13.75")
        spehereData!!.add("+14.00")
        spehereData!!.add("+14.25")
        spehereData!!.add("+14.50")
        spehereData!!.add("+14.75")
        spehereData!!.add("+15.00")
        spehereData!!.add("+15.25")
        spehereData!!.add("+15.50")
        spehereData!!.add("+15.75")
        spehereData!!.add("+16.00")
        spehereData!!.add("+16.25")
        spehereData!!.add("+16.50")
        spehereData!!.add("+16.75")
        spehereData!!.add("+17.00")
        spehereData!!.add("+17.25")
        spehereData!!.add("+17.50")
        spehereData!!.add("+17.75")
        spehereData!!.add("+18.00")
        spehereData!!.add("+18.25")
        spehereData!!.add("+18.50")
        spehereData!!.add("+18.75")
        spehereData!!.add("+19.00")
        spehereData!!.add("+19.25")
        spehereData!!.add("+19.50")
        spehereData!!.add("+19.75")
        spehereData!!.add("+20.00")
        spehereData!!.add("-0.25")
        spehereData!!.add("-0.50")
        spehereData!!.add("-0.75")
        spehereData!!.add("-1.00")
        spehereData!!.add("-1.25")
        spehereData!!.add("-1.50")
        spehereData!!.add("-1.75")
        spehereData!!.add("-2.00")
        spehereData!!.add("-2.25")
        spehereData!!.add("-2.50")
        spehereData!!.add("-2.75")
        spehereData!!.add("-3.00")
        spehereData!!.add("-3.25")
        spehereData!!.add("-3.50")
        spehereData!!.add("-3.75")
        spehereData!!.add("-4.00")
        spehereData!!.add("-4.25")
        spehereData!!.add("-4.50")
        spehereData!!.add("-4.75")
        spehereData!!.add("-5.00")
        spehereData!!.add("-5.25")
        spehereData!!.add("-5.50")
        spehereData!!.add("-5.75")
        spehereData!!.add("-6.00")
        spehereData!!.add("-6.25")
        spehereData!!.add("-6.50")
        spehereData!!.add("-6.75")
        spehereData!!.add("-7.00")
        spehereData!!.add("-7.25")
        spehereData!!.add("-7.50")
        spehereData!!.add("-7.75")
        spehereData!!.add("-8.00")
        spehereData!!.add("-8.25")
        spehereData!!.add("-8.50")
        spehereData!!.add("-8.75")
        spehereData!!.add("-9.00")
        spehereData!!.add("-9.25")
        spehereData!!.add("-9.50")
        spehereData!!.add("-9.75")
        spehereData!!.add("-10.00")
        spehereData!!.add("-10.25")
        spehereData!!.add("-10.50")
        spehereData!!.add("-10.75")
        spehereData!!.add("-11.00")
        spehereData!!.add("-11.25")
        spehereData!!.add("-11.50")
        spehereData!!.add("-11.75")
        spehereData!!.add("-12.00")
        spehereData!!.add("-12.25")
        spehereData!!.add("-12.50")
        spehereData!!.add("-12.75")
        spehereData!!.add("-13.00")
        spehereData!!.add("-13.25")
        spehereData!!.add("-13.50")
        spehereData!!.add("-13.75")
        spehereData!!.add("-14.00")
        spehereData!!.add("-14.25")
        spehereData!!.add("-14.50")
        spehereData!!.add("-14.75")
        spehereData!!.add("-15.00")
        spehereData!!.add("-15.25")
        spehereData!!.add("-15.50")
        spehereData!!.add("-15.75")
        spehereData!!.add("-16.00")
        spehereData!!.add("-16.25")
        spehereData!!.add("-16.50")
        spehereData!!.add("-16.75")
        spehereData!!.add("-17.00")
        spehereData!!.add("-17.25")
        spehereData!!.add("-17.50")
        spehereData!!.add("-17.75")
        spehereData!!.add("-18.00")
        spehereData!!.add("-18.25")
        spehereData!!.add("-18.50")
        spehereData!!.add("-18.75")
        spehereData!!.add("-19.00")
        spehereData!!.add("-19.25")
        spehereData!!.add("-19.50")
        spehereData!!.add("-19.75")
        spehereData!!.add("-20.00")

        return spehereData
    }

    private fun provideAdditionalReadingData(): List<String> {
        val AdditionalReading = ArrayList<String>()
        AdditionalReading!!.add("Reading Addition")
        AdditionalReading!!.add("0.00")
        AdditionalReading!!.add("+0.25")
        AdditionalReading!!.add("+0.50")
        AdditionalReading!!.add("+0.75")
        AdditionalReading!!.add("+1.00")
        AdditionalReading!!.add("+1.25")
        AdditionalReading!!.add("+1.50")
        AdditionalReading!!.add("+1.75")
        AdditionalReading!!.add("+2.00")
        AdditionalReading!!.add("+2.25")
        AdditionalReading!!.add("+2.50")
        AdditionalReading!!.add("+2.75")
        AdditionalReading!!.add("+3.00")
        AdditionalReading!!.add("+3.25")
        AdditionalReading!!.add("+3.50")
        AdditionalReading!!.add("+3.75")
        AdditionalReading!!.add("+4.00")
        AdditionalReading!!.add("+4.25")
        AdditionalReading!!.add("+4.50")
        AdditionalReading!!.add("+4.75")
        AdditionalReading!!.add("+5.00")
        AdditionalReading!!.add("+5.25")
        AdditionalReading!!.add("+5.50")
        AdditionalReading!!.add("+5.75")
        AdditionalReading!!.add("+6.00")
        AdditionalReading!!.add("+6.25")
        AdditionalReading!!.add("+6.50")
        AdditionalReading!!.add("+6.75")
        AdditionalReading!!.add("+7.00")
        AdditionalReading!!.add("+7.25")
        AdditionalReading!!.add("+7.50")
        AdditionalReading!!.add("+7.75")
        AdditionalReading!!.add("+8.00")
        AdditionalReading!!.add("+8.25")
        AdditionalReading!!.add("+8.50")
        AdditionalReading!!.add("+8.75")
        AdditionalReading!!.add("+9.00")
        AdditionalReading!!.add("+9.25")
        AdditionalReading!!.add("+9.50")
        AdditionalReading!!.add("+9.75")
        AdditionalReading!!.add("+10.00")
        AdditionalReading!!.add("+10.25")
        AdditionalReading!!.add("+10.50")
        AdditionalReading!!.add("+10.75")
        AdditionalReading!!.add("+11.00")
        AdditionalReading!!.add("+11.25")
        AdditionalReading!!.add("+11.50")
        AdditionalReading!!.add("+11.75")
        AdditionalReading!!.add("+12.00")
        AdditionalReading!!.add("+12.25")
        AdditionalReading!!.add("+12.50")
        AdditionalReading!!.add("+12.75")
        AdditionalReading!!.add("+13.00")
        AdditionalReading!!.add("+13.25")
        AdditionalReading!!.add("+13.50")
        AdditionalReading!!.add("+13.75")
        AdditionalReading!!.add("+14.00")
        AdditionalReading!!.add("+14.25")
        AdditionalReading!!.add("+14.50")
        AdditionalReading!!.add("+14.75")
        AdditionalReading!!.add("+15.00")
        AdditionalReading!!.add("+15.25")
        AdditionalReading!!.add("+15.75")
        AdditionalReading!!.add("+16.00")
        AdditionalReading!!.add("+16.25")
        AdditionalReading!!.add("+16.50")
        AdditionalReading!!.add("+16.75")
        AdditionalReading!!.add("+17.00")
        AdditionalReading!!.add("+17.25")
        AdditionalReading!!.add("+17.50")
        AdditionalReading!!.add("+17.75")
        AdditionalReading!!.add("+18.00")
        AdditionalReading!!.add("+18.25")
        AdditionalReading!!.add("+18.50")
        AdditionalReading!!.add("+18.75")
        AdditionalReading!!.add("+19.00")
        AdditionalReading!!.add("+19.25")
        AdditionalReading!!.add("+19.50")
        AdditionalReading!!.add("+19.75")
        AdditionalReading!!.add("+20.00")
        AdditionalReading!!.add("-0.25")
        AdditionalReading!!.add("-0.50")
        AdditionalReading!!.add("-0.75")
        AdditionalReading!!.add("-1.00")
        AdditionalReading!!.add("-1.25")
        AdditionalReading!!.add("-1.50")
        AdditionalReading!!.add("-1.75")
        AdditionalReading!!.add("-2.00")
        AdditionalReading!!.add("-2.25")
        AdditionalReading!!.add("-2.50")
        AdditionalReading!!.add("-2.75")
        AdditionalReading!!.add("-3.00")
        AdditionalReading!!.add("-3.25")
        AdditionalReading!!.add("-3.50")
        AdditionalReading!!.add("-3.75")
        AdditionalReading!!.add("-4.00")
        AdditionalReading!!.add("-4.25")
        AdditionalReading!!.add("-4.50")
        AdditionalReading!!.add("-4.75")
        AdditionalReading!!.add("-5.00")
        AdditionalReading!!.add("-5.25")
        AdditionalReading!!.add("-5.50")
        AdditionalReading!!.add("-5.75")
        AdditionalReading!!.add("-6.00")
        AdditionalReading!!.add("-6.25")
        AdditionalReading!!.add("-6.50")
        AdditionalReading!!.add("-6.75")
        AdditionalReading!!.add("-7.00")
        AdditionalReading!!.add("-7.25")
        AdditionalReading!!.add("-7.50")
        AdditionalReading!!.add("-7.75")
        AdditionalReading!!.add("-8.00")
        AdditionalReading!!.add("-8.25")
        AdditionalReading!!.add("-8.50")
        AdditionalReading!!.add("-8.75")
        AdditionalReading!!.add("-9.00")
        AdditionalReading!!.add("-9.25")
        AdditionalReading!!.add("-9.50")
        AdditionalReading!!.add("-9.75")
        AdditionalReading!!.add("-10.00")
        AdditionalReading!!.add("-10.25")
        AdditionalReading!!.add("-10.50")
        AdditionalReading!!.add("-10.75")
        AdditionalReading!!.add("-11.00")
        AdditionalReading!!.add("-11.25")
        AdditionalReading!!.add("-11.50")
        AdditionalReading!!.add("-12.00")
        AdditionalReading!!.add("-12.25")
        AdditionalReading!!.add("-12.50")
        AdditionalReading!!.add("-12.75")
        AdditionalReading!!.add("-13.00")
        AdditionalReading!!.add("-13.25")
        AdditionalReading!!.add("-13.50")
        AdditionalReading!!.add("-13.75")
        AdditionalReading!!.add("-14.00")
        AdditionalReading!!.add("-14.25")
        AdditionalReading!!.add("-14.75")
        AdditionalReading!!.add("-15.00")
        AdditionalReading!!.add("-15.25")
        AdditionalReading!!.add("-15.50")
        AdditionalReading!!.add("-15.75")
        AdditionalReading!!.add("-16.00")
        AdditionalReading!!.add("-16.25")
        AdditionalReading!!.add("-16.50")
        AdditionalReading!!.add("-16.75")
        AdditionalReading!!.add("-17.00")
        AdditionalReading!!.add("-17.25")
        AdditionalReading!!.add("-17.50")
        AdditionalReading!!.add("-17.75")
        AdditionalReading!!.add("-18.00")
        AdditionalReading!!.add("-18.25")
        AdditionalReading!!.add("-18.50")
        AdditionalReading!!.add("-18.75")
        AdditionalReading!!.add("-19.00")
        AdditionalReading!!.add("-19.25")
        AdditionalReading!!.add("-19.50")
        AdditionalReading!!.add("-19.75")
        AdditionalReading!!.add("-20.00")

        return AdditionalReading
    }

    private fun provideCylinderData(): List<String> {
        val cylinderData = ArrayList<String>()
        //CylinderArrayList!!.add("Select")
        cylinderData!!.add("Cylinder")
        cylinderData!!.add("0.00")
        cylinderData!!.add("+0.25")
        cylinderData!!.add("+0.50")
        cylinderData!!.add("+0.75")
        cylinderData!!.add("+1.00")
        cylinderData!!.add("+1.25")
        cylinderData!!.add("+1.50")
        cylinderData!!.add("+1.75")
        cylinderData!!.add("+2.00")
        cylinderData!!.add("+2.25")
        cylinderData!!.add("+2.50")
        cylinderData!!.add("+2.75")
        cylinderData!!.add("+3.00")
        cylinderData!!.add("+3.25")
        cylinderData!!.add("+3.50")
        cylinderData!!.add("+3.75")
        cylinderData!!.add("+4.00")
        cylinderData!!.add("+4.25")
        cylinderData!!.add("+4.50")
        cylinderData!!.add("+4.75")
        cylinderData!!.add("+5.00")
        cylinderData!!.add("+5.25")
        cylinderData!!.add("+5.50")
        cylinderData!!.add("+5.75")
        cylinderData!!.add("+6.00")
        cylinderData!!.add("+6.25")
        cylinderData!!.add("+6.50")
        cylinderData!!.add("+6.75")
        cylinderData!!.add("+7.00")
        cylinderData!!.add("+7.25")
        cylinderData!!.add("+7.50")
        cylinderData!!.add("+7.75")
        cylinderData!!.add("+8.00")
        cylinderData!!.add("+8.25")
        cylinderData!!.add("+8.50")
        cylinderData!!.add("+8.75")
        cylinderData!!.add("+9.00")
        cylinderData!!.add("+9.25")
        cylinderData!!.add("+9.50")
        cylinderData!!.add("+9.75")
        cylinderData!!.add("+10.00")
        cylinderData!!.add("+10.25")
        cylinderData!!.add("+10.50")
        cylinderData!!.add("+10.75")
        cylinderData!!.add("+11.00")
        cylinderData!!.add("+11.25")
        cylinderData!!.add("+11.50")
        cylinderData!!.add("+11.75")
        cylinderData!!.add("+12.00")
        cylinderData!!.add("-0.25")
        cylinderData!!.add("-0.50")
        cylinderData!!.add("-0.75")
        cylinderData!!.add("-1.00")
        cylinderData!!.add("-1.25")
        cylinderData!!.add("-1.50")
        cylinderData!!.add("-1.75")
        cylinderData!!.add("-2.00")
        cylinderData!!.add("-2.25")
        cylinderData!!.add("-2.50")
        cylinderData!!.add("-2.75")
        cylinderData!!.add("-3.00")
        cylinderData!!.add("-3.25")
        cylinderData!!.add("-3.50")
        cylinderData!!.add("-3.75")
        cylinderData!!.add("-4.00")
        cylinderData!!.add("-4.25")
        cylinderData!!.add("-4.50")
        cylinderData!!.add("-4.75")
        cylinderData!!.add("-5.00")
        cylinderData!!.add("-5.25")
        cylinderData!!.add("-5.50")
        cylinderData!!.add("-5.75")
        cylinderData!!.add("-6.00")
        cylinderData!!.add("-6.25")
        cylinderData!!.add("-6.50")
        cylinderData!!.add("-6.75")
        cylinderData!!.add("-7.00")
        cylinderData!!.add("-7.25")
        cylinderData!!.add("-7.50")
        cylinderData!!.add("-7.75")
        cylinderData!!.add("-8.00")
        cylinderData!!.add("-8.25")
        cylinderData!!.add("-8.50")
        cylinderData!!.add("-8.75")
        cylinderData!!.add("-9.00")
        cylinderData!!.add("-9.25")
        cylinderData!!.add("-9.50")
        cylinderData!!.add("-9.75")
        cylinderData!!.add("-10.00")
        cylinderData!!.add("-10.25")
        cylinderData!!.add("-10.50")
        cylinderData!!.add("-10.75")
        cylinderData!!.add("-11.00")
        cylinderData!!.add("-11.25")
        cylinderData!!.add("-11.50")
        cylinderData!!.add("-11.75")
        cylinderData!!.add("-12.00")

        return cylinderData
    }

    private fun provideAxisData(): List<String> {
        val axisdata = ArrayList<String>()
        // AxisArrayList!!.add("Select")
        axisdata!!.add("Axis")
        axisdata!!.add("0")
        axisdata!!.add("5")
        axisdata!!.add("10")
        axisdata!!.add("15")
        axisdata!!.add("20")
        axisdata!!.add("25")
        axisdata!!.add("30")
        axisdata!!.add("35")
        axisdata!!.add("40")
        axisdata!!.add("45")
        axisdata!!.add("50")
        axisdata!!.add("55")
        axisdata!!.add("60")
        axisdata!!.add("65")
        axisdata!!.add("70")
        axisdata!!.add("75")
        axisdata!!.add("80")
        axisdata!!.add("85")
        axisdata!!.add("90")
        axisdata!!.add("95")
        axisdata!!.add("100")
        axisdata!!.add("105")
        axisdata!!.add("110")
        axisdata!!.add("115")
        axisdata!!.add("120")
        axisdata!!.add("125")
        axisdata!!.add("130")
        axisdata!!.add("135")
        axisdata!!.add("140")
        axisdata!!.add("145")
        axisdata!!.add("150")
        axisdata!!.add("155")
        axisdata!!.add("160")
        axisdata!!.add("165")
        axisdata!!.add("170")
        axisdata!!.add("175")
        axisdata!!.add("180")

        return axisdata
    }

    private fun provideLogMarVisualData(): List<String> {
        val logMarData = ArrayList<String>()
        logMarData!!.add("Visual Acuity")
        logMarData!!.add("0.00")
        logMarData!!.add("0.02")
        logMarData!!.add("0.04")
        logMarData!!.add("0.06")
        logMarData!!.add("0.08")
        logMarData!!.add("0.10")
        logMarData!!.add("0.12")
        logMarData!!.add("0.14")
        logMarData!!.add("0.16")
        logMarData!!.add("0.18")
        logMarData!!.add("0.20")
        logMarData!!.add("0.22")
        logMarData!!.add("0.24")
        logMarData!!.add("0.26")
        logMarData!!.add("0.28")
        logMarData!!.add("0.30")
        logMarData!!.add("0.32")
        logMarData!!.add("0.34")
        logMarData!!.add("0.36")
        logMarData!!.add("0.38")
        logMarData!!.add("0.40")
        logMarData!!.add("0.42")
        logMarData!!.add("0.44")
        logMarData!!.add("0.46")
        logMarData!!.add("0.48")
        logMarData!!.add("0.50")
        logMarData!!.add("0.52")
        logMarData!!.add("0.54")
        logMarData!!.add("0.56")
        logMarData!!.add("0.58")
        logMarData!!.add("0.60")
        logMarData!!.add("0.62")
        logMarData!!.add("0.64")
        logMarData!!.add("0.66")
        logMarData!!.add("0.68")
        logMarData!!.add("0.70")
        logMarData!!.add("0.72")
        logMarData!!.add("0.74")
        logMarData!!.add("0.76")
        logMarData!!.add("0.78")
        logMarData!!.add("0.80")
        logMarData!!.add("0.82")
        logMarData!!.add("0.84")
        logMarData!!.add("0.86")
        logMarData!!.add("0.88")
        logMarData!!.add("0.90")
        logMarData!!.add("0.92")
        logMarData!!.add("0.94")
        logMarData!!.add("0.96")
        logMarData!!.add("0.98")
        logMarData!!.add("1.00")

        return logMarData
    }

    private fun provideMetersVisualData(): List<String> {
        val meterVisualData = ArrayList<String>()
//        meterVisualData!!.add("select")
        meterVisualData!!.add("Visual Acuity")
        meterVisualData!!.add("6/60")
        meterVisualData!!.add("6/48")
        meterVisualData!!.add("6/38")
        meterVisualData!!.add("6/30")
        meterVisualData!!.add("6/24")
        meterVisualData!!.add("6/19")
        meterVisualData!!.add("6/15")
        meterVisualData!!.add("6/12")
        meterVisualData!!.add("6/9.5")
        meterVisualData!!.add("6/7.5")
        meterVisualData!!.add("6/6.0")
        meterVisualData!!.add("6/4.8")
        meterVisualData!!.add("6/3.8")
        meterVisualData!!.add("6/3.0")
        meterVisualData!!.add("Counting fingers")
        meterVisualData!!.add("Hand motion")
        meterVisualData!!.add("Light perception")
        meterVisualData!!.add("No Light perception")

        return meterVisualData
    }

    private fun provideNearVisionVisualData(): List<String> {
        val nearVisionVisualData = ArrayList<String>()
        nearVisionVisualData!!.add("Visual Acuity")
        nearVisionVisualData!!.add("N5")
        nearVisionVisualData!!.add("N6")
        nearVisionVisualData!!.add("N8")
        nearVisionVisualData!!.add("N10")
        nearVisionVisualData!!.add("N12")
        nearVisionVisualData!!.add("N14")
        nearVisionVisualData!!.add("N18")
        nearVisionVisualData!!.add("N24")
        nearVisionVisualData!!.add("N36")
        nearVisionVisualData!!.add("N48")

        return nearVisionVisualData
    }

    private fun providePrismData(): List<String> {
        val prismData = ArrayList<String>()
        //PrismArrayList!!.add("Select")
        prismData!!.add("Prism")
        prismData!!.add("0.5")
        prismData!!.add("1.0")
        prismData!!.add("1.5")
        prismData!!.add("2.0")
        prismData!!.add("2.5")
        prismData!!.add("3.0")
        prismData!!.add("3.5")
        prismData!!.add("4.0")
        prismData!!.add("4.5")
        prismData!!.add("5.0")
        prismData!!.add("5.5")
        prismData!!.add("6.0")
        prismData!!.add("6.5")
        prismData!!.add("7.0")
        prismData!!.add("7.5")
        prismData!!.add("8.0")
        prismData!!.add("8.5")
        prismData!!.add("9.0")
        prismData!!.add("9.5")
        prismData!!.add("10.0")
        prismData!!.add("10.5")
        prismData!!.add("11.0")
        prismData!!.add("11.5")
        prismData!!.add("12.0")

        return prismData
    }

    private fun providePupillaryData(): List<String> {
        val pupillaryDistanceData = ArrayList<String>()
        pupillaryDistanceData!!.add("Pupillary Distance(mm)")
        pupillaryDistanceData!!.add("40")
        pupillaryDistanceData!!.add("41")
        pupillaryDistanceData!!.add("42")
        pupillaryDistanceData!!.add("43")
        pupillaryDistanceData!!.add("44")
        pupillaryDistanceData!!.add("45")
        pupillaryDistanceData!!.add("46")
        pupillaryDistanceData!!.add("47")
        pupillaryDistanceData!!.add("48")
        pupillaryDistanceData!!.add("49")
        pupillaryDistanceData!!.add("50")
        pupillaryDistanceData!!.add("51")
        pupillaryDistanceData!!.add("52")
        pupillaryDistanceData!!.add("53")
        pupillaryDistanceData!!.add("54")
        pupillaryDistanceData!!.add("55")
        pupillaryDistanceData!!.add("56")
        pupillaryDistanceData!!.add("57")
        pupillaryDistanceData!!.add("58")
        pupillaryDistanceData!!.add("59")
        pupillaryDistanceData!!.add("60")
        pupillaryDistanceData!!.add("61")
        pupillaryDistanceData!!.add("62")
        pupillaryDistanceData!!.add("63")
        pupillaryDistanceData!!.add("64")
        pupillaryDistanceData!!.add("65")
        pupillaryDistanceData!!.add("66")
        pupillaryDistanceData!!.add("67")
        pupillaryDistanceData!!.add("68")
        pupillaryDistanceData!!.add("69")
        pupillaryDistanceData!!.add("70")

        return pupillaryDistanceData

    }

    private fun provideFrameColor(): List<String> {
        val frameColorList = ArrayList<String>()

        frameColorList!!.add("Black")
        frameColorList!!.add("Brown")

        return frameColorList

    }

    private fun provideFrameCode(): List<String> {
        val frameCodeList = ArrayList<String>()

        frameCodeList!!.add("135")
        frameCodeList!!.add("136")
        frameCodeList!!.add("139")
        frameCodeList!!.add("140")
        frameCodeList!!.add("144")


        return frameCodeList

    }

    private fun provideBvdData(): List<String> {
        val bvdData = ArrayList<String>()
        bvdData!!.add("Bilateral Vertex Distance(mm)")
        bvdData!!.add("8")
        bvdData!!.add("9")
        bvdData!!.add("10")
        bvdData!!.add("11")
        bvdData!!.add("12")
        bvdData!!.add("13")
        bvdData!!.add("14")
        bvdData!!.add("15")
        bvdData!!.add("16")
        bvdData!!.add("17")
        bvdData!!.add("18")
        bvdData!!.add("19")
        bvdData!!.add("20")
        bvdData!!.add("21")
        bvdData!!.add("22")
        bvdData!!.add("23")
        bvdData!!.add("24")
        bvdData!!.add("25")
        bvdData!!.add("26")
        bvdData!!.add("27")
        bvdData!!.add("28")
        bvdData!!.add("29")
        bvdData!!.add("30")
        bvdData!!.add("31")
        bvdData!!.add("32")
        bvdData!!.add("33")
        bvdData!!.add("34")
        bvdData!!.add("35")
        bvdData!!.add("36")
        bvdData!!.add("37")
        bvdData!!.add("38")
        bvdData!!.add("39")
        bvdData!!.add("40")

        return bvdData
    }

    private fun providePrescriptionGivenData(): List<String> {


        val readingSpectacleGivenData = ArrayList<String>()
//        readingSpectacleGivenData!!.add("select")
        readingSpectacleGivenData!!.add("Single Vision")
        readingSpectacleGivenData!!.add("Single Vision (HP)")
        readingSpectacleGivenData!!.add("Bifocal")
        readingSpectacleGivenData!!.add("Bifocal (HP)")

        return readingSpectacleGivenData
    }

    private fun provideReadingGivenData(): List<String> {
        val nearVisionSpectacleGivenData = ArrayList<String>()
//        nearVisionSpectacleGivenData!!.add("select")
        nearVisionSpectacleGivenData!!.add("+0.25")
        nearVisionSpectacleGivenData!!.add("+0.5")
        nearVisionSpectacleGivenData!!.add("+0.75")
        nearVisionSpectacleGivenData!!.add("+1.0")
        nearVisionSpectacleGivenData!!.add("+1.25")
        nearVisionSpectacleGivenData!!.add("+1.5")
        nearVisionSpectacleGivenData!!.add("+1.75")
        nearVisionSpectacleGivenData!!.add("+2.0")
        nearVisionSpectacleGivenData!!.add("+2.25")
        nearVisionSpectacleGivenData!!.add("+2.5")
        nearVisionSpectacleGivenData!!.add("+2.75")
        nearVisionSpectacleGivenData!!.add("+3.0")
        nearVisionSpectacleGivenData!!.add("+3.25")
        nearVisionSpectacleGivenData!!.add("+3.5")
        nearVisionSpectacleGivenData!!.add("+3.75")
        nearVisionSpectacleGivenData!!.add("+4.0")

        return nearVisionSpectacleGivenData

    }

    private fun inflateBottomSheet(
        title: String,
        commonList: List<String>,
        etEditText: TextInputEditText
    ) {
        val items = ArrayList<SearchAbleList>()
        // diagnosisList.add(DiagnosisType(0,"Other"))
        for (i in commonList.indices) {
            items.add(SearchAbleList(i, commonList[i]))
        }
        val dialog = SingleSelectBottomSheetDialogFragment(
            this@RefractiveErrorFormActivity,
            items,
            title,
            0,
            true
        ) { selectedValue ->
            val selectedValue = commonList[selectedValue.position]

            etEditText.setText(selectedValue)



            setNearDistantClickLogic(ndClickedId)
            if (checkDataForGlassesGiven()) {
                binding.cvSpectacleDistribution.visibility = View.VISIBLE
            } else {
                binding.cvSpectacleDistribution.visibility = View.GONE
            }

        }

        dialog.show(supportFragmentManager, "SingleSelectBottomSheetDialogFragment")
    }

    private fun setNearDistantClickLogic(clickedId: Int) {
        // Recalculate both flags from scratch
        isDistantVision = checkIfDistantVision()
        isNearVision = checkIfNearVision()

        showLogicForSpectacleDistribution()
    }


    private fun checkIfDistantVision(): Boolean {
        return (!binding.etRDVSphere.text.isNullOrEmpty() && binding.etRDVSphere.text.toString() != "Sphere") ||
                (!binding.etRDVCylinder.text.isNullOrEmpty() && binding.etRDVCylinder.text.toString() != "Cylinder") ||
                (!binding.etRDVAxis.text.isNullOrEmpty() && binding.etRDVAxis.text.toString() != "Axis") ||
                (!binding.etRDVVisualActivity.text.isNullOrEmpty() && binding.etRDVVisualActivity.text.toString() != "Visual Acuity") ||
                (!binding.etLDVSphere.text.isNullOrEmpty() && binding.etLDVSphere.text.toString() != "Sphere") ||
                (!binding.etLDVCylinder.text.isNullOrEmpty() && binding.etLDVCylinder.text.toString() != "Cylinder") ||
                (!binding.etLDVAxis.text.isNullOrEmpty() && binding.etLDVAxis.text.toString() != "Axis") ||
                (!binding.etLDVVisualActivity.text.isNullOrEmpty() && binding.etLDVVisualActivity.text.toString() != "Visual Acuity")
    }

    private fun checkIfNearVision(): Boolean {
        return (!binding.etRNVSphere.text.isNullOrEmpty() && binding.etRNVSphere.text.toString() != "Sphere") ||
                (!binding.etRNVCylinder.text.isNullOrEmpty() && binding.etRNVCylinder.text.toString() != "Cylinder") ||
                (!binding.etRNVAxis.text.isNullOrEmpty() && binding.etRNVAxis.text.toString() != "Axis") ||
                (!binding.etRNVReadingAddition.text.isNullOrEmpty() && binding.etRNVReadingAddition.text.toString() != "Reading Addition") ||
                (!binding.etRNVVisualActivity.text.isNullOrEmpty() && binding.etRNVVisualActivity.text.toString() != "Visual Acuity") ||
                (!binding.etLNVSphere.text.isNullOrEmpty() && binding.etLNVSphere.text.toString() != "Sphere") ||
                (!binding.etLNVCylinder.text.isNullOrEmpty() && binding.etLNVCylinder.text.toString() != "Cylinder") ||
                (!binding.etLNVAxis.text.isNullOrEmpty() && binding.etLNVAxis.text.toString() != "Axis") ||
                (!binding.etLNVReadingAddition.text.isNullOrEmpty() && binding.etLNVReadingAddition.text.toString() != "Reading Addition") ||
                (!binding.etLNVVisualActivity.text.isNullOrEmpty() && binding.etLNVVisualActivity.text.toString() != "Visual Acuity")
    }

    private fun showLogicForSpectacleDistribution() {
        isDistantVision = checkIfDistantVision()
        isNearVision = checkIfNearVision()

        if (isDistantVision) {
            // Show Prescription Glasses
            binding.cbReadingSpectaclesGiven.isChecked = false
            binding.etReadingGlassesGiven.setText("")

            binding.cbPrescriptionGiven.visibility = View.VISIBLE
            binding.etlPrescriptionGiven.visibility = View.VISIBLE
            binding.etlFrameCode.visibility = View.VISIBLE
            binding.etlFrameColor.visibility = View.VISIBLE

            binding.cbReadingSpectaclesGiven.visibility = View.GONE
            binding.etlReadingGlassesGiven.visibility = View.GONE

        } else if (isNearVision) {
            // Show Reading Glasses
            binding.cbPrescriptionGiven.isChecked = false
            binding.etPrescriptionGiven.setText("")

            binding.cbPrescriptionGiven.visibility = View.GONE
            binding.etlPrescriptionGiven.visibility = View.GONE
            binding.etlFrameCode.visibility = View.GONE
            binding.etlFrameColor.visibility = View.GONE

            binding.cbReadingSpectaclesGiven.visibility = View.VISIBLE
            binding.etlReadingGlassesGiven.visibility = View.VISIBLE
        } else {
            // Hide both
            binding.cbPrescriptionGiven.isChecked = false
            binding.etPrescriptionGiven.setText("")
            binding.cbReadingSpectaclesGiven.isChecked = false
            binding.etReadingGlassesGiven.setText("")

            binding.cbPrescriptionGiven.visibility = View.GONE
            binding.etlPrescriptionGiven.visibility = View.GONE
            binding.etlFrameCode.visibility = View.GONE
            binding.etlFrameColor.visibility = View.GONE
            binding.cbReadingSpectaclesGiven.visibility = View.GONE
            binding.etlReadingGlassesGiven.visibility = View.GONE
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true
        //Eye Data validation
        if (binding.etRDVSphere.text.isNullOrEmpty() || binding.etRDVSphere.text.toString() == "Sphere") {
            if (binding.etRDVCylinder.text.isNullOrEmpty() || binding.etRDVCylinder.text.toString() == "Cylinder") {
                if (binding.etRDVAxis.text.isNullOrEmpty() || binding.etRDVAxis.text.toString() == "Axis") {
                        if (binding.etRDVVisualActivity.text.isNullOrEmpty() || binding.etRDVVisualActivity.text.toString() == "Visual Acuity") {
                            if (binding.etRNVSphere.text.isNullOrEmpty() || binding.etRNVSphere.text.toString() == "Sphere") {
                                if (binding.etRNVCylinder.text.isNullOrEmpty() || binding.etRNVCylinder.text.toString() == "Cylinder") {
                                    if (binding.etRNVAxis.text.isNullOrEmpty() || binding.etRNVAxis.text.toString() == "Axis") {
                                        if (binding.etRNVReadingAddition.text.isNullOrEmpty() || binding.etRNVReadingAddition.text.toString() == "Reading Addition") {
                                            if (binding.etRNVVisualActivity.text.isNullOrEmpty() || binding.etRNVVisualActivity.text.toString() == "Visual Acuity") {
                                                if (binding.etRightPrism.text.isNullOrEmpty() || binding.etRightPrism.text.toString() == "Prism") {
                                                    if (binding.etRightPrismBase.text.isNullOrEmpty()) {

                                                        // Left Eye Validation
                                                        if (binding.etLDVSphere.text.isNullOrEmpty() || binding.etLDVSphere.text.toString() == "Sphere") {
                                                            if (binding.etLDVCylinder.text.isNullOrEmpty() || binding.etLDVCylinder.text.toString() == "Cylinder") {
                                                                if (binding.etLDVAxis.text.isNullOrEmpty() || binding.etLDVAxis.text.toString() == "Axis") {
                                                                    if (binding.etLDVVisualActivity.text.isNullOrEmpty() || binding.etLDVVisualActivity.text.toString() == "Visual Acuity") {
                                                                        if (binding.etLNVSphere.text.isNullOrEmpty() || binding.etLNVSphere.text.toString() == "Sphere") {
                                                                            if (binding.etLNVCylinder.text.isNullOrEmpty() || binding.etLNVCylinder.text.toString() == "Cylinder") {
                                                                                if (binding.etLNVAxis.text.isNullOrEmpty() || binding.etLNVAxis.text.toString() == "Axis") {
                                                                                    if (binding.etLNVReadingAddition.text.isNullOrEmpty() || binding.etLNVReadingAddition.text.toString() == "Reading Addition") {
                                                                                        if (binding.etLNVVisualActivity.text.isNullOrEmpty() || binding.etLNVVisualActivity.text.toString() == "Visual Acuity") {
                                                                                            if (binding.etLeftPrism.text.isNullOrEmpty() || binding.etLeftPrism.text.toString() == "Prism") {
                                                                                                if (binding.etLeftPrismBase.text.isNullOrEmpty()) {

                                                                                                    // Other details validation
                                                                                                    if (binding.etPupillaryDistance.text.isNullOrEmpty() || binding.etPupillaryDistance.text.toString() == "Pupillary Distance(mm)") {
                                                                                                        if (binding.etBilateralVertexDistance.text.isNullOrEmpty() || binding.etBilateralVertexDistance.text.toString() == "Bilateral Vertex Distance(mm)") {

                                                                                                            isValid = false
                                                                                                            Utility.errorToast(
                                                                                                                this@RefractiveErrorFormActivity,
                                                                                                                "At least one field is needed"
                                                                                                            )
                                                                                                            return isValid
                                                                                                        }
                                                                                                    }

                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            }
        }


        //spectacles given validation
        if (binding.cbPrescriptionGiven.isChecked) {
            if (binding.etPrescriptionGiven.text.isNullOrEmpty()) {
                isValid = false
                Utility.errorToast(this@RefractiveErrorFormActivity, "Select Prescription Ordered")
                return isValid
            }

            if (binding.etFrameCode.text.isNullOrEmpty()) {
                isValid = false
                Utility.errorToast(this@RefractiveErrorFormActivity, "Enter Frame Code")
                return isValid
            }

            if (binding.etFrameColor.text.isNullOrEmpty()) {
                isValid = false
                Utility.errorToast(this@RefractiveErrorFormActivity, "Enter Frame Color")
                return isValid
            }
        }

        if (binding.cbReadingSpectaclesGiven.isChecked) {
            if (binding.etReadingGlassesGiven.text.isNullOrEmpty()) {
                isValid = false
                Utility.errorToast(this@RefractiveErrorFormActivity, "Select Reading Glasses Given")
                return isValid
            }
        }

        if (!binding.etPrescriptionGiven.text.isNullOrEmpty()) {
            if (!binding.cbPrescriptionGiven.isChecked) {
                isValid = false
                Utility.errorToast(this@RefractiveErrorFormActivity, "Check Prescription Ordered")
                return isValid
            }
        }

        if (!binding.etReadingGlassesGiven.text.isNullOrEmpty()) {
            if (!binding.cbReadingSpectaclesGiven.isChecked) {
                isValid = false
                Utility.errorToast(this@RefractiveErrorFormActivity, "Check Reading Glasses Given")
                return isValid
            }
        }

        return isValid
    }

    private fun saveRefractiveErrorForm() {
        //Right Eye Data
        // Distant Vision
        rdvSphere = if (binding.etRDVSphere.text?.toString() == "Sphere") "" else binding.etRDVSphere.text?.toString() ?: ""
        rdvCylinder = if (binding.etRDVCylinder.text?.toString() == "Cylinder") "" else binding.etRDVCylinder.text?.toString() ?: ""
        rdvAxis = if (binding.etRDVAxis.text?.toString() == "Axis") "" else binding.etRDVAxis.text?.toString() ?: ""
        rdvVisualActivity = if (binding.etRDVVisualActivity.text?.toString() == "Visual Acuity") "" else binding.etRDVVisualActivity.text?.toString() ?: ""
        rdvVisualActivityUnit = if (binding.etRDVUnit.text?.toString() == "Acuity Unit") "" else binding.etRDVUnit.text?.toString() ?: ""

        // Distant Vision
        rnvSphere = if (binding.etRNVSphere.text?.toString() == "Sphere") "" else binding.etRNVSphere.text?.toString() ?: ""
        rnvCylinder = if (binding.etRNVCylinder.text?.toString() == "Cylinder") "" else binding.etRNVCylinder.text?.toString() ?: ""
        rnvAxis = if (binding.etRNVAxis.text?.toString() == "Axis") "" else binding.etRNVAxis.text?.toString() ?: ""
        rnvReadingAddition = if (binding.etRNVReadingAddition.text?.toString() == "Reading Addition") "" else binding.etRNVReadingAddition.text?.toString() ?: ""
        rnvVisualActivity = if (binding.etRNVVisualActivity.text?.toString() == "Visual Acuity") "" else binding.etRNVVisualActivity.text?.toString() ?: ""

        rightPrism = if (binding.etRightPrism.text?.toString() == "Prism") "" else binding.etRightPrism.text?.toString() ?: ""
        rightBase = binding.etRightPrismBase.text?.toString() ?: ""
        rightRemarks = binding.etRightPrismRemarks.text?.toString() ?: ""

        //Left Eye Data
        // Distant Vision
        ldvSphere = if (binding.etLDVSphere.text?.toString() == "Sphere") "" else binding.etLDVSphere.text?.toString() ?: ""
        ldvCylinder = if (binding.etLDVCylinder.text?.toString() == "Cylinder") "" else binding.etLDVCylinder.text?.toString() ?: ""
        ldvAxis = if (binding.etLDVAxis.text?.toString() == "Axis") "" else binding.etLDVAxis.text?.toString() ?: ""
        ldvVisualActivity = if (binding.etLDVVisualActivity.text?.toString() == "Visual Acuity") "" else binding.etLDVVisualActivity.text?.toString() ?: ""
        ldvVisualActivityUnit = if (binding.etLDVUnit.text?.toString() == "Acuity Unit") "" else binding.etLDVUnit.text?.toString() ?: ""

        // Near Vision
        lnvSphere = if (binding.etLNVSphere.text?.toString() == "Sphere") "" else binding.etLNVSphere.text?.toString() ?: ""
        lnvCylinder = if (binding.etLNVCylinder.text?.toString() == "Cylinder") "" else binding.etLNVCylinder.text?.toString() ?: ""
        lnvAxis = if (binding.etLNVAxis.text?.toString() == "Axis") "" else binding.etLNVAxis.text?.toString() ?: ""
        lnvReadingAddition = if (binding.etLNVReadingAddition.text?.toString() == "Reading Addition") "" else binding.etLNVReadingAddition.text?.toString() ?: ""
        lnvVisualActivity = if (binding.etLNVVisualActivity.text?.toString() == "Visual Acuity") "" else binding.etLNVVisualActivity.text?.toString() ?: ""

        leftPrism = if (binding.etLeftPrism.text?.toString() == "Prism") "" else binding.etLeftPrism.text?.toString() ?: ""
        leftBase = binding.etLeftPrismBase.text?.toString() ?: ""
        leftRemarks = binding.etLeftPrismRemarks.text?.toString() ?: ""

        //other details
        pupillaryDistance = if (binding.etPupillaryDistance.text?.toString() == "Pupillary Distance(mm)") "" else binding.etPupillaryDistance.text?.toString() ?: ""
        bilateralVertexDistance = if (binding.etBilateralVertexDistance.text?.toString() == "Bilateral Vertex Distance(mm)") "" else binding.etBilateralVertexDistance.text?.toString() ?: ""
        commonRemarks = binding.etRemarks.text?.toString() ?: ""
        fundusNotes = binding.etFundusNotes.text?.toString() ?: ""

        //spectacle given details
        isPrescriptionGlassesOrdered = binding.cbPrescriptionGiven.isChecked
        prescriptionGlassesData = binding.etPrescriptionGiven.text?.toString() ?: ""
        frameCode = binding.etFrameCode.text.toString() ?: ""
        frameColor = binding.etFrameColor.text.toString() ?: ""

        isReadingGlassesGiven = binding.cbReadingSpectaclesGiven.isChecked
        readingGlassesData = binding.etReadingGlassesGiven.text?.toString() ?: ""

        val refractiveErrorForm = RefractiveError(
            _id = intentFormId,
            camp_id = campId,
            createdDate = ConstantsApp.getCurrentDate(),
            fundus_notes = fundusNotes,
            is_given = isReadingGlassesGiven,
            is_ordered = isPrescriptionGlassesOrdered,
            patient_id = patientId,
            re_bvd = bilateralVertexDistance,
            re_distant_vision_axis_left = ldvAxis,
            re_distant_vision_axis_right = rdvAxis,
            re_distant_vision_cylinder_left = ldvCylinder,
            re_distant_vision_cylinder_right = rdvCylinder,
            re_distant_vision_left = ldvVisualActivity,
            re_distant_vision_right = rdvVisualActivity,
            re_distant_vision_sphere_left = ldvSphere,
            re_distant_vision_sphere_right = rdvSphere,
            re_distant_vision_unit_left = ldvVisualActivityUnit,
            re_distant_vision_unit_right = rdvVisualActivityUnit,
            re_near_vision_axis_left = lnvAxis,
            re_near_vision_axis_right = rnvAxis,
            re_near_vision_cylinder_left = lnvCylinder,
            re_near_vision_cylinder_right = rnvCylinder,
            re_near_vision_left = lnvVisualActivity,
            re_near_vision_right = rnvVisualActivity,
            re_near_vision_sphere_left = lnvSphere,
            re_near_vision_sphere_right = rnvSphere,
            re_prism_left = leftPrism,
            re_prism_right = rightPrism,
            re_prism_unit_left = leftBase,
            re_prism_unit_right = rightBase,
            re_pupipllary_distance = pupillaryDistance,
            re_reading_addition_left = lnvReadingAddition,
            re_reading_addition_left_details = lnvReadingAddition,
            re_reading_addition_right = rnvReadingAddition,
            re_reading_addition_right_details = rnvReadingAddition,
            re_remark_left = leftRemarks,
            re_remark_right = rightRemarks,
            re_remarks = commonRemarks,
            reading_glass = readingGlassesData,
            userId = userId,
            presc_type = prescriptionGlassesData,
            frameCode = frameCode,
            frameColor = frameColor,
            isSyn = 0
        )


        sessionManager.saveRefractiveError(refractiveErrorForm)
        refractiveViewModel.insertRefractiveForm(refractiveErrorForm)


    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun checkDataForGlassesGiven(): Boolean {
        if (binding.etRDVSphere.text.isNullOrEmpty() || binding.etRDVSphere.text.toString() == "Sphere"){
            if (binding.etRDVCylinder.text.isNullOrEmpty() || binding.etRDVCylinder.text.toString() == "Cylinder") {
                if (binding.etRDVAxis.text.isNullOrEmpty() || binding.etRDVAxis.text.toString() == "Axis") {
                    if (binding.etRDVVisualActivity.text.isNullOrEmpty()) {
                        if (binding.etRNVSphere.text.isNullOrEmpty() || binding.etRNVSphere.text.toString() == "Sphere") {
                            if (binding.etRNVCylinder.text.isNullOrEmpty() || binding.etRNVCylinder.text.toString() == "Cylinder") {
                                if (binding.etRNVAxis.text.isNullOrEmpty() || binding.etRNVAxis.text.toString() == "Axis") {
                                    if (binding.etRNVReadingAddition.text.isNullOrEmpty() || binding.etRNVReadingAddition.text.toString() == "Reading Addition") {
                                        if (binding.etRNVVisualActivity.text.isNullOrEmpty() || binding.etRNVVisualActivity.text.toString() == "Visual Acuity") {
                                            if (binding.etRightPrism.text.isNullOrEmpty() || binding.etRightPrism.text.toString() == "Prism") {
                                                if (binding.etRightPrismBase.text.isNullOrEmpty()) {
                                                    //Left Eye Validation
                                                    if (binding.etLDVSphere.text.isNullOrEmpty() || binding.etLDVSphere.text.toString() == "Sphere") {
                                                        if (binding.etLDVCylinder.text.isNullOrEmpty() || binding.etLDVCylinder.text.toString() == "Cylinder") {
                                                            if (binding.etLDVAxis.text.isNullOrEmpty() || binding.etLDVAxis.text.toString() == "Axis") {
                                                                if (binding.etLDVVisualActivity.text.isNullOrEmpty()) {
                                                                    if (binding.etLNVSphere.text.isNullOrEmpty()|| binding.etLNVSphere.text.toString() == "Sphere") {
                                                                        if (binding.etLNVCylinder.text.isNullOrEmpty()|| binding.etLNVCylinder.text.toString() == "Cylinder") {
                                                                            if (binding.etLNVAxis.text.isNullOrEmpty()|| binding.etLNVAxis.text.toString() == "Axis") {
                                                                                if (binding.etLNVReadingAddition.text.isNullOrEmpty()|| binding.etLNVReadingAddition.text.toString() == "Reading Addition") {
                                                                                    if (binding.etLNVVisualActivity.text.isNullOrEmpty()|| binding.etLNVVisualActivity.text.toString() == "Visual Acuity") {
                                                                                        if (binding.etLeftPrism.text.isNullOrEmpty() || binding.etLeftPrism.text?.toString() == "Prism") {
                                                                                            if (binding.etLeftPrismBase.text.isNullOrEmpty()) {
                                                                                                //other details validation
                                                                                                if (binding.etPupillaryDistance.text.isNullOrEmpty() || binding.etPupillaryDistance.text?.toString() == "Pupillary Distance(mm)") {
                                                                                                    if (binding.etBilateralVertexDistance.text.isNullOrEmpty() || binding.etBilateralVertexDistance.text?.toString() == "Bilateral Vertex Distance(mm)") {
                                                                                                        return false
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true
    }

    private fun setPatientData() {
        // Use Gson to parse the JSON string
        intentDecodeText = sessionManager.getPatientData().toString()
        val gson = Gson()
        val patientData = gson.fromJson(intentDecodeText, PatientData::class.java)


        // Access the values
        patientFname = patientData.patientFname
        patientLname = patientData.patientLname
        patientAge = patientData.patientAge
        patientID = patientData.patientId
        campID = patientData.camp_id
        patientGender = patientData.patientGen
        camp = patientData.location
        ageUnit = patientData.AgeUnit
    }

    private fun allowClickableEditText(isEditable: Boolean) {


        binding.cbPrescriptionGiven.setOnCheckedChangeListener { _, isChecked ->
            if (isEditable) {
                binding.cbPrescriptionGiven.isChecked = isChecked
                isPrescriptionGlassesOrdered = isChecked
            } else {
                binding.cbPrescriptionGiven.isChecked = !isChecked
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }

        }



        binding.cbReadingSpectaclesGiven.setOnCheckedChangeListener { _, isChecked ->
            if (isEditable) {
                binding.cbReadingSpectaclesGiven.isChecked = isChecked
                isReadingGlassesGiven = isChecked
            } else {
                binding.cbReadingSpectaclesGiven.isChecked = !isChecked
                Utility.warningToast(this@RefractiveErrorFormActivity, "cannot Edit")
            }
        }


        binding.etRightPrismRemarks.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@RefractiveErrorFormActivity, "Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }

        binding.etLeftPrismRemarks.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@RefractiveErrorFormActivity, "Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }

        binding.etRemarks.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@RefractiveErrorFormActivity, "Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }

        binding.etFundusNotes.setOnTouchListener { _, event ->
            if (!isEditable) {
                // If the condition is false, show a toast and consume the touch event
                Utility.warningToast(this@RefractiveErrorFormActivity, "Not Editable")
                true // Consume the touch event, preventing further actions
            } else {
                false // Allow the touch event to proceed
            }
        }


    }

    private fun setUpFormData(form: RefractiveError) {

        //Right Eye - Setting Data
        binding.etRDVSphere.setText(form.re_distant_vision_sphere_right)

        binding.etRDVCylinder.setText(form.re_distant_vision_cylinder_right)

        binding.etRDVAxis.setText(form.re_distant_vision_axis_right)

        binding.etRDVUnit.setText(form.re_distant_vision_unit_right)

        binding.etRDVVisualActivity.setText(form.re_distant_vision_right)

        binding.etRNVSphere.setText(form.re_near_vision_sphere_right)

        binding.etRNVCylinder.setText(form.re_near_vision_cylinder_right)

        binding.etRNVAxis.setText(form.re_near_vision_axis_right)

        binding.etRNVReadingAddition.setText(form.re_reading_addition_right)

        binding.etRNVVisualActivity.setText(form.re_near_vision_right)

        binding.etRightPrism.setText(form.re_prism_right)

        binding.etRightPrismBase.setText(form.re_prism_unit_right)

        binding.etRightPrismRemarks.setText(form.re_remark_right)

        //Left Eye - Setting Data
        binding.etLDVSphere.setText(form.re_distant_vision_sphere_left)

        binding.etLDVCylinder.setText(form.re_distant_vision_cylinder_left)

        binding.etLDVAxis.setText(form.re_distant_vision_axis_left)

        binding.etLDVUnit.setText(form.re_distant_vision_unit_left)

        binding.etLDVVisualActivity.setText(form.re_distant_vision_left)

        binding.etLNVSphere.setText(form.re_near_vision_sphere_left)

        binding.etLNVCylinder.setText(form.re_near_vision_cylinder_left)

        binding.etLNVAxis.setText(form.re_near_vision_axis_left)

        binding.etLNVReadingAddition.setText(form.re_reading_addition_left)

        binding.etLNVVisualActivity.setText(form.re_near_vision_left)

        binding.etLeftPrism.setText(form.re_prism_left)

        binding.etLeftPrismBase.setText(form.re_prism_unit_left)

        binding.etLeftPrismRemarks.setText(form.re_remark_left)

        //Common Details - setting data

        binding.etPupillaryDistance.setText(form.re_pupipllary_distance)

        binding.etBilateralVertexDistance.setText(form.re_bvd)

        binding.etRemarks.setText(form.re_remarks)

        binding.etFundusNotes.setText(form.fundus_notes)

        //Spectacles Distribution - setting data
        if (!form.reading_glass.isNullOrEmpty()) {
            binding.cvSpectacleDistribution.visibility = View.VISIBLE

            binding.cbReadingSpectaclesGiven.visibility = View.VISIBLE
            binding.etlReadingGlassesGiven.visibility = View.VISIBLE

            binding.cbPrescriptionGiven.visibility = View.GONE
            binding.etlPrescriptionGiven.visibility = View.GONE
            binding.etlFrameCode.visibility = View.GONE
            binding.etlFrameColor.visibility = View.GONE

            binding.cbReadingSpectaclesGiven.isChecked = form.is_given
            binding.etReadingGlassesGiven.setText(form.reading_glass)
        } else if (!form.presc_type.isNullOrEmpty()) {
            binding.cvSpectacleDistribution.visibility = View.VISIBLE


            binding.cbPrescriptionGiven.visibility = View.VISIBLE
            binding.etlPrescriptionGiven.visibility = View.VISIBLE
            binding.etlFrameCode.visibility = View.VISIBLE
            binding.etlFrameColor.visibility = View.VISIBLE

            binding.cbReadingSpectaclesGiven.visibility = View.GONE
            binding.etlReadingGlassesGiven.visibility = View.GONE


            binding.cbPrescriptionGiven.isChecked = form.is_ordered
            binding.etPrescriptionGiven.setText(form.presc_type)
            binding.etFrameCode.setText(form.frameCode)
            binding.etFrameColor.setText(form.frameColor)

        }


    }

    private fun onFormEditClick() {
        canEdit = true
        binding.btnSubmit.visibility = View.VISIBLE
        binding.btnEdit.visibility = View.GONE

    }
}