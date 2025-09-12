package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.R
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.model.Measurement
import org.impactindiafoundation.iifllemeddocket.architecture.model.MeasurementPatientData
import org.impactindiafoundation.iifllemeddocket.databinding.ItemOrthosisMeasurementBinding
import org.impactindiafoundation.iifllemeddocket.databinding.ItemOtherMeasurementBinding

class OtherMeasurementAdapter(
    var isEditable:Boolean,
    val context: Context,
    val data: List<MeasurementPatientData>,
    val measurementEvent: OtherMeasurementAdapterEvent
) :
    RecyclerView.Adapter<OtherMeasurementAdapter.OtherMeasurementAdapterViewHolder>() {

    inner class OtherMeasurementAdapterViewHolder(
        private var binding: ItemOtherMeasurementBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            binding.apply {
                if (!content.orthosisMeasurement.fieldName.isNullOrEmpty()){
                    binding.etlOrthosisMeasurement.hint = content.orthosisMeasurement.fieldName
                }

                if (content.value != 0.0) {
                    binding.etValue.setText(content.value.toString())
                }

                if (!content.otherMeasurement.isNullOrEmpty()) {
                    binding.etOrthosisMeasurement.setText(content.otherMeasurement)
                }

                if (!content.unit.isNullOrEmpty()) {
                    binding.etUnit.setText(content.unit)
                }
                else{
                    binding.etUnit.setText("Inch")
                }

                val unitOptions = listOf("Inch", "Cm")
                val unitAdapter =
                    ArrayAdapter(
                        context,
                        android.R.layout.simple_dropdown_item_1line,
                        unitOptions
                    )
                binding.etUnit.setAdapter(unitAdapter)
                binding.etUnit.setOnClickListener {
                    binding.etUnit.showDropDown()
                }

                binding.etUnit.setOnClickListener {
                    if (isEditable) {
                        binding.etUnit.showDropDown()
                    } else {
                        Utility.warningToast(context, "Not Editable")
                    }
                }

                binding.etOrthosisMeasurement.isFocusable = isEditable

                binding.etOrthosisMeasurement.setOnClickListener {
                    if (!isEditable) {
                        Utility.warningToast(context, "Not Editable")
                    }
                }


                binding.etValue.isFocusable = isEditable

                binding.etValue.setOnClickListener {
                    if (!isEditable) {
                        Utility.warningToast(context, "Not Editable")
                    }
                }


                binding.etOrthosisMeasurement.doOnTextChanged { text, start, before, count ->
                    if (!text.isNullOrEmpty()) {
                        measurementEvent.setOtherMeasurementData(
                            position,
                            content.orthosisMeasurement,
                            measurementName = text.toString(),
                            measurementValue = binding.etValue.text.toString() ,
                            measurementUnit = binding.etUnit.text.toString(),
                            isOtherMeasurement = true
                        )
                    } else {
                        measurementEvent.setOtherMeasurementData(
                            position,
                            content.orthosisMeasurement,
                            measurementName = text.toString(),
                            measurementValue = binding.etValue.text.toString() ,
                            measurementUnit = binding.etUnit.text.toString(),
                            isOtherMeasurement = true
                        )
                    }
                }

                binding.etValue.doOnTextChanged { text, start, before, count ->
                    if (!text.isNullOrEmpty()) {
                        measurementEvent.setOtherMeasurementData(
                            position,
                            content.orthosisMeasurement,
                            measurementName = binding.etOrthosisMeasurement.text.toString(),
                            measurementValue = text.toString(),
                            measurementUnit = binding.etUnit.text.toString(),
                            isOtherMeasurement = true
                        )
                    } else {
                        measurementEvent.setOtherMeasurementData(
                            position,
                            content.orthosisMeasurement,
                            measurementName = binding.etOrthosisMeasurement.text.toString(),
                            measurementValue = text.toString(),
                            measurementUnit = binding.etUnit.text.toString(),
                            isOtherMeasurement = true
                        )
                    }
                }

                binding.etUnit.doOnTextChanged { text, start, before, count ->
                    if (!text.isNullOrEmpty()) {
                        measurementEvent.setOtherMeasurementData(
                            position,
                            content.orthosisMeasurement,
                            measurementName = binding.etOrthosisMeasurement.text.toString(),
                            measurementValue = binding.etValue.text.toString() ,
                            measurementUnit = text.toString(),
                            isOtherMeasurement = true
                        )
                    } else {
                        measurementEvent.setOtherMeasurementData(
                            position,
                            content.orthosisMeasurement,
                            measurementName = binding.etOrthosisMeasurement.text.toString(),
                            measurementValue = binding.etValue.text.toString() ,
                            measurementUnit = text.toString(),
                            isOtherMeasurement = true
                        )
                    }
                }


                binding.etOrthosisMeasurement.setOnEditorActionListener { _, actionId, _ ->
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        if (data.lastIndex == position) {
                            //  binding.etOrthosisMeasurement.clearFocus()  // Clear the focus from this EditText
//                            val imm =
//                                binding.etPatientWeight.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                            imm.hideSoftInputFromWindow(binding.etPatientWeight.windowToken, 0)

                            val imm =
                                binding.root.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                            val view = (binding.root.context as Activity).currentFocus
                                ?: binding.root  // Use the current focus or root view as fallback
                            imm.hideSoftInputFromWindow(view.windowToken, 0)
                        }

                        true  // Return true to indicate that we've handled the action
                    } else {
                        false  // Otherwise, do nothing
                    }
                }


                binding.ivRemoveMeasurement.setOnClickListener{
                    measurementEvent.onRemoveMeasurement(position)
                }


            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OtherMeasurementAdapterViewHolder {
        val binding =
            ItemOtherMeasurementBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return OtherMeasurementAdapterViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: OtherMeasurementAdapterViewHolder,
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

    interface OtherMeasurementAdapterEvent {
        fun setOtherMeasurementData(
            childPosition: Int,
            measurement: Measurement,
            measurementName:String,
            measurementValue: String,
            measurementUnit: String,
            isOtherMeasurement:Boolean
        )

        fun onRemoveMeasurement(childPosistion: Int)
    }

}