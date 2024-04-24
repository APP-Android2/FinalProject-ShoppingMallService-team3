package kr.co.lion.farming_customer.viewmodel.loginRegister

import androidx.lifecycle.MutableLiveData

class FindPwDoneViewModel {
    // 비밀번호
    val userPw = MutableLiveData<String>()
    // 비밀번호 확인
    val userPwConfirm = MutableLiveData<String>()
}