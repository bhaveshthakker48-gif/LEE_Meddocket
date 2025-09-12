package org.impactindiafoundation.iifllemeddocket.architecture.viewModel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.logging.Logger
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.helper.FileUploader
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.FileUploadResponse
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FileUploadViewModel @Inject constructor() : ViewModel() {

    val uploadFileResponse = MutableLiveData<Resource<FileUploadResponse>>()

//    fun uploadFile(
//        context: Context,
//        uriMap: Map<String, Uri>,
//        fileMap: Map<String, File>,
//        dataMap: Map<String, String>,
//        url:String
//    ) = CoroutineScope(Dispatchers.IO).launch {
//        uploadFileResponse.postValue(Resource.loading(null))
//        FileUploader.uploadFiles(context,
//            url,
//            uriMap,
//            fileMap,
//            dataMap,
//            object : FileUploader.UploadCallback {
//                override fun onFileUploadError(errorMessage: String?) {
//                    uploadFileResponse.postValue(Resource.error(errorMessage!!, null))
//                }
//
//                override fun onAllFilesUploaded(string: String) {
//                    Log.d("File upload", string)
//                    if (string != null) {
//                        val gson = Gson()
//                        val data: FileUploadResponse? =
//                            gson.fromJson(string, FileUploadResponse::class.java)
//                        if (data != null) {
//                            if (!data.error)
//                                uploadFileResponse.postValue(Resource.success(data))
//                            else
//                                uploadFileResponse.postValue(Resource.error(data.message, null))
//                        }
//                    }else{
//                        uploadFileResponse.postValue(Resource.error("Server Error", null))
//                    }
//                }
//            })
//    }
}