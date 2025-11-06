package org.impactindiafoundation.iifllemeddocket.ViewModel

import android.app.Application
import android.app.ProgressDialog
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.Model.CurrentInvetoryItem.getCurrentInvetoryItem
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.getResponseEyeOPDDoctorsNote
import org.impactindiafoundation.iifllemeddocket.Model.EyeOPD_Doctors_NoteModel.sendEyeOPDDoctorsNoteData
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOPNotes.AddEyePreOpNotesRequest
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOPNotes.AddEyePreOpNotesResponse
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel.AddEyePreOpInvestigationsRequest
import org.impactindiafoundation.iifllemeddocket.Model.EyePreOpInvestigationsModel.getEyePreOpInvestigationResponse
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.EyePostAndFollowrequest
import org.impactindiafoundation.iifllemeddocket.Model.Eye_Post_And_Follow_Model.getEyePostAndFollowUpResponse
import org.impactindiafoundation.iifllemeddocket.Model.GetInventoryUnits.GetInventoryUnits
import org.impactindiafoundation.iifllemeddocket.Model.ImageModel.ImageUploadParams
import org.impactindiafoundation.iifllemeddocket.Model.ImageModel.getImageUploadResponse
import org.impactindiafoundation.iifllemeddocket.Model.ImagePrescriptionModel.UploadImagePrescriptionRequest
import org.impactindiafoundation.iifllemeddocket.Model.ImagePrescriptionModel.UploadImagePrescriptionResponse
import org.impactindiafoundation.iifllemeddocket.Model.Login.LoginRequest
import org.impactindiafoundation.iifllemeddocket.Model.Login.LoginResponse
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.AddPrecriptionFinal
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.PrescriptionDetailsModel
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.PrescriptionGlassStatusDetails
import org.impactindiafoundation.iifllemeddocket.Model.PrescriptionModel.addPrecriptionGlassesResponse
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.AddRefractiveErrorRequest
import org.impactindiafoundation.iifllemeddocket.Model.RefractiveModel.getRefractiveErrorResponse
import org.impactindiafoundation.iifllemeddocket.Model.RegistrationModel.RegistrationDetailsModel
import org.impactindiafoundation.iifllemeddocket.Model.SurgicalNotesModel.SurgicalNotesRequest
import org.impactindiafoundation.iifllemeddocket.Model.SurgicalNotesModel.getSurgicalNotesResponse
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataModel
import org.impactindiafoundation.iifllemeddocket.Model.SynedDataModel.SynedDataResponse
import org.impactindiafoundation.iifllemeddocket.R
import org.impactindiafoundation.iifllemeddocket.Utils.ConstantsApp
import retrofit2.Response
import java.lang.Exception

class LLE_MedDocketViewModel(val LLE_MedDocketRespository: LLE_MedDocketRespository, val app:Application):ViewModel() {

    val userLoginLive: MutableLiveData<ResourceApp<LoginResponse>> = MutableLiveData()

    val getEyeOPDDoctorsNoteData:MutableLiveData<ResourceApp<getResponseEyeOPDDoctorsNote>> = MutableLiveData()

    val getEyePreOpInvestigationData:MutableLiveData<ResourceApp<getEyePreOpInvestigationResponse>> = MutableLiveData()

    val getEyePreOpNotesResponse:MutableLiveData<ResourceApp<AddEyePreOpNotesResponse>> = MutableLiveData()

    val getSurgicalNotesResponse:MutableLiveData<ResourceApp<getSurgicalNotesResponse>> = MutableLiveData()

    val getEyePostAndFollowUpResponse:MutableLiveData<ResourceApp<getEyePostAndFollowUpResponse>> = MutableLiveData()

    val getRefractiveErrorResponse:MutableLiveData<ResourceApp<getRefractiveErrorResponse>> = MutableLiveData()

    val getImageUploadResponse:MutableLiveData<ResourceApp<getImageUploadResponse>> = MutableLiveData()

    val getPrescriptiondetailsResponse:MutableLiveData<ResourceApp<PrescriptionDetailsModel>> = MutableLiveData()

    val getRegistrationDetailsResponse:MutableLiveData<ResourceApp<RegistrationDetailsModel>> = MutableLiveData()

    val getPriscriptionGlassStatusDetailsResponse:MutableLiveData<ResourceApp<PrescriptionGlassStatusDetails>> = MutableLiveData()

    val addPrecriptionGlassesResponse:MutableLiveData<ResourceApp<addPrecriptionGlassesResponse>> = MutableLiveData()

    val uploadSynedDataResponse:MutableLiveData<ResourceApp<SynedDataResponse>> = MutableLiveData()

    val getCurrentInventoryItems:MutableLiveData<ResourceApp<getCurrentInvetoryItem>> = MutableLiveData()

    val getInventoryUnits:MutableLiveData<ResourceApp<GetInventoryUnits>> = MutableLiveData()

      val getImagePrescriptionUploadResponse:MutableLiveData<ResourceApp<UploadImagePrescriptionResponse>> = MutableLiveData()

