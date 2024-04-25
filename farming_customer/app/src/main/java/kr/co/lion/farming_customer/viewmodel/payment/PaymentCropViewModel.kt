package kr.co.lion.farming_customer.viewmodel.payment

import android.widget.RadioGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.checkbox.MaterialCheckBox
import kr.co.lion.farming_customer.MaterialButtonToggleGroupWithRadius

class PaymentCropViewModel:ViewModel() {
    // 필수동의들
    val checkBoxPaymentCropPrivacy1 = MutableLiveData<Boolean>()
    val checkBoxPaymentCropPrivacy2 = MutableLiveData<Boolean>()

    // 필수 전체 동의
    val checkBoxPaymentCropAllAgree = MutableLiveData<Boolean>()
    val checkBoxPaymentCropAllAgreeState = MutableLiveData<Int>()

    // 결제버튼
    val buttonPaymentCropPayDone = MutableLiveData<Boolean>()

    // 상품 이름
    val textViewPaymentProductName = MutableLiveData<String>()

    // 옵션명
    val textViewPaymentProductOption = MutableLiveData<String>()

    // 옵션 개수
    val textViewPaymentProductCnt = MutableLiveData<String>()

    // 총 가격
    val textViewPaymentProductPrice = MutableLiveData<String>()

    // 모든 상품의 총 가격을 더한 가격
    val textViewPaymentCropProductPrice2 = MutableLiveData<String>()

    // 사용 가능한 포인트
    val textViewPaymentCropAvailablePoint = MutableLiveData<String>()

    // 사용할 포인트
    val textFieldPaymentCropUsePoint = MutableLiveData<String>()

    // 총 할인 받은 포인트
    val textViewPaymentCropUsePoint2 = MutableLiveData<String>()

    // 총 결제 금액
    val textViewPaymentCropTotalPayPrice2 = MutableLiveData<String>()

    // 수령인
    val textViewPaymentReceiver2 = MutableLiveData<String>()

    // 수령 주소
    val textViewPaymentReceiverAddress = MutableLiveData<String>()

    // 수령인 연락처
    val textViewPaymentReceiverPhoneNumber = MutableLiveData<String>()

    // 배달 요청사항
    val textViewPaymentReceiverRequest = MutableLiveData<String>()
    // 결제 수단
    val radioGroupPaymentType = MutableLiveData<Int>()

    fun setCheckAll(checked:Boolean){
        checkBoxPaymentCropPrivacy1.value = checked
        checkBoxPaymentCropPrivacy2.value = checked
        buttonPaymentCropPayDone.value = checked
    }

    fun onCheckBoxAllChanged(){
        setCheckAll(checkBoxPaymentCropAllAgree.value!!)
    }

    // 각 체크박스를 누르면...
    fun onCheckBoxChanged(){
        // 체크되어 있는 체크박스 개수를 담을 변수
        var checkedCnt = 0

        // 체크되어 있다면 체크되어 있는 체크박스의 개수를 1 증가시킨다.
        if(checkBoxPaymentCropPrivacy1.value == true){
            checkedCnt++
        }
        if(checkBoxPaymentCropPrivacy2.value == true){
            checkedCnt++
        }

        // 체크되어 있는 것이 없다면
        if(checkedCnt==0){
            checkBoxPaymentCropAllAgree.value = false
            checkBoxPaymentCropAllAgreeState.value = MaterialCheckBox.STATE_UNCHECKED
            buttonPaymentCropPayDone.value = false
        }
        // 모두 체크되어 있다면..
        else if(checkedCnt==2){
            checkBoxPaymentCropAllAgree.value = true
            checkBoxPaymentCropAllAgreeState.value = MaterialCheckBox.STATE_CHECKED
            buttonPaymentCropPayDone.value = true

        }
        // 일부만 체크되어 있다면..
        else{
            checkBoxPaymentCropAllAgreeState.value = MaterialCheckBox.STATE_UNCHECKED
            buttonPaymentCropPayDone.value = false
        }

    }
}