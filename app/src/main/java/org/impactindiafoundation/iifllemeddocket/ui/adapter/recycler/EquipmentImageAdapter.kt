package org.impactindiafoundation.iifllemeddocket.ui.adapter.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.util.Util
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.Utility
import org.impactindiafoundation.iifllemeddocket.architecture.model.EquipmentImage
import org.impactindiafoundation.iifllemeddocket.architecture.model.OrthosisImages
import org.impactindiafoundation.iifllemeddocket.databinding.ItemFilesListBinding


class EquipmentImageAdapter(
    val context: Context,
    val data: List<EquipmentImage>,
    val event: EquipmnetImageAdapterEvent
) :
    RecyclerView.Adapter<EquipmentImageAdapter.EquipmentImageViewHolder>() {

    inner class EquipmentImageViewHolder(
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

                    event.onImageRemove(position)

            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EquipmentImageViewHolder {
        val binding =
            ItemFilesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EquipmentImageViewHolder(binding, context)
    }

    override fun onBindViewHolder(
        holder: EquipmentImageViewHolder,
        position: Int
    ) {
        holder.bind(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface EquipmnetImageAdapterEvent {
        fun onImageClick(position: Int)
        fun onImageRemove(position: Int)
    }

}