    fun login(loginRequest: LoginRequest, progressDialog: ProgressDialog)= viewModelScope.launch{
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                val requestBody = "{\"UserName\":\"${loginRequest.UserName}\",\"Password\":\"${loginRequest.Password}\"}"
                userLoginLive.postValue(ResourceApp.Loading())
                val login=LLE_MedDocketRespository.userLogin(requestBody)
                userLoginLive.postValue(handleUserLogin(login)!!)
            } else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleUserLogin(response: Response<LoginResponse>): ResourceApp<LoginResponse>? {
        if (response.isSuccessful) {
            response.body()?.let {
                    resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun insertEyeOPDDoctorNote(progressDialog: ProgressDialog, data: sendEyeOPDDoctorsNoteData)=viewModelScope.launch {
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getEyeOPDDoctorsNoteData.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.insertEyeOPDDoctorsNote(data)
                getEyeOPDDoctorsNoteData.postValue(handleInsertEyeOPDDoctorsData(data)!!)
            } else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleInsertEyeOPDDoctorsData(response: Response<getResponseEyeOPDDoctorsNote>): ResourceApp<getResponseEyeOPDDoctorsNote>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun insertEyePreOPInvestigation(progressDialog: ProgressDialog, addEyePreOpInvestigationsRequest: AddEyePreOpInvestigationsRequest) =viewModelScope.launch{
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getEyePreOpInvestigationData.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.insertEyePreOpInvestigation(addEyePreOpInvestigationsRequest)
                getEyePreOpInvestigationData.postValue(handleInsertEyePreOpInvestigation(data)!!)
            }
            else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleInsertEyePreOpInvestigation(response: Response<getEyePreOpInvestigationResponse>): ResourceApp<getEyePreOpInvestigationResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun insertEyePreOpNotes(data: AddEyePreOpNotesRequest, progressDialog: ProgressDialog)=viewModelScope.launch {
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getEyePreOpNotesResponse.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.insertEyePreOpNotes(data)
                getEyePreOpNotesResponse.postValue(handleInsertEyePreOpNotes(data)!!)
            }
            else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleInsertEyePreOpNotes(response: Response<AddEyePreOpNotesResponse>): ResourceApp<AddEyePreOpNotesResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun insertSurgicalData(progressDialog: ProgressDialog, data: SurgicalNotesRequest)=viewModelScope.launch {
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getSurgicalNotesResponse.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.insertSurgicalNotes(data)
                getSurgicalNotesResponse.postValue(handleInsertSurgicalNotes(data)!!)
            }
            else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleInsertSurgicalNotes(response: Response<getSurgicalNotesResponse>): ResourceApp<getSurgicalNotesResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun insertEyePostOpAndFollowUps(progressDialog: ProgressDialog, data: EyePostAndFollowrequest)=viewModelScope.launch {
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getEyePostAndFollowUpResponse.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.insertEyePostOpAndFollowUps(data)
                getEyePostAndFollowUpResponse.postValue(handleInsertEyePostOpAndFollowUps(data)!!)
            }
            else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleInsertEyePostOpAndFollowUps(response: Response<getEyePostAndFollowUpResponse>): ResourceApp<getEyePostAndFollowUpResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun uploadFile1(
        progressDialog: ProgressDialog,
        imageUploadParams: ImageUploadParams
    ) = viewModelScope.launch {
        Log.d("UpdateImage", "📡 uploadFile1() called")
        Log.d("UpdateImage", "➡️ Parameters: " +
                "\n   - File: ${imageUploadParams.filePart.body.contentType()}" +
                "\n   - Type: ${imageUploadParams.imageTypeRequestBody}" +
                "\n   - Patient ID: ${imageUploadParams.patientIdRequestBody}" +
                "\n   - Camp ID: ${imageUploadParams.campIdRequestBody}" +
                "\n   - User ID: ${imageUploadParams.userIdRequestBody}" +
                "\n   - Local DB ID: ${imageUploadParams.idRequestBody}")

        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                Log.d("UpdateImage", "🌐 Internet connection available, starting upload")

                getImageUploadResponse.postValue(ResourceApp.Loading())

                // Make API call
                Log.d("UpdateImage", "🚀 Calling repository.imageUpload1()...")
                val data = LLE_MedDocketRespository.imageUpload1(imageUploadParams)
                Log.d("UpdateImage", "📬 Raw API response received: $data")

                // Handle the response
                val handledResponse = handleImageUpload(data)
                Log.d("UpdateImage", "🧩 handleImageUpload() returned: $handledResponse")

                if (handledResponse != null) {
                    Log.d("UpdateImage", "✅ Posting handled response to LiveData")
                    getImageUploadResponse.postValue(handledResponse)
                } else {
                    Log.e("UpdateImage", "⚠️ handleImageUpload() returned null — response not posted")
                }
            } else {
                Log.w("UpdateImage", "❌ No internet connection detected")
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        } catch (e: Exception) {
            Log.e("UpdateImage", "❌ Exception during uploadFile1(): ${e.message}", e)
            progressDialog.dismiss()
        } finally {
            Log.d("UpdateImage", "🧹 uploadFile1() finished (finally block reached)")
        }
    }


    private fun handleImageUpload(response: Response<getImageUploadResponse>): ResourceApp<getImageUploadResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }


    fun uploadFileImagePrescription(progressDialog: ProgressDialog, imageUploadParams: UploadImagePrescriptionRequest)=viewModelScope.launch {
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getImagePrescriptionUploadResponse.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.imagePrescriptionUpload(imageUploadParams)
                getImagePrescriptionUploadResponse.postValue(handleImagePrescriptionUpload(data)!!)
            }
            else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleImagePrescriptionUpload(response: Response<UploadImagePrescriptionResponse>): ResourceApp<UploadImagePrescriptionResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun getPriscriptionDetails(progressDialog: ProgressDialog) =viewModelScope.launch{
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getPrescriptiondetailsResponse.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.getPriscriptionDetails()
                Log.d("pawan", "isSuccessful PriscriptionDetails => ${data.isSuccessful}")
                Log.d("pawan", "response code PriscriptionDetails => ${data.code()}")
                Log.d("pawan", "response message PriscriptionDetails => ${data.message()}")
                Log.d("pawan", "response body PriscriptionDetails => ${data.body()}")
                Log.d("pawan", "errorBody PriscriptionDetails => ${data.errorBody()?.string()}")
                getPrescriptiondetailsResponse.postValue(handleGetPriscriptionDetails(data)!!)
            }
            else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleGetPriscriptionDetails(response: Response<PrescriptionDetailsModel>): ResourceApp<PrescriptionDetailsModel>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun getRegistrationDetails(progressDialog: ProgressDialog) =viewModelScope.launch{
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getRegistrationDetailsResponse.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.getRegistrationDetails()
                Log.d("pawan", "isSuccessful => ${data.isSuccessful}")
                Log.d("pawan", "response code => ${data.code()}")
                Log.d("pawan", "response message => ${data.message()}")
                Log.d("pawan", "response body => ${data.body()}")
                Log.d("pawan", "errorBody => ${data.errorBody()?.string()}")
                getRegistrationDetailsResponse.postValue(handleGetRegistrationDetails(data)!!)
            } else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleGetRegistrationDetails(response: Response<RegistrationDetailsModel>): ResourceApp<RegistrationDetailsModel>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun getPriscriptionStatusDetails(progressDialog: ProgressDialog)=viewModelScope.launch {
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getPriscriptionGlassStatusDetailsResponse.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.getPriscriptionGlassStatusDetails()
                getPriscriptionGlassStatusDetailsResponse.postValue(handleGetPriscriptionGlassStatusDetails(data)!!)
            }
            else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleGetPriscriptionGlassStatusDetails(response: Response<PrescriptionGlassStatusDetails>): ResourceApp<PrescriptionGlassStatusDetails>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun addPrecriptionGlassesResponse(progressDialog: ProgressDialog, AddPrecriptionFinal: AddPrecriptionFinal, )=viewModelScope.launch {
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                addPrecriptionGlassesResponse.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.insertAddPrecriptionGlasses(AddPrecriptionFinal)
                addPrecriptionGlassesResponse.postValue(handleAddPrecriptionGlasses(data)!!)
            } else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleAddPrecriptionGlasses(response: Response<addPrecriptionGlassesResponse>): ResourceApp<addPrecriptionGlassesResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun insertSynedData(progressDialog: ProgressDialog, data: SynedDataModel)=viewModelScope.launch {
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                uploadSynedDataResponse.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.insertSynedData(data)
                uploadSynedDataResponse.postValue(handleInsertSynedData(data)!!)
            } else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleInsertSynedData(response: Response<SynedDataResponse>): ResourceApp<SynedDataResponse>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun GetCurrentInventoryItems(progressDialog: ProgressDialog) =viewModelScope.launch{
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getCurrentInventoryItems.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.getCurrentInventoryItems()
                getCurrentInventoryItems.postValue(handleGetCurrentInventoryItems(data)!!)
            }
            else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleGetCurrentInventoryItems(response: Response<getCurrentInvetoryItem>): ResourceApp<getCurrentInvetoryItem>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }

    fun GetInventoryUnits(progressDialog: ProgressDialog) =viewModelScope.launch{
        try {
            if (ConstantsApp.checkInternetConenction(app)) {
                getInventoryUnits.postValue(ResourceApp.Loading())
                val data=LLE_MedDocketRespository.getInventoryUnits()
                getInventoryUnits.postValue(handleGetInventoryUnits(data)!!)
            } else{
                Toast.makeText(app, R.string.no_internet_connection, Toast.LENGTH_SHORT).show()
                progressDialog.dismiss()
            }
        }
        catch (e: Exception) {
            e.printStackTrace()
            progressDialog.dismiss()
        }
    }

    private fun handleGetInventoryUnits(response: Response<GetInventoryUnits>): ResourceApp<GetInventoryUnits>? {
        if (response.isSuccessful) {
            response.body()?.let { resultSuccess->
                return ResourceApp.Success(resultSuccess)
            }
        }
        return ResourceApp.Error(response.message())
    }
}