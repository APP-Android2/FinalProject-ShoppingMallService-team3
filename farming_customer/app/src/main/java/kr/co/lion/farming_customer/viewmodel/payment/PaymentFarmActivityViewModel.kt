package kr.co.lion.farming_customer.viewmodel.payment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.checkbox.MaterialCheckBox

class PaymentFarmActivityViewModel: ViewModel() {

    // 필수동의들
    val checkBoxPaymentFarmActivityPrivacy1 = MutableLiveData<Boolean>()
    val checkBoxPaymentFarmActivityPrivacy2 = MutableLiveData<Boolean>()

    // 필수 전체 동의
    val checkBoxPaymentFarmActivityAllAgree = MutableLiveData<Boolean>()
    val checkBoxPaymentFarmActivityAllAgreeState = MutableLiveData<Int>()

    // 결제버튼
    val buttonPaymentFarmActivityPayDone = MutableLiveData<Boolean>()

    // 모든 상품의 총 가격을 더한 가격
    val textViewPaymentFarmActivityProductPrice2 = MutableLiveData<String>()

    // 사용 가능한 포인트
    val textViewPaymentFarmActivityAvailablePoint = MutableLiveData<String>()

    // 사용할 포인트
    val textFieldPaymentFarmActivityUsePoint = MutableLiveData<String>()

    // 총 할인 받은 포인트
    val textViewPaymentFarmActivityUsePoint2 = MutableLiveData<String>()

    // 총 결제 금액
    val textViewPaymentFarmActivityTotalPayPrice2 = MutableLiveData<String>()

    // 예약인
    val textFieldReservationName = MutableLiveData<String>()

    // 수령인 연락처
    val textFieldReservationPhoneNumber = MutableLiveData<String>()



    fun setCheckAll(checked:Boolean){
        checkBoxPaymentFarmActivityPrivacy1.value = checked
        checkBoxPaymentFarmActivityPrivacy2.value = checked
        buttonPaymentFarmActivityPayDone.value = checked
    }


    fun onCheckBoxAllChanged(){
        setCheckAll(checkBoxPaymentFarmActivityAllAgree.value!!)
    }

    // 각 체크박스를 누르면...
    fun onCheckBoxChanged(){
        // 체크되어 있는 체크박스 개수를 담을 변수
        var checkedCnt = 0

        // 체크되어 있다면 체크되어 있는 체크박스의 개수를 1 증가시킨다.
        if(checkBoxPaymentFarmActivityPrivacy1.value == true){
            checkedCnt++
        }
        if(checkBoxPaymentFarmActivityPrivacy2.value == true){
            checkedCnt++
        }

        // 체크되어 있는 것이 없다면
        if(checkedCnt==0){
            checkBoxPaymentFarmActivityAllAgree.value = false
            checkBoxPaymentFarmActivityAllAgreeState.value = MaterialCheckBox.STATE_UNCHECKED
            buttonPaymentFarmActivityPayDone.value = false
        }
        // 모두 체크되어 있다면..
        else if(checkedCnt==2){
            checkBoxPaymentFarmActivityAllAgree.value = true
            checkBoxPaymentFarmActivityAllAgreeState.value = MaterialCheckBox.STATE_CHECKED
            buttonPaymentFarmActivityPayDone.value = true
        }
        // 일부만 체크되어 있다면..
        else{
            checkBoxPaymentFarmActivityAllAgreeState.value = MaterialCheckBox.STATE_UNCHECKED
            buttonPaymentFarmActivityPayDone.value = false
        }

    }
}