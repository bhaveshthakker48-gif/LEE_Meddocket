package org.impactindiafoundation.iifllemeddocket.Utils.imageUtils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.colourmoon.imagepicker.CMImagePicker
import com.colourmoon.imagepicker.utils.ResultImage
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.impactindiafoundation.iifllemeddocket.databinding.ImagePickerSheetBinding

class ImagePickerDialog(
    private val context: Context,
    private val launcher: ResultImage,
    private val title: String? = null,
    private val isPrescription:Boolean?=null
) : BottomSheetDialogFragment(), View.OnClickListener {


    private lateinit var binding: ImagePickerSheetBinding
    private val imagePicker: CMImagePicker by lazy {
        CMImagePicker(requireActivity(), launcher)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ImagePickerSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bottomSheet = view.parent as View
        bottomSheet.backgroundTintMode = PorterDuff.Mode.CLEAR
        bottomSheet.backgroundTintList = ColorStateList.valueOf(Color.TRANSPARENT)
        bottomSheet.setBackgroundColor(Color.TRANSPARENT)
        isCancelable = false
        if (isPrescription!=null){
            if (isPrescription){
                binding.lilGallery.visibility = View.VISIBLE
            }
            else{
                binding.lilGallery.visibility = View.GONE
            }
        }
        else{
            binding.lilGallery.visibility = View.GONE
        }
        binding.lilCamera.setOnClickListener(this)
        binding.lilGallery.setOnClickListener(this)
        binding.btnSubmit.setOnClickListener(this)
        binding.tvDialogTitle.text = title ?: "Upload Picture"
    }

    private fun openGallery() {
        imagePicker
            .allowCrop(false)
            .allowCompress(true, 80)
            .allowGalleryOnly(true)
            .allowCameraOnly(false)
            .start()
        dismiss()
    }

    private fun openCamera() {
        try {
            // Launch camera manually
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            launcher.result.launch(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        dismiss()
    }



    override fun onClick(p0: View?) {
        when (p0?.id) {
            binding.lilCamera.id -> openCamera()
            binding.lilGallery.id -> openGallery()
            binding.btnSubmit.id -> dismiss()
        }

    }

}