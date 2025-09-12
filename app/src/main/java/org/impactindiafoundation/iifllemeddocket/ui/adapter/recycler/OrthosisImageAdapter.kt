package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.architecture.model.CampPatientDataItem
import org.impactindiafoundation.iifllemeddocket.architecture.model.FormImages
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.databinding.ItemCampPatientListBinding
import org.impactindiafoundation.iifllemeddocket.databinding.ItemFilesListBinding

class OrthosisImageAdapter(
    val context: Context,
    val data: List<OrthosisImages>,
    val event: OrthosisImageAdapterEvent
) :
    RecyclerView.Adapter<OrthosisImageAdapter.OrthosisImageViewHolder>() {

    inner class OrthosisImageViewHolder(
        private var binding: ItemFilesListBinding,
        var context: Context
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            val content = data[position]
            binding.apply {
                Glide.with(context)
                    .load(content.images).placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.img_placeholder)
                    .into(binding.ivFile)
            }

            binding.ivRemoveImage.setOnClickListener {
                event.onImageRemove(position,content.images)
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrthosisImageViewHolder {
        val binding =
            ItemFilesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrthosisImageViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: OrthosisImageViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface OrthosisImageAdapterEvent {
        fun onImageClick(position: Int)
        fun onImageRemove(position: Int,image:String)
    }

}