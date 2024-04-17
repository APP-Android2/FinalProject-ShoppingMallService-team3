package kr.co.lion.farming_customer.viewmodel.loginRegister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    val isServiceTermChecked = MutableLiveData<Boolean>(false)
    val isPersonalInfoTermChecked = MutableLiveData<Boolean>(false)
    val isAlertServiceTermChecked = MutableLiveData<Boolean>(false)

}