package org.impactindiafoundation.iifllemeddocket.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import org.impactindiafoundation.iifllemeddocket.Model.ViewStatusModel.PrescriptionSpectacleCount
import org.impactindiafoundation.iifllemeddocket.R

class PrescriptionGridAdapter(private val context: Context, private val dataList: List<PrescriptionSpectacleCount>) : BaseAdapter() {

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val itemView: View

        if (convertView == null) {
            // Inflate your custom grid item layout
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            itemView = inflater.inflate(R.layout.custom_grid_item_layout, parent, false)
        } else {
            itemView = convertView
        }

        val item = getItem(position) as PrescriptionSpectacleCount
        val textView = itemView.findViewById<TextView>(R.id.TextView_status)
        val textView1 = itemView.findViewById<TextView>(R.id.TextView_count)
        val textView2 = itemView.findViewById<TextView>(R.id.TextView_singleVision)
        val layout=itemView.findViewById<LinearLayout>(R.id.LinearLayout_subType)

        layout.visibility=View.GONE
        when(item.status)
        {
            "Given"->
            {
                when {
                    item.count >= 1 -> {
                        // Perform actions if item.count is more than 1
                        textView.text=item.status
                        textView1.text= item.count.toString()

                        layout.visibility=View.VISIBLE

                        textView2.text="Single Vision : "+item.singleVisionCount+"\n"+
                                "Single Vision (HP) : "+item.singleVisionHPCount+"\n"+
                                "Bifocal : "+item.bifocalCount+"\n"+
                                "Bifocal (HP) : "+item.bifocalHPCount
                    }
                    else -> {
                        textView.text=item.status
                        textView1.text= item.count.toString()
                        layout.visibility=View.GONE
                    }
                }
            }
            else->
            {
                textView.text=item.status
                textView1.text= item.count.toString()
            }
        }
        return itemView
    }
}
