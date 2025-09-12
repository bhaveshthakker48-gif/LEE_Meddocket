package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputLayout
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.SearchAbleList
import org.impactindiafoundation.iifllemeddocket.Utils.SingleSelectBottomSheetDialogFragment
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.model.Measurement
import org.impactindiafoundation.iifllemeddocket.architecture.model.MeasurementPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel
import org.impactindiafoundation.iifllemeddocket.databinding.ItemOrthosisFormBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrthosisFormAdapter(
    var isEditable: Boolean,
    val context: Context,
    var data: List<OrthosisPatientData>,
    val layoutInflater: LayoutInflater,
    private val event: OrthosisFormClickListener,
    val fragmentManager: FragmentManager,
    val orthosisTypeList: List<OrthosisType>,
    val localUser: UserModel
) :
    RecyclerView.Adapter<OrthosisFormAdapter.OrthosisFormViewHolder>() {
    private var isAmputee = false
    private var binding: ItemOrthosisFormBinding? = null

    inner class OrthosisFormViewHolder(
        private var binding: ItemOrthosisFormBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
//            this@OrthosisFormAdapter.binding = binding
            val content = data[position]

//            setUpMeasurementRecyclerview(binding, content.patientOrthosisMeasurements, position)
//            setUpOrthosisImageRecyclerview(binding, content.orthosisImageList, position,0)
            hideAllKeypad(binding)

            binding.apply {

                setColorForAllEditText(binding)

                if (!content.patientOrthosisMeasurements.isNullOrEmpty()) {
                    if (content.orthosis.name == "Other") {
                        setUpOtherMeasurementRecyclerView(
                            isEditable,
                            binding,
                            content.patientOrthosisMeasurements,
                            position
                        )
                    } else {
                        setUpMeasurementRecyclerview(
                            isEditable,
                            binding,
                            content.patientOrthosisMeasurements,
                            position
                        )

                    }
                }

                if (isAmputee) {
                    binding!!.etlAmputationDate.visibility = View.VISIBLE
                    binding!!.etlAmputationSide.visibility = View.VISIBLE
                    binding!!.etlAmputationLevel.visibility = View.VISIBLE
                    binding!!.etlAmputationCause.visibility = View.VISIBLE

                }
//                else if (!content.amputationDate.isNullOrEmpty())
                else
                {

                    etlAmputationDate.visibility = View.GONE
                    etlAmputationSide.visibility = View.GONE
                    etlAmputationLevel.visibility = View.GONE
                    etlAmputationCause.visibility = View.GONE

                    etAmputationDate.setText("")
                    etAmputationSide.setText("")
                    etAmputationLevel.setText("")
                    etAmputationCause.setText("")

                    if (!content.amputationDate.isNullOrEmpty()) {
                        binding.etlAmputationDate.visibility = View.VISIBLE
                        binding.etAmputationDate.setText(convertDateFormatFromData(content.amputationDate))

                    }

                    if (!content.amputationSide.isNullOrEmpty()) {
                        binding.etlAmputationSide.visibility = View.VISIBLE
                        binding.etAmputationSide.setText(content.amputationSide)
                    } else {
                        binding.etlAmputationSide.visibility = View.GONE
                    }

                    if (!content.amputationLevel.isNullOrEmpty()) {
                        binding.etlAmputationLevel.visibility = View.VISIBLE
                        binding.etAmputationLevel.setText(content.amputationLevel)
                    } else {
                        binding.etlAmputationLevel.visibility = View.GONE

                    }


                    if (!content.amputationCause.isNullOrEmpty()) {
                        binding.etlAmputationCause.visibility = View.VISIBLE
                        binding.etAmputationCause.setText(content.amputationCause)
                    } else {
                        binding.etlAmputationCause.visibility = View.GONE

                    }

                    if (!content.fit_properly.isNullOrEmpty()) {
                        binding.etlFittingStatus.visibility = View.VISIBLE

                        binding.etFittingStatus.setText(content.fit_properly)

                    } else {
                        binding.etlFittingStatus.visibility = View.GONE

                    }
                    if (!content.fit_properly_reason.isNullOrEmpty()) {
                        binding.etlFittingFeedback.visibility = View.VISIBLE
                        binding.etFittingFeedback.setText(content.fit_properly_reason)

                    } else {
                        binding.etlFittingFeedback.visibility = View.GONE

                    }

                }
//                else {
//                    etlAmputationDate.visibility = View.GONE
//                    etlAmputationSide.visibility = View.GONE
//                    etlAmputationLevel.visibility = View.GONE
//                    etlAmputationCause.visibility = View.GONE
//
//                    etAmputationDate.setText("")
//                    etAmputationSide.setText("")
//                    etAmputationLevel.setText("")
//                    etAmputationCause.setText("")
//                }
//                setUpMeasurementRecyclerview(binding, content.patientOrthosisMeasurements, position)
//               setUpOrthosisImageRecyclerview(binding, content.orthosisImageList, position, content.orthoFormId)

                val orthoImageList =
                    content.orthosisImageList.filter { it.orthosisFormId == content.orthoFormId }

                if (!orthoImageList.isNullOrEmpty()) {
                    binding.llOrthosisImages.visibility = View.VISIBLE


                    if (orthoImageList.size >= 1) {

                        if (!orthoImageList[0].images.isNullOrEmpty()) {
                            binding.clOrthoImage1.visibility = View.VISIBLE
                            Glide.with(context)
                                .load(orthoImageList[0].images)
                                .placeholder(R.drawable.img_placeholder)
                                .error(R.drawable.img_placeholder)
                                .into(binding!!.ivOthoFile1)
                        } else {
                            binding.clOrthoImage1.visibility = View.GONE
                        }
                    } else {
                        binding.clOrthoImage1.visibility = View.GONE
                    }


                    if (orthoImageList.size >= 2) {
                        if (!orthoImageList[1].images.isNullOrEmpty()) {
                            binding.clOrthoImage2.visibility = View.VISIBLE
                            binding.cvOrthoImage2.visibility = View.VISIBLE
                            Glide.with(context)
                                .load(orthoImageList[1].images)
                                .placeholder(R.drawable.img_placeholder)
                                .error(R.drawable.img_placeholder)
                                .into(binding!!.ivOthoFile2)
                        } else {
                            binding.clOrthoImage2.visibility = View.GONE
                            binding.cvOrthoImage2.visibility = View.GONE
                        }
                    } else {
                        binding.clOrthoImage2.visibility = View.GONE
                        binding.cvOrthoImage2.visibility = View.GONE
                    }


                    if (orthoImageList.size >= 3) {
                        if (!orthoImageList[2].images.isNullOrEmpty()) {
                            binding.clOrthoImage3.visibility = View.VISIBLE
                            Glide.with(context)
                                .load(orthoImageList[2].images)
                                .placeholder(R.drawable.img_placeholder)
                                .error(R.drawable.img_placeholder)
                                .into(binding!!.ivOthoFile3)
                        } else {
                            binding.clOrthoImage3.visibility = View.GONE
                        }
                    } else {
                        binding.clOrthoImage3.visibility = View.GONE
                    }


                    if (orthoImageList.size == 4) {
                        if (!orthoImageList[3].images.isNullOrEmpty()) {
                            binding.clOrthoImage4.visibility = View.VISIBLE
                            Glide.with(context)
                                .load(orthoImageList[3].images)
                                .placeholder(R.drawable.img_placeholder)
                                .error(R.drawable.img_placeholder)
                                .into(binding!!.ivOthoFile4)
                        } else {
                            binding.clOrthoImage4.visibility = View.GONE
                        }
                    } else {
                        binding.clOrthoImage4.visibility = View.GONE
                    }


                    binding.ivOrthoRemove1.setOnClickListener {
                        event.onOrthosisImageRemove(position, 0, orthoImageList[0].images)
                    }

                    binding.ivOrthoRemove2.setOnClickListener {
                        event.onOrthosisImageRemove(position, 0, orthoImageList[1].images)
                    }

                    binding.ivOrthoRemove3.setOnClickListener {
                        event.onOrthosisImageRemove(position, 0, orthoImageList[2].images)
                    }

                    binding.ivOrthoRemove4.setOnClickListener {
                        event.onOrthosisImageRemove(position, 0, orthoImageList[3].images)
                    }
                } else {
                    binding.llOrthosisImages.visibility = View.GONE
                }

                if (content.orthosis.name == "Other"){
                    binding.etlOtherOrthosis.visibility = View.VISIBLE
                    binding.etOrthosisType.setText(content.orthosis.name)
                    binding.etOtherOrthosis.setText(content.otherOrthosis)
                }
                else{
                    binding.etlOtherOrthosis.visibility = View.GONE
                    binding.etOrthosisType.setText(content.orthosis.name)
                    binding.etOtherOrthosis.setText("")

                }
                binding.etOrthosisStatus.setText(content.status)
                binding.etExaminationDate.setText(getCurrentDate())




                binding.tvOrthosisType.setOnClickListener {
                    var isArrowDown = binding.llOrthosisDetails.isVisible
                    //for up arrow logic for degree's for rotation
                    val fromDegree = if (isArrowDown) 0f else 180f
                    val toDegree = if (isArrowDown) 180f else 360f

                    //for down arrow logic for degree's for rotation
//                    val fromDegree = if (isArrowDown) 180f else 0f
//                    val toDegree = if (isArrowDown) 360f else 180f

                    val rotateAnimator = ObjectAnimator.ofFloat(
                        binding.ivArrowDown,
                        "rotation",
                        fromDegree,
                        toDegree
                    )
                    rotateAnimator.duration = 300 // duration of the animation in milliseconds
                    rotateAnimator.start()

                    if (isArrowDown) {
                        binding.llOrthosisDetails.visibility = View.GONE
                        binding.llMeasurementsDetails.visibility = View.GONE
                    } else {
                        binding.llOrthosisDetails.visibility = View.VISIBLE
                        binding.llMeasurementsDetails.visibility = View.VISIBLE
                    }
                    isArrowDown = !isArrowDown
                }

                binding.ivArrowDown.setOnClickListener {
                    var isArrowDown = binding.llOrthosisDetails.isVisible
                    val fromDegree = if (isArrowDown) 0f else 180f
                    val toDegree = if (isArrowDown) 180f else 360f

//                    val fromDegree = if (isArrowDown) 180f else 0f
//                    val toDegree = if (isArrowDown) 360f else 180f

                    val rotateAnimator = ObjectAnimator.ofFloat(
                        binding.ivArrowDown,
                        "rotation",
                        fromDegree,
                        toDegree
                    )
                    rotateAnimator.duration = 300 // duration of the animation in milliseconds
                    rotateAnimator.start()

                    if (isArrowDown) {
                        binding.llOrthosisDetails.visibility = View.GONE
                        binding.llMeasurementsDetails.visibility = View.GONE
                    } else {
                        binding.llOrthosisDetails.visibility = View.VISIBLE
                        binding.llMeasurementsDetails.visibility = View.VISIBLE
                    }
                    isArrowDown = !isArrowDown
                }

//                if (content.image != "") {
//                    binding.llOrthosisDetails.visibility = View.VISIBLE
//                    binding.llMeasurementsDetails.visibility = View.VISIBLE
//
//                    var isArrowDown = true
//                    binding.tvOrthosisType.setOnClickListener {
////                    val fromDegree = if (isArrowDown) 0f else 180f
////                    val toDegree = if (isArrowDown) 180f else 360f
//
//                        val fromDegree = if (isArrowDown) 180f else 0f
//                        val toDegree = if (isArrowDown) 360f else 180f
//
//                        val rotateAnimator = ObjectAnimator.ofFloat(
//                            binding.ivArrowDown,
//                            "rotation",
//                            fromDegree,
//                            toDegree
//                        )
//                        rotateAnimator.duration = 300 // duration of the animation in milliseconds
//                        rotateAnimator.start()
//
//                        if (isArrowDown) {
//                            binding.llOrthosisDetails.visibility = View.GONE
//                            binding.llMeasurementsDetails.visibility = View.GONE
//                        } else {
//                            binding.llOrthosisDetails.visibility = View.VISIBLE
//                            binding.llMeasurementsDetails.visibility = View.VISIBLE
//                        }
//                        isArrowDown = !isArrowDown
//                    }
//
//                    binding.ivArrowDown.setOnClickListener {
////                    val fromDegree = if (isArrowDown) 0f else 180f
////                    val toDegree = if (isArrowDown) 180f else 360f
//
//                        val fromDegree = if (isArrowDown) 180f else 0f
//                        val toDegree = if (isArrowDown) 360f else 180f
//
//                        val rotateAnimator = ObjectAnimator.ofFloat(
//                            binding.ivArrowDown,
//                            "rotation",
//                            fromDegree,
//                            toDegree
//                        )
//                        rotateAnimator.duration = 300 // duration of the animation in milliseconds
//                        rotateAnimator.start()
//
//                        if (isArrowDown) {
//                            binding.llOrthosisDetails.visibility = View.GONE
//                            binding.llMeasurementsDetails.visibility = View.GONE
//                        } else {
//                            binding.llOrthosisDetails.visibility = View.VISIBLE
//                            binding.llMeasurementsDetails.visibility = View.VISIBLE
//                        }
//                        isArrowDown = !isArrowDown
//                    }
//                } else {
//                    binding.llOrthosisDetails.visibility = View.GONE
//                    binding.llMeasurementsDetails.visibility = View.GONE
//
//                    var isArrowDown = false
//                    binding.tvOrthosisType.setOnClickListener {
////                    val fromDegree = if (isArrowDown) 0f else 180f
////                    val toDegree = if (isArrowDown) 180f else 360f
//
//                        val fromDegree = if (isArrowDown) 180f else 0f
//                        val toDegree = if (isArrowDown) 360f else 180f
//
//                        val rotateAnimator = ObjectAnimator.ofFloat(
//                            binding.ivArrowDown,
//                            "rotation",
//                            fromDegree,
//                            toDegree
//                        )
//                        rotateAnimator.duration = 300 // duration of the animation in milliseconds
//                        rotateAnimator.start()
//
//                        if (isArrowDown) {
//                            binding.llOrthosisDetails.visibility = View.GONE
//                            binding.llMeasurementsDetails.visibility = View.GONE
//                        } else {
//                            binding.llOrthosisDetails.visibility = View.VISIBLE
//                            binding.llMeasurementsDetails.visibility = View.VISIBLE
//                        }
//                        isArrowDown = !isArrowDown
//                    }
//
//                    binding.ivArrowDown.setOnClickListener {
////                    val fromDegree = if (isArrowDown) 0f else 180f
////                    val toDegree = if (isArrowDown) 180f else 360f
//
//                        val fromDegree = if (isArrowDown) 180f else 0f
//                        val toDegree = if (isArrowDown) 360f else 180f
//
//                        val rotateAnimator = ObjectAnimator.ofFloat(
//                            binding.ivArrowDown,
//                            "rotation",
//                            fromDegree,
//                            toDegree
//                        )
//                        rotateAnimator.duration = 300 // duration of the animation in milliseconds
//                        rotateAnimator.start()
//
//                        if (isArrowDown) {
//                            binding.llOrthosisDetails.visibility = View.GONE
//                            binding.llMeasurementsDetails.visibility = View.GONE
//                        } else {
//                            binding.llOrthosisDetails.visibility = View.VISIBLE
//                            binding.llMeasurementsDetails.visibility = View.VISIBLE
//                        }
//                        isArrowDown = !isArrowDown
//                    }
//                }


                if (content.orthosis.name == "") {
                    binding.tvOrthosisType.text = "(${position + 1}) Select Orthosis Type"
                } else {
                    binding.tvOrthosisType.text = "(${position + 1}) ${content.orthosis.name}"
                }


                //setting initial data of orthosis

//                if (content.image != "") {
//                    // val imageFile = File(content.image)
//                    binding.cvImage.visibility = View.VISIBLE
//
//                    Glide.with(context)
//                        .load(content.image).placeholder(R.drawable.img_placeholder)
//                        .error(R.drawable.img_placeholder)
//                        .into(binding.ivOrthosisImage)
//                } else {
//                    binding.cvImage.visibility = View.GONE
//                }


//                binding.llOrthosisDetails.visibility = View.GONE
//                binding.llMeasurementsDetails.visibility = View.GONE
//                var isArrowDown = false
//                binding.tvOrthosisType.setOnClickListener {
////                    val fromDegree = if (isArrowDown) 0f else 180f
////                    val toDegree = if (isArrowDown) 180f else 360f
//
//                    val fromDegree = if (isArrowDown) 180f else 0f
//                    val toDegree = if (isArrowDown) 360f else 180f
//
//                    val rotateAnimator = ObjectAnimator.ofFloat(
//                        binding.ivArrowDown,
//                        "rotation",
//                        fromDegree,
//                        toDegree
//                    )
//                    rotateAnimator.duration = 300 // duration of the animation in milliseconds
//                    rotateAnimator.start()
//
//                    if (isArrowDown) {
//                        binding.llOrthosisDetails.visibility = View.GONE
//                        binding.llMeasurementsDetails.visibility = View.GONE
//                    } else {
//                        binding.llOrthosisDetails.visibility = View.VISIBLE
//                        binding.llMeasurementsDetails.visibility = View.VISIBLE
//                    }
//                    isArrowDown = !isArrowDown
//                }
//
//                binding.ivArrowDown.setOnClickListener {
////                    val fromDegree = if (isArrowDown) 0f else 180f
////                    val toDegree = if (isArrowDown) 180f else 360f
//
//                    val fromDegree = if (isArrowDown) 180f else 0f
//                    val toDegree = if (isArrowDown) 360f else 180f
//
//                    val rotateAnimator = ObjectAnimator.ofFloat(
//                        binding.ivArrowDown,
//                        "rotation",
//                        fromDegree,
//                        toDegree
//                    )
//                    rotateAnimator.duration = 300 // duration of the animation in milliseconds
//                    rotateAnimator.start()
//
//                    if (isArrowDown) {
//                        binding.llOrthosisDetails.visibility = View.GONE
//                        binding.llMeasurementsDetails.visibility = View.GONE
//                    } else {
//                        binding.llOrthosisDetails.visibility = View.VISIBLE
//                        binding.llMeasurementsDetails.visibility = View.VISIBLE
//                    }
//                    isArrowDown = !isArrowDown
//                }

                binding.etOrthosisType.setOnClickListener {
                    if (isEditable) {
                        inflateBottomSheet(
                            orthosisTypeList,
                            binding,
                            position
                        )
                    } else {
                        Utility.warningToast(context, "Not Editable")
                    }

                }

//                val customDropDownAdapter =
//                    CustomDropDownAdapter(context, listOf("Select", "Pending", "Given"))
//                binding.etOrthosisStatus!!.adapter = customDropDownAdapter

                val statusOptions = listOf("Pending", "Given")
                val statusadapter =
                    ArrayAdapter(
                        context,
                        android.R.layout.simple_dropdown_item_1line,
                        statusOptions
                    )
                binding.etOrthosisStatus.setAdapter(statusadapter)
                binding.etOrthosisStatus.setOnClickListener {
                    if (isEditable) {
                        binding.etOrthosisStatus.showDropDown()
                    } else {
                        Utility.warningToast(context, "Not Editable")
                    }
                }


                val fitStatusOptions = listOf("Yes", "No")
                val fitStatusAdapter =
                    ArrayAdapter(
                        context,
                        android.R.layout.simple_dropdown_item_1line,
                        fitStatusOptions
                    )
                binding.etFittingStatus.setAdapter(fitStatusAdapter)

                binding.etFittingStatus.setOnClickListener {
                    if (isEditable) {
                        binding.etFittingStatus.showDropDown()
                    } else {
                        Utility.warningToast(context, "Not Editable")
                    }
                }

                binding.etFittingFeedback.isFocusable = isEditable

                binding.etFittingFeedback.setOnClickListener {
                    if (!isEditable) {
                        Utility.warningToast(context, "Not Editable")
                    }
                }
//                var selected_eye=binding.spinnerExaminationEye.selectedItem.toString()

                val options = listOf("Left", "Right")
                val adapter =
                    ArrayAdapter(context, android.R.layout.simple_dropdown_item_1line, options)
                binding.etAmputationSide.setAdapter(adapter)
                binding.etAmputationSide.setOnClickListener {
                    if (isEditable) {
                        binding.etAmputationSide.showDropDown()
                    } else {
                        Utility.warningToast(context, "Not Editable")
                    }
                }

                binding.etAmputationDate.setOnClickListener {
                    if (isEditable) {
                        Utility.openDatePicker(
                            context,
                            Utility.HIDE_PREVIOUS_DATES,
                            binding.etAmputationDate,
                            object : Utility.DateListener {
                                override fun onDateSelected(date: String) {
                                    //  val convertedDate = convertDateFormat(date)
                                    binding.etAmputationDate.setText(date)
                                }

                            })
                    } else {
                        Utility.warningToast(context, "Not Editable")
                    }

                }

//                binding.etExaminationDate.setOnClickListener {
//                    Utility.openExaminationDatePicker(
//                        context,
//                        Utility.HIDE_PREVIOUS_DATES,
//                        binding.etExaminationDate,
//                        localUser.campFrom,
//                        localUser.campTo,
//                        object : Utility.ExaminationDateListener {
//                            override fun onDateSelected(date: String) {
//                                val convertedDate = convertDateFormat(date)
//                                binding.etExaminationDate.setText(convertedDate)
//                            }
//
//                        })
//                }

                binding.etAmputationLevel.setOnClickListener {
                    if (isEditable) {
                        binding.etAmputationLevel.isEnabled = true
                    } else {
                        binding.etAmputationLevel.isEnabled = false

                        Utility.warningToast(context, "Not Editable")
                    }
                }


            }

            binding.etAmputationCause.isFocusable = isEditable

            binding.etAmputationCause.setOnClickListener {
                if (!isEditable) {
                    Utility.warningToast(context, "Not Editable")
                }
            }

            binding.etAmputationLevel.isFocusable = isEditable
            binding.etAmputationLevel.setOnClickListener {
                if (!isEditable) {
                    Utility.warningToast(context, "Not Editable")
                }
            }


            binding.btnAddImage.setOnClickListener {
                if (isEditable) {
                    if (!binding.etOrthosisType.text.isNullOrEmpty()) {
                        var filterListByOrthoId =
                            content.orthosisImageList.filter { it.orthosisFormId == content.orthoFormId }
                        if (filterListByOrthoId.size == 4) {
                            Utility.warningToast(context, "Only 4 images are allowed")
                        } else {
                            event.onImageClick(position)
                        }
                    } else {
                        Utility.warningToast(context, "(${position + 1})Select Orthosis type first")
                    }
                } else {
                    Utility.warningToast(context, "Not Editable")
                }


            }


            //        val amputationDate: String,
//        val amputationSide: String, // Right/Left
//        val amputationLevel: String,
//        val amputationCause: String,
//        val orthosis: OrthosisType,//object
//        val status: String,
//        val patientOrthosisMeasurements: List<MeasurementPatientData>,
//        val deleted: Boolean = false
            binding.etAmputationDate.doOnTextChanged { text, start, before, count ->
                if (!text.isNullOrEmpty()) {
                    event.setOrthosisEditTextData(
                        position,
                        "amputationDate",
                        convertDateFormat(text.toString())
                    )
                } else {
                    event.setOrthosisEditTextData(
                        position,
                        "amputationDate",
                        ""
                    )
                }
            }

            binding.etAmputationSide.doOnTextChanged { text, start, before, count ->
                if (!text.isNullOrEmpty()) {
                    event.setOrthosisEditTextData(position, "amputationSide", text.toString())
                } else {
                    event.setOrthosisEditTextData(position, "amputationSide", "")

                }
            }

            binding.etAmputationLevel.doOnTextChanged { text, start, before, count ->
                if (!text.isNullOrEmpty()) {
                    event.setOrthosisEditTextData(position, "amputationLevel", text.toString())
                } else {
                    event.setOrthosisEditTextData(position, "amputationLevel", "")
                }
            }

            binding.etAmputationCause.doOnTextChanged { text, start, before, count ->
                if (!text.isNullOrEmpty()) {
                    event.setOrthosisEditTextData(position, "amputationCause", text.toString())
                } else {
                    event.setOrthosisEditTextData(position, "amputationCause", "")

                }
            }

            binding.etOrthosisStatus.doOnTextChanged { text, start, before, count ->
                if (!text.isNullOrEmpty()) {
                    if (text.toString() == "Given") {
                        binding.etlFittingStatus.visibility = View.VISIBLE
                        binding.etlFittingFeedback.visibility = View.VISIBLE
                    } else {
                        binding.etFittingStatus.setText("")
                        binding.etFittingFeedback.setText("")
                        binding.etlFittingStatus.visibility = View.GONE
                        binding.etlFittingFeedback.visibility = View.GONE
                    }
                    event.setOrthosisEditTextData(position, "status", text.toString())
                }

            }

            binding.etFittingStatus.doOnTextChanged { text, start, before, count ->
                if (!text.isNullOrEmpty()) {
                    event.setOrthosisEditTextData(position, "fittingStatus", text.toString())
                } else {
                    event.setOrthosisEditTextData(position, "fittingStatus", "")

                }

            }

            binding.etFittingFeedback.doOnTextChanged { text, start, before, count ->
                if (!text.isNullOrEmpty()) {
                    event.setOrthosisEditTextData(position, "fittingReason", text.toString())
                } else {
                    event.setOrthosisEditTextData(position, "fittingReason", "")

                }
            }

            binding.etOtherOrthosis.doOnTextChanged { text, start, before, count ->
                if (!text.isNullOrEmpty()) {
                    event.setOrthosisEditTextData(position, "otherOrthosis", text.toString())
                } else {
                    event.setOrthosisEditTextData(position, "otherOrthosis", "")

                }
            }

//            binding.etExaminationDate.doOnTextChanged { text, start, before, count ->
//                if (!text.isNullOrEmpty()) {
//                    event.setOrthosisEditTextData(position, "examinationDate", text.toString())
//                }
//
//            }

            binding.ivDeleteForm.setOnClickListener {
                if (isEditable) {
                    event.onDeleteForm(position)
                } else {
                    Utility.warningToast(context, "Not Editable")
                }
            }

            binding.tvClearFormData.setOnClickListener {
                if (isEditable) {
                    binding.tvOrthosisType.text = "(${position + 1}) Select Orthosis Type"
                    setUpMeasurementRecyclerview(isEditable, binding, listOf(), position)

                    //clearing orthosis data
                    content.amputationDate = ""
                    content.amputationSide = ""
                    content.amputationLevel = ""
                    content.amputationCause = ""
                    content.orthosis = OrthosisType(1, listOf(), "")
                    content.otherOrthosis = ""
                    content.status = ""
                    content.statusNotes = ""
                    content.fit_properly = ""
                    content.fit_properly_reason = ""
                    content.orthoFormId = 0
                    content.patientOrthosisMeasurements = listOf()
                    content.image = ""

                    //clearing orthosis images
                    content.orthosisImageList.clear()

                    binding.cvImage.visibility = View.GONE
                    clearForm(binding)
                    notifyDataSetChanged()
                } else {
                    Utility.warningToast(context, "Not Editable")
                }

            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrthosisFormAdapter.OrthosisFormViewHolder {
        val binding =
            ItemOrthosisFormBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrthosisFormViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: OrthosisFormAdapter.OrthosisFormViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    private fun inflateBottomSheet(
        orthosisTypeList: List<OrthosisType>,
        binding: ItemOrthosisFormBinding,
        itemPosition: Int
    ) {
        val items = ArrayList<SearchAbleList>()
        if (orthosisTypeList[0].name.isNullOrEmpty()) {
            orthosisTypeList[0].name = "Other"
        }
        for (i in orthosisTypeList.indices) {
            items.add(SearchAbleList(i, orthosisTypeList[i].name))
        }
        val dialog = SingleSelectBottomSheetDialogFragment(
            context,
            items,
            "Select Orthosis",
            0,
            true
        ) { selectedValue ->
            //Logger.d("TAG", "onCreate: $selectedValue")
            // binding!!.etAssembly.setText(selectedValue.title)
            // val assemblyData  = assemblyList[selectedValue.position]
            val orthosisType = orthosisTypeList[selectedValue.position]
            var otherMeasurementAdapter: OtherMeasurementAdapter? = null

            if (orthosisType.name == "Other") {

                //if orthosis is selected other - logic
                binding.etlOtherOrthosis.visibility = View.VISIBLE
                binding.btnAddMeasurement.visibility = View.VISIBLE
                binding.rvOtherMeasurements.visibility = View.VISIBLE
                binding.rvOrthosisMeasurements.visibility = View.GONE

                binding.etOrthosisType.setText(orthosisType.name)
                binding.tvOrthosisType.text = "(${itemPosition + 1}) ${orthosisType.name}"

                //adding measurement data
                val measurementList = mutableListOf<MeasurementPatientData>()
                val measurement = Measurement(deleted = false, "", "", 1)
                measurementList.add(
                    MeasurementPatientData(
                        1,
                        measurement,
                        value = 0.0,
                        unit = "",
                        otherMeasurement = ""
                    )
                )
                //setUpMeasurementRecyclerview(binding, measurementList, itemPosition)


                //measurement recyclerview
                otherMeasurementAdapter = OtherMeasurementAdapter(
                    isEditable,
                    context,
                    measurementList,
                    object : OtherMeasurementAdapter.OtherMeasurementAdapterEvent {
                        override fun setOtherMeasurementData(
                            childPosition: Int,
                            measurement: Measurement,
                            measurementName: String,
                            measurementValue: String,
                            measurementUnit: String,
                            isOtherMeasurement: Boolean

                        ) {

                            event.onMeasurementData(
                                itemPosition,
                                childPosition,
                                measurement,
                                measurementName,
                                measurementValue,
                                measurementUnit,
                                isOtherMeasurement
                            )
                        }

                        override fun onRemoveMeasurement(childPosistion: Int) {
                            if (measurementList[childPosistion].value == 0.0) {
                                measurementList.removeAt(childPosistion)
                                otherMeasurementAdapter!!.notifyDataSetChanged()
                            } else {
                                removeMeasurement(
                                    childPosistion,
                                    measurementList,
                                    measurementAdapter = otherMeasurementAdapter!!
                                )
                            }
                        }


                    })
                binding.rvOtherMeasurements.layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                binding.rvOtherMeasurements.adapter = otherMeasurementAdapter
                //end measurement recyclerview


                //updating measurement to main screen
                event.updateMeasurementList(itemPosition, measurementList, orthosisType)
//                event.setOrthosisTypeData(itemPosition, orthosisType)

                //adding and deleting measurement logic
                binding.btnAddMeasurement.setOnClickListener {
                    measurementList.add(
                        MeasurementPatientData(
                            1,
                            measurement,
                            value = 0.0,
                            unit = "",
                            otherMeasurement = ""
                        )
                    )
                    otherMeasurementAdapter.notifyDataSetChanged()
                }
            } else {
                //if orthosis is selected - logic
                binding.etOtherOrthosis.setText("")
                binding.etlOtherOrthosis.visibility = View.GONE
                binding.btnAddMeasurement.visibility = View.GONE
                binding.rvOtherMeasurements.visibility = View.GONE
                binding.rvOrthosisMeasurements.visibility = View.VISIBLE

                binding.etOtherOrthosis.setText("")

                binding.etOrthosisType.setText(orthosisType.name)
                binding.tvOrthosisType.text = "(${itemPosition + 1}) ${orthosisType.name}"

                val measurementData = ArrayList<MeasurementPatientData>()
                for (i in orthosisType.measurements) {
                    measurementData.add(
                        MeasurementPatientData(
                            1,
                            i,
                            value = 0.0,
                            unit = "",
                            otherMeasurement = ""
                        )
                    )
                }
                setUpMeasurementRecyclerview(isEditable, binding, measurementData, itemPosition)
                event.updateMeasurementList(itemPosition, measurementData, orthosisType)
//                event.setOrthosisTypeData(itemPosition, orthosisType)
            }

        }

        dialog.show(fragmentManager, "SingleSelectBottomSheetDialogFragment")
    }

    interface OrthosisFormClickListener {

        fun updateMeasurementList(
            parentPosition: Int,
            measurementList: List<MeasurementPatientData>,
            orthosisType: OrthosisType
        )

        fun onMeasurementData(
            parentPosition: Int,
            childPosition: Int,
            measurement: Measurement,
            measurementName: String,
            measurementValue: String,
            measurementUnit: String,
            isOtherMeasurement: Boolean
        )

        //        val amputationDate: String,
//        val amputationSide: String, // Right/Left
//        val amputationLevel: String,
//        val amputationCause: String,
//        val orthosis: OrthosisType,//object
//        val status: String,
//        val patientOrthosisMeasurements: List<MeasurementPatientData>,
//        val deleted: Boolean = false
        fun setOrthosisEditTextData(position: Int, fieldName: String, fieldValue: String)

        fun setOrthosisTypeData(position: Int, orthosisType: OrthosisType)


        fun onDeleteForm(position: Int)

        fun onImageClick(position: Int)

        fun onOrthosisImageRemove(parentPosition: Int, childPosition: Int, image: String)

    }

    private fun setColorForAllEditText(binding: ItemOrthosisFormBinding) {
        val orthosisTypeHint = context.getString(R.string.txt_orthosis_type)
        setAsteriskColor(binding.etlOrthosisType, orthosisTypeHint)

        val amputationDateHint = context.getString(R.string.txt_amputation_date)
        setAsteriskColor(binding.etlAmputationDate, amputationDateHint)

        val amputationSideHint = context.getString(R.string.txt_amputation_side)
        setAsteriskColor(binding.etlAmputationSide, amputationSideHint)

        val amputationLevelHint = context.getString(R.string.txt_amputation_level)
        setAsteriskColor(binding.etlAmputationLevel, amputationLevelHint)

        val amputationCauseHint = context.getString(R.string.txt_amputation_cause)
        setAsteriskColor(binding.etlAmputationCause, amputationCauseHint)

        val orthosisStatusHint = context.getString(R.string.txt_orthosis_status)
        setAsteriskColor(binding.etlOrthosisStatus, orthosisStatusHint)

        val examinationDateHint = context.getString(R.string.txt_examination_date)
        setAsteriskColor(binding.etlExaminationDate, examinationDateHint)

        val fittingStatusHint = context.getString(R.string.txt_fitting_status)
        setAsteriskColor(binding.etlFittingStatus, fittingStatusHint)

        val fittingReasonHint = context.getString(R.string.txt_fitting_feedback)
        setAsteriskColor(binding.etlFittingFeedback, fittingReasonHint)

        val enterOrthosisTypHint = context.getString(R.string.txt_enter_orthrosis_type)
        setAsteriskColor(binding.etlOtherOrthosis, enterOrthosisTypHint)
    }

    private fun setAsteriskColor(etlField: TextInputLayout, hintText: String) {
        val spannableString = SpannableString(hintText)

        val redColor = ForegroundColorSpan(context.resources.getColor(R.color.dark_red))
        spannableString.setSpan(
            redColor,
            hintText.length - 1,
            hintText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        etlField.hint = spannableString

    }

    private fun setTextColorAsteriskColor(binding: ItemOrthosisFormBinding) {
        val orthosisStatusText = "Orthosis Status*"
        val spannableString = SpannableString(orthosisStatusText)
        val redColor = ForegroundColorSpan(context.resources.getColor(R.color.dark_red))
        spannableString.setSpan(
            redColor,
            orthosisStatusText.length - 1,
            orthosisStatusText.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        //  binding.tvOrthosisStatus.text = spannableString
    }

    private fun setUpMeasurementRecyclerview(
        isEditable: Boolean,
        binding: ItemOrthosisFormBinding,
        measurementList: List<MeasurementPatientData>,
        parentPosition: Int
    ) {
        binding.rvOrthosisMeasurements.visibility = View.VISIBLE
        val childAdapter = OrthosisMeasurementAdapter(
            isEditable,
            context,
            measurementList,
            object : OrthosisMeasurementAdapter.MeasurementAdapterEvent {
                override fun setMeasurementData(
                    childPosition: Int,
                    measurement: Measurement,
                    measurementName: String,
                    measurementValue: String,
                    measurementUnit: String,
                    isOtherMeasurement: Boolean
                ) {

                    event.onMeasurementData(
                        parentPosition,
                        childPosition,
                        measurement,
                        measurementName,
                        measurementValue,
                        measurementUnit,
                        isOtherMeasurement
                    )
                }

                override fun onRemoveMeasurement(childPosistion: Int) {
                    //
                }


            })
        binding.rvOrthosisMeasurements.adapter = childAdapter
        binding.rvOrthosisMeasurements.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }


    private fun clearForm(binding: ItemOrthosisFormBinding) {
        binding.etOrthosisType.setText("")
        binding.etOrthosisStatus.setText("")
        binding.etAmputationDate.setText("")
        binding.etAmputationSide.setText("")
        binding.etAmputationLevel.setText("")
        binding.etAmputationCause.setText("")
        binding.etFittingStatus.setText("")
        binding.etFittingFeedback.setText("")
        // binding.etExaminationDate.setText("")

    }

    fun convertDateFormat(dateString: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Parse the input date string and format it to the output format
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }

    fun convertDateFormatFromData(dateString: String): String {
        // Define the input and output date formats
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())

        // Parse the input date string and format it to the output format
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date!!)
    }

    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return dateFormat.format(Date())
    }

    private fun hideKeyboard(view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun hideAllKeypad(binding: ItemOrthosisFormBinding) {
        binding.etAmputationLevel.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) hideKeyboard(view)
        }

        binding.etAmputationCause.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) hideKeyboard(view)
        }

        binding.etFittingFeedback.setOnFocusChangeListener { view, hasFocus ->
            if (!hasFocus) hideKeyboard(view)
        }

    }

    private fun setUpOrthosisImageRecyclerview(
        binding: ItemOrthosisFormBinding,
        orthosisImageList: List<OrthosisImages>,
        parentPosition: Int,
        orthosFormId: Int
    ) {
        val filteredOrthoImage = orthosisImageList.filter { it.orthosisFormId == orthosFormId }
        val orthosisImageAdapter = OrthosisImageAdapter(
            context,
            filteredOrthoImage,
            object : OrthosisImageAdapter.OrthosisImageAdapterEvent {
                override fun onImageClick(position: Int) {
                    //
                }

                override fun onImageRemove(position: Int, image: String) {
                    event.onOrthosisImageRemove(parentPosition, position, image)
                }


            })
        binding.rvOrthosisMeasurements.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvOrthosisImages.adapter = orthosisImageAdapter
    }

    private fun removeMeasurement(
        childPosition: Int,
        measurementList: MutableList<MeasurementPatientData>,
        measurementAdapter: OtherMeasurementAdapter
    ) {
        val layoutResId = R.layout.image_remove_dialog
        val alertCustomDialog = layoutInflater.inflate(layoutResId, null)
        val messageDialog = AlertDialog.Builder(context)
        messageDialog.setView(alertCustomDialog)


        val tvCancel: TextView = alertCustomDialog.findViewById(R.id.tvCancel)
        val tvTitle: TextView = alertCustomDialog.findViewById(R.id.tvTitle)
        val tvYes: TextView = alertCustomDialog.findViewById(R.id.tvDelete)

        val finalDialog = messageDialog.create()

        tvTitle.text = "Are you sure you want \nto Remove Measurement"
        tvCancel.setOnClickListener {
            finalDialog.dismiss()
        }
        tvYes.setOnClickListener {
            finalDialog.dismiss()
            //perform operation

            measurementList.removeAt(childPosition)
            measurementAdapter.notifyDataSetChanged()
        }

        finalDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        finalDialog.show()
    }

    private fun setUpOtherMeasurementRecyclerView(
        isEditable: Boolean,
        binding: ItemOrthosisFormBinding,
        measurementList: List<MeasurementPatientData>,
        parentPosition: Int
    ) {
        val childAdapter = OtherMeasurementAdapter(
            isEditable,
            context,
            measurementList,
            object : OtherMeasurementAdapter.OtherMeasurementAdapterEvent {
                override fun setOtherMeasurementData(
                    childPosition: Int,
                    measurement: Measurement,
                    measurementName: String,
                    measurementValue: String,
                    measurementUnit: String,
                    isOtherMeasurement: Boolean
                ) {
                    event.onMeasurementData(
                        parentPosition,
                        childPosition,
                        measurement,
                        measurementName,
                        measurementValue,
                        measurementUnit,
                        isOtherMeasurement
                    )
                }

                override fun onRemoveMeasurement(childPosistion: Int) {
                    //
                }


            })
        binding.rvOrthosisMeasurements.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvOrthosisMeasurements.adapter = childAdapter
    }

    fun onDiagnosisClick(isAmputee: Boolean) {
        this.isAmputee = isAmputee
        notifyDataSetChanged()
    }


    fun orthosisImageClick(
        orthosisImageList: MutableList<OrthosisImages>,
        parentPosition: Int,
        orthoFormId: Int
    ) {
        data[parentPosition].orthosisImageList = orthosisImageList
        val orthoImageList = orthosisImageList.filter { it.orthosisFormId == orthoFormId }

        if (!orthoImageList.isNullOrEmpty()) {
            if (orthoImageList.size == 1) {
                if (!orthoImageList[0].images.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(orthoImageList[0].images).placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(this@OrthosisFormAdapter.binding!!.ivOthoFile1)
                }
            }


            if (orthoImageList.size == 2) {
                if (!orthoImageList[1].images.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(orthoImageList[1].images).placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(this@OrthosisFormAdapter.binding!!.ivOthoFile2)
                }
            }


            if (orthoImageList.size == 3) {
                if (!orthoImageList[2].images.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(orthoImageList[2].images).placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(this@OrthosisFormAdapter.binding!!.ivOthoFile3)
                }
            }


            if (orthoImageList.size == 4) {
                if (!orthoImageList[3].images.isNullOrEmpty()) {
                    Glide.with(context)
                        .load(orthoImageList[3].images).placeholder(R.drawable.img_placeholder)
                        .error(R.drawable.img_placeholder)
                        .into(this@OrthosisFormAdapter.binding!!.ivOthoFile4)
                }
            }


        }

        // notifyItemChanged(parentPosition)
        // notifyDataSetChanged()

    }

    fun getDataList(): List<OrthosisPatientData> = data
}