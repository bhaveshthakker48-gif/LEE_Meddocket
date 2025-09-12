package org.impactindiafoundation.iifllemeddocket.architecture.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.impactindiafoundation.iifllemeddocket.architecture.helper.Resource
import org.impactindiafoundation.iifllemeddocket.architecture.model.UserModel
import org.impactindiafoundation.iifllemeddocket.architecture.repository.NewMainRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val newMainRepository: NewMainRepository):ViewModel() {
    private var _insertUserResponse = MutableLiveData<Resource<Long>>()
    val insertUserResponse: LiveData<Resource<Long>> get() = _insertUserResponse

    private var _userList = MutableLiveData<Resource<List<UserModel>>>()
    val userList: LiveData<Resource<List<UserModel>>> get() = _userList


    fun insertUserList(userList:UserModel) {
        CoroutineScope(Dispatchers.IO).launch {
            val message = newMainRepository.insertAllUser(userList)
            _insertUserResponse.postValue(Resource.success(message))

            if (message.equals(null)) {
                _insertUserResponse.postValue(Resource.error("Local Db Error",null))
            } else {
                _insertUserResponse.postValue(Resource.success(message))
            }
        }
    }
}