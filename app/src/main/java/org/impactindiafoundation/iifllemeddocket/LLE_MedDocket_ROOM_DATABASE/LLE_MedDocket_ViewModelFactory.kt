package org.impactindiafoundation.iifllemeddocket.LLE_MedDocket_ROOM_DATABASE

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LLE_MedDocket_ViewModelFactory(val repository: LLE_MedDocket_Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LLE_MedDocket_ViewModel::class.java)) {
            return LLE_MedDocket_ViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
