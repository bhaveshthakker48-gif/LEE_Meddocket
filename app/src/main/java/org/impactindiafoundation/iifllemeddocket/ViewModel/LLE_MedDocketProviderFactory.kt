package org.impactindiafoundation.iifllemeddocket.ViewModel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LLE_MedDocketProviderFactory(val LLE_MedDocketRespository: LLE_MedDocketRespository, val application: Application):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LLE_MedDocketViewModel(LLE_MedDocketRespository,application) as T
    }
}