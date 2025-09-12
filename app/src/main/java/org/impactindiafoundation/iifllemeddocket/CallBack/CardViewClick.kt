package org.impactindiafoundation.iifllemeddocket.CallBack

import android.view.View
import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.PatientDataLocal

interface CardViewClick {

    fun onClick(data:PatientDataLocal,position:Int, view: View)
}