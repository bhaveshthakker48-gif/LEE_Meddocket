package org.impactindiafoundation.iifllemeddocket.architecture.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.OpdSyncTable
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject

@HiltViewModel
class PharmaMainViewModel @Inject constructor(private val newMainRepository: NewMainRepository): ViewModel() {

    private var _opdSyncList = MutableLiveData<Resource<List<OpdSyncTable>>>()
    val opdSyncList: LiveData<Resource<List<OpdSyncTable>>> get() = _opdSyncList

    fun getOpdSyncTable() = CoroutineScope(Dispatchers.IO).launch {
        _opdSyncList.postValue(Resource.loading(null))
        try {
            try {
                newMainRepository.getOpdSyncTable().let {
                    _opdSyncList.postValue(Resource.success(it))
                }
            } catch (e: Exception) {
                _opdSyncList.postValue(
                    Resource.error(
                        e.toString(),
                        null
                    )
                )
            }
        } catch (e: Exception) {
            _opdSyncList.postValue(Resource.error(e.message.toString(), null))
        }
    }
}