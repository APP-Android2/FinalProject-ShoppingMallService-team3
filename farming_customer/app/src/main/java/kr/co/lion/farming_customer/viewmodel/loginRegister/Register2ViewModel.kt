package kr.co.lion.farming_customer.viewmodel.loginRegister

import androidx.lifecycle.MutableLiveData
import kr.co.lion.farming_customer.Gender
import kr.co.lion.farming_customer.R

class Register2ViewModel {
    // 닉네임
    val userNickname = MutableLiveData<String>()
    // 아이디
    val userId = MutableLiveData<String>()
    // 비밀번호
    val userPw = MutableLiveData<String>()
    // 비밀번호 확인
    val userPwConfirm = MutableLiveData<String>()
    // 이름
    val userName = MutableLiveData<String>()
    // 생년월일
    val userBirthDate = MutableLiveData<String>()
    // 성별
    val userGender = MutableLiveData<Gender>()
    // 휴대폰
    val userPhoneNumber = MutableLiveData<String>()
    // 주소
    val userAddress = MutableLiveData<String>()
    // 상세 주소
    val userAddressDetail = MutableLiveData<String>()

    // 성별을 세팅하는 메서드
    fun settingGender(buttonId: Int){
        // 성별로 분기한다.
        when(buttonId){
            R.id.buttonReg2GenderMale -> {
                userGender.value = Gender.MALE
            }
            R.id.buttonReg2GenderFemale -> {
                userGender.value = Gender.FEMALE
            }
        }
    }
}