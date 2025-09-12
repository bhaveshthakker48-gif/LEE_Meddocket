package org.impactindiafoundation.iifllemeddocket.Adapter



import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

import org.impactindiafoundation.iifllemeddocket.databinding.CustomSpinnerItemBinding


class CustomDropDownAdapter(val context: Context, var dataSource: List<String>) : BaseAdapter() {
    var selectedPosition = -1
        private set
    fun setSelectedItem(item: String) {
        val newPosition = dataSource.indexOf(item)

        if (newPosition != -1 && newPosition != selectedPosition) {
            selectedPosition = newPosition
            notifyDataSetChanged()
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: CustomSpinnerItemBinding

        if (convertView == null) {
            binding = CustomSpinnerItemBinding.inflate(LayoutInflater.from(context), parent, false)
            binding.root.tag = binding
        } else {
            binding = convertView.tag as CustomSpinnerItemBinding
        }

        binding.text.text = dataSource[position]
        binding.text.maxLines = 1
        binding.text.ellipsize = TextUtils.TruncateAt.END
        return binding.root
    }

    override fun getItem(position: Int): Any? {
        return dataSource[position]
    }

    override fun getCount(): Int {
        return dataSource.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: CustomSpinnerItemBinding = if (convertView == null) {
            val inflater = LayoutInflater.from(context)
            CustomSpinnerItemBinding.inflate(inflater, parent, false)
        } else {
            CustomSpinnerItemBinding.bind(convertView)
        }

        binding.text.text = dataSource[position]
        binding.text.maxLines = 1
        binding.text.ellipsize = TextUtils.TruncateAt.END

        return binding.root
    }
}
