package org.impactindiafoundation.iifllemeddocket.Utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.RefractiveError

import org.impactindiafoundation.iifllemeddocket.Model.Login.LoginData
import org.impactindiafoundation.iifllemeddocket.Model.Login.LoginResponse

class SessionManager() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var editor: SharedPreferences.Editor
    lateinit var mContext: Context
    private val privateMode = 0


    companion object{
        private val PREF_NAME="LLE_MedDocket"
        val PATIENT_DATA="PATIENT_DATA"
        val KEY_LOGIN_DATA="KEY_LOGIN_DATA"
        val REFRACTIVE_ERROR_DATA="REFRACTIVE_ERROR_DATA"
        val PATIENT_IDENTITY="PATIENT_IDENTITY"
        val CAMP_USER_ID="CAMP_USER_ID"
    }

    constructor(context: Context):this()
    {
        mContext=context
        sharedPreferences=context.getSharedPreferences(PREF_NAME,privateMode)
        editor=sharedPreferences.edit()
    }

    fun clearCache(context: Context)
    {
        editor.clear()
        editor.commit()
        editor.apply()
        context.deleteSharedPreferences(PREF_NAME)
    }

    fun logoutClear(context: Context)
    {
        editor.clear()
        editor.commit()
        editor.apply()
        context.deleteSharedPreferences(PREF_NAME)
    }

    fun saveLoginData(loginData: List<LoginData>) {
        val loginDataJson = Gson().toJson(loginData)
        editor.putString(KEY_LOGIN_DATA, loginDataJson)
        editor.apply()
    }

    fun saveRefractiveError(refractiveError: RefractiveError) {
        val refractiveErrorDataJson = Gson().toJson(refractiveError)
        val editor = sharedPreferences.edit()
        editor.putString(REFRACTIVE_ERROR_DATA, refractiveErrorDataJson)
        editor.apply()
    }
    fun getRefractiveError(): RefractiveError? {
        val refractiveErrorDataJson = sharedPreferences.getString(REFRACTIVE_ERROR_DATA, null)
        if (refractiveErrorDataJson != null) {
            return Gson().fromJson(refractiveErrorDataJson, RefractiveError::class.java)
        }

        return null
    }


    fun getLoginData(): List<LoginData>? {
        val loginDataJson = sharedPreferences.getString(KEY_LOGIN_DATA, null)
        return Gson().fromJson(loginDataJson, Array<LoginData>::class.java)?.toList()
    }

    fun setPatientData(patientData: String)
    {
        editor.putString(PATIENT_DATA,patientData)
        editor.apply()
    }
    fun getPatientData():String? {
        return sharedPreferences.getString(PATIENT_DATA, null)
    }

    fun setPatientIdentity(s: String, aadharNo: String) {
        val identityString = "$s|$aadharNo"
        val editor = sharedPreferences.edit()
        editor.putString(PATIENT_IDENTITY, identityString)
        editor.apply()
    }

    fun setCampUserID(camp_id: String, user_id: String) {
        val identityString = "$camp_id|$user_id"
        val editor = sharedPreferences.edit()
        editor.putString(CAMP_USER_ID, identityString)
        editor.apply()
    }

    fun getCampUserID(): Pair<String?, String?> {
        val identityString = sharedPreferences.getString(CAMP_USER_ID, null)
        val (camp_id, user_id) = identityString?.split("|")?.let {
            if (it.size == 2) {
                Pair(it[0] as String, it[1] as String)
            } else {
                Pair(null, null)
            }
        } ?: Pair(null, null)

        return Pair(camp_id, user_id)
    }
}