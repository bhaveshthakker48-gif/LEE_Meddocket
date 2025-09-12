package org.impactindiafoundation.iifllemeddocket.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import org.impactindiafoundation.iifllemeddocket.CallBack.FormClick
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.databinding.AdapterPatientReportBinding


class ViewReportAdapter(val context: Context, val labels1: Array<String>,
    val FormClick:FormClick) :RecyclerView.Adapter<ViewReportAdapter.ViewHolder>() {
    private var selectedItem: Int = RecyclerView.NO_POSITION
    fun setSelectedItem(position: Int) {
        selectedItem = position
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewReportAdapter.ViewHolder {

        return ViewHolder(
            AdapterPatientReportBinding.inflate(
                LayoutInflater.from(context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewReportAdapter.ViewHolder, position: Int) {
        val data=labels1[position]
       holder.bind(data,position)
    }

    override fun getItemCount(): Int {
       return labels1.size
    }

    inner class ViewHolder(val binding:AdapterPatientReportBinding):RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("SuspiciousIndentation")
        fun bind(data: String, position: Int)
        {
          binding.TextViewLabel.text=data
            binding.cardView.setCardBackgroundColor(
                if (adapterPosition == selectedItem) {
                    // This item is the selected item
                    ContextCompat.getColor(itemView.context, R.color.blue)
                } else {
                    // This item is not the selected item
                    ContextCompat.getColor(itemView.context, R.color.white)
                }
            )

            binding.TextViewLabel.setOnClickListener(object :View.OnClickListener
            {
                override fun onClick(v: View?) {
                   FormClick.FormClick(data,position,v!!)
                }
            })

            binding.cardView.setOnClickListener(object :View.OnClickListener
            {
                override fun onClick(v: View?) {
                    FormClick.FormClick(data,position,v!!)
                }
            })
        }
    }
}