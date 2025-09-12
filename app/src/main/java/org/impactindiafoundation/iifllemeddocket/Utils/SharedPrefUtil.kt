package org.impactindiafoundation.iifllemeddocket.Utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel


object SharedPrefUtil {
    const val USER_CAMP = "USER_CAMP"
    private const val PREFS_NAME = "prescription_prefs"
    private const val KEY_LIST = "prescription_list"
    public const val LAST_SEEN_VERSION = "last_seen_version"

    fun savePrescriptions(context: Context, list: ArrayList<CreatePrescriptionModel>) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val gson = Gson()
        val json = gson.toJson(list)
        editor.putString(KEY_LIST, json)
        editor.apply()
    }

    fun loadPrescriptions(context: Context): ArrayList<CreatePrescriptionModel> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val gson = Gson()
        val json = prefs.getString(KEY_LIST, null)
        val type = object : TypeToken<ArrayList<CreatePrescriptionModel>>() {}.type
        return if (json != null) gson.fromJson(json, type) else arrayListOf()
    }

    fun clearPrescriptions(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().clear().apply()
    }
    fun savePrefString(context: Context, key: String, value: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
        editor.commit()
    }

    fun getPrfString(context: Context, key: String): String {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val value = sharedPreferences.getString(key, "")
        return value ?: ""
    }

    fun savePrefInt(context: Context, key: String, value: Int) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
        editor.commit()
    }

    fun getPrfInt(context: Context, key: String): Int {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("USER", Context.MODE_PRIVATE)
        val value = sharedPreferences.getInt(key, 0)
        return value
    }
}