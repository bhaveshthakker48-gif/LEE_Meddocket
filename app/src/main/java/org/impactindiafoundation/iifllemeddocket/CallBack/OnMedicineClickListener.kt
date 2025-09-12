package org.impactindiafoundation.iifllemeddocket.CallBack

import android.view.View
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.Medicine_Report_Model


interface OnMedicineClickListener {

    fun onClick(data: Medicine_Report_Model, position:Int, view: View,text:String)
}