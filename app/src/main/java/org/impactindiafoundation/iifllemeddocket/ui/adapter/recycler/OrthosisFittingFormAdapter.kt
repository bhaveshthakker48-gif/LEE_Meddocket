package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.animation.ObjectAnimator
import android.content.Context
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import org.impactindiafoundation.iifllemeddocket.Adapter.CustomDropDownAdapter
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.SearchAbleList
import org.impactindiafoundation.iifllemeddocket.Utils.SingleSelectBottomSheetDialogFragment
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.model.Measurement
import org.impactindiafoundation.iifllemeddocket.architecture.model.MeasurementPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisPatientData
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisType
import org.impactindiafoundation.iifllemeddocket.databinding.ItemOrthosisFittingBinding
import org.impactindiafoundation.iifllemeddocket.databinding.ItemOrthosisFormBinding


class OrthosisFittingFormAdapter(
    private var isEditable:Boolean,
    val context: Context,
    val data: List<OrthosisPatientData>,
    private val event: OrthosisFormClickListener
) :
    RecyclerView.Adapter<OrthosisFittingFormAdapter.OrthosisFittingFormViewHolder>() {

    inner class OrthosisFittingFormViewHolder(
        private var binding: ItemOrthosisFittingBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            setUpMeasurementRecyclerview(binding, content.patientOrthosisMeasurements, position)
            binding.apply {

                if (isEditable){
                    binding.etOrthosisStatus.isEnabled = true
                    binding.etStatusNotes.isEnabled = true
                    binding.etFittingStatus.isEnabled = true
                    binding.etFittingFeedback.isEnabled = true
                }
                else{
                    binding.etOrthosisStatus.isEnabled = false
                    binding.etStatusNotes.isEnabled = false
                    binding.etFittingStatus.isEnabled = false
                    binding.etFittingFeedback.isEnabled = false
                }


                if (!content.orthosis.name.isNullOrEmpty()){
                    binding.tvOrthosisType.text = "(${position + 1}) ${content.orthosis.name}"
                }
                else{
                    binding.tvOrthosisType.text = "(${position + 1}) Select Orthosis Type"
                }


                setColorForAllEditText(binding)
                var isArrowDown = true
                binding.llOrthosisTab.setOnClickListener {
                    val fromDegree = if (isArrowDown) 0f else 180f
                    val toDegree = if (isArrowDown) 180f else 360f

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


//                val customDropDownAdapter =
//                    CustomDropDownAdapter(context, listOf("Select","Pending","Ready","Given"))
//                binding.etOrthosisStatus!!.adapter = customDropDownAdapter


                //setting data
                if (!content.status.isNullOrEmpty()) {
                    binding.etOrthosisStatus.setText(content.status)
                    if (content.status == "Given") {
                        binding.etlFittingStatus.visibility = View.VISIBLE
                        binding.etlFittingFeedback.visibility = View.VISIBLE
                        binding.etFittingStatus.setText(content.fit_properly)
                        binding.etFittingFeedback.setText(content.fit_properly_reason)
                    } else {
                        binding.etlFittingStatus.visibility = View.GONE
                        binding.etlFittingFeedback.visibility = View.GONE
                    }
                }




                val statusOptions = listOf("Given","Sent Courier","Given to Volunteer","Refitting")
                val statusadapter =
                    ArrayAdapter(
                        context,
                        android.R.layout.simple_dropdown_item_1line,
                        statusOptions
                    )
                binding.etOrthosisStatus.setAdapter(statusadapter)
                binding.etOrthosisStatus.setOnClickListener {
                    if (content.status == "Given"){
                        Utility.infoToast(context,"Cannot Change Given Status!")
                    }
                    else{
                        binding.etOrthosisStatus.showDropDown()
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
                    binding.etFittingStatus.showDropDown()
                }

                binding.etFittingStatus.setText(content.fit_properly)
                binding.etFittingFeedback.setText(content.fit_properly_reason)


                binding.etOrthosisStatus.doOnTextChanged { text, start, before, count ->
                    if (!text.isNullOrEmpty()) {
                        if (text.toString() == "Given") {
                            binding.etlFittingStatus.visibility = View.VISIBLE
                            binding.etlFittingFeedback.visibility = View.VISIBLE
                            binding.etlStatusNotes.visibility = View.GONE
                            binding.etStatusNotes.setText("")
                        } else {
                            if (text.toString() == "Pending") {
                                binding.etlStatusNotes.visibility = View.GONE
                            }
                            else if (text.toString() == "Refitting"){
                                binding.llNewMeasurements.visibility = View.VISIBLE
                                //setup measurements recycler view here
                            }
                            else{
                                binding.etlStatusNotes.visibility = View.VISIBLE
                            }
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
                    }

                }

                binding.etFittingFeedback.doOnTextChanged { text, start, before, count ->
                    if (!text.isNullOrEmpty()) {
                        event.setOrthosisEditTextData(position, "fittingReason", text.toString())
                    } else {
                        event.setOrthosisEditTextData(position, "fittingReason", "")

                    }
                }

                binding.etStatusNotes.doOnTextChanged { text, start, before, count ->
                    if (!text.isNullOrEmpty()) {
                        event.setOrthosisEditTextData(position, "statusNotes", text.toString())
                    } else {
                        event.setOrthosisEditTextData(position, "statusNotes", "")

                    }
                }
//                var selected_eye=binding.spinnerExaminationEye.selectedItem.toString()

//                binding.etAmputationDate.setOnClickListener {
//                    Utility.openDatePicker(
//                        context,
//                        Utility.HIDE_PREVIOUS_DATES,
//                        binding.etAmputationDate,
//                        object : Utility.DateListener {
//                            override fun onDateSelected(date: String) {
//                                binding.etAmputationDate.setText(date)
//                            }
//
//                        })
//                }


            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrthosisFittingFormViewHolder {
        val binding =
            ItemOrthosisFittingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrthosisFittingFormViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: OrthosisFittingFormViewHolder,
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

    interface OrthosisFormClickListener {

        fun onMeasurementData(parentPosition: Int, childPosition: Int)

        fun setOrthosisEditTextData(position: Int, fieldName: String, fieldValue: String)
    }


    private fun setUpMeasurementRecyclerview(
        binding: ItemOrthosisFittingBinding,
        measurementList: List<MeasurementPatientData>,
        parentPosition: Int
    ) {
        val childAdapter = FittingMeasurementAdapter(
            context,
            measurementList,
            object : FittingMeasurementAdapter.MeasurementAdapterEvent {
                override fun onGetData(position: Int) {
                    event.onMeasurementData(parentPosition, position)
                }

            })
        binding.rvMeasurementList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvMeasurementList.adapter = childAdapter
    }


    private fun setColorForAllEditText(binding: ItemOrthosisFittingBinding) {

        val orthosisStatusHint = context.getString(R.string.txt_orthosis_status)
        setAsteriskColor(binding.etlOrthosisStatus, orthosisStatusHint)


        val fittingStatusHint = context.getString(R.string.txt_fitting_status)
        setAsteriskColor(binding.etlFittingStatus, fittingStatusHint)

        val fittingReasonHint = context.getString(R.string.txt_fitting_feedback)
        setAsteriskColor(binding.etlFittingFeedback, fittingReasonHint)

        val statusCommentHint = context.getString(R.string.txt_comments)
        setAsteriskColor(binding.etlStatusNotes, statusCommentHint)
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
}