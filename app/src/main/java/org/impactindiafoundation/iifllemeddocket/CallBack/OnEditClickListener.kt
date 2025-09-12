package org.impactindiafoundation.iifllemeddocket.CallBack

import org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE.ROOM_DATABASE_MODEL.CreatePrescriptionModel

interface OnEditClickListener {

    fun onEditClick(data: CreatePrescriptionModel, position: Int)
}