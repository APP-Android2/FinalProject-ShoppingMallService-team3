package kr.co.lion.farming_customer.viewmodel.loginRegister

import androidx.lifecycle.MutableLiveData

class FindPwViewModel {
    // 이름
    val userName = MutableLiveData<String>()
    // 아이디
    val userId = MutableLiveData<String>()
    // 휴대폰 번호
    val userPhoneNumber = MutableLiveData<String>()
}