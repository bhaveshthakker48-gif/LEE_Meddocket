package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterFormCountBinding

class FormCountAdapter(
    private val context: Context,
    private val labels1: Array<String>,
    private val data: List<Int>,
    private val percentages: List<Float>,
    private val colors1: List<Int>,
    private val Total_forms: Int
) : RecyclerView.Adapter<FormCountAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            AdapterFormCountBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val label = labels1[position]
        val value = data[position]
        val percentage = percentages[position]
        val color = colors1[position]

        holder.bind(label, value, percentage, color)
    }

    override fun getItemCount(): Int {
        return labels1.size
    }

    inner class ViewHolder(val binding: AdapterFormCountBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(label: String, value: Int, percentage: Float, color: Int) {
            if (label == "Total Forms") {
                binding.TextViewLablel.text = label
                binding.TextViewPercentage.text = Total_forms.toString()
                binding.TextViewColor.setBackgroundColor(color)
            } else {
                val percentage1 = String.format("%.2f", percentage)
                if (value == 0) {
                    binding.TextViewLablel.text = label
                    binding.TextViewPercentage.text = "($value, 0.00%)"
                    binding.TextViewColor.setBackgroundColor(color)
                } else {
                    binding.TextViewLablel.text = label
                    binding.TextViewPercentage.text = "($value, $percentage1%)"
                    binding.TextViewColor.setBackgroundColor(color)
                }
            }
        }
    }
}
