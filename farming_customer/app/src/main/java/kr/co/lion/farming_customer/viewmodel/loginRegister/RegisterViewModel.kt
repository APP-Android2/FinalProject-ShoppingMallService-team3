package kr.co.lion.farming_customer.viewmodel.loginRegister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {
    // 서비스 이용 약관 상태 관리
    val isServiceTermChecked = MutableLiveData<Boolean>()
    // 개인정보 수집 및 이용 안내 상태 관리
    val isPersonalInfoTermChecked = MutableLiveData<Boolean>()
    // 알림 서비스 수락
    val isAlertServiceTermChecked = MutableLiveData<Boolean>()

    init {
        // 초기값 설정
        isServiceTermChecked.value = false
        isPersonalInfoTermChecked.value = false
        isAlertServiceTermChecked.value = false
    }

}