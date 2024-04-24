package kr.co.lion.farming_customer.viewmodel.loginRegister

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    // 아이디
    val textFieldLoginId = MutableLiveData<String>()
    // 비밀번호
    val textFieldLoginPw = MutableLiveData<String>()
}